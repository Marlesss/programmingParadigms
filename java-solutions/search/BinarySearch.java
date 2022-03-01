package search;

import java.util.Arrays;

public class BinarySearch {
    // Pred:
    // for i 0..(a.length - 1) int(args[i]) &&
    // for i 2..(a.length - 1) int(args[i]) >= int(args[i - 1])
    // Post: a[R] <= x && R - min available || R == n
    public static void main(final String[] args) {
        // x == int(args[0])
        int x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        // for i 1..(a.length - 1) int(args[i])
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
        }
        System.out.println(iterativusSearch(x, a));
//        System.out.println(recursiveSearch(x, a, 0, a.length));
    }

    // Pred: a - sorted non-ascending
    // Post: a.length == 0 && R = 0 ||
    //       a.length > 0 &&
    //       (a[a.length - 1] > x && R = a.length || a[R] <= x && R - min available && 0 <= R < a.length)
    public static int iterativusSearch(final int x, final int[] a) {
        int n = a.length;
        if (n == 0) {
            return 0;
        }
        int l = 0, r = n, m;
        if (a[0] <= x) {
            return 0;
        }
        // a[l] > x && a[r] <= x || l == 0 && r == n
        while (r - l > 1) {
            // a[l] > x && a[r] <= x && r - l > 1
            m = (l + r) / 2;
            // l < m < r
            if (a[m] <= x) {
                // a[m] <= x
                r = m;
                // a[r'] <= x && l < r' < r
            } else {
                // a[m] > x
                l = m;
                // a[l'] > x && l < l' < r
            }
            // a[l] > x && a[r] <= x && l < r
        }
        // r - l == 1 && a[l] > x && a[r] <= x
        return r;
    }

    // Pred: a - sorted non-ascending && (l == 0 && r == a.length || l < r && a[l] > x && a[r] <= x)
    // Post: a.length == 0 && R = 0 ||
    //       a.length > 0 &&
    //       (a[a.length - 1] > x && R = a.length || a[R] <= x && R - min available && l <= R < r)
    public static int recursiveSearch(final int x, final int[] a, final int l, final int r) {
        if (a.length == 0) {
            return 0;
        }
        // a.length > 0 && l < r
        if (a[l] <= x) {
            return l;
        }
        // a[l] > x
        if (r - l == 1) {
            return r;
        }
        // a[l] > x && a[r] <= x && l + 1 < r
        int m = (l + r) / 2;
        // l < m < r
        if (a[m] <= x) {
            // a[m] <= x
            // a[r'] <= x && l < r' < r
            return recursiveSearch(x, a, l, m);
        } else {
            // a[m] > x
            // a[l'] > x && l < l' < r
            return recursiveSearch(x, a, m, r);
        }
    }
}
