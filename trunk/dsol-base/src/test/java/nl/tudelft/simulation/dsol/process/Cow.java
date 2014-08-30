/*
 * @(#) Cow.java Feb 1, 2005 Copyright (c) 2004 Delft University of Technology
 * Jaffalaan 5, 2628 BX Delft, the Netherlands All rights reserved. This
 * software is proprietary information of Delft University of Technology The
 * code is published under the General Public License
 */
package nl.tudelft.simulation.dsol.process;

import nl.tudelft.simulation.dsol.interpreter.process.Process;

/**
 * The specifies
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2007/01/07 05:00:13 $
 * @since 1.5
 */
class Cow extends Process
{
    /**
     * constructs a new Cow
     */
    public Cow()
    {
        super();
    }

    /**
     * moos
     */
    public void moo()
    {
        System.out.println("moo");
    }

    /**
     * executes the process
     */
    public void process()
    {
        this.moo();
        this.suspend();
    }
}
