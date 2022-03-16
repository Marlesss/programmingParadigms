package expression;

public class Variable implements SuperExpression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public <T extends Number> Additive<?> evaluateImpl(Additive<T> x) {
        return x;
    }

    @Override
    public <T extends Number> Additive<?> evaluateImpl(Additive<T> x, Additive<T> y, Additive<T> z) {
        if (name.equals("x")) {
            return x;
        }
        if (name.equals("y")) {
            return y;
        }
        if (name.equals("z")) {
            return z;
        }
        return x.neutral();
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Variable other) {
            return name.equals(other.name);
        }
        return false;
    }

    public int hashCode() {
        return name.hashCode();
    }
}
