package expression.generic;

public abstract class Additive<T extends Number> {
    protected T value;

    abstract public Additive<T> parseConst(StringBuilder sb);

    abstract public Additive<T> valueOf(int i);

    abstract public Additive<T> neutral();

    abstract public Additive<T> add(Additive<T> y, boolean exceptionCheck);

    abstract public Additive<T> subtract(Additive<T> y, boolean exceptionCheck);

    abstract public Additive<T> multiply(Additive<T> y, boolean exceptionCheck);

    abstract public Additive<T> divide(Additive<T> y, boolean exceptionCheck);

    abstract public Additive<T> negate(boolean exceptionCheck);

    abstract public Additive<T> max(Additive<T> y);

    abstract public Additive<T> min(Additive<T> y);

    public Additive<T> abs(Boolean exceptionCheck) {
        if (exceptionCheck) {
            if (this.compareTo(this.neutral()) >= 0) {
                return this.copy();
            }
            if (this.negate(false).compareTo(this) <= 0) {
                throw new OverflowException("Overflow received by executing " + this);
            }
            return this.negate(false);
        }
        if (this.compareTo(this.neutral()) >= 1) {
            return this;
        }
        return this.negate(false);
    }

    abstract public Additive<T> count();

    protected abstract Additive<T> copy();

    abstract public T getValue();

    public int intValue() {
        return getValue().intValue();
    }

    abstract public int compareTo(Additive<T> o);

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Additive<?> iAO) {
            return this.getValue().equals(iAO.getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }
}
