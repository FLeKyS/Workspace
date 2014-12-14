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
* This class represent a scene exit
*
* @author Jose Ignacio Madan Frias
* @version 1.0 November 2, 2014
* 
*/

public class LinkSceneNode implements Serializable {
	private static final long serialVersionUID = -1568610551548799769L;
	private String linkSceneName;
	private int x;
	private int y;
	private int width;
	private int height;
	private String text;
	
	
	public LinkSceneNode (String linkScene, int x, int y, int width, int height, String text) {
		this.linkSceneName = linkScene;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
	}
	
	
	public String getLinkSceneName() {
		return linkSceneName;
	}
	
	public void setLinkSceneName(String linkScene) {
		this.linkSceneName = linkScene;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
