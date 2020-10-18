package nl.tudelft.simulation.dsol.animation.gis.D2;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.scijava.java3d.Bounds;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.animation.D2.Renderable2DInterface;
import nl.tudelft.simulation.dsol.animation.gis.map.MapInterface;
import nl.tudelft.simulation.dsol.animation.gis.mapfile.MapFileXMLParser;
import nl.tudelft.simulation.dsol.animation.gis.transform.CoordinateTransform;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simulators.AnimatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.d3.BoundingBox;
import nl.tudelft.simulation.language.d3.CartesianPoint;
import nl.tudelft.simulation.language.d3.DirectedPoint;
import nl.tudelft.simulation.naming.context.ContextInterface;
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
    protected MapInterface map = null;

    /** the image cached image. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected BufferedImage cachedImage = null;

    /** the cached extent. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Rectangle2D cachedExtent = new Rectangle2D.Double();

    /** the cached screenSize. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Dimension cachedScreenSize = new Dimension();

    /** the location of the map. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected DirectedPoint location = null;

    /** the bounds of the map. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Bounds bounds = null;

    /** the context for (un)binding. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected ContextInterface context;

    /** the logger. */
    private final SimLogger logger;

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
        this.logger = simulator.getLogger();
        if (!(simulator instanceof AnimatorInterface))
        {
            return;
        }
        try
        {
            this.map = MapFileXMLParser.parseMapFile(mapFile, coordinateTransform);
            this.location =
                    new DirectedPoint(new CartesianPoint(this.cachedExtent.getCenterX(), this.cachedExtent.getCenterY(), z));
            this.bounds = new BoundingBox(this.cachedExtent.getWidth(), this.cachedExtent.getHeight(), 0.0);
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
    public GisRenderable2D(final SimulatorInterface<?, ?, ?> simulator, final MapInterface map,
            final CoordinateTransform coordinateTransform, final double z)
    {
        this.logger = simulator.getLogger();
        if (!(simulator instanceof AnimatorInterface))
        {
            return;
        }
        try
        {
            this.map = map;
            this.location =
                    new DirectedPoint(new CartesianPoint(this.cachedExtent.getCenterX(), this.cachedExtent.getCenterY(), z));
            this.bounds = new BoundingBox(this.cachedExtent.getWidth(), this.cachedExtent.getHeight(), 0.0);
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
            this.context = ContextUtil.lookupOrCreateSubContext(simulator.getReplication().getContext(), "animation/2D");
            this.context.bindObject(this);
        }
        catch (NamingException | RemoteException exception)
        {
            simulator.getLogger().always().warn(exception, "<init>");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics2D graphics, final Rectangle2D extent, final Dimension screen, final ImageObserver observer)
    {
        try
        {
            // is the extent or the screensize still the same
            if (extent.equals(this.cachedExtent) && screen.equals(this.cachedScreenSize) && this.map.isSame())
            {
                graphics.drawImage(this.cachedImage, 0, 0, null);
                return;
            }
            this.map.setExtent((Rectangle2D) extent.clone());
            this.map.getImage().setSize(screen);
            this.cacheImage();
            this.paint(graphics, extent, screen, observer);
        }
        catch (Exception exception)
        {
            this.logger.always().warn(exception, "paint");
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
    public Bounds getBounds()
    {
        return this.bounds;
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint getLocation()
    {
        return this.location;
    }

    /**
     * @return map the Shapefile map
     */
    public final MapInterface getMap()
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
        this.location = new DirectedPoint(
                new CartesianPoint(this.cachedExtent.getCenterX(), this.cachedExtent.getCenterY(), -Double.MIN_VALUE));
        this.bounds = new BoundingBox(this.cachedExtent.getWidth(), this.cachedExtent.getHeight(), 0.0);
    }

    /**
     * destroys an RenderableObject by unsubscribing it from the context.
     */
    @Override
    public void destroy()
    {
        try
        {
            this.context.unbindObject(this.toString());
        }
        catch (Throwable throwable)
        {
            this.logger.always().warn(throwable, "finalize");
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Point2D pointWorldCoordinates, final Rectangle2D extent, final Dimension screenSize)
    {
        return false;
    }
}
