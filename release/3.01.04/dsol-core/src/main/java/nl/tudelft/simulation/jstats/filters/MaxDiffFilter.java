package nl.tudelft.simulation.jstats.filters;

import nl.tudelft.simulation.language.filters.AbstractFilter;

/**
 * The MaxDiffFilter accepts entries if their value is larger than the percentage of the last received Value.
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the
 * Netherlands.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a>
 * <p>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty
 * <p>
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang
 *         </a><a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:41 $
 * @since 1.5
 */
public class MaxDiffFilter extends AbstractFilter
{
    /** the snippet representing the xRange for this filter. */
    private double acceptedDifferencePercentage = Double.NaN;

    /** the amount of points already accepted. */
    private double lastReceivedValue = -Double.MAX_VALUE;

    /**
     * constructs a new MaxDiffFilter.
     * @param acceptedDifferencePercentage the maximum accepted difference percentage, e.g. 10%
     */
    public MaxDiffFilter(final double acceptedDifferencePercentage)
    {
        super();
        if (acceptedDifferencePercentage <= 0 && acceptedDifferencePercentage > 1.0)
        {
            throw new IllegalArgumentException("percentage should be between [0.0,1.0]");
        }
        this.acceptedDifferencePercentage = acceptedDifferencePercentage;
    }

    /**
     * filters based on the maximum difference.
     * @param entry we expect a double[2] representing x,y as input.
     * @see nl.tudelft.simulation.language.filters.AbstractFilter#filter(java.lang.Object)
     * @return whether to accept the entry
     */
    @Override
    public boolean filter(final Object entry)
    {
        if (!(entry instanceof double[]) || ((double[]) entry).length != 2)
        {
            throw new IllegalArgumentException("entry should be instance of double[2] representing x,y");
        }
        double[] value = (double[]) entry;
        if ((Math.abs(value[1] - this.lastReceivedValue)) >= this.lastReceivedValue * this.acceptedDifferencePercentage)
        {
            this.lastReceivedValue = value[1];
            return true;
        }
        this.lastReceivedValue = value[1];
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String getCriterium()
    {
        return "accepts entries if their value>" + this.acceptedDifferencePercentage + "% of the last received Value";
    }
}
