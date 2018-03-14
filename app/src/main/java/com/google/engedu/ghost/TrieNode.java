/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class TrieNode {

  private HashMap<Character, TrieNode> children;
  private boolean isWord;

  public TrieNode() {
    children = new HashMap<>();
    isWord = false;
  }

  public void add(String s) {
    HashMap<Character, TrieNode> hashMap = children;
    for (int i = 0; i < s.length(); i++) {
      if (!hashMap.containsKey(s.charAt(i))) {
        hashMap.put(s.charAt(i), new TrieNode());
      }
      if (i == s.length() - 1) {
        hashMap.get(s.charAt(i)).isWord = true;
      } else {
        hashMap = hashMap.get(s.charAt(i)).children;
      }
    }
  }

  public boolean isWord(String s) {
    TrieNode trieNode = searchWord(s);
    if (trieNode == null) {
      return false;
    } else {
      return trieNode.isWord;
    }
  }

  private TrieNode searchWord(String word) {
    TrieNode trieNode = this;
    for (int i = 0; i < word.length(); i++) {
      if (!trieNode.children.containsKey(word.charAt(i))) {
        return null;
      }
      trieNode = trieNode.children.get(word.charAt(i));
    }
    return trieNode;
  }

  public String getAnyWordStartingWith(String s) {
    TrieNode trieNode = searchWord(s);
    if (trieNode == null) {
      return null;
    }
    while (!trieNode.isWord) {
      for (Character c : trieNode.children.keySet()) {
        trieNode = trieNode.children.get(c);
        s += c;
        break;
      }
    }
    return s;
  }

  public String getGoodWordStartingWith(String s) {
    Random random = new Random();
    TrieNode trieNode = searchWord(s);

    if(trieNode == null){
      return null;
    }

    ArrayList<Character> completeWords = new ArrayList<>();
    ArrayList<Character> inCompleteWords = new ArrayList<>();
    Character c;

    while (true){
      completeWords.clear();
      inCompleteWords.clear();
      for(Character c1 : trieNode.children.keySet()){
        if(trieNode.children.get(c1).isWord){
          completeWords.add(c1);
        } else {
          inCompleteWords.add(c1);
        }
      }
      if(inCompleteWords.size() == 0){
        s += completeWords.get(random.nextInt(completeWords.size()));
        break;
      } else {
        c = inCompleteWords.get(random.nextInt(inCompleteWords.size()));
        s += c;
        trieNode = trieNode.children.get(c);
      }
    }

    return s;
  }
}
