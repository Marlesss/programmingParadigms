package expression.generic;

public abstract class UnaryOperation<T extends Number> implements SuperExpression<T> {
    private final SuperExpression<T> expression;

    public UnaryOperation(SuperExpression<T> expression) {
        this.expression = expression;
    }

    @Override
    public Additive<T> evaluateImpl(Additive<T> x) {
        return calc(expression.evaluateImpl(x));
    }

    @Override
    public Additive<T> evaluateImpl(Additive<T> x, Additive<T> y, Additive<T> z) {
        return calc(expression.evaluateImpl(x, y, z));
    }

    protected abstract Additive<T> calc(Additive<T> x);

    @Override
    public String toString() {
        return getOperation() + "(" + expression.toString() + ")";
    }

    protected abstract String getOperation();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UnaryOperation other) {
            return getOperation().equals(other.getOperation()) && expression.equals(other.expression);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return expression.hashCode() * 17 + getOperation().hashCode();
    }


}
