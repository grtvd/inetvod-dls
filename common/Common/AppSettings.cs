#region Copyright
// Copyright © 2006-2008 iNetVOD, Inc. All Rights Reserved.
// iNetVOD Confidential and Proprietary.  See LEGAL.txt.
#endregion
using System;
using System.IO;

namespace iNetVOD.Common
{
	public class AppSettings
	{
		#region Constants
		private static readonly string AppDirName = "Storm Media Player";
		#endregion

		#region Fields
		private static string fAppDataPath = DetermineLocalShowPath();
		#endregion

		#region Properties
		public static string AppDataPath { get { return fAppDataPath; } }
		#endregion

		#region Implementation
		private static string DetermineLocalShowPath()
		{
			string localShowPath = "C:\\Users\\Public\\Videos";	//first try for Vista
			if(!(new DirectoryInfo(localShowPath)).Exists)
				localShowPath = "C:\\Documents and Settings\\All Users\\Documents\\My Videos";	//then try for XP
			return Path.Combine(localShowPath, AppDirName);
		}
		#endregion
	}
}
