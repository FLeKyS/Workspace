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
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.Thread.State;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


/**
* This class responsible for represented the obtain image 
* (whether for take picture or select from gallery), and then
* draw the selection area for create an exit
*
* @author Jose Ignacio Madan Frias
* @version 1.0 January 18, 2014
* 
*/

public class Exits extends Activity implements Runnable {
	
	// Bundle
	private Bundle bundle;
	
	// Surface
	private SurfaceView surface;
	private SurfaceHolder holder;
	private Thread thread;
	
	private boolean locker = true;
	
	// Canvas
	private float left = 10;
	private float prev_left = 10;
	private float right = 30;
	private float prev_right = 30;
	private float top = 10;
	private float prev_top = 10;
	private float bottom = 30;
	private float prev_bottom = 30;
	
//////////////////// Touch ////////////////////

	// Coordinates
	private float x_init = 0;
	private float y_init = 0;
	private float x_init_0 = 0;
	private float y_init_0 = 0;
	private float x2_init = 0;
	private float y2_init = 0;
	private float x = 0;
	private float y = 0;
	private float x2 = 0;
	private float y2 = 0;
	
	private static Boolean firstMove;
	
	// Distances
	private float d_init = 0;
	private float d = 0;
	private float d_x = 0;
	private float d_x_t = 0;
	private float d_y = 0;
	private float d_y_t = 0;
	
	// MOVE, ZOOM, EXIT
	private boolean move = false;
	private final float establishTime = 0.1f;
	private int zoomMode = 0; // 0: general, 1: zoom, 2: zoom zone
	private final float sensibility = 15f;
	private boolean changeFinger = false;
	private static long time_ref_sec; //Reference time (seconds)
	private static float time_ref_mil; //Reference time (milliseconds)
	private float last_time = 0;
	private float init_time = 0;
	private float init_time2 = 0;
	private final float checkMakeExitTime = 0.35f;
	private int makeExit = 0;
	private final int size = 4;
	private float rad = 0;
	private float alpha_0 = 0;
	private float alpha = 0;
	
	// Touch event related variables
	private int touchState;
	private final int IDLE = 0;
	private final int MOVE = 1;
	private final int ZOOM = 3;

////////////////////////////////////////////////////////	
	
	// Image
	private String path_dir = "";
	private String name = "";
	private Bitmap scaled;
	
	private boolean first = true;
	private int surfaceWidth;
	private int surfaceHeight;
	private boolean error = false;
	
	
	// Throw makeExits
	/**
	 * Than itself name indicated, throw new activity
	 * 
	 * The activity throw is MakeExits and put in the
	 * bundle path_dir, name, x, y, width, height and idScene
	 * 
	 */
	public void startNewActivity() {
	      Intent i = new Intent(this, MakeExits.class);
	      i.putExtra("path_dir", path_dir);
	      
	      String[] tokens = name.split("[.]");
	      i.putExtra("name", tokens[0]);
	      
	      //
	      //		|	  width		|	
	      //		/////////////////	-
	      //		//			   //	|
	      //		//			   //	| height
	      //	    //			   //	|
	      //  y --> /////////////////  	-
	      //			    |			
	      //			    x
	      
	      
	      // Adapted the coordinates (surfaceWidth x surfaceHeight)--> (800x600)
	      int newLeft = Math.round((left*800)/surfaceWidth);
	      int newRight = Math.round((right*800)/surfaceWidth);
	      int newTop = Math.round((top*600)/surfaceHeight);
	      int newBottom = Math.round((bottom*600)/surfaceHeight);
	      
	      // width
	      int width = newRight - newLeft; 
	      
	      // height
	      int height = newBottom - newTop;
	      
	      // x
	      int x = newLeft;
	      
	      // y
	      int y = newTop;
	      
	      // x, y, width, height
	      i.putExtra("x", x);
	      i.putExtra("y", y);
	      i.putExtra("width", width);
	      i.putExtra("height", height);
	      
	      startActivity(i);
	}
	
	/**
	 * @return time in seconds
	 * 
	 */
	public static long getTimeSec() {
		
		return (Calendar.getInstance().getTimeInMillis()/1000) - time_ref_sec;
	}
	
