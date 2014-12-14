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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.Intent;
import android.util.Xml;


/**
* Class to create and modify a scene list,
* furthermore create xml files
*
* @author Jose Ignacio Madan Frias
* @version 1.0 December 5, 2014
* 
*/

public class ListScene implements Serializable {

	private static final long serialVersionUID = 3455179026920177714L;

	private static ArrayList<SceneNode> listScenes;
	
	/**
	 * Initiate list.
	 * 
	 */
	public static void initialListScenes() {
		if (getListScenes() != null) {
			getListScenes().clear();
		} else {
			setListScenes(new ArrayList<SceneNode>());
		}
		
	}

	/**
	 * Add node to list.
	 * 
	 * @param SceneNode
	 */
	public static void addNode (SceneNode node) {
		getListScenes().add(node);
	}
	
	/**
	 * Delete node form list.
	 * 
	 * @param sceneName
	 * 
	 */
	public static void deleteNode (String sceneName) {
		int idScene = getIdScene(sceneName);
		
		getListScenes().remove(idScene);
	}
	
	/**
	 * Modify list node
	 * 
	 * @param sceneName
	 * @param newSceneName
	 * 
	 */
	public static void modifyNode (String sceneName, String newSceneName) {
		getListScenes().get(getIdScene(sceneName)).setSceneName(newSceneName);
	}
	
	/**
	 * Modify list node
	 * 
	 * @param idScene
	 * @param listLinkScenes
	 * 
	 */
	public static void modifyNode (int idScene, ListLinkScene listLinkScenes) {
		getListScenes().get(idScene).setExits(listLinkScenes);
	}
	
	/**
	 * Modify list link node
	 * 
	 * @param idScene
	 * @param idLinkScene
	 * @param nameLinkScene
	 * @param text
	 * 
	 */
	public static void modifyNode (String sceneName, int idLinkScene, String nameLinkScene, String text) {
		getListScenes().get(getIdScene(sceneName)).getExits().getLinkSceneNode(idLinkScene).setLinkSceneName(nameLinkScene);
		getListScenes().get(getIdScene(sceneName)).getExits().getLinkSceneNode(idLinkScene).setText(text);
	}
	
	/**
	 * Get text from Link Scene node
	 * 
	 * @param sceneName
	 * @param idLinkScene
	 * 
	 * @return text from link scene node
	 * 
	 */
	public static String getTextLinkNode (String sceneName, int idLinkScene) {
		return getListScenes().get(getIdScene(sceneName)).getExits().getLinkSceneNode(idLinkScene).getText();
	}
	
	/**
	 * @param sceneName
	 * @return true if node exists in the list
	 * 
	 */
	public static boolean existsNode (String sceneName) {
		int i = 0;
		int size = getListScenes().size();
		
		while ((i < size) && (!getListScenes().get(i).getSceneName().equals(sceneName))) {
			i++;
		}
		
		return i != size;
	}
	
	/**
	 * @return size of the list
	 * 
	 */
	public static int getSize() {
		return getListScenes().size();
	}
	
	/**
	 * Return scene name from node
	 * 
	 * @param idScene
	 * @return scene name
	 */
	public static String getSceneName (int idScene) {
		return getListScenes().get(idScene).getSceneName();
	}
	
	/**
	 * @return true if at least one node exists
	 * 
	 */
	public static boolean existsListScene() {
		return (getListScenes().size() != 0);
	}
	
	/**
	 * Remove exits list from node
	 * 
	 * @param sceneName
	 * 
	 */
	public static void deleteLinkNodes(String sceneName) {
		int idScene = getIdScene(sceneName);
		ListLinkScene lls = getListScenes().get(idScene).getExits();
		int size = lls.getSize();
		
		for (int i = 0; i < size; i++) {
			lls.deleteNode(0);
		}
	}
	
