package expression.exceptions;

import expression.SuperExpression;

public class CheckedAbs extends UnaryOperation {

    public CheckedAbs(SuperExpression expression) {
        super(expression);
    }

    @Override
    protected int calc(int x) {
        if (x >= 0) {
            return x;
        }
        int result = -x;
        if (result <= x) {
            throw new OverflowException("Overflow received by executing " + this);
        }
        return result;
    }

    @Override
    protected String getOperation() {
        return "abs";
    }
}
