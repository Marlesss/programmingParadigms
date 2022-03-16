package expression.exceptions;

import expression.Additive;
import expression.Subtract;
import expression.SuperExpression;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(SuperExpression first, SuperExpression second) {
        super(first, second);
    }

    @Override
    protected Additive<?> calc(Additive<?> x, Additive<?> y) {
        return x.subtract(y, true);
    }

//    @Override
//    protected int calc(int x, int y) {
//        int result = x - y;
//        if (x >= 0) {
//            if (y >= 0) {
//                if (result > x) {
//                    throw new OverflowException("Overflow received by executing " + this);
//                }
//            } else {
//                if (result < x) {
//                    throw new OverflowException("Overflow received by executing " + this);
//                }
//            }
//        } else {
//            if (y >= 0) {
//                if (result > x) {
//                    throw new OverflowException("Overflow received by executing " + this);
//                }
//            } else {
//                if (result < x) {
//                    throw new OverflowException("Overflow received by executing " + this);
//                }
//            }
//        }
//        return result;
//    }

}
