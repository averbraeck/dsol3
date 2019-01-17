/*
 * Created on Feb 16, 2005 TODO To change the template for this generated file
 * go to Window - Preferences - Java - Code Style - Code Templates
 */
package nl.tudelft.simulation.dsol.hla.manager;

import java.util.Hashtable;
import java.util.Properties;

import org.djutils.io.URLResource;
import org.djutils.logger.CategoryLogger;

import hla.rti.AttributeHandleSet;
import hla.rti.FederateInternalError;
import nl.tudelft.simulation.dsol.hla.Barrier;
import nl.tudelft.simulation.dsol.hla.DSOLFederateAmbassador;
import nl.tudelft.simulation.dsol.hla.callBack.RTIAmbassador;
import se.pitch.prti.RTI;

/**
 * @author peter TODO To change the template for this generated type comment go to Window - Preferences - Java - Code
 *         Style - Code Templates
 */
public class FederationManager extends DSOLFederateAmbassador
{
    /** The MOM_FEDERATe. */
    public static final String FEDERATE_CLASS_NAME = "objectRoot.manager.federate";

    /** The attributes to subscribe to. */
    protected static final String FEDERATE_TYPE_ATTRIBUTE = "federateType";

    /** the FEDERATE_HANDLE_ATTRIBUTe. */
    protected static final String FEDERATE_HOST_ATTRIBUTE = "federateHost";

    /** FEDERATE_HANDLE_ATTRIBUTE attribute. */
    protected static final String FEDERATE_HANDLE_ATTRIBUTE = "federateHandle";

    /** the rtiAmbassador. */
    protected RTIAmbassador rtiAmbassador = null;

    /** the barrier. */
    protected Barrier barrier = new Barrier();

    /** the populateReadinessController. */
    protected PopulateReadinessController populuateReadinessController = null;

    /**
     * constructs a new FederationManager().
     * @param federationName the federate to join
     * @param populateReadinessController the populateReadinessController to use
     */
    public FederationManager(final Hashtable environment, final int amountOfFederatesInFederation)
    {
        this(environment, new FederateCounter(amountOfFederatesInFederation));
    }

    /**
     * constructs a new FederationManager().
     * @param federationName the federate to join
     * @param populateReadinessController PopulateReadinessController; the populateReadinessController to use
     */
    public FederationManager(final Hashtable environment, final PopulateReadinessController populateReadinessController)
    {
        super();
        try
        {
            this.populuateReadinessController = populateReadinessController;
            String host = environment.get("rti.host").toString();
            int port = Integer.valueOf(environment.get("rti.port").toString()).intValue();
            this.rtiAmbassador = new RTIAmbassador(RTI.getRTIambassador(host, port));
            this.rtiAmbassador.joinFederationExecution(environment.get("rti.federation.type").toString(),
                    environment.get("rti.federation.name").toString(), this);

            // Now we subscribe ourself to changes in the managerFederate.
            int managerFederate = this.rtiAmbassador.getObjectClassHandle(FederationManager.FEDERATE_CLASS_NAME);
            int handleAttribute =
                    this.rtiAmbassador.getAttributeHandle(FederationManager.FEDERATE_HANDLE_ATTRIBUTE, managerFederate);
            int typeAttribute =
                    this.rtiAmbassador.getAttributeHandle(FederationManager.FEDERATE_TYPE_ATTRIBUTE, managerFederate);
            int hostAttribute =
                    this.rtiAmbassador.getAttributeHandle(FederationManager.FEDERATE_HOST_ATTRIBUTE, managerFederate);
            AttributeHandleSet attributeHandleSet = RTI.attributeHandleSetFactory().create();
            attributeHandleSet.add(handleAttribute);
            attributeHandleSet.add(typeAttribute);
            attributeHandleSet.add(hostAttribute);
            this.rtiAmbassador.subscribeObjectClassAttributes(managerFederate, attributeHandleSet, false);

            // Now we create the synchronization points.
            // First we will have to wait for the ReadyToPopulate
            // synchronization
            this.rtiAmbassador.registerFederationSynchronizationPoint(READY_TO_POPULATE, null, false);
            // We wait for the registration to be successfull and the announcement to occur
            this.barrier.raise(READY_TO_POPULATE);

            // We wait until everyone shows up
            this.barrier.raise(READY_TO_POPULATE);

            // Now we register the READY TO RUN
            this.rtiAmbassador.registerFederationSynchronizationPoint(READY_TO_RUN, null, false);

            // We wait for the registration to be successfull and the announcement to occur
            this.barrier.raise(READY_TO_RUN);
            this.rtiAmbassador.synchronizationPointAchieved(READY_TO_RUN, false);

            // Let's register the READY TO RESIGN
            // Now we can register for a Ready to Run and a Ready to Resign
            this.rtiAmbassador.registerFederationSynchronizationPoint(READY_TO_RESIGN, null, false);

            // We wait for the registration to be successfull and the announcement to occur
            this.barrier.raise(READY_TO_RESIGN);
            this.rtiAmbassador.synchronizationPointAchieved(READY_TO_RESIGN, false);

        }
        catch (Exception exception)
        {
            CategoryLogger.always().error(exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void federationSynchronized(String arg0)
    {
        super.federationSynchronized(arg0);
        try
        {
            if (arg0.equals(READY_TO_POPULATE))
            {
                this.barrier.lower();
            }
        }
        catch (Exception exception)
        {
            CategoryLogger.always().warn("federationSynchronized", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void synchronizationPointRegistrationSucceeded(final String arg0)
    {
        super.synchronizationPointRegistrationSucceeded(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public void discoverObjectInstance(int theObject, int theObjectClass, String objectName)
    {
        super.discoverObjectInstance(theObject, theObjectClass, objectName);
        try
        {
            if (this.populuateReadinessController.discoverObjectInstance(theObject, theObjectClass, objectName))
            {
                this.rtiAmbassador.synchronizationPointAchieved(READY_TO_POPULATE, true);
            }
        }
        catch (Exception exception)
        {
            CategoryLogger.always().warn("discoverObjectInstance", exception);
        }
    }

    /**
     * @return Returns the populuateReadinessController.
     */
    public PopulateReadinessController getPopuluateReadinessController()
    {
        return this.populuateReadinessController;
    }

    /**
     * @param populuateReadinessController PopulateReadinessController; The populuateReadinessController to set.
     */
    public void setPopuluateReadinessController(final PopulateReadinessController populuateReadinessController)
    {
        this.populuateReadinessController = populuateReadinessController;
    }

    /**
     * executes the manager
     * @param args String[]; the command line arguments
     */
    public static void main(String[] args)
    {
        if (args.length < 1 || args.length > 2)
        {
            System.out.println("java FederationManager [rti.properties.file] [# of federates (optional: default = 3)]");
            System.exit(0);
        }
        try
        {
            Properties properties = new Properties();
            properties.load(URLResource.getResourceAsStream(args[0]));
            int numberOfFeds = 3;
            if (args.length == 2)
                numberOfFeds = Integer.parseInt(args[1]);
            new FederationManager(properties, numberOfFeds);
        }
        catch (Throwable exception)
        {
            CategoryLogger.always().error(exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void announceSynchronizationPoint(String synchronizationPointLabel, byte[] userSuppliedTag)
            throws FederateInternalError
    {
        super.announceSynchronizationPoint(synchronizationPointLabel, userSuppliedTag);
        this.barrier.lower();
    }
}
