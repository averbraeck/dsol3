/*
 * @(#) BeanUtils.java Apr 15, 2004 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.introspection.beans;

import java.beans.PropertyDescriptor;

/**
 * Utility class for bean tests.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a
 *         href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels
 *         Lang </a><a href="http://www.peter-jacobs.com/index.htm">Peter
 *         Jacobs </a>
 * @version 1.1 Apr 15, 2004
 * @since 1.5
 */

public class BeanUtils extends Object
{
    /**
     * resolves whether the bean is null
     * 
     * @param bean A bean instance.
     * @param pd A PropertyDescriptor for the property to be examined.
     * @return True, if the value of the property described by 'pd' for the
     *         instance 'bean' is null indeed. Returns false otherwise.
     */
    public static boolean isNull(final Object bean, final PropertyDescriptor pd)
    {
        Object result = null;
        try
        {
            result = pd.getReadMethod().invoke(bean, new Object[0]);
        } catch (Exception e)
        {
            return true;
        }
        return (result == null);
    }
}