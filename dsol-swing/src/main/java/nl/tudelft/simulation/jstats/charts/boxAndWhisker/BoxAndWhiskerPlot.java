package nl.tudelft.simulation.jstats.charts.boxAndWhisker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;

import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.jstats.statistics.Tally;

/**
 * The Summary chart class defines a summary chart..
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="mailto:a.verbraeck@tudelft.nl"> Alexander Verbraeck </a>
 */
public class BoxAndWhiskerPlot extends Plot implements EventListenerInterface
{
    /** serialversionUId. */
    private static final long serialVersionUID = 1L;

    /** BORDER_SIZE refers to the width of the border on the panel. */
    public static final short BORDER_SIZE = 50;

    /** PLOT_TYPE refers to the plot type. */
    public static final String PLOT_TYPE = "SUMMARY_PLOT";

    /** FONT defines the font of the plot. */
    public static final Font FONT = new Font("SansSerif", Font.PLAIN, 10);

    /** TITLE_FONT defines the font of the plot. */
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 15);

    /** target is the tally to represent. */
    protected Tally[] tallies = new Tally[0];

    /** formatter formats the text. */
    protected NumberFormat formatter = NumberFormat.getInstance();

    /** the confidenceInterval. */
    protected double confidenceInterval = 0.05;

    /**
     * constructs a new BoxAndWhiskerPlot.
     */
    public BoxAndWhiskerPlot()
    {
        super();
    }

    /**
     * adds a tally to the array of targets;
     * @param tally Tally; the tally to be summarized
     */
    public synchronized void add(final Tally tally)
    {
        tally.addListener(this, Tally.SAMPLE_MEAN_EVENT, false);
        List<Tally> list = new ArrayList<Tally>(Arrays.asList(this.tallies));
        list.add(tally);
        this.tallies = list.toArray(new Tally[list.size()]);
    }

    /** {@inheritDoc} */
    @Override
    public String getPlotType()
    {
        return PLOT_TYPE;
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        this.notifyListeners(new PlotChangeEvent(this));
    }

    /** ************ PRIVATE METHODS *********************** */
    /**
     * computes the extent of the targets
     * @param tallies Tally[]; the range of tallies
     * @return double[min,max]
     */
    private static double[] extent(final Tally[] tallies)
    {
        double[] result = {Double.MAX_VALUE, -Double.MAX_VALUE};
        for (int i = 0; i < tallies.length; i++)
        {
            if (tallies[i].getMin() < result[0])
            {
                result[0] = tallies[i].getMin();
            }
            if (tallies[i].getMax() > result[1])
            {
                result[1] = tallies[i].getMax();
            }
        }
        return result;
    }

    /**
     * determines the borders on the left and right side of the tally
     * @param g2 Graphics2D; the graphics object
     * @param context FontRenderContext; the context
     * @param tallyArray Tally[]; tallies
     * @return double[] the extent
     */
    private double[] borders(final Graphics2D g2, final FontRenderContext context, final Tally[] tallyArray)
    {
        double[] result = {0, 0};
        for (int i = 0; i < tallyArray.length; i++)
        {
            double left =
                    g2.getFont().getStringBounds(this.formatter.format(tallyArray[i].getMin()), context).getWidth();
            double rigth =
                    g2.getFont().getStringBounds(this.formatter.format(tallyArray[i].getMax()), context).getWidth();
            if (left > result[0])
            {
                result[0] = left;
            }
            if (rigth > result[1])
            {
                result[1] = rigth;
            }
        }
        result[0] = result[0] + 3;
        result[1] = result[1] + 3;
        return result;
    }

    /**
     * returns the bounding box
     * @param word String; the word
     * @param context FontRenderContext; the context
     * @return Rectangle2D the bounds
     */
    private Rectangle2D getBounds(final String word, final FontRenderContext context)
    {
        return FONT.getStringBounds(word, context);
    }

    /**
     * fills a rectangle
     * @param g2 Graphics2D; the graphics object
     * @param rectangle Rectangle2D; the area
     * @param color Color; the color
     */
    private void fillRectangle(final Graphics2D g2, final Rectangle2D rectangle, final Color color)
    {
        g2.setColor(color);
        g2.fillRect((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.getWidth(),
                (int) rectangle.getHeight());
    }

    /**
     * paints a tally
     * @param g2 Graphics2D; the graphics object
     * @param rectangle Rectangle2D; the rectangle on which to paint
     * @param tally Tally; the tally
     * @param leftX double; the lowest real value
     * @param leftBorder double; the left border
     * @param scale double; the scale
     */
    private void paintTally(final Graphics2D g2, final Rectangle2D rectangle, final Tally tally, final double leftX,
            final double leftBorder, final double scale)
    {
        this.fillRectangle(g2, rectangle, Color.WHITE);
        g2.setColor(Color.BLACK);
        g2.setFont(TITLE_FONT);
        g2.drawString(tally.getDescription(),
                (int) Math.round(leftBorder + 0.5 * (rectangle.getWidth() - leftBorder - 20)
                        - 0.5 * this.getBounds(tally.getDescription(), g2.getFontRenderContext()).getWidth()),
                25 + (int) rectangle.getY());
        g2.setFont(FONT);
        g2.drawRect((int) rectangle.getX() - 1, (int) rectangle.getY() - 1, (int) rectangle.getWidth() + 2,
                (int) rectangle.getHeight() + 2);
        int tallyMin = (int) Math.round(rectangle.getX() + (tally.getMin() - leftX) * scale + leftBorder);
        int tallyMax = (int) Math.round(rectangle.getX() + (tally.getMax() - leftX) * scale + leftBorder);
        int middle = (int) Math.round(rectangle.getY() + 0.5 * rectangle.getHeight());
        String label = this.formatter.format(tally.getMin());
        g2.drawString(label,
                (int) Math.round(tallyMin - 3 - this.getBounds(label, g2.getFontRenderContext()).getWidth()),
                (int) Math.round(middle + 0.5 * this.getBounds(label, g2.getFontRenderContext()).getHeight()));
        label = this.formatter.format(tally.getMax());
        g2.drawString(label, tallyMax + 3,
                (int) Math.round(middle + 0.5 * this.getBounds(label, g2.getFontRenderContext()).getHeight()));
        g2.drawLine(tallyMin, middle + 6, tallyMin, middle - 6);
        g2.drawLine(tallyMin, middle, tallyMax, middle);
        g2.drawLine(tallyMax, middle + 6, tallyMax, middle - 6);
        double[] confidence = tally.getConfidenceInterval(this.confidenceInterval);
        int middleX = (int) Math.round((tally.getSampleMean() - leftX) * scale + tallyMin);
        g2.fillRect(middleX, middle - 6, 2, 12);
        label = this.formatter.format(tally.getSampleMean());
        Rectangle2D bounds = this.getBounds(label, g2.getFontRenderContext());
        g2.drawString(label, (int) Math.round(middleX - 0.5 * bounds.getWidth()), Math.round(middle - 8));
        if (confidence != null)
        {
            int confX = (int) Math.round((confidence[0] - leftX) * scale + tallyMin);
            int confWidth = (int) Math.round((confidence[1] - confidence[0]) * scale);
            g2.fillRect(confX, middle - 2, confWidth, 4);
            label = this.formatter.format(confidence[0]);
            bounds = this.getBounds(label, g2.getFontRenderContext());
            g2.drawString(label, (int) Math.round(confX - bounds.getWidth()),
                    (int) Math.round(middle + 8 + bounds.getHeight()));
            label = this.formatter.format(confidence[1]);
            bounds = this.getBounds(label, g2.getFontRenderContext());
            g2.drawString(label, Math.round(confX + confWidth), (int) Math.round(middle + 8 + bounds.getHeight()));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void draw(final Graphics2D g2, final Rectangle2D rectangle, final Point2D point, final PlotState plotState,
            final PlotRenderingInfo plotRenderingInfo)
    {
        // TODO Point2D point not in use yet -- look up meaning
        g2.setBackground(Color.WHITE);
        double height = Math.min(rectangle.getHeight() / this.tallies.length * 1.0, rectangle.getHeight());
        double[] extent = BoxAndWhiskerPlot.extent(this.tallies);
        double[] border = this.borders(g2, g2.getFontRenderContext(), this.tallies);
        double scale = (0.85 * rectangle.getWidth() - 10 - border[0] - border[1]) / ((extent[1] - extent[0]) * 1.0);
        for (int i = 0; i < this.tallies.length; i++)
        {
            g2.setFont(FONT);
            Rectangle2D area = new Rectangle2D.Double(rectangle.getX() + 0.15 * rectangle.getWidth(),
                    rectangle.getY() + i * height + 3, 0.85 * rectangle.getWidth() - 10, 0.75 * height - 3);
            this.paintTally(g2, area, this.tallies[i], extent[0], border[0], scale);
        }
    }

    /**
     * Returns the confidence interval of the BoxAndWhiskerPlot
     * @return the confidence interval of the BoxAndWhiskerPlot
     */
    public double getConfidenceInterval()
    {
        return this.confidenceInterval;
    }

    /**
     * sets the confidence interval of the plot. The default value = 0.05 (=5%)
     * @param confidenceInterval double; the confidence interval
     */
    public void setConfidenceInterval(final double confidenceInterval)
    {
        this.confidenceInterval = confidenceInterval;
    }

}
