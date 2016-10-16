package nl.tudelft.simulation.xml;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.language.io.URLResource;
import nl.tudelft.simulation.xml.dsol.ExperimentParser;

/**
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Sep 28, 2004
 * @since 1.5
 */
public class ExperimentParserTest extends TestCase
{
    /** the logger. */
    private static Logger logger = LogManager.getLogger(ExperimentParserTest.class);

    /**
     * constructs a new ExperimentParserT.
     */
    public ExperimentParserTest()
    {
        this("test");
    }

    /**
     * constructs a new ExperimentParserT.
     * @param arg0
     */
    public ExperimentParserTest(String arg0)
    {
        super(arg0);
    }

    /**
     * tests the experiment test
     */
    public void test()
    {
        URL url = URLResource.getResource("/resources/DummyExperiment.xml");
        Assert.assertFalse(url == null);
        try
        {
            ExperimentalFrame experimentalFrame = ExperimentParser.parseExperimentalFrame(url);
            System.out.println(experimentalFrame);
        }
        catch (Exception exception)
        {
            logger.warn("test", exception);
            Assert.fail(exception.getMessage());
        }
    }
}
