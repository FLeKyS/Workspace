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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
* This is the principal class of the app (Main Activity), 
* so, contains the Launcher and the option to create a project and exit from it.
*
* @author Jose Ignacio Madan Frias
* @version 1.0 November 27, 2014
* 
*/

public class Main extends Activity {
	
	// Buttons
	private Button make;
	private Button exit;
	
	// Exit
	private boolean finish = false;

	
	/**
	 * Than itself name indicate, throw new activity
	 * 
	 * In this case, Properties
	 * 
	 */
	public void startNewActivity() {
	      Intent i = new Intent(this, Properties.class);
	      startActivity(i);
	} 
	
	/**
	 * Create the basic configuration on the screen,
	 * with two buttons (make for create a new project 
	 * and exit for finish the application)
	 * 
	 * @param savedInstanceState for save values of the activity
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Buttons
		make = (Button)findViewById(R.id.button1);
		exit = (Button)findViewById(R.id.button2);
		
		
		// Create a new project, for this, call startNewActivity()
  		make.setOnClickListener(new View.OnClickListener() {
  			@Override
  			public void onClick(View view) {
  				startNewActivity();
  				
  				// Reset
  				finish = false;
  			}
  		});
      	
  		// Exit from application, back to general menu
 		exit.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View view) {
 				// Start menu activity
  			    Intent intent = new Intent(Intent.ACTION_MAIN);
  			    intent.addCategory(Intent.CATEGORY_HOME);
  			    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  			    startActivity(intent);
  			    
  			    finish();
 			}
 		});
	}
	
	// Prohibited back button use	
	/**
	 * Override onBackPressed to prohibit it use, unless press back two times
	 * 
	 */
	@Override
	public void onBackPressed() {
		if (finish) {
			// Start menu activity
		    Intent intent = new Intent(Intent.ACTION_MAIN);
		    intent.addCategory(Intent.CATEGORY_HOME);
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    startActivity(intent);
		    
		    finish();
		} else {
			finish = true;
			
			String msg = "<font color='white'>Pulse atrás de nuevo si desea salir.</font>";
			Toast.makeText(getApplicationContext(), Html.fromHtml(msg), Toast.LENGTH_LONG).show();
		}
	}
	
}
