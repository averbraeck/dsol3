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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The FileContext as implementation of the Context interface.
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.3 2004-03-24
 * @since 1.5
 */
public class FileContext extends JVMContext implements Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** file links to the file. */
    private File file = null;
    
    /** the logger./ */
    private static Logger logger = LogManager.getLogger(FileContext.class);

    /**
     * constructs a new FileContext.
     * @param file the file to write to
     */
    public FileContext(final File file)
    {
        super();
        this.file = file;
    }

    /**
     * constructs a new FileContext.
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
            logger.warn("saving in FileContext failed", exception);
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
