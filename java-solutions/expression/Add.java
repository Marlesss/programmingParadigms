package expression;

public class Add extends BinaryOperation {
    public Add(SuperExpression first, SuperExpression second) {
        super(first, second);
    }

    @Override
    protected Additive<?> calc(Additive<?> x, Additive<?> y) {
        return x.add(y, false);
    }


    @Override
    protected String getOperation() {
        return "+";
    }
}