	/**
	 * @return time in milliseconds
	 * 
	 */
	public static float getTimeMillis() {
		
		return ((Calendar.getInstance().getTimeInMillis()%1000)/1000f) - time_ref_mil;
	}
	
	/**
	 * Start time
	 * 
	 */
	public static void startTime() {
		// Initial time
		time_ref_sec = Calendar.getInstance().getTimeInMillis()/1000;
		time_ref_mil = (Calendar.getInstance().getTimeInMillis()%1000)/1000f;
	}

	
	/**
	 * Create the basic configuration on the screen,
	 * with two buttons for finish and create a scene exit,
	 * furthermore describe the possible actions in the surfaceView
	 * (Move, Zoom, Zoom part)
	 * 
	 * @param savedInstanceState for save values of the activity
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (first) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		
		super.onCreate(savedInstanceState);
		
		// Mapped
		setContentView(R.layout.exits);
		
		if (first) {
			bundle = savedInstanceState;
		}
		
		// Start time
		startTime();
		
		if (firstMove == null) {
  			firstMove = false;
      			
  			String msg = "";
  			
  			for (int i = 0; i < 4; i++) {
  				if (i == 0) {
  					msg = "<font color='white'>Con un dedo fijo, mueva el otro para ampliar la zona que "
  							+ "desee.</font>";
  				} else if (i == 1) {
  					msg = "<font color='white'>Presione dos veces seguidas dentro del area para crear "
		        				+ "la salida.</font>";
  				} else if (i == 2) {
  					msg = "<font color='white'>Mueva los dos dedos a la vez para hacer zoom.</font>";
  				} else if (i == 3) {
  					msg = "<font color='white'>Presione atras para finalizar.</font>";
  				}
  				
	        	Toast.makeText(getApplicationContext(), Html.fromHtml(msg), Toast.LENGTH_LONG).show();
  			}
  		}
		
		// Surface
		surface = (SurfaceView) findViewById(R.id.surfaceView1);
		holder = surface.getHolder();
		
        // Start thread
        thread = new Thread(this);
        thread.start();
        
        //					|		|						//
        //			     Top| Bottom| 				        //
        // 					|		|						//
        //				############|####					//
        //				#			|	#					//
        //----Left----->#			|	#<----Right---------//
        //				#			|	#					//
        //				#################					//
        //													//
        
 		
 
 		surface.setOnTouchListener(new View.OnTouchListener() {
 			
			@SuppressLint("ClickableViewAccessibility")
			@SuppressWarnings("deprecation")
			@Override
 	        public boolean onTouch(View v, MotionEvent event) {
 				
 				boolean eventConsumed = true;
 				
 				if(event.getAction() == MotionEvent.ACTION_DOWN){
 					// First finger press
 					
 					// Initial coordinates
					x_init = event.getX(0);
 	 				y_init = event.getY(0);
 	 				x_init_0 = x_init;
 	 				y_init_0 = y_init;
 	 				
 	 				// Move
 	 				move = false;
 	 				changeFinger = false;
 	 				
 	 				// Zoom
 	 				zoomMode = 0;
 	 				init_time2 = getTimeSec() + getTimeMillis();
 	 				
 	 				if (makeExit == 0) {
 	 					init_time = getTimeSec() + getTimeMillis();
 	 				}
 	 				
 	 				last_time = getTimeSec() + getTimeMillis();
 	 				
 					// If touch twice in same area and time < checkMakeExitTime seconds, then make exit
 	 				
					// Inside of area
					if ((x_init < right) && (x_init > left) && (y_init > top) && (y_init < bottom)) {
						
						if (((last_time - init_time) < checkMakeExitTime)) {
							makeExit++;
	 	 				} else {
	 	 					init_time = getTimeSec() + getTimeMillis();
	 	 					makeExit = 1;
	 	 				}
						
						// Make Exit
						if (makeExit == 2) {
							makeExit = 0;
							startNewActivity();
						}
						
					} else {
						makeExit = 0;
					}
 	 				
 	 				// Initial touch state
 	 				touchState = MOVE;
 					
 				} else if (event.getAction() == MotionEvent.ACTION_UP) {
 					// First finger up
 					zoomMode = 0;
 					
 					// Resume touch state
 					touchState = IDLE;
 		        
 				} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_DOWN) {
 					// Second finger press

 					// Initial coordinates
 	 				x2_init = event.getX(1);
 	 				y2_init = event.getY(1);
 	 				
 	 				// Initial touch state
 	 				move = false;
 	 				
 	 				touchState = ZOOM;
 	 				
 				} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_UP) {
 					// Second finger up
 					
 					if (changeFinger) {
 						changeFinger = false;
 					}
 					
 					// Resume touch state
 					touchState = IDLE;
	  				
 				} else if (event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
 					// Third finger press (First again)
 					
 					// Init coordinates
					x2_init = event.getX(0);
 					y2_init = event.getY(0);
 					
 					// Initial touch state
 	 				touchState = ZOOM;
	  				
 				} else if (event.getAction() == MotionEvent.ACTION_POINTER_UP) {
 					// Third finger up (First again)
 					
 					// Update coordinates
 					if (!changeFinger) {
 						changeFinger = true;
 						x_init = x2_init;
 						y_init = y2_init;
 					}
	 					
 					
 					// Resume touch state
 					touchState = IDLE;
 					
 		        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
 		        	// Move finger
	        		
	        		// Final coordinates
 		        	try {
 		        		if (changeFinger) {
	 		        		x = event.getX(event.getPointerCount() - 1);
	 		        		y = event.getY(event.getPointerCount() - 1);
	 		        		
 		        		} else {
 		        			x = event.getX(0);
	 		        		y = event.getY(0);
 		        		}	
 		        		
 	 		        	if (touchState == IDLE) {
 		        			x_init = x;
 		        			y_init = y;
 		        		}
 	 		        	
 		        	} catch (IllegalArgumentException iae) {}
 		        	
 		        	
 		        	// Move
 		        	if (touchState == MOVE) {
 		        		
 		        		d_x = Math.abs(x - x_init);
 		        		d_y = Math.abs(y - y_init);
 		        		
 		        		// Euclidean distance
 			        	d = (float) Math.sqrt(Math.pow(d_x, 2) + Math.pow(d_y, 2));
 		        		
 		        		// Calculate alpha
 	 		        	rad = (float) Math.atan2(y_init - y, x - x_init);
 	 		        	
 	 		        	alpha = (float) ((rad * 180) / Math.PI);
 	 		        	 	 		        	
 	 		        	if (move) {
	 	 		        	if ((x > x_init) && ((alpha > -75) && (alpha < 75))) {
	 	 		        		if (right < surfaceWidth) {
	 	 		        			if (d_x > (surfaceWidth - right)) {
	 	 		        				d_x = surfaceWidth - right;
	 	 		        			}
	 	 		        			
		 	 		        		left += d_x;
		 	 		        		right += d_x;
	 	 		        		}
	 	 		        	} else if ((x < x_init) && (((alpha > 0) && (alpha > 105)) || ((alpha < 0) && (alpha < -105)))) {
	 	 		        		if (left > 0) {
	 	 		        			if (d_x > left) {
	 	 		        				d_x = left;
	 	 		        			}
	 	 		        			
	 	 		        			left -= d_x;
	 	 		        			right -= d_x;
	 	 		        		}
	 	 		        	}
	 	 		        	
	 	 		        	if ((y > y_init) && ((alpha > -165) && (alpha < -15))) {
	 	 		        		if (bottom < surfaceHeight) {
	 	 		        			if (d_y > (surfaceHeight - bottom)) {
	 	 		        				d_y = surfaceHeight - bottom;
	 	 		        			}
	 	 		        			
	 	 		        			top += d_y;
	 	 		        			bottom += d_y;
	 	 		        		}
	 	 		        	} else if ((y < y_init) && ((alpha > 15) && (alpha < 165))) {
	 	 		        		if (top > 0) {
	 	 		        			if (d_y > top) {
	 	 		        				d_y = top;
	 	 		        			}
	 	 		        			
		 	 		        		top -= d_y;
			 		        		bottom -= d_y;
	 	 		        		}
	 	 		        	}
 	 		        	}	
	 	 		        	
 	 		        	// Update coordinates
	 	 		        x_init = x;
	 	 		        y_init = y;
	 	 		        
	 	 		        // Update taotal disctance
	 	 		        d_x_t += d_x;
	 	 		        d_y_t += d_y;
	 	 		        
	 	 		        // Check time for move
	 	 		        last_time = getTimeSec() + getTimeMillis();
	 	 		        
	 	 		        if ((last_time - init_time2) > establishTime) {
	 	 		        
	 	 		        	if ((d_x_t < sensibility) && (d_y_t < sensibility)) {
		 	 		        	// Zoom zone
		 	 		        	zoomMode = 2;
		 	 		        	
	 	 		        	} else {
		 	 		        	// Zoom general (get correct coordinates) -> zoom conventional
		 	 		        	zoomMode = 0;
 							}
	 	 		        	
	 	 		        	// Reset
	 	 		        	init_time2 = getTimeSec() + getTimeMillis();
	 	 		        	d_x_t = 0;
		 	 		        d_y_t = 0;
		 	 		        
		 	 		        move = false;
	 	 		        } else {
	 	 		        	move = true;
	 	 		        }
	 	 		        
 		        	}
 		        	
		        			
 		        	// ZOOM ZONE & ZOOM
	        		if (touchState == ZOOM) {
	        			
	        			// Final coordinates
 		        		try {
 		        			// Depend on (first, second or third finger)
 		        			
 		        			if (!changeFinger) {
 		        				// Second finger
 		        				x2 = event.getX(1);
 		        				y2 = event.getY(1);
 		        			} else {
 		        				// First or third finger
 		        				x2 = event.getX(0);
 		        				y2 = event.getY(0);
 		        			}
 		        			
 		        			if (zoomMode == 0) {
 		 	 					x_init = x_init_0;
 		 	 					y_init = y_init_0;
 		 	 				}
 		        			
 		        		} catch (IllegalArgumentException iae) {}
 		        		
 		        		// ZOOM
 		        		
 		        		if (((zoomMode == 0) || (zoomMode == 1))) {
 		        			
	 		        		// Euclidean initial distance
	 	 	 				d_init = (float) Math.sqrt(Math.pow(Math.abs(x2_init - x_init), 2) + Math.pow(Math.abs(y2_init - y_init), 2));
	 		        		
	 		        		// Euclidean distance
	 			        	d = (float) Math.sqrt(Math.pow(Math.abs(x2 - x), 2) + Math.pow(Math.abs(y2 - y), 2));
	 			        	
	 			        	// Calculate alpha_0
	        				d_x = x - x_init;
	        				d_y = y_init - y;
 		        			
	 	 		        	rad = (float) Math.atan2(d_y, d_x);
	 	 		        	alpha_0 = (float) ((rad * 180) / Math.PI);
	 	 		        	
	 	 		        	// Calculate alpha
	        				d_x = x2 - x2_init;
	        				d_y = y2_init - y2;
	 	 		        	
	 	 		        	rad = (float) Math.atan2(d_y, d_x);
	 	 		        	alpha = (float) ((rad * 180) / Math.PI);
	 	 		        	
	 	 		        	// Move inside the same line
	 	 		        	if (((((alpha_0 < 45) && (alpha_0 > -45)) && ((alpha > 135) || (alpha < -135))) ||
	 	        					(((alpha_0 > 135) || (alpha_0 < -135)) && ((alpha < 45) && (alpha > -45))) ||
	 	        					(((alpha_0 > 45) && (alpha_0 < 135)) && ((alpha < -45) && (alpha > -135))) ||
	 	        					(((alpha_0 < -45) && (alpha_0 > -135)) && ((alpha > 45) && (alpha < 135))))) {
	 			        	
		 	 		        	// Zoom out
		 	        			if (d > d_init) {
		 	        				
		 	        				if ((left > 0) && (right < surfaceWidth) && (top > 0) && (bottom < surfaceHeight)) {
		 	        					left -= size;
		 		        				right += size;
		 		        				top -= size;
		 		        				bottom += size;
		 		        				
		 		        				if (zoomMode == 0) {
		 		        					zoomMode = 1;
		 		        				}
		 		        				
		 	        				}
		 			        	}
		 	        			
		 	        			// Zoom in
		 	        			if (d < d_init) {
		 	        				
		 	        				if ((right - left > 10) && (bottom - top > 10)) {
		 		        				left += size;
		 		        				right -= size;
		 		        				top += size;
		 		        				bottom -= size;
		 		        				
		 		        				if (zoomMode == 0) {
		 		        					zoomMode = 1;
		 		        				}
		 		        				
		 	        				}
		 	        			}
	 			        	}
	 	        			
	 	        			// Update coordinates
	 	        			x_init = x;
	 	        			y_init = y;
	 		        		x2_init = x2;
	 		        		y2_init = y2;
	 	        			
 		        		} else if (zoomMode == 2) {
 		        			
	 	        			// ZOOM ZONE
 		        			
		        			// Calculate alpha_0
	        				d_x = x2_init - x_init;
	        				d_y = y_init - y2_init;
 		        			
	 	 		        	rad = (float) Math.atan2(d_y, d_x);
	 	 		        	alpha_0 = (float) ((rad * 180) / Math.PI);
	 	 		        	
	 	 		        	// Calculate alpha
	        				d_x = x2 - x2_init;
	        				d_y = y2_init - y2;
	 	 		        	
	 	 		        	rad = (float) Math.atan2(d_y, d_x);
	 	 		        	alpha = (float) ((rad * 180) / Math.PI);
	 	 		        	
	 	 		        	d_x = Math.abs(d_x);
	 	 		        	d_y = Math.abs(d_y);
	 	 		        	
	 	 		        	// Depending on alpha do different actions
	 	 		        	if (((alpha_0 > 0) && (alpha_0 < 30)) || ((alpha_0 < 0) && (alpha_0 > -30))) {
	 	 		        		
	 	 		        		// Widen horizontally (right)
	 	 		        		if (((alpha < 15) && (alpha > -15)) || ((alpha > 165) || (alpha < -165))) {
		 	 		        		if (x2 > x2_init) {
		 	 		        			right += d_x;
		 	 		        		} else {
		 	 		        			right -= d_x;
		 	 		        		}
		 	 		        		
	 	 		        		}
	 	 		        		
	 	 		        	} else if ((alpha_0 > 0) && (alpha_0 < 60)) {
	 	 		        		
	 	 		        		// Widen right diagonal superior
	 	 		        		if (((alpha < 75) && (alpha > 15)) || ((alpha > -165) && (alpha < -120))) {
 	 		        				if (x2 > x2_init) {
		 	 		        			if ((right < surfaceWidth) && (top > 0)) {
			 	 		        			right += d_x;
			 	 		        			top -= d_y;
		 	 		        			}
		 	 		        		} else {
		 	 		        			if ((right - left > 10) && (bottom - top > 10)) {
		 	 		        				right -= d_x;
		 	 		        				top += d_y;
		 	 		        			}
		 	 		        		}	
 	 		        				
	 	 		        		}
	 	 		        		
	 	 		        	} else if ((alpha_0 > 0) && (alpha_0 < 120)) {
	 	 		        		
	 	 		        		// Widen vertically (top)
	 	 		        		if (((alpha < 105) && (alpha > 75)) || ((alpha > -105) && (alpha < -75))) {
 	 		        				if (y2 > y2_init) {
		 	 		        			top += d_y;
		 	 		        		} else {
		 	 		        			top -= d_y;
		 	 		        		}
 	 		        				
	 	 		        		}
	 	 		        		
	 	 		        	} else if ((alpha_0 > 0) && (alpha_0 < 150)) {
	 	 		        		
	 	 		        		// Widen left diagonal superior
	 	 		        		if (((alpha < 165) && (alpha > 105)) || ((alpha > -75) && (alpha < -15))) {
		 	 		        		if (x2 > x2_init) {
		 	 		        			if ((right - left > 10) && (bottom - top > 10)) {
			 	 		        			left += d_x;
			 	 		        			top += d_y;
		 	 		        			}
		 	 		        		} else {
		 	 		        			if ((left > 0) && (top > 0)) {
			 	 		        			left -= d_x;
			 	 		        			top -= d_y;
		 	 		        			}
		 	 		        		}
		 	 		        		
	 	 		        		}
	 	 		        		
	 	 		        	} else if ((alpha_0 < -150) || ((alpha_0 > 0) && (alpha_0 > 150))) {
	 	 		        		
	 	 		        		// Widen horizontally (left)
	 	 		        		if (((alpha < 15) && (alpha > -15)) || ((alpha > 165) || (alpha < -165))) {
		 	 		        		if (x2 > x2_init) {
		 	 		        			left += d_x;
		 	 		        		} else {
		 	 		        			left -= d_x;
		 	 		        		}
		 	 		        		
	 	 		        		}
	 	 		        		
	 	 		        	} else if (alpha_0 < -120) {
	 	 		        		
	 	 		        		// Widen left diagonal inferior
	 	 		        		if (((alpha < 75) && (alpha > 15)) || ((alpha > -165) && (alpha < -120))) {
		 	 		        		if (x2 > x2_init) {
		 	 		        			if ((right - left > 10) && (bottom - top > 10)) {
			 	 		        			left += d_x;
			 	 		        			bottom -= d_y;
		 	 		        			}
		 	 		        		} else {
		 	 		        			if ((left > 0) && (bottom < surfaceHeight)) {
			 	 		        			left -= d_x;
			 	 		        			bottom += d_y;
		 	 		        			}
		 	 		        		}
		 	 		        		
	 	 		        		}
	 	 		        		
	 	 		        	} else if (alpha_0 < -60) {
	 	 		        		
	 	 		        		// Widen vertically (bottom)
	 	 		        		if (((alpha < 105) && (alpha > 75)) || ((alpha > -105) && (alpha < -75))) {
 	 		        				if (y2 > y2_init) {
		 	 		        			bottom += d_y;
		 	 		        		} else {
		 	 		        			bottom -= d_y;
		 	 		        		}
 	 		        				
	 	 		        		}
	 	 		        		
	 	 		        	} else {
	 	 		        		
	 	 		        		// Widen right diagonal inferior
	 	 		        		if (((alpha < 165) && (alpha > 105)) || ((alpha > -75) && (alpha < -15))) {
 	 		        				if (x2 > x2_init) {
		 	 		        			if ((right < surfaceWidth) && (bottom < surfaceHeight)) {
			 	 		        			right += d_x;
			 	 		        			bottom += d_y;
		 	 		        			}
		 	 		        		} else {
		 	 		        			if ((right - left > 10) && (bottom - top > 10)) {
			 	 		        			right -= d_x;
			 	 		        			bottom -= d_y;
		 	 		        			}
		 	 		        		}
 	 		        				
	 	 		        		}
	 	 		        		
	 	 		        	}
	 	 		        	
	 	 		        	// Update coordinates
 		        			x2_init = x2;
	 		        		y2_init = y2;
 	        			}
 		        	}
	        		
 		        } else {
 		        	
 		        	eventConsumed = false;
 		        }
 				
 				return eventConsumed;
 			}
 		});
	}
	
	/**
	 * Load image previously save
	 * 
	 */
	public void loadImage() {
	        
    	// Load image
    	InputStream is;
    	error = false;
    	
		try {
			is = new FileInputStream(path_dir + "/" + name);
			BufferedInputStream bis = new BufferedInputStream(is);
			
			// Change to RGB_565 to down memory usage
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inPreferredConfig = Config.RGB_565;
			Bitmap image = BitmapFactory.decodeStream(bis);
			
			scaled = Bitmap.createScaledBitmap(image, surfaceWidth, surfaceHeight, true);
		} catch (Exception e) {
			error = true;
		} catch (OutOfMemoryError e1) {
			error = true;
		}
	}
	
