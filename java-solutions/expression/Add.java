package expression;

import java.math.BigInteger;

public class Add extends BinaryOperation {
    public Add(SuperExpression first, SuperExpression second) {
        super(first, second);
    }

    @Override
    protected <T  extends Number> T calc(T x, T y) {
        int z = 4;
        if (Integer.TYPE.isInstance(z)) {

        }
        if (x instanceof Integer)
        return x + y;
    }

    @Override
    protected String getOperation() {
        return "+";
    }

    @Override
    public String toMiniString() {
        return super.toMiniString();
    }
}
