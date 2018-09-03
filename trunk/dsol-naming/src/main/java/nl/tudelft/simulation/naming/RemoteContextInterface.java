package nl.tudelft.simulation.naming;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.CompoundName;
import javax.naming.ContextNotEmptyException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.OperationNotSupportedException;

import nl.tudelft.simulation.naming.listener.RemoteContextListenerInterface;

/**
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.1 Apr 7, 2004
 * @since 1.5
 */
public interface RemoteContextInterface extends Remote
{
    /**
     * Constant defining the first part of properties wrapped by our remote construction.
     */
    String WRAPPED_PREFIX = "wrapped.naming";

    /**
     * Adds a listener for receiving naming events fired when the object(s) identified by a target and scope changes.
     * The event source of those events is this context. See the class description for a discussion on event source and
     * target. See the descriptions of the constants <tt>OBJECT_SCOPE</tt>,<tt>ONELEVEL_SCOPE</tt>, and
     * <tt>SUBTREE_SCOPE</tt> to see how <tt>scope</tt> affects the registration.
     * <p>
     * <tt>target</tt> needs to name a context only when <tt>scope</tt> is <tt>ONELEVEL_SCOPE</tt>.<tt>target</tt> may
     * name a non-context if <tt>scope</tt> is either <tt>OBJECT_SCOPE</tt> or <tt>SUBTREE_SCOPE</tt>. Using
     * <tt>SUBTREE_SCOPE</tt> for a non-context might be useful, for example, if the caller does not know in advance
     * whether <tt>target</tt> is a context and just wants to register interest in the (possibly degenerate subtree)
     * rooted at <tt>target</tt>.
     * <p>
     * When the listener is notified of an event, the listener may in invoked in a thread other than the one in which
     * <tt>addNamingListener()</tt> is executed. Care must be taken when multiple threads are accessing the same
     * <tt>EventContext</tt> concurrently. See the <a href=package-summary.html#THREADING>package description </a> for
     * more information on threading issues.
     * @param target A nonnull name to be resolved relative to this context.
     * @param scope One of <tt>OBJECT_SCOPE</tt>,<tt>ONELEVEL_SCOPE</tt>, or <tt>SUBTREE_SCOPE</tt>.
     * @param l The nonnull listener.
     * @exception NamingException If a problem was encountered while adding the listener.
     * @exception RemoteException on network exception
     */
    public void addNamingListener(Name target, int scope, RemoteContextListenerInterface l) throws RemoteException,
            NamingException;

    /**
     * Adds a listener for receiving naming events fired when the object named by the string target name and scope
     * changes. See the overload that accepts a <tt>Name</tt> for details.
     * @param target The nonnull string name of the object resolved relative to this context.
     * @param scope One of <tt>OBJECT_SCOPE</tt>,<tt>ONELEVEL_SCOPE</tt>, or <tt>SUBTREE_SCOPE</tt>.
     * @param l The nonnull listener.
     * @exception NamingException If a problem was encountered while adding the listener.
     * @exception RemoteException on network exception
     */
    public void addNamingListener(String target, int scope, RemoteContextListenerInterface l) throws NamingException,
            RemoteException;

    /**
     * Removes a listener from receiving naming events fired by this <tt>EventContext</tt>. The listener may have
     * registered more than once with this <tt>EventContext</tt>, perhaps with different target/scope arguments. After
     * this method is invoked, the listener will no longer receive events with this <tt>EventContext</tt> instance as
     * the event source (except for those events already in the process of being dispatched). If the listener was not,
     * or is no longer, registered with this <tt>EventContext</tt> instance, this method does not do anything.
     * @param l The nonnull listener.
     * @exception NamingException If a problem was encountered while removing the listener.
     * @exception RemoteException on network exception
     */
    public void removeNamingListener(RemoteContextListenerInterface l) throws NamingException, RemoteException;

    /**
     * Determines whether a listener can register interest in a target that does not exist.
     * @return true if the target must exist; false if the target need not exist.
     * @exception NamingException If the context's behavior in this regard cannot be determined.
     * @exception RemoteException on network exception
     */
    public boolean targetMustExist() throws NamingException, RemoteException;

    /**
     * Retrieves the named object. If <tt>name</tt> is empty, returns a new instance of this context (which represents
     * the same naming context as this context, but its environment may be modified independently and it may be accessed
     * concurrently).
     * @param name the name of the object to look up
     * @return the object bound to <tt>name</tt>
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @see #lookup(String)
     * @see #lookupLink(Name)
     */
    public Object lookup(Name name) throws NamingException, RemoteException;

    /**
     * Retrieves the named object. See {@link #lookup(Name)}for details.
     * @param name the name of the object to look up
     * @return the object bound to <tt>name</tt>
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     */
    public Object lookup(String name) throws NamingException, RemoteException;

