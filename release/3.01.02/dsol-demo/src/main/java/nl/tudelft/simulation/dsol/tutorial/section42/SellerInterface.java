package nl.tudelft.simulation.dsol.tutorial.section42;

/**
 * A SellerInterface <br>
 * (c) copyright 2002-2016 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Dec 8, 2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public interface SellerInterface
{
    /**
     * order a requested amount.
     * @param buyer the requesting buyer
     * @param amount the requested amount
     */
    void order(final BuyerInterface buyer, final long amount);
}