	/**
	 * Remove exit node from exits list
	 * 
	 * @param sceneName
	 * @param idLinkScene
	 * 
	 */
	public static void deleteLinkNode (String sceneName, int idLinkScene) {
		getListScenes().get(getIdScene(sceneName)).getExits().deleteNode(idLinkScene);
	}
	
	/**
	 * @param idScene
	 * @return true if exits list exists
	 * 
	 */
	public static boolean existsLinkList (int idScene) {
		return (getListScenes().get(idScene).getExits().exists());
	}
	
	/**
	 * @param sceneName
	 * @return exits list
	 * 
	 */
	public static ListLinkScene getListLinkScene (String sceneName) {
		return (getListScenes().get(getIdScene(sceneName)).getExits());
	}
	
	
	/**
	 * Check if exists at least one error in the list link scenes
	 * 
	 * @param sceneNames list of scene names
	 * 
	 * @return correct true if error not exist
	 * 
	 */
	public static boolean getCorrect(ArrayList<String> sceneNames) {
		int i = 0;
		
		// Rapid Check for errors
		while ((i < getListScenes().size()) && (!existsErrorLinkSceneName (i, sceneNames))) {
			i++;
		}
		
		return (i == getListScenes().size());
	}
	
	/**
	 * @return id of the correspondent scene
	 * 
	 * @param sceneName
	 * 
	 */
	public static int getIdScene(String sceneName){
		int i = 0;
		int size = getListScenes().size();
		
		while ((i < size) && (!getListScenes().get(i).getSceneName().equals(sceneName))) {
			i++;
		}
		
		return i;
	}
	
	/**
	 * @return x position from scene in the eAdventure gallery
	 * 
	 */
	public static int getIdSceneX() {
		int idSceneX = 8;
		
		if (existsListScene()) {
			idSceneX = getListScenes().get(getListScenes().size() - 1).getIdSceneX();
		} else {
			idSceneX = idSceneX - 160;
		}
		
		if (idSceneX == 648) {
			// New file
			return 8;
		} else {
			return (idSceneX + 160);
		}
	}
	
	/**
	 * @return y position from scene in the eAdventure gallery
	 * 
	 */
	public static int getIdSceneY() {
		int idSceneX = 8;
		int idSceneY = 30;
		
		if (existsListScene()) {
			idSceneX = getListScenes().get(getListScenes().size() - 1).getIdSceneX();
			idSceneY = getListScenes().get(getListScenes().size() - 1).getIdSceneY();
		}
		
		if (idSceneX == 648) {
			// New file
			return (idSceneY + 120);
		} else {
			return (idSceneY);
		}
	}
	
	/**
	 * Save the list in tmp file
	 * 
	 * @param path_dir wherein project be
	 * 
	 */
	public static void saveTempListScene(String path_dir) {
	    try{
		    FileOutputStream fos = new FileOutputStream(path_dir + "listScene.tmp");
		    GZIPOutputStream gz = new GZIPOutputStream(fos);
		 
		    ObjectOutputStream oos = new ObjectOutputStream(gz);
		 
		    oos.writeObject(getListScenes());
		    oos.close();
	 
		   } catch(Exception e){}	
	}
	
	/**
	 * Read the list from tmp file
	 * 
	 * @param wherein project be
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void readTempListScene(String path_dir) {
		try {
		    FileInputStream fin = new FileInputStream(path_dir + "listScene.tmp");
			GZIPInputStream gis = new GZIPInputStream(fin);
			ObjectInputStream ois = new ObjectInputStream(gis);
		    setListScenes((ArrayList<SceneNode>) ois.readObject());
		    ois.close();
		    
		} catch (Exception e) {}
	}
	
	/**
	 * Correct the scenes in the list (if not exists remove it)
	 * 
	 * @param sceneNames
	 * 
	 */
	public static void correctScenes (ArrayList<String> sceneNames) {
		for (int i = 0; i < getListScenes().size(); i++) {
			if(!sceneNames.contains(getListScenes().get(i).getSceneName())) {
				getListScenes().remove(i);
			}
		}
	}
	
