package expression.generic;

public class IntegerAdditive extends Additive<Integer> {
    public IntegerAdditive(Integer value) {
        this.value = value;
    }

    @Override
    public IntegerAdditive parseConst(StringBuilder sb) {
        return new IntegerAdditive(Integer.parseInt(sb.toString()));
    }

    @Override
    public IntegerAdditive valueOf(int i) {
        return new IntegerAdditive(i);
    }

    @Override
    public IntegerAdditive neutral() {
        return new IntegerAdditive(0);
    }

    @Override
    public IntegerAdditive add(Additive<Integer> y, boolean exceptionCheck) {
        Integer result = this.value + y.value;
        if (exceptionCheck) {
            if (this.value >= 0) {
                if (y.value >= 0) {
                    if (result < this.value) {
                        throw new OverflowException("Overflow received");
                    }
                } else {
                    if (result > this.value) {
                        throw new OverflowException("Overflow received");
                    }
                }
            } else {
                if (y.value >= 0) {
                    if (result < this.value) {
                        throw new OverflowException("Overflow received");
                    }
                } else {
                    if (result > this.value) {
                        throw new OverflowException("Overflow received");
                    }
                }
            }
        }
        return new IntegerAdditive(result);
    }

    @Override
    public IntegerAdditive subtract(Additive<Integer> y, boolean exceptionCheck) {
        Integer result = this.value - y.value;
        if (exceptionCheck) {
            if (this.value >= 0) {
                if (y.value >= 0) {
                    if (result > this.value) {
                        throw new OverflowException("Overflow received");
                    }
                } else {
                    if (result < this.value) {
                        throw new OverflowException("Overflow received");
                    }
                }
            } else {
                if (y.value >= 0) {
                    if (result > this.value) {
                        throw new OverflowException("Overflow received");
                    }
                } else {
                    if (result < this.value) {
                        throw new OverflowException("Overflow received");
                    }
                }
            }
        }
        return new IntegerAdditive(result);
    }

    @Override
    public IntegerAdditive multiply(Additive<Integer> y, boolean exceptionCheck) {
        Integer result = this.value * y.value;
        if (exceptionCheck && y.value != 0) {
            if (this.value == -1 && y.value == Integer.MIN_VALUE || y.value == -1 && this.value == Integer.MIN_VALUE) {
                throw new OverflowException("Overflow received");
            }
            if (this.value != result / y.value) {
                throw new OverflowException("Overflow received");
            }
        }
        return new IntegerAdditive(result);
    }

    @Override
    public IntegerAdditive divide(Additive<Integer> y, boolean exceptionCheck) {
        if (exceptionCheck) {
            if (y.value == 0) {
                throw new DBZExcpetion("Division by zero");
            }
            if (this.value == Integer.MIN_VALUE && y.value == -1) {
                throw new EvaluateException("Overflow received");
            }
        }
        Integer result = this.value / y.value;
        return new IntegerAdditive(result);
    }

    @Override
    public IntegerAdditive negate(boolean exceptionCheck) {
        if (exceptionCheck) {
            if (this.value == Integer.MIN_VALUE) {
                throw new OverflowException("Overflow received");
            }
        }
        return new IntegerAdditive(-this.value);
    }

    @Override
    public IntegerAdditive max(Additive<Integer> y) {
        return new IntegerAdditive(Integer.max(this.value, y.value));
    }

    @Override
    public IntegerAdditive min(Additive<Integer> y) {
        return new IntegerAdditive(Integer.min(this.value, y.value));
    }

    @Override
    public IntegerAdditive count() {
        return new IntegerAdditive(Integer.bitCount(value));
    }

    @Override
    protected IntegerAdditive copy() {
        return new IntegerAdditive(this.value);
    }

//    @Override
//    public IntegerAdditive lZeroes() {
//        Integer x = this.getValue();
//        if (x < 0) {
//            return this.neutral();
//        }
//        int ans = 0;
//        while (x > 0) {
//            x /= 2;
//            ans++;
//        }
//        return new IntegerAdditive(32 - ans);
//    }
//
//    @Override
//    public IntegerAdditive tZeroes() {
//        Integer x = this.getValue();
//        if (x == 0) {
//            return new IntegerAdditive(32);
//        }
//        int ans = 0;
//        if (x > 0) {
//            x = x + Integer.MAX_VALUE + 1;
//        }
//        while (x % 2 == 0) {
//            x /= 2;
//            ans++;
//        }
//        return new IntegerAdditive(ans);
//    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public int compareTo(Additive<Integer> o) {
        return Integer.compare(this.getValue(), o.getValue());
    }
}
