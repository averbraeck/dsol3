package nl.tudelft.simulation.examples.dsol.terminal;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The 'active' container object.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Jul 25, 2018
 */
public class Container implements IntResourceRequestorInterface<Double, Double, SimTimeDouble>
{
    /** the simulator. */
    private final DEVSSimulatorInterface.TimeDouble simulator;

    /** the container number. */
    private final int containerNumber;

    /** the QC resources. */
    private final QC qc;

    /** the AGV resources. */
    private final AGV agv;

    /** the ship. */
    private final Ship ship;

    /** phase. */
    private int phase = 0;

    /**
     * @param simulator the simulator
     * @param containerNumber the container number
     * @param qc the QC resources
     * @param agv the AGV resources
     * @param ship the ship
     */
    public Container(final DEVSSimulatorInterface.TimeDouble simulator, final int containerNumber, final QC qc,
            final AGV agv, final Ship ship)
    {
        this.simulator = simulator;
        this.containerNumber = containerNumber;
        this.qc = qc;
        this.agv = agv;
        this.ship = ship;
        synchronized (ship)
        {
            try
            {
                if (Terminal.DEBUG)
                {
                    System.out.println("T = " + this.simulator.getSimulatorTime() + ", Claim AGV for container "
                            + this.containerNumber);
                }
                this.simulator.scheduleEventAbs(39.0 * 60.0, this, this, "checkPhase", null);
                this.agv.requestCapacity(1, this);
                this.phase++;
            }
            catch (SimRuntimeException | RemoteException e)
            {
                SimLogger.always().error(e);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void receiveRequestedResource(final long requestedCapacity,
            final IntResource<Double, Double, SimTimeDouble> resource) throws RemoteException
    {
        try
        {
            if (resource instanceof AGV)
            {
                this.phase++;
                this.simulator.scheduleEventRel(this.agv.drawDelay(), this, this, "agvReady", null);
            }

            if (resource instanceof QC)
            {
                if (Terminal.DEBUG)
                {
                    System.out.println("T = " + this.simulator.getSimulatorTime() + ", Claim QC for container "
                            + this.containerNumber);
                }
                this.phase++;
                this.simulator.scheduleEventRel(this.qc.drawDelay(), this, this, "qcReady", null);
            }
        }
        catch (SimRuntimeException e)
        {
            SimLogger.always().error(e);
        }
    }

    /** */
    protected synchronized void agvReady()
    {
        try
        {
            this.phase++;
            if (Terminal.DEBUG)
            {
                System.out.println("T = " + this.simulator.getSimulatorTime() + ", AGV ready for container "
                        + this.containerNumber);
            }
            this.agv.releaseCapacity(1);
            this.qc.requestCapacity(1, this);
        }
        catch (SimRuntimeException | RemoteException e)
        {
            SimLogger.always().error(e);
        }
    }

    /** */
    protected synchronized void qcReady()
    {
        try
        {
            if (Terminal.DEBUG)
            {
                System.out.println("T = " + this.simulator.getSimulatorTime() + ", QC ready for container "
                        + this.containerNumber);
            }
            this.qc.releaseCapacity(1);
            this.phase++;
            this.ship.incContainers();
        }
        catch (RemoteException e)
        {
            SimLogger.always().error(e);
        }
    }

    /** */
    protected void checkPhase()
    {
        if (this.phase != 5)
        {
            System.out.println("Container " + this.containerNumber + " was stuck in phase " + this.phase);
        }
    }
}
