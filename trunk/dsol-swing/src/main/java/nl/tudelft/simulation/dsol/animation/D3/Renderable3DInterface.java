package nl.tudelft.simulation.dsol.animation.D3;

/**
 * Renderable3DInterface, an interface for 3d renderables <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:37:27 $
 * @author <a href="mailto:royc@tbm.tudelft.nl">Roy Chin </a>
 */
public interface Renderable3DInterface
{
    /**
     * Static (non dynamic) objects that describe the world or landscape, possibly containing other static or animated
     * objects.
     */
    int STATIC_OBJECT = 0;

    /** Simulated objects are objects that move, rotate or change shape. */
    int DYNAMIC_OBJECT = 1;

    /**
     * Update the representation of the model
     */
    void update();

    /**
     * Get the type.
     * @return Type of renderable
     */
    int getType();
}
