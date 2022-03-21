package expression.generic;

public class DoubleAdditive extends Additive<Double> {
    public DoubleAdditive(Double value) {
        this.value = value;
    }

    @Override
    public DoubleAdditive parseConst(StringBuilder sb) {
        return new DoubleAdditive(Double.valueOf(sb.toString()));
    }

    @Override
    public DoubleAdditive valueOf(int i) {
        return new DoubleAdditive((double) i);
    }

    @Override
    public DoubleAdditive neutral() {
        return new DoubleAdditive((double) 0);
    }

    @Override
    public DoubleAdditive add(Additive<Double> y, boolean exceptionCheck) {
        return new DoubleAdditive(this.value + y.getValue());
    }

    @Override
    public DoubleAdditive subtract(Additive<Double> y, boolean exceptionCheck) {
        return new DoubleAdditive(this.value - y.getValue());
    }

    @Override
    public DoubleAdditive multiply(Additive<Double> y, boolean exceptionCheck) {
        return new DoubleAdditive(this.value * y.getValue());
    }

    @Override
    public DoubleAdditive divide(Additive<Double> y, boolean exceptionCheck) {
        return new DoubleAdditive(this.value / y.getValue());
    }

    @Override
    public DoubleAdditive negate(boolean exceptionCheck) {
        return new DoubleAdditive(-this.value);
    }

    @Override
    public DoubleAdditive max(Additive<Double> y) {
        return new DoubleAdditive(Double.max(this.value, y.value));
    }

    @Override
    public DoubleAdditive min(Additive<Double> y) {
        return new DoubleAdditive(Double.min(this.value, y.value));
    }

    @Override
    public DoubleAdditive count() {
        return new DoubleAdditive((double) Long.bitCount(Double.doubleToLongBits(value)));
    }

    @Override
    protected DoubleAdditive copy() {
        return new DoubleAdditive(this.value);
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public int compareTo(Additive<Double> o) {
        return Double.compare(this.value, o.getValue());
    }
}
