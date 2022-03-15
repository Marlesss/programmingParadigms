package expression;

public class Variable implements SuperExpression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if (name.equals("x")) {
            return x;
        }
        if (name.equals("y")) {
            return y;
        }
        if (name.equals("z")) {
            return z;
        }
        return 0;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            Variable other = (Variable) obj;
            return name.equals(other.name);
        }
        return false;
    }

    public int hashCode() {
        return name.hashCode();
    }
}
