package nl.tudelft.dsol.introspection.beans;

import java.awt.Color;
import java.awt.Font;

/**
 * Test bean for testing introspection of simple and composite color and font properties.
 * <p>
 * (c) 2002-2018-2004 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang </a><a
 *         href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.1 Apr 15, 2004
 * @since 1.5
 */
public class GUIBean
{
    /** the color to use. */
    private Color color = Color.YELLOW;

    /** the font to use. */
    private Font font = new Font("Arial", Font.BOLD, 11);

    /** the colorSet. */
    private Color[] colorSet = new Color[]{Color.BLACK, Color.BLUE};

    /**
     * @return the Color
     */
    public Color getColor()
    {
        return this.color;
    }

    /**
     * @return the Font
     */
    public Font getFont()
    {
        return this.font;
    }

    /**
     * @param color the color of the bean
     */
    public void setColor(final Color color)
    {
        this.color = color;
    }

    /**
     * sets the font
     * @param font the font
     */
    public void setFont(final Font font)
    {
        this.font = font;
    }

    /**
     * returns the colorSet
     * @return Color[]
     */
    public Color[] getColorSet()
    {
        return this.colorSet;
    }

    /**
     * sets the colorset.
     * @param colorSet the colorSet
     */
    public void setColorSet(final Color[] colorSet)
    {
        this.colorSet = colorSet;
    }

}