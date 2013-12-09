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
		Driver riskyDriver = new Driver(10, 30, new TimestampedLocation(new GregorianCalendar(),
				new Location(-93.2828, 44.8833, 150.0f)));
		Driver lessRiskyDriver = new Driver(11, 60);
		Driver mostRiskyDriver = new Driver(12, 10, new TimestampedLocation(new GregorianCalendar(),
				new Location(-93.2828, 44.8833, 150.0f)));		
		drivers.put(10, riskyDriver);
		drivers.put(11, lessRiskyDriver);
		drivers.put(12, mostRiskyDriver);
		startingPoints = new LinkedList<TimestampedLocation>();
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(-93.266670, 44.983334, 150.0f)));
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(-82.907123, 40.417287, 220.0f)));
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(-96.800451, 32.780140, 20.0f)));
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(-112.074037, 33.448377, 20.0f)));
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
