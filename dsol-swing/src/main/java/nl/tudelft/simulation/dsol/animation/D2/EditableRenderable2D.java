package nl.tudelft.simulation.dsol.animation.D2;

import nl.tudelft.simulation.dsol.animation.Editable;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * EditableRenderable2D is an abstract class that implements EditableRenderable2DInterface. This class can be extended
 * by classes that animate editable simulation objects.
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
public abstract class EditableRenderable2D extends Renderable2D implements EditableRenderable2DInterface
{

    /**
     * the source of this renderable
     */
    protected Editable source = null;

    /**
     * constructs a new EditableRenderable2D.
     * @param source the source and target
     * @param simulator the simulator
     */
    public EditableRenderable2D(final Editable source, final SimulatorInterface simulator)
    {
        super(source, simulator);
        this.source = source;
    }

    /** {@inheritDoc} */
    public boolean isClosedShape()
    {
        return true;
    }

    /** {@inheritDoc} */
    public boolean allowMove()
    {
        return true;
    }

    /** {@inheritDoc} */
    public boolean allowRotate()
    {
        return true;
    }

    /** {@inheritDoc} */
    public boolean allowScale()
    {
        return true;
    }

    /** {@inheritDoc} */
    public boolean allowEditPoints()
    {
        return true;
    }

    /** {@inheritDoc} */
    public boolean allowDelete()
    {
        return true;
    }

    /** {@inheritDoc} */
    public boolean allowAddOrDeletePoints()
    {
        return true;
    }

    /** {@inheritDoc} */
    public int getMaxNumberOfPoints()
    {
        return Integer.MAX_VALUE;
    }

    /** {@inheritDoc} */
    public int getMinNumberOfPoints()
    {
        return 1;
    }
}
