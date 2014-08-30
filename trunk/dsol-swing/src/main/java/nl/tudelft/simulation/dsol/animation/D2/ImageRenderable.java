package nl.tudelft.simulation.dsol.animation.D2;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import nl.tudelft.simulation.dsol.animation.LocatableInterface;
import nl.tudelft.simulation.dsol.animation.StaticLocation;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.d3.BoundingBox;
import nl.tudelft.simulation.language.d3.BoundsUtil;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * An abstract class for state-dependent image renderables.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:37:21 $
 * @since 1.5
 */
public abstract class ImageRenderable extends Renderable2D
{
    /** the cache of imageIcons. */
    private transient static Map<URL, ImageIcon> cache = new HashMap<URL, ImageIcon>();

    /** LEFT-BOTTOM location. */
    public static final short LB = -4;

    /** CENTER-BOTTOM location. */
    public static final short CB = -3;

    /** RIGHT-BOTTOM location. */
    public static final short RB = -2;

    /** LEFT-CENTER location. */
    public static final short LC = -1;

    /** CENTER-CENTER location. */
    public static final short CC = 0;

    /** RIGHT-CENTER location. */
    public static final short RC = 1;

    /** LEFT-TOP location. */
    public static final short LT = 2;

    /** CENTER-TOP location. */
    public static final short CT = 3;

    /** RIGHT-TOP location. */
    public static final short RT = 4;

    /** the imageIcons to use. */
    protected URL[] images = null;

    /** the imageIcons to be used. */
    protected transient ImageIcon[] imageIcons = null;

    /**
     * the origin of the image
     */
    protected short orientation = ImageRenderable.CC;

    /**
     * constructs a new ImageRenderable.
     * @param source the source to be animated.
     * @param simulator the simulator to be used.
     * @param images the image urls.
     */
    public ImageRenderable(final LocatableInterface source, final SimulatorInterface simulator, final URL[] images)
    {
        super(source, simulator);
        this.setOrientation(ImageRenderable.CC);
        this.readImages(images);
    }

    /**
     * reads and caches the images
     * @param images the images
     */
    private void readImages(final URL[] images)
    {
        this.images = images;
        this.imageIcons = new ImageIcon[images.length];
        for (int i = 0; i < images.length; i++)
        {
            if (ImageRenderable.cache.containsKey(images[i]))
            {
                this.imageIcons[i] = ImageRenderable.cache.get(images[i]);
            }
            else
            {
                this.imageIcons[i] = new ImageIcon(images[i]);
                ImageRenderable.cache.put(images[i], this.imageIcons[i]);
            }
        }
    }

    /**
     * constructs a new ImageRenderable.
     * @param staticLocation the static location of the set of imageIcons
     * @param size the size of the imageIcons in world coordinates.
     * @param simulator the simulator to be used
     * @param images the imageIcons to display.
     */
    public ImageRenderable(final DirectedPoint staticLocation, final Dimension size,
            final SimulatorInterface simulator, final URL[] images)
    {
        this(new StaticLocation(staticLocation, new BoundingBox(size.getWidth(), size.getHeight(), 0.0)), simulator,
                images);
    }

    /**
     * constructs a new ImageRenderable.
     * @param staticLocation the static location of the set of imageIcons
     * @param size the size of the imageIcons in world coordinates.
     * @param simulator the simulator to be used
     * @param images the imageIcons to display.
     */
    public ImageRenderable(final Point2D staticLocation, final Dimension size, final SimulatorInterface simulator,
            final URL[] images)
    {
        this(new StaticLocation(new DirectedPoint(staticLocation), new BoundingBox(size.getWidth(), size.getHeight(),
                0.0)), simulator, images);
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics2D graphics, final ImageObserver observer) throws RemoteException
    {
        int image = this.selectImage();
        if (this.imageIcons[image].getImageLoadStatus() != MediaTracker.COMPLETE)
        {
            return;
        }
        Dimension size =
                BoundsUtil
                        .getIntersect(this.source.getLocation(), this.source.getBounds(), this.source.getLocation().z)
                        .getBounds().getSize();
        Point2D origin = this.resolveOrigin(this.orientation, size);
        graphics.translate(origin.getX(), origin.getY());
        graphics.scale(0.001, 0.001);
        graphics.drawImage(this.imageIcons[image].getImage(), 0, 0, (int) (1000 * size.getWidth()),
                (int) (1000 * size.getHeight()), observer);
        graphics.scale(1000, 1000);
        graphics.translate(-origin.getX(), -origin.getY());
    }

