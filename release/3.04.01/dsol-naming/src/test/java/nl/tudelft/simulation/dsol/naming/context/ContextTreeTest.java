package nl.tudelft.simulation.dsol.naming.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.naming.NamingException;

import org.junit.Test;

import nl.tudelft.simulation.naming.context.ContextInterface;
import nl.tudelft.simulation.naming.context.FileContext;
import nl.tudelft.simulation.naming.context.InitialEventContext;
import nl.tudelft.simulation.naming.context.JVMContext;
import nl.tudelft.simulation.naming.context.util.ContextUtil;

/**
 * Tests the context where larger parts of the tree are evaluated.
 * <p>
 * Copyright (c) 2004-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class ContextTreeTest
{
    /**
     * test for different types of context.
     * @throws NamingException on error
     * @throws RemoteException on RMI error
     * @throws IOException on creation of temporary file
     */
    @Test
    public void testContextTree() throws NamingException, RemoteException, IOException
    {
        // test JVMContext
        ContextInterface jvmContext = new JVMContext(null, "root");
        testContextTree(jvmContext);

        // test FileContext directly
        Path path = Files.createTempFile("context-file", ".jpo");
        File file = path.toFile();
        file.deleteOnExit();
        ContextInterface fileContext = new FileContext(file);
        testContextTree(fileContext);

        // test InitialEventContext
        Properties properties = new Properties();
        properties.put("java.naming.factory.initial", "nl.tudelft.simulation.naming.context.JVMContextFactory");
        InitialEventContext eventContext = InitialEventContext.instantiate(properties);
        testContextTree(eventContext);
        eventContext.close();
        destroy(eventContext);

        // test FileContext via FileContextFactory
        Path fcPath = Files.createTempFile("factory-context-file", ".jpo");
        File fcFile = fcPath.toFile();
        fcFile.delete(); // should not exist yet -- only the name and handle.
        fcFile.deleteOnExit();
        String fcName = fcPath.toUri().toURL().toString();
        properties.put("java.naming.factory.initial", "nl.tudelft.simulation.naming.context.FileContextFactory");
        properties.put("java.naming.provider.url", fcName);
        InitialEventContext factoryFileContext = InitialEventContext.instantiate(properties);
        testContextTree(factoryFileContext);
        destroy(eventContext);

        // test RemoteContext
        properties.put("java.naming.factory.initial", "nl.tudelft.simulation.naming.context.RemoteContextFactory");
        properties.put("java.naming.provider.url", "http://localhost:1099/remoteContext");
        properties.put("wrapped.naming.factory.initial", "nl.tudelft.simulation.naming.context.JVMContextFactory");
        InitialEventContext remoteContext = InitialEventContext.instantiate(properties);
        testContextTree(remoteContext);
        destroy(eventContext);
    }

    /**
     * test basics for a Context Tree.
     * @param root the context to test
     * @throws RemoteException on RMI error
     * @throws NamingException on error
     */
    public void testContextTree(final ContextInterface root) throws NamingException, RemoteException
    {
        // fill
        root.createSubcontext("/1/11/111/1111/11111");
        root.createSubcontext("/1/11/111/1112/11121");
        root.createSubcontext("/2/21/211/2111/21111");
        ContextInterface c11 = ContextUtil.lookupSubContext(root, "/1/11");
        ContextInterface c11b = (ContextInterface) ((ContextInterface) root.get("1")).get("11");
        assertNotNull(c11);
        assertEquals(c11, c11b);
        ContextInterface c1112 = ContextUtil.lookupSubContext(c11, "111/1112");
        ContextInterface c1112b = ContextUtil.lookupSubContext(root, "/1/11/111/1112");
        assertNotNull(c1112);
        assertEquals(c1112, c1112b);
        c11.bindObject("o11", new Object());
        c11.bindObject("o12", new Object());
        c11.bindObject("o13", new Object());
        assertEquals(4, c11.keySet().size());
        ContextUtil.lookupSubContext(c11, "111").bindObject("o111", new Object());
        c1112.bind("a1112", "a-object");
        c1112.bind("b1112", "b-object");
        c1112.bind("c1112", "c-object");
        c1112.bind("d1112", "d-object");
        System.out.println(root.toString(true));
        assertEquals(5, c1112.bindings().size());
        ContextInterface c21111 = ContextUtil.lookupOrCreateSubContext(root, "/2/21/211/2111/21111");
        c21111.bindObject("o21111", "deep");
        assertEquals(1, c21111.values().size());
        
        // empty 
        c1112.destroySubcontext("11121");
        assertEquals(4, c1112.bindings().size());
        c11.destroySubcontext("111");
        assertEquals(2, root.keySet().size());
        assertEquals(3, c11.keySet().size());
        root.destroySubcontext("1");
        assertEquals(1, root.keySet().size());
        root.destroySubcontext("2");
        assertEquals(0, root.keySet().size());
    }

    /**
     * destroy the singleton InitialEventContext between tests...
     * @param ctx InitialEventContext; the context to clear
     */
    private void destroy(final InitialEventContext ctx)
    {
        try
        {
            Field instance = ctx.getClass().getDeclaredField("INSTANCE");
            instance.setAccessible(true);
            instance.set(null, null);
            instance.setAccessible(false);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }
}
