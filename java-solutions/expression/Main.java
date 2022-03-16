package expression;

import expression.generic.GenericTabulator;

public class Main {
    public static void main(String[] args) {
        String mode = "i"; String expression = "y"; int x1 = -3, x2 = 9, y1 = 0, y2 = 7, z1 = -6, z2 = 2;
        GenericTabulator tabulator = new GenericTabulator();
        try {
            Object[][][] table = tabulator.tabulate(mode, expression, x1, x2, y1, y2, z1, z2);
            for (int x = 0; x + x1 <= x2; x++) {
                for (int y = 0; y + y1 <= y2; y++) {
                    for (int z = 0; z + z1 <= z2; z++) {
                        System.out.println(x + " " + y + " " + z + " " + table[x][y][z]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
