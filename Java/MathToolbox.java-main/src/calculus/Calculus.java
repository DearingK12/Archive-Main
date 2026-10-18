package calculus;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

public class Calculus {

    public static IExpr derivative(ExprEvaluator util, String expr) {
        return util.eval("D(" + expr + ", x)");
    }

    public static IExpr integral(ExprEvaluator util, String expr) {
        return util.eval("Integrate(" + expr + ", x)");
    }

    public static IExpr simplify(ExprEvaluator util, String expr) {
        return util.eval("Simplify(" + expr + ")");
    }
}
