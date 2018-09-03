package nl.javel.gisbeans.map;

/**
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 * @version 1.2 Jun 22, 2004
 * @since 1.4
 */
public class StaticAttribute extends AbstractAttribute
{
    /** the static angle. */
    private double angle = 0.0;

    /** the static value. */
    private String value = null;

    /**
     * constructs a new StaticAttribute.
     * @param layer the layer
     * @param angle the angle
     * @param value the value
     */
    public StaticAttribute(LayerInterface layer, double angle, String value)
    {
        super(layer);
        this.angle = angle;
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public double getAngle(int shapeIndex)
    {
        return this.angle;
    }

    /** {@inheritDoc} */
    @Override
    public String getValue(int shapeIndex)
    {
        return this.value;
    }
}
