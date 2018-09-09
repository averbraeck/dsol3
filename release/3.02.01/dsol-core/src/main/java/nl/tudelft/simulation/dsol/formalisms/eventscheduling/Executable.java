package nl.tudelft.simulation.dsol.formalisms.eventscheduling;

/**
 * Executable lambda function for event scheduling. <br>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Oct 29, 2015
 */
public interface Executable
{
    /**
     * Execute the event.
     */
    void execute();
}
