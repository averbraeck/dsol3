/*
 * @(#)JVMContext.java Feb 1, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.naming;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.naming.Binding;
import javax.naming.CompoundName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.event.EventContext;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingListener;

import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventProducerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.logger.Logger;

/**
 * The JVMContext as implementation of the Context interface. The JVM context is
 * an in-memory context implementation
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">
 * www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a> <br>
 *         <a href="mailto:nlang@fbk.eur.nl">Niels Lang </a>
 * @version 1.5 2004-03-24
 * @since 1.5
 */
public class JVMContext extends EventProducer implements EventContext,
        EventProducerInterface, Serializable
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** NUMBER_CHANGED_EVENT is fired whenever the number of children changes */
    public static final EventType NUMBER_CHANGED_EVENT = new EventType(
            "Number changed");

    /** CHILD_ADDED_EVENT is fired whenever a child is added */
    public static final EventType CHILD_ADDED_EVENT = new EventType(
            "Child added");

    /** CHILD_REMOVED_EVENT is fired whenever a child is removed */
    public static final EventType CHILD_REMOVED_EVENT = new EventType(
            "Child removed");

    /** the syntax of this parser */
    private static Properties syntax = new Properties();

    static
    {
        syntax.put("jndi.syntax.direction", "left_to_right");
        syntax.put("jndi.syntax.separator", "/");
        syntax.put("jndi.syntax.escape", "&");
        syntax.put("jndi.syntax.beginquote", "\"");
        syntax.put("jndi.syntax.ava", ",");
        syntax.put("jndi.syntax.typeval", "=");
    }

    /** the parent context */
    protected Context parent;

    /** the atomicName */
    private String atomicName;

    /** the children */
    protected Map<String, Object> elements = Collections
            .synchronizedMap(new TreeMap<String, Object>());

    /** the eventListeners */
    protected List<EventContextListenerRecord> eventListeners = Collections
            .synchronizedList(new ArrayList<EventContextListenerRecord>());

    /** the nameParser */
    protected NameParser parser = new MyParser(JVMContext.syntax);

    /**
     * constructs a new JVMContext.
     */
    public JVMContext()
    {
        this(null, "");
    }

    /**
     * constructs a new JVMContext
     * 
     * @param parent the parent context
     * @param atomicName the atomicname
     */
    public JVMContext(final Context parent, final String atomicName)
    {
        this.parent = parent;
        this.atomicName = atomicName;
    }

    /**
     * @see java.lang.Object#clone()
     */
    @Override
    public synchronized Object clone() throws CloneNotSupportedException
    {
        JVMContext clone = new JVMContext();
        Map<String, Object> elementsMap = new HashMap<String, Object>(
                this.elements);
        for (String key : elementsMap.keySet())
        {
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

    /**
     * Returns true when this context is not root itself and name starts with
     * '/'
     * 
     * @param name the name
     * @return boolean
     * @throws NamingException on parse failure
     */
    private boolean isRootForwardable(final Name name) throws NamingException
    {
        return (this.parent != null && name.startsWith(this.parser.parse(syntax
                .getProperty("jndi.syntax.separator"))));
    }

    /**
     * returns the root of this context
     * 
     * @return Context the root
     * @throws NamingException on lookup exception
     */
    private Context getRoot() throws NamingException
    {
        return (Context) lookup("");
    }

    /**
     * makes the name relative
     * 
     * @param name the name
     * @return Name
     * @throws NamingException on parse failure
     */
    private Name makeRelative(final Name name) throws NamingException
    {
        if (name.startsWith(this.parser.parse(syntax
                .getProperty("jndi.syntax.separator"))))
        {
            return name.getSuffix(1);
        }
        return name;
    }

    /**
     * @see javax.naming.Context#lookup(Name)
     */
    public synchronized Object lookup(final Name name) throws NamingException
    {
        // Handle absolute path
        if (isRootForwardable(name))
        {
            return getRoot().lookup(name);
        }

        // Handle root context lookup
        if (name.size() == 0 && this.parent == null)
        {
            return this;
        }
        if (name.size() == 0)
        {
            return this.parent.lookup(name);
        }

        Name relativeName = makeRelative(name);

        // Check and handle delegation
        if (relativeName.size() > 1)
        {
            return ((Context) lookup(relativeName.get(0))).lookup(relativeName
                    .getSuffix(1));
        }

        // Lookup locally
        if (!this.elements.containsKey(relativeName.toString()))
        {
            throw new NamingException(relativeName + " not found.");
        }
        return this.elements.get(relativeName.toString());
    }

    /**
     * @see javax.naming.Context#lookup(String)
     */
    public Object lookup(final String arg0) throws NamingException
    {
        return lookup(this.parser.parse(arg0));
    }

    /**
     * @see javax.naming.Context#bind(Name, Object)
     */
    public synchronized void bind(final Name name, final Object value)
            throws NamingException
    {
        if (isRootForwardable(name))
        {
            getRoot().bind(name, value);
            return;
        }
        Name relativeName = makeRelative(name);
        if (relativeName.size() > 1)
        {
            ((Context) this.lookup(relativeName.get(0))).bind(relativeName
                    .getSuffix(1), value);
        } else
        {
            this.elements.put(relativeName.get(0), value);
            fireEvent(new Event(NUMBER_CHANGED_EVENT, this, new Integer(
                    this.elements.size())));
            fireEvent(new Event(CHILD_ADDED_EVENT, this, value));
            fireContextEvent(true, this.getNameInNamespace()
                    + syntax.getProperty("jndi.syntax.separator")
                    + relativeName, value);
        }
    }

    /**
     * @see javax.naming.Context#bind(String, Object)
     */
    public void bind(final String name, final Object value)
            throws NamingException
    {
        bind(this.parser.parse(name), value);
    }

    /**
     * @see javax.naming.Context#rebind(Name, Object)
     */
    public void rebind(final Name name, final Object value)
            throws NamingException
    {
        this.bind(name, value);
    }

    /**
     * @see javax.naming.Context#rebind(String, Object)
     */
    public void rebind(final String name, final Object value)
            throws NamingException
    {
        this.bind(name, value);
    }

    /**
     * @see javax.naming.Context#unbind(Name)
     */
    public synchronized void unbind(final Name name) throws NamingException
    {
        if (isRootForwardable(name))
        {
            getRoot().unbind(name);
            return;
        }
        Name relativeName = makeRelative(name);
        if (relativeName.size() > 1)
        {
            ((Context) this.lookup(relativeName.get(0))).unbind(relativeName
                    .getSuffix(1));
        } else
        {
            Object old = this.elements.get(relativeName.get(0));
            this.elements.remove(relativeName.get(0));
            fireEvent(new Event(NUMBER_CHANGED_EVENT, this, new Integer(
                    this.elements.size())));
            fireEvent(new Event(CHILD_REMOVED_EVENT, this, old));
            fireContextEvent(false, this.getNameInNamespace()
                    + syntax.getProperty("jndi.syntax.separator")
                    + relativeName, old);
        }
    }

    /**
     * @see javax.naming.Context#unbind(String)
     */
    public void unbind(final String name) throws NamingException
    {
        unbind(this.parser.parse(name));
    }

    /**
     * @see javax.naming.Context#rename(Name, Name)
     */
    public void rename(final Name nameOld, final Name nameNew)
            throws NamingException
    {
        rename(nameOld.toString(), nameNew.toString());
    }

    /**
     * @see javax.naming.Context#rename(String, String)
     */
    public synchronized void rename(final String nameOld, final String nameNew)
            throws NamingException
    {
        if (!this.elements.containsKey(nameOld))
        {
            throw new NamingException("Old name not found. Rename"
                    + " operation canceled.");
        }
        Object value = this.elements.get(nameOld);
        this.elements.remove(nameOld);
        this.elements.put(nameNew, value);
    }

    /**
     * @see javax.naming.Context#list(Name)
     */
    public NamingEnumeration<NameClassPair> list(final Name name)
    {
        return this.list(name.toString());
    }

    /**
     * @see javax.naming.Context#list(String)
     */
    public NamingEnumeration<NameClassPair> list(final String name)
    {
        if (name == null)
        {
            Logger.info(this, "list", "name==null");
        }
        return new NamingList<NameClassPair>(true);
    }

    /**
     * @see javax.naming.Context#listBindings(Name)
     */
    public NamingEnumeration<Binding> listBindings(final Name name)
    {
        if (name == null)
        {
            Logger.info(this, "listBindings", "name==null");
        }
        return new NamingList<Binding>(false);
    }

    /**
     * @see javax.naming.Context#listBindings(String)
     */
    public NamingEnumeration<Binding> listBindings(final String name)
    {
        if (name == null)
        {
            Logger.info(this, "listBindings", "name==null");
        }
        return new NamingList<Binding>(false);
    }

    /**
     * @see javax.naming.Context#destroySubcontext(Name)
     */
    public void destroySubcontext(final Name name) throws NamingException
    {
        this.unbind(name);
    }

    /**
     * @see javax.naming.Context#destroySubcontext(String)
     */
    public void destroySubcontext(final String name) throws NamingException
    {
        this.unbind(name);
    }

    /**
     * @see javax.naming.Context#createSubcontext(Name)
     */
    public synchronized Context createSubcontext(final Name name)
            throws NamingException
    {
        if (name.size() == 1)
        {
            String subName = name.get(0);
            Context newContext = new JVMContext(this, subName);
            this.bind(subName, newContext);
            return newContext;
        }
        Context c = (Context) this.lookup(name.get(0));
        return c.createSubcontext(name.getSuffix(1));
    }

    /**
     * @see javax.naming.Context#createSubcontext(String)
     */
    public Context createSubcontext(final String arg0) throws NamingException
    {
        return createSubcontext(this.parser.parse(arg0));
    }

    /**
     * @see javax.naming.Context#lookupLink(Name)
     */
    public Object lookupLink(final Name name)
    {
        return this.elements.get(name.toString());
    }

    /**
     * @see javax.naming.Context#lookupLink(String)
     */
    public Object lookupLink(final String name) throws NamingException
    {
        return lookup(name);
    }

    /**
     * @see javax.naming.Context#getNameParser(Name)
     */
    public NameParser getNameParser(final Name name)
    {
        if (name == null)
        {
            Logger.info(this, "getNameParser", "name==null");
        }
        return this.parser;
    }

    /**
     * @see javax.naming.Context#getNameParser(String)
     */
    public NameParser getNameParser(final String name)
    {
        if (name == null)
        {
            Logger.info(this, "getNameParser", "name==null");
        }
        return this.parser;
    }

    /**
     * @see javax.naming.Context#composeName(Name, Name)
     */
    public Name composeName(final Name arg0, final Name arg1)
            throws NamingException
    {
        throw new NamingException("composeName " + arg0 + ", " + arg1
                + " is not supported.");
    }

    /**
     * @see javax.naming.Context#composeName(String, String)
     */
    public String composeName(final String arg0, final String arg1)
            throws NamingException
    {
        throw new NamingException("composeName " + arg0 + ", " + arg1
                + " is not supported.");
    }

    /**
     * @see javax.naming.Context#addToEnvironment(String, Object)
     */
    public Object addToEnvironment(final String arg0, final Object arg1)
            throws NamingException
    {
        throw new NamingException("addToEnvironment " + arg0 + ", " + arg1
                + " is not supported.");
    }

    /**
     * @see javax.naming.Context#removeFromEnvironment(String)
     */
    public Object removeFromEnvironment(final String arg0)
            throws NamingException
    {
        throw new NamingException("removeFromEnvironment " + arg0
                + " is not supported.");
    }

    /**
     * @see javax.naming.Context#getEnvironment()
     */
    public Hashtable<?, ?> getEnvironment() throws NamingException
    {
        throw new NamingException("Not supported.");
    }

    /**
     * @see javax.naming.Context#close()
     */
    public void close()
    {
        // We don't do anything on close
    }

    /**
     * @see javax.naming.Context#getNameInNamespace()
     */
    public synchronized String getNameInNamespace() throws NamingException
    {
        if (this.parent != null)
        {
            return (this.parent.getNameInNamespace()
                    + syntax.get("jndi.syntax.separator") + this.atomicName);
        }
        return this.atomicName;
    }

    /**
     * @see javax.naming.event.EventContext #addNamingListener(Name, int,
     *      NamingListener)
     */
    public void addNamingListener(final Name target, final int scope,
            final NamingListener l)
    {
        this.eventListeners
                .add(new EventContextListenerRecord(target, scope, l));
    }

    /**
     * @see javax.naming.event.EventContext #addNamingListener(String, int,
     *      NamingListener)
     */
    public void addNamingListener(final String target, final int scope,
            final NamingListener l) throws NamingException
    {
        addNamingListener(this.parser.parse(target), scope, l);
    }

    /**
     * @see javax.naming.event.EventContext
     *      #removeNamingListener(NamingListener)
     */
    public synchronized void removeNamingListener(final NamingListener l)
    {
        EventContextListenerRecord removable = null;
        for (EventContextListenerRecord current : this.eventListeners)
        {
            if (current.getListener().equals(l))
            {
                removable = current;
                break;
            }
        }
        if (removable != null)
        {
            this.eventListeners.remove(removable);
        }
    }

    /**
     * @see javax.naming.event.EventContext#targetMustExist()
     */
    public boolean targetMustExist()
    {
        return false;
    }

    /**
     * fires a contextEvent
     * 
     * @param isAddition addition
     * @param name the name
     * @param value the value
     * @throws NamingException on failure
     */
    private void fireContextEvent(final boolean isAddition, final String name,
            final Object value) throws NamingException
    {
        fireContextEvent(isAddition, this.parser.parse(name), value);
    }

    /**
     * fires a contextEvent
     * 
     * @param isAddition addition
     * @param name the name
     * @param value the value
     */
    private synchronized void fireContextEvent(final boolean isAddition,
            final Name name, final Object value)
    {
        for (EventContextListenerRecord record : this.eventListeners)
        {
            int scope = record.getScope();
            NamingEvent namingEvent = null;
            if (isAddition)
            {
                namingEvent = new NamingEvent(this, NamingEvent.OBJECT_ADDED,
                        new Binding(name.toString(), value), null, null);
            } else
            {
                namingEvent = new NamingEvent(this, NamingEvent.OBJECT_REMOVED,
                        null, new Binding(name.toString(), value), null);
            }
            if (name.equals(record.getTarget())
                    || scope == EventContext.SUBTREE_SCOPE)
            {
                namingEvent.dispatch(record.getListener());
                continue;
            }
            if (scope == EventContext.ONELEVEL_SCOPE)
            {
                // (Wrong) assumption that this is the root context
                if (record.getTarget().size() == 1)
                {
                    namingEvent.dispatch(record.getListener());
                }
                continue;
            }
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        try
        {
            return "JVMContext: " + this.getNameInNamespace() + " ";
        } catch (Exception exception)
        {
            return super.toString();
        }
    }

    /**
     * The EventContextListenerRecord
     */
    private class EventContextListenerRecord
    {
        /** target name to which a subscription is made */
        private Name target;

        /** the scope */
        private int scope;

        /** the listener */
        private NamingListener listener;

        /**
         * constructs a new EventContextListenerRecord
         * 
         * @param target the target
         * @param scope the scope
         * @param listener the listener
         */
        public EventContextListenerRecord(final Name target, final int scope,
                final NamingListener listener)
        {
            this.target = target;
            this.scope = scope;
            this.listener = listener;
        }

        /**
         * returns the listener
         * 
         * @return NamingListener listener
         */
        public NamingListener getListener()
        {
            return this.listener;
        }

        /**
         * gets scope
         * 
         * @return Returns the scope.
         */
        public int getScope()
        {
            return this.scope;
        }

        /**
         * gets target
         * 
         * @return Returns the target.
         */
        public Name getTarget()
        {
            return this.target;
        }

    }

    /**
     * The NamingList class
     */
    private class NamingList<T extends NameClassPair> extends ArrayList<T>
            implements NamingEnumeration<T>
    {
        /** The default serial version UID for serializable classes */
        private static final long serialVersionUID = 1L;
        
        /** the iterator */
        private Iterator<T> myIterator = null;

        /**
         * constructs a new NamingList
         * 
         * @param classList isClassList
         */
        @SuppressWarnings("unchecked")
        public NamingList(final boolean classList)
        {
            for (String currentKey : JVMContext.this.elements.keySet())
            {
                if (classList)
                {
                    this.add((T) new NameClassPair(currentKey,
                            JVMContext.this.elements.get(currentKey).getClass()
                                    .toString()));
                } else
                {
                    this.add((T) new Binding(currentKey,
                            JVMContext.this.elements.get(currentKey)));
                }
            }
        }

        /**
         * @see javax.naming.NamingEnumeration#close()
         */
        public void close()
        {
            this.myIterator = null;
        }

        /**
         * @see java.util.Enumeration#hasMoreElements()
         */
        public boolean hasMoreElements()
        {
            if (this.myIterator == null)
            {
                this.myIterator = this.iterator();
            }
            boolean hasNext = this.myIterator.hasNext();
            if (!hasNext)
            {
                this.myIterator = null;
                return false;
            }
            return true;
        }

        /**
         * @see java.util.Enumeration#nextElement()
         */
        public T nextElement()
        {
            if (this.myIterator == null)
            {
                this.myIterator = this.iterator();
            }
            return this.myIterator.next();
        }

        /**
         * @see javax.naming.NamingEnumeration#hasMore()
         */
        public boolean hasMore()
        {
            return hasMoreElements();
        }

        /**
         * @see javax.naming.NamingEnumeration#next()
         */
        public T next()
        {
            return nextElement();
        }
    }

    /**
     * A default name parser
     */
    private class MyParser implements NameParser, Serializable
    {
        /** The default serial version UID for serializable classes */
        private static final long serialVersionUID = 1L;

        /** the syntax */
        private Properties syntaxProperties = null;

        /**
         * constructs a new MyParser
         * 
         * @param syntax the syntax properties
         */
        public MyParser(final Properties syntax)
        {
            this.syntaxProperties = syntax;
        }

        /**
         * @see javax.naming.NameParser#parse(String)
         */
        public Name parse(final String name) throws NamingException
        {
            Name result = new CompoundName(name, this.syntaxProperties);
            if (result.size() > 0 && result.get(0).equals(".")
                    && JVMContext.this.parent != null)
            {
                result = result.getSuffix(1);
            }
            return result;
        }
    }
}