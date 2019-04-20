package nl.tudelft.simulation.naming;

import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.event.EventContext;
import javax.naming.event.NamingListener;
import javax.naming.spi.NamingManager;

import com.sun.naming.internal.ResourceManager;

/**
 * InitialEventContext class. This class is the starting context for performing naming operations.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/mseck">Mamadou Seck</a><br>
 */
@SuppressWarnings("restriction")
public class InitialEventContext implements EventContext
{
    /** the properties of the initialEventContext. */
    @SuppressWarnings("rawtypes")
    protected Hashtable myProps = null;

    /**
     * Field holding the result of calling NamingManager.getInitialContext(). It is set by getDefaultInitCtx() the first time
     * getDefaultInitCtx() is called. Subsequent invocations of getDefaultInitCtx() return the value of defaultInitCtx.
     */
    protected EventContext defaultInitCtx = null;

    /**
     * Field indicating whether the initial context has been obtained by calling NamingManager.getInitialContext(). If true, its
     * result is in <code>defaultInitCtx</code>.
     */
    protected boolean gotDefault = false;

    /**
     * Constructs an initial context with the option of not initializing it. This may be used by a constructor in a subclass
     * when the value of the environment parameter is not yet known at the time the <tt>InitialContext</tt> constructor is
     * called. The subclass's constructor will call this constructor, compute the value of the environment, and then call
     * <tt>init()</tt> before returning.
     * @param lazy true means do not initialize the initial context; false is equivalent to calling
     *            <tt>new InitialContext()</tt>
     * @throws NamingException if a naming exception is encountered
     * @see #init(Hashtable)
     * @since 1.5
     */
    protected InitialEventContext(final boolean lazy) throws NamingException
    {
        if (!lazy)
        {
            init(null);
        }
    }

    /**
     * Constructs an initial context. No environment properties are supplied. Equivalent to <tt>new InitialContext(null)</tt>.
     * @throws NamingException if a naming exception is encountered
     */
    public InitialEventContext() throws NamingException
    {
        init(null);
    }

    /**
     * Constructs an initial context using the supplied environment. Environment properties are discussed in the class
     * description.
     * <p>
     * This constructor will not modify <tt>environment</tt> or save a reference to it, but may save a clone.
     * @param environment Hashtable&lt;?,?&gt;; environment used to create the initial context. Null indicates an empty
     *            environment.
     * @throws NamingException if a naming exception is encountered
     */
    public InitialEventContext(final Hashtable<?, ?> environment) throws NamingException
    {
        if (environment != null)
        {
            this.init((Hashtable<?, ?>) environment.clone());
        }
        else
        {
            this.init(environment);
        }
    }

    /**
     * Initializes the initial context using the supplied environment. Environment properties are discussed in the class
     * description.
     * <p>
     * This method will modify <tt>environment</tt> and save a reference to it. The caller may no longer modify it.
     * @param environment Hashtable&lt;?,?&gt;; environment used to create the initial context. Null indicates an empty
     *            environment.
     * @throws NamingException if a naming exception is encountered
     * @since 1.5
     */
    protected void init(final Hashtable<?, ?> environment) throws NamingException
    {
        this.myProps = ResourceManager.getInitialEnvironment(environment);
        if (this.myProps.get(Context.INITIAL_CONTEXT_FACTORY) != null)
        {
            // user has specified initial context factory; try to get it
            getDefaultInitCtx();
        }
    }

    /**
     * returns the URL Scheme
     * @param str String; the string
     * @return URL
     */
    private static String getURLScheme(final String str)
    {
        int colonPosn = str.indexOf(':');
        int slashPosn = str.indexOf('/');
        if (colonPosn > 0 && (slashPosn == -1 || colonPosn < slashPosn))
        {
            return str.substring(0, colonPosn);
        }
        return null;
    }

    /**
     * Retrieves the initial context by calling NamingManager.getInitialContext() and cache it in defaultInitCtx. Set
     * <code>gotDefault</code> so that we know we've tried this before.
     * @return The non-null cached initial context.
     * @throws NamingException If a naming exception was encountered.
     */
    protected EventContext getDefaultInitCtx() throws NamingException
    {
        if (!this.gotDefault)
        {
            this.defaultInitCtx = (EventContext) NamingManager.getInitialContext(this.myProps);
            this.gotDefault = true;
        }
        if (this.defaultInitCtx == null)
        {
            throw new NoInitialContextException();
        }
        return this.defaultInitCtx;
    }

