package nl.tudelft.simulation.language.support;

/**
 * DoubleCompare class. Compares two doubles except for the last two bits of the mantissa.
 * <p>
 * Copyright (c) 2002-2018  Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version Oct 17, 2009 <br>
 * @author <a href="http://tudelft.nl/mseck">Mamadou Seck</a><br>
 */
public final class DoubleCompare
{
    /**
     * Constructor that should not be used.
     */
    private DoubleCompare()
    {
        // unreachable code
    }

    /**
     * Fuzzy compare of two double variables. When they differ less than 4 ulps, that they are equal.
     * @param d1 the first double to compare
     * @param d2 the second double to compare
     * @return 0 if (almost) equal, -1 of d1 &lt; d2, and 1 if d1 &gt; d2.
     */
    @SuppressWarnings("checkstyle:needbraces")
    public static int compare(final double d1, final double d2)
    {
        double diff = d1 - d2;
        if (Math.abs(diff) == 0.0) // catches -0.0 and 0.0
            return 0;

        // From
        // long thisBits = Double.doubleToLongBits(d1); long anotherBits = Double.doubleToLongBits(d2);
        // thisBits == anotherBits => 0 : Values are equal
        // thisBits < anotherBits => -1 : (-0.0, 0.0) or (!NaN, NaN)
        // thisBits < anotherBits => +1 : (0.0, -0.0) or (NaN, !NaN)

        if (Double.isNaN(d1))
            return Double.isNaN(d2) ? 0 : 1;
        if (Double.isNaN(d2))
            return -1;
        if (Double.isInfinite(d1))
            return Double.isInfinite(d2) ? 0 : d1 > 0 ? -1 : 1;
        if (Double.isInfinite(d2))
            return d2 > 0 ? 1 : -1;

        if (diff > 0)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    public static void main(final String[] args)
    {
        cmp(1.0, 1.0);
        cmp(-1.0, 1.0);
        cmp(1.0, -1.0);
        cmp(0.0, 0.0);
        cmp(1.0, 2.0);
        cmp(2.0, 1.0);
        cmp(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        cmp(Double.NEGATIVE_INFINITY, Double.NaN);
        cmp(Double.NaN, Double.NaN);
        cmp(Double.NaN, 2.0);
    }

    private static void cmp(double a, double b)
    {
        System.out.println("compare(" + a + ", " + b + ") => " + compare(a, b));
    }

}
