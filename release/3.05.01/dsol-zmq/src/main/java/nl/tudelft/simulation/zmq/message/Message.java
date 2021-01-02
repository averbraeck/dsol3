package nl.tudelft.simulation.zmq.message;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

import nl.tudelft.simulation.zmq.ZeroMQException;

/**
 * Message conversions. These take into account the endianness of the different values. Java is by default big-endian.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class Message
{
    /** byte, 8 bit signed two's complement integer. */
    protected static final byte BYTE_8 = 0;

    /** short, 16 bit signed two's complement integer. */
    protected static final byte SHORT_16_BIG_ENDIAN = 1;

    /** int, 32 bit signed two's complement integer. */
    protected static final byte INT_32_BIG_ENDIAN = 2;

    /** long, 64 bit signed two's complement integer. */
    protected static final byte LONG_64_BIG_ENDIAN = 3;

    /** float, single-precision 32-bit IEEE 754 floating point. */
    protected static final byte FLOAT_32_BIG_ENDIAN = 4;

    /** float, double-precision 64-bit IEEE 754 floating point. */
    protected static final byte DOUBLE_64_BIG_ENDIAN = 5;

    /** boolean, sent / received as a byte; 0 = false, 1 = true. */
    protected static final byte BOOLEAN_8 = 6;

    /** char, 16-bit Unicode character. */
    protected static final byte CHAR_16_BIG_ENDIAN = 7;

    /** String, int-preceded byte array of 8-bits characters. */
    protected static final byte STRING_8_BIG_ENDIAN = 8;

    /** String, int-preceded char array of 16-bits characters. */
    protected static final byte STRING_16_BIG_ENDIAN = 9;

    /** int-preceded byte array. */
    protected static final byte BYTE_8_ARRAY_BIG_ENDIAN = 10;

    /** short, 16 bit signed two's complement integer. */
    protected static final byte SHORT_16_LITTLE_ENDIAN = 21;

    /** int, 32 bit signed two's complement integer. */
    protected static final byte INT_32_LITTLE_ENDIAN = 22;

    /** long, 64 bit signed two's complement integer. */
    protected static final byte LONG_64_LITTLE_ENDIAN = 23;

    /** float, single-precision 32-bit IEEE 754 floating point. */
    protected static final byte FLOAT_32_LITTLE_ENDIAN = 24;

    /** float, double-precision 64-bit IEEE 754 floating point. */
    protected static final byte DOUBLE_64_LITTLE_ENDIAN = 25;

    /** char, 16-bit Unicode character. */
    protected static final byte CHAR_16_LITTLE_ENDIAN = 27;

    /** String, int-preceded byte array of 8-bits characters. */
    protected static final byte STRING_8_LITTLE_ENDIAN = 28;

    /** String, int-preceded char array of 16-bits characters. */
    protected static final byte STRING_16_LITTLE_ENDIAN = 29;

    /** int-preceded byte array. */
    protected static final byte BYTE_8_ARRAY_LITTLE_ENDIAN = 30;

    /** hashcode of Byte class. */
    protected static final int BYTE_HC = Byte.class.hashCode();

    /** hashcode of Short class. */
    protected static final int SHORT_HC = Short.class.hashCode();

    /** hashcode of Integer class. */
    protected static final int INTEGER_HC = Integer.class.hashCode();

    /** hashcode of Long class. */
    protected static final int LONG_HC = Long.class.hashCode();

    /** hashcode of Float class. */
    protected static final int FLOAT_HC = Float.class.hashCode();

    /** hashcode of Double class. */
    protected static final int DOUBLE_HC = Double.class.hashCode();

    /** hashcode of Boolean class. */
    protected static final int BOOLEAN_HC = Boolean.class.hashCode();

    /** hashcode of Character class. */
    protected static final int CHAR_HC = Character.class.hashCode();

    /** hashcode of String class. */
    protected static final int STRING_HC = String.class.hashCode();

    /** hashcode of byte[] class. */
    protected static final int BYTE_ARRAY_HC = byte[].class.hashCode();

    /** the UTF-8 charset. */
    protected static final Charset UTF8 = Charset.forName("UTF-8");

    /** the UTF-16 charset. */
    protected static final Charset UTF16 = Charset.forName("UTF-16");

    /**
     * Encode the object array into a message.
     * @param content Object[]; the objects to encode
     * @return the zeroMQ message to send as a byte array
     * @throws ZeroMQException on unknown data type
     */
    public static byte[] encode(final Object[] content) throws ZeroMQException
    {
        int size = 5; // int type + number of fields
        for (int i = 0; i < content.length; i++)
        {
            size++; // for the field type
            int hc = content[i].getClass().hashCode();
            if (hc == BYTE_HC)
                size += 1;
            else if (hc == SHORT_HC)
                size += 2;
            else if (hc == INTEGER_HC)
                size += 4;
            else if (hc == LONG_HC)
                size += 8;
            else if (hc == FLOAT_HC)
                size += 4;
            else if (hc == BOOLEAN_HC)
                size += 1;
            else if (hc == DOUBLE_HC)
                size += 8;
            else if (hc == CHAR_HC)
                size += 2;
            else if (hc == STRING_HC)
                size += ((String) content[i]).length() + 4;
            else if (hc == BYTE_ARRAY_HC)
                size += ((byte[]) content[i]).length + 4;
            else
                throw new ZeroMQException("Unknown data type " + content[i].getClass() + " for encoding the ZeroMQ message");
        }

        byte[] message = new byte[size];
        int pointer = 0;

        // BIG ENDIAN ENCODING

        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN))
        {
            int nrFields = content.length;
            message[pointer++] = INT_32_BIG_ENDIAN;
            message[pointer++] = (byte) ((nrFields >> 24) & 0xFF);
            message[pointer++] = (byte) ((nrFields >> 16) & 0xFF);
            message[pointer++] = (byte) ((nrFields >> 8) & 0xFF);
            message[pointer++] = (byte) (nrFields & 0xFF);

            for (int i = 0; i < content.length; i++)
            {
                Object field = content[i];
                int hc = field.getClass().hashCode();
                if (hc == BYTE_HC)
                {
                    message[pointer++] = BYTE_8;
                    message[pointer++] = (byte) field;
                }
                else if (hc == SHORT_HC)
                {
                    message[pointer++] = SHORT_16_BIG_ENDIAN;
                    short v = (short) field;
                    message[pointer++] = (byte) (v >> 8);
                    message[pointer++] = (byte) (v);
                }
                else if (hc == INTEGER_HC)
                {
                    message[pointer++] = INT_32_BIG_ENDIAN;
                    int v = (int) field;
                    message[pointer++] = (byte) ((v >> 24) & 0xFF);
                    message[pointer++] = (byte) ((v >> 16) & 0xFF);
                    message[pointer++] = (byte) ((v >> 8) & 0xFF);
                    message[pointer++] = (byte) (v & 0xFF);
                }
                else if (hc == LONG_HC)
                {
                    message[pointer++] = LONG_64_BIG_ENDIAN;
                    long v = (long) field;
                    message[pointer++] = (byte) ((v >> 56) & 0xFF);
                    message[pointer++] = (byte) ((v >> 48) & 0xFF);
                    message[pointer++] = (byte) ((v >> 40) & 0xFF);
                    message[pointer++] = (byte) ((v >> 32) & 0xFF);
                    message[pointer++] = (byte) ((v >> 24) & 0xFF);
                    message[pointer++] = (byte) ((v >> 16) & 0xFF);
                    message[pointer++] = (byte) ((v >> 8) & 0xFF);
                    message[pointer++] = (byte) (v & 0xFF);
                }
                else if (hc == FLOAT_HC)
                {
                    message[pointer++] = FLOAT_32_BIG_ENDIAN;
                    int v = Float.floatToIntBits((float) field);
                    message[pointer++] = (byte) ((v >> 24) & 0xFF);
                    message[pointer++] = (byte) ((v >> 16) & 0xFF);
                    message[pointer++] = (byte) ((v >> 8) & 0xFF);
                    message[pointer++] = (byte) (v & 0xFF);
                }
                else if (hc == DOUBLE_HC)
                {
                    message[pointer++] = DOUBLE_64_BIG_ENDIAN;
                    long v = Double.doubleToLongBits((double) field);
                    message[pointer++] = (byte) ((v >> 56) & 0xFF);
                    message[pointer++] = (byte) ((v >> 48) & 0xFF);
                    message[pointer++] = (byte) ((v >> 40) & 0xFF);
                    message[pointer++] = (byte) ((v >> 32) & 0xFF);
                    message[pointer++] = (byte) ((v >> 24) & 0xFF);
                    message[pointer++] = (byte) ((v >> 16) & 0xFF);
                    message[pointer++] = (byte) ((v >> 8) & 0xFF);
                    message[pointer++] = (byte) (v & 0xFF);
                }
                if (hc == BOOLEAN_HC)
                {
                    message[pointer++] = BOOLEAN_8;
                    message[pointer++] = (byte) ((boolean) field ? 1 : 0);
                }
                else if (hc == CHAR_HC)
                {
                    message[pointer++] = CHAR_16_BIG_ENDIAN;
                    char v = (char) field;
                    message[pointer++] = (byte) (v >> 8);
                    message[pointer++] = (byte) (v);
                }
                else if (hc == STRING_HC)
                {
                    message[pointer++] = STRING_8_BIG_ENDIAN;
                    int len = ((String) field).length();
                    message[pointer++] = (byte) ((len >> 24) & 0xFF);
                    message[pointer++] = (byte) ((len >> 16) & 0xFF);
                    message[pointer++] = (byte) ((len >> 8) & 0xFF);
                    message[pointer++] = (byte) (len & 0xFF);
                    byte[] s = ((String) field).getBytes(UTF8);
                    for (byte b : s)
                    {
                        message[pointer++] = b;
                    }
                }
                else if (hc == BYTE_ARRAY_HC)
                {
                    message[pointer++] = BYTE_8_ARRAY_BIG_ENDIAN;
                    int len = ((byte[]) field).length;
                    message[pointer++] = (byte) ((len >> 24) & 0xFF);
                    message[pointer++] = (byte) ((len >> 16) & 0xFF);
                    message[pointer++] = (byte) ((len >> 8) & 0xFF);
                    message[pointer++] = (byte) (len & 0xFF);
                    for (byte b : (byte[]) field)
                    {
                        message[pointer++] = b;
                    }
                }
            }
        }

        // LITTLE ENDIAN ENCODING

        else

        {
            int nrFields = content.length;
            message[pointer++] = INT_32_LITTLE_ENDIAN;
            message[pointer++] = (byte) (nrFields & 0xFF);
            message[pointer++] = (byte) ((nrFields >> 8) & 0xFF);
            message[pointer++] = (byte) ((nrFields >> 16) & 0xFF);
            message[pointer++] = (byte) ((nrFields >> 24) & 0xFF);

            for (int i = 0; i < content.length; i++)
            {
                Object field = content[i];
                int hc = field.getClass().hashCode();
                if (hc == BYTE_HC)
                {
                    message[pointer++] = BYTE_8;
                    message[pointer++] = (byte) field;
                }
                else if (hc == SHORT_HC)
                {
                    message[pointer++] = SHORT_16_LITTLE_ENDIAN;
                    short v = (short) field;
                    message[pointer++] = (byte) (v >> 8);
                    message[pointer++] = (byte) (v);
                }
                else if (hc == INTEGER_HC)
                {
                    message[pointer++] = INT_32_LITTLE_ENDIAN;
                    int v = (int) field;
                    message[pointer++] = (byte) (v & 0xFF);
                    message[pointer++] = (byte) ((v >> 8) & 0xFF);
                    message[pointer++] = (byte) ((v >> 16) & 0xFF);
                    message[pointer++] = (byte) ((v >> 24) & 0xFF);
                }
                else if (hc == LONG_HC)
                {
                    message[pointer++] = LONG_64_LITTLE_ENDIAN;
                    long v = (long) field;
                    message[pointer++] = (byte) (v & 0xFF);
                    message[pointer++] = (byte) ((v >> 8) & 0xFF);
                    message[pointer++] = (byte) ((v >> 16) & 0xFF);
                    message[pointer++] = (byte) ((v >> 24) & 0xFF);
                    message[pointer++] = (byte) ((v >> 32) & 0xFF);
                    message[pointer++] = (byte) ((v >> 40) & 0xFF);
                    message[pointer++] = (byte) ((v >> 48) & 0xFF);
                    message[pointer++] = (byte) ((v >> 56) & 0xFF);
                }
                else if (hc == FLOAT_HC)
                {
                    message[pointer++] = FLOAT_32_LITTLE_ENDIAN;
                    int v = Float.floatToIntBits((float) field);
                    message[pointer++] = (byte) (v & 0xFF);
                    message[pointer++] = (byte) ((v >> 8) & 0xFF);
                    message[pointer++] = (byte) ((v >> 16) & 0xFF);
                    message[pointer++] = (byte) ((v >> 24) & 0xFF);
                }
                else if (hc == DOUBLE_HC)
                {
                    message[pointer++] = DOUBLE_64_LITTLE_ENDIAN;
                    long v = Double.doubleToLongBits((double) field);
                    message[pointer++] = (byte) (v & 0xFF);
                    message[pointer++] = (byte) ((v >> 8) & 0xFF);
                    message[pointer++] = (byte) ((v >> 16) & 0xFF);
                    message[pointer++] = (byte) ((v >> 24) & 0xFF);
                    message[pointer++] = (byte) ((v >> 32) & 0xFF);
                    message[pointer++] = (byte) ((v >> 40) & 0xFF);
                    message[pointer++] = (byte) ((v >> 48) & 0xFF);
                    message[pointer++] = (byte) ((v >> 56) & 0xFF);
                }
                if (hc == BOOLEAN_HC)
                {
                    message[pointer++] = BOOLEAN_8;
                    message[pointer++] = (byte) ((boolean) field ? 1 : 0);
                }
                else if (hc == CHAR_HC)
                {
                    message[pointer++] = CHAR_16_LITTLE_ENDIAN;
                    char v = (char) field;
                    message[pointer++] = (byte) (v >> 8);
                    message[pointer++] = (byte) (v);
                }
                else if (hc == STRING_HC)
                {
                    message[pointer++] = STRING_8_LITTLE_ENDIAN;
                    int len = ((String) field).length();
                    message[pointer++] = (byte) (len & 0xFF);
                    message[pointer++] = (byte) ((len >> 8) & 0xFF);
                    message[pointer++] = (byte) ((len >> 16) & 0xFF);
                    message[pointer++] = (byte) ((len >> 24) & 0xFF);
                    byte[] s = ((String) field).getBytes(UTF8);
                    for (byte b : s)
                    {
                        message[pointer++] = b;
                    }
                }
                else if (hc == BYTE_ARRAY_HC)
                {
                    message[pointer++] = BYTE_8_ARRAY_LITTLE_ENDIAN;
                    int len = ((byte[]) field).length;
                    message[pointer++] = (byte) (len & 0xFF);
                    message[pointer++] = (byte) ((len >> 8) & 0xFF);
                    message[pointer++] = (byte) ((len >> 16) & 0xFF);
                    message[pointer++] = (byte) ((len >> 24) & 0xFF);
                    for (byte b : (byte[]) field)
                    {
                        message[pointer++] = b;
                    }
                }
            }
        }

        return message;
    }

    /**
     * Decode the message into an object array.
     * @param message byte[]; the ZeroMQ byte array to decode
     * @return an array of objects of the right type
     * @throws ZeroMQException on unknown data type
     */
    public static Object[] decode(final byte[] message) throws ZeroMQException
    {
        int pointer = 0;
        int nrFields = decodeInt(message, 0);
        pointer += 5;

        Object[] array = new Object[nrFields];
        for (int i = 0; i < nrFields; i++)
        {
            byte type = message[pointer++];

            if (type == BYTE_8)
            {
                array[i] = message[pointer];
                pointer += 1;
            }
            else if (type == SHORT_16_BIG_ENDIAN)
            {
                array[i] = (short) (((message[pointer] & 0xff) << 8) | ((message[pointer + 1] & 0xff)));
                pointer += 2;
            }
            else if (type == INT_32_BIG_ENDIAN)
            {
                array[i] = decodeIntBigEndian(message, pointer);
                pointer += 4;
            }
            else if (type == LONG_64_BIG_ENDIAN)
            {
                array[i] = decodeLongBigEndian(message, pointer);
                pointer += 8;
            }
            else if (type == FLOAT_32_BIG_ENDIAN)
            {
                int bits = decodeIntBigEndian(message, pointer);
                array[i] = Float.intBitsToFloat(bits);
                pointer += 4;
            }
            else if (type == DOUBLE_64_BIG_ENDIAN)
            {
                long bits = decodeLongBigEndian(message, pointer);
                array[i] = Double.longBitsToDouble(bits);
                pointer += 4;
            }
            else if (type == BOOLEAN_8)
            {
                array[i] = message[i] != 0;
                pointer += 1;
            }
            else if (type == CHAR_16_BIG_ENDIAN)
            {
                // TODO array[i] =
                pointer += 2;
            }
        }

        return array;
    }

    /**
     * Decode an int.
     * @param content the ZeroMQ byte array to decode
     * @param pointer int; the first byte to consider
     * @return the integer value
     * @throws ZeroMQException when data type is not integer
     */
    private static int decodeInt(final byte[] message, final int pointer) throws ZeroMQException
    {
        if (message[pointer] == INT_32_BIG_ENDIAN)
        {
            return decodeIntBigEndian(message, pointer + 1);
        }

        if (message[pointer] == INT_32_LITTLE_ENDIAN)
        {
            return decodeIntLittleEndian(message, pointer + 1);
        }
        throw new ZeroMQException("decodeInt: expected int, but got data type " + message[pointer]);
    }

    /**
     * Decode a Big Endian int.
     * @param content the ZeroMQ byte array to decode
     * @param pointer int; the first byte to consider
     * @return the integer value
     */
    private static int decodeIntBigEndian(final byte[] message, final int pointer)
    {
        return (((message[pointer] & 0xff) << 24) | ((message[pointer + 1] & 0xff) << 16) | ((message[pointer + 2] & 0xff) << 8)
                | ((message[pointer + 3] & 0xff)));
    }

    /**
     * Decode a Little Endian int.
     * @param content the ZeroMQ byte array to decode
     * @param pointer int; the first byte to consider
     * @return the integer value
     */
    private static int decodeIntLittleEndian(final byte[] message, final int pointer)
    {
        return (((message[pointer + 3] & 0xff) << 24) | ((message[pointer + 2] & 0xff) << 16)
                | ((message[pointer + 1] & 0xff) << 8) | ((message[pointer] & 0xff)));
    }

    /**
     * Decode a Big Endian long.
     * @param content the ZeroMQ byte array to decode
     * @param pointer int; the first byte to consider
     * @return the long value
     */
    private static long decodeLongBigEndian(final byte[] message, final int pointer)
    {
        return ((((long) message[pointer]) << 56) | (((long) message[pointer + 1] & 0xff) << 48)
                | (((long) message[pointer + 2] & 0xff) << 40) | (((long) message[pointer + 3] & 0xff) << 32)
                | (((long) message[pointer + 4] & 0xff) << 24) | (((long) message[pointer + 5] & 0xff) << 16)
                | (((long) message[pointer + 6] & 0xff) << 8) | (((long) message[pointer + 7] & 0xff)));
    }

    /**
     * Decode a Little Endian long.
     * @param content the ZeroMQ byte array to decode
     * @param pointer int; the first byte to consider
     * @return the long value
     */
    private static long decodeLongLittleEndian(final byte[] message, final int pointer)
    {
        return ((((long) message[pointer + 7]) << 56) | (((long) message[pointer + 6] & 0xff) << 48)
                | (((long) message[pointer + 5] & 0xff) << 40) | (((long) message[pointer + 4] & 0xff) << 32)
                | (((long) message[pointer + 3] & 0xff) << 24) | (((long) message[pointer + 2] & 0xff) << 16)
                | (((long) message[pointer + 1] & 0xff) << 8) | (((long) message[pointer] & 0xff)));
    }

}
