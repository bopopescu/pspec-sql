package edu.thu.ss.spec.lang.pojo;

import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.thu.ss.spec.lang.expression.And;
import edu.thu.ss.spec.lang.expression.Comparison;
import edu.thu.ss.spec.lang.expression.Expression;
import edu.thu.ss.spec.lang.expression.Not;
import edu.thu.ss.spec.lang.expression.Or;
import edu.thu.ss.spec.lang.parser.ParserConstant;

public class Condition implements Parsable, Writable {
	private Expression<DataCategory> expression;
	private boolean preserveNull = false;

	public Set<DataCategory> getDataCategories() {
		return expression.getDataSet();
	}

	public Expression<DataCategory> getExpression() {
		return expression;
	}

	@Override
	public Element outputType(Document document, String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Element outputElement(Document document) {
		Element element = document.createElement(ParserConstant.Ele_Policy_Rule_Filter);
		return element;
	}

	@Override
	public void parse(Node filnode) {
		NodeList list = filnode.getChildNodes();
		/*String attr = XMLUtil.getAttrValue(filnode, "permitNull");
		if (attr.equals("TRUE")) {
			permitNull = true;
		}
		*/
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			String name = node.getLocalName();
			Expression<DataCategory> expr = null;
			if (name == null) {
				continue;
			}
			if (ParserConstant.Ele_Policy_Rule_And.equals(name)) {
				expr = new And();
				expr.parse(node);
				expression = expr;
				break;
			} else if (ParserConstant.Ele_Policy_Rule_Or.equals(name)) {
				expr = new Or();
				expr.parse(node);
				expression = expr;
				break;
			} else if (ParserConstant.Ele_Policy_Rule_Not.equals(name)) {
				expr = new Not();
				expr.parse(node);
				expression = expr;
				break;
			} else if (ParserConstant.Ele_Policy_Rule_Comparison.equals(name)) {
				expr = Comparison.parseComparison(node);
				expr.parse(node);
				expression = expr;
				break;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Filter: ");
		if (expression != null) {
			sb.append("{");
			sb.append(expression);
			sb.append("} ");
		}
		
		return sb.toString() /* + "\r\n\t\t" + expression.split().toString() */;
	}
}