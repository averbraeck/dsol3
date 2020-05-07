package nl.tudelft.simulation.dsol.hla;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.djutils.logger.CategoryLogger;

import hla.rti.AttributeAcquisitionWasNotCanceled;
import hla.rti.AttributeAcquisitionWasNotRequested;
import hla.rti.AttributeAlreadyOwned;
import hla.rti.AttributeDivestitureWasNotRequested;
import hla.rti.AttributeHandleSet;
import hla.rti.AttributeNotKnown;
import hla.rti.AttributeNotOwned;
import hla.rti.AttributeNotPublished;
import hla.rti.CouldNotRestore;
import hla.rti.EnableTimeConstrainedWasNotPending;
import hla.rti.EnableTimeRegulationWasNotPending;
import hla.rti.EventNotKnown;
import hla.rti.EventRetractionHandle;
import hla.rti.FederateAmbassador;
import hla.rti.FederateInternalError;
import hla.rti.FederateOwnsAttributes;
import hla.rti.InteractionClassNotPublished;
import hla.rti.InvalidFederationTime;
import hla.rti.LogicalTime;
import hla.rti.ObjectClassNotPublished;
import hla.rti.ObjectNotKnown;
import hla.rti.ReceivedInteraction;
import hla.rti.ReflectedAttributes;
import hla.rti.SpecifiedSaveLabelDoesNotExist;
import hla.rti.UnableToPerformSave;
import nl.tudelft.simulation.dsol.hla.simulators.HLAInteractionEvent;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;
import se.pitch.prti.LogicalTimeDouble;

/**
 * The DSOLFederateAmbassador specifies the IEEE-1516 federate.
 * <p>
 * copyright (c) 2004-2019 <a href="https://simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm"> Peter Jacobs </a>
 * @since 1.2
 */
public class DSOLFederateAmbassador extends EventProducer implements FederateAmbassador
{
    /** the TIME_ADVANCE_GRANT_EVENT */
    public static final EventType TIME_ADVANCE_GRANT_EVENT = new EventType("TIME_ADVANCE_GRANT_EVENT");

    /** TIME_CONSTRAINED_ENABLED_EVENT */
    public static final EventType TIME_CONSTRAINED_ENABLED_EVENT = new EventType("TIME_CONSTRAINED_ENABLED_EVENT");

    /** TIME_REGULATION_ENABLED_EVENT */
    public static final EventType TIME_REGULATION_ENABLED_EVENT = new EventType("TIME_REGULATION_ENABLED_EVENT");

    /** Published on interaction reception. Value: HLAInteractionEvent. */
    public static final EventType INTERACTION_REC_EVENT = new EventType("INTERACTION_REC_EVENT");

    /** the READY_TO_POPULATe. */
    public static final String READY_TO_POPULATE = "READY_TO_POPULATE";

    /** the READY_TO_RUN */
    public static final String READY_TO_RUN = "READY_TO_RUN";

    /** the READY_TO_RESIGN */
    public static final String READY_TO_RESIGN = "READY_TO_RESIGN";

    /** the context to use for object sharing. */
    protected Context context = null;

    /** to be set after a federation has been joined by this federate. */
    protected int federateHandle = -1;

    /** a list of premature synchronization points. */
    protected List synchronizationPoints = new ArrayList();

    /**
     * constructs a new DSOLFederateAmbassador
     */
    public DSOLFederateAmbassador()
    {
        this(null);
    }

    /**
     * constructs a new DSOLFederateAmbassador
     * @param environment Hashtable; the environment of the context
     */
    public DSOLFederateAmbassador(final Hashtable environment)
    {
        super();
        try
        {
            if (environment != null)
            {
                this.context = new InitialContext(environment);
            }
            else
            {
                this.context = new InitialContext();
            }
        }
        catch (NamingException namingException)
        {
            CategoryLogger.always().error("<init>", namingException);
        }

    }

