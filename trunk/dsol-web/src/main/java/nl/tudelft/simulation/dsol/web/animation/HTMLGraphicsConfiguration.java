package nl.tudelft.simulation.dsol.web.animation;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;

/**
 * The <code>HTMLGraphicsConfiguration</code> class describes the characteristics of the HTML canvas in the browser, as
 * a graphics destination to write to. Note that there can be several <code>GraphicsConfiguration</code> objects
 * associated with a single graphics device, representing different drawing modes or capabilities. <br>
 * <br>
 * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information
 * <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The source code and
 * binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class HTMLGraphicsConfiguration extends GraphicsConfiguration
{
    /** the {@link HTMLDevice} associated with this <code>HTMLGraphicsConfiguration</code>. */
    HTMLDevice htmlDevice;
    
    /** the identity AffineTransform. */
    AffineTransform identityTransform = new AffineTransform();

    /** the bounds, TODO: which should be filled in some way by the window size in the browser. */
    Rectangle bounds = new Rectangle(0, 0, 1920, 1080);

    /**
     * Create a graphics configuration for the HTML device.
     */
    public HTMLGraphicsConfiguration()
    {
        System.out.println("HTMLGraphicsConfiguration.<init>");
    }

    /** {@inheritDoc} */
    @Override
    public GraphicsDevice getDevice()
    {
        System.out.println("HTMLGraphicsConfiguration.getDevice()");
        return this.htmlDevice;
    }

    /**
     * Set the {@link HTMLDevice} associated with this <code>HTMLGraphicsConfiguration</code>.
     * @param htmlDevice a <code>GraphicsDevice</code> object that is associated with this
     *            <code>HTMLGraphicsConfiguration</code>.
     */
    public void setDevice(final HTMLDevice htmlDevice)
    {
        System.out.println("HTMLGraphicsConfiguration.setDevice()");
        this.htmlDevice = htmlDevice;
    }

    /** {@inheritDoc} */
    @Override
    public ColorModel getColorModel()
    {
        System.out.println("HTMLGraphicsConfiguration.getColorModel()");
        return ColorModel.getRGBdefault();
    }

    /** {@inheritDoc} */
    @Override
    public ColorModel getColorModel(int transparency)
    {
        System.out.println("HTMLGraphicsConfiguration.getColorModel()");
        return ColorModel.getRGBdefault();
    }

    /** {@inheritDoc} */
    @Override
    public AffineTransform getDefaultTransform()
    {
        System.out.println("HTMLGraphicsConfiguration.getDefaultTransform()");
        return this.identityTransform;
    }

    /** {@inheritDoc} */
    @Override
    public AffineTransform getNormalizingTransform()
    {
        System.out.println("HTMLGraphicsConfiguration.getNormalizingTransform()");
        return this.identityTransform;
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle getBounds()
    {
        System.out.println("HTMLGraphicsConfiguration.getBounds()");
        return this.bounds;
    }

}
