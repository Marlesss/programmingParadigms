package expression;

import expression.exceptions.DBZExcpetion;
import expression.exceptions.EvaluateException;
import expression.exceptions.OverflowException;

import java.math.BigInteger;
import java.util.Objects;

public class Additive<T extends Number> implements Comparable<Additive<?>> {
    protected T value;

    public Additive(T value) {
        this.value = value;
    }

    public Additive<?> neutral() {
        if (value instanceof Integer) {
            return new Additive<>(0);
        }
        if (value instanceof Double) {
            return new Additive<>((double) 0);
        }
        if (value instanceof BigInteger) {
            return new Additive<>(BigInteger.ZERO);
        }
        throw new RuntimeException("Unknown type of variable");
    }

    public Additive<?> add(Additive<?> y, Boolean overflowCheck) {
        if (overflowCheck) {
            if (value instanceof Integer ix && y.value instanceof Integer iy) {
                int result = ix + iy;
                if (ix >= 0) {
                    if (iy >= 0) {
                        if (result < ix) {
                            throw new OverflowException("Overflow received by executing " + this);
                        }
                    } else {
                        if (result > ix) {
                            throw new OverflowException("Overflow received by executing " + this);
                        }
                    }
                } else {
                    if (iy >= 0) {
                        if (result < ix) {
                            throw new OverflowException("Overflow received by executing " + this);
                        }
                    } else {
                        if (result > ix) {
                            throw new OverflowException("Overflow received by executing " + this);
                        }
                    }
                }
                return this.add(y, false);
            }
            throw new RuntimeException("Unknown type of variable");
        }

        if (value instanceof Integer ix && y.value instanceof Integer iy) {
            return new Additive<>(ix + iy);
        }
        if (value instanceof Double ix && y.value instanceof Double iy) {
            return new Additive<>(ix + iy);
        }
        if (value instanceof BigInteger ix && y.value instanceof BigInteger iy) {
            return new Additive<>(ix.add(iy));
        }
        throw new RuntimeException("Unknown type of variable");
    }

    public Additive<?> subtract(Additive<?> y, Boolean exceptionCheck) {
        if (exceptionCheck) {
            if (value instanceof Integer ix && y.value instanceof Integer iy) {
                int result = ix - iy;
                if (ix >= 0) {
                    if (iy >= 0) {
                        if (result > ix) {
                            throw new OverflowException("Overflow received by executing " + this);
                        }
                    } else {
                        if (result < ix) {
                            throw new OverflowException("Overflow received by executing " + this);
                        }
                    }
                } else {
                    if (iy >= 0) {
                        if (result > ix) {
                            throw new OverflowException("Overflow received by executing " + this);
                        }
                    } else {
                        if (result < ix) {
                            throw new OverflowException("Overflow received by executing " + this);
                        }
                    }
                }
                return this.subtract(y, false);
            }
            throw new RuntimeException("Unknown type of variable");
        }
        if (value instanceof Integer ix && y.value instanceof Integer iy) {
            return new Additive<>(ix - iy);
        }
        if (value instanceof Double ix && y.value instanceof Double iy) {
            return new Additive<>(ix - iy);
        }
        if (value instanceof BigInteger ix && y.value instanceof BigInteger iy) {
            return new Additive<>(ix.subtract(iy));
        }
        throw new RuntimeException("Unknown type of variable");
    }

    public Additive<?> multiply(Additive<?> y, Boolean exceptionCheck) {
        if (exceptionCheck) {
            if (y.equals(y.neutral())) {
                return this.multiply(y, false);
            }
            if (value instanceof Integer ix && y.value instanceof Integer iy) {
                if (ix == -1 && iy == Integer.MIN_VALUE || iy == -1 && ix == Integer.MIN_VALUE) {
                    throw new OverflowException("Overflow received by executing " + this);
                }
                int result = ix * iy;
                if (ix != result / iy) {
                    throw new OverflowException("Overflow received by executing " + this);
                }
                return this.multiply(y, false);

            }
            throw new RuntimeException("Unknown type of variable");
        }
        if (value instanceof Integer ix && y.value instanceof Integer iy) {
            return new Additive<>(ix * iy);
        }
        if (value instanceof Double ix && y.value instanceof Double iy) {
            return new Additive<>(ix * iy);
        }
        if (value instanceof BigInteger ix && y.value instanceof BigInteger iy) {
            return new Additive<>(ix.multiply(iy));
        }
        throw new RuntimeException("Unknown type of variable");
    }

