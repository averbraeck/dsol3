package nl.tudelft.simulation.dsol.animation.gis.map;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.djutils.immutablecollections.ImmutableArrayList;
import org.djutils.immutablecollections.ImmutableHashMap;
import org.djutils.immutablecollections.ImmutableList;
import org.djutils.immutablecollections.ImmutableMap;
import org.djutils.logger.CategoryLogger;

import nl.tudelft.simulation.dsol.animation.gis.GisObject;
import nl.tudelft.simulation.dsol.animation.gis.GraphicsException;
import nl.tudelft.simulation.dsol.animation.gis.ScreenPosition;
import nl.tudelft.simulation.dsol.animation.gis.SerializablePath;
import nl.tudelft.simulation.dsol.animation.gis.SerializableRectangle2D;

/**
 * Provides the implementation of a Map.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class Map implements MapInterface
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the extent of the map. */
    private SerializableRectangle2D extent;

    /** the map of layer names to layers. */
    private java.util.Map<String, LayerInterface> layerMap = new HashMap<>();

    /** the total list of layers of the map. */
    private List<LayerInterface> allLayers = new ArrayList<>();

    /** the visible layers of the map. */
    private List<LayerInterface> visibleLayers = new ArrayList<>();

    /** same set to false after layer change. */
    private boolean same = false;

    /** the mapfileImage. */
    private MapImageInterface image;

    /** the name of the mapFile. */
    private String name;

    /** the map units. */
    private int units;

    /** draw the background? */
    private boolean drawBackground = true;

    /** the screen resolution. */
    private static final int RESOLUTION = 72;

    /**
     * constructs a new Map.
     */
    public Map()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void addLayer(final LayerInterface layer)
    {
        this.visibleLayers.add(layer);
        this.allLayers.add(layer);
        this.layerMap.put(layer.getName(), layer);
        this.same = false;
    }

    /** {@inheritDoc} */
    @Override
    public void setLayers(final List<LayerInterface> layers)
    {
        this.allLayers = new ArrayList<>(layers);
        this.visibleLayers = new ArrayList<>(layers);
        this.layerMap.clear();
        for (LayerInterface layer : layers)
        {
            this.layerMap.put(layer.getName(), layer);
        }
        this.same = false;
    }

    /** {@inheritDoc} */
    @Override
    public void setLayer(final int index, final LayerInterface layer)
    {
        this.allLayers.set(index, layer);
        if (this.allLayers.size() == this.visibleLayers.size())
        {
            this.visibleLayers.add(index, layer);
        }
        else
        {
            this.visibleLayers.add(layer);
        }
        this.layerMap.put(layer.getName(), layer);
        this.same = false;
    }

    /** {@inheritDoc} */
    @Override
    public void hideLayer(final LayerInterface layer)
    {
        this.visibleLayers.remove(layer);
        this.same = false;
    }

    /** {@inheritDoc} */
    @Override
    public void hideLayer(final String layerName) throws RemoteException
    {
        if (this.layerMap.keySet().contains(layerName))
        {
            hideLayer(this.layerMap.get(layerName));
        }
        this.same = false;
    }

    /** {@inheritDoc} */
    @Override
    public void showLayer(final LayerInterface layer)
    {
        this.visibleLayers.add(layer);
        this.same = false;
    }

    /** {@inheritDoc} */
    @Override
    public void showLayer(final String layerName) throws RemoteException
    {
        if (this.layerMap.keySet().contains(layerName))
        {
            showLayer(this.layerMap.get(layerName));
        }
        this.same = false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isSame() throws RemoteException
    {
        boolean ret = this.same;
        this.same = true;
        return ret;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:methodlength")
    public Graphics2D drawMap(final Graphics2D graphics) throws GraphicsException
    {
        if (this.drawBackground)
        {
            // We fill the background.
            graphics.setColor(this.getImage().getBackgroundColor());
            graphics.fillRect(0, 0, (int) this.getImage().getSize().getWidth(), (int) this.getImage().getSize().getHeight());
        }

        // We compute the transform of the map
        AffineTransform transform = new AffineTransform();
        transform.scale(this.getImage().getSize().getWidth() / this.extent.getWidth(),
                -this.getImage().getSize().getHeight() / this.extent.getHeight());
        transform.translate(-this.extent.getX(), -this.extent.getY() - this.extent.getHeight());
        AffineTransform antiTransform = null;
        try
        {
            antiTransform = transform.createInverse();
        }
        catch (NoninvertibleTransformException e)
        {
            CategoryLogger.always().error(e);
        }

        // we cache the scale
        double scale = this.getScale();

        // we set the rendering hints
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // We loop over the layers
        for (Iterator<LayerInterface> i = this.visibleLayers.iterator(); i.hasNext();)
        {
            Layer layer = (Layer) i.next();
            try
            {
                if (layer.isStatus() && layer.getMaxScale() < scale && layer.getMinScale() > scale)
                {
                    List<GisObject> shapes = layer.getDataSource().getShapes(this.extent);
                    SerializablePath shape = null;
                    int shapeNumber = 0;
                    for (Iterator<GisObject> shapeIterator = shapes.iterator(); shapeIterator.hasNext();)
                    {
                        GisObject gisObject = shapeIterator.next();
                        if (layer.getDataSource().getType() == POINT)
                        {
                            shape = new SerializablePath();
                            Point2D point = (Point2D) gisObject.getShape();
                            shape.moveTo((float) point.getX(), (float) point.getY());

                        }
                        else
                        {
                            shape = (SerializablePath) gisObject.getShape();
                        }
                        if (layer.isTransform())
                        {
                            shape.transform(transform);
                        }
                        graphics.setColor(layer.getColor());
                        if (layer.getDataSource().getType() == POLYGON)
                        {
                            graphics.fill(shape);
                        }
                        if (layer.getOutlineColor() != null)
                        {
                            graphics.setColor(layer.getOutlineColor());
                        }
                        graphics.draw(shape);
                        for (Iterator<? extends AttributeInterface> iA = layer.getAttributes().iterator(); iA.hasNext();)
                        {
                            AttributeInterface attribute = iA.next();
                            if (attribute.getMaxScale() < scale && attribute.getMinScale() > scale)
                            {
                                graphics.setColor(attribute.getFontColor());
                                graphics.setFont(attribute.getFont());
                                if (layer.isTransform())
                                {
                                    graphics.translate(shape.getBounds2D().getCenterX(), shape.getBounds2D().getCenterY());
                                    graphics.rotate(2 * Math.PI - attribute.getAngle(shapeNumber));
                                }
                                FontMetrics fm = graphics.getFontMetrics(attribute.getFont());
                                int[] xy = new int[2];
                                switch (attribute.getPosition())
                                {
                                    case UpperLeft:
                                        xy[0] = (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics).getWidth();
                                        xy[1] = 0;
                                        break;
                                    case UpperCenter:
                                        xy[0] = (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics).getWidth()
                                                / 2;
                                        xy[1] = 0;
                                        break;
                                    case UpperRight:
                                        xy[0] = 0;
                                        xy[1] = 0;
                                        break;
                                    case CenterLeft:
                                        xy[0] = (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics).getWidth();
                                        xy[1] = (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics).getHeight()
                                                / 2;
                                        break;
                                    case CenterCenter:
                                        xy[0] = (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics).getWidth()
                                                / 2;
                                        xy[1] = (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics).getHeight()
                                                / 2;
                                        break;
                                    case CenterRight:
                                        xy[0] = 0;
                                        xy[1] = (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics).getHeight()
                                                / 2;
                                        break;
                                    case LowerLeft:
                                        xy[0] = (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics).getWidth();
                                        xy[1] = (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics)
                                                .getHeight();
                                        break;
                                    case LowerCenter:
                                        xy[0] = (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics).getWidth()
                                                / 2;
                                        xy[1] = (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics)
                                                .getHeight();
                                        break;
                                    case LowerRight:
                                        xy[0] = 0;
                                        xy[1] = (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics)
                                                .getHeight();
                                        break;
                                    default:
                                        xy[0] = 0;
                                        xy[1] = 0;
                                        break;
                                }
                                graphics.drawString(attribute.getValue(shapeNumber), xy[0], xy[1]);
                                if (layer.isTransform())
                                {
                                    graphics.rotate(-(2 * Math.PI - attribute.getAngle(shapeNumber)));
                                    graphics.translate(-shape.getBounds2D().getCenterX(), -shape.getBounds2D().getCenterY());
                                }
                            }
                        }
                        if (layer.isTransform())
                        {
                            shape.transform(antiTransform);
                        }
                        shapeNumber++;
                    }
                }
            }
            catch (Exception exception)
            {
                CategoryLogger.always().error(exception);
                throw new GraphicsException(exception.getMessage());
            }
        }
        return graphics;
    }

    /** {@inheritDoc} */
    @Override
    public SerializableRectangle2D getExtent()
    {
        return this.extent;
    }

    /** {@inheritDoc} */
    @Override
    public MapImageInterface getImage()
    {
        return this.image;
    }

    /** {@inheritDoc} */
    @Override
    public ImmutableList<LayerInterface> getAllLayers()
    {
        return new ImmutableArrayList<>(this.allLayers);
    }

    /** {@inheritDoc} */
    @Override
    public ImmutableList<LayerInterface> getVisibleLayers()
    {
        return new ImmutableArrayList<>(this.visibleLayers);
    }

    /** {@inheritDoc} */
    @Override
    public ImmutableMap<String, LayerInterface> getLayerMap() throws RemoteException
    {
        return new ImmutableHashMap<>(this.layerMap);
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        return this.name;
    }

    /** {@inheritDoc} */
    @Override
    public double getScale()
    {
        return (this.getImage().getSize().getWidth() / (2.54 * RESOLUTION)) * this.extent.getWidth();
    }

    /**
     * returns the scale of the Image.
     * @return double the unitPerPixel
     */
    @Override
    public double getUnitImageRatio()
    {
        return Math.min(this.extent.getWidth() / this.image.getSize().getWidth(),
                this.extent.getHeight() / this.image.getSize().getHeight());
    }

    /** {@inheritDoc} */
    @Override
    public int getUnits()
    {
        return this.units;
    }

    /** {@inheritDoc} */
    @Override
    public void setExtent(final Rectangle2D extent)
    {
        this.extent =
                new SerializableRectangle2D.Double(extent.getMinX(), extent.getMinY(), extent.getWidth(), extent.getHeight());
    }

    /** {@inheritDoc} */
    @Override
    public void setImage(final MapImageInterface image)
    {
        this.image = image;
    }

    /** {@inheritDoc} */
    @Override
    public void setName(final String name)
    {
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override
    public void setUnits(final int units)
    {
        this.units = units;
    }

    /** {@inheritDoc} */
    @Override
    public void zoom(final double zoomFactor)
    {
        double correcteddZoomFactor = (zoomFactor == 0) ? 1 : zoomFactor;

        double maxX = (getUnitImageRatio() * this.getImage().getSize().getWidth()) + this.extent.getMinX();
        double maxY = (getUnitImageRatio() * this.getImage().getSize().getHeight()) + this.extent.getMinY();

        double centerX = (maxX - this.extent.getMinX()) / 2 + this.extent.getMinX();
        double centerY = (maxY - this.extent.getMinY()) / 2 + this.extent.getMinY();

        double width = (1.0 / correcteddZoomFactor) * (maxX - this.extent.getMinX());
        double height = (1.0 / correcteddZoomFactor) * (maxY - this.extent.getMinY());

        this.extent = new SerializableRectangle2D.Double(centerX - 0.5 * width, centerY - 0.5 * height, width, height);
    }

    /** {@inheritDoc} */
    @Override
    public void zoomPoint(final Point2D pixelPosition, final double zoomFactor)
    {
        double correcteddZoomFactor = (zoomFactor == 0) ? 1 : zoomFactor;

        double maxX = (getUnitImageRatio() * this.getImage().getSize().getWidth()) + this.extent.getMinX();
        double maxY = (getUnitImageRatio() * this.getImage().getSize().getHeight()) + this.extent.getMinY();

        double centerX = (pixelPosition.getX() / this.getImage().getSize().getWidth()) * (maxX - this.extent.getMinX())
                + this.extent.getMinX();
        double centerY = maxY - (pixelPosition.getY() / this.getImage().getSize().getHeight()) * (maxY - this.extent.getMinY());

        double width = (1.0 / correcteddZoomFactor) * (maxX - this.extent.getMinX());
        double height = (1.0 / correcteddZoomFactor) * (maxY - this.getExtent().getMinY());

        this.extent = new SerializableRectangle2D.Double(centerX - 0.5 * width, centerY - 0.5 * height, width, height);
    }

    /** {@inheritDoc} */
    @Override
    public void zoomRectangle(final SerializableRectangle2D rectangle)
    {

        double maxX = (getUnitImageRatio() * this.getImage().getSize().getWidth()) + this.extent.getMinX();
        double maxY = (getUnitImageRatio() * this.getImage().getSize().getHeight()) + this.extent.getMinY();

        double width = maxX - this.extent.getMinX();
        double height = maxY - this.extent.getMinY();

        double minX = this.extent.getMinX() + (rectangle.getMinX() / this.getImage().getSize().getWidth()) * width;
        double minY = this.extent.getMinY()
                + ((this.getImage().getSize().getHeight() - rectangle.getMaxY()) / this.getImage().getSize().getHeight())
                        * height;

        maxX = minX + (rectangle.getWidth() / this.getImage().getSize().getWidth()) * width;
        maxY = minY + ((this.getImage().getSize().getHeight() - rectangle.getHeight()) / this.getImage().getSize().getHeight())
                * height;
        this.extent = new SerializableRectangle2D.Double(minX, minY, (maxX - minX), (maxY - minY));
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isDrawBackground()
    {
        return this.drawBackground;
    }

    /** {@inheritDoc} */
    @Override
    public final void setDrawBackground(final boolean drawBackground)
    {
        this.drawBackground = drawBackground;
    }

}
