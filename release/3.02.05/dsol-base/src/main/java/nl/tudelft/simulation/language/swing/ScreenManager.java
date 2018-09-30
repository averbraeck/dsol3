package nl.tudelft.simulation.language.swing;

import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import javax.swing.JFrame;

/**
 * The ScreenManager class manages initializing and displaying full screen graphics mode.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="mailto:stijnh@tbm.tudelft.nl"> Stijn-Pieter van Houten </a>
 */
public class ScreenManager
{
    /** the environment. */
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
            DisplayMode mode = new DisplayMode((int) this.environment.getMaximumWindowBounds().getWidth(),
                    (int) this.environment.getMaximumWindowBounds().getWidth(), 24, DisplayMode.REFRESH_RATE_UNKNOWN);
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
