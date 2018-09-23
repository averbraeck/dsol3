package nl.tudelft.simulation.dsol.jetty.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.AsyncContextState;

/**
 * SSEServlet.java. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information
 * <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The source code and
 * binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class SSEServlet extends HttpServlet
{
    /** */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        System.out.println("GET!!!");
        System.out.println(req.getSession(true).getId());

        BufferedReader reader = req.getReader();
        String str = "";
        while ((str = reader.readLine()) != null)
        {
            System.out.println(str);
        }
        reader.close();

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.flushBuffer();
        AsyncContextState async = (AsyncContextState) req.startAsync();

        System.out.println(async);

        /*-
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
    */
    }

    /** {@inheritDoc} */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        System.out.println("POST!!!");
        BufferedReader reader = req.getReader();
        String str = "";
        while ((str = reader.readLine()) != null)
        {
            System.out.println(str);
        }
        reader.close();
    }

    
}
