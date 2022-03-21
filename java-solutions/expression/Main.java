package expression;

import expression.parser.ExpressionParser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int z = 0, y = 0, x = -10;
//        System.out.println(-11 / 10);
        System.out.println(x * (y + ((z - 1) / 10)));
    }
}
