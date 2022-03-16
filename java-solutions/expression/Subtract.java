package expression;

public class Subtract extends BinaryOperation {
    public Subtract(SuperExpression first, SuperExpression second) {
        super(first, second);
    }

    @Override
    protected Additive<?> calc(Additive<?> x, Additive<?> y) {
        return x.subtract(y, false);
    }

    @Override
    protected String getOperation() {
        return "-";
    }

}
