package algebra;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

public class Algebra {

    public static IExpr solve(ExprEvaluator util, String expr) {
        return util.eval("Solve(" + expr + ", x)");
    }
}