    public Additive<?> divide(Additive<?> y, boolean exceptionCheck) {
        if (exceptionCheck) {
            if (y.equals(y.neutral())) {
                throw new DBZExcpetion("Division by zero when executing " + this);
            }
            if (value instanceof Integer ix && y.value instanceof Integer iy) {
                if (ix == Integer.MIN_VALUE && iy == -1) {
                    throw new EvaluateException("Overflow received by executing " + this);
                }
                return this.divide(y, false);
            }
            throw new RuntimeException("Unknown type of variable");
        }
        if (value instanceof Integer ix && y.value instanceof Integer iy) {
            return new Additive<>(ix / iy);
        }
        if (value instanceof Double ix && y.value instanceof Double iy) {
            return new Additive<>(ix / iy);
        }
        if (value instanceof BigInteger ix && y.value instanceof BigInteger iy) {
            return new Additive<>(ix.divide(iy));
        }
        throw new RuntimeException("Unknown type of variable");
    }

    public Additive<?> negate(boolean exceptionCheck) {
        if (exceptionCheck) {
            if (value instanceof Integer x) {
                int result = -x;
                if (x > 0 && result >= x || x < 0 && result <= x) {
                    throw new OverflowException("Overflow received by executing " + this);
                }
                return this.negate(false);
            }
            throw new RuntimeException("Unknown type of variable");
        }
        if (value instanceof Integer ix) {
            return new Additive<>(-ix);
        }
        if (value instanceof Double ix) {
            return new Additive<>(-ix);
        }
        if (value instanceof BigInteger ix) {
            return new Additive<>(ix.negate());
        }
        throw new RuntimeException("Unknown type of variable");
    }

    public Additive<?> max(Additive<?> y) {
        if (value instanceof Integer ix && y.value instanceof Integer iy) {
            return new Additive<>(Integer.max(ix, iy));
        }
        if (value instanceof Double ix && y.value instanceof Double iy) {
            return new Additive<>(Double.max(ix, iy));
        }
        if (value instanceof BigInteger ix && y.value instanceof BigInteger iy) {
            return new Additive<>(ix.max(iy));
        }
        throw new RuntimeException("Unknown type of variable");
    }

    public Additive<?> min(Additive<?> y) {
        if (value instanceof Integer ix && y.value instanceof Integer iy) {
            return new Additive<>(Integer.min(ix, iy));
        }
        if (value instanceof Double ix && y.value instanceof Double iy) {
            return new Additive<>(Double.min(ix, iy));
        }
        if (value instanceof BigInteger ix && y.value instanceof BigInteger iy) {
            return new Additive<>(ix.min(iy));
        }
        throw new RuntimeException("Unknown type of variable");
    }

    public Additive<?> abs(Boolean overflowCheck) {
        if (overflowCheck) {
            if (value instanceof Integer x) {
                if (x >= 0) {
                    return this;
                }
                if (-x <= x) {
                    throw new OverflowException("Overflow received by executing " + this);
                }
                return this.negate(false);
            }
            throw new RuntimeException("Unknown type of variable");
        }
        if (this.compareTo(this.neutral()) >= 1) {
            return this;
        }
        return this.negate(false);
    }


    public int intValue() {
        return value.intValue();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Additive<?> additive = (Additive<?>) o;
        return Objects.equals(value, additive.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(Additive<?> o) {
        if (value instanceof Integer ix && o.value instanceof Integer iy) {
            return Integer.compare(ix, iy);
        }
        if (value instanceof Double ix && o.value instanceof Double iy) {
            return Double.compare(ix, iy);
        }
        if (value instanceof BigInteger ix && o.value instanceof BigInteger iy) {
            return ix.compareTo(iy);
        }
        throw new RuntimeException("Unknown type of variable");
    }

    public Number getValue() {
        return value;
    }

    public Additive<?> lZeroes() {
        if (value instanceof Integer x) {
            if (x < 0) {
                return this.neutral();
            }
            int ans = 0;
            while (x > 0) {
                x /= 2;
                ans++;
            }
            return new Additive<>(32 - ans);

        }
        throw new RuntimeException("Unknown type of variable");
    }

    public Additive<?> tZeroes() {
        if (value instanceof Integer x) {
            if (x == 0) {
                return new Additive<>(32);
            }
            int ans = 0;
            if (x > 0) {
                x = x + Integer.MAX_VALUE + 1;
            }
            while (x % 2 == 0) {
                x /= 2;
                ans++;
            }
            return new Additive<>(ans);

        }
        throw new RuntimeException("Unknown type of variable");
    }
}