	/**
	 * Thread to paint in the surfaceView
	 * 
	 */
	@Override
	public void run() {
		
		while(locker) {
			
			// Checks if the lockCanvas() method will be success, and if not, will check this statement again
			if(!holder.getSurface().isValid()){
				continue;
			}
			
			/** Start editing pixels in this surface.*/
			Canvas canvas = holder.lockCanvas();
	  
			// ALL PAINT-JOB MAKE IN draw(canvas); method.
			draw(canvas);
	  
			// End of painting to canvas. system will paint with this canvas, to the surface.
	    	holder.unlockCanvasAndPost(canvas);
	    }
	}
	
	/**
	 * Method for update left side
	 * 
	 */
	private void leftMove() {
		if (left <= 0) {
			left = 0;
		} else if ((left >= right - 10) && (left != prev_left)) {
			left = right - 10;
		}
		
		prev_left = left;
	}
	
	/**
	 * Method for update right side
	 * 
	 */
	private void rightMove() {
		if (right >= surfaceWidth) {
			right = surfaceWidth;
		} else if ((right <= left + 10) && (right != prev_right)) {
			right = left + 10;
		}
		
		prev_right = right;
	}
	
	/**
	 * Method for update top side
	 * 
	 */
	private void topMove() {
		if (top <= 0) {
			top = 0;
		} else if ((top >= bottom - 10) && (top != prev_top)) {
			top = bottom - 10;
		}
		
		prev_top = top;
	}
	
