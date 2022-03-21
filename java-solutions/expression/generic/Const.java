package expression.generic;

public class Const<T extends Number> implements SuperExpression<T> {
    private final Additive<T> cons;

    public Const(Additive<T> cons) {
        this.cons = cons;
    }

    @Override
    public Additive<T> evaluateImpl(Additive<T> x) {
        return cons;
    }

    @Override
    public Additive<T> evaluateImpl(Additive<T> x, Additive<T> y, Additive<T> z) {
        return cons;
    }

    @Override
    public String toString() {
        return cons.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const other) {
            return cons.equals(other.cons);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return cons.intValue();
    }
}
