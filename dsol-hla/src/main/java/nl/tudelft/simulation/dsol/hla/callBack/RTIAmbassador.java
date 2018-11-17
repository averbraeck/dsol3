package nl.tudelft.simulation.dsol.hla.callBack;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.plaf.synth.Region;

import hla.rti.AsynchronousDeliveryAlreadyDisabled;
import hla.rti.AsynchronousDeliveryAlreadyEnabled;
import hla.rti.AttributeAcquisitionWasNotRequested;
import hla.rti.AttributeAlreadyBeingAcquired;
import hla.rti.AttributeAlreadyBeingDivested;
import hla.rti.AttributeAlreadyOwned;
import hla.rti.AttributeDivestitureWasNotRequested;
import hla.rti.AttributeHandleSet;
import hla.rti.AttributeNotDefined;
import hla.rti.AttributeNotOwned;
import hla.rti.AttributeNotPublished;
import hla.rti.ConcurrentAccessAttempted;
import hla.rti.CouldNotOpenFED;
import hla.rti.DeletePrivilegeNotHeld;
import hla.rti.DimensionNotDefined;
import hla.rti.EnableTimeConstrainedPending;
import hla.rti.EnableTimeRegulationPending;
import hla.rti.ErrorReadingFED;
import hla.rti.EventRetractionHandle;
import hla.rti.FederateAlreadyExecutionMember;
import hla.rti.FederateAmbassador;
import hla.rti.FederateHandleSet;
import hla.rti.FederateLoggingServiceCalls;
import hla.rti.FederateNotExecutionMember;
import hla.rti.FederateNotSubscribed;
import hla.rti.FederateOwnsAttributes;
import hla.rti.FederateWasNotAskedToReleaseAttribute;
import hla.rti.FederatesCurrentlyJoined;
import hla.rti.FederationExecutionAlreadyExists;
import hla.rti.FederationExecutionDoesNotExist;
import hla.rti.FederationTimeAlreadyPassed;
import hla.rti.InteractionClassNotDefined;
import hla.rti.InteractionClassNotPublished;
import hla.rti.InteractionClassNotSubscribed;
import hla.rti.InteractionParameterNotDefined;
import hla.rti.InvalidExtents;
import hla.rti.InvalidFederationTime;
import hla.rti.InvalidLookahead;
import hla.rti.InvalidOrderingHandle;
import hla.rti.InvalidRegionContext;
import hla.rti.InvalidResignAction;
import hla.rti.InvalidRetractionHandle;
import hla.rti.InvalidTransportationHandle;
import hla.rti.LogicalTime;
import hla.rti.LogicalTimeInterval;
import hla.rti.MobileFederateServices;
import hla.rti.NameNotFound;
import hla.rti.ObjectAlreadyRegistered;
import hla.rti.ObjectClassNotDefined;
import hla.rti.ObjectClassNotPublished;
import hla.rti.ObjectClassNotSubscribed;
import hla.rti.ObjectNotKnown;
import hla.rti.OwnershipAcquisitionPending;
import hla.rti.RTIambassador;
import hla.rti.RTIinternalError;
import hla.rti.RegionInUse;
import hla.rti.RegionNotKnown;
import hla.rti.RestoreInProgress;
import hla.rti.RestoreNotRequested;
import hla.rti.SaveInProgress;
import hla.rti.SaveNotInitiated;
import hla.rti.SpaceNotDefined;
import hla.rti.SuppliedAttributes;
import hla.rti.SuppliedParameters;
import hla.rti.SynchronizationLabelNotAnnounced;
import hla.rti.TimeAdvanceAlreadyInProgress;
import hla.rti.TimeConstrainedAlreadyEnabled;
import hla.rti.TimeConstrainedWasNotEnabled;
import hla.rti.TimeRegulationAlreadyEnabled;
import hla.rti.TimeRegulationWasNotEnabled;
import nl.tudelft.simulation.event.util.EventProducingList;

/**
 * @author peter TODO To change the template for this generated type comment go to Window - Preferences - Java - Code
 *         Style - Code Templates
 */
public class RTIAmbassador
{
    /** the RTI ambassador wrapper. */
    private RTIambassador target = null;

    /** the callback list. */
    protected EventProducingList callbackList = new EventProducingList(Collections.synchronizedList(new ArrayList()));

