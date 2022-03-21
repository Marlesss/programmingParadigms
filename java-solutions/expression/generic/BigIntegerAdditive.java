package expression.generic;

import java.math.BigInteger;

public class BigIntegerAdditive extends Additive<BigInteger> {
    public BigIntegerAdditive(BigInteger value) {
        this.value = value;
    }

    @Override
    public BigIntegerAdditive parseConst(StringBuilder sb) {
        return new BigIntegerAdditive(BigInteger.valueOf(Long.parseLong(sb.toString())));
    }

    @Override
    public BigIntegerAdditive valueOf(int i) {
        return new BigIntegerAdditive(BigInteger.valueOf(i));
    }

    @Override
    public BigIntegerAdditive neutral() {
        return new BigIntegerAdditive(BigInteger.ZERO);
    }

    @Override
    public BigIntegerAdditive add(Additive<BigInteger> y, boolean exceptionCheck) {
        return new BigIntegerAdditive(value.add(y.value));
    }

    @Override
    public BigIntegerAdditive subtract(Additive<BigInteger> y, boolean exceptionCheck) {
        return new BigIntegerAdditive(value.subtract(y.value));
    }

    @Override
    public BigIntegerAdditive multiply(Additive<BigInteger> y, boolean exceptionCheck) {
        return new BigIntegerAdditive(value.multiply(y.value));
    }

    @Override
    public BigIntegerAdditive divide(Additive<BigInteger> y, boolean exceptionCheck) {
        return new BigIntegerAdditive(value.divide(y.value));
    }

    @Override
    public BigIntegerAdditive negate(boolean exceptionCheck) {
        return new BigIntegerAdditive(value.negate());
    }

    @Override
    public BigIntegerAdditive max(Additive<BigInteger> y) {
        return new BigIntegerAdditive(value.max(y.value));
    }

    @Override
    public BigIntegerAdditive min(Additive<BigInteger> y) {
        return new BigIntegerAdditive(value.min(y.value));
    }

    @Override
    public BigIntegerAdditive count() {
        return new BigIntegerAdditive(BigInteger.valueOf(value.bitCount()));
    }

    @Override
    protected BigIntegerAdditive copy() {
        return new BigIntegerAdditive(value);
    }

    @Override
    public BigInteger getValue() {
        return value;
    }

    @Override
    public int compareTo(Additive<BigInteger> o) {
        return value.compareTo(o.value);
    }
}
