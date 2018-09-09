package nl.tudelft.simulation.naming;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URI;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A factory for FileContext instances, automatically invoked by JNDI when the correct jndi.properties file has been
 * used.
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.3 2004-03-24
 * @since 1.5
 */
public class FileContextFactory implements InitialContextFactory
{
    /** context refers to the static JVMContext. */
    private static FileContext context = null;

    /** the logger./ */
    private static Logger logger = LogManager.getLogger(FileContextFactory.class);

    /** {@inheritDoc} */
    @Override
    public synchronized Context getInitialContext(final Hashtable<?, ?> environment)
    {
        if (context == null)
        {
            try
            {
                URI fileURI = new URI(environment.get("java.naming.provider.url").toString());
                File file = new File(fileURI);
                if (file.exists())
                {
                    ObjectInputStream stream =
                            new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
                    FileContextFactory.context = (FileContext) stream.readObject();
                    stream.close();
                }
                else
                {
                    FileContextFactory.context = new FileContext(file);
                }
            }
            catch (Exception exception)
            {
                logger.warn("getInitialContext", exception);
            }
        }
        return context;
    }
}
