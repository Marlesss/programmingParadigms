package expression;

public class Negate implements SuperExpression {
    private final SuperExpression expression;

    public Negate(SuperExpression expression) {
        this.expression = expression;
    }

    @Override
    public int evaluate(int x) {
        return -expression.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return -expression.evaluate(x, y, z);
    }

    @Override
    public String toString() {
        return "-(" + expression.toString() + ")";
    }

    public boolean equals(Object obj) {
        if (obj instanceof Negate) {
            Negate other = (Negate) obj;
            return expression.equals(other.expression);
        }
        return false;
    }

    public int hashCode() {
        return expression.hashCode() * 17 + 1;
    }

}
