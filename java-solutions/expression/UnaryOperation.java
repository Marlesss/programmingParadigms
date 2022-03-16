package expression;

public abstract class UnaryOperation implements SuperExpression {
    private final SuperExpression expression;

    public UnaryOperation(SuperExpression expression) {
        this.expression = expression;
    }

    @Override
    public <T extends Number> Additive<?> evaluateImpl(Additive<T> x) {
        return calc(expression.evaluateImpl(x));
    }

    @Override
    public <T extends Number> Additive<?> evaluateImpl(Additive<T> x, Additive<T> y, Additive<T> z) {
        return calc(expression.evaluateImpl(x, y, z));
    }

    protected abstract Additive<?> calc(Additive<?> x);

    @Override
    public String toString() {
        return getOperation() + "(" + expression.toString() + ")";
    }

    public boolean equals(Object obj) {
        if (obj instanceof UnaryOperation other) {
            return getOperation().equals(other.getOperation()) && expression.equals(other.expression);
        }
        return false;
    }

    protected abstract String getOperation();

    public int hashCode() {
        return expression.hashCode() * 17 + getOperation().hashCode();
    }


}
