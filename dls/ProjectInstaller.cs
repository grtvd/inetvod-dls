#region Copyright
// Copyright � 2006-2008 iNetVOD, Inc. All Rights Reserved.
// iNetVOD Confidential and Proprietary.  See LEGAL.txt.
#endregion
using System;
using System.Collections;
using System.ComponentModel;
using System.Configuration.Install;

namespace iNetVOD.DSL
{
	/// <summary>
	/// Summary description for ProjectInstaller.
	/// </summary>
	[RunInstaller(true)]
	public class ProjectInstaller : System.Configuration.Install.Installer
	{
		private System.ServiceProcess.ServiceProcessInstaller serviceProcessInstaller1;
		private System.ServiceProcess.ServiceInstaller serviceInstaller1;
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.Container components = null;

		public ProjectInstaller()
		{
			// This call is required by the Designer.
			InitializeComponent();
		}

		/// <summary> 
		/// Clean up any resources being used.
		/// </summary>
		protected override void Dispose( bool disposing )
		{
			if( disposing )
			{
				if(components != null)
				{
					components.Dispose();
				}
			}
			base.Dispose( disposing );
		}


		#region Component Designer generated code
		/// <summary>
		/// Required method for Designer support - do not modify
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
			this.serviceProcessInstaller1 = new System.ServiceProcess.ServiceProcessInstaller();
			this.serviceInstaller1 = new System.ServiceProcess.ServiceInstaller();

			this.AfterInstall += new InstallEventHandler(this.StartServiceAfterInstall);  
			this.BeforeUninstall += new InstallEventHandler(this.StopServiceBeforeUninstall); 
			this.BeforeRollback += new InstallEventHandler(this.StopServiceBeforeUninstall); 

			// 
			// serviceProcessInstaller1
			// 
			this.serviceProcessInstaller1.Account = System.ServiceProcess.ServiceAccount.LocalSystem;
			this.serviceProcessInstaller1.Password = null;
			this.serviceProcessInstaller1.Username = null;
			// 
			// serviceInstaller1
			// 
			this.serviceInstaller1.ServiceName = AppService.Name; 
			this.serviceInstaller1.DisplayName = AppService.Name;
			this.serviceInstaller1.StartType = System.ServiceProcess.ServiceStartMode.Automatic;

			// 
			// ProjectInstaller
			// 
			this.Installers.AddRange(new System.Configuration.Install.Installer[] {
																					  this.serviceProcessInstaller1,
																					  this.serviceInstaller1});
		}

		private void StartServiceAfterInstall(object obj, InstallEventArgs e  )
		{
			System.ServiceProcess.ServiceController sc = new System.ServiceProcess.ServiceController(AppService.Name);
			sc.Start(); 
		}

		private void StopServiceBeforeUninstall(object obj, InstallEventArgs e  )
		{
			System.ServiceProcess.ServiceController sc = new System.ServiceProcess.ServiceController(AppService.Name);
			sc.Stop(); 
		}

		#endregion

	}
}
