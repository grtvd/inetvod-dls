/**
 * Copyright © 2008-2009 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.dls.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import com.inetvod.common.core.DateUtil;
import com.inetvod.common.core.LockableRandomAccessFile;
import com.inetvod.common.core.StreamUtil;
import com.inetvod.common.core.XmlDataReader;
import com.inetvod.common.core.XmlDataWriter;
import com.inetvod.dls.AppSettings;

public class UserDataMgr
{
	/* Constants */
	public static String UserFileName = "user.xml";
	private static String UserElement = "UserData";

	/* Fields */
	private static UserDataMgr fTheUserDataMgr;

	private File fUserFilePath;
	//TODO private long fNextProcessTimeIncMillis;	// Used by Service code

	private Settings fSettings;	// short-term cache of Settings data
	private ShowList fShowList;	// short-term cache of Shows data

	/* Getters and Setters */
	public String getLocalShowPath() { return fSettings.getLocalShowPath(); }
	public int getMaxSizeForShows() { return fSettings.getMaxSizeForShows(); }
	public String getUserLogonID() { return fSettings.getUserLogonID(); }
	public String getUserPIN() { return fSettings.getUserPIN(); }
	public boolean getRememberUserPIN() { return fSettings.getRememberUserPIN(); }
	public boolean getEnableLog() { return fSettings.getEnableLog(); }
	public ShowList getShowList() { return fShowList; }	//TODO: clone() ???

	/* Construction */
	@SuppressWarnings({ "UnusedDeclaration" })
	private UserDataMgr(String fileLocation, long nextProcessTimeIncSecs)
	{
		fUserFilePath = new File(fileLocation, UserFileName);
		//TODO fNextProcessTimeIncMillis = nextProcessTimeIncSecs * 1000;
	}

	public static void createNew() throws Exception
	{
		UserDataMgr userDataMgr = new UserDataMgr(AppSettings.getAppDataPath(), 0);
		userDataMgr.createDataFile();
	}

	public static UserDataMgr initialize(long nextProcessTimeIncSecs) throws Exception
	{
		fTheUserDataMgr = new UserDataMgr(AppSettings.getAppDataPath(), nextProcessTimeIncSecs);
		fTheUserDataMgr.refresh();
		return fTheUserDataMgr;
	}

	public static UserDataMgr getThe()
	{
		return fTheUserDataMgr;
	}

	/* Implementation */
	public void refresh() throws Exception
	{
		LockableRandomAccessFile lockableFile = LockableRandomAccessFile.newInstance(fUserFilePath);
		if(lockableFile != null)
		{
			try
			{
				readDataFile(lockableFile);
			}
			finally
			{
				lockableFile.close();
			}
		}
	}

	public void setMaxSizeForShows(int maxSizeForShows) throws Exception
	{
		LockableRandomAccessFile lockableFile = LockableRandomAccessFile.newInstance(fUserFilePath);
		if(lockableFile != null)
		{
			try
			{
				UserData userData = readDataFile(lockableFile);

				userData.getSettings().setMaxSizeForShows(maxSizeForShows);

				writeDataFile(lockableFile, userData);
			}
			finally
			{
				lockableFile.close();
			}
		}
	}

	public void setShowsConfigDetails(int maxSizeForShows, String pathForShows) throws Exception
	{
		LockableRandomAccessFile lockableFile = LockableRandomAccessFile.newInstance(fUserFilePath);
		if(lockableFile != null)
		{
			try
			{
				UserData userData = readDataFile(lockableFile);

				userData.getSettings().setMaxSizeForShows(maxSizeForShows);
				userData.getSettings().setLocalShowPath(pathForShows);

				writeDataFile(lockableFile, userData);
			}
			finally
			{
				lockableFile.close();
			}
		}
	}

	public void setUserCredentials(String userLogonID, String userPIN, boolean rememberUserPIN) throws Exception
	{
		LockableRandomAccessFile lockableFile = LockableRandomAccessFile.newInstance(fUserFilePath);
		if(lockableFile != null)
		{
			try
			{
				UserData userData = readDataFile(lockableFile);

				fSettings.setUserLogonID(userLogonID);
				fSettings.setUserPIN(userPIN);
				fSettings.setRememberUserPIN(rememberUserPIN);

				writeDataFile(lockableFile, userData);
			}
			finally
			{
				lockableFile.close();
			}
		}
	}

	public void setEnableLog(boolean enableLog) throws Exception
	{
		LockableRandomAccessFile lockableFile = LockableRandomAccessFile.newInstance(fUserFilePath);
		if(lockableFile != null)
		{
			try
			{
				UserData userData = readDataFile(lockableFile);

				userData.getSettings().setEnableLog(enableLog);

				writeDataFile(lockableFile, userData);
			}
			finally
			{
				lockableFile.close();
			}
		}
	}

	public void setNextProcessNow() throws Exception
	{
		LockableRandomAccessFile lockableFile = LockableRandomAccessFile.newInstance(fUserFilePath);
		if(lockableFile != null)
		{
			try
			{
				UserData userData = readDataFile(lockableFile);

				userData.getSettings().setNextProcessTime(DateUtil.now());

				writeDataFile(lockableFile, userData);
			}
			finally
			{
				lockableFile.close();
			}
		}
	}

	private void createDataFile() throws Exception
	{
		if(!fUserFilePath.getParentFile().exists())
			fUserFilePath.getParentFile().mkdirs();

		if(fUserFilePath.exists())
			return;

		String userFileXml = String.format(
			"<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<UserData>"
				+ "<Settings>"
					+ "<LocalShowPath>%s</LocalShowPath>"
					+ "<MaxSizeForShows>20</MaxSizeForShows>"
				+ "</Settings>"
			+ "</UserData>", AppSettings.determineLocalShowPath());
		StreamUtil.stringToFile(userFileXml, fUserFilePath);
	}

	private UserData readDataFile(LockableRandomAccessFile lockableFile) throws Exception
	{
		XmlDataReader reader = new XmlDataReader(lockableFile.readFile());
		UserData userData = reader.readObject(UserElement, UserData.CtorDataReader);

		fSettings = userData.getSettings();
		fShowList = userData.getShowList();

		return userData;
	}

	private static void writeDataFile(LockableRandomAccessFile lockableFile, UserData userData) throws Exception
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		XmlDataWriter writer = new XmlDataWriter(output);
		writer.writeObject(UserElement, userData);
		writer.flush();
		lockableFile.writeFile(new ByteArrayInputStream(output.toByteArray()));
	}
}
