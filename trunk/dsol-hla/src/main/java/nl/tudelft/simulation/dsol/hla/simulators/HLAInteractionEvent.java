/*
 * Created on Mar 9, 2005 TODO To change the template for this generated file go
 * to Window - Preferences - Java - Code Style - Code Templates
 */
package nl.tudelft.simulation.dsol.hla.simulators;

import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;
import se.pitch.prti.LogicalTimeDouble;

/**
 * Fired by the FedAmb. Allows the simulator to act on interactions.
 * @author simlab TODO To change the template for this generated type comment go to Window - Preferences - Java - Code
 *         Style - Code Templates
 */
public class HLAInteractionEvent
{
    private final double TIMERECEIVED;

    private final ReceivedInteraction INTERACTION;

    private final int TYPE;

    public HLAInteractionEvent(int interactionType, ReceivedInteraction interaction, LogicalTime timeReceived)
    {
        double timeValue = -1;
        if (timeReceived != null)
            timeValue = ((LogicalTimeDouble) timeReceived).getValue();
        this.TIMERECEIVED = timeValue;
        this.INTERACTION = interaction;
        this.TYPE = interactionType;
    }

    /**
     * Returns -1 if no time was set!
     */
    public double getScheduledTime()
    {
        return TIMERECEIVED;
    }

    public int getInteractionType()
    {
        return TYPE;
    }

    public ReceivedInteraction getInteraction()
    {
        return INTERACTION;
    }
}
