/*
 * @(#) ClassDescriptor.java $Date: 2007/01/07 05:00:12 $ Copyright (c)
 * 2002-2005 Delft University of Technology Jaffalaan 5, 2628 BX Delft, the
 * Netherlands. All rights reserved. This software is proprietary information of
 * Delft University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.util.HashMap;
import java.util.Map;

/**
 * A ClassDescriptor <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $ $Date: 2007/01/07 05:00:12 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class ClassDescriptor
{
    /** the repository which caches descriptors */
    private static final Map<Class<?>, ClassDescriptor> CACHE = new HashMap<Class<?>, ClassDescriptor>();

    /** the constantPool */
    private Constant[] constantPool = null;

    /** the localVariables */
    private Map<AccessibleObject, MethodDescriptor> methods = new HashMap<AccessibleObject, MethodDescriptor>();

    /** the javaClass we are reading */
    private Class<?> javaClass = null;

    /**
     * returns the classDescriptor of this class
     * @param clazz the class the clazz to parse
     * @return ClassDescriptor the descriptor
     * @throws IOException on IO exception
     * @throws ClassNotFoundException if clazz cannot be found
     */
    public static ClassDescriptor get(final Class clazz) throws IOException, ClassNotFoundException
    {
        synchronized (CACHE)
        {
            if (CACHE.containsKey(clazz))
            {
                return CACHE.get(clazz);
            }
            ClassDescriptor classDescriptor = new ClassDescriptor(clazz);
            CACHE.put(clazz, classDescriptor);
            return classDescriptor;
        }
    }

    /**
     * constructs a new ClassDescriptor
     * @param javaClass the class to read
     * @throws IOException on IO - failure
     * @throws ClassNotFoundException on incomplete classPath
     */
    private ClassDescriptor(final Class javaClass) throws IOException, ClassNotFoundException
    {
        super();
        this.javaClass = javaClass;
        ClassLoader classLoader = javaClass.getClassLoader();
        if (classLoader == null)
        {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        this.readClass(new DataInputStream(classLoader.getResourceAsStream(javaClass.getName().replace('.', '/')
                + ".class")));
    }

    /**
     * returns the methodDescriptor of the method.
     * @param method the method to resolve.
     * @return its descriptor.
     */
    public MethodDescriptor getMethod(final AccessibleObject method)
    {
        return this.methods.get(method);
    }

    /**
     * returns the constantpool of a classfile
     * @return Constant[] the constantpool
     */
    public Constant[] getConstantPool()
    {
        return this.constantPool;
    }

    /**
     * returns the methods of this class
     * @return MethodDescriptor[]
     */
    public MethodDescriptor[] getMethods()
    {
        return this.methods.values().toArray(new MethodDescriptor[this.methods.size()]);
    }

    /** ** PRIVATE PARSING METHODS *** */

    /**
     * reads the class
     * @param dataInput the dataInput
     * @throws IOException on failure
     * @throws ClassNotFoundException on incomplete classPath
     */
    private void readClass(final DataInput dataInput) throws IOException, ClassNotFoundException
    {
        // We skip the magic(u4) and version(u2,u2)
        dataInput.skipBytes(8);

        // Now we read the constantPool
        this.readConstantPool(dataInput);

        // We skip the accessFlag(u2), thisClass(u2),superClass(u2)
        dataInput.skipBytes(6);

        // We skip the interfaces
        int interfacesCount = dataInput.readUnsignedShort();
        dataInput.skipBytes(2 * interfacesCount);

        // We skip the fields
        // AttributeInfo has nameIndex(u2),length(u4)
        int fieldCount = dataInput.readUnsignedShort();
        for (int i = 0; i < fieldCount; i++)
        {
            // fieldInfo has accessFlag(u2),name(u2),descriptor(u2)
            dataInput.skipBytes(6);
            int attributeCount = dataInput.readUnsignedShort();
            for (int j = 0; j < attributeCount; j++)
            {
                // AttributeInfo has nameIndex(u2)
                dataInput.skipBytes(2);
                dataInput.skipBytes(dataInput.readInt());
            }
        }

        // Finally we read the methods
        int methodCount = dataInput.readUnsignedShort();
        for (int i = 0; i < methodCount; i++)
        {
            MethodDescriptor methodDescriptor = new MethodDescriptor(dataInput, this.constantPool);
            AccessibleObject method =
                    this.parseMethod(methodDescriptor.getName(), methodDescriptor.getMethodSignature()
                            .getParameterTypes());
            methodDescriptor.setMethod(method);
            this.methods.put(method, methodDescriptor);
        }
    }

    /**
     * reads the constantpool from file
     * @param dataInput the stream
     * @throws IOException on failure
     */
    private void readConstantPool(final DataInput dataInput) throws IOException
    {
        this.constantPool = new Constant[dataInput.readUnsignedShort()];

        /*
         * constant_pool[0] is unused by the compiler and may be used freely by the implementation.
         */
        for (int i = 1; i < this.constantPool.length; i++)
        {
            this.constantPool[i] = Constant.readConstant(this.constantPool, dataInput);

            if (this.constantPool[i] instanceof ConstantDouble || this.constantPool[i] instanceof ConstantLong)
            {
                /*
                 * Quote from the JVM specification: "All eight byte constants take up two spots in the constant pool.
                 * If this is the n'th byte in the constant pool, then the next item will be numbered n+2" Thus we have
                 * to increment the index counter.
                 */
                i++;
            }
        }
    }

    /**
     * parses a methodName and descriptor to an accessibleObject
     * @param methodName the name of the method
     * @param argumentClasses the argumentClasses
     * @return the AccessibleObject
     */
    private AccessibleObject parseMethod(final String methodName, final Class[] argumentClasses)
    {
        try
        {
            if (methodName.equals("<clinit>"))
            {
                return null;
            }
            if (!methodName.equals("<init>"))
            {
                return this.javaClass.getDeclaredMethod(methodName, argumentClasses);
            }
            return this.javaClass.getDeclaredConstructor(argumentClasses);
        }
        catch (Exception exception)
        {
            return null;
        }
    }
}