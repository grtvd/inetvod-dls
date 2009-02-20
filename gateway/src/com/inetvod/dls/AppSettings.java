/**
 * Copyright © 2008-2009 iNetVOD, Inc. All Rights Reserved.
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
		// Gateway applet can't access restricted directories, so fAppDataPath was changed to match local show path.
		fAppDataPath = determineLocalShowPath();
	}

	/* Implementation */

	public static String determineLocalShowPath()
	{
		File localShowPath = new File("c:/Users/Public/Videos");	//first try for Vista
		if(!localShowPath.exists())
			localShowPath = new File("c:/Documents and Settings/All Users/Documents/My Videos");	//then try for XP
		return (new File(localShowPath, AppDirName)).getAbsolutePath();
	}
}
