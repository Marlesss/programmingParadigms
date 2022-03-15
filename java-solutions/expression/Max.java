package expression;

public class Max extends BinaryOperation {
    public Max(SuperExpression first, SuperExpression second) {
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