    /**
     * Retrieves a context for resolving the string name <code>name</code>. If <code>name</code> name is a URL string, then
     * attempt to find a URL context for it. If none is found, or if <code>name</code> is not a URL string, then return
     * <code>getDefaultInitCtx()</code>.
     * <p>
     * See getURLOrDefaultInitCtx(Name) for description of how a subclass should use this method.
     * @param name String; The non-null name for which to get the context.
     * @return A URL context for <code>name</code> or the cached initial context. The result cannot be null.
     * @throws NamingException on exception
     */
    protected Context getURLOrDefaultInitCtx(final String name) throws NamingException
    {
        if (NamingManager.hasInitialContextFactoryBuilder())
        {
            return getDefaultInitCtx();
        }
        String scheme = getURLScheme(name);
        if (scheme != null)
        {
            Context ctx = NamingManager.getURLContext(scheme, this.myProps);
            if (ctx != null)
            {
                return ctx;
            }
        }
        return getDefaultInitCtx();
    }

    /**
     * @param name Name; The non-null name for which to get the context.
     * @return A URL context for <code>name</code>
     * @throws NamingException In a naming exception is encountered.
     */
    protected Context getURLOrDefaultInitCtx(final Name name) throws NamingException
    {
        if (NamingManager.hasInitialContextFactoryBuilder())
        {
            return getDefaultInitCtx();
        }
        if (name.size() > 0)
        {
            String first = name.get(0);
            String scheme = getURLScheme(first);
            if (scheme != null)
            {
                Context ctx = NamingManager.getURLContext(scheme, this.myProps);
                if (ctx != null)
                {
                    return ctx;
                }
            }
        }
        return getDefaultInitCtx();
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#lookup(java.lang.String)
     */
    @Override
    public Object lookup(final String name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).lookup(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#lookup(javax.naming.Name)
     */
    @Override
    public Object lookup(final Name name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).lookup(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
     */
    @Override
    public void bind(final String name, final Object obj) throws NamingException
    {
        getURLOrDefaultInitCtx(name).bind(name, obj);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
     */
    @Override
    public void bind(final Name name, final Object obj) throws NamingException
    {
        getURLOrDefaultInitCtx(name).bind(name, obj);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
     */
    @Override
    public void rebind(final String name, final Object obj) throws NamingException
    {
        getURLOrDefaultInitCtx(name).rebind(name, obj);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
     */
    @Override
    public void rebind(final Name name, final Object obj) throws NamingException
    {
        getURLOrDefaultInitCtx(name).rebind(name, obj);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#unbind(java.lang.String)
     */
    @Override
    public void unbind(final String name) throws NamingException
    {
        getURLOrDefaultInitCtx(name).unbind(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#unbind(javax.naming.Name)
     */
    @Override
    public void unbind(final Name name) throws NamingException
    {
        getURLOrDefaultInitCtx(name).unbind(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
     */
    @Override
    public void rename(final String oldName, final String newName) throws NamingException
    {
        getURLOrDefaultInitCtx(oldName).rename(oldName, newName);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
     */
    @Override
    public void rename(final Name oldName, final Name newName) throws NamingException
    {
        getURLOrDefaultInitCtx(oldName).rename(oldName, newName);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#list(java.lang.String)
     */
    @Override
    public NamingEnumeration<NameClassPair> list(final String name) throws NamingException
    {
        return (getURLOrDefaultInitCtx(name).list(name));
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#list(javax.naming.Name)
     */
    @Override
    public NamingEnumeration<NameClassPair> list(final Name name) throws NamingException
    {
        return (getURLOrDefaultInitCtx(name).list(name));
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#listBindings(java.lang.String)
     */
    @Override
    public NamingEnumeration<Binding> listBindings(final String name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).listBindings(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#listBindings(javax.naming.Name)
     */
    @Override
    public NamingEnumeration<Binding> listBindings(final Name name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).listBindings(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#destroySubcontext(java.lang.String)
     */
    @Override
    public void destroySubcontext(final String name) throws NamingException
    {
        getURLOrDefaultInitCtx(name).destroySubcontext(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
     */
    @Override
    public void destroySubcontext(final Name name) throws NamingException
    {
        getURLOrDefaultInitCtx(name).destroySubcontext(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#createSubcontext(java.lang.String)
     */
    @Override
    public Context createSubcontext(final String name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).createSubcontext(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#createSubcontext(javax.naming.Name)
     */
    @Override
    public Context createSubcontext(final Name name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).createSubcontext(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#lookupLink(java.lang.String)
     */
    @Override
    public Object lookupLink(final String name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).lookupLink(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#lookupLink(javax.naming.Name)
     */
    @Override
    public Object lookupLink(final Name name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).lookupLink(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#getNameParser(java.lang.String)
     */
    @Override
    public NameParser getNameParser(final String name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).getNameParser(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#getNameParser(javax.naming.Name)
     */
    @Override
    public NameParser getNameParser(final Name name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).getNameParser(name);
    }

    /**
     * Composes the name of this context with a name relative to this context. Since an initial context may never be named
     * relative to any context other than itself, the value of the <tt>prefix</tt> parameter must be an empty name
     * (<tt>""</tt>).
     * @param name String; the name
     * @param prefix String; the prefix
     * @return String
     * @throws NamingException on exception
     */
    @Override
    public String composeName(final String name, final String prefix) throws NamingException
    {
        throw new NamingException("composeName " + name + ", " + prefix + " is not supported.");
    }

    /**
     * Composes the name of this context with a name relative to this context. Since an initial context may never be named
     * relative to any context other than itself, the value of the <tt>prefix</tt> parameter must be an empty name.
     * @param name Name; the name
     * @param prefix Name; the prefix
     * @return Name
     * @throws NamingException on exception
     */
    @Override
    public Name composeName(final Name name, final Name prefix) throws NamingException
    {
        throw new NamingException("composeName " + name + ", " + prefix + " is not supported.");
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context #addToEnvironment(java.lang.String, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object addToEnvironment(final String propName, final Object propVal) throws NamingException
    {
        this.myProps.put(propName, propVal);
        return getDefaultInitCtx().addToEnvironment(propName, propVal);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
     */
    @Override
    public Object removeFromEnvironment(final String propName) throws NamingException
    {
        this.myProps.remove(propName);
        return getDefaultInitCtx().removeFromEnvironment(propName);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#getEnvironment()
     */
    @Override
    public Hashtable<?, ?> getEnvironment() throws NamingException
    {
        return getDefaultInitCtx().getEnvironment();
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#close()
     */
    @Override
    public void close() throws NamingException
    {
        this.myProps = null;
        if (this.defaultInitCtx != null)
        {
            this.defaultInitCtx.close();
            this.defaultInitCtx = null;
        }
        this.gotDefault = false;
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#getNameInNamespace()
     */
    @Override
    public String getNameInNamespace() throws NamingException
    {
        return getDefaultInitCtx().getNameInNamespace();
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.event.EventContext #addNamingListener(javax.naming.Name, int, NamingListener)
     */
    @Override
    public void addNamingListener(final Name target, final int scope, final NamingListener l) throws NamingException
    {
        this.getDefaultInitCtx().addNamingListener(target, scope, l);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.event.EventContext #addNamingListener(java.lang.String, int, NamingListener)
     */
    @Override
    public void addNamingListener(final String target, final int scope, final NamingListener l) throws NamingException
    {
        this.getDefaultInitCtx().addNamingListener(target, scope, l);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.event.EventContext #removeNamingListener(javax.naming.event.NamingListener)
     */
    @Override
    public void removeNamingListener(final NamingListener l) throws NamingException
    {
        this.getDefaultInitCtx().removeNamingListener(l);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.event.EventContext#targetMustExist()
     */
    @Override
    public boolean targetMustExist() throws NamingException
    {
        return this.getDefaultInitCtx().targetMustExist();
    }
}
