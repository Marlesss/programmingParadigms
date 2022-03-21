package expression.exceptions;

import expression.SuperExpression;

public class CheckedMultiply extends BinaryOperation {
    public CheckedMultiply(SuperExpression first, SuperExpression second) {
        super(first, second);

    }

    @Override
    protected String getOperation() {
        return "*";
    }

    @Override
    protected int calc(int x, int y) {
        if (x == -1 && y == Integer.MIN_VALUE || y == -1 && x == Integer.MIN_VALUE) {
            throw new OverflowException("Overflow received by executing " + this);
        }
        int result = x * y;
        if (y == 0) {
            return result;
        }
        if (x != result / y) {
            throw new OverflowException("Overflow received by executing " + this);
        }
        return result;

    }

}
