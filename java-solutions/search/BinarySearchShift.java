package search;


public class BinarySearchShift {
    public static void main(final String[] args) {
        int[] a = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            a[i] = Integer.parseInt(args[i]);
        }
        System.out.println(iterativusSearch(a));
    }

    // Pred: a - sorted non-ascending and cyclically shifted
    // Post: a.length == 0 && R = 0 ||
    //       a.length > 0 &&
    //       for i in 1..R-1 a[i] >= a[i - 1] && a[R - 1] <= a[R] && for i in R + 1..n - 1 a[i] >= a[i - 1]
    public static int iterativusSearch(final int[] a) {
        int n = a.length;
        if (n == 0) {
            return 0;
        }
        // n > 0
        int l = -1, r = n, m;
        // a[l] > a[n - 1] && a[r] <= a[n - 1] || l == -1 && r == n
        while (r - l > 1) {
            // a[l] > a[n - 1] && a[r] <= a[n - 1] && r - l > 1
            m = (l + r) / 2;
            // l < m < r
            if (a[m] <= a[n - 1]) {
                // a[m] <= a[n - 1]
                r = m;
                // a[r'] <= a[n - 1] && l < r' < r
            } else {
                // a[m] > a[n - 1]
                l = m;
                // a[l'] > a[n - 1] && l < l' < r
            }
            // a[l] > a[n - 1] && a[r] <= a[n - 1] && l < r
        }
        // r - l == 1 && a[l] > a[n - 1] && a[r] <= a[n - 1]
        // a[l] - max
        // for i in 1..l a[i] >= a[i - 1] && a[l] <= a[l + 1] && for i in l + 2..n - 1 a[i] >= a[i - 1]
        return l + 1;
    }
}