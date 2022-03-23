package expression.generic;

public class Main {
    public static void main(String[] args) {
        if (args.length != 8) {
            System.out.println("Wrong arguments");
            System.out.println("You have to launch this program with arguments: mode expression x1 x2 y1 y2 z1 z2");
            System.out.println("mode in {\"i\", \"d\", \"bi\", \"u\", \"l\", \"s\"}");
            System.out.println("expression - string with expression to calculate");
            System.out.println("x1, x2, y1, y2, z1, z2 - integers of min and max value of variable");
            return;
        }
        String mode = args[0];
        String expression = args[1];
        int x1 = Integer.parseInt(args[2]), x2 = Integer.parseInt(args[3]),
                y1 = Integer.parseInt(args[4]), y2 = Integer.parseInt(args[5]),
                z1 = Integer.parseInt(args[6]), z2 = Integer.parseInt(args[7]);
        GenericTabulator tabulator = new GenericTabulator();
        try {
            Object[][][] table = tabulator.tabulate(mode, expression, x1, x2, y1, y2, z1, z2);
            System.out.println("Output in format: \"Xi Yi Zi expression(Xi, Yi, Zi)\"");
            System.out.println("for Xi in [x1, x2], for Yi in [y1, y2], for Zi in [z1, z2]");
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
