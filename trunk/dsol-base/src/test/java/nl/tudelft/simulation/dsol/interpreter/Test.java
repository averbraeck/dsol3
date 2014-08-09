/*
 * @(#) Test.java Dec 16, 2004 Copyright (c) 2004 Delft University of Technology
 * Jaffalaan 5, 2628 BX Delft, the Netherlands All rights reserved. This
 * software is proprietary information of Delft University of Technology The
 * code is published under the General Public License
 */
package nl.tudelft.simulation.dsol.interpreter;

import nl.tudelft.simulation.dsol.interpreter.process.Process;

/**
 * A Test class.
 */
public class Test
{
    /**
     * executes the test
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // You can play with the log level to see the interpretation take place.
        new Test().doIt();
    }

    private void doIt()
    {
        // Now we create a process and resume its operation.
        Process process = new MyProcess();
        process.resume();
    }

    /**
     * counts the links
     * @param link the link
     * @return the number of links
     */
    public static int countLinks(Link link)
    {
        if (link == null)
            return 0;
        return 1 + countLinks(link.next);
    }

    /**
     * A link class
     */
    private class Link
    {
        /** the next in the chain */
        public Link next;

        /**
         * constructs a new Link
         * @param next the next
         */
        public Link(Link next)
        {
            this.next = next;
        }
    }

    /**
     * the Process
     */
    private class MyProcess extends Process
    {
        /**
         * constructs a Process
         */
        public MyProcess()
        {
            super();
        }

        /**
         * the process method
         */
        public void process()
        {
            System.out.println("This process is started");
            Link link = new Link(new Link(new Link(null)));
            int count = Test.countLinks(link);
            System.out.println("links=" + count);
            System.out.println("This process is finished");
        }
    }
}