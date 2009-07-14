/* *********************************************************************** *
 * project: org.matsim.*
 * QueryAgentActivityStatus.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
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

package org.matsim.vis.otfvis.opengl.queries;

import java.util.Collection;

import org.matsim.api.basic.v01.Id;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.events.Events;
import org.matsim.core.mobsim.queuesim.QueueLink;
import org.matsim.core.mobsim.queuesim.QueueNetwork;
import org.matsim.core.mobsim.queuesim.QueueVehicle;
import org.matsim.core.population.ActivityImpl;
import org.matsim.core.population.PersonImpl;
import org.matsim.core.population.PlanImpl;
import org.matsim.core.population.PopulationImpl;
import org.matsim.vis.otfvis.data.OTFServerQuad;
import org.matsim.vis.otfvis.interfaces.OTFDrawer;
import org.matsim.vis.otfvis.interfaces.OTFQuery;

public class QueryAgentActivityStatus implements OTFQuery {

	private static final long serialVersionUID = -8532403277319196797L;

	private Id agentId = null;

	private double now;
	//out
	int activityNr = -1;
	double finished = 0;

	public OTFQuery query(QueueNetwork net, PopulationImpl plans, Events events, OTFServerQuad quad) {
		PersonImpl person = plans.getPersons().get(this.agentId);
		if (person == null) return this;

		PlanImpl plan = person.getSelectedPlan();

		// find the actual activity by searching all activity links
		// for a vehicle with this agent id

		for (int i=0;i< plan.getPlanElements().size(); i+=2) {
			ActivityImpl act = (ActivityImpl)plan.getPlanElements().get(i);
			QueueLink link = net.getQueueLink(act.getLinkId());
			Collection<QueueVehicle> allVehicles = link.getAllVehicles();
			for (QueueVehicle veh : allVehicles) {
				if (veh.getDriver().getPerson().getId().compareTo(this.agentId) == 0) {
					// we found the little nutty, now lets reason about the length of 1st activity
					double departure = veh.getDriver().getDepartureTime();
					double arrival = act.getStartTime(); //we do not really know if the act started there, but it should have!
					double diff =  departure - arrival;
					this.finished = (this.now - arrival) / diff;
					this.activityNr = i/2;
				}
			}
		}
		return this;
	}

	public void remove() {
	}

	public void draw(OTFDrawer drawer) {
	}

	public boolean isAlive() {
		return false;
	}

	public Type getType() {
		return OTFQuery.Type.AGENT;
	}

	public void setId(String id) {
		this.agentId = new IdImpl(id);
	}

	public void setNow(double now) {
		this.now = now;
	}
}
