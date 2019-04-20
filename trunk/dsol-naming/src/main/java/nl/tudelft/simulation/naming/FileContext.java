package nl.tudelft.simulation.naming;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;

import org.djutils.logger.CategoryLogger;

/**
 * The FileContext as a file-based implementation of the Context interface.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class FileContext extends JVMContext implements Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** file links to the file. */
    private File file = null;

    /**
     * constructs a new FileContext.
     * @param file File; the file to write to
     */
    public FileContext(final File file)
    {
        super();
        this.file = file;
    }

    /**
     * constructs a new FileContext.
     * @param file File; the file to which to write
     * @param parent Context; the parent context
     * @param atomicName String; the atomicName
     */
    public FileContext(final File file, final Context parent, final String atomicName)
    {
        super(parent, atomicName);
        this.file = file;
    }

    /**
     * saves this object to file
     * @throws NamingException on ioException
     */
    private synchronized void save() throws NamingException
    {
        try
        {
            FileContext clone = (FileContext) this.clone();
            clone.listeners.clear();
            ObjectOutputStream stream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(this.file)));
            stream.writeObject(this);
            stream.close();
        }
        catch (Exception exception)
        {
            CategoryLogger.always().warn(exception, "saving in FileContext failed");
            throw new NamingException(exception.getMessage());
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void bind(final Name name, final Object value) throws NamingException
    {
        super.bind(name, value);
        this.save();
    }

    /** {@inheritDoc} */
    @Override
    public void bind(final String name, final Object value) throws NamingException
    {
        super.bind(name, value);
        this.save();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized Context createSubcontext(final Name name) throws NamingException
    {
        Context result = super.createSubcontext(name);
        this.save();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public Context createSubcontext(final String arg0) throws NamingException
    {
        Context result = super.createSubcontext(arg0);
        this.save();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void destroySubcontext(final Name arg0) throws NamingException
    {
        super.destroySubcontext(arg0);
        this.save();
    }

    /** {@inheritDoc} */
    @Override
    public void destroySubcontext(final String arg0) throws NamingException
    {
        super.destroySubcontext(arg0);
        this.save();
    }

    /** {@inheritDoc} */
    @Override
    public void rebind(final Name name, final Object value) throws NamingException
    {
        super.rebind(name, value);
        this.save();
    }

    /** {@inheritDoc} */
    @Override
    public void rebind(final String name, final Object value) throws NamingException
    {
        super.rebind(name, value);
        this.save();
    }

    /** {@inheritDoc} */
    @Override
    public Object removeFromEnvironment(final String arg0) throws NamingException
    {
        Object result = super.removeFromEnvironment(arg0);
        this.save();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void rename(final Name nameOld, final Name nameNew) throws NamingException
    {
        super.rename(nameOld, nameNew);
        this.save();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void rename(final String nameOld, final String nameNew) throws NamingException
    {
        super.rename(nameOld, nameNew);
        this.save();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void unbind(final Name name) throws NamingException
    {
        super.unbind(name);
        this.save();
    }

    /** {@inheritDoc} */
    @Override
    public void unbind(final String name) throws NamingException
    {
        super.unbind(name);
        this.save();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized Object clone() throws CloneNotSupportedException
    {
        FileContext clone = new FileContext(this.file);
        Map<String, Object> elementsMap = new HashMap<String, Object>(this.elements);
        for (Iterator<String> i = elementsMap.keySet().iterator(); i.hasNext();)
        {
            String key = i.next();
            Object value = elementsMap.get(key);
            if (value instanceof JVMContext)
            {
                JVMContext item = (JVMContext) value;
                value = item.clone();
            }
            elementsMap.put(key, value);
        }
        clone.elements = elementsMap;
        return clone;
    }
}
