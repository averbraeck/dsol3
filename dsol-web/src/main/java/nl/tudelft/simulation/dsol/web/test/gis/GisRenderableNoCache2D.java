package nl.tudelft.simulation.dsol.web.test.gis;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.net.URL;

import javax.media.j3d.Bounds;
import javax.naming.Context;
import javax.naming.NamingException;

import nl.javel.gisbeans.io.esri.CoordinateTransform;
import nl.javel.gisbeans.map.MapInterface;
import nl.javel.gisbeans.map.mapfile.MapFileXMLParser;
import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.animation.D2.Renderable2DInterface;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simulators.AnimatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.d3.BoundingBox;
import nl.tudelft.simulation.language.d3.CartesianPoint;
import nl.tudelft.simulation.language.d3.DirectedPoint;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * This renderable draws CAD/GIS objects.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class GisRenderableNoCache2D implements Renderable2DInterface<GisRenderableNoCache2D>, Locatable
{
    /** the map to display */
    protected MapInterface map = null;

    /** the location of the map. */
    protected DirectedPoint location = null;

    /** the bounds of the map. */
    protected Bounds bounds = null;

    /**
     * the context for (un)binding.
     */
    protected Context context;

    /**
     * constructs a new GisRenderable2D.
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator.
     * @param mapFile URL; the mapfile to use.
     */
    public GisRenderableNoCache2D(final SimulatorInterface<?, ?, ?> simulator, final URL mapFile)
    {
        this(simulator, mapFile, new CoordinateTransform.NoTransform());
    }

    /**
     * constructs a new GisRenderable2D.
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator.
     * @param mapFile URL; the mapfile to use.
     * @param coordinateTransform CoordinateTransform; the transformation of (x, y) coordinates to (x', y') coordinates.
     */
    public GisRenderableNoCache2D(final SimulatorInterface<?, ?, ?> simulator, final URL mapFile,
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
    public GisRenderableNoCache2D(final SimulatorInterface<?, ?, ?> simulator, final URL mapFile,
            final CoordinateTransform coordinateTransform, final double z)
    {
        super();
        if (!(simulator instanceof AnimatorInterface))
        {
            return;
        }
        try
        {
            this.map = MapFileXMLParser.parseMapFile(mapFile, coordinateTransform);
            this.location =
                    new DirectedPoint(new CartesianPoint(this.map.getExtent().getCenterX(), this.map.getExtent().getCenterY(), z));
            this.bounds = new BoundingBox(this.map.getExtent().getWidth(), this.map.getExtent().getHeight(), 0.0);
            // XXX simulator.getReplication().getTreatment().getProperties().put("animationPanel.extent",
            // XXX        this.map.getExtent());
            this.bind2Context(simulator);
        }
        catch (Exception exception)
        {
            SimLogger.always().warn(exception, "<init>");
        }
    }

    /**
     * constructs a new GisRenderable2D based on an existing Map.
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator.
     * @param map MapInterface; the map to use.
     * @param coordinateTransform CoordinateTransform; the transformation of (x, y) coordinates to (x', y') coordinates.
     * @param z double; the z-value to use
     */
    public GisRenderableNoCache2D(final SimulatorInterface<?, ?, ?> simulator, final MapInterface map,
            final CoordinateTransform coordinateTransform, final double z)
    {
        super();
        if (!(simulator instanceof AnimatorInterface))
        {
            return;
        }
        try
        {
            this.map = map;
            this.location =
                    new DirectedPoint(new CartesianPoint(this.map.getExtent().getCenterX(), this.map.getExtent().getCenterY(), z));
            this.bounds = new BoundingBox(this.map.getExtent().getWidth(), this.map.getExtent().getHeight(), 100.0);
            // XXX simulator.getReplication().getTreatment().getProperties().put("animationPanel.extent",
            // XXX        this.map.getExtent());
            this.bind2Context(simulator);
        }
        catch (Exception exception)
        {
            SimLogger.always().warn(exception, "<init>");
        }
    }

    /**
     * binds a renderable2D to the context. The reason for specifying this in an independent method instead of adding
     * the code in the constructor is related to the RFE submitted by van Houten that in specific distributed context,
     * such binding must be overwritten.
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator used for binding the object.
     */
    protected void bind2Context(final SimulatorInterface<?, ?, ?> simulator)
    {
        try
        {
            this.context = ContextUtil.lookup(simulator.getReplication().getContext(), "/animation/2D");
            ContextUtil.bind(this.context, this);
        }
        catch (NamingException exception)
        {
            SimLogger.always().warn(exception, "<init>");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics2D graphics, final Rectangle2D extent, final Dimension screen,
            final ImageObserver observer)
    {
        try
        {
            this.map.setExtent((Rectangle2D) extent.clone());
            this.map.drawMap(graphics);
        }
        catch (Exception exception)
        {
            SimLogger.always().warn(exception, "paint");
        }
    }

    /** {@inheritDoc} */
    @Override
    public GisRenderableNoCache2D getSource()
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
     * destroys an RenderableObject by unsubscribing it from the context.
     */
    @Override
    public void destroy()
    {
        try
        {
            ContextUtil.unbind(this.context, this);
        }
        catch (Throwable throwable)
        {
            SimLogger.always().warn(throwable, "finalize");
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Point2D pointWorldCoordinates, final Rectangle2D extent, final Dimension screenSize)
    {
        return false;
    }
}
