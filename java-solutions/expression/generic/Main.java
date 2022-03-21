package expression.generic;

public class Main {
    public static void main(String[] args) {
        System.out.println(Integer.bitCount(Short.toUnsignedInt((short) -30)));
        //        String mode = "s"; String expression = "count(-30)"; int x1 = -7, x2 = 2, y1 = -2, y2 = 6, z1 = -5, z2 = 5;
//        GenericTabulator tabulator = new GenericTabulator();
//        try {
//            Object[][][] table = tabulator.tabulate(mode, expression, x1, x2, y1, y2, z1, z2);
//            for (int x = 0; x + x1 <= x2; x++) {
//                for (int y = 0; y + y1 <= y2; y++) {
//                    for (int z = 0; z + z1 <= z2; z++) {
//                        System.out.println(x + " " + y + " " + z + " " + table[x][y][z]);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
