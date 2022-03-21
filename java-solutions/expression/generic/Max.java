package expression.generic;

public class Max<T extends Number> extends BinaryOperation<T> {
    public Max(SuperExpression<T> first, SuperExpression<T> second) {
        super(first, second);
    }

    @Override
    protected Additive<T> calc(Additive<T> x, Additive<T> y) {
        return x.max(y);
    }

    @Override
    protected String getOperation() {
        return "max";
    }

}
