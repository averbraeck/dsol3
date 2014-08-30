package nl.tudelft.simulation.xml.dsol;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.ModelInterface;
import nl.tudelft.simulation.dsol.experiment.Experiment;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.Treatment;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.TimeUnit;
import nl.tudelft.simulation.dsol.simtime.UnitTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSAnimator;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.language.io.URLResource;
import nl.tudelft.simulation.language.reflection.ClassUtil;
import nl.tudelft.simulation.logger.Logger;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * The ExperimentParser parses xml-based experiments into their java objects <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class ExperimentParser
{
    /** builder the xerces parser with validation turned on. */
    private static SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser", true);
    static
    {
        // turns on Schema Validation with Xerces
        builder.setFeature("http://xml.org/sax/features/validation", true);
        builder.setFeature("http://apache.org/xml/features/validation/schema", true);
        // Let's find the XSD file
        String xsd = URLResource.getResource("/xsd/experiment.xsd").toExternalForm();
        builder.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation",
                "http://www.simulation.tudelft.nl " + xsd);
    }

    /**
     * constructs a new ExperimentParser This is a Utility Class.
     */
    protected ExperimentParser()
    {
        // A utility class should not be instantiated
    }

    /**
     * parses an experimentalFrame xml-file.
     * @param input the inputstream
     * @return ExperimentalFrame the experimentalFrame
     * @throws IOException whenever parsing fails
     */
    public static ExperimentalFrame parseExperimentalFrame(final URL input) throws IOException
    {
        try
        {
            return ExperimentParser.parseExperimentalFrame(new InitialContext(), input);
        }
        catch (NamingException exception)
        {
            throw new IOException(exception.getMessage());
        }
    }

    /**
     * parses an experimentalFrame xml-file.
     * @param input the url of the xmlfile
     * @param context the root context for the experimentalFrame
     * @return ExperimentalFrame the experimentalFrame
     * @throws IOException whenever parsing fails
     */
    public static ExperimentalFrame parseExperimentalFrame(final Context context, final URL input) throws IOException
    {
        if (input == null)
        {
            throw new IOException("experiment URL=null");
        }
        try
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            String name = DateFormat.getDateTimeInstance().format(calendar.getTime());
            Context root = context.createSubcontext(name);

            Element rootElement = builder.build(input).getRootElement();
            List<Experiment> experiments = new ArrayList<Experiment>();
            @SuppressWarnings("unchecked")
            List<Element> experimentElements = rootElement.getChildren("experiment");
            int number = 0;

            for (Element element : experimentElements)
            {
                Context experimentContext = root.createSubcontext("experiment[" + number + "]");
                Experiment experiment = ExperimentParser.parseExperiment(experimentContext, element, input);
                experiment.setDescription("experiment " + number);
                experiments.add(experiment);
                number++;
            }
            ExperimentalFrame frame = new ExperimentalFrame(root, input);
            frame.setExperiments(experiments);
            return frame;
        }
        catch (Exception exception)
        {
            Logger.warning(ExperimentParser.class, "parseExperimentalFrame", exception);
            return null;
        }
    }

    /**
     * parses an experiment xml-file.
     * @param context the context for this experiment
     * @param url the url of the experimentfile
     * @param rootElement the element representing the experiment
     * @return ExperimentalFrame the experiment
     * @throws IOException whenever parsing fails
     */
    public static Experiment parseExperiment(final Context context, final Element rootElement, final URL url)
            throws IOException
    {
        try
        {
            // resolve the MODEL element
            ClassLoader loader = ExperimentParser.resolveClassLoader(url);
            Experiment experiment = new Experiment(context);
            Element modelElement = rootElement.getChild("model");
            String modelClassName = ExperimentParser.cleanName(modelElement.getChildText("model-class"));

            if (modelElement.getChild("class-path") != null)
            {
                @SuppressWarnings("unchecked")
                List<Element> jarFiles = modelElement.getChild("class-path").getChildren("jar-file");
                URL[] urls = new URL[jarFiles.size()];
                int nr = 0;
                for (Iterator<Element> i = jarFiles.iterator(); i.hasNext();)
                {
                    Element child = i.next();
                    urls[nr] = URLResource.getResource(child.getValue());
                    nr++;
                }
                loader = new URLClassLoader(urls, loader);
            }
            Thread.currentThread().setContextClassLoader(loader);

            // XXX: START TO REMOVE
            ArrayList<URL> urls = new ArrayList<URL>();
            for (int i = 0; i < ((URLClassLoader) loader).getURLs().length; i++)
            {
                urls.add(((URLClassLoader) loader).getURLs()[i]);
            }
            System.err.println("classloader URLs: " + urls);
            URLClassLoader urlLoader = (URLClassLoader) loader;
            System.err.println("model: " + modelClassName);
            // XXX: END TO REMOVE

            Class<?> modelClass = Class.forName(modelClassName, true, loader);
            ModelInterface model = (ModelInterface) ClassUtil.resolveConstructor(modelClass, null).newInstance();
            experiment.setModel(model);

            // resolve the ANALYST element
            String analyst = "";
            if (rootElement.getChild("analyst") != null)
            {
                analyst = rootElement.getChild("analyst").getText();
            }
            experiment.setAnalyst(analyst);

            // resolve the name element
            String name = "";
            if (rootElement.getChild("name") != null)
            {
                name = rootElement.getChild("name").getText();
                experiment.setDescription(name);
            }
            // no "else" because experiment already has a default description
            // provided in parseExperimentalFrame

            // resolve the TREATMENT element
            Treatment treatment = ExperimentParser.parseTreatment(rootElement.getChild("treatment"), experiment);
            experiment.setTreatment(treatment);

            // resolve the REPLICATIONS element
            Element replicationsElement = rootElement.getChild("replications");
            @SuppressWarnings("unchecked")
            List<Element> replicationElements = replicationsElement.getChildren("replication");
            List<Replication> replicationArray = new ArrayList<Replication>();
            int number = 1;
            for (Iterator<Element> i = replicationElements.iterator(); i.hasNext();)
            {
                replicationArray.add(ExperimentParser.parseReplication(i.next(), experiment, context));
                number++;
            }
            experiment.setReplications(replicationArray);

            // resolve the SIMULATOR-CLASS element
            SimulatorInterface simulator = null;
            if (modelElement.getChild("simulator-class") == null)
            {
                simulator = new DEVSAnimator();
            }
            else
            {
                Class<?> simulatorClass = Class.forName(rootElement.getChildText("simulator-class"), true, loader);
                simulator = (SimulatorInterface) ClassUtil.resolveConstructor(simulatorClass, null).newInstance();
            }
            experiment.setSimulator(simulator);
            return experiment;
        }
        catch (Exception exception)
        {
            Logger.warning(ExperimentParser.class, "parseExperiment", exception);
            throw new IOException(exception.getMessage());
        }
    }

    /** characters that can be used in a classpath. */
    private static String legalChars = ".@$_-abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * clean the name of a class
     */
    private static String cleanName(final String string)
    {
        String out = "";
        for (int i = 0; i < string.length(); i++)
        {
            if (legalChars.indexOf(string.charAt(i)) >= 0)
                out += string.charAt(i);
        }
        return out;
    }

    // *********************** PRIVATE PARSING FUNCTIONS *******************//
    /**
     * parses an experiment xml-file.
     * @param input the inputstream
     * @return Classloader the classLoader
     */
    private static ClassLoader resolveClassLoader(final URL input)
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (input.getProtocol().equals("file"))
        {
            try
            {
                List<URL> urls = new ArrayList<URL>();
                String path = input.getPath().substring(0, input.getPath().lastIndexOf('/')) + "/";

                // We add the path
                urls.add(new URL("file:" + path));

                // If the classpath ends with src, we also add the bin
                if (path.endsWith("src/"))
                {
                    path = path.substring(0, path.length() - 4) + "bin/";
                    urls.add(new URL("file:" + path));
                }
                else if (path.endsWith("src/conf/"))
                // If the classpath ends with src/conf, we add target/classes
                {
                    path = path.substring(0, path.length() - 9) + "target/classes/";
                    urls.add(new URL("file:" + path));
                }
                else if (path.endsWith("target/classes/"))
                {
                    // If the classpath ends with target/classes, leave as is
                }
                else
                {
                    // We assume there might be a src & bin dir, and target, and target/classes
                    path = path.substring(0, path.length()) + "bin/";
                    urls.add(new URL("file:" + path));
                    path = path.substring(0, path.length()) + "target/";
                    urls.add(new URL("file:" + path));
                    path = path.substring(0, path.length()) + "target/classes/";
                    urls.add(new URL("file:" + path));
                }
                // XXX: START TO REMOVE
                System.err.println(urls);
                // XXX: END TO REMOVE
                URL[] urlArray = urls.toArray(new URL[urls.size()]);
                URLClassLoader urlClassLoader = new URLClassLoader(urlArray, loader);
                return urlClassLoader;
            }
            catch (MalformedURLException exception)
            {
                return loader;
            }
        }
        return loader;
    }

    /**
     * parses the dateTime
     * @param value the string value in the yyyy-mm-ddThh:mm:ss format
     * @return long the amount of milliseconds since 1970.
     */
    private static long parseDateTime(final String value)
    {
        Calendar calendar = Calendar.getInstance();
        String concatDate = value.split("T")[0];
        String concatTime = value.split("T")[1];
        String[] date = concatDate.split("-");
        String[] time = concatTime.split(":");
        calendar.set(new Integer(date[0]).intValue(), new Integer(date[1]).intValue() - 1,
                new Integer(date[2]).intValue(), new Integer(time[0]).intValue(), new Integer(time[1]).intValue(),
                new Integer(time[2]).intValue());
        return calendar.getTimeInMillis();
    }

    /**
     * parses a period
     * @param element the xml-element representing the period
     * @param treatmentTimeUnit the timeUnit of the treatment
     * @return double the value in units defined by the treatment
     * @throws Exception whenever the period.
     */
    private static double parsePeriod(final Element element, final TimeUnit treatmentTimeUnit) throws Exception
    {
        TimeUnit timeUnit = ExperimentParser.parseTimeUnit(element.getAttribute("unit").getValue());
        double value = -1;
        if (element.getText().equals("INF"))
        {
            value = Double.MAX_VALUE;
        }
        else
        {
            value = new Double(element.getText()).doubleValue();
        }
        if (value < 0)
        {
            throw new JDOMException("parsePeriod: value = " + value + " <0. simulator cannot schedule in past");
        }
        return timeUnit.getFactor() * value / treatmentTimeUnit.getFactor();
    }

    /**
     * parses a replication
     * @param element the JDOM element
     * @param parent the experiment
     * @param number the number
     * @return the replication
     * @throws Exception on failure
     */
    private static Replication parseReplication(final Element element, final Experiment parent, final Context context)
            throws Exception
    {
        Replication replication = new Replication(context, parent);
        if (element.getAttribute("description") != null)
        {
            replication.setDescription(element.getAttribute("description").getValue());
        }
        Map<String, StreamInterface> streams = new HashMap<String, StreamInterface>();
        @SuppressWarnings("unchecked")
        List<Element> streamElements = element.getChildren("stream");
        for (Element streamElement : streamElements)
        {
            long seed = new Long(streamElement.getAttributeValue("seed")).longValue();
            StreamInterface stream = new MersenneTwister(seed);
            streams.put(streamElement.getAttributeValue("name"), stream);
        }
        replication.setStreams(streams);
        return replication;
    }

    /**
     * parses proprties to treatments
     * @param element the element
     * @return Properties
     */
    private static Properties parseProperties(final Element element)
    {
        Properties result = new Properties();
        @SuppressWarnings("unchecked")
        List<Element> children = element.getChildren("property");
        for (Iterator<Element> i = children.iterator(); i.hasNext();)
        {
            Element child = i.next();
            String key = child.getAttributeValue("key");
            String value = child.getAttributeValue("value");
            result.put(key, value);
        }
        return result;
    }

    /**
     * parses a timeUnit
     * @param name the name
     * @return TimeUnitInterface result
     * @throws Exception on failure
     */
    private static TimeUnit parseTimeUnit(final String name) throws Exception
    {
        if (name.equals("DAY"))
        {
            return TimeUnit.DAY;
        }
        if (name.equals("HOUR"))
        {
            return TimeUnit.HOUR;
        }
        if (name.equals("MILLISECOND"))
        {
            return TimeUnit.MILLISECOND;
        }
        if (name.equals("MINUTE"))
        {
            return TimeUnit.MINUTE;
        }
        if (name.equals("SECOND"))
        {
            return TimeUnit.SECOND;
        }
        if (name.equals("WEEK"))
        {
            return TimeUnit.WEEK;
        }
        if (name.equals("UNIT"))
        {
            return TimeUnit.UNIT;
        }
        throw new Exception("parseTimeUnit.. unknown argument: " + name);
    }

    /**
     * parses the treatment
     * @param element the xml-element
     * @param parent parent
     * @param number the number
     * @return Treatment
     * @throws Exception on failure
     */
    private static Treatment parseTreatment(final Element element, final Experiment parent) throws Exception
    {
        TimeUnit tu = ExperimentParser.parseTimeUnit(element.getChildText("timeUnit"));
        long startTime = 0L;
        if (element.getChild("startTime") != null)
        {
            startTime = ExperimentParser.parseDateTime(element.getChildText("startTime"));
        }
        double warmupPeriod = 0.0;
        if (element.getChild("warmupPeriod") != null)
        {
            warmupPeriod = ExperimentParser.parsePeriod(element.getChild("warmupPeriod"), tu);
        }
        double runLength = 0.0;
        if (element.getChild("runLength") != null)
        {
            runLength = ExperimentParser.parsePeriod(element.getChild("runLength"), tu);
        }
        Treatment treatment =
                new Treatment(parent, "tr", new SimTimeDoubleUnit(new UnitTimeDouble(1.0 * startTime, tu)),
                        warmupPeriod, runLength);

        if (element.getChild("properties") != null)
        {
            treatment.setProperties(ExperimentParser.parseProperties(element.getChild("properties")));
        }

        return treatment;
    }
}
