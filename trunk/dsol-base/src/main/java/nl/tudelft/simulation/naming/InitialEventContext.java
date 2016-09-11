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
 * InitialEventContext class. This class is the starting context for performing naming operations. Copyright (c) 2009
 * Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See for project
 * information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>. Redistribution and use in
 * source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * <br>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * @version Oct 17, 2009 <br>
 * @author <a href="http://tudelft.nl/mseck">Mamadou Seck</a><br>
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a><br>
 */
@SuppressWarnings("restriction")
public class InitialEventContext implements EventContext
{
    /** the properties of the initialEventContext. */
    protected Hashtable myProps = null;

    /**
     * Field holding the result of calling NamingManager.getInitialContext(). It is set by getDefaultInitCtx() the first
     * time getDefaultInitCtx() is called. Subsequent invocations of getDefaultInitCtx() return the value of
     * defaultInitCtx.
     */
    protected EventContext defaultInitCtx = null;

    /**
     * Field indicating whether the initial context has been obtained by calling NamingManager.getInitialContext(). If
     * true, its result is in <code>defaultInitCtx</code>.
     */
    protected boolean gotDefault = false;

    /**
     * Constructs an initial context with the option of not initializing it. This may be used by a constructor in a
     * subclass when the value of the environment parameter is not yet known at the time the <tt>InitialContext</tt>
     * constructor is called. The subclass's constructor will call this constructor, compute the value of the
     * environment, and then call <tt>init()</tt> before returning.
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
     * Constructs an initial context. No environment properties are supplied. Equivalent to
     * <tt>new InitialContext(null)</tt>.
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
     * @param environment environment used to create the initial context. Null indicates an empty environment.
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
     * @param environment environment used to create the initial context. Null indicates an empty environment.
     * @throws NamingException if a naming exception is encountered
     * @since 1.5
     */
    @SuppressWarnings("unchecked")
    protected void init(final Hashtable<?, ?> environment) throws NamingException
    {
        // TODO ResourceManager.getInitialEnvironment(environment) is an internal API that is forbidden to access?
        this.myProps = ResourceManager.getInitialEnvironment(environment);
        if (this.myProps.get(Context.INITIAL_CONTEXT_FACTORY) != null)
        {
            // user has specified initial context factory; try to get it
            getDefaultInitCtx();
        }
    }

