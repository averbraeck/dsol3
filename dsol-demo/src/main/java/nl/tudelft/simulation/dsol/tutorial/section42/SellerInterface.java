package nl.tudelft.simulation.dsol.tutorial.section42;

/**
 * A SellerInterface <br>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands.
 * <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
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
