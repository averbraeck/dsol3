/*
 * @(#) CreateFederation.java Feb 11, 2005 Copyright (c) 2004 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology The code is published under the General Public License
 */
package nl.tudelft.simulation.dsol.hla;

import hla.rti.RTIambassador;
import nl.tudelft.simulation.language.io.URLResource;
import se.pitch.prti.RTI;

/**
 * The specifies
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl/dsol/">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/">
 * www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public
 * License (GPL) </a>, no warranty <br>
 * 
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm"> Peter
 *         Jacobs </a>
 * @version $Revision: 1.6 $ $Date: 2005/03/24 18:04:46 $
 * @since 1.2
 */
public class CreateFederation
{
    public final static String DEFAULT_FED = "http://www.simulation.tudelft.nl/dsol-hla/fed/RPCFederation.fed";

    /**
     * constructs a new CreateFederation
     */
    private CreateFederation()
    {
        super();
    }

    /**
     * executes a fedeation
     * 
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
            } else
            {
                fedURL = args[0];
            }
            RTIambassador rtiAmbassador = RTI.getRTIambassador(
                    "localhost", 8989);
            rtiAmbassador.createFederationExecution(
                    parseFederationName(fedURL), URLResource
                            .getResource(fedURL));
        } catch (Exception exception)
        {
            exception.printStackTrace();
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