package nl.tudelft.simulation.dsol.animation.gis.map;

import java.awt.Color;
import java.util.List;

import nl.tudelft.simulation.dsol.animation.gis.io.DataSourceInterface;

/**
 * This interface defines the image as defined in the mapInterface.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class Layer implements LayerInterface
{
    /** */
    private static final long serialVersionUID = 20201015L;

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
    private List<? extends AttributeInterface> attributes;

    /**
     * constructs a new layer.
     */
    public Layer()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public List<? extends AttributeInterface> getAttributes()
    {
        return this.attributes;
    }

    /** {@inheritDoc} */
    @Override
    public AttributeInterface getAttribute(final int index)
    {
        return this.attributes.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public Color getColor()
    {
        return this.fillColor;
    }

    /** {@inheritDoc} */
    @Override
    public void setColor(final Color color)
    {
        this.fillColor = color;
    }

    /** {@inheritDoc} */
    @Override
    public DataSourceInterface getDataSource()
    {
        return this.dataSource;
    }

    /** {@inheritDoc} */
    @Override
    public void setAttributes(final List<? extends AttributeInterface> attributes)
    {
        this.attributes = attributes;
    }

    /** {@inheritDoc} */
    @Override
    public void setDataSource(final DataSourceInterface dataSource)
    {
        this.dataSource = dataSource;
    }

    /** {@inheritDoc} */
    @Override
    public int getMaxScale()
    {
        return this.maxScale;
    }

    /** {@inheritDoc} */
    @Override
    public void setMaxScale(final int maxScale)
    {
        this.maxScale = maxScale;
    }

    /** {@inheritDoc} */
    @Override
    public int getMinScale()
    {
        return this.minScale;
    }

    /** {@inheritDoc} */
    @Override
    public void setMinScale(final int minScale)
    {
        this.minScale = minScale;
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        return this.name;
    }

    /** {@inheritDoc} */
    @Override
    public void setName(final String name)
    {
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override
    public Color getOutlineColor()
    {
        return this.outlineColor;
    }

    /** {@inheritDoc} */
    @Override
    public void setOutlineColor(final Color outlineColor)
    {
        this.outlineColor = outlineColor;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isStatus()
    {
        return this.status;
    }

    /** {@inheritDoc} */
    @Override
    public void setStatus(final boolean status)
    {
        this.status = status;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isTransform()
    {
        return this.transform;
    }

    /** {@inheritDoc} */
    @Override
    public void setTransform(final boolean transform)
    {
        this.transform = transform;
    }

}
