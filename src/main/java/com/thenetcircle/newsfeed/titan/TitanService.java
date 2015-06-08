package com.thenetcircle.newsfeed.titan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.thenetcircle.newsfeed.base.model.AModel;
import com.thenetcircle.newsfeed.titan.client.TitanClient;
import com.thenetcircle.newsfeed.titan.model.DatabaseConfigure;
import com.thenetcircle.newsfeed.titan.model.init.NewsfeedEdge;
import com.thenetcircle.newsfeed.titan.model.pojo.CommunityModelField;
import com.thenetcircle.newsfeed.titan.model.pojo.CommunityModelFields;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;

public class TitanService {
	private static final HashMap<String, TitanClient> clients = new HashMap<String, TitanClient>();
	private static final HashMap<String, HashMap<String, CommunityModelFields>> communityModels = new HashMap<String, HashMap<String, CommunityModelFields>>();
	private static final HashMap<String, HashMap<String, String>> communityModelVerticesInitSQLs = new HashMap<String, HashMap<String, String>>();
	private static final HashMap<String, HashMap<String, NewsfeedEdge>> communityModelEdgesInitSQLs = new HashMap<String, HashMap<String, NewsfeedEdge>>();
	private static final HashMap<String, DatabaseConfigure> communityDatabaseConfigure = new HashMap<String, DatabaseConfigure>();
	private static final HashMap<String, String> communityHashcode = new HashMap<String, String>();

	public static void addClient(String community,
			JSONObject configureJsonObject) {
		TitanClient client = new TitanClient(getTitanGraph(configureJsonObject));
		clients.put(community, client);
	}

	public static TitanClient getClientByCommunity(String community) {
		return clients.get(community);
	}

