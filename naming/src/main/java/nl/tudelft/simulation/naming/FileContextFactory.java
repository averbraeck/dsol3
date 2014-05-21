/*
 * @(#)FileContextFactory.java Feb 1, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.naming;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URI;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;

import nl.tudelft.simulation.logger.Logger;

/**
 * A factory for FileContext instances, automatically invoked by JNDI when the
 * correct jndi.properties file has been used.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">
 * www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version 1.3 2004-03-24
 * @since 1.5
 */
public class FileContextFactory implements InitialContextFactory
{
    /** context refers to the static JVMContext */
    private static FileContext context = null;

    /**
     * @see javax.naming.spi.InitialContextFactory#getInitialContext( Hashtable)
     */
    public synchronized Context getInitialContext(
            final Hashtable< ? , ? > environment)
    {
        if (context == null)
        {
            try
            {
                URI fileURI = new URI(environment.get(
                        "java.naming.provider.url").toString());
                File file = new File(fileURI);
                if (file.exists())
                {
                    ObjectInputStream stream = new ObjectInputStream(
                            new BufferedInputStream(new FileInputStream(file)));
                    FileContextFactory.context = (FileContext) stream
                            .readObject();
                    stream.close();
                } else
                {
                    FileContextFactory.context = new FileContext(file);
                }
            } catch (Exception exception)
            {
                Logger.warning(this, "getInitialContext", exception);
            }
        }
        return context;
    }
}