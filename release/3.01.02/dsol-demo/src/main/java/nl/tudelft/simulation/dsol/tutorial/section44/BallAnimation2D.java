package nl.tudelft.simulation.dsol.tutorial.section44;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.animation.D2.Renderable2D;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * The Animation of a Ball.
 * <p>
 * (c) copyright 2002-2016 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Apr 16, 2004
 * @since 1.5
 */
public class BallAnimation2D extends Renderable2D
{
    /** the color of the ballAnimation. */
    private Color color = Color.ORANGE;

    /**
     * constructs a new BallAnimation2D.
     * @param source the source
     * @param simulator the simulator
     */
    public BallAnimation2D(final Locatable source, final SimulatorInterface.TimeDouble simulator)
    {
        super(source, simulator);
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics2D graphics, final ImageObserver observer)
    {
        graphics.setColor(this.color);
        graphics.fillOval(-(int) Ball.RADIUS, -(int) Ball.RADIUS, (int) (Ball.RADIUS * 2.0), (int) (Ball.RADIUS * 2.0));
        graphics.setFont(graphics.getFont().deriveFont(Font.BOLD));
        graphics.setColor(Color.GRAY);
        graphics.drawString(this.source.toString(), (int) (Ball.RADIUS * -1.0), (int) (Ball.RADIUS * 1.0));
    }

    /**
     * @return Returns the color.
     */
    public Color getColor()
    {
        return this.color;
    }

    /**
     * @param color The color to set.
     */
    public void setColor(final Color color)
    {
        this.color = color;
    }

}
