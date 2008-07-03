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
		#region Fields
		private static string fAppDataPath = Path.Combine(Environment.GetFolderPath(
			Environment.SpecialFolder.CommonApplicationData), "Storm Media Player");
		#endregion

		#region Properties
		public static string AppDataPath { get { return fAppDataPath; } }
		#endregion
	}
}