	private static TitanGraph getTitanGraph(JSONObject configureJsonObject) {
		Configuration conf = new BaseConfiguration();
		try {
			// local only
			// conf.setProperty("storage.buffer-size", 0);
			// conf.setProperty("cache.db-cache-clean-wait", 0);
			// conf.setProperty(GraphDatabaseConfiguration.ALLOW_SETTING_VERTEX_ID_KEY,
			// false);
			// conf.setProperty(GraphDatabaseConfiguration.DB_CACHE_CLEAN_WAIT_KEY,
			// 0);
			// conf.setProperty(GraphDatabaseConfiguration.DB_CACHE_SIZE_KEY,
			// 0);
			// conf.setProperty(GraphDatabaseConfiguration.DB_CACHE_TIME_KEY,
			// 0);
			// conf.setProperty(GraphDatabaseConfiguration.IDS_FLUSH_KEY,
			// false);
			// conf.setProperty(GraphDatabaseConfiguration.DB_CACHE_CLEAN_WAIT_KEY,
			// 0);
			// conf.setProperty(GraphDatabaseConfiguration.DB_CACHE_TIME_KEY,
			// 0);
			// conf.setProperty(GraphDatabaseConfiguration.TX_CACHE_SIZE_KEY,
			// 0);
			//
			conf.setProperty("storage.backend",
					configureJsonObject.getString("storage.backend"));

			if ("hbase"
					.equals(configureJsonObject.getString("storage.backend"))) {
				conf.setProperty("storage.tablename",
						configureJsonObject.getString("storage.tablename"));
			} else if ("cassandra" == configureJsonObject
					.getString("storage.backend")) {
				conf.setProperty("storage.port",
						configureJsonObject.getString("storage.port"));
				conf.setProperty("storage.keyspace",
						configureJsonObject.getString("storage.keyspace"));
				conf.setProperty("storage.connection-timeout",
						configureJsonObject
								.getString("storage.connection-timeout"));
				conf.setProperty("storage.connection-pool-size",
						configureJsonObject
								.getString("storage.connection-pool-size"));
				conf.setProperty("storage.read-consistency-level",
						configureJsonObject
								.getString("storage.read-consistency-level"));
				conf.setProperty("storage.write-consistency-level",
						configureJsonObject
								.getString("storage.write-consistency-level"));
				conf.setProperty("storage.replication-factor",
						configureJsonObject
								.getString("storage.replication-factor"));
			}

			conf.setProperty("storage.batch-loading", "true");
			conf.setProperty("storage.hostname",
					configureJsonObject.getString("storage.hostname"));

			conf.setProperty("storage.lock-wait-time", "100");
			conf.setProperty("storage.write-attempts", "5");
			conf.setProperty("storage.attempt-wait", "10000");
			conf.setProperty("ids.block-size", 10000000);
			conf.setProperty("ids.renew-timeout", 600000);
			conf.setProperty("ids.renew-percentage", 0.98);
			conf.setProperty("storage.buffer-size", 10240);
			conf.setProperty("autotype", "none");

			conf.setProperty("storage.index.search.backend",
					configureJsonObject
							.getString("storage.index.search.backend"));
			conf.setProperty("storage.index.search.client-only",
					configureJsonObject
							.getString("storage.index.search.client-only"));
			conf.setProperty("storage.index.search.hostname",
					configureJsonObject
							.getString("storage.index.search.hostname"));
			conf.setProperty("storage.index.search.local-mode",
					configureJsonObject
							.getString("storage.index.search.local-mode"));
			conf.setProperty("storage.index.search.cluster-name",
					configureJsonObject
							.getString("storage.index.search.cluster-name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TitanFactory.open(conf);
		// return TitanFactory.open("/tmp/titan143");

	}

	/**
	 * 
	 * @param community
	 * @param allModels
	 *            -
	 */
	public static void initModelsForCommunity(String community,
			JSONArray allModels) {
		HashMap<String, CommunityModelFields> modelMap = new HashMap<String, CommunityModelFields>();
		try {
			for (int i = 0; i < allModels.length(); i++) {
				JSONObject aModel;
				aModel = allModels.getJSONObject(i);
				Iterator<String> keys = aModel.keys();
				while (keys.hasNext()) {
					String modelName = keys.next();
					CommunityModelFields model = new CommunityModelFields();
					model.setFields(aModel.getJSONArray(modelName));
					modelMap.put(modelName, model);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		communityModels.put(community, modelMap);
	}

	public static HashMap<String, CommunityModelFields> getAllModelsByCommunity(
			String community) {
		return communityModels.get(community);
	}

	/**
	 * Append system reserved model fields.
	 * 
	 * @param community
	 * @param modelName
	 * @return ArrayList<String>
	 */
	public static ArrayList<String> getLegallFieldNamesByCommunityAndModel(
			String community, String modelName) {
		HashMap<String, CommunityModelFields> communityHM = communityModels
				.get(community);
		ArrayList<String> modelFields = communityHM.get(modelName)
				.getFieldNames();
		String[] reservedFields = AModel.getReservedModelFields();
		for (int i = 0; i < reservedFields.length; i++) {
			modelFields.add(reservedFields[i]);
		}
		return modelFields;
	}

	public static ArrayList<CommunityModelField> getLegallFieldsByCommunityAndModel(
			String community, String modelName) {
		HashMap<String, CommunityModelFields> communityHM = communityModels
				.get(community);
		return communityHM.get(modelName).getFields();
	}

	public static void setModelVerticesInitSQLForCommunity(String community,
			JSONArray allVertices) {
		HashMap<String, String> modelMap = new HashMap<String, String>();
		try {
			for (int i = 0; i < allVertices.length(); i++) {
				JSONObject aModel;
				aModel = allVertices.getJSONObject(i);
				Iterator<String> keys = aModel.keys();
				while (keys.hasNext()) {
					String modelName = keys.next();
					modelMap.put(modelName, aModel.getString(modelName));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		communityModelVerticesInitSQLs.put(community, modelMap);
	}

	public static void setModelEdgesInitSQLForCommunity(String community,
			JSONArray allEdges) {
		HashMap<String, NewsfeedEdge> modelMap = new HashMap<String, NewsfeedEdge>();
		try {
			for (int i = 0; i < allEdges.length(); i++) {
				JSONObject aEdge;
				aEdge = allEdges.getJSONObject(i);
				Iterator<String> keys = aEdge.keys();
				while (keys.hasNext()) {
					String edgeLabel = keys.next();
					JSONObject edgeDefinition = aEdge.getJSONObject(edgeLabel);
					JSONObject fromJSONObject = edgeDefinition.getJSONObject("from");
					JSONObject toJSONObject = edgeDefinition.getJSONObject("to");

					NewsfeedEdge newsfeedEdge = new NewsfeedEdge();
					newsfeedEdge.push(fromJSONObject.getString("model"),
							fromJSONObject.getString("sql"),
							toJSONObject.getString("model"),
							toJSONObject.getString("sql"),
							edgeDefinition.getInt("direction"),
							edgeDefinition.getString("from-id"),
							edgeDefinition.getString("to-id"));
					modelMap.put(edgeLabel, newsfeedEdge);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		communityModelEdgesInitSQLs.put(community, modelMap);
	}

	public static String getInitSQLForCommunityModel(String community,
			String model) {
		HashMap<String, String> modelSQL = communityModelVerticesInitSQLs
				.get(community);
		return modelSQL.get(model);
	}

	public static NewsfeedEdge getEdgeFromCommunityModel(String community,
			String edgeLabel){
		HashMap<String, NewsfeedEdge> hashMap = communityModelEdgesInitSQLs
				.get(community);
		return hashMap.get(edgeLabel);
	}
	public static String getFromVertexSQLForCommunityModel(String community,
			String edgeLabel) {
		NewsfeedEdge edge = getEdgeFromCommunityModel(community, edgeLabel);
		return edge.getFrom().getSql();
	}

	public static String getToVertexSQLForCommunityModel(String community,
			String edgeLabel) {
		NewsfeedEdge edge = getEdgeFromCommunityModel(community, edgeLabel);
		return edge.getTo().getSql();
	}
	
	public static void initDatabaseConfigureForCommunity(String community,
			JSONObject configureObject) {
		DatabaseConfigure configure = new DatabaseConfigure();
		try {
			configure.setUsername(configureObject.getString("username"));
			configure.setPassword(configureObject.getString("password"));
			configure.setHost(configureObject.getString("host"));
			configure.setDatabase(configureObject.getString("database"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		communityDatabaseConfigure.put(community, configure);
	}

	public static DatabaseConfigure getDatabaseConfigureByCommunity(
			String community) {
		return communityDatabaseConfigure.get(community);
	}

	public static void initHashcodeForCommunity(String community,
			String hashcode) {
		communityHashcode.put(community, hashcode);
	}

	public static String getHashcodeByCommunity(String community) {
		return communityHashcode.get(community);
	}
}
