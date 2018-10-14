package nl.tudelft.simulation.dsol.jetty.test.sse;

import java.net.URL;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import nl.tudelft.simulation.language.io.URLResource;

/**
 * DSOLServer.java. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class DSOLServer
{

    /**
     * 
     */
    public DSOLServer()
    {
    }

    /**
     * @param args none
     * @throws Exception in case jetty crashes
     */
    public static void main(final String[] args) throws Exception
    {
        Server server = new Server(8080);
        ResourceHandler resourceHandler = new ResourceHandler();
        
        URL home = URLResource.getResource("/home");
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
        resourceHandler.setResourceBase(home.toURI().getPath());
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resourceHandler, new DefaultHandler() });
        server.setHandler(handlers);
        
        server.start();
        server.join();
    }
}
