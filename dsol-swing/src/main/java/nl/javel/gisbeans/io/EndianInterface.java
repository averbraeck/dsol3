/*
 * EndianInterface.java
 * 
 * Created on 29 april 2001, 22:12 Last edited on October 11 2002
 */
package nl.javel.gisbeans.io;

/**
 * This interface defines what we expect from an input or output object to handle both Little as Big Endian.
 * @author <a href="mailto:peter.jacobs@javel.nl">Peter Jacobs </a><a href="mailto:paul.jacobs@javel.nl">Paul Jacobs
 *         </a>
 * @version 1.0
 * @since JDK 1.0
 */
public interface EndianInterface extends java.io.Serializable
{
    /** BIG ENDIAN */
    byte BIG_ENDIAN = 0;

    /** LITTLE ENDIAN */
    byte LITTLE_ENDIAN = 1;

    /**
     * sets the Encode
     * @param encode the encode which is either BIG_ENDIAN or LITTLE_ENDIAN
     */
    void setEncode(int encode);

    /**
     * returns the encode
     * @return int
     */
    int getEncode();
}
