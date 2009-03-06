using System;
using System.Reflection;

using iNetVOD.Common.Core;

namespace iNetVOD.Common.Request
{
	public class PlayerRqst : Writeable
	{
		#region Constants
		private static readonly int VersionMaxLength = 16;
		private static readonly int SessionDataMaxLength = Int16.MaxValue;
		#endregion

		#region Fields
		private TString fVersion;
		private TString fSessionData;
		private RequestData fRequestData;
		#endregion

		#region Properties
		public string Version { set { fVersion = new TString(value); } }
		public string SessionData { set { fSessionData = new TString(value); } }
		public RequestData RequestData { set { fRequestData = value; } }
		#endregion

		#region Constuction
		private PlayerRqst()
		{
		}

		public static PlayerRqst NewInstance()
		{
			return new PlayerRqst();
		}
		#endregion

		#region Implementation
		public void WriteTo(DataWriter writer)
		{
			writer.WriteString("Version", fVersion, VersionMaxLength);
			writer.WriteString("SessionData", fSessionData, SessionDataMaxLength);

			writer.WriteObject("RequestData", fRequestData);
		}
		#endregion
	}

	public class PlayerResp : Readable
	{
		#region Constants
		public static readonly ConstructorInfo CtorDataReader = typeof(PlayerResp).GetConstructor(new Type[] { typeof (DataReader) });
		private static readonly int StatusMessageMaxLength = 1024;
		#endregion

		#region Fields
		private StatusCode fStatusCode;
		private TString fStatusMessage;
		private ResponseData fResponseData;
		#endregion

		#region Properties
		public StatusCode StatusCode { get { return fStatusCode; } }
		public TString StatusMessage { get { return fStatusMessage; } }
		public ResponseData ResponseData { get { return fResponseData; } }
		#endregion

		#region Constuction
		public PlayerResp(DataReader reader)
		{
			ReadFrom(reader);
		}
		#endregion

		#region Implementation
		public void ReadFrom(DataReader reader)
		{
			fStatusCode = (StatusCode)reader.ReadInt("StatusCode").Value;
			fStatusMessage = reader.ReadString("StatusMessage", StatusMessageMaxLength);
			fResponseData = (ResponseData)reader.ReadObject("ResponseData", ResponseData.CtorDataReader);
		}
		#endregion
	}
}
