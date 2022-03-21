package expression.generic;

public interface SuperExpression<T extends Number> {
    Additive<T> evaluateImpl(Additive<T> x);

    Additive<T> evaluateImpl(Additive<T> x, Additive<T> y, Additive<T> z);

    default Const<T> c(Additive<T> c) {
        return new Const<T>(c);
    }

    default RuntimeException wrongType() {
        return new RuntimeException("Wrong type of number");
    }
}
