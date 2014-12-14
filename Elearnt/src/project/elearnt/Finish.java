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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
* Class for established a connection with server,
* and then send files to him
*
* @author Jose Ignacio Madan Frias
* @version 1.0 December 9, 2014
* 
*/

public class Finish extends Activity implements Runnable {
	
	// Edit text
	private EditText editText1;
		
	// Buttons
	private Button send;
	private Button finish;
	
	// Send
	private Thread thread;
	private boolean finished = false;
	private String msg = "";
	private PrintStream ps;
	private OutputStream os;
	private InputStream is;
	private BufferedReader br;
	private FileInputStream fis;
	
	private File dir = null;
	private String[] nameFiles;
	private String path_dir;
	
	
	/**
	 * Method for send file to server
	 * 
	 * @param name of the file which sends
	 * 
	 */
	public boolean sendFile(String name) {
		// Open the client connection
		Socket socket = null;
		SocketAddress addr = null;
		
		try {
			addr = new InetSocketAddress(editText1.getText().toString(), 12345);
			socket = new Socket();
			socket.connect(addr, 1000);
			socket.setSoTimeout(5000);
		} catch (Exception e) {
			msg = "<font color='white'>Error al conectar con el servidor</font>";
			return false;
		}
		
		// Open stream
		try {
			os = socket.getOutputStream();
			ps = new PrintStream(os);
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
		} catch (Exception e) {
			msg = "<font color='white'>Error al crear los medios de transferencia</font>";
			try {
				socket.close();
			} catch (Exception e1) {}
			
			return false;
		}
		
		// Send name file
		ps.println(name);
		ps.flush();
		
		// Send file
		try {
			// Confirm
			br.readLine();
			
			fis = new FileInputStream(path_dir + "/" + name);
			
			byte[] buffer = new byte[1024];
			int len = 0;
			
			while ((len = fis.read(buffer)) != -1) {
				ps.write(buffer, 0, len);
				ps.flush();
			}
						
		} catch (Exception e) {
			msg = "<font color='white'>Error al transferir el fichero</font>";
			return false;
		} finally {
			
			try {
	    		// Close file
	    		if (null != fis) {
	    			fis.close();
	    			socket.close();
	    		}
			} catch (Exception e) {
				msg = "<font color='white'>Error al terminar la conexion con el servidor</font>";
				return false;
			}
        }
		return true;
	}
	
	/**
	 * Thread to send files to the server
	 * 
	 */
	public void run() {
		while (!finished) {
			// Images names
			dir = new File(path_dir);
	        
	        // List the directory
	        nameFiles = dir.list();
	        ArrayList<String> listNameFiles = new ArrayList<String>();
	        
	        // Not send .tmp file
	        if (nameFiles != null) {
	            for (int i = 0; i < nameFiles.length; i++) {
	            	String[] tokens = nameFiles[i].split("[.]");
	            	
	            	if (!tokens[1].equals("tmp")) {
	            		listNameFiles.add(nameFiles[i]);
	            	}
	            }
	        }
	
			// Send files
	        int i = 0;
	        
		    while ((i < listNameFiles.size()) && (sendFile(listNameFiles.get(i)))) {
		    	i++;
	        }
		    
		    if (i < listNameFiles.size()) {
		    	break;
		    }
		    
		    finished = true;
		}
	}

	
	/**
	 * Create the basic configuration on the screen,
	 * with two buttons, one for send files to the server,
	 * and other to return to the principal app activity,
	 * and two edit text, one for ip and other for port,
	 * of the server, respectively
	 * 
	 * @param savedInstanceState for save values of the activity
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finish);
		
		// Buttons
		send = (Button) findViewById(R.id.button1);
		finish = (Button) findViewById(R.id.button2);
		
		// Edit text
		editText1 = (EditText) findViewById(R.id.editText1);
		
		// Path directory
		Bundle bundle = getIntent().getExtras();
	    path_dir = bundle.getString("path_dir");
	    
		
		send.setOnClickListener(new View.OnClickListener() {
  			@Override
  			public void onClick(View view) {
  				// Send files (.xml, .jpg)
  				thread = new Thread(Finish.this);
  				thread.start();

  				// Update msg and finish
  				try {
  					while(msg.equals("") && (!finished)) {
  						Thread.sleep(1000);
  					}
				} catch (InterruptedException e1) {}
  				
  				// If not finish show error
  				if (!finished) {
  					send.setTextColor(Color.RED);
  					
  					Toast.makeText(getApplicationContext(), Html.fromHtml(msg), Toast.LENGTH_LONG).show();
  					
  				} else {
  					// To re-send
  					finished = false;
  					
  					send.setTextColor(Color.GREEN);
  				}
  				
  				// Reset
  				msg = "";
  			}
  		});
		
		finish.setOnClickListener(new View.OnClickListener() {
  			@Override
  			public void onClick(View view) {
  				// Remove stack
  				getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    
  				// Back to principal activity
  				getIntent().setClass(getApplicationContext(), Main.class);
			    startActivity(getIntent());
			    
			    finish();
  			}
  		});
	}

}
