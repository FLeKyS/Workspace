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

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.AdapterView.OnItemClickListener;


/**
* Class to represent a custom gallery with
* images in project
*
* @author Jose Ignacio Madan Frias
* @version 1.0 November 2, 2014
* 
*/

@SuppressWarnings("deprecation")
public class ScenesGallery extends Activity {
	
	public static String sceneNameSelect = "";
	
	private ArrayList<String> scenesName = new ArrayList<String>();
	
	
	/**
	 * Create the basic configuration on the screen,
	 * with a custom gallery
	 * 
	 * @param savedInstanceState for save values of the activity
	 * 
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scenesgallery);
        
        // Path directory
 		Bundle bundle = getIntent().getExtras();
 	    String path_dir = bundle.getString("path_dir");
 	    
 	    // Actual Scene Name
 	    String actualSceneName = bundle.getString("actualSceneName");
 	    if (actualSceneName == null) {
 	        actualSceneName = "";
 	    }
 	    
        // Reference the Gallery view
        Gallery g = (Gallery) findViewById(R.id.gallery);
        g.setSpacing(2);
        
        // Set the adapter to our custom adapter (below)
        g.setAdapter(new ImageAdapter(this, path_dir, scenesName, actualSceneName));

        g.setOnItemClickListener(new OnItemClickListener() {
        	@Override
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        		// Scene name select
        		sceneNameSelect = scenesName.get(position);
        		finish();
	        }
        });
    }
}
