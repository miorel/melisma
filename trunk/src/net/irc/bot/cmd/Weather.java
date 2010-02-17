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
package net.irc.bot.cmd;

import static com.googlecode.lawu.util.Iterators.iterator;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.bot.CommandShell;
import net.irc.cmd.PrivmsgCommand;

import org.w3c.dom.Node;

import util.Nodes;

import com.googlecode.lawu.util.Strings;

public class Weather extends AsynchronousCommand {	
	public Weather(CommandShell shell) {
		super(shell, "weather");
	}

	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%sweather location\2. Gives a weather report of the location, which may be a city, state, country, zip code (US or Canada), airport code, or comma-separated latitude/longitude pair. Defaults to the bot's location if none is specified. Uses Weather Underground (http://www.wunderground.com/) as data source.", trigger);
	}
	
	@Override
	protected void doExecute(IrcClient client, Entity origin, String channel, String args) {
		String target = channel == null ? origin.getNick() : channel;
		args = args.trim();
		if(args.isEmpty())
			args = "Gainesville, FL";
		try {
			String url = String.format("http://api.wunderground.com/auto/wui/geo/WXCurrentObXML/index.xml?query=%s", URLEncoder.encode(args, "UTF-8"));
			Node node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url);
			node = Nodes.getChildNode(node, "current_observation");
			String observationEpoch = Nodes.getText(node, "observation_epoch");
			if(observationEpoch == null)
				client.send(new PrivmsgCommand(target, "I couldn't get data for that place :("));	
			else {
				String location = Nodes.getText(node, "display_location", "full");
				if(location == null)
					throw new IllegalStateException("expected location when observation epoch is not null");
				List<String> reportList = new ArrayList<String>();
				String weather = Nodes.getText(node, "weather");
				if(weather != null)
					reportList.add(Strings.toTitleCase(weather.trim(), 1));
				String temperature = Nodes.getText(node, "temperature_string");
				if(temperature != null)
					reportList.add("Temperature: " + temperature.trim());
				String humidity = Nodes.getText(node, "relative_humidity");
				if(humidity != null)
					reportList.add("Relative humidity: " + humidity.trim());
				String wind = Nodes.getText(node, "wind_string");
				if(wind != null)
					reportList.add("Wind: " + wind.trim().replaceAll("^From", "from").replaceAll("MPH", "mph"));
				String pressure = Nodes.getText(node, "pressure_string");
				if(pressure != null)
					reportList.add("Pressure: " + pressure.trim());
				String visibility = Nodes.getText(node, "visibility_mi");
				if(pressure != null)
					reportList.add("Visibility: " + visibility.trim() + " miles");
				if(reportList.isEmpty())
					throw new IllegalStateException("expected weather information when observation epoch is not null");
				StringBuilder sb = new StringBuilder();
				sb.append("Weather Underground report for ").append(location.trim()).append(" - ").append(Strings.join(", ", iterator(reportList)));
				client.send(new PrivmsgCommand(target, sb.toString()));	
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			client.send(new PrivmsgCommand(target, "There was a problem interpreting the data. Try again later?"));
		}
	}
}
