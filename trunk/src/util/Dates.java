/*
 * Copyright (C) 2010 Miorel-Lucian Palii
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */
package util;

import java.text.DateFormat;
import java.util.Date;

public class Dates {
	private static final DateFormat DATE = DateFormat.getDateInstance(DateFormat.LONG);
	private static final DateFormat DATE_TIME = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

	private Dates() {
	}

	public static String date() {
		return date(new Date());
	}
	
	public static String date(Date date) {
		return DATE.format(date);
	}

	public static String time() {
		return time(new Date());
	}
	
	public static String time(Date date) {
		return DATE_TIME.format(date);
	}
}
