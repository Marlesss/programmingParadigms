package expression.exceptions;


import expression.SuperExpression;

public class CheckedNegate extends UnaryOperation {
    public CheckedNegate(SuperExpression expression) {
        super(expression);
    }

    protected int calc(int x) {
        int result = -x;
        if (x > 0 && result >= x || x < 0 && result <= x) {
            throw new OverflowException("Overflow received by executing " + this);
        }

        return result;
    }

    @Override
    protected String getOperation() {
        return "-";
    }

}
