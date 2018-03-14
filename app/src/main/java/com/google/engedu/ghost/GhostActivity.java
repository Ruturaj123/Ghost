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

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {

  private static final String COMPUTER_TURN = "Computer's turn";
  private static final String USER_TURN = "Your turn";
  private GhostDictionary dictionary;
  private boolean userTurn = false;
  private Random random = new Random();
  private Button challengeButton, resetButton;
  private SimpleDictionary simpleDictionary;
  private FastDictionary fastDictionary;
  private TextView ghostText, label;
  private String alphabets = "abcdefghijklmnopqrstuvwxyz";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ghost);

    AssetManager assetManager = getAssets();
    try {
      fastDictionary = new FastDictionary(assetManager.open("words.txt"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    /**
     **
     **  YOUR CODE GOES HERE
     **
     **/
    onStart(null);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_ghost, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Handler for the "Reset" button.
   * Randomly determines whether the game starts with a user turn or a computer turn.
   *
   * @return true
   */
  public boolean onStart(View view) {
    userTurn = random.nextBoolean();
    ghostText = (TextView) findViewById(R.id.ghostText);
    ghostText.setText("");
    label = (TextView) findViewById(R.id.gameStatus);
    if (userTurn) {
      label.setText(USER_TURN);
    } else {
      label.setText(COMPUTER_TURN);
      computerTurn();
    }
    return true;
  }

  private void computerTurn() {
    label = (TextView) findViewById(R.id.gameStatus);
    // Do computer turn stuff then make it the user's turn again
    String fragment = ghostText.getText().toString();
    if (fragment.length() >= 4 && fastDictionary.isWord(fragment)) {
      label.setText("Computer Won!");
      Toast.makeText(this, "Computer won!", Toast.LENGTH_SHORT).show();
      return;
    } else {
      String anyWord = fastDictionary.getGoodWordStartingWith(fragment);
      if (anyWord == null) {
        label.setText("Computer Won!");
        Toast.makeText(this, "Computer won!", Toast.LENGTH_SHORT).show();
        return;
      } else {
        addTextToWord(anyWord.charAt(fragment.length()));
      }
    }
    userTurn = true;
    label.setText(USER_TURN);
  }

  /**
   * Handler for user key presses.
   *
   * @return whether the key stroke was handled.
   */
  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event) {
    /**
     **
     **  YOUR CODE GOES HERE
     **
     **/
    char c = (char) event.getUnicodeChar();
    if (Character.isLetter(c)) {
      userTurn = false;
      label.setText(COMPUTER_TURN);
      addTextToWord(c);
      computerTurn();
      return true;
    } else {
      Toast.makeText(GhostActivity.this, "Please enter the valid character!", Toast.LENGTH_SHORT)
          .show();
      return super.onKeyUp(keyCode, event);
    }
  }

  public void challengeHandler(View view){
    String fragment = ghostText.getText().toString();
    if(fragment.length() == 4 && fastDictionary.isWord(fragment)){
      label.setText("User won!");
      Toast.makeText(this, "User won!", Toast.LENGTH_SHORT).show();
    } else {
      if(fastDictionary.getGoodWordStartingWith(fragment) != null){
        label.setText("Computer Won!");
        Toast.makeText(this, "Computer won!", Toast.LENGTH_SHORT).show();
        ghostText.setText(fastDictionary.getGoodWordStartingWith(fragment));
      } else {
        label.setText("User won!");
        Toast.makeText(this, "User won!", Toast.LENGTH_SHORT).show();
      }
    }
  }

  private void addTextToWord(char c) {
    ghostText.setText(ghostText.getText().toString() + c);
  }
}
