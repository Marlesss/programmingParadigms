package expression.generic;

public class Variable<T extends Number> implements SuperExpression<T> {
    private final String name;

    public Variable(String name) {
        if (!(name.equals("x") || name.equals("y") || name.equals("z"))) {
            throw new RuntimeException("Unknown name of variable");
        }
        this.name = name;
    }

    @Override
    public Additive<T> evaluateImpl(Additive<T> x) {
        return x;
    }

    @Override
    public Additive<T> evaluateImpl(Additive<T> x, Additive<T> y, Additive<T> z) {
        if (name.equals("x")) {
            return x;
        }
        if (name.equals("y")) {
            return y;
        }
        return z;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable other) {
            return name.equals(other.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
