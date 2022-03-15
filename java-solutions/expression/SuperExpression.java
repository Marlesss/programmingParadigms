package expression;

public interface SuperExpression extends Expression, TripleExpression {
    <T extends Number> T evaluate(T x);
    <T extends Number> T evaluate(T x, T y, T z);
}
