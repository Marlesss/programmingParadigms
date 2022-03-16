package expression.exceptions;

import expression.Additive;
import expression.Multiply;
import expression.SuperExpression;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(SuperExpression first, SuperExpression second) {
        super(first, second);

    }

    @Override
    protected Additive<?> calc(Additive<?> x, Additive<?> y) {
        return x.multiply(y, true);
    }


//    @Override
//    protected int calc(int x, int y) {
//        if (x == -1 && y == Integer.MIN_VALUE || y == -1 && x == Integer.MIN_VALUE) {
//            throw new OverflowException("Overflow received by executing " + this);
//        }
//        int result = x * y;
//        if (y == 0) {
//            return result;
//        }
//        if (x != result / y) {
//            throw new OverflowException("Overflow received by executing " + this);
//        }
//        return result;
//
//    }

}
