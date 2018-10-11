package nl.tudelft.simulation.dsol.jetty.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * ServerApplication.java. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">
 * www.simulation.tudelft.nl</a>. The source code and binary code of this software is proprietary information of Delft
 * University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class ServerApplication extends AbstractHandler
{

    /**
     * @throws Exception
     */
    public ServerApplication() throws Exception
    {
        // nothing to set for now
    }

    /** {@inheritDoc} */
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        // Declare response encoding and types
        response.setContentType("text/html; charset=utf-8");

        // Declare response status code
        response.setStatus(HttpServletResponse.SC_OK);

        // Write back response
        response.getWriter().println("<h1>Hello World</h1>");

        // Inform jetty that this request has now been handled
        baseRequest.setHandled(true);
    }

    public static void main(final String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new ServerApplication());

        server.start();
        server.join();
    }

}