    /**
     * Binds a name to an object. All intermediate contexts and the target context (that named by all but terminal
     * atomic component of the name) must already exist.
     * @param name the name to bind; may not be empty
     * @param obj the object to bind; possibly null
     * @throws javax.naming.directory.InvalidAttributesException if object did not supply all mandatory attributes
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @see #bind(String, Object)
     * @see #rebind(Name, Object)
     * @see javax.naming.directory.DirContext#bind(Name, Object, javax.naming.directory.Attributes)
     */
    public void bind(Name name, Object obj) throws NamingException, RemoteException;

    /**
     * Binds a name to an object. See {@link #bind(Name, Object)}for details.
     * @param name the name to bind; may not be empty
     * @param obj the object to bind; possibly null
     * @throws javax.naming.directory.InvalidAttributesException if object did not supply all mandatory attributes
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     */
    public void bind(String name, Object obj) throws NamingException, RemoteException;

    /**
     * Binds a name to an object, overwriting any existing binding. All intermediate contexts and the target context
     * (that named by all but terminal atomic component of the name) must already exist.
     * <p>
     * If the object is a <tt>DirContext</tt>, any existing attributes associated with the name are replaced with those
     * of the object. Otherwise, any existing attributes associated with the name remain unchanged.
     * @param name the name to bind; may not be empty
     * @param obj the object to bind; possibly null
     * @throws javax.naming.directory.InvalidAttributesException if object did not supply all mandatory attributes
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @see #rebind(String, Object)
     * @see #bind(Name, Object)
     * @see javax.naming.directory.DirContext#rebind(Name, Object, javax.naming.directory.Attributes)
     * @see javax.naming.directory.DirContext
     */
    public void rebind(Name name, Object obj) throws NamingException, RemoteException;

    /**
     * Binds a name to an object, overwriting any existing binding. See {@link #rebind(Name, Object)}for details.
     * @param name the name to bind; may not be empty
     * @param obj the object to bind; possibly null
     * @throws javax.naming.directory.InvalidAttributesException if object did not supply all mandatory attributes
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     */
    public void rebind(String name, Object obj) throws NamingException, RemoteException;

    /**
     * Unbinds the named object. Removes the terminal atomic name in <code>name</code> from the target context--that
     * named by all but the terminal atomic part of <code>name</code>.
     * <p>
     * This method is idempotent. It succeeds even if the terminal atomic name is not bound in the target context, but
     * throws <tt>NameNotFoundException</tt> if any of the intermediate contexts do not exist.
     * <p>
     * Any attributes associated with the name are removed. Intermediate contexts are not changed.
     * @param name the name to unbind; may not be empty
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @see #unbind(String)
     */
    public void unbind(Name name) throws NamingException, RemoteException;

    /**
     * Unbinds the named object. See {@link #unbind(Name)}for details.
     * @param name the name to unbind; may not be empty
     * @exception RemoteException on network exception
     * @throws NamingException if a naming exception is encountered
     */
    public void unbind(String name) throws NamingException, RemoteException;

    /**
     * Binds a new name to the object bound to an old name, and unbinds the old name. Both names are relative to this
     * context. Any attributes associated with the old name become associated with the new name. Intermediate contexts
     * of the old name are not changed.
     * @param oldName the name of the existing binding; may not be empty
     * @param newName the name of the new binding; may not be empty
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @see #rename(String, String)
     * @see #bind(Name, Object)
     * @see #rebind(Name, Object)
     */
    public void rename(Name oldName, Name newName) throws NamingException, RemoteException;

    /**
     * Binds a new name to the object bound to an old name, and unbinds the old name. See {@link #rename(Name, Name)}for
     * details.
     * @param oldName the name of the existing binding; may not be empty
     * @param newName the name of the new binding; may not be empty
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     */
    public void rename(String oldName, String newName) throws NamingException, RemoteException;

    /**
     * Enumerates the names bound in the named context, along with the class names of objects bound to them. The
     * contents of any subcontexts are not included.
     * <p>
     * If a binding is added to or removed from this context, its effect on an enumeration previously returned is
     * undefined.
     * @param name the name of the context to list
     * @return an enumeration of the names and class names of the bindings in this context. Each element of the
     *         enumeration is of type <tt>NameClassPair</tt>.
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @see #list(String)
     * @see #listBindings(Name)
     * @see NameClassPair
     */
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException, RemoteException;

    /**
     * Enumerates the names bound in the named context, along with the class names of objects bound to them. See
     * {@link #list(Name)}for details.
     * @param name the name of the context to list
     * @return an enumeration of the names and class names of the bindings in this context. Each element of the
     *         enumeration is of type <tt>NameClassPair</tt>.
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     */
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException, RemoteException;

