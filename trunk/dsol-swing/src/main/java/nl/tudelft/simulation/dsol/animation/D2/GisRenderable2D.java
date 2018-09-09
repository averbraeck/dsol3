package nl.tudelft.simulation.dsol.animation.D2;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.rmi.RemoteException;

import javax.media.j3d.Bounds;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.javel.gisbeans.io.esri.CoordinateTransform;
import nl.javel.gisbeans.map.MapInterface;
import nl.javel.gisbeans.map.mapfile.MapFileXMLParser;
import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.simulators.AnimatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.d3.BoundingBox;
import nl.tudelft.simulation.language.d3.CartesianPoint;
import nl.tudelft.simulation.language.d3.DirectedPoint;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * This renderable draws CAD/GIS objects.
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:37:20 $
 * @since 1.5
 */
public class GisRenderable2D implements Renderable2DInterface, Locatable
{
    /** the map to display */
    protected MapInterface map = null;

    /** the image cached image. */
    protected BufferedImage image = null;

    /** the cached extent. */
    protected Rectangle2D extent = new Rectangle2D.Double();

    /** the cached screenSize. */
    protected Dimension screenSize = new Dimension();

    /** the location of the image. */
    protected DirectedPoint location = null;

    /** the bounds of the image. */
    protected Bounds bounds = null;

    /** the logger. */
    private static Logger logger = LogManager.getLogger(GisRenderable2D.class);

    /**
     * the context for (un)binding.
     */
    protected Context context;

    /**
     * constructs a new GisRenderable2D.
     * @param simulator the simulator.
     * @param mapFile the mapfile to use.
     */
    public GisRenderable2D(final SimulatorInterface<?, ?, ?> simulator, final URL mapFile)
    {
        this(simulator, mapFile, new CoordinateTransform.NoTransform());
    }

    /**
     * constructs a new GisRenderable2D.
     * @param simulator the simulator.
     * @param mapFile the mapfile to use.
     * @param coordinateTransform the transformation of (x, y) coordinates to (x', y') coordinates.
     */
    public GisRenderable2D(final SimulatorInterface<?, ?, ?> simulator, final URL mapFile,
            final CoordinateTransform coordinateTransform)
    {
        this(simulator, mapFile, coordinateTransform, -Double.MAX_VALUE);
    }

    /**
     * constructs a new GisRenderable2D.
     * @param simulator the simulator.
     * @param mapFile the mapfile to use.
     * @param coordinateTransform the transformation of (x, y) coordinates to (x', y') coordinates.
     * @param z the z-value to use
     */
    public GisRenderable2D(final SimulatorInterface<?, ?, ?> simulator, final URL mapFile,
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
                    new DirectedPoint(new CartesianPoint(this.extent.getCenterX(), this.extent.getCenterY(), z));
            this.bounds = new BoundingBox(this.extent.getWidth(), this.extent.getHeight(), 0.0);
            simulator.getReplication().getTreatment().getProperties().put("animationPanel.extent",
                    this.map.getExtent());
            this.bind2Context(simulator);
        }
        catch (Exception exception)
        {
            logger.warn("<init>", exception);
        }
    }

    /**
     * constructs a new GisRenderable2D based on an existing Map.
     * @param simulator the simulator.
     * @param map the map to use.
     * @param coordinateTransform the transformation of (x, y) coordinates to (x', y') coordinates.
     * @param z the z-value to use
     */
    public GisRenderable2D(final SimulatorInterface<?, ?, ?> simulator, final MapInterface map,
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
                    new DirectedPoint(new CartesianPoint(this.extent.getCenterX(), this.extent.getCenterY(), z));
            this.bounds = new BoundingBox(this.extent.getWidth(), this.extent.getHeight(), 0.0);
            simulator.getReplication().getTreatment().getProperties().put("animationPanel.extent",
                    this.map.getExtent());
            this.bind2Context(simulator);
        }
        catch (Exception exception)
        {
            logger.warn("<init>", exception);
        }
    }

    /**
     * binds a renderable2D to the context. The reason for specifying this in an independent method instead of adding
     * the code in the constructor is related to the RFE submitted by van Houten that in specific distributed context,
     * such binding must be overwritten.
     * @param simulator the simulator used for binding the object.
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
            logger.warn("<init>", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics2D graphics, final Rectangle2D extent, final Dimension screen,
            final ImageObserver observer)
    {
        try
        {
            // is the extent or the screensize still the same
            if (extent.equals(this.extent) && screen.equals(this.screenSize) && this.map.isSame())
            {
                graphics.drawImage(this.image, 0, 0, null);
                return;
            }
            this.map.setExtent((Rectangle2D) extent.clone());
            this.map.getImage().setSize(screen);
            this.cacheImage();
            this.paint(graphics, extent, screen, observer);
        }
        catch (Exception exception)
        {
            logger.warn("paint", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Locatable getSource()
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
        this.image = new BufferedImage((int) this.map.getImage().getSize().getWidth(),
                (int) this.map.getImage().getSize().getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D bg = this.image.createGraphics();
        this.map.drawMap(bg);
        bg.dispose();
        this.screenSize = (Dimension) this.map.getImage().getSize().clone();
        this.extent = this.map.getExtent();
        this.location = new DirectedPoint(
                new CartesianPoint(this.extent.getCenterX(), this.extent.getCenterY(), -Double.MIN_VALUE));
        this.bounds = new BoundingBox(this.extent.getWidth(), this.extent.getHeight(), 0.0);
    }

    /**
     * destroys an RenderableObject by unsubscribing it from the context.
     */
    public void destroy()
    {
        try
        {
            ContextUtil.unbind(this.context, this);
        }
        catch (Throwable throwable)
        {
            logger.warn("finalize", throwable);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Point2D pointWorldCoordinates, final Rectangle2D extent, final Dimension screen)
    {
        return false;
    }
}
