package com.hortonworks;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

import com.hortonworks.streaming.impl.domain.transport.TruckConfiguration;
import com.hortonworks.streaming.impl.messages.StartSimulation;
import com.hortonworks.streaming.impl.messages.StopSimulation;
import com.hortonworks.streaming.listeners.SimulatorListener;
import com.hortonworks.streaming.masters.SimulationMaster;

public class App {
	public static void main(String[] args) {
			try {
				
				String routesDirectory = args[5];
				int emitters = TruckConfiguration.initialize(routesDirectory);
				
				final Class eventEmitterClass = Class.forName(args[2]);
				final Class eventCollectorClass = Class.forName(args[3]);
				final long demoId = Long.parseLong(args[4]);
				final int numberOfEventEmitters = emitters;
				final int numberOfEvents = Integer.parseInt(args[1]);	

				ActorSystem system = ActorSystem.create("EventSimulator");
				final ActorRef listener = system.actorOf(
						Props.create(SimulatorListener.class), "listener");
				final ActorRef eventCollector = system.actorOf(
						Props.create(eventCollectorClass), "eventCollector");
				System.out.println(eventCollector.path());
				final ActorRef master = system.actorOf(new Props(
						new UntypedActorFactory() {
							public UntypedActor create() {
								return new SimulationMaster(
										numberOfEventEmitters,
										eventEmitterClass, listener, numberOfEvents, demoId);
							}
						}), "master");
				Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
					public void run() {
						master.tell(new StopSimulation(), master);
					}
				}));
				master.tell(new StartSimulation(), master);
			} catch (NumberFormatException e) {
				System.err.println("Invalid number of emitters: "
						+ e.getMessage());
			} catch (ClassNotFoundException e) {
				System.err.println("Cannot find classname: " + e.getMessage());
			}
		
	}
}
