package expression.generic;

public class Min<T extends Number> extends BinaryOperation<T> {
    public Min(SuperExpression<T> first, SuperExpression<T> second) {
        super(first, second);
    }

    @Override
    protected Additive<T> calc(Additive<T> x, Additive<T> y) {
        return x.min(y);
    }

    @Override
    protected String getOperation() {
        return "min";
    }
}
