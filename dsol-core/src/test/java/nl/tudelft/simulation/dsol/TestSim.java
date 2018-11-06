package nl.tudelft.simulation.dsol;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class TestSim
{
    /** */
    private DEVSSimulatorInterface.TimeDouble devsSimulator;

    /*- 
    No need to specify everything separately anymore for a single replication, like it used to be:
    Context context = new InitialContext();
    Experiment.TimeDouble experiment = new Experiment.TimeDouble(context);
    experiment.setModel(model);
    Replication.TimeDouble rep = new Replication.TimeDouble(context, experiment);
    Treatment.TimeDouble treatment = new Treatment.TimeDouble(experiment, "tr1", new SimTimeDouble(0.0), 0.0, 100.0);
    experiment.setTreatment(treatment);
    
    Now this is sufficient:
    Replication.TimeDouble rep = new Replication.TimeDouble("rep1", new SimTimeDouble(0.0), 0.0, 100.0, model);
     */

    /**
     * @throws SimRuntimeException in case of error
     * @throws RemoteException in case of error
     * @throws NamingException in case of error
     */
    public TestSim() throws SimRuntimeException, RemoteException, NamingException
    {
        this.devsSimulator = new DEVSSimulator.TimeDouble();
        Model model = new Model(this.devsSimulator);
        Replication.TimeDouble rep = Replication.TimeDouble.create("rep1", 0.0, 0.0, 100.0, model);
        this.devsSimulator.initialize(rep, ReplicationMode.TERMINATING);

        for (int i = 0; i < 10; i++)
        {
            this.devsSimulator.scheduleEventAbs(1.0d * i, this, this, "print", null);
        }
        this.devsSimulator.start();
    }

    /**
     * @throws RemoteException in case of error
     */
    protected final void print() throws RemoteException
    {
        System.out.println("print scheduled at t=" + this.devsSimulator.getSimulatorTime().doubleValue());
    }

    /**
     * @param args args for main, not used.
     * @throws SimRuntimeException in case of error
     * @throws RemoteException in case of error
     * @throws NamingException in case of error
     */
    public static void main(final String[] args) throws SimRuntimeException, RemoteException, NamingException
    {
        new TestSim();
    }

    /** */
    public class Model extends AbstractDSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * @param simulator the simulator.
         */
        public Model(final DEVSSimulatorInterface.TimeDouble simulator)
        {
            super(simulator);
        }

        /** {@inheritDoc} */
        @Override
        public void constructModel() throws SimRuntimeException
        {
            //
        }

    }
}
