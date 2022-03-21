package expression.generic;

public class Multiply<T extends Number> extends BinaryOperation<T> {
    private final boolean exceptionCheck;

    public Multiply(SuperExpression<T> first, SuperExpression<T> second, boolean exceptionCheck) {
        super(first, second);
        this.exceptionCheck = exceptionCheck;
    }

    @Override
    protected Additive<T> calc(Additive<T> x, Additive<T> y) {
        return x.multiply(y, exceptionCheck);
    }

    @Override
    protected String getOperation() {
        return "*";
    }


}
