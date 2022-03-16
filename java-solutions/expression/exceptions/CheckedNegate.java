package expression.exceptions;


import expression.Additive;
import expression.Negate;
import expression.SuperExpression;
import expression.UnaryOperation;

public class CheckedNegate extends Negate {
    public CheckedNegate(SuperExpression expression) {
        super(expression);
    }

    @Override
    protected Additive<?> calc(Additive<?> x) {
        return x.negate(true);
    }

//
//    protected int calc(int x) {
//        int result = -x;
//        if (x > 0 && result >= x || x < 0 && result <= x) {
//            throw new OverflowException("Overflow received by executing " + this);
//        }
//
//        return result;
//    }
}
