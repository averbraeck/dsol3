/*
 * Layer.java
 * 
 * Created on April 17, 2002, 7:23 PM Last edited on October 12, 2002
 */
package nl.javel.gisbeans.map;

import java.awt.Color;
import java.util.List;

import nl.javel.gisbeans.io.DataSourceInterface;

/**
 * This interface defines the image as defined in the mapInterface
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.0
 * @version 1.0
 */
public class Layer implements LayerInterface
{
    /** the fillColor of the layer. */
    private Color fillColor = new Color(255, 255, 255, 255);

    /** the dataSource to use. */
    private DataSourceInterface dataSource;

    /** the maxScale. */
    private int maxScale = 0;

    /** the minScale. */
    private int minScale = Integer.MAX_VALUE;

    /** the outlineColor. */
    private Color outlineColor = Color.BLACK;

    /** the name of the layer. */
    private String name;

    /** the status. */
    private boolean status = true;

    /** whether to transform. */
    private boolean transform = false;

    /** the attributes of the layer. */
    private List attributes;

    /** the symbols of the layer. */
    private List symbols;

    /**
     * constructs a new layer.
     */
    public Layer()
    {
        super();
    }

    /** {@inheritDoc} */
    public List getSymbols()
    {
        return this.symbols;
    }

    /** {@inheritDoc} */
    public List getAttributes()
    {
        return this.attributes;
    }

    /** {@inheritDoc} */
    public AttributeInterface getAttribute(int index)
    {
        return (AttributeInterface) this.attributes.get(index);
    }

    /** {@inheritDoc} */
    public Color getColor()
    {
        return this.fillColor;
    }

    /** {@inheritDoc} */
    public void setColor(Color color)
    {
        this.fillColor = color;
    }

    /** {@inheritDoc} */
    public DataSourceInterface getDataSource()
    {
        return this.dataSource;
    }

    /** {@inheritDoc} */
    public void setAttributes(List attributes)
    {
        this.attributes = attributes;
    }

    /** {@inheritDoc} */
    public void setDataSource(DataSourceInterface dataSource)
    {
        this.dataSource = dataSource;
    }

    /** {@inheritDoc} */
    public int getMaxScale()
    {
        return this.maxScale;
    }

    /** {@inheritDoc} */
    public void setMaxScale(int maxScale)
    {
        this.maxScale = maxScale;
    }

    /** {@inheritDoc} */
    public int getMinScale()
    {
        return this.minScale;
    }

    /** {@inheritDoc} */
    public void setMinScale(int minScale)
    {
        this.minScale = minScale;
    }

    /** {@inheritDoc} */
    public String getName()
    {
        return this.name;
    }

    /** {@inheritDoc} */
    public void setName(String name)
    {
        this.name = name;
    }

    /** {@inheritDoc} */
    public Color getOutlineColor()
    {
        return this.outlineColor;
    }

    /** {@inheritDoc} */
    public void setOutlineColor(Color outlineColor)
    {
        this.outlineColor = outlineColor;
    }

    /** {@inheritDoc} */
    public boolean isStatus()
    {
        return this.status;
    }

    /** {@inheritDoc} */
    public void setStatus(boolean status)
    {
        this.status = status;
    }

    /** {@inheritDoc} */
    public boolean isTransform()
    {
        return this.transform;
    }

    /** {@inheritDoc} */
    public void setTransform(boolean transform)
    {
        this.transform = transform;
    }

    /**
     * Sets the symbols.
     * @param symbols The symbols to set
     */
    public void setSymbols(final List symbols)
    {
        this.symbols = symbols;
    }
}
