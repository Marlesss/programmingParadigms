package expression.exceptions;

import expression.Additive;
import expression.SuperExpression;
import expression.UnaryOperation;

public class LZerores extends UnaryOperation {
    public LZerores(SuperExpression expression) {
        super(expression);
    }

    @Override
    protected Additive<?> calc(Additive<?> x) {
        return x.lZeroes();
    }

//    @Override
//    protected int calc(int x) {
//        if (x < 0) {
//            return 0;
//        }
//        int ans = 0;
//        while (x > 0) {
//            x /= 2;
//            ans++;
//        }
//        return 32 - ans;
//    }

    @Override
    protected String getOperation() {
        return "l0";
    }
}
