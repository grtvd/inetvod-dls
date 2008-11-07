/**
 * Copyright © 2008 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.dls.data;

import java.lang.reflect.Constructor;
import java.util.Date;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.Writeable;

public class Settings implements Readable, Writeable
{
	/* Constants */
	public static final int LocalShowPathMaxLength = 500;
	public static final int UserLogonIDMaxLength = 64;
	public static final int UserPINMaxLength = 32;

	public static final Constructor<Settings> CtorDataReader = DataReader.getCtor(Settings.class);

	/* Fields */
	private String fLocalShowPath;
	private int fMaxSizeForShows;
	private String fUserLogonID;
	private String fUserPIN;
	private boolean fRememberUserPIN;
	private boolean fEnableLog;
	private Date fNextProcessTime;

	/* Getters and Setters */
	public String getLocalShowPath() { return fLocalShowPath; }
	public void setLocalShowPath(String localShowPath) { fLocalShowPath = localShowPath; }

	public int getMaxSizeForShows() { return fMaxSizeForShows; }
	public void setMaxSizeForShows(int maxSizeForShows) { fMaxSizeForShows = maxSizeForShows; }

	public String getUserLogonID() { return fUserLogonID; }
	public void setUserLogonID(String userLogonID) { fUserLogonID = userLogonID; }

	public String getUserPIN() { return fUserPIN; }
	public void setUserPIN(String userPIN) { fUserPIN = userPIN; }

	public boolean getRememberUserPIN() { return fRememberUserPIN; }
	public void setRememberUserPIN(boolean rememberUserPIN) { fRememberUserPIN = rememberUserPIN; }

	public boolean getEnableLog() { return fEnableLog; }
	public void setEnableLog(boolean enableLog) { fEnableLog = enableLog; }

	public Date getNextProcessTime() { return fNextProcessTime; }
	public void setNextProcessTime(Date nextProcessTime) { fNextProcessTime = nextProcessTime; }

	/* Construction */
	public Settings(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fLocalShowPath = reader.readString("LocalShowPath", LocalShowPathMaxLength);
		fMaxSizeForShows = reader.readIntValue("MaxSizeForShows");
		fUserLogonID = reader.readString("UserLogonID", UserLogonIDMaxLength);
		fUserPIN = reader.readString("UserPIN", UserPINMaxLength);
		fRememberUserPIN = reader.readBooleanValue("RememberUserPIN", false);
		fEnableLog = reader.readBooleanValue("EnableLog", false);
		fNextProcessTime = reader.readDateTime("NextProcessTime");
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeString("LocalShowPath", fLocalShowPath, LocalShowPathMaxLength);
		writer.writeInt("MaxSizeForShows", fMaxSizeForShows);
		writer.writeString("UserLogonID", fUserLogonID, UserLogonIDMaxLength);
		writer.writeString("UserPIN", fUserPIN, UserPINMaxLength);
		writer.writeBooleanValue("RememberUserPIN", fRememberUserPIN);
		writer.writeBooleanValue("EnableLog", fEnableLog);
		writer.writeDateTime("NextProcessTime", fNextProcessTime);
	}
}
