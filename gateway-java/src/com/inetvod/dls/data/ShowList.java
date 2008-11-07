/**
 * Copyright © 2008 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.dls.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.data.RentedShowID;

public class ShowList extends ArrayList<Show>
{
	/* Constants */
	public static final Constructor<ShowList> Ctor = CtorUtil.getCtorDefault(ShowList.class);

	/* Implementation */

	public Show findByRentedShowID(RentedShowID rentedShowID)
	{
		for(Show show : this)
			if(show.getRentedShowID().equals(rentedShowID))
				return show;
		return null;
	}

	public Show findByDataFileName(String dataFileName)
	{
		for(Show show : this)
			if(show.getDataFileName().equals(dataFileName))
				return show;
		return null;
	}

	public boolean containsByRentedShowID(RentedShowID rentedShowID)
	{
		return findByRentedShowID(rentedShowID) != null;
	}

	public boolean containsByDataFileName(String dataFileName)
	{
		return findByDataFileName(dataFileName) != null;
	}
}
