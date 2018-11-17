/*
 * Created on Feb 23, 2005 TODO To change the template for this generated file
 * go to Window - Preferences - Java - Code Style - Code Templates
 */
package nl.tudelft.simulation.dsol.hla;

import java.util.Hashtable;
import java.util.Properties;

import org.djutils.io.URLResource;
import org.djutils.logger.CategoryLogger;

import hla.rti.EnableTimeConstrainedWasNotPending;
import hla.rti.EnableTimeRegulationWasNotPending;
import hla.rti.FederateInternalError;
import hla.rti.InvalidFederationTime;
import hla.rti.LogicalTime;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Experiment;
import nl.tudelft.simulation.dsol.hla.callBack.RTIAmbassador;
import nl.tudelft.simulation.dsol.hla.simulators.HLADEVSSimulator;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import se.pitch.prti.LogicalTimeDouble;
import se.pitch.prti.LogicalTimeIntervalDouble;
import se.pitch.prti.RTI;

/**
 * @author peter TODO To change the template for this generated type comment go to Window - Preferences - Java - Code
 *         Style - Code Templates
 */
public class HLAModel extends DSOLFederateAmbassador implements DSOLModel
{
    /** the RTI Ambassador to use. */
    protected RTIAmbassador rtiAmbassador = null;

    /** the barrier we wait for. */
    protected Barrier barrier = new Barrier();

    /** the targetModel to use. */
    protected DSOLModel targetModel = null;

    /**
     * constructs a new HLAModel.
     * @param targetModel DSOLModel; the targetModel
     * @param rtIAmbassador the rtIAmbassador
     */
    public HLAModel(final DSOLModel targetModel, final RTIAmbassador rtiAmbassador, final Hashtable environment)
    {
        super();
        this.targetModel = targetModel;
        try
        {
            this.rtiAmbassador = rtiAmbassador;
            this.federateHandle =
                    this.rtiAmbassador.joinFederationExecution(environment.get("rti.federation.type").toString(),
                            environment.get("rti.federation.name").toString(), this);

            this.rtiAmbassador.enableTimeConstrained(false);
            Logger.info(this, "<init>", "Raising for Success TimeConstraint");
            this.barrier.raise("enableTimeConstrained");
            // Lookahaed
            this.rtiAmbassador.enableTimeRegulation(new LogicalTimeDouble(0.0), new LogicalTimeIntervalDouble(0),
                    false);
            Logger.info(this, "<init>", "Raising for Success TimeRegulate");
            this.barrier.raise("enableTimeRegulation");
            Logger.info(this, "<init>", "Ready intialize");
        }
        catch (Exception exception)
        {
            CategoryLogger.always().warn(exception, "<init>");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface arg0) throws SimRuntimeException
    {
        try
        {
            // We wait for the first announce READY_TO_POPULATE
            if (!super.synchronizationPoints.contains(READY_TO_POPULATE))
            {
                Logger.info(this, "constructModel", "Raising for ReadyToPopulate");
                this.barrier.raise(READY_TO_POPULATE);
            }

            // We are ready to populate, let's do so.
            Logger.info(this, "constructModel", "SynchronizationPointAchieved Populate");
            this.rtiAmbassador.synchronizationPointAchieved(READY_TO_POPULATE, false);
            // We wait for the federation sais we may start to populate
            Logger.info(this, "constructModel", "Raising for others to populate");
            this.barrier.raise(READY_TO_POPULATE);

            // Now we have reached the READY_TO_POPULATE phase; let's construct
            // the model
            Logger.info(this, "constructModel", "Constructructng target model");
            this.targetModel.constructModel(arg0);

            // We wait for the first announce READY_TO_RUN
            if (!super.synchronizationPoints.contains(READY_TO_RUN))
            {
                Logger.info(this, "constructModel", "Raisinf for READYTORUN");
                this.barrier.raise(READY_TO_RUN);
            }
            Logger.info(this, "constructModel", "SynchronizationPointAchieved RUN");
            this.rtiAmbassador.synchronizationPointAchieved(READY_TO_RUN, false);
            // Again we wait for the federation to be ready
            Logger.info(this, "constructModel", "Wait for others to run");
            this.barrier.raise(READY_TO_RUN);

            // Let's do the trick.
            Logger.info(this, "constructModel", "Let's rock & roll");
        }
        catch (Exception exception)
        {
            CategoryLogger.always().error(exception);
            throw new SimRuntimeException(exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void announceSynchronizationPoint(String synchronizationPointLabel, byte[] userSuppliedTag)
            throws FederateInternalError
    {
        super.announceSynchronizationPoint(synchronizationPointLabel, userSuppliedTag);
        // There are two options. We are either already waiting, or have not
        // achieved the point where we can accept the announcement
        if (!this.barrier.isLowered() && this.barrier.getPurpose().equals(synchronizationPointLabel))
        {
            // Let's continue!
            this.barrier.lower();
        }
        else
        {
            super.synchronizationPoints.add(synchronizationPointLabel);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void federationSynchronized(final String arg0)
    {
        super.federationSynchronized(arg0);
        this.barrier.lower();
    }

    /** {@inheritDoc} */
    @Override
    public void timeConstrainedEnabled(LogicalTime theFederateTime)
            throws InvalidFederationTime, EnableTimeConstrainedWasNotPending, FederateInternalError
    {
        super.timeConstrainedEnabled(theFederateTime);
        this.barrier.lower();
    }

    /** {@inheritDoc} */
    @Override
    public void timeRegulationEnabled(LogicalTime theFederateTime)
            throws InvalidFederationTime, EnableTimeRegulationWasNotPending, FederateInternalError
    {
        super.timeRegulationEnabled(theFederateTime);
        this.barrier.lower();
    }

    /**
     * executes the manager
     * @param args String[]; the command line arguments
     */
    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.out.println("java HLAModel [rti.properties.file] [experiment]");
            System.exit(0);
        }
        try
        {
            Properties properties = new Properties();
            properties.load(URLResource.getResourceAsStream(args[0]));

            String host = properties.get("rti.host").toString();
            int port = Integer.valueOf(properties.get("rti.port").toString()).intValue();
            RTIAmbassador rtiAmbassador = new RTIAmbassador(RTI.getRTIambassador(host, port));

            Experiment experiment = ExperimentParser.parseExperiment(URLResource.getResource(args[1]));
            HLAModel model = new HLAModel(experiment.getModel(), rtiAmbassador, properties);
            experiment.setModel(model);
            experiment.setSimulator(new HLADEVSSimulator(rtiAmbassador, model));
            experiment.start();
        }
        catch (Exception exception)
        {
            CategoryLogger.always().error(exception);
        }
    }
}
