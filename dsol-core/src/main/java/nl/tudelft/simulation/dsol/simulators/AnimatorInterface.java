package nl.tudelft.simulation.dsol.simulators;

import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.NamingException;

import nl.tudelft.simulation.event.EventType;

/**
 * The AnimatorInterface defines a DEVSDESS simulator with wallclock delay between the consequtive time steps.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @since 1.5
 */
public interface AnimatorInterface
{
    /** DEFAULT_ANIMATION_DELAY of 100 milliseconds used in the animator. */
    long DEFAULT_ANIMATION_DELAY = 100L;

    /** UPDATE_ANIMATION_EVENT is fired to wake up animatable components. */
    EventType UPDATE_ANIMATION_EVENT = new EventType("UPDATE_ANIMATION_EVENT");

    /** ANIMATION_DELAY_CHANGED_EVENT is fired when the time step is set. */
    EventType ANIMATION_DELAY_CHANGED_EVENT = new EventType("ANIMATION_DELAY_CHANGED_EVENT");

    /**
     * returns the animation delay between each consequtive animation update.
     * @return the animation delay in milliseconds of wallclock time
     * @throws RemoteException on network failure
     */
    long getAnimationDelay() throws RemoteException;

    /**
     * sets the animationDelay.
     * @param miliseconds the animation delay
     * @throws RemoteException on network failure
     */
    void setAnimationDelay(long miliseconds) throws RemoteException;

    /**
     * UpdateAnimation takes care of firing the UPDATE_ANIMATION_EVENT.
     */
    void updateAnimation();

    /**
     * <p />
     * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
     * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @version Feb 1, 2015
     */
    public class AnimationThread extends Thread
    {
        /** is the animator running? */
        private boolean running = true;

        /** the animator. */
        private final AnimatorInterface animator;

        /**
         * @param animator the animator.
         */
        public AnimationThread(final AnimatorInterface animator)
        {
            super();
            this.animator = animator;
        }

        /** {@inheritDoc} */
        @Override
        public final void run()
        {
            while (this.running)
            {
                try
                {
                    sleep(this.animator.getAnimationDelay());
                    this.animator.updateAnimation();
                }
                catch (InterruptedException exception)
                {
                    // if interrupted by stopAnimation, this.running is false and the animation stops.
                    this.running = false;
                }
                catch (RemoteException exception)
                {
                    this.running = false;
                }
            }
        }

        /**
         * Stop the animation.
         */
        public final void stopAnimation()
        {
            this.running = false;
            interrupt();
        }
    }
}
