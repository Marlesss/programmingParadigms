package expression;

public class Const implements SuperExpression {
    private final Additive<?> cons;

    public Const(int cons) {
        this.cons = new Additive<>(cons);
    }

    public <N extends Number> Const(N cons) {
        this.cons = new Additive<>(cons);
    }

    @Override
    public <T extends Number> Additive<?> evaluateImpl(Additive<T> x) {
        return cons;
    }

    @Override
    public <T extends Number> Additive<?> evaluateImpl(Additive<T> x, Additive<T> y, Additive<T> z) {
        return cons;
    }

    @Override
    public String toString() {
        return String.valueOf(cons);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Const other) {
            return cons.equals(other.cons);
        }
        return false;
    }

    public int hashCode() {
        return cons.intValue();
    }
}
