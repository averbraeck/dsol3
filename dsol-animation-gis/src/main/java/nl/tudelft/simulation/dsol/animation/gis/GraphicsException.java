package nl.tudelft.simulation.dsol.animation.gis;

/**
 * GraphicsException for exceptions when reading or drawing GIS layers.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class GraphicsException extends Exception
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new blank GraphicsException.
     */
    public GraphicsException()
    {
        super();
    }

    /**
     * Constructs a new GraphicsException.
     * @param message String; the message to display
     * @param cause Throwable; the underlying exception
     */
    public GraphicsException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructs a new GraphicsException.
     * @param message String; the message to display
     */
    public GraphicsException(final String message)
    {
        super(message);
    }

    /**
     * Constructs a new GraphicsException.
     * @param cause Throwable; the underlying exception
     */
    public GraphicsException(final Throwable cause)
    {
        super(cause);
    }

}
