package expression.generic;

public class ShortAdditive extends Additive<Short> {
    public ShortAdditive(Short value) {
        this.value = value;
    }

    public ShortAdditive(int value) {
        this.value = (short) value;
    }

    @Override
    public ShortAdditive parseConst(StringBuilder sb) {
        return new ShortAdditive(Integer.parseInt(sb.toString()));
    }

    @Override
    public ShortAdditive valueOf(int i) {
        return new ShortAdditive((short) i);
    }

    @Override
    public ShortAdditive neutral() {
        return new ShortAdditive(Short.valueOf("0"));
    }

    @Override
    public ShortAdditive add(Additive<Short> y, boolean exceptionCheck) {
        return new ShortAdditive(value + y.value);
    }

    @Override
    public ShortAdditive subtract(Additive<Short> y, boolean exceptionCheck) {
        return new ShortAdditive(value - y.value);
    }

    @Override
    public ShortAdditive multiply(Additive<Short> y, boolean exceptionCheck) {
        return new ShortAdditive(value * y.value);
    }

    @Override
    public ShortAdditive divide(Additive<Short> y, boolean exceptionCheck) {
        return new ShortAdditive(value / y.value);
    }

    @Override
    public ShortAdditive negate(boolean exceptionCheck) {
        return new ShortAdditive(-value);
    }

    @Override
    public ShortAdditive max(Additive<Short> y) {
        if (value >= y.value) {
            return new ShortAdditive(value);
        }
        return new ShortAdditive(y.value);
    }

    @Override
    public ShortAdditive min(Additive<Short> y) {
        if (value >= y.value) {
            return new ShortAdditive(y.value);
        }
        return new ShortAdditive(value);
    }

    @Override
    public ShortAdditive count() {
        return new ShortAdditive(Integer.bitCount(Short.toUnsignedInt((value))));
    }

    @Override
    protected ShortAdditive copy() {
        return new ShortAdditive(value);
    }

    @Override
    public Short getValue() {
        return value;
    }

    @Override
    public int compareTo(Additive<Short> o) {
        return 0;
    }
}
