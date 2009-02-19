/**
 * Copyright © 2008-2009 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.dls.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

import com.inetvod.common.core.LockableRandomAccessFile;
import com.inetvod.common.core.StreamUtil;
import com.inetvod.common.core.XmlDataReader;
import com.inetvod.common.core.XmlDataWriter;
import com.inetvod.dls.AppSettings;

public class ConfigDataMgr
{
	/* Constants */
	public static String ConfigFileName = "config.xml";
	private static String ConfigElement = "Config";

	/* Fields */
	private static ConfigDataMgr fTheConfigDataMgr;
	private File fConfigFilePath;

	private Config fConfig;

	/* Getters and Setters */
	public Config getConfig() { return fConfig; }

	/* Construction */
	private ConfigDataMgr(String fileLocation)
	{
		fConfigFilePath = new File(fileLocation, ConfigFileName);
	}

	public static ConfigDataMgr createNew() throws Exception
	{
		fTheConfigDataMgr = new ConfigDataMgr(AppSettings.getAppDataPath());
		fTheConfigDataMgr.createDataFile();
		return fTheConfigDataMgr;
	}

	public static ConfigDataMgr initialize() throws Exception
	{
		fTheConfigDataMgr = new ConfigDataMgr(AppSettings.getAppDataPath());
		fTheConfigDataMgr.refresh();
		return fTheConfigDataMgr;
	}

	public static ConfigDataMgr getThe()
	{
		return fTheConfigDataMgr;
	}

	/* Implementation */
	private void createDataFile() throws Exception
	{
		if(!fConfigFilePath.getParentFile().exists())
			fConfigFilePath.getParentFile().mkdirs();

		if(fConfigFilePath.exists())
			return;

		String configFileXml = String.format(
			"<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<Config>"
				+ "<General>"
					+ "<ServiceURL>http://api.stormmediaplayer.com/webapi/playerapi/xml</ServiceURL>"
					+ "<LoopIntervalSecs>300</LoopIntervalSecs>"
				+ "</General>"
				+ "<Player>"
					+ "<ManufacturerID>inetvod</ManufacturerID>"
					+ "<ModelNo>windls</ModelNo>"
					+ "<SerialNo>%s</SerialNo>"
					+ "<Version>1.0.0000</Version>"
				+ "</Player>"
			+ "</Config>", UUID.randomUUID().toString());
		StreamUtil.stringToFile(configFileXml, fConfigFilePath);
	}

	private void refresh() throws Exception
	{
		LockableRandomAccessFile lockableFile = LockableRandomAccessFile.newInstance(fConfigFilePath);
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

	private void readDataFile(LockableRandomAccessFile lockableFile) throws Exception
	{
		XmlDataReader reader = new XmlDataReader(lockableFile.readFile());
		fConfig = reader.readObject(ConfigElement, Config.CtorDataReader);
	}

	/* Save, file is read-only*/
	private static void writeDataFile(LockableRandomAccessFile lockableFile, Config config) throws Exception
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		XmlDataWriter writer = new XmlDataWriter(output);
		writer.writeObject(ConfigElement, config);
		writer.flush();
		lockableFile.writeFile(new ByteArrayInputStream(output.toByteArray()));
	}
}