	/**
	 * Detect error in the exits list
	 * 
	 * @param sceneId
	 * @param sceneNames
	 * 
	 */
	public static boolean existsErrorLinkSceneName (int sceneId, ArrayList<String> sceneNames) {
		int i = 0;
		
		while ((i < getListScenes().get(sceneId).getExits().getSize()) 
			&& (sceneNames.contains(getListScenes().get(sceneId).getExits().getLinkSceneNode(i).getLinkSceneName()))) {
			i++;
		}
		
		return (i != getListScenes().get(sceneId).getExits().getSize());
	}
	
	/**
	 * Correct the exits from specific sceneName
	 * 
	 * @param sceneName
	 * @param path_dir wherein project be
	 * @param c context of the scenes activity
	 * 
	 */
	public static void correctLinkScenes (String sceneName, String path_dir, Context c) {

		for (int i = 0; i < getListScenes().get(getIdScene(sceneName)).getExits().getSize(); i++) {
			
			// Correct the Scene Link Name
		    Intent intent = new Intent(c, CorrectLinkScene.class);
		    intent.putExtra("path_dir", path_dir);
		    intent.putExtra("name", sceneName);
		    intent.putExtra("idLinkScene", i);
	        
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    
		    c.startActivity(intent);
		}
	}
	
	/**
	 * If Scene name change, update the correspondent links scenes to him
	 * 
	 * @param sceneName preview sceneName
	 * @param newSceneName last sceneName
	 * 
	 */
	public static void updateLinkScenes (String sceneName, String newSceneName) {
		ListLinkScene lls;
		LinkSceneNode lsn;
		
		for (int i = 0; i < getSize(); i++) {
			
			if (!getListScenes().get(i).getSceneName().equals(newSceneName)) {
				
				lls = getListScenes().get(i).getExits();
				
				for (int j = 0; j < lls.getSize(); j++) {
					lsn = lls.getLinkSceneNode(j);
					
					// Update scene links
					if (lsn.getLinkSceneName().equals(sceneName)) {
						lsn.setLinkSceneName(newSceneName);
					}
				}
			}
		}
	}
	
	/**
	 * If Scene delete, update the correspondent links scenes to him
	 * 
	 * @param sceneName preview sceneName
	 * 
	 */
	public static void updateLinkScenes (String sceneName) {
		ListLinkScene lls;
		LinkSceneNode lsn;
		
		for (int i = 0; i < getSize(); i++) {
			
			lls = getListScenes().get(i).getExits();
			
			for (int j = 0; j < lls.getSize(); j++) {
				lsn = lls.getLinkSceneNode(j);
				
				// Update scene links
				if (lsn.getLinkSceneName().equals(sceneName)) {
					lls.deleteNode(j);
				}
			}
		}
	}
	
	/**
	 * Read descriptor.xml to get graphic mode
	 * 
	 * @return graphic mode
	 * 
	 */
	public static String getGraphicMode(String path_dir) {
		FileInputStream fin = null;
		String graphic_mode = "";

		try {
			fin = new FileInputStream(path_dir + "/descriptor.xml");
		} catch (Exception e) {}

		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(fin, "UTF-8");

			int event = parser.next();
			
			while(event != XmlPullParser.END_DOCUMENT) {
				
				if(event == XmlPullParser.START_TAG) {
					if (parser.getName().equals("graphics")) {
						graphic_mode = parser.getAttributeValue(0);
						break;
					}
				}

				event = parser.next();
			}
			
			// Close file
			fin.close();
			
		} catch (Exception e) {}
		
