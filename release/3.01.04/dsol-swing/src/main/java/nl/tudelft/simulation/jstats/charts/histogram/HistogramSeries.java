package nl.tudelft.simulation.jstats.charts.histogram;

import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;

import org.jfree.data.general.AbstractDataset;

/**
 * The serie defines a histogram series containing the entries of a set.
 * <p>
 * (c) copyright 2002-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="mailto:a.verbraeck@tudelft.nl"> Alexander Verbraeck </a> <br>
 *         <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:39:02 $
 * @since 1.5
 */
public class HistogramSeries extends AbstractDataset implements EventListenerInterface
{
    /** default UId. */
    private static final long serialVersionUID = 1L;

    /** name refers to the name of the serie. */
    private String name;

    /** bins refers to the bins in this serie. */
    private Bin[] bins = null;

    /**
     * constructs a new HistogramSeries.
     * @param name the name of the dataset
     * @param domain the domain of the serie
     * @param range the range of the serie
     * @param numberOfBins the number of bins to be used
     */
    public HistogramSeries(final String name, final double[] domain, final double[] range, final int numberOfBins)
    {

        super();
        this.name = name;
        this.bins = new Bin[numberOfBins + 2];
        double binWidth = (domain[1] - domain[0]) / numberOfBins * 1.0;
        double min = domain[0] - binWidth;
        for (int i = 0; i < numberOfBins + 2; i++)
        {
            this.bins[i] = new Bin(new double[]{min, min + binWidth}, range);
            if (range != null)
            {
                this.bins[i].setFixed(true);
            }
            min = min + binWidth;
        }
        this.fireDatasetChanged();
    }

    /**
     * returns the name of the serie
     * @return String the name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * returns the maxX value for bin
     * @param bin the bin number
     * @return Number
     */
    public double getEndXValue(final int bin)
    {
        return this.bins[bin].getEndXValue();
    }

    /**
     * returns the maxY value
     * @param bin the bin number
     * @return Number
     */
    public double getEndYValue(final int bin)
    {
        return this.bins[bin].getEndYValue();
    }

    /**
     * returns the minimumX value
     * @param bin the bin number
     * @return Number
     */
    public double getStartXValue(final int bin)
    {
        return this.bins[bin].getStartXValue();
    }

    /**
     * returns the minimumY value
     * @param bin the bin number
     * @return Number
     */
    public double getStartYValue(final int bin)
    {
        return this.bins[bin].getStartYValue();
    }

    /**
     * returns the number of bins in the histogram
     * @return int
     */
    public int getBinCount()
    {
        return this.bins.length;
    }

    /**
     * returns the x value
     * @param bin the bin number
     * @return Number
     */
    public double getXValue(final int bin)
    {
        return this.bins[bin].getXValue();
    }

    /**
     * returns the Y value
     * @param bin the bin number
     * @return Number
     */
    public int getYValue(final int bin)
    {
        return this.bins[bin].getYValue();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void notify(final EventInterface event)
    {
        double value = ((Double) event.getContent()).doubleValue();
        this.bins[this.resolveBin(value)].increase();
        this.fireDatasetChanged();
    }

    /**
     * resolves the bin for a particular value
     * @param value the value of the incomming event
     * @return int the bin number
     */
    private int resolveBin(final double value)
    {
        if (value <= this.bins[1].getStartXValue())
        {
            return 0;
        }
        for (int i = 1; i < this.bins.length - 1; i++)
        {
            if (value > this.bins[i].getStartXValue() && value <= this.bins[i].getEndXValue())
            {
                return i;
            }
        }
        return this.bins.length - 1;
    }

    /**
     * defines the bins in the histogram.
     */
    private class Bin
    {
        /** the domain of the bin. */
        private double[] domain;

        /** the range of the bin. */
        private double[] range = null;

        /** observations refers to the value of this bin. */
        private int observations = 0;

        /** fixed refers to autoscaling versus fixed scaling. */
        private boolean fixed = false;

        /**
         * constructs a new Bin.
         * @param domain the domain of the bin
         * @param range the range of the bin
         */
        public Bin(final double[] domain, final double[] range)
        {
            this.domain = domain;
            if (range == null)
            {
                this.range = new double[]{0, 1};
            }
            else
            {
                this.range = range;
                this.fixed = true;
            }
        }

        /**
         * increases the value of the bin with 1
         */
        public void increase()
        {
            this.observations++;
            if (!this.fixed && this.observations >= this.range[1])
            {
                this.range[1] = this.observations;
            }
        }

        /**
         * returns the minimum x value
         * @return Number
         */
        public double getStartXValue()
        {
            return this.domain[0];
        }

        /**
         * returns the minimum y value
         * @return Number
         */
        public double getStartYValue()
        {
            return this.range[0];
        }

        /**
         * returns the maximum X value
         * @return Number
         */
        public double getEndXValue()
        {
            return this.domain[1];
        }

        /**
         * returns the maximum Y value
         * @return Number
         */
        public double getEndYValue()
        {
            return this.range[1];
        }

        /**
         * returns the x value
         * @return Number
         */
        public synchronized double getXValue()
        {
            return 0.5 * (this.domain[1] - this.domain[0]);
        }

        /**
         * returns the y value
         * @return Number
         */
        public int getYValue()
        {
            return this.observations;
        }

        /**
         * sets the fixed attributed
         * @param fixed is the bin fixed in range
         */
        public void setFixed(final boolean fixed)
        {
            this.fixed = fixed;
        }

    }
}