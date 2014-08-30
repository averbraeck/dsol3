/*
 * @(#) FileContext.java Oct 23, 2003 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
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

import nl.tudelft.simulation.logger.Logger;

/**
 * The FileContext as implementation of the Context interface.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.3 2004-03-24
 * @since 1.5
 */
public class FileContext extends JVMContext implements Serializable
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** file links to the file */
    private File file = null;

    /**
     * constructs a new FileContext
     * @param file the file to write to
     */
    public FileContext(final File file)
    {
        super();
        this.file = file;
    }

    /**
     * constructs a new FileContext
     * @param file the file to which to write
     * @param parent the parent context
     * @param atomicName the atomicName
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
            ObjectOutputStream stream =
                    new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(this.file)));
            stream.writeObject(this);
            stream.close();
        }
        catch (Exception exception)
        {
            Logger.warning(this, "save", exception);
            throw new NamingException(exception.getMessage());
        }
    }

    /**
     * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
     */
    @Override
    public void bind(final Name name, final Object value) throws NamingException
    {
        super.bind(name, value);
        this.save();
    }

    /**
     * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
     */
    @Override
    public void bind(final String name, final Object value) throws NamingException
    {
        super.bind(name, value);
        this.save();
    }

    /**
     * @see javax.naming.Context#createSubcontext(javax.naming.Name)
     */
    @Override
    public Context createSubcontext(final Name name) throws NamingException
    {
        Context result = super.createSubcontext(name);
        this.save();
        return result;
    }

    /**
     * @see javax.naming.Context#createSubcontext(java.lang.String)
     */
    @Override
    public Context createSubcontext(final String arg0) throws NamingException
    {
        Context result = super.createSubcontext(arg0);
        this.save();
        return result;
    }

    /**
     * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
     */
    @Override
    public void destroySubcontext(final Name arg0) throws NamingException
    {
        super.destroySubcontext(arg0);
        this.save();
    }

    /**
     * @see javax.naming.Context#destroySubcontext(java.lang.String)
     */
    @Override
    public void destroySubcontext(final String arg0) throws NamingException
    {
        super.destroySubcontext(arg0);
        this.save();
    }

    /**
     * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
     */
    @Override
    public void rebind(final Name name, final Object value) throws NamingException
    {
        super.rebind(name, value);
        this.save();
    }

    /**
     * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
     */
    @Override
    public void rebind(final String name, final Object value) throws NamingException
    {
        super.rebind(name, value);
        this.save();
    }

    /**
     * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
     */
    @Override
    public Object removeFromEnvironment(final String arg0) throws NamingException
    {
        Object result = super.removeFromEnvironment(arg0);
        this.save();
        return result;
    }

    /**
     * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
     */
    @Override
    public void rename(final Name nameOld, final Name nameNew) throws NamingException
    {
        super.rename(nameOld, nameNew);
        this.save();
    }

    /**
     * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
     */
    @Override
    public void rename(final String nameOld, final String nameNew) throws NamingException
    {
        super.rename(nameOld, nameNew);
        this.save();
    }

    /**
     * @see javax.naming.Context#unbind(javax.naming.Name)
     */
    @Override
    public void unbind(final Name name) throws NamingException
    {
        super.unbind(name);
        this.save();
    }

    /**
     * @see javax.naming.Context#unbind(java.lang.String)
     */
    @Override
    public void unbind(final String name) throws NamingException
    {
        super.unbind(name);
        this.save();
    }

    /**
     * @see java.lang.Object#clone()
     */
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