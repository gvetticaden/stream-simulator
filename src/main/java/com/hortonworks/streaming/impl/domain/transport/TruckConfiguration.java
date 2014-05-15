package com.hortonworks.streaming.impl.domain.transport;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hortonworks.streaming.impl.domain.gps.Location;
import com.hortonworks.streaming.impl.domain.gps.TimestampedLocation;

public class TruckConfiguration {
	private static int lastTruckId = 9;
	private static int nextDriverId = 10;
	private static Map<Integer, Driver> drivers;
	private static Logger logger = Logger.getLogger(TruckConfiguration.class);
	private static List<TimestampedLocation> startingPoints = null;
	private static Iterator<TimestampedLocation> startingPointIterator = null;

	static {
		reset();
	}
	
	public static void reset() {
		lastTruckId = 9;
		nextDriverId = 10;
		drivers = new HashMap<Integer, Driver>();

		//Columbia, MO, 38.933379, -92.310685
		Driver riskyDriver = new Driver(10, 30, new TimestampedLocation(new GregorianCalendar(),
				new Location(-92.310685, 38.933379, 150.0f)));
		
		//Jeff City: 38.523884, -92.159845
		Driver lessRiskyDriver = new Driver(11, 100, new TimestampedLocation(new GregorianCalendar(),
				new Location(-92.159845, 38.523884, 150.0f)));
	
		//Saint Louis: 38.615509 -90.395569
		Driver mostRiskyDriver = new Driver(12, 10, new TimestampedLocation(new GregorianCalendar(),
				new Location(-90.395569, 38.615509, 150.0f)));		
		
		drivers.put(10, riskyDriver);
		drivers.put(11, lessRiskyDriver);
		drivers.put(12, mostRiskyDriver);
		
		
		startingPoints = new LinkedList<TimestampedLocation>();

		//Kansas City, 39.133616 -94.747812
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(-94.747812, 39.133616, 150.0f)));
		
		//SpringField IL, 39.802787, -89.651201
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(-89.651201, 39.802787, 220.0f)));
		
		//Evansville, IL 37.998016, -87.5836
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(-87.5836, 37.998016, 20.0f)));
		
		//SpringField, MO, 37.238909, -93.33455
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(-93.33455, 37.238909, 20.0f)));
		
		//Joplin, MO 37.060655, -94.495437
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(-94.495437, 37.060655, 20.0f)));
		
		//Other, MO, 39.57182223734374, -91.25244140624999
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(-91.25244140624999, 39.57182223734374, 20.0f)));		
		
		//Other, MO 37.52715361723378, -90.28564453124999
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(-90.28564453124999, 37.52715361723378, 20.0f)));
		
		startingPointIterator = startingPoints.iterator();
	}

	public synchronized static int getNextTruckId() {
		lastTruckId++;
		return lastTruckId;
	}

	public synchronized static Driver getNextDriver() {
		Driver nextDriver = drivers.get(nextDriverId);
		if(nextDriver == null) {
			drivers.put(nextDriverId, new Driver(nextDriverId, 100));
		}
		nextDriver = drivers.get(nextDriverId);
		logger.debug("Next Driver: " + nextDriver.toString());
		nextDriverId++;
		return nextDriver;
	}

	public synchronized static TimestampedLocation getNextStartingPoint() {
		if (startingPointIterator.hasNext())
			return startingPointIterator.next();
		else
			startingPointIterator = startingPoints.iterator();
		return getNextStartingPoint();
	}
}
