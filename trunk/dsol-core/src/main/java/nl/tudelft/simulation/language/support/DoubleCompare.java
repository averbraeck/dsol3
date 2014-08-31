package nl.tudelft.simulation.language.support;

/**
 * DoubleCompare class. Compares two doubles except for the last two bits of the mantissa.
 * <p>
 * Copyright (c) 2002-2009 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
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
    /** masks out last 2 bits. */
    private static final long MASK_2 = 0x000ffffffffffffcL;

    /**
     * Constructor that should not be used.
     */
    private DoubleCompare()
    {
        // unreachable code
    }

    /**
     * Fuzzy compare of two double variables. When they only differ in the last two bits of the mantissa, report bakc
     * that they are equal.
     * @param d1 the first double to compare
     * @param d2 the seond double to compare
     * @return 0 if (almost) equal, -1 of d1 < d2, and 1 if d1 > d2.
     */
    public static int compare(final double d1, final double d2)
    {
        double diff = d1 - d2;
        if (diff == 0)
        {
            return 0;
        }
        // keep everything except the last two bits of the mantissa
        double dd1 = Double.longBitsToDouble(Double.doubleToLongBits(d1) & DoubleCompare.MASK_2);
        double dd2 = Double.longBitsToDouble(Double.doubleToLongBits(d2) & DoubleCompare.MASK_2);
        if (dd1 - dd2 == 0)
        {
            return 0;
        }
        if (diff > 0)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

}
