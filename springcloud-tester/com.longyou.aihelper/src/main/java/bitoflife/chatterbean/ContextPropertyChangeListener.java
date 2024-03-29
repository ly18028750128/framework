/*
Copyleft (C) 2005 Hélio Perroni Filho
xperroni@yahoo.com
ICQ: 2490863

This file is part of ChatterBean.

ChatterBean is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

ChatterBean is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with ChatterBean (look at the Documents/ directory); if not, either write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA, or visit (http://www.gnu.org/licenses/gpl.txt).
*/

package bitoflife.chatterbean;

import java.beans.PropertyChangeListener;

public abstract class ContextPropertyChangeListener implements PropertyChangeListener {
  /*
  Attribute Section
  */

  /**
   * Name of the property whose changes to listen for.
   */
  private String name;
  
  /*
  Constructor Section
  */

  /**
   * Creates a new change listener for the named Context Property.
   *
   * @param name The name of the property whose changes this object listens for.
   */
  public ContextPropertyChangeListener(String name) {
    this.name = name;
  }
  
  /*
  Accessor Section
  */

  /**
   * Returns the name of the property whose changes this object listens for.
   *
   * @return The name of the property whose changes this object listens for.
   */
  public String name() {
    return name;
  }
}
