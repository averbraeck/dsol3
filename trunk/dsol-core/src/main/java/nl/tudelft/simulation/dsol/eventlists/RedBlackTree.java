package nl.tudelft.simulation.dsol.eventlists;

import java.util.NoSuchElementException;
import java.util.TreeSet;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;

/**
 * A RedBlackTree implementation of the eventlistInterface. This implementation is based on Java's TreeSet.
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:45 $
 * @param <T> the type of simulation time, e.g. SimTimeCalendarLong or SimTimeDouble or SimTimeDoubleUnit.
 * @since 1.5
 */
public class RedBlackTree<T extends SimTime<?, ?, T>> extends TreeSet<SimEventInterface<T>>
        implements EventListInterface<T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new <code>RedBlackTree</code>.
     */
    public RedBlackTree()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public synchronized SimEventInterface<T> removeFirst()
    {
        SimEventInterface<T> first = this.first();
        this.remove(first);
        return first;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public synchronized SimEventInterface<T> removeLast()
    {
        SimEventInterface<T> last = this.last();
        this.remove(last);
        return last;
    }

    /**
     * we re-implemented the first method. Instead of throwing exceptions if the tree is empty, we return a null value.
     * @see java.util.TreeSet#first()
     * @return the first SimEvent in the tree.
     */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public SimEventInterface<T> first()
    {
        try
        {
            return super.first();
        }
        catch (NoSuchElementException noSuchElementException)
        {
            return null;
        }
    }

    /**
     * we re-implemented the last method. Instead of throwing exceptions if the tree is empty, we return a null value.
     * @see java.util.TreeSet#last()
     * @return the last SimEvent in the tree.
     */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public SimEventInterface<T> last()
    {
        try
        {
            return super.first();
        }
        catch (NoSuchElementException noSuchElementException)
        {
            return null;
        }
    }
}
