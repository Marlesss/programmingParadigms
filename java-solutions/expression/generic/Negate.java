package expression.generic;

public class Negate<T extends Number> extends UnaryOperation<T> {
    boolean exceptionCheck;

    public Negate(SuperExpression<T> expression, boolean exceptionCheck) {
        super(expression);
        this.exceptionCheck = exceptionCheck;
    }

    @Override
    protected Additive<T> calc(Additive<T> x) {
        return x.negate(exceptionCheck);
    }

    @Override
    protected String getOperation() {
        return "-";
    }

}
