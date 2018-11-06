package nl.tudelft.simulation.dsol.jetty.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.AsyncContextState;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import nl.tudelft.simulation.dsol.logger.SimLogger;

/**
 * SSEServer.java. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">
 * www.simulation.tudelft.nl</a>. The source code and binary code of this software is proprietary information of Delft
 * University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class SSEServer extends AbstractHandler
{
    // @formatter:off
    /** the html page. */
    String[] page = {
            "<!DOCTYPE html>",
            "<html>",
            "<head>",
            "<meta charset=\"ISO-8859-1\">",
            "<title>Server-Side Event Test</title>",
            "</head>",
            "<body>",
            "<script>",
            " var eventSource = new EventSource('test');",
            " eventSource.onmessage = function(event) {",
            "     console.log(event);",
            "     window.alert(event.data);",
            " };",
            "</script>",
            "<h1>Server-Side Event Test</h1>",
            "</body>",
            "</html>" 
       };
    // @formatter:on

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
        for (String s : this.page)
            response.getWriter().println(s);

        // Inform jetty that this request has now been handled
        baseRequest.setHandled(true);
    }

    /**
     * @param args String[]; should be empty
     * @throws Exception on jetty server error
     */
    public static void main(String[] args) throws Exception
    {
        HandlerCollection handlerCollection = new HandlerCollection(true);
        // handlerCollection.addHandler(new SSEServer());
        Server server = new Server(8080);
        server.setHandler(handlerCollection);
        server.start();

        WebAppContext context = new WebAppContext();
        handlerCollection.addHandler(context);
        context.setContextPath("/");
        context.setResourceBase("/jetty");
        // ServletHolder servletHolder = new ServletHolder(new SSEServlet());
        ServletHolder servletHolder = new ServletHolder(new DefaultServlet()
        {
            /** {@inheritDoc} */
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException
            {
                System.out.println("GET");

                super.doGet(request, response);

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("text/event-stream");
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.flushBuffer();

                AsyncContextState async = (AsyncContextState) request.startAsync();
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            for (int i = 0; i < 3; i++)
                            {
                                try
                                {
                                    Thread.sleep(3000);
                                }
                                catch (InterruptedException e)
                                {
                                    SimLogger.always().error(e);
                                }
                                ServletOutputStream outputStream = async.getResponse().getOutputStream();
                                if (outputStream.isReady())
                                {
                                    String message = "data: refresh\n\n";
                                    outputStream.write(message.getBytes(StandardCharsets.UTF_8), 0, message.length());
                                    outputStream.flush();
                                    async.getResponse().flushBuffer();
                                }
                                else
                                {
                                    System.out.println("not ready!");
                                }
                                System.out.println("wrote!");
                            }
                        }
                        catch (IOException e)
                        {
                            async.complete();
                            System.err.println(e.getMessage());
                        }
                    };
                }.start();
            }

            /** {@inheritDoc} */
            @Override
            protected void doPost(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException
            {
                System.out.println("POST");
                BufferedReader reader = request.getReader();
                String str = "";
                while ((str = reader.readLine()) != null)
                {
                    System.out.println(str);
                }
                reader.close();
                // super.doPost(request, response);
            }
        });
        servletHolder.setAsyncSupported(true);
        context.addServlet(servletHolder, "/");
        context.start();
    }

}
