package expression.exceptions;

import expression.Max;
import expression.SuperExpression;

public class CheckedMax extends Max {
    public CheckedMax(SuperExpression first, SuperExpression second) {
        super(first, second);
    }
}
