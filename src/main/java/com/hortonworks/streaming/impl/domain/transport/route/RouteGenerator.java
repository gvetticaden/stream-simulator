package com.hortonworks.streaming.impl.domain.transport.route;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hortonworks.streaming.impl.domain.gps.Location;

public class RouteGenerator implements Route {

	private static final Logger LOG = Logger.getLogger(RouteGenerator.class);
	
	private List<Location> locations;
	private int locationIndex=0;
	private String routeName;
	private boolean routeEnded = false;

	
	public RouteGenerator(String routeName, Location startingPoint) {
		locations = new ArrayList<Location>();
		locations.add(startingPoint);
		this.routeName = routeName;
	}

	public Location getStartingPoint() {
		return locations.get(0);
	}

	public Location getNextLocation() {
		Location location = null;
		if(locationIndex == locations.size()) {
			location = getNextNewLocation();
			locations.add(location);
		} else {
			location = locations.get(locationIndex);
		}
		locationIndex++;
		return location;
	}

	private Location getNextNewLocation() {
		LOG.debug("Generating new Location for Route...");
		// Get Previous location
		Location previousLocation = locations.get(locationIndex - 1);
		double randomLat = (Math.random() - 0.7D)/10;
		double randomLong = (Math.random() - 0.7D) /10;
		Location nextLocation = new Location(previousLocation.getLongitude() + randomLong,
											previousLocation.getLatitude() + randomLat,
											previousLocation.getAltitude());
		locations.add(nextLocation);
		return nextLocation;	
	}
	
	public List<Location> getLocations() {
		return this.locations;
	}

	@Override
	public boolean routeEnded() {
		return routeEnded;
	}

	@Override
	public String getRouteName() {
		return routeName;
	}
}
