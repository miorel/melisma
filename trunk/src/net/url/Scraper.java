/*
 * Copyright (C) 2009-2010 Miorel-Lucian Palii
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
package net.url;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Scraper {
	public StringBuilder scrape(URL url) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStream stream = url.openStream();
		byte[] buffer = new byte[1 << 12];
		for(int read; (read = stream.read(buffer)) > 0;)
			sb.append(new String(buffer, 0, read));
		return sb;
	}
}
