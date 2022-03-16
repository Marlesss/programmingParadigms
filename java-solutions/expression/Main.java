package expression;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        System.out.println(
                new Subtract(
                        new Multiply(
                                new Const(BigInteger.valueOf(2)),
                                new Variable("x")
                        ),
                        new Const(BigInteger.valueOf(3))
                ).evaluate(BigInteger.valueOf(5))
        );
        System.out.println(new Subtract(
                new Multiply(
                        new Const(2),
                        new Variable("x")
                ),
                new Const(3)
        ).toString());

        System.out.println(new Multiply(new Const(2), new Variable("x"))
                .equals(new Multiply(new Const(2), new Variable("x")))
        );

        System.out.println(new Multiply(new Const(2), new Variable("x"))
                .equals(new Multiply(new Variable("x"), new Const(2)))
        );
    }
}
