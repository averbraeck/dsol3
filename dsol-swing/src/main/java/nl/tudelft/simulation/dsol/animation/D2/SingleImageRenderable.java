package nl.tudelft.simulation.dsol.animation.D2;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.net.URL;

import nl.tudelft.simulation.dsol.animation.LocatableInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:37:20 $
 * @since 1.5
 */
public class SingleImageRenderable extends ImageRenderable
{
    /**
     * constructs a new SingleImageRenderable
     * @param source the moving source
     * @param simulator the simulator
     * @param image the image to animate
     */
    public SingleImageRenderable(final LocatableInterface source, final SimulatorInterface simulator, final URL image)
    {
        super(source, simulator, new URL[]{image});
    }

    /**
     * constructs a new SingleImageRenderable
     * @param staticLocation the static location
     * @param size the size
     * @param simulator the simulator
     * @param image the image
     */
    public SingleImageRenderable(final Point2D staticLocation, final Dimension size,
            final SimulatorInterface simulator, final URL image)
    {
        super(staticLocation, size, simulator, new URL[]{image});
    }

    /**
     * constructs a new SingleImageRenderable
     * @param staticLocation the static location
     * @param size the size of the image
     * @param simulator the simulator
     * @param image the image
     */
    public SingleImageRenderable(final DirectedPoint staticLocation, final Dimension size,
            final SimulatorInterface simulator, final URL image)
    {
        super(staticLocation, size, simulator, new URL[]{image});
    }

    /** {@inheritDoc} */
    @Override
    public int selectImage()
    {
        // We only have one image to show. Let's use this one.
        return 0;
    }
}
