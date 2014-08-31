package nl.tudelft.simulation.dsol.test;

import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.ModelInterface;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Experiment;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.experiment.Treatment;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * <p>
 * Copyright (c) 2002-2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
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
 * @version Aug 5, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class TestSim
{
    /** */
    private DEVSSimulatorInterface.Double simulatorDouble;

    /**
     * @throws SimRuntimeException
     * @throws RemoteException
     * @throws NamingException
     */
    public TestSim() throws SimRuntimeException, RemoteException, NamingException
    {
        this.simulatorDouble = new DEVSSimulator.Double();

        Model model = new Model(this.simulatorDouble);
        Context context = new InitialContext();
        Experiment experiment = new Experiment(context);
        experiment.setModel(model);
        Replication rep = new Replication<>(context, experiment);
        Treatment treatment = new Treatment(experiment, "tr1", new SimTimeDouble(0.0), 0.0, 100.0);
        experiment.setTreatment(treatment);
        this.simulatorDouble.initialize(rep, ReplicationMode.TERMINATING);

        for (int i = 0; i < 10; i++)
            this.simulatorDouble.scheduleEventAbs(1.0d * i, this, this, "print", null);
        this.simulatorDouble.start();
    }

    /**
     * @throws RemoteException
     */
    protected void print() throws RemoteException
    {
        System.out.println("print scheduled at t=" + this.simulatorDouble.getSimulatorTime().get().doubleValue());
    }

    /**
     * @param args
     * @throws SimRuntimeException
     * @throws RemoteException
     * @throws NamingException
     */
    public static void main(String[] args) throws SimRuntimeException, RemoteException, NamingException
    {
        new TestSim();
    }

    public class Model implements ModelInterface
    {
        /** */
        private SimulatorInterface simulator;

        /**
         * @param simulator
         */
        public Model(SimulatorInterface simulator)
        {
            super();
            this.simulator = simulator;
        }

        /** {@inheritDoc} */
        @Override
        public void constructModel(SimulatorInterface simulator) throws SimRuntimeException, RemoteException
        {
            //
        }

        /** {@inheritDoc} */
        @Override
        public SimulatorInterface getSimulator() throws RemoteException
        {
            return this.simulator;
        }

    }
}
