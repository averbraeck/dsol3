package nl.tudelft.simulation.introspection.beans;

import java.beans.PropertyDescriptor;

/**
 * Utility class for bean tests.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */

public class BeanUtils extends Object
{
    /**
     * resolves whether the bean is null
     * @param bean A bean instance.
     * @param pd A PropertyDescriptor for the property to be examined.
     * @return True, if the value of the property described by 'pd' for the instance 'bean' is null indeed. Returns
     *         false otherwise.
     */
    public static boolean isNull(final Object bean, final PropertyDescriptor pd)
    {
        Object result = null;
        try
        {
            result = pd.getReadMethod().invoke(bean, new Object[0]);
        }
        catch (Exception e)
        {
            return true;
        }
        return (result == null);
    }
}
