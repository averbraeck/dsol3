package nl.tudelft.simulation.jstats.statistics;

import javax.swing.table.TableModel;

import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.jstats.distributions.DistNormal;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;

/**
 * The Tally class defines a statistics event tally.
 * <p>
 * (c) 2002-2018-2004 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:40 $
 * @since 1.5
 */
public class Tally extends StatisticsObject implements EventListenerInterface
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** SAMPLE_MEAN_EVENT is fired whenever the sample mean is updated. */
    public static final EventType SAMPLE_MEAN_EVENT = new EventType("SAMPLE_MEAN_EVENT");

    /**
     * SAMPLE_VARIANCE_EVENT is fired whenever the sample variance is updated.
     */
    public static final EventType SAMPLE_VARIANCE_EVENT = new EventType("SAMPLE_VARIANCE_EVENT");

    /** MIN_EVENT is fired whenever a new minimum value has reached. */
    public static final EventType MIN_EVENT = new EventType("MIN_EVENT");

    /** MAX_EVENT is fired whenever a new maximum value has reached. */
    public static final EventType MAX_EVENT = new EventType("MAX_EVENT");

    /** N_EVENT is fired whenever on a change in measurements. */
    public static final EventType N_EVENT = new EventType("N_EVENT");

    /** STANDARD_DEVIATION_EVENT is fired whenever the standard deviation is updated. */
    public static final EventType STANDARD_DEVIATION_EVENT = new EventType("STANDARD_DEVIATION_EVENT");

    /** SUM_EVENT is fired whenever the sum sis updated. */
    public static final EventType SUM_EVENT = new EventType("SUM_EVENT");

    /** LEFT_SIDE_CONFIDENCE refers to the left side confidence. */
    public static final short LEFT_SIDE_CONFIDENCE = -1;

    /** BOTH_SIDE_CONFIDENCE refers to both sides of the confidence. */
    public static final short BOTH_SIDE_CONFIDENCE = 0;

    /** RIGTH_SIDE_CONFIDENCE refers to the right side confidence. */
    public static final short RIGTH_SIDE_CONFIDENCE = 1;

    /** sum refers to the sum of the tally. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double sum = Double.NaN;

    /** min refers to the min of the tally. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double min = Double.NaN;

    /** max refers to the max of the tally. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double max = Double.NaN;

    /** sampleMean refers to the mean of the tally. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double sampleMean = Double.NaN;

    /** varianceSum refers to the varianceSum of the tally. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double varianceSum = Double.NaN;

    /** n refers to the number of measurements. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected long n = Long.MIN_VALUE;

    /** description refers to the description of this tally. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected String description;

    /** the confidenceDistribution. */
    private DistNormal confidenceDistribution = new DistNormal(new MersenneTwister());

    /** the semaphore. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Object semaphore = new Object();

    /**
     * Constructs a new Tally.
     * @param description the description of this tally
     */
    public Tally(final String description)
    {
        super();
        this.description = description;
    }

    /**
     * Returns the sampleMean of all observations since the initialization.
     * @return double the sampleMean
     */
    public final double getSampleMean()
    {
        return this.sampleMean;
    }

    /**
     * returns the confidence interval on either side of the mean.
     * @param alpha Alpha is the significance level used to compute the confidence level. The confidence level equals
     *            100*(1 - alpha)%, or in other words, an alpha of 0.05 indicates a 95 percent confidence level.
     * @return double[] the confidence interval of this tally
     */
    public final double[] getConfidenceInterval(final double alpha)
    {
        return this.getConfidenceInterval(alpha, Tally.BOTH_SIDE_CONFIDENCE);
    }

    /**
     * returns the confidence interval based of the mean.
     * @param alpha Alpha is the significance level used to compute the confidence level. The confidence level equals
     *            100*(1 - alpha)%, or in other words, an alpha of 0.05 indicates a 95 percent confidence level.
     * @param side the side of the confidence interval with respect to the mean
     * @return double[] the confidence interval of this tally
     */
    public final double[] getConfidenceInterval(final double alpha, final short side)
    {
        if (!(side == LEFT_SIDE_CONFIDENCE || side == BOTH_SIDE_CONFIDENCE || side == RIGTH_SIDE_CONFIDENCE))
        {
            throw new IllegalArgumentException("side of confidence level is not defined");
        }
        if (alpha < 0 || alpha > 1)
        {
            throw new IllegalArgumentException("1 >= confidenceLevel >= 0");
        }
        synchronized (this.semaphore)
        {
            if (new Double(this.sampleMean).isNaN() || new Double(this.getStdDev()).isNaN())
            {
                return null;
            }
            double level = 1 - alpha;
            if (side == Tally.BOTH_SIDE_CONFIDENCE)
            {
                level = 1 - alpha / 2.0;
            }
            double z = this.confidenceDistribution.getInverseCumulativeProbability(level);
            double confidence = z * Math.sqrt(this.getSampleVariance() / this.n);
            double[] result = {this.sampleMean - confidence, this.sampleMean + confidence};
            if (side == Tally.LEFT_SIDE_CONFIDENCE)
            {
                result[1] = this.sampleMean;
            }
            if (side == Tally.RIGTH_SIDE_CONFIDENCE)
            {
                result[0] = this.sampleMean;
            }
            result[0] = Math.max(result[0], this.min);
            result[1] = Math.min(result[1], this.max);
            return result;
        }
    }

    /**
     * returns the description of this tally.
     * @return Sting description
     */
    public final String getDescription()
    {
        return this.description;
    }

    /**
     * Returns the max.
     * @return double
     */
    public final double getMax()
    {
        return this.max;
    }

    /**
     * Returns the min.
     * @return double
     */
    public final double getMin()
    {
        return this.min;
    }

    /**
     * Returns the number of observations.
     * @return long n
     */
    public final long getN()
    {
        return this.n;
    }

    /**
     * Returns the current tally standard deviation.
     * @return double the standard deviation
     */
    @SuppressWarnings("checkstyle:designforextension")
    public double getStdDev()
    {
        synchronized (this.semaphore)
        {
            if (this.n > 1)
            {
                return Math.sqrt(this.varianceSum / (this.n - 1));
            }
            return Double.NaN;
        }
    }

    /**
     * returns the sum of the values of the observations.
     * @return double sum
     */
    public final double getSum()
    {
        return this.sum;
    }

    /**
     * Returns the current tally variance.
     * @return double samplevariance
     */
    @SuppressWarnings("checkstyle:designforextension")
    public double getSampleVariance()
    {
        synchronized (this.semaphore)
        {
            if (this.n > 1)
            {
                return this.varianceSum / (this.n - 1);
            }
            return Double.NaN;
        }
    }

    /** {@inheritDoc} */
    @Override
    public final TableModel getTable()
    {
        String[] columnNames = {"field", "value"};
        EventType[] eventTypes = {null, Tally.N_EVENT, Tally.MIN_EVENT, Tally.MAX_EVENT, Tally.SAMPLE_MEAN_EVENT,
                Tally.SAMPLE_VARIANCE_EVENT, Tally.STANDARD_DEVIATION_EVENT, Tally.SUM_EVENT};
        StatisticsTableModel result = new StatisticsTableModel(columnNames, eventTypes, 8);
        this.addListener(result, Tally.N_EVENT, true);
        this.addListener(result, Tally.MAX_EVENT, true);
        this.addListener(result, Tally.MIN_EVENT, true);
        this.addListener(result, Tally.SAMPLE_MEAN_EVENT, true);
        this.addListener(result, Tally.SAMPLE_VARIANCE_EVENT, true);
        this.addListener(result, Tally.STANDARD_DEVIATION_EVENT, true);
        this.addListener(result, Tally.SUM_EVENT, true);

        result.setValueAt("name", 0, 0);
        result.setValueAt("n", 1, 0);
        result.setValueAt("min", 2, 0);
        result.setValueAt("max", 3, 0);
        result.setValueAt("sample-mean", 4, 0);
        result.setValueAt("sample-variance", 5, 0);
        result.setValueAt("st. dev.", 6, 0);
        result.setValueAt("sum", 7, 0);

        // Since the result is subscribed to the actual values
        // there is no need to create a synchronized block.
        result.setValueAt(this.description, 0, 1);
        result.setValueAt(new Long(this.n), 1, 1);
        result.setValueAt(new Double(this.min), 2, 1);
        result.setValueAt(new Double(this.max), 3, 1);
        result.setValueAt(new Double(this.sampleMean), 4, 1);
        result.setValueAt(new Double(this.getSampleVariance()), 5, 1);
        result.setValueAt(new Double(this.getStdDev()), 6, 1);
        result.setValueAt(new Double(this.getSum()), 7, 1);
        return result;
    }

    /**
     * initializes the Tally. This methods sets the max, min, n, sum and variance values to their initial values.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public void initialize()
    {
        synchronized (this.semaphore)
        {
            this.setMax(-Double.MAX_VALUE);
            this.setMin(Double.MAX_VALUE);
            this.setN(0);
            this.setSum(0.0);
            this.varianceSum = 0.0;
        }
    }

    /**
     * is this tally initialized?
     * @return true whenever this.initialize is invoked.
     */
    public final boolean isInitialized()
    {
        return !Double.isNaN(this.max);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void notify(final EventInterface event)
    {
        if (!(event.getContent() instanceof Number))
        {
            throw new IllegalArgumentException("Tally does not accept " + event);
        }
        double value = ((Number) event.getContent()).doubleValue();

        synchronized (this.semaphore)
        {
            if (new Double(this.sampleMean).isNaN())
            {
                this.sampleMean = 0.0;
            }
            // see Knuth's The Art Of Computer Programming
            // Volume II: Seminumerical Algorithms
            double newsampleMean = this.sampleMean + (value - this.sampleMean) / (this.n + 1);
            this.varianceSum += (value - this.sampleMean) * (value - newsampleMean);
            this.setSampleMean(newsampleMean);
            this.setSum(this.sum + value);
            this.setN(this.n + 1);
            if (value < this.min)
            {
                this.setMin(value);
            }
            if (value > this.max)
            {
                this.setMax(value);
            }
            if (this.n > 1)
            {
                this.fireEvent(Tally.STANDARD_DEVIATION_EVENT, getStdDev());
                this.fireEvent(Tally.SAMPLE_VARIANCE_EVENT, getSampleVariance());
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return this.description;
    }

    // ***************** PROTECTED METHODS **************/
    
    /**
     * sets sampleMean.
     * @param sampleMean the new mean
     * @return double sampleMean
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected double setSampleMean(final double sampleMean)
    {
        this.sampleMean = sampleMean;
        this.fireEvent(Tally.SAMPLE_MEAN_EVENT, sampleMean);
        return this.sampleMean;
    }

    /**
     * sets min.
     * @param min the new minimum value
     * @return double the input
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected double setMin(final double min)
    {
        this.min = min;
        this.fireEvent(Tally.MIN_EVENT, min);
        return this.min;
    }

    /**
     * sets max.
     * @param max the new maximum value
     * @return double the input
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected double setMax(final double max)
    {
        this.max = max;
        this.fireEvent(Tally.MAX_EVENT, max);
        return this.max;
    }

    /**
     * sets n.
     * @param n the new n
     * @return double the input
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected long setN(final long n)
    {
        this.n = n;
        this.fireEvent(Tally.N_EVENT, n);
        return this.n;
    }

    /**
     * sets the count.
     * @param sum the new sum
     * @return double the input
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected double setSum(final double sum)
    {
        this.sum = sum;
        this.fireEvent(Tally.SUM_EVENT, sum);
        return this.sum;
    }
}
