/* *********************************************************************** *
 * project: org.matsim.*
 * JavaDEQSim.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package playground.wrashid.deqsim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.matsim.basic.v01.Id;
import org.matsim.events.Events;
import org.matsim.gbl.Gbl;
import org.matsim.network.Link;
import org.matsim.network.NetworkLayer;
import org.matsim.population.Act;
import org.matsim.population.Leg;
import org.matsim.population.Person;
import org.matsim.population.Plan;
import org.matsim.population.Population;

import playground.wrashid.PDES2.Road;
import playground.wrashid.PDES2.Scheduler;
import playground.wrashid.PDES2.SimulationParameters;
import playground.wrashid.PDES2.Vehicle;
import playground.wrashid.DES.utils.Timer;

public class JavaPDEQSim2 {

	final Population population;
	final NetworkLayer network;
	
	public JavaPDEQSim2(final NetworkLayer network, final Population population, final Events events) {
		// constructor
		


		
		this.population = population;
		this.network = network;
		
		
		
		// initialize Simulation parameters
		SimulationParameters.linkCapacityPeriod=network.getCapacityPeriod();
		SimulationParameters.events=events;
		SimulationParameters.stuckTime= Double.parseDouble(Gbl.getConfig().getParam("simulation", "stuckTime"));
		SimulationParameters.flowCapacityFactor= Double.parseDouble(Gbl.getConfig().getParam("simulation", "flowCapacityFactor"));
		SimulationParameters.flowCapacityFactor= Double.parseDouble(Gbl.getConfig().getParam("simulation", "storageCapacityFactor"));
		
	}
	
	public void run() {
		//System.out.println("JavaDEQSim.run");
		Timer t=new Timer();
		t.startTimer();
		double bucketBoundries[]=new double[SimulationParameters.numberOfZoneBuckets-1];
		int bucketCount[]=new int[SimulationParameters.numberOfZoneBuckets];
		
		
		Scheduler scheduler=new Scheduler();
		
		
		
		// initialize network (roads)
		Road.allRoads=new HashMap<String,Road>();

		
		
		Road road=null;
		for (Link link: network.getLinks().values()){
			road= new Road(scheduler,link);
			double xCoordinate= road.getXCoordinate();
			
			if (xCoordinate<SimulationParameters.minXCoodrinate){
				SimulationParameters.minXCoodrinate=xCoordinate;
			} else if (xCoordinate>SimulationParameters.maxXCoodrinate){
				SimulationParameters.maxXCoodrinate=xCoordinate;
			}
			
			Road.allRoads.put(link.getId().toString(), road);
		}
		
		
		
		
		//
		
		
		
		double bucketDistance=(SimulationParameters.maxXCoodrinate-SimulationParameters.minXCoodrinate)/SimulationParameters.numberOfZoneBuckets;
		for (int i=0;i<SimulationParameters.numberOfZoneBuckets-1;i++){
			bucketBoundries[i]=(i+1)*bucketDistance;
		}
		
		
		// initialize vehicles
		Vehicle vehicle=null;
		for (Person person : this.population.getPersons().values()) {
			vehicle =new Vehicle(scheduler,person);
			
			
			// TODO: we could make this more precise (e.g. take all act links or also path links)
			Plan plan = person.getSelectedPlan(); 
			ArrayList<Object> actsLegs = plan.getActsLegs();
			// assumption, an action is followed by a let always
			// and a plan starts with a action
			Act act=null;
			Leg leg=null;
			for (int i=0;i<actsLegs.size();i++){
				
				if (actsLegs.get(i) instanceof Act){
					act=(Act) actsLegs.get(i);
					//System.out.print(".");
					bucketCount[getZone(act.getLink().getFromNode().getCoord().getX(),bucketBoundries)]++;
				} else {
					leg = (Leg) actsLegs.get(i);
					Link[] links=leg.getRoute().getLinkRoute();
					for (int j=0;j<links.length;j++){
						bucketCount[getZone(links[j].getFromNode().getCoord().getX(),bucketBoundries)]++;
					}
				}
				//System.out.println();
			}
			
			//System.out.println(act.getLink().getFromNode().getCoord().getX());
			
		}
		
		double sumOfBuckets=0;
		
		for (int i=0;i<SimulationParameters.numberOfZoneBuckets-1;i++){
			sumOfBuckets+=bucketCount[i];
			//System.out.println(bucketCount[i]);
		}
		// because later, many bucket may get more than 'maxEventsPerBucket', we can get into the problem
		// that there is not enough for the last few processors
		double maxEventsPerBucket=sumOfBuckets/(SimulationParameters.numberOfZones+Runtime.getRuntime().availableProcessors()/6);
		System.out.println("sumOfBuckets="+sumOfBuckets);
		
		
		// Equi Event zones
		//TODO: review this code..
		// there is a slight difference in size of buckets assigned, which could be improved
		
		double tmpBucketCount=0;
		int bucketCounter=0;
		for (int i=0;i<SimulationParameters.numberOfZones-1;i++){
			tmpBucketCount=0;
			while (tmpBucketCount<maxEventsPerBucket){
				tmpBucketCount+=bucketCount[bucketCounter];
				bucketCounter++;
			}
			System.out.println("tmpBucketCount="+tmpBucketCount);
			SimulationParameters.zoneBorderLines[i]=bucketBoundries[bucketCounter];
			System.out.println(i+"-th boundry:" + SimulationParameters.zoneBorderLines[i]);
		}
		
		
		
		
		
		 // Equi-Distant zones
		/*
		SimulationParameters.xZoneDistance=(SimulationParameters.maxXCoodrinate-SimulationParameters.minXCoodrinate)/SimulationParameters.numberOfZones;
		for (int i=0;i<SimulationParameters.numberOfZones-1;i++){
			SimulationParameters.zoneBorderLines[i]=(i+1)*SimulationParameters.xZoneDistance;
			System.out.println(i+"-th boundry:" + SimulationParameters.zoneBorderLines[i]);
		}
		*/
		
		
		// assign a zone to each road
		for (Road r:Road.allRoads.values()){
			r.initializeZoneId();
		}
		
		// 
		scheduler.inititZoneMessageQueues();
		
		
		
		
		
		
		
		

		
		scheduler.startSimulation();
		
		

		
		
		t.endTimer();
		t.printMeasuredTime("Time needed for one iteration (only PDES part): ");
		
		
		// print output
		//for(int i=0;i<SimulationParameters.eventOutputLog.size();i++) {
		//	SimulationParameters.eventOutputLog.get(i).print();
		//}
		
	}
	
	public int getZone(double xCoordinate, double bucketBoundries[]) {
		int zoneId=0;
		for (int i=0;i<SimulationParameters.numberOfZoneBuckets-1;i++){
			zoneId=i;
			if (xCoordinate<bucketBoundries[i]){
				//System.out.println(zoneId);
				return zoneId;
			}
		}
		zoneId=SimulationParameters.numberOfZoneBuckets-1;
		//System.out.println(zoneId);
		return zoneId;
	}
}
