package nl.tudelft.simulation.dsol.jetty.test;

import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * ServletTest.java. <br>
 * <br>
 * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank"> www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class ServletTest
{
    /**
     * @param args String[];
     * @throws Exception on jetty server error
     */
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        ContextHandlerCollection handlerCollection = new ContextHandlerCollection();
        server.setHandler(handlerCollection);

        ServletContextHandler root = new ServletContextHandler(ServletContextHandler.SESSIONS);
        // WebAppContext root = new WebAppContext();
        root.setContextPath("/test");
        root.setResourceBase("file:///E:/jetty");
        handlerCollection.addHandler(root);
        HttpServlet servlet = new DefaultServlet();
        /*-
        HttpServlet servlet = new HttpServlet()
        {
            @Override
            public void doGet(HttpServletRequest req, HttpServletResponse resp)
            {
                System.out.println("Handling GET");
            }
        
            @Override
            public void doPost(HttpServletRequest req, HttpServletResponse resp)
            {
                System.out.println("Handling POST");
            }
        
        };
        */
        ServletHolder holder = new ServletHolder(servlet);
        // holder.setAsyncSupported(true);
        root.addServlet(holder, "/test");
        server.start();
        server.join();
    }

}
