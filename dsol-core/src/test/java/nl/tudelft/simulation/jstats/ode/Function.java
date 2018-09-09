package nl.tudelft.simulation.jstats.ode;

/**
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Apr 20, 2004
 * @since 1.5
 */
public class Function extends DifferentialEquation
{
    /**
     * constructs a new Function.
     * @param stepSize the stepSize
     * @param integrationMethod the methodOfIntegration
     */
    public Function(final double stepSize, final short integrationMethod)
    {
        super(stepSize, integrationMethod);
        super.initialize(0, new double[]{0.5, 1.5});
    }

    /** {@inheritDoc} */
    @Override
    public double[] dy(final double x, final double[] y)
    {
        return new double[]{y[1], -0.2 * y[1] - Math.sin(y[0])};
    }
}
