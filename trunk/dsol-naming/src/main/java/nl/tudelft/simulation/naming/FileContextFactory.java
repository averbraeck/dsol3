package nl.tudelft.simulation.naming;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URI;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;

import org.djutils.logger.CategoryLogger;

/**
 * A factory for FileContext instances, automatically invoked by JNDI when the correct jndi.properties file has been
 * used.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class FileContextFactory implements InitialContextFactory
{
    /** context refers to the static JVMContext. */
    private static FileContext context = null;

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
                CategoryLogger.always().warn(exception, "getInitialContext");
            }
        }
        return context;
    }
}
