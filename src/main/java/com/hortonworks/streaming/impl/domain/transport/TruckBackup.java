//package com.hortonworks.streaming.impl.domain.transport;
//
//import java.sql.Timestamp;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.List;
//import java.util.Random;
//
//import akka.actor.ActorRef;
//
//import com.hortonworks.streaming.impl.domain.AbstractEventEmitter;
//import com.hortonworks.streaming.impl.domain.gps.BackToTheFutureException;
//import com.hortonworks.streaming.impl.domain.gps.Location;
//import com.hortonworks.streaming.impl.domain.gps.Path;
//import com.hortonworks.streaming.impl.domain.gps.TimestampedLocation;
//import com.hortonworks.streaming.impl.domain.transport.route.jaxb.Placemark;
//import com.hortonworks.streaming.impl.messages.EmitEvent;
//
//public class TruckBackup extends AbstractEventEmitter {
//	private static final long serialVersionUID = 9157180698115417087L;
//	private Driver driver;
//	private int truckId;
//	private int messageCount = 0;
//	private List<MobileEyeEventTypeEnum> eventTypes;
//	private Random rand = new Random();
//	private Path path = new Path();
//	private TimestampedLocation startingPoint = null;
//	private int numberOfEventsToGenerate;
//	private long demoId;
//	
//	boolean runWithFixedRoutes;
//
//	public TruckBackup(int numberOfEvents, long demoId) {
//		driver = TruckConfiguration.getNextDriver();
//		truckId = TruckConfiguration.getNextTruckId();
//		eventTypes = Arrays.asList(MobileEyeEventTypeEnum.values());
//		this.numberOfEventsToGenerate = numberOfEvents;
//		this.demoId = demoId;
//		if (driver.getStartingPoint() == null)
//			startingPoint = TruckConfiguration.getNextStartingPoint();
//		else
//			startingPoint = driver.getStartingPoint();
//		addWayPoint(startingPoint);
//	}
//
////	public Truck(List<Placemark> placemarks) {
////		runWithFixedRoutes = true;
////		this.placemarks = placemarks;
////		
////	}
//
//
//	public Driver getDriver() {
//		return driver;
//	}
//
//	public void setDriver(Driver driver) {
//		this.driver = driver;
//	}
//
//	public MobileEyeEvent generateEvent() {
//		Location nextLocation = getNextLocationNearExistingLocation(path
//				.getFinish().getLocation());
//		addWayPoint(new TimestampedLocation(new GregorianCalendar(),
//				nextLocation));
//		// System.out.println("Truck traveled: " +
//		// path.getOverGroundAverageSpeed() + "MPH");
//		if (messageCount % driver.getRiskFactor() == 0)
//			return new MobileEyeEvent(demoId, nextLocation, getRandomUnsafeEvent(),
//					this);
//		else
//			return new MobileEyeEvent(demoId, nextLocation,
//					MobileEyeEventTypeEnum.NORMAL, this);
//	}
//
//	private Location getNextLocationNearExistingLocation(Location location) {
//		
//		double randomLat = (Math.random() - 0.7D)/40;
//		double randomLong = (Math.random() - 0.7D) /40;
//		Location nextLocation = new Location(location.getLongitude() + randomLong,
//											 location.getLatitude() + randomLat,
//											 location.getAltitude());
////		Location nextLocation = new Location(location.getLongitude() +  0.02, 
////											 location.getLatitude() + 0.02, location.getAltitude()
////				+ Math.random());		
//		return nextLocation;
//	}
//
//	private MobileEyeEventTypeEnum getRandomUnsafeEvent() {
//		return eventTypes.get(rand.nextInt(eventTypes.size() - 1));
//	}
//
//	public Path getPath() {
//		return path;
//	}
//
//	public void setPath(Path path) {
//		this.path = path;
//	}
//
//	private void addWayPoint(TimestampedLocation waypoint) {
//		try {
//			path.addWaypoint(waypoint);
//		} catch (BackToTheFutureException e) {
//		}
//	}
//
//	@Override
//	public String toString() {
//		return new Timestamp(new Date().getTime()) + "|" + truckId + "|"
//				+ driver.getDriverId() + "|";
//	}
//
//	@Override
//	public void onReceive(Object message) throws Exception {
//		if (message instanceof EmitEvent) {
//			ActorRef actor = this.context().system()
//					.actorFor("akka://EventSimulator/user/eventCollector");
//			Random rand = new Random();
//			int sleepOffset = rand.nextInt(200);
//			if(numberOfEventsToGenerate == -1) {
//				while(true) {
//					messageCount++;
//					Thread.sleep(500 + sleepOffset);
//					actor.tell(generateEvent(), this.getSender());					
//				}
//				
//			} else {
//				while (messageCount < numberOfEventsToGenerate) {
//					messageCount++;
//					Thread.sleep(1000 + sleepOffset);
//					actor.tell(generateEvent(), this.getSender());
//				}				
//			}
//
//		}
//	}
//}
