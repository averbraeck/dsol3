/*
 * Created on Jun 16, 2003 To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package nl.tudelft.simulation.introspection.mapping;

import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jfree.ui.FontChooserDialog;

/**
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @version 1.0 02.04.2003 <br>
 * @author <a href="http://www.simulation.tudelft.nl/people/lang.html">Niels
 *         Lang </a>, <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public class MyFontChooserDialog extends FontChooserDialog
{
    /** the listeners */
    private List<UserListenerInterface> listeners = Collections
            .synchronizedList(new ArrayList<UserListenerInterface>(4));

    /**
     * @param arg0 the dialog
     * @param arg1 the title
     * @param arg2 the parent frame
     * @param arg3 the font
     */
    public MyFontChooserDialog(final Dialog arg0, final String arg1,
            final boolean arg2, final Font arg3)
    {
        super(arg0, arg1, arg2, arg3);
    }

    /**
     * @param arg0 the dialog
     * @param arg1 the title
     * @param arg2 the parent frame
     * @param arg3 the font
     */
    public MyFontChooserDialog(final Frame arg0, final String arg1,
            final boolean arg2, final Font arg3)
    {
        super(arg0, arg1, arg2, arg3);
    }

    /**
     * a userListener interface reacting on OK, cancel
     */
    public static interface UserListenerInterface
    {
        /**
         * triggered when the OKAction is performed.
         */
        public void okActionPerformed();

        /**
         * triggered when the cancel action is performed.
         */
        public void cancelActionPerformed();
    }

    /**
     * adds a user listener to the fontChooser Dialog
     * 
     * @param l the listener
     */
    public void addUserListener(final UserListenerInterface l)
    {
        this.listeners.add(l);
    }

    /**
     * removes a userListener in the fontChooser
     * 
     * @param l the listener
     */
    public void removeUserListener(final UserListenerInterface l)
    {
        this.listeners.remove(l);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent evt)
    {
        if (evt.getActionCommand().equals("okButton"))
        {
            notifyOK();
        }
        if (evt.getActionCommand().equals("cancelButton"))
        {
            notifyCancel();
        }
        super.actionPerformed(evt);
    }

    /**
     * notofies OK
     */
    private void notifyOK()
    {
        for (Iterator<UserListenerInterface> i = this.listeners.iterator(); i
                .hasNext();)
        {
            i.next().okActionPerformed();
        }
    }

    /**
     * notifies cancels
     */
    private void notifyCancel()
    {
        for (Iterator<UserListenerInterface> i = this.listeners.iterator(); i
                .hasNext();)
        {
            i.next().cancelActionPerformed();
        }
    }
}