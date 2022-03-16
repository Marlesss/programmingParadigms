package expression.generic;

import expression.SuperExpression;
import expression.parser.ExpressionParser;

import java.math.BigInteger;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        System.err.println("String mode = \"" + mode + "\"; String expression = \"" + expression + "\"; int x1 = " + x1 + ", x2 = " + x2 + ", y1 = " + y1 + ", y2 = " + y2 + ", z1 = " + z1 + ", z2 = " + z2 + ";");
        ExpressionParser parser = new ExpressionParser(mode);
        SuperExpression parsedExpression = parser.parse(expression);
        Object[][][] table = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int x = 0; x + x1 <= x2; x++) {
            for (int y = 0; y + y1 <= y2; y++) {
                for (int z = 0; z + z1 <= z2; z++) {
                    Number ix, iy, iz;
                    switch (mode) {
                        case "i":
                            ix = x + x1;
                            iy = y + y1;
                            iz = z + z1;
                            break;
                        case "d":
                            ix = (double) (x + x1);
                            iy = (double) (y + y1);
                            iz = (double) (z + z1);
                            break;
                        case "bi":
                            ix = BigInteger.valueOf(x + x1);
                            iy = BigInteger.valueOf(y + y1);
                            iz = BigInteger.valueOf(z + z1);
                            break;
                        default:
                            ix = null;
                            iy = null;
                            iz = null;
                    }
                    try {
                        table[x][y][z] = parsedExpression.evaluate(ix, iy, iz);
                    } catch (Exception e) {
                        table[x][y][z] = null;
                    }
                }
            }
        }
        return table;
    }
}
