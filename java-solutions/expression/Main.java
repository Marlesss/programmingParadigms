package expression;

import expression.exceptions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println(new ExpressionParser().parse("1 min1"));
    }
}
