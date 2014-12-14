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

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;


/**
* This is the principal class of the server graphic interface,
* consist in create the frame with all options (points 1 to 6)
* between figure select eAdventureEditor.exe, project directory
* and connect with server among others
* 
* Furthermore contains the launcher.
*
* @author Jose Ignacio Madan Frias
* @version 1.0 December 2, 2014
* 
*/

public class Main extends JFrame {

	private static final long serialVersionUID = -1709410489077586729L;
	private JPanel contentPane;
	private JFileChooser fileChooser = new JFileChooser();
	private JFileChooser directoryChooser = new JFileChooser();
	private String project_dir_path = "";
	public static JTextArea serverTextArea = new JTextArea();
	private JScrollPane scroll = new JScrollPane(serverTextArea);
	private JTextArea textArea = new JTextArea();
	private JScrollPane scroll2 = new JScrollPane(textArea);
	private JButton btnMakeProject;
	private String current_dir = "";
	private String arch = "";
	private String os = "";
	private String elevate = "";
	private String command = "";
	private String adventure = "";
	private File file;
	private String eAdventureEditor_path = "";
	private MultiThreadServer mts = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
					frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnMakeProject = new JButton("Crear Proyecto");
		btnMakeProject.setBounds(12, 55, 168, 23);
		contentPane.add(btnMakeProject);
		
		JLabel lblCreeUn = new JLabel("1. Cree un proyecto mendiante eAdventureEditor y cierrelo");
		lblCreeUn.setBounds(12, 13, 434, 21);
		contentPane.add(lblCreeUn);
		
		JLabel lblIndiqueEl = new JLabel("2. Indique el directorio del proyecto creado");
		lblIndiqueEl.setBounds(12, 105, 318, 21);
		contentPane.add(lblIndiqueEl);
		
		JButton btnDirectory = new JButton("Directorio");
		btnDirectory.setBounds(12, 150, 130, 25);
		contentPane.add(btnDirectory);
		
		JLabel lblEjecuteEl = new JLabel("3. Ejecute el servidor y pulse Enviar en la aplicacion");
		lblEjecuteEl.setBounds(12, 204, 422, 16);
		contentPane.add(lblEjecuteEl);
		
		JButton btnEjecutarServidor = new JButton("Ejecutar Servidor");
		btnEjecutarServidor.setBounds(12, 246, 202, 25);
		contentPane.add(btnEjecutarServidor);
		scroll.setBounds(12, 500, 758, 130);
		contentPane.add(scroll);
		
		JLabel lblEjecuteSu = new JLabel("4. Carge su proyecto");
		lblEjecuteSu.setBounds(12, 370, 159, 16);
		contentPane.add(lblEjecuteSu);
		
		JLabel lblPresioneCtrl = new JLabel("5. Presione Ctrl + R");
		lblPresioneCtrl.setBounds(12, 459, 159, 16);
		contentPane.add(lblPresioneCtrl);
		
		JButton btnEjecutarProyecto = new JButton("Cargar Proyecto");
		btnEjecutarProyecto.setBounds(12, 412, 173, 25);
		contentPane.add(btnEjecutarProyecto);
		
		scroll2.setBounds(12, 285, 758, 70);
		contentPane.add(scroll2);
		
		JLabel lblNewLabel = new JLabel("Copyright © 2014  Jose Ignacio Madan Frias");
		lblNewLabel.setBounds(257, 643, 355, 16);
		contentPane.add(lblNewLabel);
		
    	// Architecture
    	arch = System.getProperty("os.arch");
		if (arch.contains("86")) {
			arch = "x86";
		} else {
			arch = "x64";
		}
		
