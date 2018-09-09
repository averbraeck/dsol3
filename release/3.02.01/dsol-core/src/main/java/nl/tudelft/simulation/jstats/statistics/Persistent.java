package nl.tudelft.simulation.jstats.statistics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.event.TimedEvent;

/**
 * The Persisten class defines a statistics event persistent. A Persistent is a time-averaged tally.
 * <p>
 * (c) 2002-2018-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:40 $
 * @since 1.5
 */
public class Persistent extends Tally
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** VALUE_EVENT is fired whenever on a change in measurements. */
    public static final EventType VALUE_EVENT = new EventType("VALUE_EVENT");

    /** startTime defines the time of the first event. */
    private double startTime = Double.NaN;

    /** elapsedTime tracks the elapsed time. */
    private double elapsedTime = Double.NaN;

    /** deltaTime defines the time between 2 events. */
    private double deltaTime = Double.NaN;

    /** lastvalue tracks the last value. */
    private double lastValue = Double.NaN;
    
    /** the logger. */
    private static Logger logger = LogManager.getLogger(Persistent.class);

    /**
     * constructs a new Persistent with a description.
     * @param description the description of this Persistent
     */
    public Persistent(final String description)
    {
        super(description);
    }

    /** {@inheritDoc} */
    @Override
    public double getStdDev()
    {
        synchronized (this.semaphore)
        {
            if (super.n > 1)
            {
                return Math.sqrt(super.varianceSum / (this.elapsedTime - this.deltaTime));
            }
            return Double.NaN;
        }
    }

    /** {@inheritDoc} */
    @Override
    public double getSampleVariance()
    {
        synchronized (this.semaphore)
        {
            if (super.n > 1)
            {
                return super.varianceSum / (this.elapsedTime - this.deltaTime);
            }
            return Double.NaN;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void initialize()
    {
        synchronized (this.semaphore)
        {
            super.initialize();
            this.deltaTime = 0.0;
            this.elapsedTime = 0.0;
            this.lastValue = 0.0;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        if (!(event instanceof TimedEvent<?>) || !(event.getContent() instanceof Number))
        {
            throw new IllegalArgumentException(
                    "event != TimedEvent || event.source != Double (" + event.getContent().getClass().toString() + ")");
        }
        // TODO what if this is not a Double?
        TimedEvent<Double> timedEvent = (TimedEvent<Double>) event;
        double value = 0.0;
        if (event.getContent() instanceof Number)
        {
            value = ((Number) event.getContent()).doubleValue();
        }
        else
        {
            logger.warn("notify: " + event.getContent() + "should be a number.");
        }

        synchronized (this.semaphore)
        {
            super.fireTimedEvent(Persistent.VALUE_EVENT, this.lastValue, timedEvent.getTimeStamp());
            super.fireTimedEvent(Persistent.VALUE_EVENT, value, timedEvent.getTimeStamp());
            super.setN(super.n + 1); // we increase the number of measurements.
            if (value < super.min)
            {
                super.setMin(value);
            }
            if (value > super.max)
            {
                super.setMax(value);
            }
            super.setSum(super.sum + value);

            // see Knuth's The Art Of Computer Programming Volume II: Seminumerical Algorithms
            if (this.n == 1)
            {
                super.setSampleMean(value);
                this.startTime = timedEvent.getTimeStamp();
            }
            else
            {
                this.deltaTime = timedEvent.getTimeStamp() - (this.elapsedTime + this.startTime);
                if (this.deltaTime > 0.0)
                {
                    double newAverage = ((super.sampleMean * (this.elapsedTime)) + (this.lastValue * this.deltaTime))
                            / (this.elapsedTime + this.deltaTime);
                    super.varianceSum +=
                            (this.lastValue - super.sampleMean) * (this.lastValue - newAverage) * this.deltaTime;
                    super.setSampleMean(newAverage);
                    this.elapsedTime = this.elapsedTime + this.deltaTime;
                }
            }
            if (this.n > 1)
            {
                super.fireEvent(Tally.STANDARD_DEVIATION_EVENT, this.getStdDev());
                this.fireEvent(Tally.SAMPLE_VARIANCE_EVENT, this.getSampleVariance());
            }
            this.lastValue = value;
        }
    }
}
