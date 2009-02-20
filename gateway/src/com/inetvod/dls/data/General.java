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

public class General implements Readable, Writeable
{
	/* Constants */
	public static final int ServiceURLMaxLength = 500;

	public static final Constructor<General> CtorDataReader = DataReader.getCtor(General.class);

	/* Fields */
	private String fServiceURL;
	private long fLoopIntervalSecs;

	/* Getters and Setters */
	public String getServiceURL() { return fServiceURL; }
	public long getLoopIntervalSecs() { return fLoopIntervalSecs; }

	/* Construction */
	public General(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fServiceURL = reader.readString("ServiceURL", ServiceURLMaxLength);
		fLoopIntervalSecs = reader.readLongValue("LoopIntervalSecs");
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeString("ServiceURL", fServiceURL, ServiceURLMaxLength);
		writer.writeLongValue("LoopIntervalSecs", fLoopIntervalSecs);
	}
}
