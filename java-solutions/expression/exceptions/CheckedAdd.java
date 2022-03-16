package expression.exceptions;

import expression.Add;
import expression.Additive;
import expression.SuperExpression;

public class CheckedAdd extends Add {
    public CheckedAdd(SuperExpression first, SuperExpression second) {
        super(first, second);
    }

    @Override
    protected Additive<?> calc(Additive<?> x, Additive<?> y) {
        return x.add(y, true);
    }

//    @Override
//    protected int calc(int x, int y) {
//        int result = x + y;
//        if (x >= 0) {
//            if (y >= 0) {
//                if (result < x) {
//                    throw new OverflowException("Overflow received by executing " + this);
//                }
//            } else {
//                if (result > x) {
//                    throw new OverflowException("Overflow received by executing " + this);
//                }
//            }
//        } else {
//            if (y >= 0) {
//                if (result < x) {
//                    throw new OverflowException("Overflow received by executing " + this);
//                }
//            } else {
//                if (result > x) {
//                    throw new OverflowException("Overflow received by executing " + this);
//                }
//            }
//        }
//        return result;
//    }

}
