package expression;

public class Negate extends UnaryOperation {
    public Negate(SuperExpression expression) {
        super(expression);
    }

    @Override
    protected Additive<?> calc(Additive<?> x) {
        return x.negate(false);
    }

    @Override
    protected String getOperation() {
        return "-";
    }

}
