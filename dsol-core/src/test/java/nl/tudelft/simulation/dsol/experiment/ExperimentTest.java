package nl.tudelft.simulation.dsol.experiment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.exceptions.Try;
import org.junit.Test;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.Sleep;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.SimCounter;
import nl.tudelft.simulation.dsol.statistics.SimPersistent;
import nl.tudelft.simulation.dsol.statistics.SimTally;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.jstats.distributions.DistExponential;
import nl.tudelft.simulation.naming.context.ContextInterface;

/**
 * ExperimentTest tests the correct working of the Experiment object.
 * <p>
 * Copyright (c) 2021-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class ExperimentTest
{
    /**
     * Test the Experiment object.
     */
    @Test
    @SuppressWarnings("null")
    public void testDoubleExperiment()
    {
        DEVSSimulator.TimeDouble simulator = new DEVSSimulator.TimeDouble("simulator");
        DSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble> model = new CountModel(simulator, new LinkedHashMap<>());
        Experiment.TimeDouble<DEVSSimulatorInterface.TimeDouble> expd =
                new Experiment.TimeDouble<>("Exp 1", simulator, model, 10.0, 1.0, 12.0, 10);

        expd.removeFromContext(); // should not yet have been added
        assertEquals("Exp 1", expd.getId());
        assertEquals("Exp 1", expd.getDescription());
        assertEquals("Exp 1", expd.getSourceId());
        expd.setDescription("Experiment 1");
        assertEquals("Experiment 1", expd.getDescription());
        assertEquals(10.0, expd.getStartTime(), 1E-9);
        assertEquals(10.0, expd.getStartSimTime().get(), 1E-9);
        assertEquals(22.0, expd.getEndTime(), 1E-9);
        assertEquals(22.0, expd.getEndSimTime().get(), 1E-9);
        assertEquals(11.0, expd.getWarmupTime(), 1E-9);
        assertEquals(11.0, expd.getWarmupSimTime().get(), 1E-9);
        assertEquals(12.0, expd.getRunLength(), 1E-9);
        assertEquals(1.0, expd.getWarmupPeriod(), 1E-9);
        assertTrue(expd.toString().contains("Experiment 1"));
        ContextInterface c1 = expd.getContext();
        assertNotNull(c1);
        ContextInterface c2 = expd.getContext();
        assertEquals(c1, c2);
        assertEquals(simulator, expd.getSimulator());
        assertEquals(model, expd.getModel());
        assertEquals(10, expd.getNumberOfReplications());
        assertEquals(0, expd.getStartedReplications().size());
        assertEquals(-1, expd.getCurrentReplication());
        expd.removeFromContext();

        // errors
        Try.testFail(() -> { new Experiment.TimeDouble<>(null, simulator, model, 0.0, 10.0, 20.0, 10); });
        Try.testFail(() -> { new Experiment.TimeDouble<>("exp1", null, model, 0.0, 10.0, 20.0, 10); });
        Try.testFail(() -> { new Experiment.TimeDouble<>("exp1", simulator, null, 0.0, 10.0, 20.0, 10); });
        Try.testFail(() -> { new Experiment.TimeDouble<>("exp1", simulator, model, (Double) null, 10.0, 20.0, 10); });
        Try.testFail(() -> { new Experiment.TimeDouble<>("exp1", simulator, model, 0.0, (Double) null, 20.0, 10); });
        Try.testFail(() -> { new Experiment.TimeDouble<>("exp1", simulator, model, 0.0, 10.0, (Double) null, 10); });
        Try.testFail(() -> { new Experiment.TimeDouble<>("exp1", simulator, model, 0.0, -10.0, 20.0, 10); });
        Try.testFail(() -> { new Experiment.TimeDouble<>("exp1", simulator, model, 0.0, 10.0, 0.0, 10); });
        Try.testFail(() -> { new Experiment.TimeDouble<>("exp1", simulator, model, 0.0, 10.0, -20.0, 10); });
        Try.testFail(() -> { new Experiment.TimeDouble<>("exp1", simulator, model, 0.0, 10.0, 20.0, 0); });
        Try.testFail(() -> { new Experiment.TimeDouble<>("exp1", simulator, model, 0.0, 10.0, 20.0, -10); });

        // should be ok
        new Experiment.TimeDouble<>("Exp1a", simulator, model, 0.0, 0.0, 20.0, 10);
        new Experiment.TimeDouble<>("Exp1a", simulator, model, -10.0, 0.0, 20.0, 10);
    }

    /**
     * Test the Experiment object.
     */
    @Test
    public void testExperimentTypes()
    {
        // generic experiment
        DEVSSimulator<Double, Double, SimTimeDouble> simd = new DEVSSimulator<>("simulator");
        DSOLModel<Double, Double, SimTimeDouble, DEVSSimulatorInterface<Double, Double, SimTimeDouble>> modd =
                new AbstractDSOLModel<Double, Double, SimTimeDouble, DEVSSimulatorInterface<Double, Double, SimTimeDouble>>(
                        simd)
                {
                    /** */
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void constructModel() throws SimRuntimeException
                    {
                        //
                    }

                    @Override
                    public Serializable getSourceId()
                    {
                        return "exp";
                    }
                };
        Experiment<Double, Double, SimTimeDouble, DEVSSimulatorInterface<Double, Double, SimTimeDouble>> d1 =
                new Experiment<>("Exp 1", simd, modd, new SimTimeDouble(10.0), 1.0, 12.0, 10);
        assertEquals(22.0, d1.getEndTime(), 1E-6);
        assertEquals(modd, d1.getModel());
        d1.makeExperimentReplication();

        // float experiment
        DEVSSimulator.TimeFloat simf = new DEVSSimulator.TimeFloat("simulator");
        DSOLModel.TimeFloat<DEVSSimulatorInterface.TimeFloat> modf =
                new AbstractDSOLModel.TimeFloat<DEVSSimulatorInterface.TimeFloat>(simf)
                {
                    /** */
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void constructModel() throws SimRuntimeException
                    {
                        //
                    }

                    @Override
                    public Serializable getSourceId()
                    {
                        return "exp";
                    }
                };
        Experiment.TimeFloat<DEVSSimulatorInterface.TimeFloat> f1 =
                new Experiment.TimeFloat<>("f1", simf, modf, 10.0f, 1.0f, 12.0f, 10);
        assertEquals(22.0f, f1.getEndTime(), 1E-6);
        assertEquals(modf, f1.getModel());
        f1.makeExperimentReplication();

        // long experiment
        DEVSSimulator.TimeLong siml = new DEVSSimulator.TimeLong("simulator");
        DSOLModel.TimeLong<DEVSSimulatorInterface.TimeLong> modl =
                new AbstractDSOLModel.TimeLong<DEVSSimulatorInterface.TimeLong>(siml)
                {
                    /** */
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void constructModel() throws SimRuntimeException
                    {
                        //
                    }

                    @Override
                    public Serializable getSourceId()
                    {
                        return "exp";
                    }
                };
        Experiment.TimeLong<DEVSSimulatorInterface.TimeLong> l1 = new Experiment.TimeLong<>("l1", siml, modl, 10L, 1L, 12L, 10);
        assertEquals(22L, l1.getEndTime().longValue());
        assertEquals(modl, l1.getModel());
        l1.makeExperimentReplication();

        // double unit experiment
        DEVSSimulator.TimeDoubleUnit simdu = new DEVSSimulator.TimeDoubleUnit("simulator");
        DSOLModel.TimeDoubleUnit<DEVSSimulatorInterface.TimeDoubleUnit> moddu =
                new AbstractDSOLModel.TimeDoubleUnit<DEVSSimulatorInterface.TimeDoubleUnit>(simdu)
                {
                    /** */
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void constructModel() throws SimRuntimeException
                    {
                        //
                    }

                    @Override
                    public Serializable getSourceId()
                    {
                        return "exp";
                    }
                };
        Experiment.TimeDoubleUnit<DEVSSimulatorInterface.TimeDoubleUnit> du1 = new Experiment.TimeDoubleUnit<>("du1", simdu,
                moddu, Time.ZERO, Duration.ZERO, Duration.instantiateSI(1000.0), 10);
        assertEquals(1000.0, du1.getEndTime().doubleValue(), 1E-6);
        assertEquals(moddu, du1.getModel());
        du1.makeExperimentReplication();

        // float unit experiment
        DEVSSimulator.TimeFloatUnit simfu = new DEVSSimulator.TimeFloatUnit("simulator");
        DSOLModel.TimeFloatUnit<DEVSSimulatorInterface.TimeFloatUnit> modfu =
                new AbstractDSOLModel.TimeFloatUnit<DEVSSimulatorInterface.TimeFloatUnit>(simfu)
                {
                    /** */
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void constructModel() throws SimRuntimeException
                    {
                        //
                    }

                    @Override
                    public Serializable getSourceId()
                    {
                        return "exp";
                    }
                };
        Experiment.TimeFloatUnit<DEVSSimulatorInterface.TimeFloatUnit> fu1 = new Experiment.TimeFloatUnit<>("du1", simfu, modfu,
                FloatTime.ZERO, FloatDuration.ZERO, FloatDuration.instantiateSI(1000.0f), 10);
        assertEquals(1000.0f, fu1.getEndTime().floatValue(), 1E-6);
        assertEquals(modfu, fu1.getModel());
        fu1.makeExperimentReplication();
    }

    /**
     * test the execution of an experiment with 10 replications.
     * @throws RemoteException on error
     */
    @Test
    public void testExperimentRun() throws RemoteException
    {
        SortedMap<Integer, Integer> dataCollector = new TreeMap<>();
        DEVSSimulator.TimeDouble simulator = new DEVSSimulator.TimeDouble("simulator");
        DSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble> model = new CountModel(simulator, dataCollector);
        Experiment.TimeDouble<DEVSSimulatorInterface.TimeDouble> expd =
                new Experiment.TimeDouble<>("Exp 1", simulator, model, 10.0, 1.0, 12.0, 10);

        expd.start();
        int count = 0;
        while (expd.isRunning() && count < 1000)
        {
            count++;
            Sleep.sleep(1);
        }
        assertTrue(count < 1000);

        assertEquals(10, dataCollector.size());
        for (int i = 0; i < 10; i++)
        {
            assertEquals(13, dataCollector.get(i).intValue());
        }

        // test failure
        Try.testFail(() -> { expd.start(); });
        Try.testFail(() -> { expd.startNextReplication(); });

        // reset and do again
        dataCollector.clear();
        expd.reset();
        expd.start();
        count = 0;
        while (expd.isRunning() && count < 1000)
        {
            count++;
            Sleep.sleep(1);
        }
        assertTrue(count < 1000);
        assertEquals(10, dataCollector.size());
        for (int i : dataCollector.keySet())
        {
            assertEquals(13, dataCollector.get(i).intValue());
        }
    }

    /**
     * test the calculation of a summary statistic foran experiment with 10 replications.
     * @throws RemoteException on error
     */
    @Test
    public void testSummaryStatistics() throws RemoteException
    {
        DEVSSimulator.TimeDouble simulator = new DEVSSimulator.TimeDouble("simulator");
        DSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble> model = new MM1Model(simulator);
        Experiment.TimeDouble<DEVSSimulatorInterface.TimeDouble> expd =
                new Experiment.TimeDouble<>("Exp 1", simulator, model, 10.0, 10.0, 20.0, 10);

        expd.start();
        int count = 0;
        while (expd.isRunning() && count < 1000)
        {
            count++;
            Sleep.sleep(1);
        }
        assertTrue(count < 2000);

        assertEquals(3, expd.getSummaryStatistics().size());
        System.out.println(expd.getSummaryStatistics());
    }

    /**
     * Model class.
     */
    public static class CountModel extends AbstractDSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /** counter. */
        private int count;

        /** the id of this model. */
        private int id = -1;

        /** the data collector. */
        private final Map<Integer, Integer> dataCollector;

        /**
         * @param simulator the simulator
         * @param dataCollector the data collector
         */
        public CountModel(final DEVSSimulatorInterface.TimeDouble simulator, final Map<Integer, Integer> dataCollector)
        {
            super(simulator);
            this.dataCollector = dataCollector;
        }

        /** {@inheritDoc} */
        @Override
        public void constructModel() throws SimRuntimeException
        {
            this.count = 0;
            this.id++;
            next();
        }

        /** next method. */
        public void next()
        {
            getSimulator().scheduleEventRel(1.0, this, this, "next", null);
            this.count++;
            this.dataCollector.put(this.id, this.count);
        }

        /** {@inheritDoc} */
        @Override
        public Serializable getSourceId()
        {
            return "model";
        }
    }

    /**
     * Quick and dirty MM1 queuing system Model class.
     */
    public static class MM1Model extends AbstractDSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /** queue. */
        private List<Entity> queue;

        /** generator: every 1 time unit on average. */
        private DistContinuous iatDist;

        /** processing : 0.8 time units on average. */
        private DistContinuous procDist;

        /** a counter of the number of arrivals. */
        private SimCounter.TimeDouble count;

        /** a tally of the waiting time. */
        private SimTally.TimeDouble queueTimeTally;

        /** a persistent of the time in queue. */
        private SimPersistent.TimeDouble nrInQueuePersistent;

        /**
         * @param simulator the simulator
         */
        public MM1Model(final DEVSSimulatorInterface.TimeDouble simulator)
        {
            super(simulator);
            this.iatDist = new DistExponential(getStream("default"), 1.0);
            this.procDist = new DistExponential(getStream("default"), 0.8);
        }

        /** {@inheritDoc} */
        @Override
        public void constructModel() throws SimRuntimeException
        {
            try
            {
                this.queue = new ArrayList<>();
                this.outputStatistics.clear();
                this.count = new SimCounter.TimeDouble("arrivals", this.simulator);
                this.count.initialize();
                this.outputStatistics.add(this.count);
                this.queueTimeTally = new SimTally.TimeDouble("timeInQueue", this.simulator);
                this.queueTimeTally.initialize();
                this.outputStatistics.add(this.queueTimeTally);
                this.nrInQueuePersistent = new SimPersistent.TimeDouble("nrInQueue", this.simulator);
                this.nrInQueuePersistent.initialize();
                this.outputStatistics.add(this.nrInQueuePersistent);
            }
            catch (RemoteException rme)
            {
                throw new SimRuntimeException(rme);
            }

            next();
        }

        /** next method. */
        public void next()
        {
            this.count.ingest(1);
            Entity entity = new Entity(this.simulator.getSimulatorTime());
            this.queue.add(entity);
            this.nrInQueuePersistent.ingest(this.queue.size());
            getSimulator().scheduleEventRel(this.iatDist.draw(), this, this, "next", null);
            getSimulator().scheduleEventRel(this.procDist.draw(), this, this, "endWait", new Object[] {entity});
        }

        /** @param entity the entity that is ready */
        protected void endWait(final Entity entity)
        {
            this.queue.remove(entity);
            this.nrInQueuePersistent.ingest(this.queue.size());
            this.queueTimeTally.ingest(this.simulator.getSimulatorTime() - entity.getCreateTime());
        }

        /** {@inheritDoc} */
        @Override
        public Serializable getSourceId()
        {
            return "MM1";
        }
    }

    /** the entity class. */
    static class Entity
    {
        /** the of creation of the entity. */
        private final double createTime;

        /**
         * @param createTime the create time of the entity.
         */
        Entity(final double createTime)
        {
            this.createTime = createTime;
        }

        /** @return the createTime of the entity. */
        double getCreateTime()
        {
            return this.createTime;
        }
    }
}
