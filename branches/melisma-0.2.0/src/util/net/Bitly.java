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

package util.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import util.Nodes;

public class Bitly {
	private final String login;
	private final String apiKey;
	
	private final DocumentBuilder docBuilder;
	
	public Bitly(String login, String apiKey) throws ParserConfigurationException {
		if(login == null || login.isEmpty())
			throw new IllegalArgumentException("the login may not be null or zero length");
		if(apiKey == null || apiKey.isEmpty())
			throw new IllegalArgumentException("the API key may not be null or zero length");
		this.login = login;
		this.apiKey = apiKey;
		this.docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}
	
	protected String getLogin() {
		return login;
	}

	protected String getApiKey() {
		return apiKey;
	}
	
	public String shorten(String longUrl) throws UnsupportedEncodingException, SAXException, IOException {
		if(longUrl.indexOf("://") < 0)
			longUrl = "http://" + longUrl;
		Node node = docBuilder.parse(String.format("http://api.bit.ly/shorten?version=2.0.1&longUrl=%s&format=xml&login=%s&apiKey=%s", URLEncoder.encode(longUrl, "UTF-8"), URLEncoder.encode(getLogin(), "UTF-8"), URLEncoder.encode(getApiKey(), "UTF-8")));
		String ret = Nodes.getText(node, "bitly", "results", "nodeKeyVal", "shortUrl");
		if(ret == null)
			throw new NoSuchElementException("couldn't get a short URL");
		return ret;
	}
}
