package expression.generic;

public class Count<T extends Number> extends UnaryOperation<T>{
    public Count(SuperExpression<T> expression) {
        super(expression);
    }

    @Override
    protected Additive<T> calc(Additive<T> x) {
        return x.count();
    }

    @Override
    protected String getOperation() {
        return "count";
    }
}