		// File & Directory chooser
		os = System.getProperty("os.name");
		directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		// Windows
		if (os.contains("Windows")) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Ejecutables", "exe");
			fileChooser.setFileFilter(filter);
			fileChooser.setCurrentDirectory(new File("/Program Files/eAdventure"));
			current_dir = System.getProperty("user.home") + "/Documents/Mis proyectos de eAdventure";
			directoryChooser.setCurrentDirectory(new File(current_dir));
		} else {
			// Linux
			fileChooser.setCurrentDirectory(new File("/usr/bin"));
			current_dir = System.getProperty("user.home") + "/Projects";
			directoryChooser.setCurrentDirectory(new File(current_dir));
		}
    	
		// Current dir
		current_dir = System.getProperty("user.dir");
		
		// Elevate
		elevate = current_dir + "\\bin\\" + arch + "\\Release\\Elevate.exe";
		
		btnMakeProject.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	if (!command.contains("eAdventureEditor") && !command.contains("eAdventure-editor")) {
			    	int seleccion = fileChooser.showOpenDialog(Main.this);
			    	if (seleccion == JFileChooser.APPROVE_OPTION) {
			    		
			    		eAdventureEditor_path = fileChooser.getSelectedFile().getAbsolutePath();
			    		
		    			command = "\"" + elevate + "\" \"" +
		    			eAdventureEditor_path + "\"";
			    		
		    			try {
			    			if (command.contains("eAdventureEditor")||command.contains("eAdventure-editor")) {
			    				// Windows
			    				if (os.contains("Windows")) {
				    				Runtime.getRuntime().exec(command);
				    			} else {
				    				// Linux
				    				Runtime.getRuntime().exec(eAdventureEditor_path);
				    			}
			    			} else {
			    				Dialog dialog = new Dialog("El ejecutable no corresponde a eAdventureEditor");
				                dialog.setModal(true);
				                dialog.setVisible(true);
			    			}
			    		} catch (Exception e1) {
			    			Dialog dialog = new Dialog("Error: " + e1.getMessage());
			                dialog.setModal(true);
			                dialog.setVisible(true);
			    		}
			    	} else if (seleccion == JFileChooser.ERROR_OPTION) {
		    			Dialog dialog = new Dialog("Error al seleccionar eAdventureEditor.exe");
		                dialog.setModal(true);
		                dialog.setVisible(true);
			    	}
		    	} else {
		    		
	    			try {
		    			// Windows
		    			if (os.contains("Windows")) {
		    				Runtime.getRuntime().exec(command);
		    			} else {
		    				// Linux
		    				Runtime.getRuntime().exec(eAdventureEditor_path);
		    			}
		    		} catch (Exception e1) {
		    			Dialog dialog = new Dialog("Error: " + e1.getMessage());
		                dialog.setModal(true);
		                dialog.setVisible(true);
		    		}
		    	}
		    }
		});
		
		btnDirectory.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	int seleccion = directoryChooser.showOpenDialog(Main.this);
		    	if (seleccion == JFileChooser.APPROVE_OPTION) {
		    		project_dir_path = directoryChooser.getSelectedFile().getAbsolutePath();
		    		adventure = project_dir_path + ".eap";
		    	} else if (seleccion == JFileChooser.ERROR_OPTION) {
	    			Dialog dialog = new Dialog("Error al seleccionar el directorio");
	                dialog.setModal(true);
	                dialog.setVisible(true);
		    	}
		    }
		});
		
		btnEjecutarServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
	    		file = new File (adventure);
	    		
				if (file.exists()) {
					if (mts != null) {
						// Stop multiThread Server
						mts.stopMultiThreadServer();
					}
					
			    	mts = new MultiThreadServer(project_dir_path);
					mts.start();
					
					String text = "En caso de no poder realizar el paso 3, ";
		    		text += "conecte el dispositivo mediante el cable usb y a continuacion ";
		    		
		    		// Windows
		    		if (os.contains("Windows")) {
		    			text += "copie los ficheros .xml que se\nencuentran dentro de la ";
		    		} else {
		    			// Linux
		    			text += "copie los ficheros\n .xml que se encuentran dentro de la ";
		    		}
		    		
		    		text += "carpera ../Projects/NombreDeSuAventura dentro de la carpeta del proyecto\n";
		    		text += project_dir_path + " y los .jpg dentro de\n";
		    		text += project_dir_path;
		    		
		    		// Windows
		    		if (os.contains("Windows")) {
		    			text += "\\assets\\background";
		    		} else {
		    			// Linux
		    			text += "/assets/background";
		    		}
		    		
		    		textArea.setText(text);
		    		
				} else {
					Dialog dialog = new Dialog("No existe la aventura en el directorio seleccionado.");
	                dialog.setModal(true);
	                dialog.setVisible(true);
				}
		    }
		});
		
		btnEjecutarProyecto.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	btnMakeProject.doClick();
		    }
		});
	}
}
