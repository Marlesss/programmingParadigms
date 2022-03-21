package expression.exceptions;

import expression.SuperExpression;

public class Const implements SuperExpression {
    private final int value;

    public Const(int value) {
        this.value = value;
    }

    @Override
    public int evaluate(int x) {
        return value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Const) {
            Const other = (Const) obj;
            return value == other.value;
        }
        return false;
    }

    public int hashCode() {
        return value;
    }
}
