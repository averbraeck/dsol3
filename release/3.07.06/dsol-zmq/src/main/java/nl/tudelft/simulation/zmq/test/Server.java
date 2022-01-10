package nl.tudelft.simulation.zmq.test;

import org.djutils.logger.CategoryLogger;
import org.zeromq.ZMQ;

/**
 * Server example for JeroMQ / ZeroMQ.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class Server
{
    /**
     * @param args String[];
     */
    public static void main(String[] args)
    {
        ZMQ.Context context = ZMQ.context(1);

        // Socket to talk to clients
        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:5555");

        while (!Thread.currentThread().isInterrupted())
        {
            // Wait for next request from the client
            byte[] request = responder.recv(0);
            System.out.println("Received " + request);

            // Do some 'work'
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException exception)
            {
                CategoryLogger.always().error(exception);
            }

            // Send reply back to client
            String reply = "World";
            responder.send(reply.getBytes(), 0);
        }
        responder.close();
        context.term();
    }
}
