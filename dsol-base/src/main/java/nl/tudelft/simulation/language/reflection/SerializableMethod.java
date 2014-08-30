package nl.tudelft.simulation.language.reflection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * A SerializableMethod.
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
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2009/10/21 07:32:43 $
 * @since 1.5
 */
public class SerializableMethod implements Serializable
{
    /** the method to use. */
    private Method method = null;

    /**
     * constructs a new SerializableMethod.
     * @param method the method
     */
    public SerializableMethod(final Method method)
    {
        super();
        this.method = method;
    }

    /**
     * constructs a new SerializableMethod.
     * @param clazz the clazz this field is instance of
     * @param methodName the name of the method
     * @param parameterTypes The parameterTypes of the method
     * @throws NoSuchMethodException whenever the method is not defined in clazz
     */
    public SerializableMethod(final Class<?> clazz, final String methodName, final Class<?>[] parameterTypes)
            throws NoSuchMethodException
    {
        this.method = ClassUtil.resolveMethod(clazz, methodName, parameterTypes);
    }

    /**
     * deserializes the field
     * @return the Field
     */
    public Method deSerialize()
    {
        return this.method;
    }

    /**
     * writes a serializable method to stream
     * @param out the outputstream
     * @throws IOException on IOException
     */
    private void writeObject(final ObjectOutputStream out) throws IOException
    {
        try
        {
            out.writeObject(this.method.getDeclaringClass());
            out.writeObject(this.method.getName());
            out.writeObject(new MethodSignature(this.method));
        }
        catch (Exception exception)
        {
            throw new IOException(exception.getMessage());
        }
    }

    /**
     * reads a serializable method from stream
     * @param in the inputstream
     * @throws IOException on IOException
     */
    private void readObject(final java.io.ObjectInputStream in) throws IOException
    {
        try
        {
            Class<?> declaringClass = (Class<?>) in.readObject();
            String methodName = (String) in.readObject();
            MethodSignature signature = (MethodSignature) in.readObject();
            this.method = ClassUtil.resolveMethod(declaringClass, methodName, signature.getParameterTypes());
        }
        catch (Exception exception)
        {
            throw new IOException(exception.getMessage());
        }
    }
}
