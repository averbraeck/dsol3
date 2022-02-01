package nl.tudelft.simulation.dsol.animation.gis.osm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.djutils.io.URLResource;
import org.openstreetmap.osmosis.core.task.v0_6.RunnableSource;
import org.openstreetmap.osmosis.xml.common.CompressionMethod;
import org.openstreetmap.osmosis.xml.v0_6.XmlReader;

import crosby.binary.osmosis.OsmosisReader;
import nl.tudelft.simulation.dsol.animation.gis.DataSourceInterface;
import nl.tudelft.simulation.dsol.animation.gis.FeatureInterface;
import nl.tudelft.simulation.dsol.animation.gis.transform.CoordinateTransform;

/**
 * OsmFileReader reads one layer from an OpenStreetMap file based on the given specifications. The supported formats are pbf,
 * osm, osm.gz, and osm.bz2.
 * <p>
 * Copyright (c) 2021-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class OsmFileReader implements DataSourceInterface
{
    /** */
    private static final long serialVersionUID = 20220130L;

    /** the URL for the osm file to be read. */
    private URL osmURL = null;

    /** an optional transformation of the lat/lon (or other) coordinates. */
    private final CoordinateTransform coordinateTransform;

    /** the features to read by this OpenStreeetMap reader. */
    private final List<FeatureInterface> featuresToRead;

    /**
     * Constructs a new reader for a layer in an OSM shape file.
     * @param osmURL URL; URL can have several valid extensions: .pbf, .osm, .osm.gz, and .osm.bz2
     * @param coordinateTransform CoordinateTransform; the transformation of (x, y) coordinates to (x', y') coordinates.
     * @param featuresToRead the features to read
     * @throws IOException throws an IOException if the url is not accessible
     */
    public OsmFileReader(final URL osmURL, final CoordinateTransform coordinateTransform,
            final List<FeatureInterface> featuresToRead) throws IOException
    {
        this.osmURL = osmURL;
        this.coordinateTransform = coordinateTransform;
        this.featuresToRead = featuresToRead;
    }
    
    /** {@inheritDoc} */
    @Override
    public List<FeatureInterface> getFeatures()
    {
        return this.featuresToRead;
    }

    /** {@inheritDoc} */
    @Override
    public void populateShapes() throws IOException
    {
        String filename = this.osmURL.toString();
        InputStream fis = URLResource.getResourceAsStream(filename);

        OsmLayerSink sinkImplementation = new OsmLayerSink(this.featuresToRead, this.coordinateTransform);
        CompressionMethod compression = CompressionMethod.None;
        boolean pbf = false;
        RunnableSource reader = null;

        if (filename.endsWith(".pbf"))
        {
            pbf = true;
        }
        else if (filename.endsWith(".gz"))
        {
            compression = CompressionMethod.GZip;
        }
        else if (filename.endsWith(".bz2"))
        {
            compression = CompressionMethod.BZip2;
        }

        if (pbf)
        {
            try
            {
                reader = new OsmosisReader(fis);
                System.out.println("osm map to read: " + filename);
            }
            catch (Exception exception)
            {
                throw new IOException("Error during reading of OSM file " + filename, exception);
            }
        }
        else
        {
            File file = null;
            try
            {
                file = new File(URLResource.getResource(filename).toURI());
            }
            catch (Exception e)
            {
                // if it fails, try to get the file in the current root.
                file = new File(getExecutionPath() + filename);
            }
            reader = new XmlReader(file, false, compression);
            System.out.println("osm map to read: " + file.getAbsolutePath());
        }

        reader.setSink(sinkImplementation);

        Thread readerThread = new Thread(reader);
        readerThread.start();

        while (readerThread.isAlive())
        {
            try
            {
                readerThread.join();
            }
            catch (InterruptedException e)
            {
                System.err.println("The map reader thread got a problem!");
                throw new IOException(e);
            }
        }

        System.out.println("OSM layer has been read");
    }

    /**
     * @return Execution Path
     */
    private String getExecutionPath()
    {
        String absolutePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
        absolutePath = absolutePath.replaceAll("%20", " ");
        return absolutePath;
    }

    /** {@inheritDoc} */
    @Override
    public URL getURL()
    {
        return this.osmURL;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDynamic()
    {
        return false; // OSM data is static
    }

}
