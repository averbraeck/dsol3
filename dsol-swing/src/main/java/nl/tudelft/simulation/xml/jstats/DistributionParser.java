
package nl.tudelft.simulation.xml.jstats;

import java.io.IOException;
import java.net.URL;

import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.jstats.distributions.Dist;
import nl.tudelft.simulation.jstats.distributions.DistBernoulli;
import nl.tudelft.simulation.jstats.distributions.DistBeta;
import nl.tudelft.simulation.jstats.distributions.DistBinomial;
import nl.tudelft.simulation.jstats.distributions.DistConstant;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.jstats.distributions.DistDiscrete;
import nl.tudelft.simulation.jstats.distributions.DistExponential;
import nl.tudelft.simulation.jstats.distributions.DistNormal;
import nl.tudelft.simulation.jstats.distributions.DistTriangular;
import nl.tudelft.simulation.jstats.streams.Java2Random;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.logger.Logger;

import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * <br>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version Jun 25, 2004 <br>
 * @author <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public final class DistributionParser
{
    /** the default schema file. */
    public static final URL DISTRIBUTIONFILE_SCHEMA = DistributionParser.class.getResource("/xsd/distribution.xsd");

    /**
     * constructs a new DistributionParser.
     */
    private DistributionParser()
    {
        super();
        // unreachable code
    }

    /**
     * parses a xml-element representing a distribution function
     * @param element The j-dom element
     * @param simulator the simulator to use
     * @return the distribution function
     * @throws IOException on failure
     */
    public static Dist parseDistribution(final Element element, final SimulatorInterface simulator) throws IOException
    {
        try
        {
            String streamId = element.getAttributeValue("stream");
            StreamInterface stream = new Java2Random();
            try
            {
                stream = simulator.getReplication().getStream(streamId);
            }
            catch (Exception e)
            {
                Logger.warning(DistributionParser.class, "parseDistribution", "stream " + stream + " not found"
                        + "\nDefault stream used instead.");
            }
            Namespace xsi = Namespace.getNamespace("http://www.w3.org/2001/XMLSchema-instance");
            String distributionType = element.getAttributeValue("type", xsi);
            if (distributionType.startsWith("dsol:"))
            {
                distributionType = distributionType.substring(5);
            }
            if ("bernouilli".equals(distributionType))
            {
                double p = new Double(element.getAttributeValue("p")).doubleValue();
                return new DistBernoulli(stream, p);
            }
            else if ("beta".equals(distributionType))
            {
                double alpha1 = new Double(element.getAttributeValue("alpha1")).doubleValue();
                double alpha2 = new Double(element.getAttributeValue("alpha2")).doubleValue();
                return new DistBeta(stream, alpha1, alpha2);
            }
            else if ("binomial".equals(distributionType))
            {
                long n = new Long(element.getAttributeValue("n")).longValue();
                double p = new Double(element.getAttributeValue("p")).doubleValue();
                return new DistBinomial(stream, n, p);
            }
            else if ("constant".equals(distributionType))
            {
                double c = new Double(element.getAttributeValue("c")).doubleValue();
                return new DistConstant(stream, c);
            }
            else if ("exponential".equals(distributionType))
            {
                double lambda = new Double(element.getAttributeValue("lambda")).doubleValue();
                return new DistExponential(stream, lambda);
            }
            else if ("normal".equals(distributionType))
            {
                double mean = new Double(element.getAttributeValue("mean")).doubleValue();
                double stdev = new Double(element.getAttributeValue("stdev")).doubleValue();
                return new DistNormal(stream, mean, stdev);
            }
            else if ("triangular".equals(distributionType))
            {
                double a = new Double(element.getAttributeValue("a")).doubleValue();
                double b = new Double(element.getAttributeValue("b")).doubleValue();
                double c = new Double(element.getAttributeValue("c")).doubleValue();
                return new DistTriangular(stream, a, b, c);
            }
            else
            {
                throw new Exception("Unknown distribution in " + element.getName());
            }
        }
        catch (Exception exception)
        {
            throw new IOException(exception.getMessage());
        }
    }

    /**
     * parses a xml-element representing a distribution function
     * @param element The j-dom element
     * @param simulator the simulator to use
     * @return the distribution function
     * @throws IOException on failure
     */
    public static DistDiscrete parseDiscreteDistribution(final Element element, final SimulatorInterface simulator)
            throws IOException
    {
        return (DistDiscrete) DistributionParser.parseDistribution(element, simulator);
    }

    /**
     * parses a xml-element representing a distribution function
     * @param element The j-dom element
     * @param simulator the simulator to use
     * @return the distribution function
     * @throws IOException on failure
     */
    public static DistContinuous parseContinuousDistribution(final Element element, final SimulatorInterface simulator)
            throws IOException
    {
        return (DistContinuous) DistributionParser.parseDistribution(element, simulator);
    }
}
