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
    /** the fillColor of the layer */
    private Color fillColor = new Color(255, 255, 255, 255);

    /** the dataSource to use */
    private DataSourceInterface dataSource;

    /** the maxScale */
    private int maxScale = 0;

    /** the minScale */
    private int minScale = Integer.MAX_VALUE;

    /** the outlineColor */
    private Color outlineColor = Color.BLACK;

    /** the name of the layer */
    private String name;

    /** the status */
    private boolean status = true;

    /** whether to transform */
    private boolean transform = false;

    /** the attributes of the layer */
    private List attributes;

    /** the symbols of the layer */
    private List symbols;

    /**
     * constructs a new layer
     */
    public Layer()
    {
        super();
    }

    /**
     * @see nl.javel.gisbeans.map.LayerInterface#getSymbols()
     */
    public List getSymbols()
    {
        return this.symbols;
    }

    /**
     * @see nl.javel.gisbeans.map.LayerInterface#getAttributes()
     */
    public List getAttributes()
    {
        return this.attributes;
    }

    /**
     * @see nl.javel.gisbeans.map.LayerInterface#getAttribute(int)
     */
    public AttributeInterface getAttribute(int index)
    {
        return (AttributeInterface) this.attributes.get(index);
    }

    /**
     * @see LayerInterface#getColor()
     */
    public Color getColor()
    {
        return this.fillColor;
    }

    /**
     * @see LayerInterface#setColor(Color)
     */
    public void setColor(Color color)
    {
        this.fillColor = color;
    }

    /**
     * @see LayerInterface#getDataSource()
     */
    public DataSourceInterface getDataSource()
    {
        return this.dataSource;
    }

    /**
     * @see LayerInterface#setAttributes(List attributes)
     */
    public void setAttributes(List attributes)
    {
        this.attributes = attributes;
    }

    /**
     * @see LayerInterface#setDataSource(nl.javel.gisbeans.io.DataSourceInterface)
     */
    public void setDataSource(DataSourceInterface dataSource)
    {
        this.dataSource = dataSource;
    }

    /**
     * @see LayerInterface#getMaxScale()
     */
    public int getMaxScale()
    {
        return this.maxScale;
    }

    /**
     * @see LayerInterface#setMaxScale(int)
     */
    public void setMaxScale(int maxScale)
    {
        this.maxScale = maxScale;
    }

    /**
     * @see LayerInterface#getMinScale()
     */
    public int getMinScale()
    {
        return this.minScale;
    }

    /**
     * @see LayerInterface#setMinScale(int)
     */
    public void setMinScale(int minScale)
    {
        this.minScale = minScale;
    }

    /**
     * @see LayerInterface#getName()
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @see LayerInterface#setName(String)
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @see LayerInterface#getOutlineColor()
     */
    public Color getOutlineColor()
    {
        return this.outlineColor;
    }

    /**
     * @see LayerInterface#setOutlineColor(Color)
     */
    public void setOutlineColor(Color outlineColor)
    {
        this.outlineColor = outlineColor;
    }

    /**
     * @see LayerInterface#isStatus()
     */
    public boolean isStatus()
    {
        return this.status;
    }

    /**
     * @see LayerInterface#setStatus(boolean)
     */
    public void setStatus(boolean status)
    {
        this.status = status;
    }

    /**
     * @see LayerInterface#isTransform()
     */
    public boolean isTransform()
    {
        return this.transform;
    }

    /**
     * @see LayerInterface#setTransform(boolean)
     */
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