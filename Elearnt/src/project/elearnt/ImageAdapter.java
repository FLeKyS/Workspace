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
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;


/**
* Class to adapt images in the gallery
*
* @author Jose Ignacio Madan Frias
* @version 1.0 December 5, 2014
* 
*/

@SuppressWarnings("deprecation")
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private String path_dir;
    
	private ArrayList<String> scenesName;
	private ArrayList<Boolean> isLoaded;
	private ArrayList<Bitmap> images = new ArrayList<Bitmap>();
    private String actualSceneName;
    
    private String[] nameFiles;
    
    // Load image
    private InputStream is;
    private boolean first = true;
    private int width;
    private int height;
    
	/**
	 * Than itself name indicate, load scene names
	 * in project
	 * 
	 */
    public void loadScenesName() {
    	
    	// List dir
    	File dir = new File (path_dir);
    	String sceneName;
    	
    	// List the directory
    	isLoaded = new ArrayList<Boolean>();
        nameFiles = dir.list();
        
        // Add name files to list
        if (nameFiles != null) {
        	
        	// Correct Scenes
        	String [] tokens;
        	for (int i = 0; i < nameFiles.length; i++) {
        		tokens = nameFiles[i].split("[.]");
            	
            	if ((tokens[1].equals("jpg")) && (ListScene.existsNode(tokens[0]))) {
            		scenesName.add(tokens[0]);
            	}
        	}
        	
        	ListScene.correctScenes(scenesName);
        	
        	// Reset
        	scenesName.clear();
        	
            for (int i = 0; i < ListScene.getSize(); i++) {
            	sceneName = ListScene.getSceneName(i);
            	
            	if (!sceneName.equals(actualSceneName)) {
            		scenesName.add(sceneName);
            		isLoaded.add(false);
            	}
            }
        }
	}
    
	/**
	 * Constructor with four parameters
	 * 
	 * @param c from scenesGallery
	 * @param path_dir wherein project be
	 * @param scenesName names of the scenes in project
	 * @param actualSceneName name of the actual scene
	 * 
	 */
	public ImageAdapter(Context c, String path_dir, ArrayList<String> scenesName, String actualSceneName) {
        this.mContext = c;
        this.path_dir = path_dir;
        this.scenesName = scenesName;
        this.actualSceneName = actualSceneName;
        loadScenesName();
        first = true;
        
        // Adapted width and height
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        width = Math.round(metrics.widthPixels)/3;
        height = metrics.heightPixels - 50;
    }

	/**
	 * @return number of images in the view
	 * 
	 */
    public int getCount() {
        return scenesName.size();
    }

	/**
	 * @return image position
	 * 
	 */
    public Object getItem(int position) {
        return position;
    }

	/**
	 * @return image position
	 * 
	 */
    public long getItemId(int position) {
        return position;
    }

	/**
	 * Obtain view of the image
	 * 
	 * @param position of the actual image
	 * @param convertView modified view
	 * @param parent view
	 * 
	 */
    public View getView(int position, View convertView, ViewGroup parent) {
  	
    	ImageView i = new ImageView(mContext);
    	Bitmap image = null;
    	
    	// If the image don't loaded, then loaded it
    	if (!isLoaded.get(position)) {
    		// Load image
			try {
				is = new FileInputStream(path_dir + "/" + scenesName.get(position) + ".jpg");
				BufferedInputStream bis = new BufferedInputStream(is);
				
				// Change to RGB_565 to down memory usage
			    BitmapFactory.Options options = new BitmapFactory.Options();
			    options.inPreferredConfig = Config.RGB_565;
			    
				image = BitmapFactory.decodeStream(bis, null, options);
				
				image = Bitmap.createScaledBitmap(image, width, Math.round(height/2), true);
				
				// Add to images
				images.add(position, image);
				
		        // Is loaded OK
		        isLoaded.add(position, true);
				
			} catch (Exception e) {
				if (first) {
					first = false;
					String msg = "<font color='white'>Memoria ocupada, no se pueden cargar mas imagenes.</font>";
	  				Toast.makeText(mContext, Html.fromHtml(msg), Toast.LENGTH_LONG).show();
				}
			} catch (OutOfMemoryError e1) {
				if (first) {
					first = false;
					String msg = "<font color='white'>Memoria ocupada, no se pueden cargar mas imagenes.</font>";
	  				Toast.makeText(mContext, Html.fromHtml(msg), Toast.LENGTH_LONG).show();
	  				msg = "<font color='white'>Memoria ocupada, no se pueden cargar mas imagenes.</font>";
	  				Toast.makeText(mContext, Html.fromHtml(msg), Toast.LENGTH_LONG).show();
				}
			}
		
    	} else {
    		image = images.get(position);
    	}
    	
    	// Set image
		i.setImageBitmap(image);
		
        // Change dimensions
        i.setLayoutParams(new Gallery.LayoutParams(width, height));
        
        return i;

    };
}