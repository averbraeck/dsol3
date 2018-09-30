/*
 * ObjectEndianInputStream
 * 
 * Created on 25 april 2001, 22:36 Last edited on October 2001
 */
package nl.javel.gisbeans.io;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class enables the object outputstream to be switched from little to big endian. The class works exactly like an
 * ObjectOutputStream
 * @author <a href="mailto:peter.jacobs@javel.nl">Peter Jacobs </a><a href="mailto:paul.jacobs@javel.nl">Paul Jacobs
 *         </a>
 * @since JDK 1.0
 */
public class ObjectEndianOutputStream implements EndianInterface, DataOutput
{

    /** the dataOutputStream. */
    private DataOutputStream dataOutputStream = null;

    /** the buffer. */
    private byte[] buffer = new byte[8];

    /** the encode. */
    private int encode = EndianInterface.BIG_ENDIAN;

    /**
     * constructs a new ObjectEndianOutputStream.
     * @param outputStream the target.
     */
    public ObjectEndianOutputStream(final OutputStream outputStream)
    {
        this.dataOutputStream = new DataOutputStream(outputStream);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final byte[] _buffer, final int off, final int len) throws IOException
    {
        this.dataOutputStream.write(this.buffer, off, len);
    }

    /** {@inheritDoc} */
    @Override
    public void writeFloat(final float value) throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            this.dataOutputStream.writeFloat(value);
        }
        else
        {
            this.writeInt(Float.floatToIntBits(value));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void write(final int value) throws IOException
    {
        this.dataOutputStream.write(value);
    }

    /** {@inheritDoc} */
    @Override
    public void writeShort(final int value) throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            this.dataOutputStream.writeShort(value);
        }
        else
        {
            this.buffer[0] = (byte) value;
            this.buffer[1] = (byte) (value >> 8);
            this.dataOutputStream.write(this.buffer, 0, 2);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeBytes(final String string) throws IOException
    {
        this.dataOutputStream.writeBytes(string);
    }

    /** {@inheritDoc} */
    @Override
    public void writeChar(final int value) throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            this.dataOutputStream.writeChar(value);
        }
        else
        {
            this.buffer[0] = (byte) value;
            this.buffer[1] = (byte) (value >> 8);
            this.dataOutputStream.write(this.buffer, 0, 2);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeByte(final int value) throws IOException
    {
        this.dataOutputStream.writeByte(value);
    }

    /** {@inheritDoc} */
    @Override
    public void writeBoolean(final boolean value) throws IOException
    {
        this.dataOutputStream.writeBoolean(value);
    }

    /** {@inheritDoc} */
    @Override
    public void writeLong(final long value) throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            this.dataOutputStream.writeLong(value);
        }
        else
        {
            this.buffer[0] = (byte) value;
            this.buffer[1] = (byte) (value >> 8);
            this.buffer[2] = (byte) (value >> 16);
            this.buffer[3] = (byte) (value >> 24);
            this.buffer[4] = (byte) (value >> 32);
            this.buffer[5] = (byte) (value >> 40);
            this.buffer[6] = (byte) (value >> 48);
            this.buffer[7] = (byte) (value >> 56);
            this.dataOutputStream.write(this.buffer, 0, 8);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeUTF(final String string) throws IOException
    {
        this.dataOutputStream.writeUTF(string);
    }

    /** {@inheritDoc} */
    @Override
    public void writeInt(final int value) throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            this.dataOutputStream.writeInt(value);
        }
        else
        {
            this.buffer[0] = (byte) value;
            this.buffer[1] = (byte) (value >> 8);
            this.buffer[2] = (byte) (value >> 16);
            this.buffer[3] = (byte) (value >> 24);
            this.dataOutputStream.write(this.buffer, 0, 4);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeChars(final String string) throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            this.dataOutputStream.writeChars(string);
        }
        else
        {
            int length = string.length();
            for (int i = 0; i < length; i++)
            {
                this.writeChar(string.charAt(i));
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void write(final byte[] _buffer) throws IOException
    {
        this.dataOutputStream.write(this.buffer, 0, this.buffer.length);
    }

    /** {@inheritDoc} */
    @Override
    public void writeDouble(final double value) throws IOException
    {
        if (this.encode == EndianInterface.BIG_ENDIAN)
        {
            this.dataOutputStream.writeDouble(value);
        }
        else
        {
            this.writeLong(Double.doubleToLongBits(value));
        }
    }

    /**
     * @return the size of the stream
     */
    public final int size()
    {
        return this.dataOutputStream.size();
    }

    /**
     * flushes the stream
     * @throws IOException on i/o error
     */
    public void flush() throws IOException
    {
        this.dataOutputStream.flush();
    }

    /**
     * closes the stream
     * @throws IOException on i/o error
     */
    public void close() throws IOException
    {
        this.dataOutputStream.close();
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
