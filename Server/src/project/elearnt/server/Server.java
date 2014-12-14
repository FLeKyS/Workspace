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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;


/**
* This class implements a thread server for receive
* files
*
* @author Jose Ignacio Madan Frias
* @version 1.0 December 9, 2014
* 
*/

public class Server extends Thread {
	private Socket connection;
	private PrintStream ps;
	private OutputStream os;
	private InputStream is;
	private FileOutputStream fos;
	private BufferedReader br;
	private String path;
	private String name;
	
	
	/**
	 * Modify constructor of server with two parameters
	 * 
	 * @param connection realized in multithread server
	 * @param path wherein project be
	 * 
	 */
	public Server (Socket connection, String path) {
		this.connection = connection;
		this.path = path;
	}
	
	
	/**
	 * Receive the file from client
	 * 
	 * @param path wherein project be
	 * 
	 */
	public void receiveFile (String path) {
		
		// File name
		try {
			name = br.readLine();
		} catch (Exception e1) {
			Main.serverTextArea.setText("Problems to get name");
		}
		
		MultiThreadServer.serverText += name + "\n";
		Main.serverTextArea.setText(MultiThreadServer.serverText);
		
		if (name.contains(".jpg")) {
			
			// Change path
			if (path.contains(":")) {
				// Windows
				path = path + "\\assets\\background";
			} else {
				// Linux
				path = path + "/assets/background";
			}
			
			// If directory not exists then create
			File dir = new File (path);
			
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
		
		// Receive file
		try {
			// Confirm
			ps.println();
			ps.flush();
			
			fos = new FileOutputStream(new File(path, name));
			
			byte[] buffer = new byte[1024];
			int len = 0;
			
			while (((len = is.read(buffer)) != -1)) {
				fos.write(buffer, 0, len);
				fos.flush();
			}
			
		} catch (Exception e) {
			Main.serverTextArea.setText("Problems to read the buffer " + e.getMessage());
		} finally {
        	try {
        		// Close file
        		if (null != fos) {
        			fos.close();
        		}
        		
        	} catch (Exception e2) {
        		Main.serverTextArea.setText("Problems to read the buffer " + e2.getMessage());
        	}
        }
	}
	
	
	/**
	 * Thread for open streams, receive files,
	 * and for last close connection
	 * 
	 */
	public void run() {
		
		// Open stream
		try {
			os = connection.getOutputStream();
			ps = new PrintStream(os);
			is = connection.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
		} catch (Exception e) {
			Main.serverTextArea.setText("Problems to open stream " + e.getMessage());
		}
		
		// Receive file
		receiveFile(path);
		
		// Close the connection
		try {
			connection.close();
		} catch (IOException e) {
			Main.serverTextArea.setText("Problems to close the connection " + e.getMessage());
		}	
	
	}

}
