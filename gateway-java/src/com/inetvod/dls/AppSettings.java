/**
 * Copyright © 2008 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.dls;

import java.io.File;

public class AppSettings
{
	/* Constants */
	private static final String AppDirName = "Storm Media Player";

	/* Fields */
	private static String fAppDataPath;

	/* Getters and Setters */
	public static String getAppDataPath() { return fAppDataPath; }

	/* Construction */
	static
	{
		initialize();
	}

	private static void initialize()
	{
		File file = new File("c:/ProgramData", AppDirName);
		if(file.exists())
		{
			fAppDataPath = file.getAbsolutePath();
			return;
		}

		file = new File("c:/Documents and Settings/All Users/Application Data", AppDirName);
		if(file.exists())
		{
			fAppDataPath = file.getAbsolutePath();
			//return;
		}
	}

	/* Implementation */
}
