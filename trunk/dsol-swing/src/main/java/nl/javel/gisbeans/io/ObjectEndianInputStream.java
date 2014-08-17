/*
 * ObjectEndianInputStream
 * 
 * Created on 25 april 2001, 22:36 Last edited on October 2001
 */
package nl.javel.gisbeans.io;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class enables the object inputstream to be switched from little to big endian. The class works exactly like an
 * ObjectInputStream
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a><a href="mailto:peter.jacobs@javel.nl">Peter Jacobs
 *         </a>
 * @version 1.0
 * @since JDK 1.0
 */
public class ObjectEndianInputStream implements EndianInterface, DataInput
{

    /** the datainput stream */
    private DataInputStream dataInputStream;

    /** the inputStream */
    private InputStream inputStream;

    /** an 8byte buffer */
    private byte[] buffer = new byte[8];

    /** the code */
    private int encode = EndianInterface.BIG_ENDIAN;

    /**
     * constructs a new ObjectEndianInputStream
     * @param inputStream the inputStream to use
     */
    public ObjectEndianInputStream(final InputStream inputStream)
    {
        this.inputStream = inputStream;
        this.dataInputStream = new DataInputStream(inputStream);
    }

    /**
     * @see java.io.DataInput#readShort()
     */
    public final short readShort() throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            return this.dataInputStream.readShort();
        }
        this.dataInputStream.readFully(this.buffer, 0, 2);
        return (short) ((this.buffer[1] & 0xff) << 8 | (this.buffer[0] & 0xff));
    }

    /**
     * @see java.io.DataInput#readUnsignedShort()
     */
    public final int readUnsignedShort() throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            return this.dataInputStream.readUnsignedShort();
        }
        this.dataInputStream.readFully(this.buffer, 0, 2);
        return ((this.buffer[1] & 0xff) << 8 | (this.buffer[0] & 0xff));
    }

    /**
     * @see java.io.DataInput#readChar()
     */
    public final char readChar() throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            return this.dataInputStream.readChar();
        }
        this.dataInputStream.readFully(this.buffer, 0, 2);
        return (char) ((this.buffer[1] & 0xff) << 8 | (this.buffer[0] & 0xff));
    }

    /**
     * @see java.io.DataInput#readInt()
     */
    public final int readInt() throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            return this.dataInputStream.readInt();
        }
        this.dataInputStream.readFully(this.buffer, 0, 4);
        return (this.buffer[3]) << 24 | (this.buffer[2] & 0xff) << 16 | (this.buffer[1] & 0xff) << 8
                | (this.buffer[0] & 0xff);
    }

    /**
     * @see java.io.DataInput#readLong()
     */
    public final long readLong() throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            return this.dataInputStream.readLong();
        }
        this.dataInputStream.readFully(this.buffer, 0, 8);
        return (long) (this.buffer[7]) << 56 | (long) (this.buffer[6] & 0xff) << 48
                | (long) (this.buffer[5] & 0xff) << 40 | (long) (this.buffer[4] & 0xff) << 32
                | (long) (this.buffer[3] & 0xff) << 24 | (long) (this.buffer[2] & 0xff) << 16
                | (long) (this.buffer[1] & 0xff) << 8 | (this.buffer[0] & 0xff);
    }

    /**
     * reads a float from the stream
     * @see java.io.DataInput#readFloat()
     */
    public final float readFloat() throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            return this.dataInputStream.readFloat();
        }
        return Float.intBitsToFloat(readInt());
    }

    /**
     * @see java.io.DataInput#readDouble()
     */
    public final double readDouble() throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            return this.dataInputStream.readDouble();
        }
        return Double.longBitsToDouble(readLong());
    }

    /**
     * reads b from the stream
     * @param b
     * @return in the value
     * @throws IOException on failure
     */
    public final int read(final byte[] b) throws IOException
    {
        return this.inputStream.read(b);
    }

    /**
     * reads b from the stream
     * @param b
     * @param off
     * @param len
     * @return in the value
     * @throws IOException on failure
     */
    public final int read(final byte[] b, final int off, final int len) throws IOException
    {
        return this.inputStream.read(b, off, len);
    }

    /**
     * @see java.io.DataInput#readFully(byte[])
     */
    public final void readFully(final byte[] b) throws IOException
    {
        this.dataInputStream.readFully(b, 0, b.length);
    }

    /**
     * @see java.io.DataInput#readFully(byte[], int, int)
     */
    public final void readFully(final byte[] b, final int off, final int len) throws IOException
    {
        this.dataInputStream.readFully(b, off, len);
    }

    /**
     * @see java.io.DataInput#skipBytes(int)
     */
    public final int skipBytes(final int n) throws IOException
    {
        return this.dataInputStream.skipBytes(n);
    }

    /**
     * @see java.io.DataInput#readBoolean()
     */
    public final boolean readBoolean() throws IOException
    {
        return this.dataInputStream.readBoolean();
    }

    /**
     * @see java.io.DataInput#readByte()
     */
    public final byte readByte() throws IOException
    {
        return this.dataInputStream.readByte();
    }

    /**
     * @see java.io.DataInput#readUnsignedByte()
     */
    public final int readUnsignedByte() throws IOException
    {
        return this.dataInputStream.readUnsignedByte();
    }

    /**
     * @see java.io.DataInput#readUTF()
     */
    public final String readUTF() throws IOException
    {
        return this.dataInputStream.readUTF();
    }

    /**
     * @see java.io.DataInput#readLine()
     */
    public final String readLine()
    {
        return null; // This method is deprecated because it does not work
        // OK...
    }

    /**
     * reads UTF from the stream
     * @param dataInput
     * @return String the value
     * @throws IOException
     */
    public static final String readUTF(final DataInput dataInput) throws IOException
    {
        return DataInputStream.readUTF(dataInput);
    }

    /**
     * @throws IOException
     */
    public final void close() throws IOException
    {
        this.dataInputStream.close();
    }

    /**
     * @see nl.javel.gisbeans.io.EndianInterface#setEncode(int)
     */
    public void setEncode(final int encode)
    {
        this.encode = encode;
    }

    /**
     * @see nl.javel.gisbeans.io.EndianInterface#getEncode()
     */
    public int getEncode()
    {
        return this.encode;
    }
}