package nl.tudelft.simulation.dsol.model.inputparameters;

import java.util.SortedMap;
import java.util.TreeMap;

import org.djutils.exceptions.Throw;
import org.djutils.logger.CategoryLogger;

import nl.tudelft.simulation.jstats.distributions.DistBeta;
import nl.tudelft.simulation.jstats.distributions.DistConstant;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.jstats.distributions.DistExponential;
import nl.tudelft.simulation.jstats.distributions.DistNormal;
import nl.tudelft.simulation.jstats.distributions.DistTriangular;
import nl.tudelft.simulation.jstats.distributions.DistUniform;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * InputParameterDistContinuousSelection takes care of exposing the necessary parameters for each of the continuous distribution
 * functions. It has a function called getDist() which returns the distribution of the current choice. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class InputParameterDistContinuousSelection extends InputParameterSelectionMap<String, InputParameterMapDistContinuous>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the options for the distribution functions. */
    private static SortedMap<String, InputParameterMapDistContinuous> distOptions;

    /** the random number stream to use for the distribution. */
    private StreamInterface stream;

    /** Make the map with distribution function options. */
    static
    {
        distOptions = new TreeMap<>();
        try
        {
            distOptions.put("Beta", new Beta());
            distOptions.put("Constant", new Constant());
            distOptions.put("Exponential", new Exponential());
            distOptions.put("Normal", new Normal());
            distOptions.put("Triangular", new Triangular());
            distOptions.put("Uniform", new Uniform());
        }
        catch (InputParameterException exception)
        {
            CategoryLogger.always().error(exception);
        }
    }

    /**
     * @param key String; unique name for the selection parameter of the distribution function
     * @param shortName String; concise description of the input parameter
     * @param description String; long description of the input parameter (may use HTML markup)
     * @param stream the random number stream to use for the distribution
     * @param displayPriority double; sorting order when properties are displayed to the user
     * @throws InputParameterException in case the default value is not part of the list; should not happen
     */
    public InputParameterDistContinuousSelection(final String key, final String shortName, final String description,
            final StreamInterface stream, final double displayPriority) throws InputParameterException
    {
        super(key, shortName, description, distOptions, distOptions.get("Exponential"), displayPriority);
        this.stream = stream;
        for (InputParameterMapDistContinuous dist : getOptions().values())
        {
            dist.setStream(stream);
        }
    }

    /**
     * Return the distribution function corresponding to the chosen distribution and parameters.
     * @return DistContinuous; the distribution function corresponding to the chosen distribution and parameters
     * @throws InputParameterException on error retrieving the values for the distribution
     */
    public DistContinuous getDist() throws InputParameterException
    {
        return getValue().getDist();
    }

    /**
     * @return stream
     */
    public final StreamInterface getStream()
    {
        return this.stream;
    }

    /***********************************************************************************************************/
    /*************************************** DISTRIBUTION FUNCTIONS ********************************************/
    /***********************************************************************************************************/

    /** InputParameterDistContinuous.Exponential class. */
    public static class Exponential extends InputParameterMapDistContinuous
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * Construct the input for the Exponential distribution.
         * @throws InputParameterException on error with the distribution
         */
        public Exponential() throws InputParameterException
        {
            super("Exponential", "Exponential", "Negative Exponential distribution", 1.0);
            add(new InputParameterDouble("lambda", "lambda", "lambda value, mean of the exponential distribution", 1.0, 0.0,
                    Double.MAX_VALUE, false, false, "%f", 1.0));
        }

        /** {@inheritDoc} */
        @Override
        public DistExponential getDist() throws InputParameterException
        {
            Throw.when((Double) get("lambda").getValue() <= 0.0, InputParameterException.class,
                    "DistExponential: lambda <= 0.0");
            try
            {
                return new DistExponential(getStream(), (Double) get("lambda").getValue());
            }
            catch (Exception exception)
            {
                throw new InputParameterException("DistExponential: " + exception.getMessage(), exception);
            }
        }
    }

    /** InputParameterDistContinuous.Normal class. */
    public static class Normal extends InputParameterMapDistContinuous
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * Construct the input for the Normal distribution.
         * @throws InputParameterException on error with the distribution
         */
        public Normal() throws InputParameterException
        {
            super("Normal", "Normal", "Normal distribution", 1.0);
            add(new InputParameterDouble("mu", "mu", "mu value, mean of the Normal distribution", 0.0, -Double.MAX_VALUE,
                    Double.MAX_VALUE, false, false, "%f", 1.0));
            add(new InputParameterDouble("sigma", "sigma", "sigma value, standard deviation of the Normal distribution", 1.0,
                    0.0, Double.MAX_VALUE, true, false, "%f", 2.0));
        }

        /** {@inheritDoc} */
        @Override
        public DistNormal getDist() throws InputParameterException
        {
            Throw.when((Double) get("sigma").getValue() < 0.0, InputParameterException.class, "DistNormal: sigma < 0.0");
            try
            {
                return new DistNormal(getStream(), (Double) get("mu").getValue(), (Double) get("sigma").getValue());
            }
            catch (Exception exception)
            {
                throw new InputParameterException("DistNormal: " + exception.getMessage(), exception);
            }
        }
    }

    /** InputParameterDistContinuous.Triangular class. */
    public static class Triangular extends InputParameterMapDistContinuous
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * Construct the input for the Triangular distribution.
         * @throws InputParameterException on error with the distribution
         */
        public Triangular() throws InputParameterException
        {
            super("Triangular", "Triangular", "Triangular distribution", 1.0);
            add(new InputParameterDouble("a", "min", "a value, lowest value of the Triangular distribution", 0.0,
                    -Double.MAX_VALUE, Double.MAX_VALUE, false, false, "%f", 1.0));
            add(new InputParameterDouble("b", "mode", "b value, mode value of the Triangular distribution", 1.0,
                    -Double.MAX_VALUE, Double.MAX_VALUE, false, false, "%f", 2.0));
            add(new InputParameterDouble("c", "max", "c value, highest value of the Triangular distribution", 2.0,
                    -Double.MAX_VALUE, Double.MAX_VALUE, false, false, "%f", 3.0));
        }

        /** {@inheritDoc} */
        @Override
        public DistTriangular getDist() throws InputParameterException
        {
            Throw.when((Double) get("a").getValue() > (Double) get("b").getValue(), InputParameterException.class,
                    "DistTriangular: a > b");
            Throw.when((Double) get("b").getValue() > (Double) get("c").getValue(), InputParameterException.class,
                    "DistTriangular: b > c");
            Throw.when(
                    ((Double) get("a").getValue()).equals(get("b").getValue())
                            && ((Double) get("b").getValue()).equals(get("c").getValue()),
                    InputParameterException.class, "DistTriangular: a == b == c");
            try
            {
                return new DistTriangular(getStream(), (Double) get("a").getValue(), (Double) get("b").getValue(),
                        (Double) get("c").getValue());
            }
            catch (Exception exception)
            {
                throw new InputParameterException("DistTriangular: " + exception.getMessage(), exception);
            }
        }
    }

    /** InputParameterDistContinuous.Uniform class. */
    public static class Uniform extends InputParameterMapDistContinuous
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * Construct the input for the Uniform distribution.
         * @throws InputParameterException on error with the distribution
         */
        public Uniform() throws InputParameterException
        {
            super("Uniform", "Uniform", "Uniform distribution", 1.0);
            add(new InputParameterDouble("a", "min", "a value, lowest value of the Uniform distribution", 0.0,
                    -Double.MAX_VALUE, Double.MAX_VALUE, false, false, "%f", 1.0));
            add(new InputParameterDouble("b", "max", "b value, highest value of the Uniform distribution", 1.0,
                    -Double.MAX_VALUE, Double.MAX_VALUE, false, false, "%f", 2.0));
        }

        /** {@inheritDoc} */
        @Override
        public DistUniform getDist() throws InputParameterException
        {
            Throw.when((Double) get("a").getValue() >= (Double) get("b").getValue(), InputParameterException.class,
                    "DistUniform: a >= b");
            try
            {
                return new DistUniform(getStream(), (Double) get("a").getValue(), (Double) get("b").getValue());
            }
            catch (Exception exception)
            {
                throw new InputParameterException("DistUniform: " + exception.getMessage(), exception);
            }
        }
    }

    /** InputParameterDistContinuous.Beta class. */
    public static class Beta extends InputParameterMapDistContinuous
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * Construct the input for the Beta distribution.
         * @throws InputParameterException on error with the distribution
         */
        public Beta() throws InputParameterException
        {
            super("Beta", "Beta", "Beta distribution", 1.0);
            add(new InputParameterDouble("alpha1", "alpha1", "alpha1 value", 1.0, 0.0, Double.MAX_VALUE, false, false, "%f",
                    1.0));
            add(new InputParameterDouble("alpha2", "alpha2", "alpha2 value", 1.0, 0.0, Double.MAX_VALUE, false, false, "%f",
                    2.0));
        }

        /** {@inheritDoc} */
        @Override
        public DistBeta getDist() throws InputParameterException
        {
            Throw.when((Double) get("alpha1").getValue() <= 0.0, InputParameterException.class, "DistBeta: alpha1 <= 0.0");
            Throw.when((Double) get("alpha2").getValue() <= 0.0, InputParameterException.class, "DistBeta: alpha2 <= 0.0");
            try
            {
                return new DistBeta(getStream(), (Double) get("alpha1").getValue(), (Double) get("alpha2").getValue());
            }
            catch (Exception exception)
            {
                throw new InputParameterException("DistBeta: " + exception.getMessage(), exception);
            }
        }
    }

    /** InputParameterDistContinuous.Constant class. */
    public static class Constant extends InputParameterMapDistContinuous
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * Construct the input for the Constant distribution.
         * @throws InputParameterException on error with the distribution
         */
        public Constant() throws InputParameterException
        {
            super("Constant", "Constant", "Constant 'distribution'", 1.0);
            add(new InputParameterDouble("c", "c", "constant value to return", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE, false,
                    false, "%f", 1.0));
        }

        /** {@inheritDoc} */
        @Override
        public DistConstant getDist() throws InputParameterException
        {
            try
            {
                return new DistConstant(getStream(), (Double) get("c").getValue());
            }
            catch (Exception exception)
            {
                throw new InputParameterException("DistConstant: " + exception.getMessage(), exception);
            }
        }
    }

}
