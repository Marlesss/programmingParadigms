package expression.exceptions;

import expression.Additive;
import expression.SuperExpression;
import expression.UnaryOperation;

public class TZeroes extends UnaryOperation {
    public TZeroes(SuperExpression expression) {
        super(expression);
    }

    @Override
    protected Additive<?> calc(Additive<?> x) {
        return x.tZeroes();
    }


//    @Override
//    protected int calc(int x) {
//        if (x == 0) {
//            return 32;
//        }
//        int ans = 0;
//        if (x > 0) {
//            x = x + Integer.MAX_VALUE + 1;
//        }
//        while (x % 2 == 0) {
//            x /= 2;
//            ans++;
//        }
//        return ans;
//    }

    @Override
    protected String getOperation() {
        return "t0";
    }
}
