package edu.thu.ss.spec.util;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Sort;
import com.microsoft.z3.Status;
import com.microsoft.z3.Symbol;
import com.microsoft.z3.Z3Exception;

public class Z3Sample {
	public static void main(String[] args) throws Z3Exception {
		Context ctx = new Context();

		Symbol p = ctx.mkSymbol("p");
		Symbol q = ctx.mkSymbol("q");

		Symbol[] names = new Symbol[] { p, q };
		Sort[] types = new Sort[] { ctx.getIntSort(), ctx.getIntSort() };

		Expr p1 = ctx.mkBound(1, ctx.getIntSort());
		Expr q1 = ctx.mkBound(0, ctx.getIntSort());
		//	p1 = (IntExpr) ctx.mkConst(p, ctx.getIntSort());
		Expr one = ctx.mkNumeral(1, ctx.getIntSort());
		Expr two = ctx.mkNumeral(2, ctx.getIntSort());
		BoolExpr pre = ctx.mkEq(p1, one);
		BoolExpr post = ctx.mkAnd(ctx.mkOr(ctx.mkEq(p1, one), ctx.mkEq(p1, two)), ctx.mkEq(q1, one));

		BoolExpr implies = ctx.mkImplies(pre, post);
		BoolExpr formula = ctx.mkForall(types, names, implies, 1, null, null, null, null);

		System.out.println(formula.toString());

		Solver solver = ctx.mkSolver();
		solver.add(formula);
		Status status = solver.check();

		System.out.println(status);

		ctx.dispose();

	}
}
