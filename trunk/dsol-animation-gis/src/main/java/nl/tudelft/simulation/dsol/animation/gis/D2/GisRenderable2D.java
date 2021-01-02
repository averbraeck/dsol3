package nl.tudelft.simulation.dsol.animation.gis.D2;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.DirectedPoint3d;
import org.djutils.draw.point.Point2d;
import org.djutils.logger.CategoryLogger;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.animation.D2.Renderable2DInterface;
import nl.tudelft.simulation.dsol.animation.gis.map.GisMapInterface;
import nl.tudelft.simulation.dsol.animation.gis.mapfile.MapFileXMLParser;
import nl.tudelft.simulation.dsol.animation.gis.transform.CoordinateTransform;
import nl.tudelft.simulation.dsol.simulators.AnimatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.naming.context.util.ContextUtil;

/**
 * This renderable draws CAD/GIS objects.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class GisRenderable2D implements Renderable2DInterface<GisRenderable2D>, Locatable
{
    /** */
    private static final long serialVersionUID = 20200108L;

    /** the map to display. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected GisMapInterface map = null;

    /** the image cached image. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected BufferedImage cachedImage = null;

    /** the cached extent. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Bounds2d cachedExtent = new Bounds2d(0, 0, 0, 0);

    /** the cached screenSize. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Dimension cachedScreenSize = new Dimension();

    /** the location of the map. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected DirectedPoint3d location = null;

    /** the bounds of the map. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Bounds3d bounds = null;

    /**
     * constructs a new GisRenderable2D.
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator.
     * @param mapFile URL; the mapfile to use.
     */
    public GisRenderable2D(final SimulatorInterface<?, ?, ?> simulator, final URL mapFile)
    {
        this(simulator, mapFile, new CoordinateTransform.NoTransform());
    }

    /**
     * constructs a new GisRenderable2D.
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator.
     * @param mapFile URL; the mapfile to use.
     * @param coordinateTransform CoordinateTransform; the transformation of (x, y) coordinates to (x', y') coordinates.
     */
    public GisRenderable2D(final SimulatorInterface<?, ?, ?> simulator, final URL mapFile,
            final CoordinateTransform coordinateTransform)
    {
        this(simulator, mapFile, coordinateTransform, -Double.MAX_VALUE);
    }

    /**
     * constructs a new GisRenderable2D.
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator.
     * @param mapFile URL; the mapfile to use.
     * @param coordinateTransform CoordinateTransform; the transformation of (x, y) coordinates to (x', y') coordinates.
     * @param z double; the z-value to use
     */
    public GisRenderable2D(final SimulatorInterface<?, ?, ?> simulator, final URL mapFile,
            final CoordinateTransform coordinateTransform, final double z)
    {
        if (!(simulator instanceof AnimatorInterface))
        {
            return;
        }
        try
        {
            this.map = MapFileXMLParser.parseMapFile(mapFile, coordinateTransform);
            this.cachedExtent = this.map.getExtent();
            this.location = new DirectedPoint3d(this.cachedExtent.midPoint().getX(), this.cachedExtent.midPoint().getY(), z);
            this.bounds = new Bounds3d(this.cachedExtent.getDeltaX(), this.cachedExtent.getDeltaY(), 0.0);
            this.bind2Context(simulator);
        }
        catch (Exception exception)
        {
            simulator.getLogger().always().warn(exception, "<init>");
        }
    }

    /**
     * constructs a new GisRenderable2D based on an existing Map.
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator.
     * @param map MapInterface; the map to use.
     * @param coordinateTransform CoordinateTransform; the transformation of (x, y) coordinates to (x', y') coordinates.
     * @param z double; the z-value to use
     */
    public GisRenderable2D(final SimulatorInterface<?, ?, ?> simulator, final GisMapInterface map,
            final CoordinateTransform coordinateTransform, final double z)
    {
        if (!(simulator instanceof AnimatorInterface))
        {
            return;
        }
        try
        {
            this.map = map;
            this.location = new DirectedPoint3d(this.cachedExtent.midPoint().getX(), this.cachedExtent.midPoint().getY(), z);
            this.bounds = new Bounds3d(this.cachedExtent.getDeltaX(), this.cachedExtent.getDeltaY(), 0.0);
            this.bind2Context(simulator);
        }
        catch (Exception exception)
        {
            simulator.getLogger().always().warn(exception, "<init>");
        }
    }

    /**
     * binds a renderable2D to the context. The reason for specifying this in an independent method instead of adding the code
     * in the constructor is related to the RFE submitted by van Houten that in specific distributed context, such binding must
     * be overwritten.
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator used for binding the object.
     */
    protected void bind2Context(final SimulatorInterface<?, ?, ?> simulator)
    {
        try
        {
            ContextUtil.lookupOrCreateSubContext(simulator.getReplication().getContext(), "animation/2D")
                    .bindObject(Integer.toString(System.identityHashCode(this)), this);
        }
        catch (NamingException | RemoteException exception)
        {
            simulator.getLogger().always().warn(exception, "<init>");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics2D graphics, final Bounds2d extent, final Dimension screen, final ImageObserver observer)
    {
        try
        {
            // is the extent or the screensize still the same
            if (extent.equals(this.cachedExtent) && screen.equals(this.cachedScreenSize) && this.map.isSame())
            {
                graphics.drawImage(this.cachedImage, 0, 0, null);
                return;
            }
            this.map.setExtent(extent);
            this.map.getImage().setSize(screen);
            this.cacheImage();
            this.paint(graphics, extent, screen, observer);
        }
        catch (Exception exception)
        {
            CategoryLogger.always().warn(exception, "paint");
        }
    }

    /** {@inheritDoc} */
    @Override
    public GisRenderable2D getSource()
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Bounds3d getBounds()
    {
        return this.bounds;
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint3d getLocation()
    {
        return this.location;
    }

    /**
     * @return map the Shapefile map
     */
    public final GisMapInterface getMap()
    {
        return this.map;
    }

    /**
     * caches the GIS map by creating an image. This prevents continuous rendering.
     * @throws Exception on graphicsProblems and network connection failures.
     */
    private void cacheImage() throws Exception
    {
        this.cachedImage = new BufferedImage((int) this.map.getImage().getSize().getWidth(),
                (int) this.map.getImage().getSize().getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D bg = this.cachedImage.createGraphics();
        this.map.drawMap(bg);
        bg.dispose();
        this.cachedScreenSize = (Dimension) this.map.getImage().getSize().clone();
        this.cachedExtent = this.map.getExtent();
        this.location = new DirectedPoint3d(this.cachedExtent.midPoint().getX(), this.cachedExtent.midPoint().getY(),
                -Double.MIN_VALUE);
        this.bounds = new Bounds3d(this.cachedExtent.getDeltaX(), this.cachedExtent.getDeltaY(), 0.0);
    }

    /**
     * destroys an RenderableObject by unsubscribing it from the context.
     */
    @Override
    public void destroy(final SimulatorInterface<?, ?, ?> simulator)
    {
        try
        {
            ContextUtil.lookupOrCreateSubContext(simulator.getReplication().getContext(), "animation/2D")
                    .unbindObject(Integer.toString(System.identityHashCode(this)));
        }
        catch (Throwable throwable)
        {
            simulator.getLogger().always().warn(throwable, "finalize");
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Point2d pointWorldCoordinates, final Bounds2d extent)
    {
        return false;
    }
}
