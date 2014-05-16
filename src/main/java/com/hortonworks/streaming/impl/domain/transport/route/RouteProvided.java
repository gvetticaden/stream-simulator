package com.hortonworks.streaming.impl.domain.transport.route;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hortonworks.streaming.impl.domain.gps.Location;

public class RouteProvided implements Route {

	private static final Logger LOG = Logger.getLogger(RouteProvided.class);
	
	private List<Location> locations;
	private int locationIndex=0;
	private String routeName;
	private boolean forward=true;


	
	public RouteProvided (String routeName, List<Location> locations) {
		this.locations = locations;
		this.routeName = routeName;
	}

	public Location getStartingPoint() {
		return locations.get(0);
	}

	public Location getNextLocation() {
		Location location = null;
		if(locationIndex == locations.size()) {
			//go background if if we got the end
			LOG.info("Revering Direction..");
			locationIndex--;
			forward = false;
		} else if(locationIndex == -1) {
			//go forward
			LOG.info("Going Original Direction...");
			locationIndex++;
			forward=true;
		}
		location = locations.get(locationIndex);
		nextLocationIndex(); 
		return location;
	}
	
	public void nextLocationIndex() {
		if(forward) {
			locationIndex++;
		} else {
			locationIndex--;
		}
	}

	public List<Location> getLocations() {
		return this.locations;
	}

	

}
