package com.hortonworks.streaming.impl.domain.transport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hortonworks.streaming.impl.domain.gps.Location;
import com.hortonworks.streaming.impl.domain.transport.route.Route;
import com.hortonworks.streaming.impl.domain.transport.route.Route;
import com.hortonworks.streaming.impl.domain.transport.route.RouteGenerator;
import com.hortonworks.streaming.impl.domain.transport.route.TruckRoutesParser;

public class TruckConfiguration {
	
	private static Logger logger = Logger.getLogger(TruckConfiguration.class);
		
	private static int lastTruckId = 9;
	private static int nextDriverId = 10;
	
	private static Map<Integer, Driver> drivers;
	private static List<Location> startingPoints = null;
	private static Iterator<Location> startingPointIterator = null;
	public static List<Route> truckRoutes;

	private static int routeIndex;
	


	public static void initialize() {
		lastTruckId = 9;
		nextDriverId = 10;
		routeIndex = 0;
		drivers = new HashMap<Integer, Driver>();
		truckRoutes = null;
	}
	
	public static void initialize(String routeDirectoryLocation) {
		lastTruckId = 9;
		nextDriverId = 10;
		routeIndex = 0;
		drivers = new HashMap<Integer, Driver>();
		truckRoutes = null;
		
		parseRoutes(routeDirectoryLocation);

	}	
	

	public static boolean hasRoutes() {
		return truckRoutes != null && !truckRoutes.isEmpty();
	}

	public static void parseRoutes(String routeDirectoryLocation) {
		truckRoutes = new TruckRoutesParser().parseAllRoutes(routeDirectoryLocation);
	}
	
	public static void configureInitialDrivers() {
		Route route1;
		Route route2;
		Route route3;
		if (hasRoutes()) {
			route1 = getNextRoute();
			route2 = getNextRoute();
		} else {
			route1 = new RouteGenerator("route1", new Location(-92.310685, 38.933379, 150.0f));
			route2 = new RouteGenerator("route2", new Location(-90.395569, 38.615509, 150.0f));
		}
		Driver riskyDriver = new Driver(10, 30, route1);
		Driver mostRiskyDriver = new Driver(11, 10, route2);
		drivers.put(10, riskyDriver);
		drivers.put(11, mostRiskyDriver);
	}	
	
	public static void configureStartingPoints() {
		startingPoints = new ArrayList<Location>();

		// Kansas City, 39.133616 -94.747812
		startingPoints.add(new Location(-94.747812, 39.133616, 150.0f));

		// SpringField IL, 39.802787, -89.651201
		startingPoints.add(new Location(-89.651201, 39.802787, 220.0f));

		// Evansville, IL 37.998016, -87.5836
		startingPoints.add(new Location(-87.5836, 37.998016, 20.0f));

		// SpringField, MO, 37.238909, -93.33455
		startingPoints.add(new Location(-93.33455, 37.238909, 20.0f));

		// Joplin, MO 37.060655, -94.495437
		startingPoints.add(new Location(-94.495437, 37.060655, 20.0f));

		// Other, MO, 39.57182223734374, -91.25244140624999
		startingPoints.add(new Location(-91.25244140624999, 39.57182223734374,
				20.0f));

		// Other, MO 37.52715361723378, -90.28564453124999
		startingPoints.add(new Location(-90.28564453124999, 37.52715361723378,
				20.0f));

		startingPointIterator = startingPoints.iterator();
	}



	private static Route getNextRoute() {
		Route route = truckRoutes.get(routeIndex);
		routeIndex++;
		return route;
	}


	public synchronized static int getNextTruckId() {
		lastTruckId++;
		return lastTruckId;
	}

	public synchronized static Driver getNextDriver() {
		Driver nextDriver = drivers.get(nextDriverId);
		if (nextDriver == null) {
			if(hasRoutes()) {
				Route route = getNextRoute();
				nextDriver = new Driver(nextDriverId, 100, route);
			} else {
				Route route = new RouteGenerator("routex", getNextStartingPoint());
				nextDriver = new Driver(nextDriverId, 100, route);
			}
			
			drivers.put(nextDriverId, nextDriver);
		}
		logger.debug("Next Driver: " + nextDriver.toString());
		nextDriverId++;
		return nextDriver;
	}

	public synchronized static Location getNextStartingPoint() {
		if (startingPointIterator.hasNext())
			return startingPointIterator.next();
		else
			startingPointIterator = startingPoints.iterator();
		return getNextStartingPoint();
	}
}