		return graphic_mode;
	}
	
	/**
	 * Create the project xml file.
	 * 
	 * @param path_dir wherein project be
	 * 
	 */
	public static void createProjectXml (String path_dir) {
		FileWriter fileW = null;
        PrintWriter pw = null;
        
        try {
            fileW = new FileWriter(new File(path_dir, "project.xml"));
            pw = new PrintWriter(fileW);
 
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pw.println("<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">");
            pw.println("<properties>");
            pw.println("\t<comment>Project Configuration</comment>");
            
            // Create scene position in the box
            for (int i = 0; i < getListScenes().size(); i++) {
            	pw.println("\t<entry key=\"Chapter0." + getListScenes().get(i).getSceneName() + ".Visible\">true</entry>");
            	
            	// X and Y position
            	pw.println("\t<entry key=\"Chapter0." + 
            			getListScenes().get(i).getSceneName() + ".X\">" + 
            			getListScenes().get(i).getIdSceneX() + "</entry>");
            	pw.println("\t<entry key=\"Chapter0." + 
            			getListScenes().get(i).getSceneName() + ".Y\">" + 
            			getListScenes().get(i).getIdSceneY() + "</entry>");
            }
            
            pw.println("\t<entry key=\"autosave\">yes</entry>");
            pw.println("</properties>");
 
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
        	try {
        		// Close file
        		if (null != fileW) {
        			fileW.close();
        		}
        		
        	} catch (Exception e2) {
        		e2.printStackTrace();
        	}
        }
	}
	
	/**
	 * Create the chapter1 xml file
	 * 
	 * @param path_dir wherein project be
	 * 
	 */
	public static void createChapter1Xml (String path_dir) {
		FileWriter fileW = null;
        PrintWriter pw = null;
        String first = "no";
        
        try {
            fileW = new FileWriter(new File(path_dir, "chapter1.xml"));
            pw = new PrintWriter(fileW);
 
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pw.println("<!DOCTYPE eAdventure SYSTEM \"eadventure.dtd\">");
            pw.println("<eAdventure>");
            pw.println("\t<comment>Project Configuration</comment>");
            
            // Create scene with exits
            for (int i = 0; i < getListScenes().size(); i++) {
            	if (i == 0) {
            		first = "yes";
            	} else {
            		first = "no";
            	}
            	
            	pw.println("\t<scene start=\"" + first + "\" playerScale=\"1.0\" playerLayer=\"-1\" id=\"" + 
            	getListScenes().get(i).getSceneName() + "\">");
            	
            	pw.println("\t\t<resources name=\"No name\">");
            	pw.println("\t\t\t<asset uri=\"assets/background/" + 
            	getListScenes().get(i).getSceneName() + ".jpg\" type=\"background\"/>");
            	pw.println("\t\t</resources>");
            	pw.println("\t\t<name/>");
            	pw.println("\t\t<default-initial-position y=\"300\" x=\"400\"/>");
            	
            	// If exits exists
            	if (existsLinkList (i))  {
            		
            		pw.println("\t\t<exits>");
            		
            		for (int j = 0; j < getListScenes().get(i).getExits().getSize(); j++) {
            			
            			// X, Y, Width, Height, linkSceneName
                		pw.println("\t\t\t<exit y=\"" + 
                		getListScenes().get(i).getExits().getLinkSceneNode(j).getY() + "\" x=\"" + 
                		getListScenes().get(i).getExits().getLinkSceneNode(j).getX() + "\" width=\"" + 
                		getListScenes().get(i).getExits().getLinkSceneNode(j).getWidth() + 
                		"\" transitionType=\"0\" transitionTime=\"0\"" + 
                		" rectangular=\"yes\" not-effects=\"no\" idTarget=\"" + 
                		getListScenes().get(i).getExits().getLinkSceneNode(j).getLinkSceneName() + "\" height=\"" +
                		getListScenes().get(i).getExits().getLinkSceneNode(j).getHeight() + 
                		"\" hasInfluenceArea=\"no\" destinyY=\"-2147483648\" destinyX=\"-2147483648\">");
                		
                		// Text
                		pw.println("\t\t\t\t<exit-look text=\"" + 
                		getListScenes().get(i).getExits().getLinkSceneNode(j).getText() + "\"/>");
                		pw.println("\t\t\t</exit>");
            		}
            		
            		pw.println("\t\t</exits>");
            	}
            	
            	pw.println("\t</scene>");
            
            }
            
            // Default values
            if (!existsListScene()) {
            	pw.println("\t<scene start=\"yes\" playerScale=\"1.0\" playerLayer=\"-1\" id=\"" + 
            	"IdEscena\">");
            	
            	pw.println("\t\t<resources name=\"No name\">");
            	pw.println("\t\t\t<asset uri=\"assets/special/" + 
            			"EmptyBackground.png\" type=\"background\"/>");
            	pw.println("\t\t</resources>");
            	pw.println("\t\t<name/>");
            	pw.println("\t\t<default-initial-position y=\"300\" x=\"400\"/>");
            	pw.println("\t</scene>");
            }
                    
            pw.println("\t<player>");
            pw.println("\t\t<resources name=\"No name\">");
            pw.println("\t\t\t<asset uri=\"assets/special/EmptyAnimation\" type=\"walkright\"/>");
            pw.println("\t\t\t<asset uri=\"assets/special/EmptyAnimation\" type=\"speakup\"/>");
            pw.println("\t\t\t<asset uri=\"assets/special/EmptyAnimation\" type=\"speakright\"/>");
            pw.println("\t\t\t<asset uri=\"assets/special/EmptyAnimation\" type=\"speakleft\"/>");
            pw.println("\t\t\t<asset uri=\"assets/special/EmptyAnimation\" type=\"walkdown\"/>");
            pw.println("\t\t\t<asset uri=\"assets/special/EmptyAnimation\" type=\"standdown\"/>");
            pw.println("\t\t\t<asset uri=\"assets/special/EmptyAnimation\" type=\"useright\"/>");
            pw.println("\t\t\t<asset uri=\"assets/special/EmptyAnimation\" type=\"standleft\"/>");
            pw.println("\t\t\t<asset uri=\"assets/special/EmptyAnimation\" type=\"useleft\"/>");
            pw.println("\t\t\t<asset uri=\"assets/special/EmptyAnimation\" type=\"standup\"/>");
            pw.println("\t\t\t<asset uri=\"assets/special/EmptyAnimation\" type=\"walkleft\"/>");
            pw.println("\t\t\t<asset uri=\"assets/special/EmptyAnimation\" type=\"speakdown\"/>");
            pw.println("\t\t\t<asset uri=\"assets/special/EmptyAnimation\" type=\"standright\"/>");
            pw.println("\t\t\t<asset uri=\"assets/special/EmptyAnimation\" type=\"walkup\"/>");
            pw.println("\t\t</resources>");
            pw.println("\t\t<textcolor showsSpeechBubble=\"no\"" + 
            " bubbleBorderColor=\"#00000\" bubbleBkgColor=\"#FFFFFF\">");
            pw.println("\t\t\t<frontcolor color=\"#FFFFFF\"/>");
            pw.println("\t\t\t<bordercolor color=\"#000000\"/>");
            pw.println("\t\t</textcolor>");
            pw.println("\t\t<description>");
            pw.println("\t\t\t<name/>");
            pw.println("\t\t\t<brief/>");
            pw.println("\t\t\t<detailed/>");
            pw.println("\t\t</description>");
            pw.println("\t\t<voice name=\"\" synthesizeAlways=\"no\"/>");
            pw.println("\t</player>");
            pw.println("</eAdventure>");
 
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
        	try {
        		// Close file
        		if (null != fileW) {
        			fileW.close();
        		}
        		
        	} catch (Exception e2) {
        		e2.printStackTrace();
        	}
        }
	}

	public static ArrayList<SceneNode> getListScenes() {
		return listScenes;
	}

	public static void setListScenes(ArrayList<SceneNode> listScenes) {
		ListScene.listScenes = listScenes;
	}
}