    /**
     * Returns the federatehandle, or -1 if not joined.
     */
    public int getFederateHandle()
    {
        return federateHandle;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#turnInteractionsOn(int)
     */
    public void turnInteractionsOn(int theHandle) throws InteractionClassNotPublished, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#requestAttributeOwnershipRelease(int, hla.rti.AttributeHandleSet, byte[])
     */
    public void requestAttributeOwnershipRelease(int theObject, AttributeHandleSet candidateAttributes,
            byte[] userSuppliedTag) throws ObjectNotKnown, AttributeNotKnown, AttributeNotOwned, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    @Override
    public void announceSynchronizationPoint(String synchronizationPointLabel, byte[] userSuppliedTag)
            throws FederateInternalError
    {
        Logger.info(this, "announceSynchronizationPoint", synchronizationPointLabel);
    }

    /** {@inheritDoc} */
    @Override
    public void synchronizationPointRegistrationSucceeded(String synchronizationPointLabel)
    {
        Logger.info(this, "synchronizationPointRegistrationSucceeded", synchronizationPointLabel);
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#attributeIsNotOwned(int, int)
     */
    public void attributeIsNotOwned(int theObject, int theAttribute)
    {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    @Override
    public void federationSynchronized(String synchronizationPointLabel)
    {
        Logger.info(this, "federationSynchronized", synchronizationPointLabel);
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#turnInteractionsOff(int)
     */
    public void turnInteractionsOff(int theHandle) throws InteractionClassNotPublished, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    @Override
    public void receiveInteraction(int interactionClass, ReceivedInteraction theInteraction, byte[] userSuppliedTag)
    {
        Logger.warning(this, "receiveInteraction", "Receiving interaction at NO time");
        super.fireEvent(INTERACTION_REC_EVENT, new HLAInteractionEvent(interactionClass, theInteraction, null));
    }

    /** {@inheritDoc} */
    @Override
    public void receiveInteraction(int interactionClass, ReceivedInteraction theInteraction, byte[] userSuppliedTag,
            LogicalTime theTime, EventRetractionHandle eventRetractionHandle)
    {
        Logger.info(this, "receiveInteraction", "Receiving interaction at " + theTime.toString());
        super.fireEvent(INTERACTION_REC_EVENT, new HLAInteractionEvent(interactionClass, theInteraction, theTime));
    }

    /** {@inheritDoc} */
    @Override
    public void requestAttributeOwnershipAssumption(int theObject, AttributeHandleSet offeredAttributes,
            byte[] userSuppliedTag) throws ObjectNotKnown, AttributeNotKnown, AttributeAlreadyOwned,
            AttributeNotPublished, FederateInternalError
    {
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    @Override
    public void confirmAttributeOwnershipAcquisitionCancellation(int theObject, AttributeHandleSet theAttributes)
            throws ObjectNotKnown, AttributeNotKnown, AttributeAlreadyOwned, AttributeAcquisitionWasNotCanceled,
            FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#provideAttributeValueUpdate(int, hla.rti.AttributeHandleSet)
     */
    public void provideAttributeValueUpdate(int theObject, AttributeHandleSet theAttributes)
            throws ObjectNotKnown, AttributeNotKnown, AttributeNotOwned, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#federationRestored()
     */
    public void federationRestored() throws FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#informAttributeOwnership(int, int, int)
     */
    public void informAttributeOwnership(int theObject, int theAttribute, int theOwner)
            throws ObjectNotKnown, AttributeNotKnown, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    @Override
    public void timeAdvanceGrant(LogicalTime theTime)
    {
        Logger.info(this, "timeAdvanceGrant", theTime.toString());
        super.fireEvent(DSOLFederateAmbassador.TIME_ADVANCE_GRANT_EVENT, ((LogicalTimeDouble) theTime).getValue());
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#initiateFederateSave(java.lang.String)
     */
    public void initiateFederateSave(String label) throws UnableToPerformSave, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#attributeOwnershipUnavailable(int, hla.rti.AttributeHandleSet)
     */
    public void attributeOwnershipUnavailable(int theObject, AttributeHandleSet theAttributes) throws ObjectNotKnown,
            AttributeNotKnown, AttributeAlreadyOwned, AttributeAcquisitionWasNotRequested, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#attributeOwnedByRTI(int, int)
     */
    public void attributeOwnedByRTI(int theObject, int theAttribute)
            throws ObjectNotKnown, AttributeNotKnown, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#reflectAttributeValues(int, hla.rti.ReflectedAttributes, byte[])
     */
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] userSuppliedTag)
            throws ObjectNotKnown, AttributeNotKnown, FederateOwnsAttributes, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#reflectAttributeValues(int, hla.rti.ReflectedAttributes, byte[],
     * hla.rti.LogicalTime, hla.rti.EventRetractionHandle)
     */
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] userSuppliedTag,
            LogicalTime theTime, EventRetractionHandle retractionHandle) throws ObjectNotKnown, AttributeNotKnown,
            FederateOwnsAttributes, InvalidFederationTime, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#federationRestoreBegun()
     */
    public void federationRestoreBegun() throws FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#requestFederationRestoreSucceeded(java.lang.String)
     */
    public void requestFederationRestoreSucceeded(String label) throws FederateInternalError
    {
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    @Override
    public void discoverObjectInstance(int theObject, int theObjectClass, String objectName)
    {
        Logger.info(this, "discoverObjectInstance",
                "object=" + theObject + "  class=" + theObjectClass + "  name=" + objectName);
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#attributeOwnershipAcquisitionNotification(int, hla.rti.AttributeHandleSet)
     */
    public void attributeOwnershipAcquisitionNotification(int theObject, AttributeHandleSet securedAttributes)
            throws ObjectNotKnown, AttributeNotKnown, AttributeAcquisitionWasNotRequested, AttributeAlreadyOwned,
            AttributeNotPublished, FederateInternalError
    {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#federationNotSaved()
     */
    public void federationNotSaved() throws FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#requestFederationRestoreFailed(java.lang.String, java.lang.String)
     */
    public void requestFederationRestoreFailed(String label, String reason) throws FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#timeConstrainedEnabled(hla.rti.LogicalTime)
     */
    public void timeConstrainedEnabled(LogicalTime theFederateTime)
            throws InvalidFederationTime, EnableTimeConstrainedWasNotPending, FederateInternalError
    {
        Logger.info(this, "timeConstrainedEnabled", theFederateTime.toString());

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#synchronizationPointRegistrationFailed(java.lang.String)
     */
    public void synchronizationPointRegistrationFailed(String synchronizationPointLabel) throws FederateInternalError
    {
        Logger.info(this, "synchronizationPointRegistrationFailed", synchronizationPointLabel);

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#initiateFederateRestore(java.lang.String, int)
     */
    public void initiateFederateRestore(String label, int federateHandle)
            throws SpecifiedSaveLabelDoesNotExist, CouldNotRestore, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#attributesInScope(int, hla.rti.AttributeHandleSet)
     */
    public void attributesInScope(int theObject, AttributeHandleSet theAttributes)
            throws ObjectNotKnown, AttributeNotKnown, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#attributesOutOfScope(int, hla.rti.AttributeHandleSet)
     */
    public void attributesOutOfScope(int theObject, AttributeHandleSet theAttributes)
            throws ObjectNotKnown, AttributeNotKnown, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#removeObjectInstance(int, byte[])
     */
    public void removeObjectInstance(int theObject, byte[] userSuppliedTag) throws ObjectNotKnown, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#removeObjectInstance(int, byte[], hla.rti.LogicalTime,
     * hla.rti.EventRetractionHandle)
     */
    public void removeObjectInstance(int theObject, byte[] userSuppliedTag, LogicalTime theTime,
            EventRetractionHandle retractionHandle) throws ObjectNotKnown, InvalidFederationTime, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#turnUpdatesOffForObjectInstance(int, hla.rti.AttributeHandleSet)
     */
    public void turnUpdatesOffForObjectInstance(int theObject, AttributeHandleSet theAttributes)
            throws ObjectNotKnown, AttributeNotOwned, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#attributeOwnershipDivestitureNotification(int, hla.rti.AttributeHandleSet)
     */
    public void attributeOwnershipDivestitureNotification(int theObject, AttributeHandleSet releasedAttributes)
            throws ObjectNotKnown, AttributeNotKnown, AttributeNotOwned, AttributeDivestitureWasNotRequested,
            FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#requestRetraction(hla.rti.EventRetractionHandle)
     */
    public void requestRetraction(EventRetractionHandle theHandle) throws EventNotKnown, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#federationSaved()
     */
    public void federationSaved() throws FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#federationNotRestored()
     */
    public void federationNotRestored() throws FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#timeRegulationEnabled(hla.rti.LogicalTime)
     */
    public void timeRegulationEnabled(LogicalTime theFederateTime)
            throws InvalidFederationTime, EnableTimeRegulationWasNotPending, FederateInternalError
    {
        Logger.info(this, "timeRegulationEnabled", theFederateTime.toString());

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#startRegistrationForObjectClass(int)
     */
    public void startRegistrationForObjectClass(int theClass) throws ObjectClassNotPublished, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#stopRegistrationForObjectClass(int)
     */
    public void stopRegistrationForObjectClass(int theClass) throws ObjectClassNotPublished, FederateInternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.FederateAmbassador#turnUpdatesOnForObjectInstance(int, hla.rti.AttributeHandleSet)
     */
    public void turnUpdatesOnForObjectInstance(int theObject, AttributeHandleSet theAttributes)
            throws ObjectNotKnown, AttributeNotOwned, FederateInternalError
    {
        // TODO Auto-generated method stub

    }
}
