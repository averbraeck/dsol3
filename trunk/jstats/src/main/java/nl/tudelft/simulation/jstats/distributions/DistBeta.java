/*
 * @(#)DistBeta.java Apr 3, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;
import cern.jet.stat.Gamma;

/**
 * The Beta distribution. For more information on this distribution see <a
 * href="http://mathworld.wolfram.com/BetaDistribution.html">
 * http://mathworld.wolfram.com/BetaDistribution.html </a>
 * <p>
 * (c) copyright 2002-2004 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">
 * www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="mailto:a.verbraeck@tudelft.nl">
 *         Alexander Verbraeck </a> <br>
 *         <a href="http://www.peter-jacobs.com/index.htm"> Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:39 $
 * @since 1.5
 */
public class DistBeta extends DistContinuous
{

    /** dist1 refers to the first Gamma distribution */
    private DistGamma dist1;

    /** dist2 refers to the second Gamma distribution */
    private DistGamma dist2;

    /** alpha1 is the first parameter for the Beta distribution */
    private double alpha1;

    /** alpha2 is the second parameter for the Beta distribution */
    private double alpha2;

    /**
     * constructs a new beta distribution.
     * 
     * @param stream the stream.
     * @param alpha1 the first alpha parameter for the distribution.
     * @param alpha2 the second alpha parameter for the distribution.
     */
    public DistBeta(final StreamInterface stream, final double alpha1,
            final double alpha2)
    {
        super(stream);
        if ((alpha1 > 0.0) && (alpha2 > 0.0))
        {
            this.alpha1 = alpha1;
            this.alpha2 = alpha2;
        } else
        {
            throw new IllegalArgumentException(
                    "Error alpha1 <= 0.0 or alpha2 <= 0.0");
        }
        this.dist1 = new DistGamma(stream, this.alpha1, 1.0);
        this.dist2 = new DistGamma(stream, this.alpha2, 1.0);
    }

    /**
     * @see DistContinuous#draw()
     */
    @Override
    public double draw()
    {
        // according to Law and Kelton, Simulation Modeling and Analysis, 1991
        // pages 492-493
        double y1 = this.dist1.draw();
        double y2 = this.dist2.draw();
        return y1 / (y1 + y2);
    }

    /**
     * @see nl.tudelft.simulation.jstats.distributions.DistContinuous
     *      #probDensity(double)
     */
    @Override
    public double probDensity(final double observation)
    {
        if (observation > 0 && observation < 1)
        {
            return (Math.pow(observation, this.alpha1 - 1) * Math.pow(
                    1 - observation, this.alpha2 - 1))
                    / Gamma.beta(this.alpha1, this.alpha2);
        }
        return 0;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Beta(" + this.alpha1 + "," + this.alpha2 + ")";
    }
}