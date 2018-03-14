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

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {

  private ArrayList<String> words;
  private Random random = new Random();

  public SimpleDictionary(InputStream wordListStream) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
    words = new ArrayList<>();
    String line = null;
    while ((line = in.readLine()) != null) {
      String word = line.trim();
      if (word.length() >= MIN_WORD_LENGTH) {
        words.add(line.trim());
      }
    }
  }

  @Override
  public boolean isWord(String word) {
    return words.contains(word);
  }

  @Override
  public String getAnyWordStartingWith(String prefix) {
    if (prefix.equals("")) {
      return words.get(random.nextInt(words.size()));
    } else {
      int index = binSearch(words, prefix);
      if(index == -1){
        return null;
      } else {
        Log.e("Searched word", words.get(index));
        return words.get(index);
      }
    }
  }

  @Override
  public String getGoodWordStartingWith(String prefix) {
    String selected = null;
    return selected;
  }

  private int binSearch(ArrayList<String> words, String prefix) {
    int beg = 0, end = words.size();
    while (beg < end) {
      int mid = (beg + end) / 2;
      if(words.get(mid).length() < prefix.length()) beg = mid+1;
      else {
        String word = words.get(mid).substring(0, prefix.length());
        if (word.equals(prefix)) {
          return mid;
        } else if (word.compareTo(prefix) > 0) {
          end = mid - 1;
        } else if (word.compareTo(prefix) < 0) {
          beg = mid + 1;
        }
      }
    }
    return -1;
  }
}
