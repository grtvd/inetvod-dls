/**
 * Copyright © 2008 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.dls.data;

public enum DownloadStatus
{
	NotStarted,
	InProgress,
	Completed;

	/* Constants */
	public static final int MaxLength = 32;

	/* Construction */
	public static DownloadStatus convertFromString(String value)
	{
		if((value == null) || (value.length() == 0))
			return null;

		DownloadStatus downloadStatus = valueOf(value);
		if(downloadStatus != null)
			return downloadStatus;

		return null;
	}

	public static String convertToString(DownloadStatus value)
	{
		if(value == null)
			return null;
		return value.toString();
	}
}
