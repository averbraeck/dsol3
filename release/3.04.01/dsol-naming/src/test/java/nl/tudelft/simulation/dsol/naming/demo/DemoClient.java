package nl.tudelft.simulation.dsol.naming.demo;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.naming.NamingException;

import nl.tudelft.simulation.naming.context.RemoteContextInterface;

/**
 * DemoClient sets up a connection to the remote context at DemoServer and periodically prints the results. 
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class DemoClient
{
    /**
     * @param args empty
     * @throws NamingException on context error
     * @throws IOException on error reading from stdin
     * @throws NotBoundException when server is not running
     */
    public static void main(String[] args) throws NamingException, IOException, NotBoundException
    {
        URL url = new URL("http://localhost:1099/remoteContext");
        Registry registry = LocateRegistry.getRegistry(url.getHost(), url.getPort());
        RemoteContextInterface remoteContext = (RemoteContextInterface) registry.lookup(url.getFile());
        DemoServer.print(remoteContext.getRootContext(), 0);
    }
}
