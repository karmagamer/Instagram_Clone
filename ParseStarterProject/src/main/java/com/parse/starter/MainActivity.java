/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.media.Image;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener {

  TextView changeSignUp;
  Button signUpButton;
  EditText passwordEditText;
  Boolean signUpModeActive = true;

  public void showUserList(){
    Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
    startActivity(intent);
  }
  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {
    if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
      signUp(view);
    }
    return false;
  }
  @Override
  public void onClick(View view){
if(view.getId() == R.id.changeSignUp){
  signUpButton = (Button) findViewById(R.id.signUpButton);
  if(signUpModeActive){
    signUpModeActive = false;
    signUpButton.setText("Login");
    changeSignUp.setText("or, Sign Up");
  }
  else{
    signUpModeActive = true;
    signUpButton.setText("Sign Up");
    changeSignUp.setText("or, Login");
  }

}else if(view.getId()==R.id.backgroundRelative || view.getId() == R.id.logoImageView){
      InputMethodManager inputMethod = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethod.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0 );
    }
  }


public void signUp(View view){

  EditText UserNameeditText = (EditText) findViewById(R.id.UserNameeditText);
  passwordEditText = (EditText) findViewById(R.id.passwordEditText);

  if (UserNameeditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
    Toast.makeText(this, "Username & Password Required", Toast.LENGTH_SHORT).show();
  }
  else{
    if(signUpModeActive) {

      ParseUser user = new ParseUser();
      user.setUsername(UserNameeditText.getText().toString());
      user.setPassword(passwordEditText.getText().toString());
      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
          if (e == null) {
            Log.i("signUp", "Done!");
            showUserList();
          } else {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
          }
        }
      });
    }
    else{
      ParseUser.logInInBackground(UserNameeditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {
          if(user !=null){
Log.i("Sign Up"," Login Successful");
          showUserList();}
          else{
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
          }
        }
      });

    }
  }

}

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
      setTitle("Instagram Clone");
   changeSignUp = (TextView) findViewById(R.id.changeSignUp);

    changeSignUp.setOnClickListener(this);
    passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    passwordEditText.setOnKeyListener(this);
    RelativeLayout backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelative);
    ImageView imageLogo = (ImageView) findViewById(R.id.logoImageView);
    backgroundRelativeLayout.setOnKeyListener(this);
    imageLogo.setOnKeyListener(this);

    if(ParseUser.getCurrentUser() !=null){
      showUserList();
    }
    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }



}