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

import java.io.Serializable;


/**
* This class represent a scene
*
* @author Jose Ignacio Madan Frias
* @version 1.0 November 15, 2014
* 
*/

public class SceneNode implements Serializable {
	private static final long serialVersionUID = 288579103211364312L;
	private String sceneName;
	private int idSceneX;
	private int idSceneY;
	private ListLinkScene exits;
	
	
	public SceneNode(String sceneName, int idSceneX, int idSceneY) {
		
		this.sceneName = sceneName;
		this.idSceneX = idSceneX;
		this.idSceneY = idSceneY;
	    	
		ListLinkScene lls = new ListLinkScene();
		this.exits = lls;
	}
	
	public String getSceneName() {
		return sceneName;
	}
	
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	
	public int getIdSceneX() {
		return idSceneX;
	}
	
	public void setIdSceneX(int idSceneX) {
		this.idSceneX = idSceneX;
	}
	
	public int getIdSceneY() {
		return idSceneY;
	}
	
	public void setIdSceneY(int idSceneY) {
		this.idSceneY = idSceneY;
	}
	
	public ListLinkScene getExits() {
		return exits;
	}
	
	public void setExits(ListLinkScene exits) {
		this.exits = exits;
	}
}
