package expression.generic;

public class LongAdditive extends Additive<Long> {
    public LongAdditive(Long value) {
        this.value = value;
    }

    @Override
    public LongAdditive parseConst(StringBuilder sb) {
        return new LongAdditive(Long.valueOf(sb.toString()));
    }

    @Override
    public LongAdditive valueOf(int i) {
        return new LongAdditive((long) i);
    }

    @Override
    public LongAdditive neutral() {
        return new LongAdditive(0L);
    }

    @Override
    public LongAdditive add(Additive<Long> y, boolean exceptionCheck) {
        return new LongAdditive(value + y.value);
    }

    @Override
    public LongAdditive subtract(Additive<Long> y, boolean exceptionCheck) {
        return new LongAdditive(value - y.value);
    }

    @Override
    public LongAdditive multiply(Additive<Long> y, boolean exceptionCheck) {
        return new LongAdditive(value * y.value);
    }

    @Override
    public LongAdditive divide(Additive<Long> y, boolean exceptionCheck) {
        return new LongAdditive(value / y.value);
    }

    @Override
    public LongAdditive negate(boolean exceptionCheck) {
        return new LongAdditive(-value);
    }

    @Override
    public LongAdditive max(Additive<Long> y) {
        return new LongAdditive(Long.max(value, y.value));
    }

    @Override
    public LongAdditive min(Additive<Long> y) {
        return new LongAdditive(Long.min(value, y.value));
    }

    @Override
    public LongAdditive count() {
        return new LongAdditive((long) Long.bitCount(value));
    }

    @Override
    protected LongAdditive copy() {
        return new LongAdditive(value);
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public int compareTo(Additive<Long> o) {
        return 0;
    }
}
