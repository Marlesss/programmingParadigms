package expression.generic;

import java.util.function.*;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        System.err.println("mode = " + mode + " expression = " + expression);
        Object[][][] table = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int x = 0; x + x1 <= x2; x++) {
            for (int y = 0; y + y1 <= y2; y++) {
                for (int z = 0; z + z1 <= z2; z++) {
                    table[x][y][z] = 0.0;
                }
            }
        }
        return table;
    }
}
