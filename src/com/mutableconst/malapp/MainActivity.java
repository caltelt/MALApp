package com.mutableconst.malapp;

import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends Activity 
{
	static final String PREFERENCES = "PrefsFile";
	static String username = "";
	static String password = "";
	static boolean rememberLogin = false;;
	
	EditText usernameText = null;
	EditText passwordText = null;
	Button loginButton = null;
	CheckBox rememberLoginCheckBox = null;
	ProgressBar loginProgressBar = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Create gui elements
		usernameText = (EditText) findViewById(R.id.username);
		passwordText = (EditText) findViewById(R.id.password);
		loginButton = (Button) findViewById(R.id.loginButton);
		rememberLoginCheckBox = (CheckBox) findViewById(R.id.rememberLogin);
		loginProgressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
		
		//Get preferences stored for last time saved
		SharedPreferences settings = getSharedPreferences(PREFERENCES, 0);
		username = settings.getString("username", "");
		password = settings.getString("password", "");
		rememberLogin = settings.getBoolean("rememberLogin", false);
		fillLogin();
	}

	/**
	 * et the login gui elements if rememberLogin is checked
	 */
	private void fillLogin()
	{
		if(rememberLogin)
		{
			usernameText.setText(username);
			passwordText.setText(password);
			rememberLoginCheckBox.setChecked(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	/**
	 * when login button is clicked, if remember login is checked, 
	 * set preferences for future reference, then try to login. 
	 * @param v
	 */
	public void loginClicked(View v)
	{
		SharedPreferences settings = getSharedPreferences(PREFERENCES, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		boolean rememberLogin = rememberLoginCheckBox.isChecked();
		
		if(rememberLogin)
		{
			editor.putString("username", usernameText.getText().toString());
			editor.putString("password", passwordText.getText().toString());
		}
		
		loginProgressBar.setVisibility(android.view.View.VISIBLE);
		
		editor.commit();
		
		MalConnection connection = MalConnection.getConnector();
		if(connection.createConnection())
		{
			connection.verifyCredentials(username, password);
		}
		
	}
}