    /**
     * selects the image. This methods makes the ImageRenderable state dependent. One is required to return the index
     * number of the imageIcons[] which has to be drawn.
     * @return int the current (state-dependent) image.
     */
    public abstract int selectImage();

    /**
     * @param orientation The orientation to set.
     */
    public void setOrientation(final short orientation)
    {
        this.orientation = orientation;
    }

    /**
     * @return Returns the imageIcons.
     */
    public ImageIcon[] getImages()
    {
        return this.imageIcons;
    }

    /**
     * resolves the origin of the image
     * @param orientation the orientation (CC,..)
     * @return Point2D the location
     * @param size the size of the image.
     */
    protected Point2D resolveOrigin(final short orientation, final Dimension size)
    {
        Point2D imageOrigin = new Point2D.Double(0.0, 0.0);
        switch (orientation)
        {
            case ImageRenderable.LB:
                imageOrigin.setLocation(imageOrigin.getX(), imageOrigin.getY() - size.getHeight());
                return imageOrigin;
            case ImageRenderable.CB:
                imageOrigin.setLocation(imageOrigin.getX() - 0.5 * size.getWidth(),
                        imageOrigin.getY() - size.getHeight());
                return imageOrigin;
            case ImageRenderable.RB:
                imageOrigin.setLocation(imageOrigin.getX() - 1.0 * size.getWidth(),
                        imageOrigin.getY() - size.getHeight());
                return imageOrigin;
            case ImageRenderable.LC:
                imageOrigin.setLocation(imageOrigin.getX(), imageOrigin.getY() - 0.5 * size.getHeight());
                return imageOrigin;
            case ImageRenderable.CC:
                imageOrigin.setLocation(imageOrigin.getX() - 0.5 * size.getWidth(),
                        imageOrigin.getY() - 0.5 * size.getHeight());
                return imageOrigin;
            case ImageRenderable.RC:
                imageOrigin.setLocation(imageOrigin.getX() - 1.0 * size.getWidth(),
                        imageOrigin.getY() - 0.5 * size.getHeight());
                return imageOrigin;
            case ImageRenderable.LT:
                imageOrigin.setLocation(imageOrigin.getX(), imageOrigin.getY());
                return imageOrigin;
            case ImageRenderable.CT:
                imageOrigin.setLocation(imageOrigin.getX() - 0.5 * size.getWidth(), imageOrigin.getY());
                return imageOrigin;
            case ImageRenderable.RT:
                imageOrigin.setLocation(imageOrigin.getX() - 1.0 * size.getWidth(), imageOrigin.getY());
                return imageOrigin;
            default:
                throw new IllegalArgumentException("unknown origin location");
        }
    }

    /**
     * Returns the orientation of this image to the point [0,0]. Orientations are either LEFT, CENTER, RIGHT and TOP,
     * CENTER, or BOTTOM. An example is thus ImageRenderable.RT.
     * @return the orientation of this image
     */
    public short getOrientation()
    {
        return this.orientation;
    }

    /**
     * writes a serializable object to stream
     * @param out the outputstream
     * @throws IOException on IOException
     */
    private synchronized void writeObject(final ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }

    /**
     * reads a serializable object from stream
     * @param in the inputstream
     * @throws IOException on IOException
     */
    private synchronized void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        if (ImageRenderable.cache == null)
        {
            ImageRenderable.cache = new HashMap<URL, ImageIcon>();
        }
        this.readImages(this.images);
    }
}