	/**
	 * Method for update bottom side
	 * 
	 */
	private void bottomMove() {
		if (bottom >= surfaceHeight) {
			bottom = surfaceHeight;
		} else if ((bottom <= top + 10) && (bottom != prev_bottom)) {
			bottom = top + 10;
		}
		
		prev_bottom = bottom;
	}
	
	
	/**
	 * Method for draw in the canvas
	 * 
	 * @param canvas to draw
	 * 
	 */
	private void draw(Canvas canvas) {
		
	    // Load image
	    if (first) {
	        first = false;
	        
	        // Path
			Bundle b = getIntent().getExtras();
		    path_dir = b.getString("path_dir");
		    
		    // Name image
		    name = b.getString("name");
	 		
	        surfaceWidth = canvas.getWidth();
	        
	        surfaceHeight = canvas.getHeight();
	        
	        loadImage();
	    }

	    if (!error) {
	    	
		    // Draw the image
		 	canvas.drawBitmap(scaled, 0, 0, null);
		 	
		    // Paint a rectangular
		    Paint paint = new Paint();
		    paint.setColor(Color.BLUE);
		    paint.setStrokeWidth(3);
		    paint.setStyle(Paint.Style.STROKE);
		    
		    // Update sides
		    leftMove();
		    topMove();
		    rightMove();
		    bottomMove();
		    
	        canvas.drawRect(left, top, right, bottom, paint);
	        
	    } else {
	    	pause();
	    	finish();
	    }
	    
	}
	
