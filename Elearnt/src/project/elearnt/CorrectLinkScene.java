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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
* Class for correct scenes with bad exits creates
*
* @author Jose Ignacio Madan Frias
* @version 1.0 December 1, 2014
* 
*/

public class CorrectLinkScene extends Activity {
	
	// Files
	private String path_dir = "";
	private File dir = null;
	private String[] nameFiles;
	private String name = "";
	private String tokens[];
	
	// Button
	private Button finish;
	private Button delete;
	private Button search;
	
	// Edit text
	private EditText editText1;
	private String text;
	
	// Text View
	private TextView textView1;
	private TextView textView2;
	
	// Image View
	private ImageView imageView1;
	private Bitmap scaled;
	private boolean first = true;
	private int imageWidth;
	private int imageHeight;
	int x, newX, y, newY, width, newWidth, height, newHeight, left, right, top, bottom;
	
	// List Scenes
	private int idSceneLink;
	
	/**
	 * Load image previously save
	 * 
	 */
	public void loadImage() {
        
    	// Load image
    	InputStream is;
    	
		try {
			is = new FileInputStream(path_dir + "/" + name + ".jpg");
			BufferedInputStream bis = new BufferedInputStream(is);
			Bitmap image = BitmapFactory.decodeStream(bis);
			scaled = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true);
		} catch (FileNotFoundException e) {}
	}
	
	/**
	 * Search if exists scenes in the project
	 * 
	 * @return true if exists at least one scene in the project
	 * 
	 */
	public boolean existsScenesName() {
		// Check
		boolean existsSceneName = false;
		
		// Images names
		dir = new File(path_dir);
        
        // List the directory
        nameFiles = dir.list();
        
        // Add name files to sceneNames
        if (nameFiles != null) {
        	int i = 0;
            while ((i < nameFiles.length) && (!existsSceneName)) {
            	tokens = nameFiles[i].split("[.]");
            	
            	if (tokens[1].equals("jpg") && (!tokens[0].equals(name)) && (ListScene.existsNode(tokens[0]))) {
            		existsSceneName = true;
            	}
            	i++;
            }
        }
        
        return existsSceneName;
	}
	
	/**
	 * Than itself name indicate, throw new activity, 
	 * but before finish() the last activity run
	 * 
	 */
	public void startNewActivity() {
		Intent i = new Intent(this, ScenesGallery.class);
		i.putExtra("path_dir", path_dir);
		i.putExtra("actualSceneName", name);
		  
		startActivity(i);
	}
	

	/**
	 * Create the basic configuration on the screen,
	 * with three buttons (search, delete and finish),
	 * edit text to put in the exit, and image view of
	 * the scene
	 * 
	 * @param savedInstanceState for save values of the activity
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.correctlinkscene);
		
		// Path
		Bundle bundle = getIntent().getExtras();
	    path_dir = bundle.getString("path_dir");
	    
	    // Scene name (principal)
	    name = bundle.getString("name");
	    
	    // Id Link Scene
	    idSceneLink = bundle.getInt("idLinkScene");
	    
	    // Buttons
	    finish = (Button) findViewById(R.id.button1);
	    delete = (Button) findViewById(R.id.button2);
	    search = (Button) findViewById(R.id.button3);
	    
	    // Edit text
	    editText1 = (EditText) findViewById(R.id.editText1);
	    text = ListScene.getTextLinkNode(name, idSceneLink);
	    editText1.setText(text);
	    
	    // Text view
	    textView1 = (TextView) findViewById(R.id.textView1);
	    textView2 = (TextView) findViewById(R.id.textView2);
	    
	    if (!existsScenesName()) {
			finish.setVisibility(View.INVISIBLE);
			search.setVisibility(View.INVISIBLE);
			editText1.setVisibility(View.INVISIBLE);
			textView1.setVisibility(View.INVISIBLE);
			textView2.setVisibility(View.INVISIBLE);
			
			String msg = "<font color='white'>No existen escenas con las que enlazar.</font>";
			Toast.makeText(getApplicationContext(), Html.fromHtml(msg), Toast.LENGTH_LONG).show();
	    }
	    
	    // Reset
		ScenesGallery.sceneNameSelect = "";
	    
	    search.setOnClickListener(new View.OnClickListener() {
  			@Override
  			public void onClick(View view) {
  				
  				if (existsScenesName()) {
  					startNewActivity();
  				} else {
  					String msg = "<font color='white'>No existen escenas con las que enlazar.</font>";
	  				Toast.makeText(getApplicationContext(), Html.fromHtml(msg), Toast.LENGTH_LONG).show();
  				}
  			}
  		});
	    
	    delete.setOnClickListener(new View.OnClickListener() {
  			@Override
  			public void onClick(View view) {
  				// Delete link scene node
  				ListScene.deleteLinkNode(name, idSceneLink);
  				
  				// Save
				ListScene.saveTempListScene(path_dir + "/");
				
				Intent result = new Intent();
				setResult(Activity.RESULT_OK, result);
				
  				finish();
  				
  				// Reset
				ScenesGallery.sceneNameSelect = "";
  			}
  		});
        
        finish.setOnClickListener(new View.OnClickListener() {
  			@Override
  			public void onClick(View view) {
  				// Correct the link scene name and text
  				if (!ScenesGallery.sceneNameSelect.equals("")) {
  					
  					text = editText1.getText().toString();
  					ListScene.modifyNode(name, idSceneLink, ScenesGallery.sceneNameSelect, text);
	  				
	  				// Save
					ListScene.saveTempListScene(path_dir + "/");
					
					Intent result = new Intent();
					setResult(Activity.RESULT_OK, result);
					
	  				finish();
	  				
	  				// Reset
					ScenesGallery.sceneNameSelect = "";
  				} else {
  					String msg = "<font color='white'>Por favor, seleccione la escena con la que enlazar en la lista.</font>";
	  				Toast.makeText(getApplicationContext(), Html.fromHtml(msg), Toast.LENGTH_LONG).show();
  				}
  			}
  		});
	}
	
	/**
	 * Load image size (width, height) and then
	 * paint into the image view with exit indicate
	 * 
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
		if (hasFocus) {
		    // Image View
		    imageView1 = (ImageView) findViewById(R.id.imageView1);
		    imageWidth = imageView1.getWidth();
		    imageHeight = imageView1.getHeight();
		    
	        // Show Scene
	        if (first) {
	        	first = false;
	        	loadImage();
	        }
	        
	        // Show image and paint link scene area
	        Paint p = new Paint();
	        p.setColor(Color.RED);
	        p.setStrokeWidth(0);
		    p.setStyle(Paint.Style.STROKE);
	        Canvas c = new Canvas(scaled);
	        imageView1.setImageBitmap(scaled);
	        
	        // Adapted the coordinates (800 x 600)--> (imageViewWidthximageViewHeight)
	        x = ListScene.getListLinkScene(name).getLinkSceneNode(idSceneLink).getX();
	        y = ListScene.getListLinkScene(name).getLinkSceneNode(idSceneLink).getY();
	        width = ListScene.getListLinkScene(name).getLinkSceneNode(idSceneLink).getWidth();
	        height = ListScene.getListLinkScene(name).getLinkSceneNode(idSceneLink).getHeight();
	        
	        newX = Math.round((x*imageWidth)/800);
	        newY = Math.round((y*imageHeight)/600);
	        newWidth = Math.round((width*imageWidth)/800);
	        newHeight = Math.round((height*imageHeight)/600);
	        
		    left = newX;
		    right = newX + newWidth;
		    top = newY;
		    bottom = newY + newHeight;
	        
		    c.drawRect(left, top, right, bottom, p);
		}
	}

}
