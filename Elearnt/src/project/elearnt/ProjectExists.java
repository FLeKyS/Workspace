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
package project.elearnt;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


/**
* This class responsible for correct the before projects creates
* between the options figures create again, modify (name and graphic mode),
* delete and send
*
* @author Jose Ignacio Madan Frias
* @version 1.0 November 27, 2014
* 
*/

public class ProjectExists extends Activity {
	
	// Spinner
	private Spinner spinner1;
	private Spinner spinner2;
	private ArrayList<String> list = new ArrayList<String>();
	private String firstElement;
	private String graphic_mode = "";
	private File dir;
	private String nameProject;
	private boolean create = false;;
	
	// Button
	private Button continues;
	
	// Edit text
	private EditText editText1;
	
	// Path
	private String path_dir = "";
	private String path_projects = "";
	private String[] nameProjects;
	private ArrayList<String> listProjects;
	
	// File
	private File file;
	
	// Read list Scene
	private boolean read = false;
	
	/**
	 * This method delete the directory put in the
	 * argument
	 * 
	 * @param dir the directory you want to delete
	 * 
	 */
	public void delete(File dir) {

        // List all the directory contents
		String files[] = dir.list();
	 
		if (files != null) {
			for (String temp : files) {
				// Construct the file structure
			    File file = new File(dir, temp);
		 
			    // Delete file
		        file.delete();
		    }
	 
	        // Delete empty directory
			dir.delete();
		}
	}
	
	/**
	 * List the projects names
	 * 
	 */
	public void listProjects() {
		// List projects
		dir = new File (path_projects);
		nameProjects = dir.list();
		listProjects = new ArrayList<String>();
		
		if (nameProjects != null) {
			for (int i = 0; i < nameProjects.length; i++) {
				listProjects.add(nameProjects[i]);
			}
		}
	}
	
	/**
	 * Put graphic mode used in first position to show
	 * 
	 */
	public void setGraphicsModes() {

		// Compare with previous graphic_mode
		firstElement = ListScene.getGraphicMode(path_projects + "/" + nameProject);
		
		if (!firstElement.equals("windowed")) {
			list.add("Ventana");
		} else {
			list.add(0, "Ventana");
		}
				
		if (!firstElement.equals("blackbkg")) {
			list.add("Ventana fondo en negro");
		} else {
			list.add(0, "Ventana fondo en negro");
		}
	
		if (!firstElement.equals("fullscreen")) {
			list.add("Pantalla completa (Windows)");
		} else {
			list.add(0, "Pantalla completa (Windows)");
		}
			
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
        (this, android.R.layout.simple_spinner_item, list);
         
		dataAdapter.setDropDownViewResource
		            (android.R.layout.simple_spinner_dropdown_item);
         
		spinner2.setAdapter(dataAdapter);
		
	}
	
