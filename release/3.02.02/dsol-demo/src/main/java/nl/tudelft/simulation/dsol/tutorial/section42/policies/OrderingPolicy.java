package nl.tudelft.simulation.dsol.tutorial.section42.policies;

/**
 * A OrderingPolicy <br>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Dec 8, 2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public interface OrderingPolicy
{
    /**
     * decides the amount of product to order.
     * @param inventory the current inventory
     * @return long the amount
     */
    long computeAmountToOrder(final long inventory);
}
