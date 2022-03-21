package expression;

abstract class BinaryOperation implements SuperExpression {
    private final SuperExpression first;
    private final SuperExpression second;


    public BinaryOperation(SuperExpression first, SuperExpression second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int evaluate(int x) {
        return calc(first.evaluate(x), second.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calc(first.evaluate(x, y, z), second.evaluate(x, y, z));
    }

    protected abstract int calc(int x, int y);


    @Override
    public String toString() {
        return "(" + first.toString() + " " + getOperation() + " " + second.toString() + ")";
    }

    public boolean equals(Object obj) {
        if (obj instanceof BinaryOperation) {
            BinaryOperation other = (BinaryOperation) obj;
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