	/**
	 * Call pause()
	 * 
	 */
	@Override
	protected void onPause() {    
		super.onPause();
	    pause();
	}
	
	/**
	 * Set locker false and release thread
	 * 
	 */
	private void pause() {
	    // CLOSE LOCKER FOR run();
	    locker = false;
	    
	    while(true){
	    	try {
	    		// WAIT UNTIL THREAD DIE, THEN EXIT WHILE LOOP AND RELEASE a thread
	    		thread.join();
	    	} catch (InterruptedException e) {}
	    	
	    	break;
	    }
	    
	    thread = null;
	}

	/**
	 * Call resume()
	 * 
	 */
	@Override
	protected void onResume() {
		super.onResume();
	    resume();    
	}

	/**
	 *Set locker true
	 * 
	 */
	private void resume() {
	    //RESTART THREAD AND OPEN LOCKER FOR run();
	    locker = true;
	}


	/**
	 * Restart activity if needed, and save ListScene.tmp
	 * 
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		
		// Restart activity
		try {
			if(thread.getState() == State.TERMINATED) {
				onCreate(bundle);
			}
		} catch (NullPointerException n) {
			onResume();
			onCreate(bundle);
		}
	}

	/**
	 * Show errors if exists
	 * 
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (error) {
			String msg = "<font color='white'>Memoria ocupada, espere un momento.</font>";
			Toast.makeText(getApplicationContext(), Html.fromHtml(msg), Toast.LENGTH_LONG).show();
		}
	}
	
}
