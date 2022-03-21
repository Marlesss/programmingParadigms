package expression.generic;

public class Abs<T extends Number> extends UnaryOperation<T> {
    boolean exceptionCheck = false;

    public Abs(SuperExpression<T> expression, boolean exceptionCheck) {
        super(expression);
        this.exceptionCheck = exceptionCheck;
    }

    @Override
    protected Additive<T> calc(Additive<T> x) {
        return x.abs(exceptionCheck);
    }

    @Override
    protected String getOperation() {
        return "abs";
    }
}
