/*
 * @(#)AnimatorInterface.java Aug 18, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.simulators;

import java.rmi.RemoteException;

import nl.tudelft.simulation.event.EventType;

/**
 * The AnimatorInterface defines a DEVSDESS simulator with wallclock delay between the consequtive time steps.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @since 1.5
 */
public interface AnimatorInterface extends DEVDESSSimulatorInterface
{
    /** DEFAULT_ANIMATION_DELAY of 0 miliseconds used in the animator */
    long DEFAULT_ANIMATION_DELAY = 0L;

    /** UPDATE_ANIMATION_EVENT is fired to wake up animatable components */
    EventType UPDATE_ANIMATION_EVENT = new EventType("UPDATE_ANIMATION_EVENT");

    /** ANIMATION_DELAY_CHANGED_EVENT is fired when the time step is set */
    EventType ANIMATION_DELAY_CHANGED_EVENT = new EventType("ANIMATION_DELAY_CHANGED_EVENT");

    /**
     * returns the animation delay between each consequtive timestep
     * @return the animaiton delay in milliseconds wallclock
     * @throws RemoteException on network failure
     */
    long getAnimationDelay() throws RemoteException;

    /**
     * sets the animationDelay
     * @param miliseconds the animation delay
     * @throws RemoteException on network failure
     */
    void setAnimationDelay(long miliseconds) throws RemoteException;
}