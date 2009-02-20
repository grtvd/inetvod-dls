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
import com.inetvod.common.data.RentedShowID;

public class Show implements Readable, Writeable
{
	/* Constants */
	public static final int ShowURLMaxLength = 4096;
	public static final int DataFileNameMaxLength = 200;

	public static final Constructor<Show> CtorDataReader = DataReader.getCtor(Show.class);

	/* Fields */
	private RentedShowID fRentedShowID;
	private String fShowURL;
	private String fDataFileName;
	private DownloadStatus fDownloadStatus;

	/* Getters and Setters */
	public RentedShowID getRentedShowID() { return fRentedShowID; }
	public String getShowURL() { return fShowURL; }
	public void setShowURL(String showURL) { fShowURL = showURL; }
	public String getDataFileName() { return fDataFileName; }
	public void setDataFileName(String dataFileName) { fDataFileName = dataFileName; }
	public DownloadStatus getDownloadStatus() { return fDownloadStatus; }
	public void setDownloadStatus(DownloadStatus downloadStatus) { fDownloadStatus = downloadStatus; }

	/* Construction */
	private Show(RentedShowID rentedShowID)
	{
		fRentedShowID = rentedShowID;
	}

	public Show(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	public static Show newInstance(RentedShowID rentedShowID)
	{
		return new Show(rentedShowID);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fRentedShowID = reader.readDataID("RentedShowID", RentedShowID.MaxLength, RentedShowID.CtorString);
		fShowURL = reader.readString("ShowURL", ShowURLMaxLength);
		fDataFileName = reader.readString("DataFileName", DataFileNameMaxLength);
		fDownloadStatus = DownloadStatus.convertFromString(reader.readString("DownloadStatus", DownloadStatus.MaxLength));
	}

	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("RentedShowID", fRentedShowID, RentedShowID.MaxLength);
		writer.writeString("ShowURL", fShowURL, ShowURLMaxLength);
		writer.writeString("DataFileName", fDataFileName, DataFileNameMaxLength);
		writer.writeString("DownloadStatus", DownloadStatus.convertToString(fDownloadStatus), DownloadStatus.MaxLength);
	}
}
