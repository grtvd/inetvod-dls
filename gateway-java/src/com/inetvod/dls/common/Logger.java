/**
 * Copyright © 2008 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.dls.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;

import com.inetvod.common.core.DateUtil;
import com.inetvod.common.core.ISO8601DateTimeFormat;
import com.inetvod.common.core.LockableRandomAccessFile;
import com.inetvod.common.core.StrUtil;
import com.inetvod.dls.AppSettings;

public class Logger
{
	/* Constants */
	private static final String LogFileNameFormat = "log-%s.txt";

	/* Fields */
	private static boolean fLoggerEnabled;
	private static File fLogFile;

	/* Getters and Setters */

	/* Construction */
	public static void initialize(boolean enable)
	{
		fLoggerEnabled = enable;

		if(fLoggerEnabled)
		{
			File logDir = (new File(AppSettings.getAppDataPath(), "logs")).getAbsoluteFile();
			logDir.mkdirs();

			fLogFile = new File(logDir, String.format(LogFileNameFormat, DateUtil.formatDate(DateUtil.today(),
				"yyyy-MM-dd")));
		}
	}

	/* Implementation */
	private static void log(String type, String objClass, String method, String message, Exception e)
	{
		if(!fLoggerEnabled)
			return;

		StringBuilder sb = new StringBuilder();

		sb.append((new ISO8601DateTimeFormat()).format(DateUtil.now()));
		sb.append("|");
		sb.append(type);
		sb.append("|");
		sb.append(objClass);
		sb.append(".");
		sb.append(method);

		if(StrUtil.hasLen(message))
		{
			sb.append("|");
			sb.append(message);
		}

		if(e != null)
		{
			sb.append("|");
			Writer writer = new CharArrayWriter();
			e.printStackTrace(new PrintWriter(writer));
			sb.append(writer.toString());
		}

		addendLogFile(sb.toString());
	}

	private static void addendLogFile(String text)
	{
		if(fLogFile != null)
		{
			try
			{
				LockableRandomAccessFile lockableFile = LockableRandomAccessFile.newInstance(fLogFile);
				if(lockableFile != null)
				{
					try
					{
						ByteArrayOutputStream output = new ByteArrayOutputStream();
						PrintWriter printWriter = new PrintWriter(output);
						printWriter.println(text);
						printWriter.flush();
						lockableFile.appendFile(new ByteArrayInputStream(output.toByteArray()));
						return;
					}
					finally
					{
						lockableFile.close();
					}
				}
			}
			catch(Exception ignore)
			{
				//System.out.println(String.format("addendLogFile: Exception: %s", e));
			}
		}

		System.out.println(text);
	}

	public static void logInfo(Class objClass, String method, String message, Exception e)
	{
		log("INFO", objClass.getName(), method, message, e);
	}

	public static void logInfo(Class objClass, String method, String message)
	{
		logInfo(objClass, method, message, null);
	}

	public static void logInfo(Class objClass, String method, Exception e)
	{
		logInfo(objClass, method, null, e);
	}

	public static void logInfo(Object objClass, String method, String message, Exception e)
	{
		logInfo(objClass.getClass(), method, message, e);
	}

	public static void logInfo(Object objClass, String method, String message)
	{
		logInfo(objClass.getClass(), method, message, null);
	}

	public static void logInfo(Object objClass, String method, Exception e)
	{
		logInfo(objClass.getClass(), method, null, e);
	}
}
