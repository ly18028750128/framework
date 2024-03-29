/*
Copyleft (C) 2005 Hélio Perroni Filho
xperroni@yahoo.com
ICQ: 2490863

This file is part of ChatterBean.

ChatterBean is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

ChatterBean is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with ChatterBean (look at the Documents/ directory); if not, either write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA, or visit (http://www.gnu.org/licenses/gpl.txt).
*/

package bitoflife.chatterbean.text;

import java.util.Arrays;


/**
 * 请求对象
 */
public class Request {

  /**
   * 句子
   */
  private Sentence[] sentences;

  /**
   * 来源
   */
  private String original;

  /**
   * 构造
   */
  public Request() {
  }

  public Request(String original) {
    this.original = original;
  }

  /**
   * 复合构造
   */
  public Request(String original, Sentence... sentences) {
    this.original = original;
    this.sentences = sentences;
  }

  /**
   * 空句子
   *
   * @return
   */
  public boolean empty() {
    return (sentences == null || sentences.length == 0);
  }


  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Request)) {
      return false;
    }

    Request compared = (Request) obj;
    return original.equals(compared.original) &&
        Arrays.equals(sentences, compared.sentences);
  }


  /**
   * 最后一个句子
   *
   * @param index
   * @return
   */
  public Sentence lastSentence(int index) {
    return sentences[sentences.length - (1 + index)];
  }

  public String toString() {
    return original;
  }

  public String trimOriginal() {
    return original.trim();
  }

  public String getOriginal() {
    return original;
  }

  public void setOriginal(String original) {
    this.original = original;
  }

  public Sentence[] getSentences() {
    return sentences;
  }

  public Sentence getSentences(int index) {
    return sentences[index];
  }

  public void setSentences(Sentence[] sentences) {
    this.sentences = sentences;
  }
}