	/**
	 * This method change the visibility of the
	 * screen components, like spinner and editText
	 * depend on the option select in the spinner 
	 * (create again, modify, delete or send)
	 * 
	 */
	public void setVisibility() {
		
		if (String.valueOf(spinner1.getSelectedItem()).equals("Crear de Nuevo")) {
			editText1.setVisibility(View.VISIBLE);
			spinner2.setVisibility(View.VISIBLE);
		} else if (String.valueOf(spinner1.getSelectedItem()).equals("Modificar")) {
			editText1.setVisibility(View.VISIBLE);
			spinner2.setVisibility(View.VISIBLE);
		} else {
			editText1.setVisibility(View.INVISIBLE);
			spinner2.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * Than itself name indicated, throw new activity
	 * 
	 * @param cls name of new activity to run
	 * 
	 */
	public void startNewActivity(Class<?> cls) {
	      Intent i = new Intent(this, cls);
	      i.putExtra("path_dir", path_dir);
	      startActivity(i);
	} 
	
	/**
	 * Create the basic configuration on the screen,
	 * with one button for continue, spinner and
	 * edit text to get the principal properties of
	 * the project, and of course spinner for select
	 * what do you want do with the project 
	 * (create again, modify, delete or send)
	 * 
	 * @param savedInstanceState for save values of the activity
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.projectexists);
		
		// Buttons
		continues = (Button)findViewById(R.id.button1);
		
		// Spinner
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		
		// Edit text
		editText1 = (EditText) findViewById(R.id.editText1);
		
		// Path
		Bundle b = getIntent().getExtras();
		path_projects = b.getString("path_dir");
		nameProject = b.getString("nameProject");
		
		setGraphicsModes();
		
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        setVisibility();
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {}
		});
		
		
        // Continue
  		continues.setOnClickListener(new View.OnClickListener() {
  			@Override
  			public void onClick(View view) {
  				create = false;
  				
  				if (String.valueOf(spinner1.getSelectedItem()).equals("Crear de Nuevo")) {
  					
  					// Value of edit text
  					String nameDir = "";
  	  				nameDir = editText1.getText().toString();
  	  				
  	  				// List projects
  	  				listProjects();
  	  				
  	  				if ((!listProjects.contains(nameDir))) {
  	  				
	  					// Value of spinner
	  	  				if (String.valueOf(spinner2.getSelectedItem()).equals("Ventana")) {
	  						graphic_mode = "windowed";
	  					} else if (String.valueOf(spinner2.getSelectedItem()).equals("Ventana fondo en negro")) {
	  						graphic_mode = "blackbkg";
	  					} else if (String.valueOf(spinner2.getSelectedItem()).equals("Pantalla completa (Windows)")) {
	  						graphic_mode = "fullscreen";
	  					}
	  					
	  					// Make directory
	  		        	path_dir = path_projects + "/" + nameProject;
	  	  				file = new File(path_dir);
	  					
	  					if (!file.mkdirs()) {
	  	  	  				// Delete directory
	  						delete(file);
	  					}
	  					
		  	  			if ((!nameDir.equals("")) && (!nameDir.equals(nameProject))) {
		  	  				// Make new directory
							nameProject = nameDir;
							path_dir = path_projects + "/" + nameProject;
		  	  				file = new File(path_dir);
		  	  				file.mkdirs();
						} else {
							// Recreate directory
	  						file.mkdirs();
						}
	  					
		  	  			// Load list scenes
	  					if (!read) {
	  						ListScene.readTempListScene(path_dir + "/");
	  						read = true;
	  					}
	  					
	  					// Initial list scenes
	  					ListScene.initialListScenes();
	  					
	  					create = true;
	  					
  	  				} else {
  	  					String msg = "<font color='white'>El nombre de la aventura ya esta utilizado.</font>";
	  					Toast.makeText(getApplicationContext(), Html.fromHtml(msg), Toast.LENGTH_LONG).show();
	  				}
  	  				
  				} else if (String.valueOf(spinner1.getSelectedItem()).equals("Modificar")) {
  					
  					// Value of edit text
  					String nameDir = "";
  	  				nameDir = editText1.getText().toString();
  	  				
  	  				// List projects
  	  				listProjects();
  	  				
  	  				if ((!listProjects.contains(nameDir))) {
  	  				
	  	  				if ((!nameDir.equals("")) && (!nameDir.equals(nameProject))) {
		  	  				// Path
		  		        	path_dir = path_projects + "/";
		  	  				file = new File(path_dir);
			  				
		  					File project = new File (path_dir + nameProject);
							
							project.renameTo(new File (path_dir + nameDir));
							
							nameProject = nameDir;
						}
						
						// New path
		  	  			path_dir = path_projects + "/" + nameProject;
	
	  					// Value of spinner
			  	  		if (String.valueOf(spinner2.getSelectedItem()).equals("Ventana")) {
	  						graphic_mode = "windowed";
	  					} else if (String.valueOf(spinner2.getSelectedItem()).equals("Ventana fondo en negro")) {
	  						graphic_mode = "blackbkg";
	  					} else if (String.valueOf(spinner2.getSelectedItem()).equals("Pantalla completa (Windows)")) {
	  						graphic_mode = "fullscreen";
	  					}
			  	  		
	  					// Load list scenes
	  					if (!read) {
	  						ListScene.readTempListScene(path_dir + "/");
	  						read = true;
	  					}
	  					
	  					// Initial list scenes
	  					if (ListScene.getListScenes() == null) {
	  						ListScene.initialListScenes();
	  					}
	  					
	  					if (graphic_mode.equals(firstElement)) {
	  						startNewActivity(Scenes.class);
	  					} else {
	  						create = true;
	  					}
	  					
	  				} else {
	  					String msg = "<font color='white'>El nombre de la aventura ya esta utilizado.</font>";
	  					Toast.makeText(getApplicationContext(), Html.fromHtml(msg), Toast.LENGTH_LONG).show();
	  				}
	  	  			
  				} else if (String.valueOf(spinner1.getSelectedItem()).equals("Enviar")) {
  					// Send project to server
  					path_dir = path_projects + "/" + nameProject;
  					startNewActivity(Finish.class);
  					
  				} else {
  	  				
  		        	path_dir = path_projects + "/" + nameProject;
  	  				
  					dir = new File(path_dir);
  					delete(dir);
  					
  					finish();
  				}
  				
  				// Create descriptor.xml
  				if (create) {
			        FileWriter fileW = null;
			        PrintWriter pw = null;
			        
			        try {
			            fileW = new FileWriter(new File(path_dir, "descriptor.xml"));
			            pw = new PrintWriter(fileW);
			 
			            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			            pw.println("<!DOCTYPE game-descriptor SYSTEM \"descriptor.dtd\">");
			            pw.println("<game-descriptor versionNumber=\"25\">");
			            pw.println("\t<title>" + nameProject + "</title>");
			            pw.println("\t<description/>");
			            pw.println("\t<configuration defaultClickAction=\"showDetails\" dragBehaviour=\"considerNonTargets\" keepShowing=\"no\" keyboard-navigation=\"disabled\" perspective=\"regular\">");
			            pw.println("\t\t<gui inventoryPosition=\"none\" type=\"contextual\"/>");
			            pw.println("\t\t<mode playerTransparent=\"yes\"/>");
			            pw.println("\t\t<graphics mode=\"" + graphic_mode + "\"/>");
			            pw.println("\t</configuration>");
			            pw.println("\t<contents>");
			            pw.println("\t\t<chapter path=\"chapter1.xml\">");
			            pw.println("\t\t\t<title>Capitulo 1</title>");
			            pw.println("\t\t\t<description/>");
			            pw.println("\t\t</chapter>");
			            pw.println("\t</contents>");
			            pw.println("</game-descriptor>");
			 
			        } catch (Exception e) {
			            e.printStackTrace();
			            
			        } finally {
			        	try {
			        		// Close file
			        		if (null != fileW) {
			        			fileW.close();
			        			startNewActivity(Scenes.class);
			        		}
			        		
			        	} catch (Exception e2) {}
			        }  
  				}    
  			}
  		});
	}
	
	/**
	 * Update Spinner when modifies graphic mode
	 * 
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		
		if (((String.valueOf(spinner1.getSelectedItem()).equals("Modificar")) || 
				(String.valueOf(spinner1.getSelectedItem()).equals("Crear de Nuevo"))) &&
				(!graphic_mode.equals(firstElement))) {
			
			list.clear();
			setGraphicsModes();
		}
	}

}
