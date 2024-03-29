///*
//Copyleft (C) 2005 Hélio Perroni Filho
//xperroni@yahoo.com
//ICQ: 2490863
//
//This file is part of ChatterBean.
//
//ChatterBean is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
//
//ChatterBean is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License along with ChatterBean (look at the Documents/ directory); if not, either write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA, or visit (http://www.gnu.org/licenses/gpl.txt).
//*/
//
//package bitoflife.chatterbean.aiml;
//
//import bitoflife.chatterbean.AliceBot;
//import bitoflife.chatterbean.Context;
//import bitoflife.chatterbean.Match;
//import junit.framework.TestCase;
//
//public class SystemTest extends TestCase
//{
//  /*
//  Attributes
//  */
//
//  private java.lang.System tag;
//
//  /*
//  Events
//  */
//
//  protected void setUp()
//  {
//
//  }
//
//  protected void tearDown()
//  {
//    tag = null;
//  }
//
//  /*
//  Methods
//  */
//
//  public void testParse()
//  {
//    tag = new java.lang.System("result = \"Hello System!\"");
//    AliceBot bot = new AliceBot();
//    bot.setContext(new Context());
//    Match match = new Match();
//    match.setCallback(bot);
//
//    assertEquals("Hello System!", tag.process(match));
//  }
//
//  public void testArithmetics()
//  {
//    tag = new java.lang.System("1 + 1");
//    AliceBot bot = new AliceBot();
//    bot.setContext(new Context());
//    Match match = new Match();
//    match.setCallback(bot);
//
//    assertEquals("2", tag.process(match));
//  }
//}