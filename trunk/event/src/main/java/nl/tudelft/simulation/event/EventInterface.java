/*
 * @(#) EventInterface.java Dec 10, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.event;

import java.io.Serializable;

/**
 * The EventInterface defines the interface for all events in the event project.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands.
 * <p>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl/dsol/event">www.simulation.tudelft.nl/event
 * </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty
 * 
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:12 $
 * @since 1.5
 */

public interface EventInterface extends Serializable
{

    /**
     * returns the source of the event. The source is the sender of the event
     * 
     * @return the source of the event
     */
    Object getSource();

    /**
     * returns the content of this event.
     * 
     * @return the content of this event
     */
    Object getContent();

    /**
     * returns the type of the event.
     * 
     * @return the eventType of the event
     */
    EventType getType();
}