package nl.tudelft.simulation.examples.dsol.mm1queue;

/**
 * A truly basic customer for the DSOL framework. <br>
 * (c) copyright 2003 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands.
 * <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @version 2.0 21.09.2003 <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 */
public class Customer
{
    /** entranceTime refers to the time when the customer entered the queue. */
    private double entranceTime = Double.NaN;

    /**
     * constructs a new Customer.
     */
    public Customer()
    {
        super();
    }

    /**
     * sets the entrance time of the customer to the queue.
     * @param time the entranceTime
     */
    public final void setEntranceTime(final double time)
    {
        this.entranceTime = time;
    }

    /**
     * returns the time this customer entered the queue.
     * @return double the entranceTime
     */
    public final double getEntranceTime()
    {
        return this.entranceTime;
    }
}
