/**
 * Copyright © 2008 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.dls.gateway;

import java.io.File;
import java.util.ArrayList;

import com.inetvod.common.data.RentedShowID;
import com.inetvod.dls.common.Logger;
import com.inetvod.dls.data.ConfigDataMgr;
import com.inetvod.dls.data.Show;
import com.inetvod.dls.data.UserDataMgr;

/**
 * This is an object running in the background on a separate thread. Due to security restrictions, DownloadServiceMgr
 * cannot
 */
class DownloadServiceGateway extends Thread
{
	private static final String QuickTimePlayer = "qt";
	private static final String WindowsMediaPlayer = "wm";
	private static final String InternetExplorer = "ie";

	/* Fields */
	private boolean fIsRunning;
	private boolean fStopRequested;

	private ConfigDataMgr fConfigDataMgr;
	private UserDataMgr fUserDataMgr;

	private boolean fDoRefresh;				// Refresh requested
	private boolean fSetUserCredantials;	// SetUserCredantials requested
	private String fNewUserLogonID;			// SetUserCredantials - new UserLogonID
	private String fNewUserPIN;				// SetUserCredantials - new UserPIN
	private boolean fNewRememberUserPIN;	// SetUserCredantials - new RememberUserPIN 
	private boolean fSetNextProcessNow;		// SetNextProcessNow requested
	private boolean fDoPlayRentedShow;		// PlayRentedShow requested
	private String fPlayRentedShowPath;		// PlayRentedShow requested
	private String fPlayRentedShowURL;		// PlayRentedShow requested
	private String fPlayUseApp;				// PlayRentedShow requested

	/* Getters and Setters */
	public boolean isRunning() { return fIsRunning; }
	public void setStopRequested() { fStopRequested = true; }

	/* Construction */
	private DownloadServiceGateway() throws Exception
	{
		fConfigDataMgr = ConfigDataMgr.initialize();
		fUserDataMgr = UserDataMgr.initialize(fConfigDataMgr.getConfig().getGeneral().getLoopIntervalSecs());
		Logger.initialize(fUserDataMgr.getEnableLog());
	}

	public static DownloadServiceGateway newInstance()
	{
		try
		{
			DownloadServiceGateway downloadServiceGateway = new DownloadServiceGateway();
			downloadServiceGateway.start();
			return downloadServiceGateway;
		}
		catch(Exception e)
		{
			Logger.logInfo(DownloadServiceGateway.class, "newInstance", e);
		}
		return null;
	}

	/* Implementation - Threading */

	@Override
	public void run()
	{
		fIsRunning = true;
		while(!fStopRequested)
		{
			Logger.logInfo(this, "run", "begin");
			checkRefresh();
			checkSetUserCredantials();
			checkSetNextProcessNow();
			checkPlayRendtedShow();
			Logger.logInfo(this, "run", "end");

			try
			{
				Thread.sleep(100);
			}
			catch(InterruptedException ignore)
			{
				fStopRequested = true;
				fIsRunning = false;
				Thread.currentThread().interrupt();
			}
		}
		fIsRunning = false;
	}

	private void checkRefresh()
	{
		if(fDoRefresh)
		{
			try
			{
				fUserDataMgr.refresh();
			}
			catch(Exception e)
			{
				Logger.logInfo(this, "checkRefresh", e);
			}
			fDoRefresh = false;
		}
	}

	private void checkSetUserCredantials()
	{
		if (fSetUserCredantials)
		{
			try
			{
				fUserDataMgr.setUserCredentials(fNewUserLogonID, fNewUserPIN, fNewRememberUserPIN);
			}
			catch(Exception e)
			{
				Logger.logInfo(this, "checkSetUserCredantials", e);
			}
			fSetUserCredantials = false;
		}
	}

	private void checkSetNextProcessNow()
	{
		if (fSetNextProcessNow)
		{
			try
			{
				fUserDataMgr.setNextProcessNow();
			}
			catch(Exception e)
			{
				Logger.logInfo(this, "checkSetNextProcessNow", e);
			}
			fSetNextProcessNow = false;
		}
	}

