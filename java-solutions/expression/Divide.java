package expression;

public class Divide extends BinaryOperation {
    public Divide(SuperExpression first, SuperExpression second) {
        super(first, second);
    }

    @Override
    protected Additive<?> calc(Additive<?> x, Additive<?> y) {
        return x.divide(y, false);
    }

    @Override
    protected String getOperation() {
        return "/";
    }

}
