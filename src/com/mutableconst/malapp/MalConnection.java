package com.mutableconst.malapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

public class MalConnection 
{
	public final static String malSite = "http://myanimelist.net/";
	public final static String verifyCredentialsUrl = "api/account/verify_credentials.xml"; 
	public final static String animeSearchUrl = "api/anime/search.xml?q=";
	public final static String mangaSearchUrl = "api/manga/search.xml?q=";
	
	public static MalConnection malConnection = null;
	private static HttpURLConnection urlConnection = null; 
	
	public static boolean isConnected = false;
	
	/**
	 * Singleton connection class
	 */
	private MalConnection()
	{
	}
	
	/**
	 * Returns this
	 * @return
	 */
	public static MalConnection getConnector()
	{
		if(malConnection == null)
		{
			malConnection = new MalConnection();
		}
		
		return malConnection;
	}
	
	/**
	 * Returns true if connection was successfull
	 * @return
	 */
	public boolean createConnection()
	{
		URL malUrl = null;
		try
		{
			malUrl = new URL(malSite);
		}
		catch(MalformedURLException e)
		{
			System.out.println("Error creating MAL URL: " + e.toString());
			return false;
		}
		
		if(malUrl != null)
		{
			try
			{
				urlConnection = (HttpURLConnection) malUrl.openConnection();
			}
			catch(IOException e)
			{
				System.out.println("Error opening connection to MAL: "+ e.toString());
				return false;
			}
		}
		
		isConnected = true;
		
		return true;
	}
	
	public boolean verifyCredentials(String username, String password)
	{
		//I dont know why I have to do this lol
		final String usernameFinal = username;
		final String passwordFinal = password;
		
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(usernameFinal, passwordFinal.toCharArray());
			}
		});

		
		if(isConnected)
		{
			BufferedReader br;
			try
			{
				URL malUrl = new URL(malSite + verifyCredentialsUrl);
				urlConnection = (HttpURLConnection) malUrl.openConnection();

				System.out.println(malSite + verifyCredentialsUrl);
				InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
				br = new BufferedReader(in); 
				
				String line = br.readLine();
				while(line != null)
				{
					System.out.println(line);
					line = br.readLine();
				}
			
				return true;
			}
			catch(IOException e)
			{
				System.out.println("Error reading input stream verifiying credentials: " + e.toString());
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
}
