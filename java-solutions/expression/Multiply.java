package expression;

public class Multiply extends BinaryOperation {
    public Multiply(SuperExpression first, SuperExpression second) {
        super(first, second);

    }

    @Override
    protected Additive<?> calc(Additive<?> x, Additive<?> y) {
        return x.multiply(y, false);
    }

    @Override
    protected String getOperation() {
        return "*";
    }


}
