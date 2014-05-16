package com.hortonworks.streaming.impl.domain.transport;

import com.hortonworks.streaming.impl.domain.gps.TimestampedLocation;
import com.hortonworks.streaming.impl.domain.transport.route.Route;
import com.hortonworks.streaming.interfaces.DomainObject;

public class Driver implements DomainObject {
	private static final long serialVersionUID = 6113264533619087412L;
	
	private int driverId;
	private int riskFactor;
	private Route route;

	public Driver(int driverId, int riskFactor,
				  Route route) {
		this.driverId = driverId;
		this.riskFactor = riskFactor;
		this.route = route;
	}

	public int getDriverId() {
		return driverId;
	}

	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}

	public int getRiskFactor() {
		return riskFactor;
	}

	public void setRiskFactor(int riskFactor) {
		this.riskFactor = riskFactor;
	}

	@Override
	public String toString() {
		return this.driverId + "|" + this.riskFactor;
	}

	public Route getRoute() {
		return route;
	}

}