    /**
     * constructs a new RTIAmbassador.
     */
    public RTIAmbassador(final RTIambassador target)
    {
        super();
        this.target = target;
        new Thread(new CallbackManager(this.callbackList)).start();
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#requestFederationSave(java.lang.String)
     */
    public void requestFederationSave(String arg0) throws FederateNotExecutionMember, SaveInProgress, RestoreInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#changeAttributeTransportationType(int, hla.rti.AttributeHandleSet, int)
     */
    public void changeAttributeTransportationType(int arg0, AttributeHandleSet arg1, int arg2)
            throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, InvalidTransportationHandle,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#queryMinNextEventTime()
     */
    public LogicalTime queryMinNextEventTime() throws FederateNotExecutionMember, SaveInProgress, RestoreInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#changeAttributeOrderType(int, hla.rti.AttributeHandleSet, int)
     */
    public void changeAttributeOrderType(int arg0, AttributeHandleSet arg1, int arg2)
            throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, InvalidOrderingHandle,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#timeAdvanceRequest(hla.rti.LogicalTime)
     */
    public void timeAdvanceRequest(LogicalTime arg0) throws InvalidFederationTime, FederationTimeAlreadyPassed,
            TimeAdvanceAlreadyInProgress, EnableTimeRegulationPending, EnableTimeConstrainedPending,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        this.target.timeAdvanceRequest(arg0);
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#nextEventRequest(hla.rti.LogicalTime)
     */
    public void nextEventRequest(LogicalTime arg0) throws InvalidFederationTime, FederationTimeAlreadyPassed,
            TimeAdvanceAlreadyInProgress, EnableTimeRegulationPending, EnableTimeConstrainedPending,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        this.target.nextEventRequest(arg0);
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#unassociateRegionForUpdates(hla.rti.Region, int)
     */
    public void unassociateRegionForUpdates(Region arg0, int arg1)
            throws ObjectNotKnown, InvalidRegionContext, RegionNotKnown, FederateNotExecutionMember, SaveInProgress,
            RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getRoutingSpaceName(int)
     */
    public String getRoutingSpaceName(int arg0) throws SpaceNotDefined, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getDimensionName(int, int)
     */
    public String getDimensionName(int arg0, int arg1)
            throws SpaceNotDefined, DimensionNotDefined, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#requestObjectAttributeValueUpdate(int, hla.rti.AttributeHandleSet)
     */
    public void requestObjectAttributeValueUpdate(int arg0, AttributeHandleSet arg1)
            throws ObjectNotKnown, AttributeNotDefined, FederateNotExecutionMember, SaveInProgress, RestoreInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getTransportationName(int)
     */
    public String getTransportationName(int arg0)
            throws InvalidTransportationHandle, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#queryFederateTime()
     */
    public LogicalTime queryFederateTime() throws FederateNotExecutionMember, SaveInProgress, RestoreInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        return this.target.queryFederateTime();
    }

    /**
     * @throws RTIinternalError
     * @throws FederateNotExecutionMember
     * @throws NameNotFound
     * @throws ObjectClassNotDefined
     * @see hla.rti.RTIambassador#getAttributeHandle(java.lang.String, int)
     */
    public int getAttributeHandle(String arg0, int arg1)
            throws ObjectClassNotDefined, NameNotFound, FederateNotExecutionMember, RTIinternalError
    {
        return this.target.getAttributeHandle(arg0, arg1);
    }

