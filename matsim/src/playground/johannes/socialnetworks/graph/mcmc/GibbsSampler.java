/* *********************************************************************** *
 * project: org.matsim.*
 * GibbsSampler.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2009 by the members listed in the COPYING,        *
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

/**
 * 
 */
package playground.johannes.socialnetworks.graph.mcmc;

import java.util.Random;

import org.apache.log4j.Logger;


/**
 * @author illenberger
 *
 */
public class GibbsSampler {
	
	private static final Logger logger = Logger.getLogger(GibbsSampler.class);

	protected Random random;
	
	private int inteval = 100000;
	
	public GibbsSampler() {
		random = new Random();
	}
	
	public GibbsSampler(long seed) {
		random = new Random(seed);
	}
	
	public void setInterval(int interval) {
		this.inteval = interval;
	}
	
	public void sample(AdjacencyMatrix y, ConditionalDistribution d, SampleHandler handler) {
		long time = System.currentTimeMillis();
		
		int accept = 0;
//		boolean terminate = false;
		long it = 0;
		while(handler.handle(y, it)) {
//		for(long it = 0; it < burninTime; it++) {
			it++;
			if(step(y, d))
				accept++;
			
			if(it % inteval == 0) {
				logger.info(String.format("[%1$s] Accepted %2$s of %3$s steps (ratio = %4$s).", it, accept, inteval, accept/(float)inteval));
				accept = 0;
//				if(handler.checkTerminationCondition(y)) {
//					logger.info("Termination condition reached.");
//					break;
//				}
			}
		}
		
//		logger.info(String.format("Drawing %1$s samples in %2$s steps...", handler.getSampleSize(), handler.getSampleInterval()));
//		for(int it = 0; it < (handler.getSampleSize() * handler.getSampleInterval()); it++) {
//			if(step(y, d))
//				accept++;
//			
//			if(it % handler.getSampleInterval() == 0) {
//				handler.handleSample(y);
//			}
//			
//			if(it % inteval == 0) {
//				logger.info(String.format("[%1$s] Accepted %2$s of %3$s steps (ratio = %4$s).", it, accept, inteval, accept/(float)inteval));
//				accept = 0;
//			}
//		}
		
		logger.info(String.format("Sampling done in %1$s s.", (System.currentTimeMillis() - time)/1000));
	}
	
	public boolean step(AdjacencyMatrix m, ConditionalDistribution d) {
		boolean accept = false;
		int i = random.nextInt(m.getVertexCount());
		int j = random.nextInt(m.getVertexCount());
		
		if(i != j) {
			boolean y_ij = m.getEdge(i, j);
//			double p = 1 / (1 + d.changeStatistic(m, i, j, y_ij));
			double p = d.getNormConstant(i) / (1 + d.changeStatistic(m, i, j, y_ij));
			
			if(random.nextDouble() <= p) {
				/*
				 * Switch or leave the edge on.
				 */
				if(!y_ij) {
					m.addEdge(i, j);
					accept = true;;
				}
			} else {
				/*
				 * Switch or leave the edge off.
				 */
				if(y_ij) {
					m.removeEdge(i, j);
					accept = true;
				}
			}
		}
		
		return accept;
	}
	
}
