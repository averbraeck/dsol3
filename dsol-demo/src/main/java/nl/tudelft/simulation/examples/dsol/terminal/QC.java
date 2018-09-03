package nl.tudelft.simulation.examples.dsol.terminal;

import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * The QCs modeled as resources.
 * <p>
 * copyright (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Jul 25, 2018
 */
public class QC extends IntResource<Double, Double, SimTimeDouble>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** QC time delay. */
    private final DistContinuous qcTime;

    /**
     * @param simulator the simulator
     * @param description the description
     * @param capacity the capacity
     * @param qcTime QC time delay
     */
    public QC(final DEVSSimulatorInterface.TimeDouble simulator, final String description, final long capacity,
            final DistContinuous qcTime)
    {
        super(simulator, description, capacity);
        this.qcTime = qcTime;
    }

    /**
     * @return the QC handling time
     */
    public double drawDelay()
    {
        return this.qcTime.draw();
    }
}
