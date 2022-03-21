package expression.exceptions;

import expression.Negate;
import expression.SuperExpression;

abstract class UnaryOperation implements SuperExpression {
    private final SuperExpression expression;

    public UnaryOperation(SuperExpression expression) {
        this.expression = expression;
    }

    @Override
    public int evaluate(int x) {
        return calc(expression.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calc(expression.evaluate(x, y, z));
    }

    protected abstract int calc(int x);


    @Override
    public String toString() {
        return getOperation() + "(" + expression.toString() + ")";
    }

    public boolean equals(Object obj) {
        if (obj instanceof UnaryOperation) {
            UnaryOperation other = (UnaryOperation) obj;
            return getOperation().equals(other.getOperation()) && expression.equals(other.expression);
        }
        return false;
    }

    protected abstract String getOperation();

    public int hashCode() {
        return expression.hashCode() * 17 + getOperation().hashCode();
    }


}
