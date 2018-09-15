package nl.tudelft.simulation.jstats.charts.xy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.AbstractSeriesDataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;

import nl.tudelft.simulation.language.filters.FilterInterface;

/**
 * The xyDataset specifies the xyDataset in DSOL
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2014/05/05 00:26:06 $
 * @since 1.5
 */
public class XYDataset extends AbstractSeriesDataset implements org.jfree.data.xy.XYDataset, DatasetChangeListener
{

    /** default UId. */
    private static final long serialVersionUID = 1L;

    /** series contains the series of the set. */
    private XYSeries[] series = new XYSeries[0];

    /**
     * constructs a new XYDataset.
     */
    public XYDataset()
    {
        super();
        this.fireDatasetChanged();
    }

    /** {@inheritDoc} */
    @Override
    public void datasetChanged(final DatasetChangeEvent arg0)
    {
        if (arg0 != null)
        {
            this.fireDatasetChanged();
        }
    }

    /**
     * adds a dataset to the series
     * @param dataset the set
     */
    public synchronized void addSeries(final XYSeries dataset)
    {
        dataset.addChangeListener(this);
        List<XYSeries> list = new ArrayList<XYSeries>(Arrays.asList(this.series));
        list.add(dataset);
        this.series = list.toArray(new XYSeries[list.size()]);
        this.fireDatasetChanged();
    }

    /** {@inheritDoc} */
    @Override
    public DomainOrder getDomainOrder()
    {
        return DomainOrder.ASCENDING;
    }

    /** {@inheritDoc} */
    @Override
    public int getItemCount(final int serie)
    {
        return this.series[serie].getItemCount();
    }

    /** {@inheritDoc} */
    @Override
    public double getXValue(final int serie, final int item)
    {
        return this.series[serie].getXValue(item);
    }

    /** {@inheritDoc} */
    @Override
    public double getYValue(final int serie, final int item)
    {
        return this.series[serie].getYValue(item);
    }

    /** {@inheritDoc} */
    @Override
    public int getSeriesCount()
    {
        return this.series.length;
    }

    /**
     * @param serie the series to retrieve the name for
     * @return name the name of the series
     */
    public String getSeriesName(final int serie)
    {
        return this.series[serie].getSeriesName();
    }

    /** {@inheritDoc} */
    @Override
    public Number getX(final int serie, final int item)
    {
        return new Double(this.series[serie].getXValue(item));
    }

    /** {@inheritDoc} */
    @Override
    public Number getY(final int serie, final int item)
    {
        return new Double(this.series[serie].getYValue(item));
    }

    /**
     * applies a filter on the chart
     * @param filter the filter to apply
     */
    public void setFilter(final FilterInterface filter)
    {
        for (int i = 0; i < this.series.length; i++)
        {
            this.series[i].setFilter(filter);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Comparable<?> getSeriesKey(final int seriesNumber)
    {
        return this.series[seriesNumber].getSeriesName();
    }

}