    /**
     * returns the URL Scheme
     * @param str the string
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
     * Retrieves a context for resolving the string name <code>name</code>. If <code>name</code> name is a URL string,
     * then attempt to find a URL context for it. If none is found, or if <code>name</code> is not a URL string, then
     * return <code>getDefaultInitCtx()</code>.
     * <p>
     * See getURLOrDefaultInitCtx(Name) for description of how a subclass should use this method.
     * @param name The non-null name for which to get the context.
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
     * @param name The non-null name for which to get the context.
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
    public Object lookup(final String name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).lookup(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#lookup(javax.naming.Name)
     */
    public Object lookup(final Name name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).lookup(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
     */
    public void bind(final String name, final Object obj) throws NamingException
    {
        getURLOrDefaultInitCtx(name).bind(name, obj);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
     */
    public void bind(final Name name, final Object obj) throws NamingException
    {
        getURLOrDefaultInitCtx(name).bind(name, obj);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
     */
    public void rebind(final String name, final Object obj) throws NamingException
    {
        getURLOrDefaultInitCtx(name).rebind(name, obj);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
     */
    public void rebind(final Name name, final Object obj) throws NamingException
    {
        getURLOrDefaultInitCtx(name).rebind(name, obj);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#unbind(java.lang.String)
     */
    public void unbind(final String name) throws NamingException
    {
        getURLOrDefaultInitCtx(name).unbind(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#unbind(javax.naming.Name)
     */
    public void unbind(final Name name) throws NamingException
    {
        getURLOrDefaultInitCtx(name).unbind(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
     */
    public void rename(final String oldName, final String newName) throws NamingException
    {
        getURLOrDefaultInitCtx(oldName).rename(oldName, newName);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
     */
    public void rename(final Name oldName, final Name newName) throws NamingException
    {
        getURLOrDefaultInitCtx(oldName).rename(oldName, newName);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#list(java.lang.String)
     */
    public NamingEnumeration<NameClassPair> list(final String name) throws NamingException
    {
        return (getURLOrDefaultInitCtx(name).list(name));
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#list(javax.naming.Name)
     */
    public NamingEnumeration<NameClassPair> list(final Name name) throws NamingException
    {
        return (getURLOrDefaultInitCtx(name).list(name));
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#listBindings(java.lang.String)
     */
    public NamingEnumeration<Binding> listBindings(final String name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).listBindings(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#listBindings(javax.naming.Name)
     */
    public NamingEnumeration<Binding> listBindings(final Name name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).listBindings(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#destroySubcontext(java.lang.String)
     */
    public void destroySubcontext(final String name) throws NamingException
    {
        getURLOrDefaultInitCtx(name).destroySubcontext(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
     */
    public void destroySubcontext(final Name name) throws NamingException
    {
        getURLOrDefaultInitCtx(name).destroySubcontext(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#createSubcontext(java.lang.String)
     */
    public Context createSubcontext(final String name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).createSubcontext(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#createSubcontext(javax.naming.Name)
     */
    public Context createSubcontext(final Name name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).createSubcontext(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#lookupLink(java.lang.String)
     */
    public Object lookupLink(final String name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).lookupLink(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#lookupLink(javax.naming.Name)
     */
    public Object lookupLink(final Name name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).lookupLink(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#getNameParser(java.lang.String)
     */
    public NameParser getNameParser(final String name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).getNameParser(name);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#getNameParser(javax.naming.Name)
     */
    public NameParser getNameParser(final Name name) throws NamingException
    {
        return getURLOrDefaultInitCtx(name).getNameParser(name);
    }

    /**
     * Composes the name of this context with a name relative to this context. Since an initial context may never be
     * named relative to any context other than itself, the value of the <tt>prefix</tt> parameter must be an empty name
     * (<tt>""</tt>).
     * @param name the name
     * @param prefix the prefix
     * @return String
     * @throws NamingException on exception
     */
    public String composeName(final String name, final String prefix) throws NamingException
    {
        throw new NamingException("composeName " + name + ", " + prefix + " is not supported.");
    }

    /**
     * Composes the name of this context with a name relative to this context. Since an initial context may never be
     * named relative to any context other than itself, the value of the <tt>prefix</tt> parameter must be an empty
     * name.
     * @param name the name
     * @param prefix the prefix
     * @return Name
     * @throws NamingException on exception
     */
    public Name composeName(final Name name, final Name prefix) throws NamingException
    {
        throw new NamingException("composeName " + name + ", " + prefix + " is not supported.");
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context #addToEnvironment(java.lang.String, java.lang.Object)
     */
    public Object addToEnvironment(final String propName, final Object propVal) throws NamingException
    {
        this.myProps.put(propName, propVal);
        return getDefaultInitCtx().addToEnvironment(propName, propVal);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
     */
    public Object removeFromEnvironment(final String propName) throws NamingException
    {
        this.myProps.remove(propName);
        return getDefaultInitCtx().removeFromEnvironment(propName);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#getEnvironment()
     */
    public Hashtable<?, ?> getEnvironment() throws NamingException
    {
        return getDefaultInitCtx().getEnvironment();
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.Context#close()
     */
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
    public String getNameInNamespace() throws NamingException
    {
        return getDefaultInitCtx().getNameInNamespace();
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.event.EventContext #addNamingListener(javax.naming.Name, int, NamingListener)
     */
    public void addNamingListener(final Name target, final int scope, final NamingListener l) throws NamingException
    {
        this.getDefaultInitCtx().addNamingListener(target, scope, l);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.event.EventContext #addNamingListener(java.lang.String, int, NamingListener)
     */
    public void addNamingListener(final String target, final int scope, final NamingListener l) throws NamingException
    {
        this.getDefaultInitCtx().addNamingListener(target, scope, l);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.event.EventContext #removeNamingListener(javax.naming.event.NamingListener)
     */
    public void removeNamingListener(final NamingListener l) throws NamingException
    {
        this.getDefaultInitCtx().removeNamingListener(l);
    }

    /**
     * {@inheritDoc}
     * @see javax.naming.event.EventContext#targetMustExist()
     */
    public boolean targetMustExist() throws NamingException
    {
        return this.getDefaultInitCtx().targetMustExist();
    }
}
