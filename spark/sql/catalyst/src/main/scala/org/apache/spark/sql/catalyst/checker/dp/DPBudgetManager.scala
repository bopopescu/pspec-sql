package org.apache.spark.sql.catalyst.checker.dp

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.HashMap
import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.checker.PrivacyException
import org.apache.spark.sql.catalyst.plans.logical.Aggregate
import org.apache.spark.sql.catalyst.plans.logical.Aggregate
import edu.thu.ss.spec.lang.pojo.DataCategory
import edu.thu.ss.spec.lang.pojo.PrivacyParams
import edu.thu.ss.spec.lang.pojo.UserCategory
import edu.thu.ss.spec.lang.pojo.GlobalBudget
import edu.thu.ss.spec.lang.pojo.FineBudget
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.expressions.Cast
import org.apache.spark.sql.catalyst.expressions.Alias
import org.apache.spark.sql.catalyst.expressions.AggregateExpression
import scala.collection.mutable.HashSet
import org.apache.spark.sql.catalyst.checker.util.TypeUtil._
import org.apache.spark.sql.catalyst.expressions.Attribute

object DPBudgetManager {
  def apply(param: PrivacyParams, user: UserCategory): DPBudgetManager = {
    val budget = param.getPrivacyBudget;

    budget match {
      case global: GlobalBudget => {
        val budget = global.getBudget(user);
        if (budget == null) {
          return new GlobalBudgetManager(0);
        } else {
          return new GlobalBudgetManager(budget);
        }

      }
      case fine: FineBudget => {
        val map = fine.getBudget(user);
        val converted = new HashMap[DataCategory, Double];
        map.asScala.foreach(t => converted += ((t._1, t._2)));
        return new FineBudgetManager(converted);
      }
    }

  }

}

trait DPBudgetManager {

  def commit: Unit;

  def rollback: Unit;

  def specified(data: DataCategory): Boolean;

  def consume(plan: Aggregate, epsilon: Double): Unit;

  def consume(data: DataCategory, epsilon: Double): Unit;

  def budget(data: DataCategory): Double;

  def copy(): DPBudgetManager;
}

class GlobalBudgetManager(var budget: Double) extends DPBudgetManager with Logging {
  var tmpBudget: Double = budget;

  def commit() {
    logWarning(s"commit global privacy budget, buget left: $tmpBudget");
    budget = tmpBudget;
  }

  def rollback() {
    logWarning(s"rollback global privacy budget to $budget");
    tmpBudget = budget;
  }

  def specified(data: DataCategory) = true;

  def consume(data: DataCategory, epsilon: Double) {
    if (tmpBudget - epsilon < 0) {
      rollback;
      throw new PrivacyException(s"No enough global privacy budget for the query. Privacy budget left: $budget");
    }
    tmpBudget -= epsilon;
  }

  def consume(plan: Aggregate, epsilon: Double) {
    //TODO add budget management for global budget
    plan.aggregateExpressions.foreach(consume(_, epsilon));
  }

  private def consume(expr: Expression, epsilon: Double) {
    expr match {
      case cast: Cast => consume(cast.child, epsilon);
      case alias: Alias => consume(alias.child, epsilon);
      case agg: AggregateExpression => {
        if (agg.enableDP) {
          consume(null.asInstanceOf[DataCategory], epsilon);
        }
      }
      case _ =>
    }
  }

  def budget(data: DataCategory) = tmpBudget;

  def copy() = {
    new GlobalBudgetManager(this.budget);
  }

}

class FineBudgetManager(val budgets: mutable.Map[DataCategory, Double]) extends DPBudgetManager with Logging {

  val tmpBudgets = new HashMap[DataCategory, Double];
  sync(budgets, tmpBudgets);

  def commit() {
    sync(tmpBudgets, budgets);

    val sb = new StringBuilder;
    for (t <- budgets) {
      sb.append(s"\tdata: ${t._1.getId()}, budget: ${t._2}\n");
    }
    logWarning(s"commit fine privacy budget, budget left:\n ${sb.toString}");
  }

  def specified(data: DataCategory): Boolean = {
    return budgets.contains(data);
  }

  def rollback() {
    logWarning(s"rollback fine privacy budget");
    sync(budgets, tmpBudgets);
  }

  def consume(data: DataCategory, epsilon: Double) {
    if (!specified(data)) {
      return ;
    }
    val budget = tmpBudgets.getOrElse(data, 0.0);
    if (budget - epsilon < 0) {
      rollback;
      throw new PrivacyException(s"No enough privacy budget for ${data.getId}, privacy budget left: $budget");
    }
    tmpBudgets += ((data, budget - epsilon));
  }

  def budget(data: DataCategory) = tmpBudgets.getOrElse(data, 0.0);

  def consume(plan: Aggregate, epsilon: Double) {
    plan.aggregateExpressions.foreach(consume(_, epsilon, plan));
  }

  private def consume(expr: Expression, epsilon: Double, plan: Aggregate) {
    expr match {
      case attr: Attribute =>
      case cast: Cast => consume(cast.child, epsilon, plan);
      case alias: Alias => consume(alias.child, epsilon, plan);
      case agg: AggregateExpression => {
        if (agg.enableDP) {
          val set = new HashSet[DataCategory];
          val attr = resolveSimpleAttribute(agg.children(0)).asInstanceOf[Attribute];
          //TODO luochen add support for complex types
          val label = plan.childLabel(attr);
          set ++= label.getDatas;
          plan.condLabels.foreach(set ++= _.getDatas);
          set.withFilter(specified(_)).foreach(consume(_, epsilon));
        }
      }

    }
  }

  def copy(): DPBudgetManager = {
    val map = new HashMap[DataCategory, Double];
    sync(budgets, map);
    return new FineBudgetManager(map);
  }

  private def sync(src: mutable.Map[DataCategory, Double], dest: mutable.Map[DataCategory, Double]) {
    dest.clear;
    src.iterator.foreach(t => {
      dest += t;
    });
  }

}
