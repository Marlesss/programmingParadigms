package expression.exceptions;

import expression.SuperExpression;

public class CheckedMin extends BinaryOperation {
    public CheckedMin(SuperExpression first, SuperExpression second) {
        super(first, second);
    }

    @Override
    protected String getOperation() {
        return "min";
    }

    @Override
    protected int calc(int x, int y) {
        return Math.min(x, y);
    }

}
