package expression.exceptions;

import expression.SuperExpression;

public class CheckedMax extends BinaryOperation {
    public CheckedMax(SuperExpression first, SuperExpression second) {
        super(first, second);
    }

    @Override
    protected String getOperation() {
        return "max";
    }

    @Override
    protected int calc(int x, int y) {
        return Math.max(x, y);
    }

}
