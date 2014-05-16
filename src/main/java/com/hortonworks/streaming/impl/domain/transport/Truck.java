package com.hortonworks.streaming.impl.domain.transport;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import akka.actor.ActorRef;

import com.hortonworks.streaming.impl.domain.AbstractEventEmitter;
import com.hortonworks.streaming.impl.domain.gps.BackToTheFutureException;
import com.hortonworks.streaming.impl.domain.gps.Location;
import com.hortonworks.streaming.impl.domain.gps.Path;
import com.hortonworks.streaming.impl.domain.gps.TimestampedLocation;
import com.hortonworks.streaming.impl.domain.transport.route.Route;
import com.hortonworks.streaming.impl.domain.transport.route.jaxb.Placemark;
import com.hortonworks.streaming.impl.messages.EmitEvent;

public class Truck extends AbstractEventEmitter {
	
	private static final long serialVersionUID = 9157180698115417087L;
	private Driver driver;
	
	private int truckId;
	private int messageCount = 0;
	
	private List<MobileEyeEventTypeEnum> eventTypes;
	
	private Random rand = new Random();

	
	private int numberOfEventsToGenerate;
	private long demoId;
	

	public Truck(int numberOfEvents, long demoId) {
		driver = TruckConfiguration.getNextDriver();
		truckId = TruckConfiguration.getNextTruckId();
		eventTypes = Arrays.asList(MobileEyeEventTypeEnum.values());
		
		this.numberOfEventsToGenerate = numberOfEvents;
		this.demoId = demoId;
	}



	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public MobileEyeEvent generateEvent() {

		Location nextLocation = getDriver().getRoute().getNextLocation();
		if (messageCount % driver.getRiskFactor() == 0)
			return new MobileEyeEvent(demoId, nextLocation, getRandomUnsafeEvent(),
					this);
		else
			return new MobileEyeEvent(demoId, nextLocation,
					MobileEyeEventTypeEnum.NORMAL, this);
	}

	private MobileEyeEventTypeEnum getRandomUnsafeEvent() {
		return eventTypes.get(rand.nextInt(eventTypes.size() - 1));
	}


	@Override
	public String toString() {
		return new Timestamp(new Date().getTime()) + "|" + truckId + "|"
				+ driver.getDriverId() + "|";
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof EmitEvent) {
			ActorRef actor = this.context().system()
					.actorFor("akka://EventSimulator/user/eventCollector");
			Random rand = new Random();
			int sleepOffset = rand.nextInt(200);
			if(numberOfEventsToGenerate == -1) {
				while(true) {
					messageCount++;
					Thread.sleep(500 + sleepOffset);
					actor.tell(generateEvent(), this.getSender());					
				}
				
			} else {
				while (messageCount < numberOfEventsToGenerate) {
					messageCount++;
					Thread.sleep(1000 + sleepOffset);
					actor.tell(generateEvent(), this.getSender());
				}				
			}

		}
	}
}
