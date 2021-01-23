package nl.tudelft.simulation.dsol.swing.animation.D2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.text.NumberFormat;

import javax.swing.JPanel;

import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;

import nl.tudelft.simulation.dsol.animation.D2.Renderable2DInterface;

/**
 * The GridPanel introduces the gridPanel.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author Niels Lang
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public class GridPanel extends JPanel
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the UP directions for moving/zooming. */
    public static final int UP = 1;

    /** the DOWN directions for moving/zooming. */
    public static final int DOWN = 2;

    /** the LEFT directions for moving/zooming. */
    public static final int LEFT = 3;

    /** the RIGHT directions for moving/zooming. */
    public static final int RIGHT = 4;

    /** the ZOOM factor. */
    public static final double ZOOMFACTOR = 1.2;

    /** gridColor. */
    protected static final Color GRIDCOLOR = Color.BLACK;

    /** the extent of this panel. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Bounds2d extent = null;

    /** the initial and default extent of this panel. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Bounds2d homeExtent = null;

    /** show the grid. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected boolean showGrid = true;

    /** the gridSize in world Units. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double gridSize = 100.0;

    /** the formatter to use. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected NumberFormat formatter = NumberFormat.getInstance();

    /** the last computed Dimension. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Dimension lastDimension = null;

    /** the last known world coordinate of the mouse. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Point2D worldCoordinate = new Point2D.Double();

    /** whether to show a tooltip with the coordinates or not. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected boolean showToolTip = true;

    /**
     * constructs a new GridPanel.
     * @param homeExtent Bounds2d; the initial extent.
     */
    public GridPanel(final Bounds2d homeExtent)
    {
        super();
        this.extent = homeExtent;
        this.homeExtent = homeExtent;
        this.setBackground(Color.WHITE);
        this.lastDimension = this.getSize();
    }

    /**
     * returns the extent of this panel.
     * @return Bounds2d
     */
    public final Bounds2d getExtent()
    {
        return this.extent;
    }

    /** {@inheritDoc} */
    @Override
    public void paintComponent(final Graphics g)
    {
        super.paintComponent(g);

        if (!this.getSize().equals(this.lastDimension))
        {
            this.lastDimension = this.getSize();
            this.extent = Renderable2DInterface.Util.computeVisibleExtent(this.extent, this.getSize());
        }
        if (this.showGrid)
        {
            this.drawGrid(g);
        }
    }

    /**
     * show the grid?
     * @param bool boolean; true/false
     */
    public final synchronized void showGrid(final boolean bool)
    {
        this.showGrid = bool;
        this.repaint();
    }

    /**
     * Set the world coordinates based on a mouse move.
     * @param point Point2D; the x,y world coordinates
     */
    public final synchronized void setWorldCoordinate(final Point2D point)
    {
        this.worldCoordinate = point;
    }

    /**
     * @return worldCoordinate
     */
    public final synchronized Point2D getWorldCoordinate()
    {
        return this.worldCoordinate;
    }

    /**
     * Display a tooltip with the last known world coordinates of the mouse, in case the tooltip should be displayed.
     */
    public final synchronized void displayWorldCoordinateToolTip()
    {
        if (this.showToolTip)
        {
            String worldPoint = "(x=" + this.formatter.format(this.worldCoordinate.getX()) + " ; y="
                    + this.formatter.format(this.worldCoordinate.getY()) + ")";
            setToolTipText(worldPoint);
        }
    }

    /**
     * @return showToolTip
     */
    public final synchronized boolean isShowToolTip()
    {
        return this.showToolTip;
    }

    /**
     * @param showToolTip boolean; set showToolTip
     */
    public final synchronized void setShowToolTip(final boolean showToolTip)
    {
        this.showToolTip = showToolTip;
    }

    /**
     * pans the panel in a specified direction.
     * @param direction int; the direction
     * @param percentage double; the percentage
     */
    public final synchronized void pan(final int direction, final double percentage)
    {
        if (percentage <= 0 || percentage > 1.0)
        {
            throw new IllegalArgumentException("percentage<=0 || >1.0");
        }
        switch (direction)
        {
            case LEFT:
                this.extent = new Bounds2d(this.extent.getMinX() - percentage * this.extent.getDeltaX(),
                        this.extent.getMaxX() - percentage * this.extent.getDeltaX(), this.extent.getMinY(),
                        this.extent.getMaxY());
                break;
            case RIGHT:
                this.extent = new Bounds2d(this.extent.getMinX() + percentage * this.extent.getDeltaX(),
                        this.extent.getMaxX() + percentage * this.extent.getDeltaX(), this.extent.getMinY(),
                        this.extent.getMaxY());
                break;
            case UP:
                this.extent = new Bounds2d(this.extent.getMinX(), this.extent.getMaxX(),
                        this.extent.getMinY() + percentage * this.extent.getDeltaY(),
                        this.extent.getMaxY() + percentage * this.extent.getDeltaY());
                break;
            case DOWN:
                this.extent = new Bounds2d(this.extent.getMinX(), this.extent.getMaxX(),
                        this.extent.getMinY() - percentage * this.extent.getDeltaY(),
                        this.extent.getMaxY() - percentage * this.extent.getDeltaY());
                break;
            default:
                throw new IllegalArgumentException("direction unkown");
        }
        this.repaint();
    }

    /**
     * resets the panel to its original extent.
     */
    public final synchronized void home()
    {
        this.extent = Renderable2DInterface.Util.computeVisibleExtent(this.homeExtent, this.getSize());
        this.repaint();
    }

    /**
     * @return Returns the showGrid.
     */
    public final boolean isShowGrid()
    {
        return this.showGrid;
    }

    /**
     * @param showGrid boolean; The showGrid to set.
     */
    public final void setShowGrid(final boolean showGrid)
    {
        this.showGrid = showGrid;
    }

    /**
     * zooms in/out.
     * @param factor double; The zoom factor
     */
    public final synchronized void zoom(final double factor)
    {
        zoom(factor, (int) (this.getWidth() / 2.0), (int) (this.getHeight() / 2.0));
    }

    /**
     * zooms in/out.
     * @param factor double; The zoom factor
     * @param mouseX int; x-position of the mouse around which we zoom
     * @param mouseY int; y-position of the mouse around which we zoom
     */
    public final synchronized void zoom(final double factor, final int mouseX, final int mouseY)
    {
        Point2d mwc =
                Renderable2DInterface.Util.getWorldCoordinates(new Point2D.Double(mouseX, mouseY), this.extent, this.getSize());
        double minX = mwc.getX() - (mwc.getX() - this.extent.getMinX()) * factor;
        double minY = mwc.getY() - (mwc.getY() - this.extent.getMinY()) * factor;
        double w = this.extent.getDeltaX() * factor;
        double h = this.extent.getDeltaY() * factor;

        this.extent = new Bounds2d(minX, minX + w, minY, minY + h);
        this.repaint();
    }

    /**
     * Added to make sure the recursive render-call calls THIS render method instead of a potential super-class defined
     * 'paintComponent' render method.
     * @param g Graphics; the graphics object
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected synchronized void drawGrid(final Graphics g)
    {
        // we prepare the graphics object for the grid
        g.setFont(g.getFont().deriveFont(11.0f));
        g.setColor(GRIDCOLOR);
        double scale = Renderable2DInterface.Util.getScale(this.extent, this.getSize());

        int count = 0;
        int gridSizePixels = (int) Math.round(this.gridSize / scale);
        while (gridSizePixels < 40)
        {
            this.gridSize = 10 * this.gridSize;
            int maximumNumberOfDigits = (int) Math.max(0, 1 + Math.ceil(Math.log(1 / this.gridSize) / Math.log(10)));
            this.formatter.setMaximumFractionDigits(maximumNumberOfDigits);
            gridSizePixels = (int) Math.round(this.gridSize / scale);
            if (count++ > 10)
            {
                break;
            }
        }

        count = 0;
        while (gridSizePixels > 10 * 40)
        {
            int maximumNumberOfDigits = (int) Math.max(0, 2 + Math.ceil(Math.log(1 / this.gridSize) / Math.log(10)));
            this.formatter.setMaximumFractionDigits(maximumNumberOfDigits);
            this.gridSize = this.gridSize / 10;
            gridSizePixels = (int) Math.round(this.gridSize / scale);
            if (count++ > 10)
            {
                break;
            }
        }

        // Let's draw the vertical lines
        double mod = this.extent.getMinX() % this.gridSize;
        int x = (int) -Math.round(mod / scale);
        while (x < this.getWidth())
        {
            Point2d point =
                    Renderable2DInterface.Util.getWorldCoordinates(new Point2D.Double(x, 0), this.extent, this.getSize());
            if (point != null)
            {
                String label = this.formatter.format(Math.round(point.getX() / this.gridSize) * this.gridSize);
                double labelWidth = this.getFontMetrics(this.getFont()).getStringBounds(label, g).getWidth();
                if (x > labelWidth + 4)
                {
                    g.drawLine(x, 15, x, this.getHeight());
                    g.drawString(label, (int) Math.round(x - 0.5 * labelWidth), 11);
                }
            }
            x = x + gridSizePixels;
        }

        // Let's draw the horizontal lines
        mod = Math.abs(this.extent.getMinY()) % this.gridSize;
        int y = (int) Math.round(this.getSize().getHeight() - (mod / scale));
        while (y > 15)
        {
            Point2d point =
                    Renderable2DInterface.Util.getWorldCoordinates(new Point2D.Double(0, y), this.extent, this.getSize());
            if (point != null)
            {
                String label = this.formatter.format(Math.round(point.getY() / this.gridSize) * this.gridSize);
                RectangularShape labelBounds = this.getFontMetrics(this.getFont()).getStringBounds(label, g);
                g.drawLine((int) Math.round(labelBounds.getWidth() + 4), y, this.getWidth(), y);
                g.drawString(label, 2, (int) Math.round(y + labelBounds.getHeight() * 0.3));
            }
            y = y - gridSizePixels;
        }
    }
}
