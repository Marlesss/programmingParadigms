package expression;

public interface SuperExpression extends Expression, TripleExpression {
    default int evaluate(int x) {
        return evaluateImpl(new Additive<>(x)).intValue();
    }

    default int evaluate(int x, int y, int z) {
        return evaluateImpl(new Additive<>(x), new Additive<>(y), new Additive<>(z)).intValue();
    }

    default <T extends Number> Number evaluate(T x) {
        return evaluateImpl(new Additive<>(x)).value;
    }

    default <T extends Number> Number evaluate(T x, T y, T z) {
        return evaluateImpl(new Additive<>(x), new Additive<>(y), new Additive<>(z)).value;
    }


    <T extends Number>
    Additive<?> evaluateImpl(Additive<T> x);

    <T extends Number>
    Additive<?> evaluateImpl(Additive<T> x, Additive<T> y, Additive<T> z);

}
