package nl.tudelft.simulation.language.reflection;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import nl.tudelft.simulation.language.primitives.Primitive;

/**
 * A field descriptor represents the type of a class, instance, or local variable. It is a series of characters
 * generated by the grammar described at <a href =
 * "http://java.sun.com/docs/books/vmspec/2nd-edition/html/ClassFile.doc.html#1169"> The Java Virtual Machine
 * Specification </a>.
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
 * @author Peter Jacobs, Niels Lang, Alexander Verbraeck
 * @version $Revision: 1.2 $ $Date: 2009/10/21 07:32:43 $
 * @since 1.5
 */
public class FieldSignature implements Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the CAHCHe. */
    private static final Map<String, Class<?>> CACHE = new HashMap<String, Class<?>>();

    /** the value of the field descriptor. */
    private String value;

    /**
     * constructs a new FieldSignature.
     * @param value the value of the descriptor
     */
    public FieldSignature(final String value)
    {
        super();
        this.value = value;
    }

    /**
     * constructs a new FieldSignature.
     * @param clazz The class
     */
    public FieldSignature(final Class<?> clazz)
    {
        this(FieldSignature.toDescriptor(clazz));
    }

    /**
     * @return Returns the value of the field descriptor
     */
    public String getStringValue()
    {
        return this.value;
    }

    /**
     * @return Returns the value of the field descriptor
     * @throws ClassNotFoundException if the class cannot be found.
     */
    public Class<?> getClassValue() throws ClassNotFoundException
    {
        return FieldSignature.toClass(this.value);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return this.value;
    }

    /**
     * converts an array of fields to its descriptor
     * @param classes the classes to represent
     * @return String the descriptor String
     */
    public static final String toDescriptor(final Class<?>[] classes)
    {
        String result = "";
        for (int i = 0; i < classes.length; i++)
        {
            result = result + FieldSignature.toDescriptor(classes[i]);
        }
        return result;
    }

    /**
     * converts a field to its descriptor
     * @param clazz the clazz to represent
     * @return String the descriptor String
     */
    public static final String toDescriptor(final Class<?> clazz)
    {
        if (clazz.getName().startsWith("["))
        {
            return clazz.getName().replace('.', '/');
        }
        if (clazz.isPrimitive())
        {
            if (clazz.equals(int.class))
            {
                return "I";
            }
            if (clazz.equals(double.class))
            {
                return "D";
            }
            if (clazz.equals(boolean.class))
            {
                return "Z";
            }
            if (clazz.equals(char.class))
            {
                return "C";
            }
            if (clazz.equals(byte.class))
            {
                return "B";
            }
            if (clazz.equals(float.class))
            {
                return "F";
            }
            if (clazz.equals(long.class))
            {
                return "J";
            }
            if (clazz.equals(short.class))
            {
                return "S";
            }
            return "V";
        }
        return "L" + clazz.getName().replace('.', '/') + ";";
    }

    /**
     * converts a fieldDescriptor to its class representation
     * @param descriptor the descriptor
     * @return Class the class
     * @throws ClassNotFoundException on failure
     */
    public static final Class<?> toClass(final String descriptor) throws ClassNotFoundException
    {
        if (FieldSignature.CACHE.containsKey(descriptor))
        {
            return FieldSignature.CACHE.get(descriptor);
        }
        String className = descriptor;
        Class<?> result = null;
        int array = 0;
        while (className.charAt(array) == '[')
        {
            array++;
        }
        className = className.substring(array);
        if (className.startsWith("L"))
        {
            className = className.replaceAll("/", ".");
            className = className.substring(1, className.length() - 1);
            try
            {
                result = Class.forName(className);
            }
            catch (Exception exception)
            {
                result = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
            }
        }
        else
        {
            result = Primitive.forName(className);
        }
        if (result == null && !descriptor.startsWith("["))
        {
            // For some reason not all classes start with L and end with ;
            return FieldSignature.toClass("L" + descriptor + ";");
        }
        if (array == 0)
        {
            FieldSignature.CACHE.put(descriptor, result);
            return result;
        }
        try
        {
            int[] dimensions = new int[array];
            result = Array.newInstance(result, dimensions).getClass();
        }
        catch (Exception exception)
        {
            throw new ClassNotFoundException(result + " class not found");
        }
        FieldSignature.CACHE.put(descriptor, result);
        return result;
    }
}