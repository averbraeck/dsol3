package nl.javel.gisbeans.map;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import nl.javel.gisbeans.geom.GisObject;
import nl.javel.gisbeans.geom.SerializableGeneralPath;
import nl.javel.gisbeans.geom.SerializableRectangle2D;

/**
 * Provides the implementation of a Map.
 */
public class Map implements MapInterface
{
    /** the extent of the map */
    private SerializableRectangle2D extent;

    /** the layers of the map */
    private List layers;

    /** the mapfileImage */
    private ImageInterface image;

    /** the name of the mapFile */
    private String name;

    /** the referenceMap */
    private ReferenceMapInterface referenceMap;

    /** the map units */
    private int units;

    /** the screen resolution */
    private final int RESOLUTION = 72;

    /**
     * constructs a new Map
     */
    public Map()
    {
        super();
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#addLayer(LayerInterface)
     */
    public void addLayer(LayerInterface layer)
    {
        this.layers.add(layer);
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#drawLegend(java.awt.Graphics2D)
     */
    public Graphics2D drawLegend(final Graphics2D graphics)
    {
        if (this.getImage().getLegend().isStatus())
        {
            graphics.setColor(this.getImage().getLegend().getBackgroundColor());
            graphics.fillRect(0, 0, (int) this.getImage().getLegend().getSize().getWidth(), (int) this.getImage()
                    .getLegend().getSize().getHeight());

            graphics.setColor(this.getImage().getLegend().getOutlineColor());
            graphics.drawRect(0, 0, (int) this.getImage().getLegend().getSize().getWidth(), (int) this.getImage()
                    .getLegend().getSize().getHeight());

            int space = 2;
            int position = space;
            int dPosition =
                    (int) Math.floor((this.getImage().getLegend().getSize().getHeight() - 2 * space)
                            / (1 + this.getLayers().size()));
            int nr = 0;
            for (Iterator i = this.getLayers().iterator(); i.hasNext();)
            {
                Layer layer = (Layer) i.next();
                graphics.setColor(layer.getColor());
                graphics.fillRect(space, position, dPosition - space, dPosition - space);
                if (layer.getOutlineColor() != null)
                    graphics.setColor(layer.getOutlineColor());
                graphics.drawRect(space, position, dPosition - space - 1, dPosition - space - 1);
                graphics.setFont(this.getImage().getLegend().getFont());

                FontMetrics fm = graphics.getFontMetrics(this.getImage().getLegend().getFont());

                while (fm.getStringBounds(layer.getName(), graphics).getWidth() > (this.getImage().getLegend()
                        .getSize().getWidth()
                        - 2 * space - dPosition)
                        || fm.getStringBounds(layer.getName(), graphics).getHeight() > dPosition - 2 * space)
                {
                    graphics.setFont(new Font(this.getImage().getLegend().getFont().getFontName(), Font.TRUETYPE_FONT,
                            graphics.getFont().getSize() - 1));
                    fm = graphics.getFontMetrics(graphics.getFont());
                }

                graphics.setColor(this.getImage().getLegend().getFontColor());
                graphics.drawString(layer.getName(), 4 + dPosition, dPosition + position - 4);
                position = nr++ * (int) (this.getImage().getLegend().getSize().getHeight() / (this.getLayers().size()));
            }
        }
        return graphics;
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#drawMap(Graphics2D)
     */
    public Graphics2D drawMap(Graphics2D graphics) throws GraphicsException
    {
        // We fill the background */
        graphics.setColor(this.getImage().getBackgroundColor());
        graphics.fillRect(0, 0, (int) this.getImage().getSize().getWidth(), (int) this.getImage().getSize().getHeight());

        // We compute the transform of the map
        AffineTransform transform = new AffineTransform();
        transform.scale(this.getImage().getSize().getWidth() / this.extent.getWidth(), -this.getImage().getSize()
                .getHeight()
                / this.extent.getHeight());
        transform.translate(-this.extent.getX(), -this.extent.getY() - this.extent.getHeight());
        AffineTransform antiTransform = null;
        try
        {
            antiTransform = transform.createInverse();
        }
        catch (NoninvertibleTransformException e)
        {
            e.printStackTrace();
        }

        // we cache the scale
        double scale = this.getScale();

        // we set the rendering hints
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // We loop over the layers
        for (Iterator i = this.getLayers().iterator(); i.hasNext();)
        {
            Layer layer = (Layer) i.next();
            try
            {
                if (layer.isStatus() && layer.getMaxScale() < scale && layer.getMinScale() > scale)
                {
                    List shapes = layer.getDataSource().getShapes(this.extent);
                    SerializableGeneralPath shape = null;
                    int shapeNumber = 0;
                    for (Iterator shapeIterator = shapes.iterator(); shapeIterator.hasNext();)
                    {
                        GisObject gisObject = (GisObject) shapeIterator.next();
                        if (layer.getDataSource().getType() == POINT)
                        {
                            shape = new SerializableGeneralPath();
                            Point2D point = (Point2D) gisObject.getShape();
                            shape.moveTo((float) point.getX(), (float) point.getY());

                        }
                        else
                        {
                            shape = (SerializableGeneralPath) gisObject.getShape();
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
                        for (Iterator iA = layer.getAttributes().iterator(); iA.hasNext();)
                        {
                            AbstractAttribute attribute = (AbstractAttribute) iA.next();
                            if (attribute.getMaxScale() < scale && attribute.getMinScale() > scale)
                            {
                                graphics.setColor(attribute.getFontColor());
                                graphics.setFont(attribute.getFont());
                                if (layer.isTransform())
                                {
                                    graphics.translate(shape.getBounds2D().getCenterX(), shape.getBounds2D()
                                            .getCenterY());
                                    graphics.rotate(2 * Math.PI - attribute.getAngle(shapeNumber));
                                }
                                FontMetrics fm = graphics.getFontMetrics(attribute.getFont());
                                int[] xy = new int[2];
                                switch (attribute.getPosition())
                                {
                                    case MapInterface.UL:
                                        xy[0] =
                                                (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics)
                                                        .getWidth();
                                        xy[1] = 0;
                                        break;
                                    case MapInterface.UC:
                                        xy[0] =
                                                (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics)
                                                        .getWidth() / 2;
                                        xy[1] = 0;
                                        break;
                                    case MapInterface.UR:
                                        xy[0] = 0;
                                        xy[1] = 0;
                                        break;
                                    case MapInterface.CL:
                                        xy[0] =
                                                (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics)
                                                        .getWidth();
                                        xy[1] =
                                                (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics)
                                                        .getHeight() / 2;
                                        break;
                                    case MapInterface.CC:
                                        xy[0] =
                                                (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics)
                                                        .getWidth() / 2;
                                        xy[1] =
                                                (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics)
                                                        .getHeight() / 2;
                                        break;
                                    case MapInterface.CR:
                                        xy[0] = 0;
                                        xy[1] =
                                                (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics)
                                                        .getHeight() / 2;
                                        break;
                                    case MapInterface.LL:
                                        xy[0] =
                                                (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics)
                                                        .getWidth();
                                        xy[1] =
                                                (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics)
                                                        .getHeight();
                                        break;
                                    case MapInterface.LC:
                                        xy[0] =
                                                (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics)
                                                        .getWidth() / 2;
                                        xy[1] =
                                                (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics)
                                                        .getHeight();
                                        break;
                                    case MapInterface.LR:
                                        xy[0] = 0;
                                        xy[1] =
                                                (int) -fm.getStringBounds(attribute.getValue(shapeNumber), graphics)
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
                                    graphics.translate(-shape.getBounds2D().getCenterX(), -shape.getBounds2D()
                                            .getCenterY());
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
                exception.printStackTrace();
                throw new GraphicsException(exception.getMessage());
            }
        }
        return graphics;
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#drawReferenceMap(Graphics2D)
     */
    public Graphics2D drawReferenceMap(Graphics2D graphics)
    {
        return graphics;
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#drawScalebar(Graphics2D)
     */
    public Graphics2D drawScalebar(Graphics2D graphics)
    {
        if (this.getImage().getScalebar().isStatus())
        {
            graphics.setColor(this.getImage().getScalebar().getBackgroundColor());
            graphics.fillRect(0, 0, (int) this.getImage().getScalebar().getSize().getWidth(), (int) this.getImage()
                    .getScalebar().getSize().getHeight());

            graphics.setColor(this.getImage().getScalebar().getColor());
            graphics.drawRect(0, 0, (int) this.getImage().getScalebar().getSize().getWidth() - 1, (int) this.getImage()
                    .getScalebar().getSize().getHeight() - 1);
            graphics.drawRect(0, 0, (int) this.getImage().getScalebar().getSize().getWidth() - 1, (int) this.getImage()
                    .getScalebar().getSize().getHeight() / 2);

            graphics.setFont(this.getImage().getScalebar().getFont());
            graphics.setColor(this.getImage().getScalebar().getFontColor());

            String units = new String();
            switch (this.getImage().getScalebar().getUnits())
            {
                case FEET:
                    units = " ft.";
                    break;
                case INCHES:
                    units = " in.";
                    break;
                case KILOMETERS:
                    units = " km.";
                    break;
                case METERS:
                    units = " m.";
                    break;
                case MILES:
                    units = " mi.";
                    break;
                case DD:
                    units = " dd.";
                    break;
                default:
                    units = " m.";
                    break;
            }

            DecimalFormat formatter = new DecimalFormat("#.00");

            double[] factor = {FEET_TO_METER, INCH_TO_METER, KILOMETER_TO_METER, 1, MILES_TO_METER, DD_TO_METER};
            String scale =
                    formatter.format(((this.getImage().getScalebar().getSize().getWidth() / this.getImage().getSize()
                            .getWidth()) * this.getExtent().getWidth())
                            * (factor[this.getUnits()] / factor[this.getImage().getScalebar().getUnits()]))
                            + units;

            FontMetrics fm = graphics.getFontMetrics(this.getImage().getScalebar().getFont());
            while (fm.getStringBounds((formatter.format(this.getUnitImageRatio()) + units), graphics).getWidth() > this
                    .getImage().getScalebar().getSize().getWidth()
                    || fm.getStringBounds((formatter.format(this.getUnitImageRatio()) + units), graphics).getHeight() > this
                            .getImage().getScalebar().getSize().getHeight() / 2)
            {
                graphics.setFont(new Font(this.getImage().getScalebar().getFont().getFontName(), Font.TRUETYPE_FONT,
                        graphics.getFont().getSize() - 1));
                fm = graphics.getFontMetrics(graphics.getFont());
            }
            graphics.drawString(scale, (int) this.getImage().getScalebar().getSize().getWidth() - fm.stringWidth(scale)
                    - 1, (int) this.getImage().getScalebar().getSize().getHeight() - 2);

            int x = 0;
            for (int i = 0; i <= this.getImage().getScalebar().getIntervals(); i++)
            {
                if (i % 2 != 0)
                {
                    graphics.setColor(this.getImage().getScalebar().getColor());
                    graphics.fillRect(x, 0, ((int) this.getImage().getScalebar().getSize().getWidth() / this.getImage()
                            .getScalebar().getIntervals()),
                            (int) this.getImage().getScalebar().getSize().getHeight() / 2);
                    x +=
                            (((int) this.getImage().getScalebar().getSize().getWidth()) / this.getImage().getScalebar()
                                    .getIntervals()) * 2;
                }
            }
        }
        return graphics;
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#getExtent()
     */
    public SerializableRectangle2D getExtent()
    {
        return this.extent;
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#getImage()
     */
    public ImageInterface getImage()
    {
        return this.image;
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#getLayers()
     */
    public List getLayers()
    {
        return this.layers;
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#getName()
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @see MapInterface#getScale()
     */
    public double getScale()
    {
        return (this.getImage().getSize().getWidth() / (2.54 * this.RESOLUTION)) * this.extent.getWidth();
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#getReferenceMap()
     */
    public ReferenceMapInterface getReferenceMap()
    {
        return this.referenceMap;
    }

    /**
     * returns the scale of the Image
     * @return double the unitPerPixel
     */
    public double getUnitImageRatio()
    {
        return Math.min(this.extent.getWidth() / this.image.getSize().getWidth(), this.extent.getHeight()
                / this.image.getSize().getHeight());
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#getUnits()
     */
    public int getUnits()
    {
        return this.units;
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#setExtent(java.awt.geom.Rectangle2D)
     */
    public void setExtent(final Rectangle2D extent)
    {
        this.extent =
                new SerializableRectangle2D.Double(extent.getMinX(), extent.getMinY(), extent.getWidth(),
                        extent.getHeight());
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#setImage(ImageInterface)
     */
    public void setImage(ImageInterface image)
    {
        this.image = image;
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#setLayers(List)
     */
    public void setLayers(List layers)
    {
        this.layers = layers;
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#setLayer(int, LayerInterface)
     */
    public void setLayer(int index, LayerInterface layer)
    {
        this.layers.set(index, layer);
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#setName(String)
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#setReferenceMap(ReferenceMapInterface)
     */
    public void setReferenceMap(ReferenceMapInterface referenceMap)
    {
        this.referenceMap = referenceMap;
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#setUnits(int)
     */
    public void setUnits(int units)
    {
        this.units = units;
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface#zoom(double)
     */
    public void zoom(double zoomFactor)
    {
        if (zoomFactor == 0)
        {
            zoomFactor = 1;
        }

        double maxX = (getUnitImageRatio() * this.getImage().getSize().getWidth()) + this.extent.getMinX();
        double maxY = (getUnitImageRatio() * this.getImage().getSize().getHeight()) + this.extent.getMinY();

        double centerX = (maxX - this.extent.getMinX()) / 2 + this.extent.getMinX();
        double centerY = (maxY - this.extent.getMinY()) / 2 + this.extent.getMinY();

        double width = (1.0 / zoomFactor) * (maxX - this.extent.getMinX());
        double height = (1.0 / zoomFactor) * (maxY - this.extent.getMinY());

        this.extent = new SerializableRectangle2D.Double(centerX - 0.5 * width, centerY - 0.5 * height, width, height);
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface #zoomPoint(java.awt.geom.Point2D, double)
     */
    public void zoomPoint(Point2D pixelPosition, double zoomFactor)
    {
        if (zoomFactor == 0)
            zoomFactor = 1;

        double maxX = (getUnitImageRatio() * this.getImage().getSize().getWidth()) + this.extent.getMinX();
        double maxY = (getUnitImageRatio() * this.getImage().getSize().getHeight()) + this.extent.getMinY();

        double centerX =
                (pixelPosition.getX() / this.getImage().getSize().getWidth()) * (maxX - this.extent.getMinX())
                        + this.extent.getMinX();
        double centerY =
                maxY - (pixelPosition.getY() / this.getImage().getSize().getHeight()) * (maxY - this.extent.getMinY());

        double width = (1.0 / zoomFactor) * (maxX - this.extent.getMinX());
        double height = (1.0 / zoomFactor) * (maxY - this.getExtent().getMinY());

        this.extent = new SerializableRectangle2D.Double(centerX - 0.5 * width, centerY - 0.5 * height, width, height);
    }

    /**
     * @see nl.javel.gisbeans.map.MapInterface #zoomRectangle(nl.javel.gisbeans.geom.SerializableRectangle2D)
     */
    public void zoomRectangle(SerializableRectangle2D rectangle)
    {

        double maxX = (getUnitImageRatio() * this.getImage().getSize().getWidth()) + this.extent.getMinX();
        double maxY = (getUnitImageRatio() * this.getImage().getSize().getHeight()) + this.extent.getMinY();

        double width = maxX - this.extent.getMinX();
        double height = maxY - this.extent.getMinY();

        double minX = this.extent.getMinX() + (rectangle.getMinX() / this.getImage().getSize().getWidth()) * width;
        double minY =
                this.extent.getMinY()
                        + ((this.getImage().getSize().getHeight() - rectangle.getMaxY()) / this.getImage().getSize()
                                .getHeight()) * height;

        maxX = minX + (rectangle.getWidth() / this.getImage().getSize().getWidth()) * width;
        maxY =
                minY
                        + ((this.getImage().getSize().getHeight() - rectangle.getHeight()) / this.getImage().getSize()
                                .getHeight()) * height;
        this.extent = new SerializableRectangle2D.Double(minX, minY, (maxX - minX), (maxY - minY));
    }
}