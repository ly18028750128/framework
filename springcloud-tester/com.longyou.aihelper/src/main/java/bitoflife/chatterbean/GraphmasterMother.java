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

import bitoflife.chatterbean.aiml.Category;
import bitoflife.chatterbean.aiml.Star;

public class GraphmasterMother {
  /*
  Methods
  */

  public Graphmaster newInstance() {
    Graphmaster root = new Graphmaster();

    root.append(new Category(" SAY _ AGAIN ", "What, again? \"", new Star(1), "\"."));
    root.append(new Category(" SAY IT NOW ", "Whatever you want..."));
    root.append(new Category(" SAY * ", new Star(1), "!"));
    root.append(new Category(" DO YOU SEE THE * IN MY EYES ", "Yes, I see the ", new Star(1), " in your eyes."));

    return root;
  }
}
