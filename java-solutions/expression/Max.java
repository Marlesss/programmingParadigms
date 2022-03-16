package expression;

public class Max extends BinaryOperation {
    public Max(SuperExpression first, SuperExpression second) {
        super(first, second);
    }

    @Override
    protected Additive<?> calc(Additive<?> x, Additive<?> y) {
        return x.max(y);
    }

    @Override
    protected String getOperation() {
        return "max";
    }

}
