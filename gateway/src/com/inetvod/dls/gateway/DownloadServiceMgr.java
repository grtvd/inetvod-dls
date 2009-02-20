/**
 * Copyright © 2008 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.dls.gateway;

import java.applet.Applet;

import com.inetvod.dls.common.Logger;

/**
 * This is the object that is accessed from JavaScript.  Due to security restrictions, this object needs to make
 * use of DownloadServiceGateway to do the actual work.
 */
public class DownloadServiceMgr extends Applet
{
	/* Constants */
	private static final int TriesToStopGateway = 10;
	private static final int WaitToStopGatewayMillis = 100;

	/* Fields */
	private DownloadServiceGateway fDownloadServiceGateway;

	/* Implementation */
	@Override
	public void init()
	{
		super.init();

		fDownloadServiceGateway = DownloadServiceGateway.newInstance();
	}

	private void stopGateway()
	{
		if (fDownloadServiceGateway != null)
		{
			fDownloadServiceGateway.setStopRequested();
			for(int i = 0; i < TriesToStopGateway; i++)
			{
				if(!fDownloadServiceGateway.isRunning())
					break;

				try
				{
					Logger.logInfo(this, "stopGateway", "waiting to stop");
					Thread.sleep(WaitToStopGatewayMillis);
				}
				catch(InterruptedException ignore)
				{
					break;
				}
			}
		}
		fDownloadServiceGateway = null;
	}

	@Override
	public void stop()
	{
		stopGateway();
		super.stop();
	}

	@Override
	public void destroy()
	{
		stopGateway();
		super.destroy();
	}

//	@Override
//	public void paint(Graphics g)
//	{
//	}

	/**
	 * Seems to be needed or crashes in IE
	 */
	@SuppressWarnings({ "deprecation" })
	@Override
	@Deprecated
	public void hide()
	{
		stopGateway();
		super.hide();
	}

	public void refresh()
	{
		if (fDownloadServiceGateway != null)
			fDownloadServiceGateway.refresh();
	}

	public String getPlayerSerialNo()
	{
		if (fDownloadServiceGateway != null)
			return fDownloadServiceGateway.getPlayerSerialNo();
		return null;
	}

	public int getMaxSizeForShows()
	{
		if (fDownloadServiceGateway != null)
			return fDownloadServiceGateway.getMaxSizeForShows();
		return -1;
	}

	public void setMaxSizeForShows(int maxSizeForShows)
	{
		if (fDownloadServiceGateway != null)
			fDownloadServiceGateway.setMaxSizeForShows(maxSizeForShows);
	}

	public String getUserLogonID()
	{
		if (fDownloadServiceGateway != null)
			return fDownloadServiceGateway.getUserLogonID();
		return null;
	}

	public String getUserPIN()
	{
		if (fDownloadServiceGateway != null)
			return fDownloadServiceGateway.getUserPIN();
		return null;
	}

	public boolean getRememberUserPIN()
	{
		if (fDownloadServiceGateway != null)
			return fDownloadServiceGateway.getRememberUserPIN();
		return false;
	}

	public void setUserCredentials(String userLogonID, String userPIN, boolean rememberUserPIN)
	{
		if (fDownloadServiceGateway != null)
			fDownloadServiceGateway.setUserCredentials(userLogonID, userPIN, rememberUserPIN);
	}

	public boolean getEnableLog()
	{
		if (fDownloadServiceGateway != null)
			return fDownloadServiceGateway.getEnableLog();
		return false;
	}

	public void setEnableLog(boolean enable)
	{
		if (fDownloadServiceGateway != null)
			fDownloadServiceGateway.setEnableLog(enable);
	}

	public void processNow()
	{
		Logger.logInfo(this, "processNow", "called");
		if (fDownloadServiceGateway != null)
			fDownloadServiceGateway.processNow();
	}

	public String getRentedShowPath(String rentedShowID)
	{
		if (fDownloadServiceGateway != null)
			return fDownloadServiceGateway.getRentedShowPath(rentedShowID);
		return null;
	}

	public String getRentedShowStatus(String rentedShowID)
	{
		if (fDownloadServiceGateway != null)
			return fDownloadServiceGateway.getRentedShowStatus(rentedShowID);
		return null;
	}

	public boolean playRentedShow(String rentedShowID, String useApp)
	{
		if (fDownloadServiceGateway != null)
			return fDownloadServiceGateway.playRentedShow(rentedShowID, useApp);
		return false;
	}
}
