package expression;

public class Min extends BinaryOperation {
    public Min(SuperExpression first, SuperExpression second) {
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
