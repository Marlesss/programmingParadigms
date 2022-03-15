package expression.exceptions;

import expression.SuperExpression;

public class CheckedLZerores extends UnaryOperation {
    public CheckedLZerores(SuperExpression expression) {
        super(expression);
    }

    @Override
    protected int calc(int x) {
        if (x < 0) {
            return 0;
        }
        int ans = 0;
        while (x > 0) {
            x /= 2;
            ans++;
        }
        return 32 - ans;
    }

    @Override
    protected String getOperation() {
        return "l0";
    }
}