    /**
     * Enumerates the names bound in the named context, along with the objects bound to them. The contents of any
     * subcontexts are not included.
     * <p>
     * If a binding is added to or removed from this context, its effect on an enumeration previously returned is
     * undefined.
     * @param name the name of the context to list
     * @return an enumeration of the bindings in this context. Each element of the enumeration is of type
     *         <tt>Binding</tt>.
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @see #listBindings(String)
     * @see #list(Name)
     * @see Binding
     */
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException, RemoteException;

    /**
     * Enumerates the names bound in the named context, along with the objects bound to them. See
     * {@link #listBindings(Name)}for details.
     * @param name the name of the context to list
     * @return an enumeration of the bindings in this context. Each element of the enumeration is of type
     *         <tt>Binding</tt>.
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     */
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException, RemoteException;

    /**
     * Destroys the named context and removes it from the namespace. Any attributes associated with the name are also
     * removed. Intermediate contexts are not destroyed.
     * <p>
     * This method is idempotent. It succeeds even if the terminal atomic name is not bound in the target context, but
     * throws <tt>NameNotFoundException</tt> if any of the intermediate contexts do not exist.
     * <p>
     * In a federated naming system, a context from one naming system may be bound to a name in another. One can
     * subsequently look up and perform operations on the foreign context using a composite name. However, an attempt
     * destroy the context using this composite name will fail with <tt>NotContextException</tt>, because the foreign
     * context is not a "subcontext" of the context in which it is bound. Instead, use <tt>unbind()</tt> to remove the
     * binding of the foreign context. Destroying the foreign context requires that the <tt>destroySubcontext()</tt> be
     * performed on a context from the foreign context's "native" naming system.
     * @param name the name of the context to be destroyed; may not be empty
     * @throws NameNotFoundException if an intermediate context does not exist
     * @throws NotContextException if the name is bound but does not name a context, or does not name a context of the
     *             appropriate type
     * @throws ContextNotEmptyException if the named context is not empty
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @see #destroySubcontext(String)
     */
    public void destroySubcontext(Name name) throws NamingException, RemoteException;

    /**
     * Destroys the named context and removes it from the namespace. See {@link #destroySubcontext(Name)}for details.
     * @param name the name of the context to be destroyed; may not be empty
     * @throws NameNotFoundException if an intermediate context does not exist
     * @throws NotContextException if the name is bound but does not name a context, or does not name a context of the
     *             appropriate type
     * @throws ContextNotEmptyException if the named context is not empty
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     */
    public void destroySubcontext(String name) throws NamingException, RemoteException;

    /**
     * Creates and binds a new context. Creates a new context with the given name and binds it in the target context
     * (that named by all but terminal atomic component of the name). All intermediate contexts and the target context
     * must already exist.
     * @param name the name of the context to create; may not be empty
     * @return the newly created context
     * @throws NameAlreadyBoundException if name is already bound
     * @throws javax.naming.directory.InvalidAttributesException if creation of the subcontext requires specification of
     *             mandatory attributes
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @see #createSubcontext(String)
     */
    public RemoteContextInterface createSubcontext(Name name) throws NamingException, RemoteException;

    /**
     * Creates and binds a new context. See {@link #createSubcontext(Name)}for details.
     * @param name the name of the context to create; may not be empty
     * @return the newly created context
     * @throws NameAlreadyBoundException if name is already bound
     * @throws javax.naming.directory.InvalidAttributesException if creation of the subcontext requires specification of
     *             mandatory attributes
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     */
    public RemoteContextInterface createSubcontext(String name) throws NamingException, RemoteException;

    /**
     * Retrieves the named object, following links except for the terminal atomic component of the name. If the object
     * bound to <tt>name</tt> is not a link, returns the object itself.
     * @param name the name of the object to look up
     * @return the object bound to <tt>name</tt>, not following the terminal link (if any).
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @see #lookupLink(String)
     */
    public Object lookupLink(Name name) throws NamingException, RemoteException;

    /**
     * Retrieves the named object, following links except for the terminal atomic component of the name. See
     * {@link #lookupLink(Name)}for details.
     * @param name the name of the object to look up
     * @return the object bound to <tt>name</tt>, not following the terminal link (if any)
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     */
    public Object lookupLink(String name) throws NamingException, RemoteException;

    /**
     * Retrieves the parser associated with the named context. In a federation of namespaces, different naming systems
     * will parse names differently. This method allows an application to get a parser for parsing names into their
     * atomic components using the naming convention of a particular naming system. Within any single naming system,
     * <tt>NameParser</tt> objects returned by this method must be equal (using the <tt>equals()</tt> test).
     * @param name the name of the context from which to get the parser
     * @return a name parser that can parse compound names into their atomic components
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @see #getNameParser(String)
     * @see CompoundName
     */
    public NameParser getNameParser(Name name) throws NamingException, RemoteException;

