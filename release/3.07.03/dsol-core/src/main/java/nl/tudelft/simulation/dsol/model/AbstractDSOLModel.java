package nl.tudelft.simulation.dsol.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.EventProducer;
import org.djutils.exceptions.Throw;

import nl.tudelft.simulation.dsol.experiment.StreamInformation;
import nl.tudelft.simulation.dsol.model.inputparameters.AbstractInputParameter;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterException;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterMap;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.StatisticsInterface;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * AbstractDSOLModel, an abstract helper class to easily construct a DSOLModel. The model automatically acts as an
 * EventProducer, so events can be sent to statistics gathering classes. <br>
 * <br>
 * Copyright (c) 2003-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @param <S> the simulator type to use
 */
public abstract class AbstractDSOLModel<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>, S extends SimulatorInterface<A, R, T>> extends EventProducer
        implements DSOLModel<A, R, T, S>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected S simulator;

    /** the input parameters. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected InputParameterMap inputParameterMap = new InputParameterMap("model", "Model parameters", "Model parameters", 1.0);

    /** the output statistics. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected List<StatisticsInterface<A, R, T>> outputStatistics = new ArrayList<>();

    /** streams used in the replication. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Map<String, StreamInterface> streams = new LinkedHashMap<String, StreamInterface>();

    /**
     * Construct a DSOL model and set the simulator.
     * @param simulator S; the simulator to use for this model
     * @throws NullPointerException when simulator is null
     */
    public AbstractDSOLModel(final S simulator)
    {
        Throw.whenNull(simulator, "simulator cannot be null");
        this.simulator = simulator;
        setInitialStreams();
    }

    /**
     * Construct a DSOL model and set the simulator as well as the initial streams, so they can be used in the constructor of
     * the model.
     * @param simulator S; the simulator to use for this model
     * @param streamInformation StreamInformation; the streams that have been prepared in a StreamInformation class
     * @throws NullPointerException when simulator or streamInformation is null
     */
    public AbstractDSOLModel(final S simulator, final StreamInformation streamInformation)
    {
        Throw.whenNull(simulator, "simulator cannot be null");
        this.simulator = simulator;
        setInitialStreams(streamInformation);
    }

    /** {@inheritDoc} */
    @Override
    public void setInitialStreams(final StreamInformation streamInformation)
    {
        Throw.whenNull(streamInformation, "streamInformation cannot be null");
        this.streams.clear();
        this.streams.putAll(streamInformation.getStreams());
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, StreamInterface> getStreams()
    {
        return this.streams;
    }

    /** {@inheritDoc} */
    @Override
    public StreamInterface getStream(final String streamId)
    {
        synchronized (this.streams)
        {
            return this.streams.get(streamId);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void resetStreams()
    {
        synchronized (this.streams)
        {
            for (StreamInterface stream : getStreams().values())
            {
                stream.reset();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public S getSimulator()
    {
        return this.simulator;
    }

    /** {@inheritDoc} */
    @Override
    public InputParameterMap getInputParameterMap()
    {
        return this.inputParameterMap;
    }

    /**
     * Add an input parameter to the list of input parameters.
     * @param inputParameter AbstractInputParameter&lt;?,?&gt;; the input parameter to add
     * @throws InputParameterException in case an input parameter with the same key already exists
     */
    public void addInputParameter(final AbstractInputParameter<?, ?> inputParameter) throws InputParameterException
    {
        this.inputParameterMap.add(inputParameter);
    }

    /**
     * Retrieve the value of an input parameter from the map of input parameters, based on a key. The key can use the 'dot
     * notation' to access values in sub-maps of input parameters.
     * @param key String; the key of the input parameter to retrieve
     * @return the value belonging to the key, or null if the key could not be found
     * @throws InputParameterException in case the input parameter with this key does not exist
     */
    public Object getInputParameter(final String key) throws InputParameterException
    {
        return this.inputParameterMap.get(key).getCalculatedValue();
    }

    /** {@inheritDoc} */
    @Override
    public List<StatisticsInterface<A, R, T>> getOutputStatistics()
    {
        return this.outputStatistics;
    }

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /**
     * Easy access class AbstractDSOLModel.TimeDouble.
     * @param <S> the simulator type to use
     */
    public abstract static class TimeDouble<S extends SimulatorInterface.TimeDouble>
            extends AbstractDSOLModel<Double, Double, SimTimeDouble, S> implements DSOLModel.TimeDouble<S>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * Construct an abstract DSOL model and set the simulator.
         * @param simulator S; the simulator to use for this model
         * @throws NullPointerException when simulator is null
         */
        public TimeDouble(final S simulator)
        {
            super(simulator);
        }

        /**
         * Construct an abstract DSOL model and set the simulator as well as the initial streams, so they can be used in the
         * constructor of the model.
         * @param simulator S; the simulator to use for this model
         * @param streamInformation StreamInformation; the streams that have been prepared in a StreamInformation class
         * @throws NullPointerException when simulator or streamInformation is null
         */
        public TimeDouble(final S simulator, final StreamInformation streamInformation)
        {
            super(simulator, streamInformation);
        }
    }

    /**
     * Easy access class AbstractDSOLModel.TimeFloat.
     * @param <S> the simulator type to use
     */
    public abstract static class TimeFloat<S extends SimulatorInterface.TimeFloat>
            extends AbstractDSOLModel<Float, Float, SimTimeFloat, S> implements DSOLModel.TimeFloat<S>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * Construct an abstract DSOL model and set the simulator.
         * @param simulator S; the simulator to use for this model
         * @throws NullPointerException when simulator is null
         */
        public TimeFloat(final S simulator)
        {
            super(simulator);
        }

        /**
         * Construct an abstract DSOL model and set the simulator as well as the initial streams, so they can be used in the
         * constructor of the model.
         * @param simulator S; the simulator to use for this model
         * @param streamInformation StreamInformation; the streams that have been prepared in a StreamInformation class
         * @throws NullPointerException when simulator or streamInformation is null
         */
        public TimeFloat(final S simulator, final StreamInformation streamInformation)
        {
            super(simulator, streamInformation);
        }
    }

    /**
     * Easy access class AbstractDSOLModel.TimeLong.
     * @param <S> the simulator type to use
     */
    public abstract static class TimeLong<S extends SimulatorInterface.TimeLong>
            extends AbstractDSOLModel<Long, Long, SimTimeLong, S> implements DSOLModel.TimeLong<S>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * Construct an abstract DSOL model and set the simulator.
         * @param simulator S; the simulator to use for this model
         * @throws NullPointerException when simulator is null
         */
        public TimeLong(final S simulator)
        {
            super(simulator);
        }

        /**
         * Construct an abstract DSOL model and set the simulator as well as the initial streams, so they can be used in the
         * constructor of the model.
         * @param simulator S; the simulator to use for this model
         * @param streamInformation StreamInformation; the streams that have been prepared in a StreamInformation class
         * @throws NullPointerException when simulator or streamInformation is null
         */
        public TimeLong(final S simulator, final StreamInformation streamInformation)
        {
            super(simulator, streamInformation);
        }
    }

    /**
     * Easy access class AbstractDSOLModel.TimeDoubleUnit.
     * @param <S> the simulator type to use
     */
    public abstract static class TimeDoubleUnit<S extends SimulatorInterface.TimeDoubleUnit>
            extends AbstractDSOLModel<Time, Duration, SimTimeDoubleUnit, S> implements DSOLModel.TimeDoubleUnit<S>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * Construct an abstract DSOL model and set the simulator.
         * @param simulator S; the simulator to use for this model
         * @throws NullPointerException when simulator is null
         */
        public TimeDoubleUnit(final S simulator)
        {
            super(simulator);
        }

        /**
         * Construct an abstract DSOL model and set the simulator as well as the initial streams, so they can be used in the
         * constructor of the model.
         * @param simulator S; the simulator to use for this model
         * @param streamInformation StreamInformation; the streams that have been prepared in a StreamInformation class
         * @throws NullPointerException when simulator or streamInformation is null
         */
        public TimeDoubleUnit(final S simulator, final StreamInformation streamInformation)
        {
            super(simulator, streamInformation);
        }
    }

    /**
     * Easy access class AbstractDSOLModel.TimeFloatUnit.
     * @param <S> the simulator type to use
     */
    public abstract static class TimeFloatUnit<S extends SimulatorInterface.TimeFloatUnit>
            extends AbstractDSOLModel<FloatTime, FloatDuration, SimTimeFloatUnit, S> implements DSOLModel.TimeFloatUnit<S>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * Construct an abstract DSOL model and set the simulator.
         * @param simulator S; the simulator to use for this model
         * @throws NullPointerException when simulator is null
         */
        public TimeFloatUnit(final S simulator)
        {
            super(simulator);
        }

        /**
         * Construct an abstract DSOL model and set the simulator as well as the initial streams, so they can be used in the
         * constructor of the model.
         * @param simulator S; the simulator to use for this model
         * @param streamInformation StreamInformation; the streams that have been prepared in a StreamInformation class
         * @throws NullPointerException when simulator or streamInformation is null
         */
        public TimeFloatUnit(final S simulator, final StreamInformation streamInformation)
        {
            super(simulator, streamInformation);
        }
    }

}
