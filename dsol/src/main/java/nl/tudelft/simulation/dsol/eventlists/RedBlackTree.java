package nl.tudelft.simulation.dsol.eventlists;

import java.util.NoSuchElementException;
import java.util.TreeSet;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;

/**
 * A RedBlackTree implementation of the eventlistInterface. This implementation is based on Java's TreeSet.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:45 $
 * @since 1.5
 */
public class RedBlackTree extends TreeSet<SimEventInterface> implements EventListInterface
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new <code>RedBlackTree</code>
     */
    public RedBlackTree()
    {
        super();
    }

    /**
     * @see nl.tudelft.simulation.dsol.eventlists.EventListInterface #removeFirst()
     */
    public synchronized SimEventInterface removeFirst()
    {
        SimEventInterface first = this.first();
        this.remove(first);
        return first;
    }

    /**
     * @see nl.tudelft.simulation.dsol.eventlists.EventListInterface #removeLast()
     */
    public synchronized SimEventInterface removeLast()
    {
        SimEventInterface last = this.last();
        this.remove(last);
        return last;
    }

    /**
     * we re-implemented the first method. Instead of throwing exceptions if the tree is emty, we return a null value
     * @see java.util.TreeSet#first()
     */
    @Override
    public SimEventInterface first()
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
     * we re-implemented the last method. Instead of throwing exceptions if the
     * @see java.util.TreeSet#last()
     */
    @Override
    public SimEventInterface last()
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