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
import java.util.ArrayList;


/**
* This class create and modify the list of
* scene exits
*
* @author Jose Ignacio Madan Frias
* @version 1.0 November 2, 2014
* 
*/

public class ListLinkScene implements Serializable {

	private static final long serialVersionUID = -1108904368574116450L;
	private ArrayList<LinkSceneNode> listLinksScenes;
	
	/**
	 * Initiate the list.
	 * 
	 */
	public ListLinkScene () {
		this.listLinksScenes = new ArrayList<LinkSceneNode>();
	}
	
	/**
	 * Add node to list.
	 * 
	 */
	public void addNode (LinkSceneNode node) {
		this.listLinksScenes.add(node);
	}
	
	/**
	 * Delete node from list.
	 * 
	 */
	public void deleteNode (int idLinkScene) {
		this.listLinksScenes.remove(idLinkScene);
	}
	
	/**
	 * @return size of the list
	 * 
	 */
	public int getSize() {
		return this.listLinksScenes.size();
	}
	
	/**
	 * @return true if at least one node exists
	 * 
	 */
	public boolean exists() {
		return getSize() != 0;
	}
	
	/**
	 * @return node of the list
	 * 
	 */
	public LinkSceneNode getLinkSceneNode (int idLinkScene) {
		return this.listLinksScenes.get(idLinkScene);
	}

}
