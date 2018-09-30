package nl.tudelft.simulation.zmq.test;

import org.zeromq.ZMQ;

import nl.tudelft.simulation.logger.CategoryLogger;

/**
 * Server example for JeroMQ / ZeroMQ.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Oct 21, 2016
 */
public class Server
{
    /**
     * @param args
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
