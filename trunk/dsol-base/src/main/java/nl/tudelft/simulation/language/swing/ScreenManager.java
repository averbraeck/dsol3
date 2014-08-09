/*
 * @(#) ScreenManager.java Apr 29, 2004 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.language.swing;

import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import javax.swing.JFrame;

/**
 * The ScreenManager class manages initializing and displaying full screen graphics mode.
 * <p>
 * Copyright (c) 2002-2009 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version $Revision: 1.2 $ $Date: 2009/10/21 07:32:42 $
 * @author <a href="mailto:stijnh@tbm.tudelft.nl"> Stijn-Pieter van Houten </a>
 */
public class ScreenManager
{
    /** the environment */
    private GraphicsEnvironment environment;

    /**
     * Constructs a new ScreenManager.
     */
    public ScreenManager()
    {
        super();
        this.environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    }

    /**
     * Method setFullScreen. Enters full screen mode and changes the display mode.
     * @param window The window to show full screen.
     */
    public void setFullScreen(final JFrame window)
    {
        window.setUndecorated(true);
        window.setResizable(false);
        this.environment.getDefaultScreenDevice().setFullScreenWindow(window);
        if (this.environment.getDefaultScreenDevice().isDisplayChangeSupported())
        {
            DisplayMode mode =
                    new DisplayMode((int) this.environment.getMaximumWindowBounds().getWidth(), (int) this.environment
                            .getMaximumWindowBounds().getWidth(), 24, DisplayMode.REFRESH_RATE_UNKNOWN);
            this.environment.getDefaultScreenDevice().setDisplayMode(mode);

        }
    }

    /**
     * Method getFullScreenWindow.
     * @return Returns the window currently used in full screen mode.
     */
    public Window getFullScreenWindow()
    {
        return this.environment.getDefaultScreenDevice().getFullScreenWindow();
    }

    /**
     * Method restoreScreen. Restores the screen's display mode.
     */
    public void restoreScreen()
    {
        Window window = this.environment.getDefaultScreenDevice().getFullScreenWindow();
        if (window != null)
        {
            window.dispose();
        }
        this.environment.getDefaultScreenDevice().setFullScreenWindow(null);
    }
}