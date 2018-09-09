package nl.tudelft.simulation.dsol.formalisms.flow;

import java.io.Serializable;
import java.rmi.MarshalledObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * A duplicate station duplicates incoming objects and sends them to their alternative destination. <br>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author Peter Jacobs, Alexander Verbraeck
 */
public class Duplicate extends Station
{
    /** the logger. */
    private static Logger logger = LogManager.getLogger(Duplicate.class);

    /**
     * duplicateDestination which is the duplicate definition.
     */
    private StationInterface duplicateDestination;

    /** numberCopies refers to the number of duplicates. */
    private int numberCopies;

    /**
     * Method Duplicate. Creates a new Duplicate
     * @param simulator on which is scheduled
     * @param duplicateDestination the duplicate destination
     */
    public Duplicate(final DEVSSimulatorInterface simulator, final StationInterface duplicateDestination)
    {
        this(simulator, duplicateDestination, 1);
    }

    /**
     * Method Duplicate.
     * @param simulator on which is scheduled
     * @param duplicateDestination which is the duplicate definition
     * @param numberCopies the number of copies
     */
    public Duplicate(final DEVSSimulatorInterface simulator, final StationInterface duplicateDestination,
            final int numberCopies)
    {
        super(simulator);
        this.duplicateDestination = duplicateDestination;
        this.numberCopies = numberCopies;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void receiveObject(final Object object)
    {
        super.receiveObject(object);
        try
        {
            this.releaseObject(object);
            if (object instanceof Serializable)
            {
                for (int i = 0; i < this.numberCopies; i++)
                {
                    Object clone = new MarshalledObject(object).get();
                    this.fireEvent(StationInterface.RELEASE_EVENT, 1);
                    this.duplicateDestination.receiveObject(clone);
                }
            }
            else
            {
                throw new Exception(
                        "cannot duplicate object: " + object.getClass() + " does not implement java.io.Serializable");
            }
        }
        catch (Exception exception)
        {
            logger.warn("receiveMethod", exception);
        }
    }
}
