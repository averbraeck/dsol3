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
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a><a href="mailto:peter.jacobs@javel.nl">Peter Jacobs </a>
 * @since JDK 1.0
 */
public class ObjectEndianInputStream implements EndianInterface, DataInput
{

    /** the datainput stream. */
    private DataInputStream dataInputStream;

    /** the inputStream. */
    private InputStream inputStream;

    /** an 8byte buffer. */
    private byte[] buffer = new byte[8];

    /** the code. */
    private int encode = EndianInterface.BIG_ENDIAN;

    /**
     * constructs a new ObjectEndianInputStream
     * @param inputStream InputStream; the inputStream to use
     */
    public ObjectEndianInputStream(final InputStream inputStream)
    {
        this.inputStream = inputStream;
        this.dataInputStream = new DataInputStream(inputStream);
    }

    /** {@inheritDoc} */
    @Override
    public final short readShort() throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            return this.dataInputStream.readShort();
        }
        this.dataInputStream.readFully(this.buffer, 0, 2);
        return (short) ((this.buffer[1] & 0xff) << 8 | (this.buffer[0] & 0xff));
    }

    /** {@inheritDoc} */
    @Override
    public final int readUnsignedShort() throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            return this.dataInputStream.readUnsignedShort();
        }
        this.dataInputStream.readFully(this.buffer, 0, 2);
        return ((this.buffer[1] & 0xff) << 8 | (this.buffer[0] & 0xff));
    }

    /** {@inheritDoc} */
    @Override
    public final char readChar() throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            return this.dataInputStream.readChar();
        }
        this.dataInputStream.readFully(this.buffer, 0, 2);
        return (char) ((this.buffer[1] & 0xff) << 8 | (this.buffer[0] & 0xff));
    }

    /** {@inheritDoc} */
    @Override
    public final int readInt() throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            return this.dataInputStream.readInt();
        }
        this.dataInputStream.readFully(this.buffer, 0, 4);
        return (this.buffer[3]) << 24 | (this.buffer[2] & 0xff) << 16 | (this.buffer[1] & 0xff) << 8 | (this.buffer[0] & 0xff);
    }

    /** {@inheritDoc} */
    @Override
    public final long readLong() throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            return this.dataInputStream.readLong();
        }
        this.dataInputStream.readFully(this.buffer, 0, 8);
        return (long) (this.buffer[7]) << 56 | (long) (this.buffer[6] & 0xff) << 48 | (long) (this.buffer[5] & 0xff) << 40
                | (long) (this.buffer[4] & 0xff) << 32 | (long) (this.buffer[3] & 0xff) << 24
                | (long) (this.buffer[2] & 0xff) << 16 | (long) (this.buffer[1] & 0xff) << 8 | (this.buffer[0] & 0xff);
    }

    /** {@inheritDoc} */
    @Override
    public final float readFloat() throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            return this.dataInputStream.readFloat();
        }
        return Float.intBitsToFloat(readInt());
    }

    /** {@inheritDoc} */
    @Override
    public final double readDouble() throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            return this.dataInputStream.readDouble();
        }
        return Double.longBitsToDouble(readLong());
    }

    /**
     * reads b from the stream.
     * @param b byte[]; byte
     * @return in the value
     * @throws IOException on failure
     */
    public final int read(final byte[] b) throws IOException
    {
        return this.inputStream.read(b);
    }

    /**
     * reads b from the stream.
     * @param b byte[]; byte
     * @param off int; offset
     * @param len int; length
     * @return in the value
     * @throws IOException on failure
     */
    public final int read(final byte[] b, final int off, final int len) throws IOException
    {
        return this.inputStream.read(b, off, len);
    }

    /** {@inheritDoc} */
    @Override
    public final void readFully(final byte[] b) throws IOException
    {
        this.dataInputStream.readFully(b, 0, b.length);
    }

    /** {@inheritDoc} */
    @Override
    public final void readFully(final byte[] b, final int off, final int len) throws IOException
    {
        this.dataInputStream.readFully(b, off, len);
    }

    /** {@inheritDoc} */
    @Override
    public final int skipBytes(final int n) throws IOException
    {
        return this.dataInputStream.skipBytes(n);
    }

    /** {@inheritDoc} */
    @Override
    public final boolean readBoolean() throws IOException
    {
        return this.dataInputStream.readBoolean();
    }

    /** {@inheritDoc} */
    @Override
    public final byte readByte() throws IOException
    {
        return this.dataInputStream.readByte();
    }

    /** {@inheritDoc} */
    @Override
    public final int readUnsignedByte() throws IOException
    {
        return this.dataInputStream.readUnsignedByte();
    }

    /** {@inheritDoc} */
    @Override
    public final String readUTF() throws IOException
    {
        return this.dataInputStream.readUTF();
    }

    /** {@inheritDoc} */
    @Override
    public final String readLine()
    {
        return null; // This method is deprecated because it does not work OK..
    }

    /**
     * reads UTF from the stream.
     * @param dataInput DataInput; data input
     * @return String the value
     * @throws IOException on read failure
     */
    public static final String readUTF(final DataInput dataInput) throws IOException
    {
        return DataInputStream.readUTF(dataInput);
    }

    /**
     * @throws IOException on close failure
     */
    public final void close() throws IOException
    {
        this.dataInputStream.close();
    }

    /** {@inheritDoc} */
    @Override
    public void setEncode(final int encode)
    {
        this.encode = encode;
    }

    /** {@inheritDoc} */
    @Override
    public int getEncode()
    {
        return this.encode;
    }
}
