package expression;

abstract class BinaryOperation implements SuperExpression {
    private final SuperExpression first;
    private final SuperExpression second;


    public BinaryOperation(SuperExpression first, SuperExpression second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public <T extends Number> Additive<?> evaluateImpl(Additive<T> x) {
        return calc(first.evaluateImpl(x), second.evaluateImpl(x));
    }

    @Override
    public <T extends Number> Additive<?> evaluateImpl(Additive<T> x, Additive<T> y, Additive<T> z) {
        return calc(first.evaluateImpl(x, y, z), second.evaluateImpl(x, y, z));
    }

    protected abstract Additive<?> calc(Additive<?> x, Additive<?> y);

    @Override
    public String toString() {
        return "(" + first.toString() + " " + getOperation() + " " + second.toString() + ")";
    }

    public boolean equals(Object obj) {
        if (obj instanceof BinaryOperation other) {
            return getOperation().equals(other.getOperation()) && first.equals(other.first) &&
                    second.equals(other.second);
        }
        return false;
    }

    public int hashCode() {
        return (first.hashCode() * 17 + second.hashCode()) * 17 + getOperation().hashCode();
    }

    protected abstract String getOperation();
}
