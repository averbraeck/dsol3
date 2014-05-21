/*
 * @(#) Swingable.java Oct 19, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.jstats;

import java.awt.Container;
import java.rmi.RemoteException;

/**
 * An interface implemented by all charts and statistics objects defining their capability to present themselves as
 * Swing component. These components can be dropped on any GUI panel.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:41 $
 * @since 1.5
 */

public interface Swingable
{
    /**
     * represents this statisticsObject as Swing Container
     * @return a Container representation of an object
     * @throws RemoteException on network failure
     */
    Container getSwingPanel() throws RemoteException;
}