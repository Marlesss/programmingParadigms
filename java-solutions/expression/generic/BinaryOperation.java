package expression.generic;

abstract class BinaryOperation<T extends Number> implements SuperExpression<T> {
    private final SuperExpression<T> first;
    private final SuperExpression<T> second;


    public BinaryOperation(SuperExpression<T> first, SuperExpression<T> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public Additive<T> evaluateImpl(Additive<T> x) {
        return calc(first.evaluateImpl(x), second.evaluateImpl(x));
    }

    @Override
    public Additive<T> evaluateImpl(Additive<T> x, Additive<T> y, Additive<T> z) {
        return calc(first.evaluateImpl(x, y, z), second.evaluateImpl(x, y, z));
    }

    protected abstract Additive<T> calc(Additive<T> x, Additive<T> y);

    protected abstract String getOperation();

    @Override
    public String toString() {
        return "(" + first.toString() + " " + getOperation() + " " + second.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinaryOperation other) {
            return getOperation().equals(other.getOperation()) && first.equals(other.first) &&
                    second.equals(other.second);
        }
        return false;
    }

    // :NOTE: Objects.hash
    @Override
    public int hashCode() {
        return (first.hashCode() * 17 + second.hashCode()) * 17 + getOperation().hashCode();
    }
}
