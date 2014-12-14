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
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
* This class responsible for get the principal properties
* of the link scene name (name and text)
*
* @author Jose Ignacio Madan Frias
* @version 1.0 December 9, 2014
* 
*/

public class MakeExits extends Activity {
	
	// Path directory
	private String path_dir = "";
	
	// List the directory
	private File dir;
    private String [] nameFiles;
    private String tokens[];
    private ArrayList<String> sceneNames;
	
	// Name principal Scene
	private String name = "";
	
	// Link Node
	private String linkSceneName = "";
	int x, y, width, height;
	
	// Buttons
	private Button finish;
	private Button search;
	
	// Edit texts
	private EditText editText1;
	private EditText editText2;
	
	/**
	 * Than itself name indicated, throw new activity
	 * 
	 * The activity throw is ScenesGallery and put in the
	 * bundle path_dir and name
	 * 
	 */
	public void startNewActivity() {
		Intent i = new Intent(this, ScenesGallery.class);
		i.putExtra("path_dir", path_dir);
		i.putExtra("actualSceneName", name);
		  
		startActivity(i);
	}
	
	/**
	 * List the scene name contains in the project directory
	 * 
	 */
	private void listScenesName() {
		
		// Images names
		dir = new File(path_dir);
        
        // List the directory
        nameFiles = dir.list();
        
        // Reset
        if (sceneNames == null) {
        	sceneNames = new ArrayList<String>();
        } else {
        	sceneNames.clear();
        }
        
        // Add name files to sceneNames
        if (nameFiles != null) {
            for (int i = 0; i < nameFiles.length; i++) {
            	tokens = nameFiles[i].split("[.]");
            	
            	if ((tokens[1].equals("jpg")) && (!tokens[0].equals(name)) && (ListScene.existsNode(tokens[0]))) {
            		sceneNames.add(tokens[0]);
            	}
            }
        }
	}
	
	/**
	 * Search if exists scenes in the project
	 * 
	 * @return true if exists at least one scene in the project
	 * 
	 */
	public boolean existsScenesName() {
        
        return (sceneNames.size() != 0);
	}
	
	
	/**
	 * Obtain default name scene
	 * 
	 * @return name of the scene by default (1..N)
	 * 
	 */
	public String getDefaultName() {
		String sceneName = "-1";
		
		int i = -1;
		
		while ((sceneNames.contains(sceneName)) || (sceneName.equals(name))) {
			i--;
			sceneName = Integer.toString(i);
		}
		
		return sceneName;
	}
	
	
	/**
	 * Create the basic configuration on the screen,
	 * with two buttons for finish and search preview scene create,
	 * two edit texts to put name and text of the scene link
	 * 
	 * @param savedInstanceState for save values of the activity
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.makexits);
		
		// Path directory
		Bundle bundle = getIntent().getExtras();
	    path_dir = bundle.getString("path_dir");
	    
	    // Name of Scene
	    name = bundle.getString("name");
	    
	    // x, y, width, height
	    x = bundle.getInt("x");
	    y = bundle.getInt("y");
	    width = bundle.getInt("width");
	    height = bundle.getInt("height");
	    
	    // Edit texts
	    editText1 = (EditText) findViewById(R.id.editText1);
	    editText2 = (EditText) findViewById(R.id.editText2);

	    // Button
	    finish = (Button) findViewById(R.id.button1);
		search = (Button) findViewById(R.id.button2);
		
		// List scenes
		listScenesName();
		
		if(!existsScenesName()) {
			search.setVisibility(View.INVISIBLE);
		}
		
		search.setOnClickListener(new View.OnClickListener() {
  			@Override
  			public void onClick(View view) {
  				if (existsScenesName()) {
  					startNewActivity();
  				} else {
  					String msg = "<font color='white'>No existen escenas creadas.</font>";
	  				Toast.makeText(getApplicationContext(), Html.fromHtml(msg), Toast.LENGTH_LONG).show();
  				}
  			}
  		});
        
        finish.setOnClickListener(new View.OnClickListener() {
  			@Override
  			public void onClick(View view) {
  				if (!ScenesGallery.sceneNameSelect.equals("")) {
  					linkSceneName = ScenesGallery.sceneNameSelect;
  				} else {
  					linkSceneName = editText1.getText().toString();
  					
  					if (linkSceneName.equals("")) {
  						// Default name
  						linkSceneName = getDefaultName();
  						
  						String msg = "<font color='white'>Escena a la que enlaza por defecto: " + linkSceneName + "</font>";
  	  					Toast.makeText(getApplicationContext(), Html.fromHtml(msg), Toast.LENGTH_LONG).show();
  					}
  				}
  				
  				// Create node
  				if ((!linkSceneName.equals("")) && (!linkSceneName.equals(name))) {
  					LinkSceneNode ln = new LinkSceneNode (linkSceneName, 
  							x, y, width, height, editText2.getText().toString());
  					
  					// Add node to link scenes list
  					ListScene.getListLinkScene(name).addNode(ln);
  					
  					// Save list scenes
  				    ListScene.saveTempListScene(path_dir + "/");
  					
  					// Reset
					ScenesGallery.sceneNameSelect = "";
					
  					finish();
  				} else {
					String msg = "<font color='white'>No se puede enlazar con la misma escena.</font>";
  					Toast.makeText(getApplicationContext(), Html.fromHtml(msg), Toast.LENGTH_LONG).show();
  				}
  			}
  		});
	}
	
}
