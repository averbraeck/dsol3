package nl.tudelft.simulation.dsol.hla;

import org.djutils.io.URLResource;
import org.djutils.logger.CategoryLogger;

import hla.rti.RTIambassador;
import se.pitch.prti.RTI;

/**
 * The specifies
 * <p>
 * copyright (c) 2004-2019 <a href="https://simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm"> Peter Jacobs </a>
 * @since 1.2
 */
public class CreateFederation
{
    public final static String DEFAULT_FED = "https://simulation.tudelft.nl/dsol-hla/fed/RPCFederation.fed";

    /**
     * constructs a new CreateFederation.
     */
    private CreateFederation()
    {
        super();
    }

    /**
     * executes a fedeation
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            String fedURL = DEFAULT_FED;
            if (args.length != 1)
            {
                System.out.println("Usage: CreateFederation <FED url>");
                System.out.println("Now using default instead: " + fedURL);
            }
            else
            {
                fedURL = args[0];
            }
            RTIambassador rtiAmbassador = RTI.getRTIambassador("localhost", 8989);
            rtiAmbassador.createFederationExecution(parseFederationName(fedURL), URLResource.getResource(fedURL));
        }
        catch (Exception exception)
        {
            CategoryLogger.always().error(exception);
        }
    }

    protected static String parseFederationName(String fedURL)
    {
        // Assume name is between final '/' and '.'
        String result = fedURL;
        result = result.substring(result.lastIndexOf('/') + 1);
        result = result.substring(0, result.indexOf('.'));
        System.out.println("Parsed FED name: " + result);
        return result;
    }
}
