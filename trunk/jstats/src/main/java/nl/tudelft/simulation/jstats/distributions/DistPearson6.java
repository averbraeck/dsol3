/*
 * @(#)DistPearson6.java Apr 3, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;
import cern.jet.stat.Gamma;

/**
 * The Pearson6 distribution. For more information on this distribution see <a
 * href="http://mathworld.wolfram.com/Pearson6Distribution.html"> http://mathworld.wolfram.com/Pearson6Distribution.html
 * </a>
 * <p>
 * (c) copyright 2002-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="mailto:a.verbraeck@tudelft.nl"> Alexander Verbraeck </a> <br>
 *         <a href="http://www.peter-jacobs.com/index.htm"> Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:39 $
 * @since 1.5
 */
public class DistPearson6 extends DistContinuous
{
    /** dist1 is the first gamma distribution */
    private DistGamma dist1;

    /** dist2 is the second gamma distribution */
    private DistGamma dist2;

    /** alpha1 is the first shape parameter */
    private double alpha1;

    /** alpha2 is the second shape parameter */
    private double alpha2;

    /** beta is the scale parameter */
    private double beta;

    /**
     * constructs a new Pearson5 distribution
     * @param stream the numberstream
     * @param alpha1 the first shape parameter
     * @param alpha2 the second shape parameter
     * @param beta the scale parameter
     */
    public DistPearson6(final StreamInterface stream, final double alpha1, final double alpha2, final double beta)
    {
        super(stream);
        if ((alpha1 > 0.0) && (alpha2 > 0.0) && (beta > 0.0))
        {
            this.alpha1 = alpha1;
            this.alpha2 = alpha2;
            this.beta = beta;
        }
        else
        {
            throw new IllegalArgumentException("Error alpha1 <= 0.0 or alpha2 <= 0.0 or beta <= 0.0");
        }
        this.dist1 = new DistGamma(super.stream, this.alpha1, this.beta);
        this.dist2 = new DistGamma(super.stream, this.alpha2, this.beta);
    }

    /**
     * @see DistContinuous#draw()
     */
    @Override
    public double draw()
    {
        // according to Law and Kelton, Simulation Modeling and Analysis, 1991
        // page 494
        return this.dist1.draw() / this.dist2.draw();
    }

    /**
     * @see nl.tudelft.simulation.jstats.distributions.DistContinuous #probDensity(double)
     */
    @Override
    public double probDensity(final double observation)
    {
        if (observation > 0)
        {
            return Math.pow(observation / this.beta, this.alpha1 - 1)
                    / (this.beta * Gamma.beta(this.alpha1, this.alpha2) * Math.pow(1 + (observation / this.beta),
                            (this.alpha1 + this.alpha2)));
        }
        return 0;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Pesrson6(" + this.alpha1 + "," + this.alpha2 + "," + this.beta + ")";
    }
}