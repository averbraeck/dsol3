/**
 * Provides classes and interfaces for streams used in the JSTATS package. The differences between these generators is
 * expressed by their quality criteria as expressed by (Knuth, 1980):
 * <ul type="circle">
 * <li>the computational speed of the algoritm used
 * <li>the period of recurrence
 * </ul>
 * The following table illustrates the streams implemented in the JSTATS package. The speed is computed on a 32-bit
 * Intel based Pentium processor on JDK1.4.2_04 by drawing 10<sup>7</sup> double values from the particular stream.
 * <table cellspacing="2" cellpadding="2" border="1" summary="speeds of the streams">
 * <tr>
 * <td></td>
 * <td>SPEED (milliseconds)</td>
 * <td>PERIOD</td>
 * </tr>
 * <tr>
 * <td>LC64Generator</td>
 * <td>1292</td>
 * <td>1.8 x 10<sup>19</sup></td>
 * </tr>
 * <tr>
 * <td>Java2Random</td>
 * <td>1452</td>
 * <td>2.8 x 10<sup>14</sup></td>
 * </tr>
 * <tr>
 * <td>DX-120</td>
 * <td>1703</td>
 * <td>0.679 x 10<sup>1120</sup></td>
 * </tr>
 * <tr>
 * <td>MersenneTwister</td>
 * <td>2153</td>
 * <td>1 x 10<sup>6001.6</sup></td>
 * </tr>
 * </table>
 */
package nl.tudelft.simulation.jstats.streams;
