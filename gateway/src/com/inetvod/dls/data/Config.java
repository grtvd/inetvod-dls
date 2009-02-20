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

public class Config implements Readable, Writeable
{
	/* Constants */
	public static final Constructor<Config> CtorDataReader = DataReader.getCtor(Config.class);

	/* Fields */
	private General fGeneral;
	private Player fPlayer;

	/* Getters and Setters */
	public General getGeneral() { return fGeneral; }
	public Player getPlayer() { return fPlayer; }

	/* Construction */
	public Config(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fGeneral = reader.readObject("General", General.CtorDataReader );
		fPlayer = reader.readObject("Player", Player.CtorDataReader);
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeObject("General", fGeneral);
		writer.writeObject("Player", fPlayer);
	}
}
