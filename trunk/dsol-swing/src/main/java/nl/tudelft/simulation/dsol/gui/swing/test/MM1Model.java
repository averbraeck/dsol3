package nl.tudelft.simulation.dsol.gui.swing.test;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.dist.DistContinuousTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.jstats.distributions.DistExponential;
import nl.tudelft.simulation.jstats.distributions.DistTriangular;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="https://simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version Aug 15, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class MM1Model implements DSOLModel.TimeDouble
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator;

    /** resource capacity. */
    private int capacity = 1;

    /** busy units. */
    private int busy = 0;

    /** the stream. */
    private StreamInterface stream = new MersenneTwister();

    /** interarrival time. */
    private DistContinuousTime.TimeDouble interarrivalTime =
            new DistContinuousTime.TimeDouble(new DistExponential(this.stream, 1.0));

    /** processing time. */
    private DistContinuousTime.TimeDouble processingTime =
            new DistContinuousTime.TimeDouble(new DistTriangular(this.stream, 0.8, 0.9, 1.1));

    /** queue of waiting entities. */
    private List<QueueEntry<Entity>> queue = new ArrayList<QueueEntry<Entity>>();

    /** entity counter for id. */
    private int entityCounter = 0;

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface<Double, Double, SimTimeDouble> _simulator)
            throws SimRuntimeException
    {
        this.simulator = (DEVSSimulatorInterface.TimeDouble) _simulator;
        generate();
    }

    /**
     * Generate new items. Reschedules itself.
     * @throws SimRuntimeException on simulation error
     */
    protected void generate() throws SimRuntimeException
    {
        Entity entity = new Entity(this.entityCounter++, this.simulator.getSimTime());
        System.out.println("Generated: " + entity);
        synchronized (this.queue)
        {
            if (this.capacity - this.busy >= 1)
            {
                // process
                startProcess(entity);
            }
            else
            {
                // queue
                this.queue.add(new QueueEntry<Entity>(entity, this.simulator.getSimTime()));
                System.out.println("In Queue: " + entity);
            }
        }
        this.simulator.scheduleEventRel(this.interarrivalTime.draw(), this, this, "generate", null);
    }

    /**
     * @param entity entity to process
     * @throws SimRuntimeException on simulation error
     */
    protected void startProcess(Entity entity) throws SimRuntimeException
    {
        synchronized (this.queue)
        {
            this.busy++;
            this.simulator.scheduleEventRel(this.processingTime.draw(), this, this, "endProcess", new Object[]{entity});
            System.out.println("Start Proc.: " + entity);
        }
    }

    /**
     * @param entity entity to stop processing
     * @throws SimRuntimeException on simulation error
     */
    protected void endProcess(Entity entity) throws SimRuntimeException
    {
        System.out.println("End Process: " + entity);
        synchronized (this.queue)
        {
            this.busy--;
            if (!this.queue.isEmpty())
            {
                startProcess(this.queue.remove(0).getObject());
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public DEVSSimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }

    /******************************************************************************************************/
    protected class Entity
    {
        /** time of creation for statistics. */
        private final SimTimeDouble createTime;

        /** id number. */
        private final int id;

        /**
         * @param id entity id number
         * @param createTime time of creation for statistics
         */
        public Entity(int id, SimTimeDouble createTime)
        {
            super();
            this.id = id;
            this.createTime = createTime;
        }

        /**
         * @return time of creation for statistics
         */
        public SimTimeDouble getCreateTime()
        {
            return this.createTime;
        }

        /**
         * @return entity id number
         */
        public int getId()
        {
            return this.id;
        }

        /** {@inheritDoc} */
        @Override
        public String toString()
        {
            return "Entity [createTime=" + this.createTime + ", id=" + this.id + "]";
        }
    }

    /******************************************************************************************************/
    protected class QueueEntry<E>
    {
        /** time of queue entry for statistics. */
        private final SimTimeDouble queueInTime;

        /** object in the queue. */
        final E object;

        /**
         * @param object the object to insert in the queue
         * @param queueInTime the time it gets into the queue
         */
        public QueueEntry(E object, SimTimeDouble queueInTime)
        {
            super();
            this.object = object;
            this.queueInTime = queueInTime;
        }

        /**
         * @return queueInTime
         */
        public SimTimeDouble getQueueInTime()
        {
            return this.queueInTime;
        }

        /**
         * @return object
         */
        public E getObject()
        {
            return this.object;
        }

        /** {@inheritDoc} */
        @Override
        public String toString()
        {
            return "QueueEntry [queueInTime=" + this.queueInTime + ", object=" + this.object + "]";
        }
    }
}
