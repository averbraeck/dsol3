package nl.tudelft.simulation.dsol.animation.D2;

import java.util.Comparator;

/**
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:37:49 $
 * @since 1.5
 */
public class Renderable2DComparator implements Comparator<Renderable2DInterface>
{    
    /**
     * constructs a new Renderable2DComparator
     */
    public Renderable2DComparator()
    {
        super();
    }

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(final Renderable2DInterface r1, final Renderable2DInterface r2)
    {
        try
        {
            if (r1.getSource().getLocation().z > r2.getSource().getLocation().z)
            {
                return 1;
            }
            if (r1.getSource().getLocation().z < r2.getSource().getLocation().z)
            {
                return -1;
            }
        }
        catch (Exception exception)
        {
            // ignore as this can happen when the source is in the process of deletion
            // and therefore it cannot return a proper location.
        }
        return new Integer(r1.hashCode()).compareTo(new Integer(r2.hashCode()));
    }
}