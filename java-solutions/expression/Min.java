package expression;

public class Min extends BinaryOperation {
    public Min(SuperExpression first, SuperExpression second) {
        super(first, second);
    }

    @Override
    protected Additive<?> calc(Additive<?> x, Additive<?> y) {
        return x.min(y);
    }

    @Override
    protected String getOperation() {
        return "min";
    }
}