	private void checkPlayRendtedShow()
	{
		if(fDoPlayRentedShow)
		{
			Logger.logInfo(this, "checkPlayRendtedShow", String.format("rentedShowPath(%s), rentedShowURL(%s), useApp(%s)",
				fPlayRentedShowPath, fPlayRentedShowURL, fPlayUseApp));
			try
			{
				ArrayList<String> command = new ArrayList<String>();

				if(QuickTimePlayer.equals(fPlayUseApp))
				{
					command.add("C:\\\\Program Files\\\\QuickTime\\\\QuickTimePlayer.exe");
					command.add(fPlayRentedShowPath);
				}
				else if(WindowsMediaPlayer.equals(fPlayUseApp))
				{
					command.add("C:\\\\Program Files\\\\Windows Media Player\\\\wmplayer.exe");
					command.add(fPlayRentedShowPath);
				}
				else if(InternetExplorer.equals(fPlayUseApp))
				{
					command.add("C:\\\\Program Files\\\\Internet Explorer\\\\iexplore.exe");
					command.add(fPlayRentedShowURL);
				}

				if (command.size() > 0)
				{
					ProcessBuilder processBuilder = new ProcessBuilder(command);
					Process process = processBuilder.start();
					Logger.logInfo(this, "checkPlayRendtedShow", String.format("process %s",
						((process != null) ? "not null" : "is null")));
				}
				else
					Logger.logInfo(this, "checkPlayRendtedShow", "command.size() == 0");
			}
			catch(Exception e)
			{
				Logger.logInfo(this, "checkPlayRendtedShow", e);
			}
			fDoPlayRentedShow = false;
		}
	}

	/* Implementation - Interface */

	public void refresh()
	{
		fDoRefresh = true;
	}

	public String getPlayerSerialNo()
	{
		try
		{
			return fConfigDataMgr.getConfig().getPlayer().getSerialNo();
		}
		catch(Exception e)
		{
			Logger.logInfo(this, "getPlayerSerialNo", e);
		}
		return null;
	}

	public int getMaxSizeForShows()
	{
		return fUserDataMgr.getMaxSizeForShows();
	}

	public void setMaxSizeForShows(int maxSizeForShows)
	{
//TODO		fUserDataMgr.setMaxSizeForShows(maxSizeForShows);
	}

	public String getUserLogonID()
	{
		return fUserDataMgr.getUserLogonID();
	}

	public String getUserPIN()
	{
		return fUserDataMgr.getUserPIN();
	}

	public boolean getRememberUserPIN()
	{
		return fUserDataMgr.getRememberUserPIN();
	}

	public void setUserCredentials(String userLogonID, String userPIN, boolean rememberUserPIN)
	{
		Logger.logInfo(this, "setUserCredentials", String.format("called(%s,%s,%b)", userLogonID, userPIN,
			rememberUserPIN));
		fNewUserLogonID = userLogonID;
		fNewUserPIN = userPIN;
		fNewRememberUserPIN = rememberUserPIN;
		fSetUserCredantials = true;
	}

	public boolean getEnableLog()
	{
		return fUserDataMgr.getEnableLog();
	}

	public void setEnableLog(boolean enable)
	{
//TODO		fUserDataMgr.setEnableLog(enable);
	}

	public void processNow()
	{
		fSetNextProcessNow = true;
	}

	public String getRentedShowPath(String rentedShowID)
	{
		Show show = fUserDataMgr.getShowList().findByRentedShowID(new RentedShowID(rentedShowID));
		if(show == null)
			return null;
		return (new File(fUserDataMgr.getLocalShowPath(), show.getDataFileName())).getAbsolutePath();
	}

	public String getRentedShowStatus(String rentedShowID)
	{
		Logger.logInfo(this, "getRentedShowStatus", String.format("rentedShowID(%s)", rentedShowID));
		Show show = fUserDataMgr.getShowList().findByRentedShowID(new RentedShowID(rentedShowID));
		if(show == null)
			return null;
		return show.getDownloadStatus().toString();
	}

	public boolean playRentedShow(String rentedShowID, String useApp)
	{
		Logger.logInfo(this, "playRentedShow", String.format("rentedShowID(%s), useApp(%s)", rentedShowID, useApp));
		Show show = fUserDataMgr.getShowList().findByRentedShowID(new RentedShowID(rentedShowID));
		if(show == null)
			return false;
		fPlayRentedShowPath = (new File(fUserDataMgr.getLocalShowPath(), show.getDataFileName())).getAbsolutePath();
		fPlayRentedShowURL = show.getShowURL();
		fPlayUseApp = useApp;
		fDoPlayRentedShow = true;
		return true;
	}
}
