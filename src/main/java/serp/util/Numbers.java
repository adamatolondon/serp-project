package serp.util;

/**
 * Number utilities.
 *
 * @author Abe White
 */
public class Numbers {
    private static final Integer INT_NEGONE = Integer.valueOf(-1);
    private static final Long LONG_NEGONE = Long.valueOf(-1);
    private static final Integer[] INTEGERS = new Integer[50];
    private static final Long[] LONGS = new Long[50];

    static {
        for (int i = 0; i < INTEGERS.length; i++)
            INTEGERS[i] = Integer.valueOf(i);
        for (int i = 0; i < LONGS.length; i++)
            LONGS[i] = Long.valueOf(i);
    }

    /**
     * Return the wrapper for the given number, taking advantage of cached
     * common values.
     * 
     * @param n the primitive integer value
     * @return the wrapper for the given number
     */
    public static Integer valueOf(int n) {
        if (n == -1)
            return INT_NEGONE;
        if (n >= 0 && n < INTEGERS.length)
            return INTEGERS[n];
        return Integer.valueOf(n);
    }

    /**
     * Return the wrapper for the given number, taking advantage of cached
     * common values.
     * 
     * @param n the primitive long value
     * @return the wrapper for the given number
     */
    public static Long valueOf(long n) {
        if (n == -1)
            return LONG_NEGONE;
        if (n >= 0 && n < LONGS.length)
            return LONGS[(int) n];
        return Long.valueOf(n);
    }
}
