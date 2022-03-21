package expression.generic;

import java.math.BigInteger;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
//        System.err.println("String mode = \"" + mode + "\"; String expression = \"" + expression + "\"; int x1 = " + x1 + ", x2 = " + x2 + ", y1 = " + y1 + ", y2 = " + y2 + ", z1 = " + z1 + ", z2 = " + z2 + ";");
        Number[][][] table = new Number[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        switch (mode) {
            case "u":
            case "i":
                IntegerAdditive intNeutral = new IntegerAdditive(0).neutral();
                SuperExpression<Integer> intParsedExpression = (new ExpressionParser<>(mode.equals("i"), intNeutral)).parse(expression);
                work(intParsedExpression, table, intNeutral, x1, x2, y1, y2, z1, z2);
                break;
            case "d":
                DoubleAdditive doubleNeutral = new DoubleAdditive((double) 0).neutral();
                SuperExpression<Double> doubleParsedExpression = (new ExpressionParser<>(false, doubleNeutral)).parse(expression);
                work(doubleParsedExpression, table, doubleNeutral, x1, x2, y1, y2, z1, z2);
                break;
            case "bi":
                BigIntegerAdditive bigIntNeutral = new BigIntegerAdditive(BigInteger.ZERO).neutral();
                SuperExpression<BigInteger> bigIntParsedExpression = (new ExpressionParser<>(false, bigIntNeutral)).parse(expression);
                work(bigIntParsedExpression, table, bigIntNeutral, x1, x2, y1, y2, z1, z2);
                break;
            case "l":
                LongAdditive longNeutral = new LongAdditive(0L).neutral();
                SuperExpression<Long> longParsedExpression = (new ExpressionParser<>(false, longNeutral)).parse(expression);
                work(longParsedExpression, table, longNeutral, x1, x2, y1, y2, z1, z2);
                break;
            case "s":
                ShortAdditive shortNeutral = new ShortAdditive(0).neutral();
                SuperExpression<Short> shortParsedExpression = (new ExpressionParser<>(false, shortNeutral)).parse(expression);
                work(shortParsedExpression, table, shortNeutral, x1, x2, y1, y2, z1, z2);
                break;
            default:
                throw new RuntimeException("Unknown parameter");
        }
        return table;
    }

    private <T extends Number> void work(SuperExpression<T> parsedExpression, Number[][][] table, Additive<T> neutral,
                                         int x1, int x2, int y1, int y2, int z1, int z2) {
        for (int x = 0; x + x1 <= x2; x++) {
            for (int y = 0; y + y1 <= y2; y++) {
                for (int z = 0; z + z1 <= z2; z++) {
                    int ix = x + x1, iy = y + y1, iz = z + z1;
                    try {
                        table[x][y][z] = calc(parsedExpression, neutral, ix, iy, iz);
                    } catch (Exception e) {
                        table[x][y][z] = null;
                    }
                }
            }
        }
    }

    private <T extends Number> T calc(SuperExpression<T> parsedExpression, Additive<T> neutral, int x, int y, int z) {
        return parsedExpression.evaluateImpl(
                neutral.valueOf(x),
                neutral.valueOf(y),
                neutral.valueOf(z)
        ).getValue();
    }
}
