/*
 * DbfReader.java
 * 
 * Created on October 11, 2002 Last edited on October 11, 2002
 */
package nl.javel.gisbeans.io.esri;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import nl.javel.gisbeans.io.EndianInterface;
import nl.javel.gisbeans.io.ObjectEndianInputStream;

/**
 * This class reads a dbf file (dBaseIII) used in Esri ShapeFiles
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a> <br>
 *         <a href="mailto:peter.jacobs@javel.nl">Peter Jacobs </a>
 * @since JDK 1
 * @version 1
 */
public class DbfReader implements Serializable
{
    /** the FBFFile */
    private URL dbfFile;

    /** the numberofColumns */
    private int[] columnLength;

    /** the names of the columns */
    private String[] columnNames;

    /** the headerLength */
    private int headerLength = 0;

    /** the number of columns */
    private int numColumns = 0;

    /** the number of records */
    private int numRecords = 0;

    /** the length of the records */
    private int recordLength = 0;

    /** the cachedContent */
    private transient String[][] cachedContent = null;

    /** may we cache parsed data?.. */
    private boolean cache = true;

    /**
     * constructs a DbfReader
     * @param dbfFile the URL of the dbfFile
     * @throws IOException whenever url does not occur to exist.
     */
    public DbfReader(URL dbfFile) throws IOException
    {
        this.dbfFile = dbfFile;
        ObjectEndianInputStream dbfInput = new ObjectEndianInputStream(dbfFile.openStream());
        if (dbfInput.readByte() != 3)
            throw new IOException("dbf file does not seem to be a Dbase III file");

        dbfInput.skipBytes(3);
        dbfInput.setEncode(EndianInterface.LITTLE_ENDIAN);

        this.numRecords = dbfInput.readInt();
        this.headerLength = dbfInput.readShort();
        this.numColumns = (this.headerLength - 33) / 32;
        this.recordLength = dbfInput.readShort();

        this.columnLength = new int[this.numColumns];
        this.columnNames = new String[this.numColumns];

        dbfInput.skipBytes(20);
        for (int i = 0; i < this.numColumns; i++)
        {
            StringBuffer buffer = new StringBuffer();
            for (int j = 0; j < 10; j++)
            {
                byte b = dbfInput.readByte();
                if (b > 31)
                    buffer.append((char) b);
            }
            this.columnNames[i] = buffer.toString();
            dbfInput.setEncode(EndianInterface.BIG_ENDIAN);
            dbfInput.readChar();

            dbfInput.skipBytes(4);

            this.columnLength[i] = dbfInput.readByte();
            if (this.columnLength[i] < 0)
                this.columnLength[i] += 256;
            dbfInput.skipBytes(15);
        }
        dbfInput.close();
    }

    /**
     * returns the columnNames
     * @return String[] the columnNames
     */
    public String[] getColumnNames()
    {
        return this.columnNames;
    }

    /**
     * returns the row
     * @param rowNumber the rowNumber
     * @return String[] the attributes of the row
     * @throws IOException
     * @throws IndexOutOfBoundsException whenever the rowNumber > numRecords
     */
    public String[] getRow(int rowNumber) throws IOException, IndexOutOfBoundsException
    {
        if (rowNumber > this.numRecords)
        {
            throw new IndexOutOfBoundsException("dbfFile : rowNumber > numRecords");
        }

        // Let's see if we may cache..
        if (this.cachedContent != null && this.cache)
        {
            return this.cachedContent[rowNumber];
        }

        // Either we may not cache of the cache is still empty
        String[] row = new String[this.numColumns];

        ObjectEndianInputStream dbfInput = new ObjectEndianInputStream(this.dbfFile.openStream());
        dbfInput.skipBytes(this.headerLength + 1);
        dbfInput.skipBytes(rowNumber * this.recordLength);
        for (int i = 0; i < this.numColumns; i++)
        {
            byte[] bytes = new byte[this.columnLength[i]];
            dbfInput.read(bytes);
            row[i] = new String(bytes);
        }
        dbfInput.close();
        return row;
    }

    /**
     * returns a table of all attributes stored for the particular dbf-file
     * @return String[][] a table of attributes
     * @throws IOException an IOException
     */
    public String[][] getRows() throws IOException
    {
        // Let's see if we may cache..
        if (this.cachedContent != null && this.cache)
        {
            return this.cachedContent;
        }

        String[][] result = new String[this.numRecords][this.numColumns];
        ObjectEndianInputStream dbfInput = new ObjectEndianInputStream(this.dbfFile.openStream());
        dbfInput.skipBytes(this.headerLength + 1);
        for (int row = 0; row < this.numRecords; row++)
        {
            for (int col = 0; col < this.numColumns; col++)
            {
                byte[] bytes = new byte[this.columnLength[col]];
                dbfInput.read(bytes);
                result[row][col] = new String(bytes);
                if (col == this.numColumns - 1)
                {
                    dbfInput.skipBytes(1);
                }
            }
        }
        dbfInput.close();
        if (this.cache)
        {
            this.cachedContent = result;
        }
        return result;
    }

    /**
     * returns the array of rowNumbers belonging to a attribute/column pair
     * @param attribute the attribute value
     * @param columnName the name of the column
     * @return int[] the array of shape numbers.
     * @throws IOException
     */
    public int[] getRowNumbers(String attribute, String columnName) throws IOException
    {
        ArrayList result = new ArrayList();
        String[][] rows = this.getRows();
        for (int col = 0; col < this.numColumns; col++)
        {
            if (this.columnNames[col].equals(columnName))
            {
                for (int row = 0; row < this.numRecords; row++)
                {
                    if (rows[row][col].equals(attribute))
                        result.add(new Integer(row));
                }
            }
        }
        int[] array = new int[result.size()];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = ((Integer) result.get(i)).intValue();
        }
        return array;
    }

    /**
     * may we cache parsed data for a session. If false, every getRows results in IO activity. If true data is stored
     * inMemory
     * @return Returns the cache.
     */
    public boolean isCache()
    {
        return this.cache;
    }

    /**
     * @param cache The cache to set.
     */
    public void setCache(boolean cache)
    {
        this.cache = cache;
    }
}