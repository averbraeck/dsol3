package nl.tudelft.simulation.dsol.tutorial.section25;

/**
 * The Order class as presented in section 2.5 in the DSOL tutorial.
 * <p>
 * copyright (c) 2002-2018 <a href="http://www.simulation.tudelft.nl"> Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.1 Sep 6, 2004 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Order
{
    /** the product of an order. */
    private String product = null;

    /** the amount of product to order. */
    private double amount = Double.NaN;

    /**
     * constructs a new Order.
     * @param product the product
     * @param amount the amount to buy
     */
    public Order(final String product, final double amount)
    {
        super();
        this.product = product;
        this.amount = amount;
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return "Order[" + this.product + ";" + this.amount + "]";
    }
}
