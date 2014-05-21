/*
 * @(#) RemoteEventProducerInterface.java Dec 5, 2003 Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.event.remote;

import java.rmi.Remote;

import nl.tudelft.simulation.event.EventProducerInterface;

/**
 * The RemoteEventProducerInterface provides a remote implementation of the
 * EventProducerInterface.
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
 * @version $Revision: 1.1 $ $Date: 2007/01/07 04:57:35 $
 * @author <a
 *         href="mailto:stijnh@tbm.tudelft.nl">Stijn-Pieter
 *         van Houten </a>
 */

public interface RemoteEventProducerInterface extends Remote,
        EventProducerInterface
{
    // nothing here
}