/**
 * 
 */
package com.thenetcircle.newsfeed.titan;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanProperty;
import com.thinkaurelius.titan.core.TitanRelation;
import com.thinkaurelius.titan.core.TitanTransaction;
import com.thinkaurelius.titan.core.TitanVertex;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author jchi
 * 
 */
public class Utils {
	private static final Logger logger = Logger.getLogger(Utils.class
			.getName());

	/**
	 * 
	 */
	public Utils() {
		// TODO Auto-generated constructor stub
	}

	private void printVertexPs(TitanGraph graph, TitanVertex v) {
		logger.debug(String.format("printVertexPs Vertex(ID:%s) .... \n",
				v.getID()));
		TitanTransaction gt = graph.buildTransaction()
				.checkInternalVertexExistence().start();
		TitanVertex vee = gt.getVertex(v.getID());
		if (null != vee) {
			logger.debug(String.format("Vertex %s is removed: %s!", v.getID(),
					vee.isRemoved()));
		}
		gt.commit();

		Iterator<TitanProperty> ps = v.getProperties().iterator();
		while (ps.hasNext()) {
			TitanProperty p = ps.next();
			logger.debug(String.format("  > property: %s!", p.getValue()
					.toString()));
		}
		Iterator<TitanRelation> rs = v.getRelations().iterator();
		while (rs.hasNext()) {
			TitanRelation r = rs.next();
			logger.debug(String.format("  > relationship: %s!", r.toString()));
		}

	}

	private void printAllVertices(TitanGraph graph) {
		logger.debug(String.format("printAllVertices ... "));
		TitanTransaction gt = graph.buildTransaction()
				.checkInternalVertexExistence().start();
		Iterator<Vertex> vs = gt.getVertices().iterator();
		while (vs.hasNext()) {
			TitanVertex v = (TitanVertex) vs.next();
			logger.debug(String
					.format("  > vertex id : %s! is deleted: %s; is modified: %s; is new : %s",
							v.getID(), v.isRemoved(), v.isModified(), v.isNew()));
		}
		gt.commit();
	}
}
