package nl.tudelft.simulation.jstats.charts.boxAndWhisker;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;

import nl.tudelft.simulation.jstats.Swingable;
import nl.tudelft.simulation.jstats.statistics.Tally;

/**
 * The summaryuChart specifies a summaryChart. <br>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:39:07 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="mailto:a.verbraeck@tudelft.nl"> Alexander Verbraeck </a>
 */
public class BoxAndWhiskerChart implements Swingable
{
    /** TITLE_FONT refers to the font to be used for the title of the plot. */
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 18);

    /** chart refers to the actual chart. */
    private JFreeChart chart = null;

    /**
     * constructs a new BoxAndWhiskerChart.
     * @param title the title of the chart
     */
    public BoxAndWhiskerChart(final String title)
    {
        Plot plot = new BoxAndWhiskerPlot();
        this.chart = new JFreeChart(title, TITLE_FONT, plot, true);
        this.chart.setBackgroundPaint(new GradientPaint(0.0F, 0.0F, Color.white, 1000F, 0.0F, Color.blue));
    }

    /**
     * adds a tally to the chart
     * @param tally the tally to be added
     */
    public void add(final Tally tally)
    {
        ((BoxAndWhiskerPlot) this.chart.getPlot()).add(tally);
    }

    /**
     * returns the chart
     * @return JFreeChart
     */
    public JFreeChart getChart()
    {
        return this.chart;
    }

    /**
     * returns the swing panel of this chart
     * @return Container the swing panel
     */
    public Container getSwingPanel()
    {
        ChartPanel result = new ChartPanel(this.chart);
        result.setMouseZoomable(true, false);
        result.setPreferredSize(new Dimension(800, 600));
        return result;
    }

    /**
     * Returns the confidence interval of the BoxAndWhiskerPlot
     * @return the confidence interval of the BoxAndWhiskerPlot
     */
    public double getConfidenceInterval()
    {
        return ((BoxAndWhiskerPlot) this.chart.getPlot()).getConfidenceInterval();
    }

    /**
     * sets the confidence interval of the plot. The default value = 0.05 (=5%)
     * @param confidenceInterval the confidence interval
     */
    public void setConfidenceInterval(final double confidenceInterval)
    {
        ((BoxAndWhiskerPlot) this.chart.getPlot()).setConfidenceInterval(confidenceInterval);
    }
}