    /**
     * Retrieves the parser associated with the named context. See {@link #getNameParser(Name)}for details.
     * @param name the name of the context from which to get the parser
     * @return a name parser that can parse compound names into their atomic components
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     */
    public NameParser getNameParser(String name) throws NamingException, RemoteException;

    /**
     * Composes the name of this context with a name relative to this context. Given a name (<code>name</code>) relative
     * to this context, and the name (<code>prefix</code>) of this context relative to one of its ancestors, this method
     * returns the composition of the two names using the syntax appropriate for the naming system(s) involved. That is,
     * if <code>name</code> names an object relative to this context, the result is the name of the same object, but
     * relative to the ancestor context. None of the names may be null.
     * <p>
     * For example, if this context is named "wiz.com" relative to the initial context, then
     * 
     * <pre>
     * composeName(&quot;east&quot;, &quot;wiz.com&quot;)
     * </pre>
     * 
     * might return <code>"east.wiz.com"</code>. If instead this context is named "org/research", then
     * 
     * <pre>
     * composeName(&quot;user/jane&quot;, &quot;org/research&quot;)
     * </pre>
     * 
     * might return <code>"org/research/user/jane"</code> while
     * 
     * <pre>
     * composeName(&quot;user/jane&quot;, &quot;research&quot;)
     * </pre>
     * 
     * returns <code>"research/user/jane"</code>.
     * @param name a name relative to this context
     * @param prefix the name of this context relative to one of its ancestors
     * @return the composition of <code>prefix</code> and <code>name</code>
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @see #composeName(String, String)
     */
    public Name composeName(Name name, Name prefix) throws NamingException, RemoteException;

    /**
     * Composes the name of this context with a name relative to this context. See {@link #composeName(Name, Name)}for
     * details.
     * @param name a name relative to this context
     * @param prefix the name of this context relative to one of its ancestors
     * @return the composition of <code>prefix</code> and <code>name</code>
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     */
    public String composeName(String name, String prefix) throws NamingException, RemoteException;

    /**
     * Adds a new environment property to the environment of this context. If the property already exists, its value is
     * overwritten. See class description for more details on environment properties.
     * @param propName the name of the environment property to add; may not be null
     * @param propVal the value of the property to add; may not be null
     * @return the previous value of the property, or null if the property was not in the environment before
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @see #getEnvironment()
     * @see #removeFromEnvironment(String)
     */
    public Object addToEnvironment(String propName, Object propVal) throws NamingException, RemoteException;

    /**
     * Removes an environment property from the environment of this context. See class description for more details on
     * environment properties.
     * @param propName the name of the environment property to remove; may not be null
     * @return the previous value of the property, or null if the property was not in the environment
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @see #getEnvironment()
     * @see #addToEnvironment(String, Object)
     */
    public Object removeFromEnvironment(String propName) throws NamingException, RemoteException;

    /**
     * Retrieves the environment in effect for this context. See class description for more details on environment
     * properties.
     * <p>
     * The caller should not make any changes to the object returned: their effect on the context is undefined. The
     * environment of this context may be changed using <tt>addToEnvironment()</tt> and <tt>removeFromEnvironment()</tt>.
     * @return the environment of this context; never null
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @see #addToEnvironment(String, Object)
     * @see #removeFromEnvironment(String)
     */
    public Hashtable<?, ?> getEnvironment() throws NamingException, RemoteException;

    /**
     * Closes this context. This method releases this context's resources immediately, instead of waiting for them to be
     * released automatically by the garbage collector.
     * <p>
     * This method is idempotent: invoking it on a context that has already been closed has no effect. Invoking any
     * other method on a closed context is not allowed, and results in undefined behaviour.
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     */
    public void close() throws NamingException, RemoteException;

    /**
     * Retrieves the full name of this context within its own namespace.
     * <p>
     * Many naming services have a notion of a "full name" for objects in their respective namespaces. For example, an
     * LDAP entry has a distinguished name, and a DNS record has a fully qualified name. This method allows the client
     * application to retrieve this name. The string returned by this method is not a JNDI composite name and should not
     * be passed directly to context methods. In naming systems for which the notion of full name does not make sense,
     * <tt>OperationNotSupportedException</tt> is thrown.
     * @return this context's name in its own namespace; never null
     * @throws OperationNotSupportedException if the naming system does not have the notion of a full name
     * @throws NamingException if a naming exception is encountered
     * @exception RemoteException on network exception
     * @since 1.5
     */
    public String getNameInNamespace() throws NamingException, RemoteException;
}