    /** {@inheritDoc} */
    @Override
    public int getInteractionClassHandle(String arg0) throws NameNotFound, FederateNotExecutionMember, RTIinternalError
    {
        return this.target.getInteractionClassHandle(arg0);
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getObjectInstanceHandle(java.lang.String)
     */
    public int getObjectInstanceHandle(String arg0) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getInteractionRoutingSpaceHandle(int)
     */
    public int getInteractionRoutingSpaceHandle(int arg0)
            throws InteractionClassNotDefined, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public void createFederationExecution(String federationExecutionName, URL fdd, boolean callBack)
            throws FederationExecutionAlreadyExists, CouldNotOpenFED, ErrorReadingFED, RTIinternalError,
            ConcurrentAccessAttempted
    {
        if (!callBack)
        {
            this.target.createFederationExecution(federationExecutionName, fdd);
        }
        else
        {
            this.callbackList.add(new CallbackTask(this, this.target, "createFederationExecution",
                    new Object[]{federationExecutionName, fdd}));
        }
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#flushQueueRequest(hla.rti.LogicalTime)
     */
    public void flushQueueRequest(LogicalTime arg0) throws InvalidFederationTime, FederationTimeAlreadyPassed,
            TimeAdvanceAlreadyInProgress, EnableTimeRegulationPending, EnableTimeConstrainedPending,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#subscribeObjectClassAttributesWithRegion(int, hla.rti.Region,
     * hla.rti.AttributeHandleSet)
     */
    public void subscribeObjectClassAttributesWithRegion(int arg0, Region arg1, AttributeHandleSet arg2)
            throws ObjectClassNotDefined, AttributeNotDefined, RegionNotKnown, InvalidRegionContext,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#subscribeObjectClassAttributesPassivelyWithRegion(int, hla.rti.Region,
     * hla.rti.AttributeHandleSet)
     */
    public void subscribeObjectClassAttributesPassivelyWithRegion(int arg0, Region arg1, AttributeHandleSet arg2)
            throws ObjectClassNotDefined, AttributeNotDefined, RegionNotKnown, InvalidRegionContext,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#unsubscribeObjectClassWithRegion(int, hla.rti.Region)
     */
    public void unsubscribeObjectClassWithRegion(int arg0, Region arg1)
            throws ObjectClassNotDefined, RegionNotKnown, FederateNotSubscribed, FederateNotExecutionMember,
            SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getOrderingName(int)
     */
    public String getOrderingName(int arg0) throws InvalidOrderingHandle, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void registerFederationSynchronizationPoint(String synchronizationPointLabel, byte[] userSuppliedTag,
            boolean callBack) throws FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError,
            ConcurrentAccessAttempted
    {
        if (!callBack)
        {
            this.target.registerFederationSynchronizationPoint(synchronizationPointLabel, userSuppliedTag);
        }
        else
        {
            if (userSuppliedTag == null)
            {
                userSuppliedTag = new byte[0];
            }
            this.callbackList.add(new CallbackTask(this, this.target, "registerFederationSynchronizationPoint",
                    new Object[]{synchronizationPointLabel, userSuppliedTag}));
        }

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#registerFederationSynchronizationPoint(java.lang.String, byte[],
     * hla.rti.FederateHandleSet)
     */
    public void registerFederationSynchronizationPoint(String arg0, byte[] arg1, FederateHandleSet arg2)
            throws FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError,
            ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#attributeOwnershipReleaseResponse(int, hla.rti.AttributeHandleSet)
     */
    public AttributeHandleSet attributeOwnershipReleaseResponse(int arg0, AttributeHandleSet arg1)
            throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, FederateWasNotAskedToReleaseAttribute,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#queryLBTS()
     */
    public LogicalTime queryLBTS() throws FederateNotExecutionMember, SaveInProgress, RestoreInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#subscribeObjectClassAttributesPassively(int, hla.rti.AttributeHandleSet)
     */
    public void subscribeObjectClassAttributesPassively(int arg0, AttributeHandleSet arg1)
            throws ObjectClassNotDefined, AttributeNotDefined, FederateNotExecutionMember, SaveInProgress,
            RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#unsubscribeObjectClass(int)
     */
    public void unsubscribeObjectClass(int arg0) throws ObjectClassNotDefined, ObjectClassNotSubscribed,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#registerObjectInstance(int)
     */
    public int registerObjectInstance(int arg0) throws ObjectClassNotDefined, ObjectClassNotPublished,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#registerObjectInstance(int, java.lang.String)
     */
    public int registerObjectInstance(int arg0, String arg1)
            throws ObjectClassNotDefined, ObjectClassNotPublished, ObjectAlreadyRegistered, FederateNotExecutionMember,
            SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#deleteObjectInstance(int, byte[])
     */
    public void deleteObjectInstance(int arg0, byte[] arg1) throws ObjectNotKnown, DeletePrivilegeNotHeld,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#deleteObjectInstance(int, byte[], hla.rti.LogicalTime)
     */
    public EventRetractionHandle deleteObjectInstance(int arg0, byte[] arg1, LogicalTime arg2)
            throws ObjectNotKnown, DeletePrivilegeNotHeld, InvalidFederationTime, FederateNotExecutionMember,
            SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#enableInteractionRelevanceAdvisorySwitch()
     */
    public void enableInteractionRelevanceAdvisorySwitch()
            throws FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#disableInteractionRelevanceAdvisorySwitch()
     */
    public void disableInteractionRelevanceAdvisorySwitch()
            throws FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#federateRestoreComplete()
     */
    public void federateRestoreComplete() throws RestoreNotRequested, FederateNotExecutionMember, SaveInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#associateRegionForUpdates(hla.rti.Region, int, hla.rti.AttributeHandleSet)
     */
    public void associateRegionForUpdates(Region arg0, int arg1, AttributeHandleSet arg2)
            throws ObjectNotKnown, AttributeNotDefined, InvalidRegionContext, RegionNotKnown,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getObjectClassName(int)
     */
    public String getObjectClassName(int arg0)
            throws ObjectClassNotDefined, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getParameterName(int, int)
     */
    public String getParameterName(int arg0, int arg1) throws InteractionClassNotDefined,
            InteractionParameterNotDefined, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#enableAttributeRelevanceAdvisorySwitch()
     */
    public void enableAttributeRelevanceAdvisorySwitch()
            throws FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#disableAttributeRelevanceAdvisorySwitch()
     */
    public void disableAttributeRelevanceAdvisorySwitch()
            throws FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#enableAttributeScopeAdvisorySwitch()
     */
    public void enableAttributeScopeAdvisorySwitch()
            throws FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#disableAttributeScopeAdvisorySwitch()
     */
    public void disableAttributeScopeAdvisorySwitch()
            throws FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#enableAsynchronousDelivery()
     */
    public void enableAsynchronousDelivery() throws AsynchronousDeliveryAlreadyEnabled, FederateNotExecutionMember,
            SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#disableAsynchronousDelivery()
     */
    public void disableAsynchronousDelivery() throws AsynchronousDeliveryAlreadyDisabled, FederateNotExecutionMember,
            SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getRoutingSpaceHandle(java.lang.String)
     */
    public int getRoutingSpaceHandle(String arg0) throws NameNotFound, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getDimensionHandle(java.lang.String, int)
     */
    public int getDimensionHandle(String arg0, int arg1)
            throws SpaceNotDefined, NameNotFound, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getAttributeRoutingSpaceHandle(int, int)
     */
    public int getAttributeRoutingSpaceHandle(int arg0, int arg1)
            throws ObjectClassNotDefined, AttributeNotDefined, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getTransportationHandle(java.lang.String)
     */
    public int getTransportationHandle(String arg0) throws NameNotFound, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#enableTimeRegulation(hla.rti.LogicalTime, hla.rti.LogicalTimeInterval)
     */
    public void enableTimeRegulation(LogicalTime arg0, LogicalTimeInterval arg1, boolean callBack)
            throws TimeRegulationAlreadyEnabled, EnableTimeRegulationPending, TimeAdvanceAlreadyInProgress,
            InvalidFederationTime, InvalidLookahead, FederateNotExecutionMember, SaveInProgress, RestoreInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        if (!callBack)
        {
            this.target.enableTimeRegulation(arg0, arg1);
        }
        else
        {
            this.callbackList
                    .add(new CallbackTask(this, this.target, "enableTimeRegulation", new Object[]{arg0, arg1}));
        }

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#disableTimeRegulation()
     */
    public void disableTimeRegulation() throws TimeRegulationWasNotEnabled, FederateNotExecutionMember, SaveInProgress,
            RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#createRegion(int, int)
     */
    public Region createRegion(int arg0, int arg1) throws SpaceNotDefined, InvalidExtents, FederateNotExecutionMember,
            SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#unsubscribeInteractionClassWithRegion(int, hla.rti.Region)
     */
    public void unsubscribeInteractionClassWithRegion(int arg0, Region arg1)
            throws InteractionClassNotDefined, InteractionClassNotSubscribed, RegionNotKnown,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#queryAttributeOwnership(int, int)
     */
    public void queryAttributeOwnership(int arg0, int arg1) throws ObjectNotKnown, AttributeNotDefined,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#attributeOwnershipAcquisitionIfAvailable(int, hla.rti.AttributeHandleSet)
     */
    public void attributeOwnershipAcquisitionIfAvailable(int arg0, AttributeHandleSet arg1)
            throws ObjectNotKnown, ObjectClassNotPublished, AttributeNotDefined, AttributeNotPublished,
            FederateOwnsAttributes, AttributeAlreadyBeingAcquired, FederateNotExecutionMember, SaveInProgress,
            RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#destroyFederationExecution(java.lang.String)
     */
    public void destroyFederationExecution(String arg0) throws FederatesCurrentlyJoined,
            FederationExecutionDoesNotExist, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#cancelAttributeOwnershipAcquisition(int, hla.rti.AttributeHandleSet)
     */
    public void cancelAttributeOwnershipAcquisition(int arg0, AttributeHandleSet arg1)
            throws ObjectNotKnown, AttributeNotDefined, AttributeAlreadyOwned, AttributeAcquisitionWasNotRequested,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#unconditionalAttributeOwnershipDivestiture(int, hla.rti.AttributeHandleSet)
     */
    public void unconditionalAttributeOwnershipDivestiture(int arg0, AttributeHandleSet arg1)
            throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, FederateNotExecutionMember, SaveInProgress,
            RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#resignFederationExecution(int)
     */
    public void resignFederationExecution(int arg0) throws FederateOwnsAttributes, FederateNotExecutionMember,
            InvalidResignAction, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#retract(hla.rti.EventRetractionHandle)
     */
    public void retract(EventRetractionHandle arg0) throws InvalidRetractionHandle, FederateNotExecutionMember,
            SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#notifyOfRegionModification(hla.rti.Region)
     */
    public void notifyOfRegionModification(Region arg0) throws RegionNotKnown, InvalidExtents,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#registerObjectInstanceWithRegion(int, int[], hla.rti.Region[])
     */
    public int registerObjectInstanceWithRegion(int arg0, int[] arg1, Region[] arg2) throws ObjectClassNotDefined,
            ObjectClassNotPublished, AttributeNotDefined, AttributeNotPublished, RegionNotKnown, InvalidRegionContext,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#registerObjectInstanceWithRegion(int, java.lang.String, int[], hla.rti.Region[])
     */
    public int registerObjectInstanceWithRegion(int arg0, String arg1, int[] arg2, Region[] arg3)
            throws ObjectClassNotDefined, ObjectClassNotPublished, AttributeNotDefined, AttributeNotPublished,
            RegionNotKnown, InvalidRegionContext, ObjectAlreadyRegistered, FederateNotExecutionMember, SaveInProgress,
            RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#subscribeInteractionClassWithRegion(int, hla.rti.Region)
     */
    public void subscribeInteractionClassWithRegion(int arg0, Region arg1)
            throws InteractionClassNotDefined, RegionNotKnown, InvalidRegionContext, FederateLoggingServiceCalls,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#subscribeInteractionClassPassivelyWithRegion(int, hla.rti.Region)
     */
    public void subscribeInteractionClassPassivelyWithRegion(int arg0, Region arg1)
            throws InteractionClassNotDefined, RegionNotKnown, InvalidRegionContext, FederateLoggingServiceCalls,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#federateSaveBegun()
     */
    public void federateSaveBegun() throws SaveNotInitiated, FederateNotExecutionMember, RestoreInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#sendInteractionWithRegion(int, hla.rti.SuppliedParameters, byte[], hla.rti.Region)
     */
    public void sendInteractionWithRegion(int arg0, SuppliedParameters arg1, byte[] arg2, Region arg3)
            throws InteractionClassNotDefined, InteractionClassNotPublished, InteractionParameterNotDefined,
            RegionNotKnown, InvalidRegionContext, FederateNotExecutionMember, SaveInProgress, RestoreInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#sendInteractionWithRegion(int, hla.rti.SuppliedParameters, byte[], hla.rti.Region,
     * hla.rti.LogicalTime)
     */
    public EventRetractionHandle sendInteractionWithRegion(int arg0, SuppliedParameters arg1, byte[] arg2, Region arg3,
            LogicalTime arg4) throws InteractionClassNotDefined, InteractionClassNotPublished,
            InteractionParameterNotDefined, InvalidFederationTime, RegionNotKnown, InvalidRegionContext,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#requestClassAttributeValueUpdateWithRegion(int, hla.rti.AttributeHandleSet,
     * hla.rti.Region)
     */
    public void requestClassAttributeValueUpdateWithRegion(int arg0, AttributeHandleSet arg1, Region arg2)
            throws ObjectClassNotDefined, AttributeNotDefined, RegionNotKnown, FederateNotExecutionMember,
            SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#unpublishObjectClass(int)
     */
    public void unpublishObjectClass(int arg0)
            throws ObjectClassNotDefined, ObjectClassNotPublished, OwnershipAcquisitionPending,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#unsubscribeInteractionClass(int)
     */
    public void unsubscribeInteractionClass(int arg0) throws InteractionClassNotDefined, InteractionClassNotSubscribed,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#localDeleteObjectInstance(int)
     */
    public void localDeleteObjectInstance(int arg0) throws ObjectNotKnown, FederateOwnsAttributes,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#enableClassRelevanceAdvisorySwitch()
     */
    public void enableClassRelevanceAdvisorySwitch()
            throws FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#disableClassRelevanceAdvisorySwitch()
     */
    public void disableClassRelevanceAdvisorySwitch()
            throws FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#federateSaveComplete()
     */
    public void federateSaveComplete() throws SaveNotInitiated, FederateNotExecutionMember, RestoreInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    @Override
    public void synchronizationPointAchieved(final String synchronizationPointLabel, boolean callBack)
            throws SynchronizationLabelNotAnnounced, FederateNotExecutionMember, SaveInProgress, RestoreInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        if (!callBack)
        {
            this.target.synchronizationPointAchieved(synchronizationPointLabel);
        }
        else
        {
            this.callbackList.add(new CallbackTask(this, this.target, "synchronizationPointAchieved",
                    new Object[]{synchronizationPointLabel}));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void federateRestoreNotComplete() throws RestoreNotRequested, FederateNotExecutionMember, SaveInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    @Override
    public void subscribeInteractionClassPassively(int arg0)
            throws InteractionClassNotDefined, FederateNotExecutionMember, FederateLoggingServiceCalls, SaveInProgress,
            RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#changeInteractionTransportationType(int, int)
     */
    public void changeInteractionTransportationType(int arg0, int arg1)
            throws InteractionClassNotDefined, InteractionClassNotPublished, InvalidTransportationHandle,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#changeInteractionOrderType(int, int)
     */
    public void changeInteractionOrderType(int arg0, int arg1)
            throws InteractionClassNotDefined, InteractionClassNotPublished, InvalidOrderingHandle,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getAttributeName(int, int)
     */
    public String getAttributeName(int arg0, int arg1)
            throws ObjectClassNotDefined, AttributeNotDefined, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getInteractionClassName(int arg0)
            throws InteractionClassNotDefined, FederateNotExecutionMember, RTIinternalError
    {
        return this.target.getInteractionClassName(arg0);
    }

    /**
     * @throws ConcurrentAccessAttempted
     * @throws RTIinternalError
     * @throws RestoreInProgress
     * @throws SaveInProgress
     * @throws FederateNotExecutionMember
     * @throws AttributeNotDefined
     * @throws ObjectClassNotDefined
     * @see hla.rti.RTIambassador#subscribeObjectClassAttributes(int, hla.rti.AttributeHandleSet)
     */
    public void subscribeObjectClassAttributes(int arg0, AttributeHandleSet arg1, boolean callBack)
            throws ObjectClassNotDefined, AttributeNotDefined, FederateNotExecutionMember, SaveInProgress,
            RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        if (!callBack)
        {
            this.target.subscribeObjectClassAttributes(arg0, arg1);
        }
        else
        {
            this.callbackList.add(new CallbackTask(this, this.target, "subscribeObjectClassAttributes",
                    new Object[]{new Integer(arg0), arg1}));
        }
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#updateAttributeValues(int, hla.rti.SuppliedAttributes, byte[])
     */
    public void updateAttributeValues(int arg0, SuppliedAttributes arg1, byte[] arg2)
            throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, FederateNotExecutionMember, SaveInProgress,
            RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#updateAttributeValues(int, hla.rti.SuppliedAttributes, byte[], hla.rti.LogicalTime)
     */
    public EventRetractionHandle updateAttributeValues(int arg0, SuppliedAttributes arg1, byte[] arg2, LogicalTime arg3)
            throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, InvalidFederationTime,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getObjectInstanceName(int)
     */
    public String getObjectInstanceName(int arg0) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#requestFederationRestore(java.lang.String)
     */
    public void requestFederationRestore(String arg0) throws FederateNotExecutionMember, SaveInProgress,
            RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    @Override
    public int getObjectClassHandle(String arg0) throws NameNotFound, FederateNotExecutionMember, RTIinternalError
    {
        return this.target.getObjectClassHandle(arg0);
    }

    /**
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getParameterHandle(java.lang.String, int)
     */
    public int getParameterHandle(String arg0, int arg1)
            throws InteractionClassNotDefined, NameNotFound, FederateNotExecutionMember, RTIinternalError
    {
        return this.target.getParameterHandle(arg0, arg1);
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getOrderingHandle(java.lang.String)
     */
    public int getOrderingHandle(String arg0) throws NameNotFound, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public int joinFederationExecution(String arg0, String arg1, FederateAmbassador arg2)
            throws FederateAlreadyExecutionMember, FederationExecutionDoesNotExist, SaveInProgress, RestoreInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        return this.target.joinFederationExecution(arg0, arg1, arg2);
    }

    /** {@inheritDoc} */
    @Override
    public int joinFederationExecution(String arg0, String arg1, FederateAmbassador arg2, MobileFederateServices arg3)
            throws FederateAlreadyExecutionMember, FederationExecutionDoesNotExist, SaveInProgress, RestoreInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        return this.target.joinFederationExecution(arg0, arg1, arg2, arg3);
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#sendInteraction(int, hla.rti.SuppliedParameters, byte[])
     */
    public void sendInteraction(int arg0, SuppliedParameters arg1, byte[] arg2, boolean callBack)
            throws InteractionClassNotDefined, InteractionClassNotPublished, InteractionParameterNotDefined,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        if (!callBack)
        {
            this.target.sendInteraction(arg0, arg1, arg2);
        }
        else
        {
            this.callbackList.add(new CallbackTask(this, this.target, "sendInteraction",
                    new Object[]{new Integer(arg0), arg1, arg2}));
        }
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#sendInteraction(int, hla.rti.SuppliedParameters, byte[], hla.rti.LogicalTime)
     */
    public EventRetractionHandle sendInteraction(int arg0, SuppliedParameters arg1, byte[] arg2, LogicalTime arg3)
            throws InteractionClassNotDefined, InteractionClassNotPublished, InteractionParameterNotDefined,
            InvalidFederationTime, FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError,
            ConcurrentAccessAttempted
    {
        return this.target.sendInteraction(arg0, arg1, arg2, arg3);
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#attributeOwnershipAcquisition(int, hla.rti.AttributeHandleSet, byte[])
     */
    public void attributeOwnershipAcquisition(int arg0, AttributeHandleSet arg1, byte[] arg2) throws ObjectNotKnown,
            ObjectClassNotPublished, AttributeNotDefined, AttributeNotPublished, FederateOwnsAttributes,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#deleteRegion(hla.rti.Region)
     */
    public void deleteRegion(Region arg0) throws RegionNotKnown, RegionInUse, FederateNotExecutionMember,
            SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#modifyLookahead(hla.rti.LogicalTimeInterval)
     */
    public void modifyLookahead(LogicalTimeInterval arg0) throws InvalidLookahead, FederateNotExecutionMember,
            SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        this.target.modifyLookahead(arg0);
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#queryLookahead()
     */
    public LogicalTimeInterval queryLookahead() throws FederateNotExecutionMember, SaveInProgress, RestoreInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        return this.target.queryLookahead();
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#negotiatedAttributeOwnershipDivestiture(int, hla.rti.AttributeHandleSet, byte[])
     */
    public void negotiatedAttributeOwnershipDivestiture(int arg0, AttributeHandleSet arg1, byte[] arg2)
            throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, AttributeAlreadyBeingDivested,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#enableTimeConstrained()
     */
    public void enableTimeConstrained(boolean callBack)
            throws TimeConstrainedAlreadyEnabled, EnableTimeConstrainedPending, TimeAdvanceAlreadyInProgress,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        if (!callBack)
        {
            this.target.enableTimeConstrained();
        }
        else
        {
            this.callbackList.add(new CallbackTask(this, this.target, "enableTimeConstrained", null));
        }
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#disableTimeConstrained()
     */
    public void disableTimeConstrained(boolean callBack) throws TimeConstrainedWasNotEnabled,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        if (!callBack)
        {
            this.target.disableTimeConstrained();
        }
        else
        {
            this.callbackList.add(new CallbackTask(this, this.target, "disableTimeConstrained", null));
        }

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#cancelNegotiatedAttributeOwnershipDivestiture(int, hla.rti.AttributeHandleSet)
     */
    public void cancelNegotiatedAttributeOwnershipDivestiture(int arg0, AttributeHandleSet arg1)
            throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, AttributeDivestitureWasNotRequested,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#timeAdvanceRequestAvailable(hla.rti.LogicalTime)
     */
    public void timeAdvanceRequestAvailable(LogicalTime arg0) throws InvalidFederationTime, FederationTimeAlreadyPassed,
            TimeAdvanceAlreadyInProgress, EnableTimeRegulationPending, EnableTimeConstrainedPending,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#nextEventRequestAvailable(hla.rti.LogicalTime)
     */
    public void nextEventRequestAvailable(LogicalTime arg0) throws InvalidFederationTime, FederationTimeAlreadyPassed,
            TimeAdvanceAlreadyInProgress, EnableTimeRegulationPending, EnableTimeConstrainedPending,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        this.target.nextEventRequestAvailable(arg0);
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#tick()
     */
    public void tick() throws RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#publishObjectClass(int, hla.rti.AttributeHandleSet)
     */
    public void publishObjectClass(int arg0, AttributeHandleSet arg1)
            throws ObjectClassNotDefined, AttributeNotDefined, OwnershipAcquisitionPending, FederateNotExecutionMember,
            SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#unpublishInteractionClass(int)
     */
    public void unpublishInteractionClass(int arg0) throws InteractionClassNotDefined, InteractionClassNotPublished,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#subscribeInteractionClass(int)
     */
    public void subscribeInteractionClass(int arg0, boolean callBack)
            throws InteractionClassNotDefined, FederateNotExecutionMember, FederateLoggingServiceCalls, SaveInProgress,
            RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        if (!callBack)
        {
            this.target.subscribeInteractionClass(arg0);
        }
        else
        {
            this.callbackList.add(
                    new CallbackTask(this, this.target, "subscribeInteractionClass", new Object[]{new Integer(arg0)}));
        }
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#getObjectClass(int)
     */
    public int getObjectClass(int arg0) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#federateSaveNotComplete()
     */
    public void federateSaveNotComplete() throws SaveNotInitiated, FederateNotExecutionMember, RestoreInProgress,
            RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#requestClassAttributeValueUpdate(int, hla.rti.AttributeHandleSet)
     */
    public void requestClassAttributeValueUpdate(int arg0, AttributeHandleSet arg1)
            throws ObjectClassNotDefined, AttributeNotDefined, FederateNotExecutionMember, SaveInProgress,
            RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#isAttributeOwnedByFederate(int, int)
     */
    public boolean isAttributeOwnedByFederate(int arg0, int arg1) throws ObjectNotKnown, AttributeNotDefined,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#publishInteractionClass(int)
     */
    public void publishInteractionClass(int arg0, boolean callBack) throws InteractionClassNotDefined,
            FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        if (!callBack)
        {
            this.target.publishInteractionClass(arg0);
        }
        else
        {
            this.callbackList.add(
                    new CallbackTask(this, this.target, "publishInteractionClass", new Object[]{new Integer(arg0)}));
        }
    }

    /*
     * (non-Javadoc)
     * @see hla.rti.RTIambassador#requestFederationSave(java.lang.String, hla.rti.LogicalTime)
     */
    public void requestFederationSave(String arg0, LogicalTime arg1)
            throws FederationTimeAlreadyPassed, InvalidFederationTime, FederateNotExecutionMember, SaveInProgress,
            RestoreInProgress, RTIinternalError, ConcurrentAccessAttempted
    {
        // TODO Auto-generated method stub

    }
}
