/**
 * Copyright © 2008 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.dls.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Readable;
import com.inetvod.common.core.Writeable;

public class UserData implements Readable, Writeable
{
	/* Constants */
	public static final Constructor<UserData> CtorDataReader = DataReader.getCtor(UserData.class);

	/* Fields */
	private Settings fSettings;
	private ShowList fShowList;

	/* Getters and Setters */
	public Settings getSettings() { return fSettings; }
	public ShowList getShowList() { return fShowList; }

	/* Construction */
	public UserData(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */

	public void readFrom(DataReader reader) throws Exception
	{
		fSettings = reader.readObject("Settings", Settings.CtorDataReader);
		fShowList = reader.readList("Show", ShowList.Ctor,  Show.CtorDataReader);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeObject("Settings", fSettings);
		writer.writeList("Show", fShowList);
	}
}
