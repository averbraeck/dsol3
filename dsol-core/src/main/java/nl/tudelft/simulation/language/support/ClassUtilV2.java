package nl.tudelft.simulation.language.support;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * ClassUtilV2 class. Inclused a couple of reflection methods that are added in
 * dsol 2.1. This class can be removed as soon as dsol-devs is part of dsol 2.1.
 * <p>
 * Copyright (c) 2002-2009 Delft University of Technology, Jaffalaan 5, 2628 BX
 * Delft, the Netherlands. All rights reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/">
 * www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is"
 * and any express or implied warranties, including, but not limited to, the
 * implied warranties of merchantability and fitness for a particular purpose
 * are disclaimed. In no event shall the copyright holder or contributors be
 * liable for any direct, indirect, incidental, special, exemplary, or
 * consequential damages (including, but not limited to, procurement of
 * substitute goods or services; loss of use, data, or profits; or business
 * interruption) however caused and on any theory of liability, whether in
 * contract, strict liability, or tort (including negligence or otherwise)
 * arising in any way out of the use of this software, even if advised of the
 * possibility of such damage.
 * 
 * @version Oct 17, 2009 <br>
 * @author <a href="http://tudelft.nl/mseck">Mamadou Seck</a><br>
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a><br>
 */
public final class ClassUtilV2
{
    /**
     * private constructor -- should not be called.
     */
    private ClassUtilV2()
    {
        // unreacheable code
    }

    /**
     * gets all the fields of a class (public, protected, package, and private)
     * and adds the result to the return value.
     * 
     * @param clazz the class
     * @param result the resulting set
     * @return the set of fields including all fields of the field clazz
     */
    public static Set<Field> getAllFields(final Class<?> clazz, final Set<Field> result)
    {
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++)
        {
            result.add(fields[i]);
        }
        if (clazz.getSuperclass() != null)
        {
            return ClassUtilV2.getAllFields(clazz.getSuperclass(), result);
        }
        return result;
    }

    /**
     * gets all the fields of a class (public, protected, package, and private).
     * 
     * @param clazz the class
     * @return all fields of the class
     */
    public static Set<Field> getAllFields(final Class<?> clazz)
    {
        Set<Field> fieldSet = new HashSet<Field>();
        return ClassUtilV2.getAllFields(clazz, fieldSet);
    }

}
