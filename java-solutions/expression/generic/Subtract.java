package expression.generic;

public class Subtract<T extends Number> extends BinaryOperation<T> {
    private final boolean exceptionCheck;

    public Subtract(SuperExpression<T> first, SuperExpression<T> second, boolean exceptionCheck) {
        super(first, second);
        this.exceptionCheck = exceptionCheck;
    }

    @Override
    protected Additive<T> calc(Additive<T> x, Additive<T> y) {
        return x.subtract(y, exceptionCheck);
    }

    @Override
    protected String getOperation() {
        return "-";
    }

}
