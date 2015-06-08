package com.thenetcircle.newsfeed.titan.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.thenetcircle.newsfeed.base.client.AResult;
import com.thenetcircle.newsfeed.base.model.AModel;
import com.thenetcircle.newsfeed.titan.TitanService;
import com.thinkaurelius.titan.core.TitanEdge;
import com.thinkaurelius.titan.core.TitanVertex;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONMode;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONUtility;

public class TitanResult extends AResult {

	public static JSONObject getVertexJSON(Vertex v) {
		JSONObject r = new JSONObject();
		if (null == v) {
			return r;
		}
		Iterator<String> keyIterator = v.getPropertyKeys().iterator();
		while (keyIterator.hasNext()) {
			String name = keyIterator.next();
			try {
				if (name.equals(AModel.IID)) {
					r.put(AModel.COMMUNITY_ID, v.getProperty(name));
				} else {
					r.put(name, v.getProperty(name));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return r;
	}

	public static JSONObject transferVerticesToJSONObject(String community,
			Iterator<Vertex> i, boolean showTypes) {

		JSONObject r = new JSONObject();
		int count = 0;
		JSONArray a = new JSONArray();
		while (i.hasNext()) {
			Vertex next = i.next();
			final GraphSONMode mode = showTypes ? GraphSONMode.EXTENDED
					: GraphSONMode.NORMAL;
			Object modelName = next.getProperty(AModel.PROPERTY_MODEL);
			if(modelName == null){
				continue;
			}
			ArrayList<String> legallFields = TitanService
					.getLegallFieldNamesByCommunityAndModel(community, String
							.valueOf(modelName));
			try {
				a.put(GraphSONUtility.jsonFromElement(next, new HashSet<String>(
						legallFields), mode));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			count++;
		}
		try {
			r.put("result", a);
			r.put("count", count);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return r;
	}

	public static JSONObject getEdgesJSON(TitanVertex vertex) {
		JSONObject result = new JSONObject();
		if (null == vertex) {
			return result;
		}
		try {
			Iterator<TitanEdge> keyIterator = vertex.getEdges().iterator();

			result.put("count", vertex.getEdgeCount());
			JSONArray list = new JSONArray();
			while (keyIterator.hasNext()) {
				TitanEdge edge = keyIterator.next();
				JSONObject obj = new JSONObject();
				obj.put(edge.getLabel(), edge.getDirection(vertex));
				list.put(obj);
			}
			result.put("result", list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

}
