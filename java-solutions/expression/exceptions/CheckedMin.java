package expression.exceptions;

import expression.Min;
import expression.SuperExpression;

public class CheckedMin extends Min {
    public CheckedMin(SuperExpression first, SuperExpression second) {
        super(first, second);
    }
}
