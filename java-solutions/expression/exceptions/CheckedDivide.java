package expression.exceptions;

import expression.SuperExpression;

public class CheckedDivide extends BinaryOperation {
    public CheckedDivide(SuperExpression first, SuperExpression second) {
        super(first, second);
    }

    @Override
    protected String getOperation() {
        return "/";
    }

    @Override
    protected int calc(int x, int y) {
        if (y == 0) {
            throw new DBZExcpetion("Division by zero when executing " + this);
        }
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new EvaluateException("Overflow received by executing " + this);
        }
        return x / y;
    }

}
