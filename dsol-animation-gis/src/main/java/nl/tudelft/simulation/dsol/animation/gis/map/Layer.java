package nl.tudelft.simulation.dsol.animation.gis.map;

import java.awt.Color;

import nl.tudelft.simulation.dsol.animation.gis.io.DataSourceInterface;

/**
 * This is an implementation of the LayerInterface that just stores the basic metadata for each layer. The actual information of
 * the layer (points, shapes) is contained in the DataSource.
 * <p>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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

    /** the name of the layer. */
    private String name;

    /** the dataSource to use. */
    private DataSourceInterface dataSource;

    /** the minScale. */
    private int minScale = Integer.MAX_VALUE;

    /** the maxScale. */
    private int maxScale = 0;

    /** the fillColor of the layer. */
    private Color fillColor = Color.WHITE;

    /** the outlineColor. */
    private Color outlineColor = Color.BLACK;

    /** whether to display the layer. */
    private boolean display = true;

    /** whether to transform the layer. */
    private boolean transform = false;

    /**
     * constructs a new layer.
     */
    public Layer()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public Color getFillColor()
    {
        return this.fillColor;
    }

    /** {@inheritDoc} */
    @Override
    public void setFillColor(final Color color)
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
    public boolean isDisplay()
    {
        return this.display;
    }

    /** {@inheritDoc} */
    @Override
    public void setDisplay(final boolean status)
    {
        this.display = status;
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
