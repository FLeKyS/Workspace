/*
 *  Copyright (C) 2014  Jose Ignacio Madan Frias

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
*/
package project.elearnt.server;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;


/**
* This is class contains the custom dialog, who put preview
* message get
*
* @author Jose Ignacio Madan Frias
* @version 1.0 October 31, 2014
* 
*/

public class Dialog extends JDialog {

	private static final long serialVersionUID = 4919927320681060350L;
	private final JPanel contentPanel = new JPanel();
	private JTextArea textArea;
	private JScrollPane scroll;

	/**
	 * Create the dialog.
	 * 
	 * @param text error show in dialog
	 * 
	 */
	public Dialog(String text) {
		setBounds(100, 100, 773, 406);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		textArea = new JTextArea(text);
		scroll = new JScrollPane(textArea);
		scroll.setBounds(103, 55, 538, 40);
		
		contentPanel.add(scroll);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
	}
}
