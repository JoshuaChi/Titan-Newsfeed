package com.thenetcircle.newsfeed.bootstrap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.thenetcircle.newsfeed.titan.TitanService;

public class NewsfeedContextListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent e) {
	}

	/**
	 * We init the titanclient for all communities defined in json file, but
	 * only for $env.
	 */
	@Override
	public void contextInitialized(ServletContextEvent e) {
		ServletContext context = e.getServletContext();
		try {
			initTitan(context.getInitParameter("env"),
					context.getResourceAsStream(context
							.getInitParameter("service-conf")));
			initCommunityModels(context.getResourceAsStream(context
					.getInitParameter("community-schema")));
			initCommunityModelAsVerticesSQLs(context.getResourceAsStream(context
					.getInitParameter("community-model-vertex-init-sql")));
			initCommunityModelAsEdgesSQLs(context.getResourceAsStream(context
					.getInitParameter("community-model-edge-init-sql")));
			initCommunityDatabase(context.getInitParameter("env"),
					context.getResourceAsStream(context
							.getInitParameter("community-database-configure")));
			initCommunityHashcode(context.getInitParameter("env"),
					context.getResourceAsStream(context
							.getInitParameter("community-hashcode")));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected void initTitan(String envConf, InputStream stream)
			throws IOException, JSONException {
		JSONObject jsonObject = parseInputStream(stream);
		Iterator<String> keys = jsonObject.keys();
		while (keys.hasNext()) {
			String community = keys.next();
			JSONObject envsJsonObject = jsonObject.getJSONObject(community);
			System.out.println("community:" + community);
			System.out.println("json:" + envsJsonObject.toString());
			TitanService.addClient(community,
					envsJsonObject.getJSONObject(envConf));
		}
	}

	private JSONObject parseInputStream(InputStream stream) throws IOException,
			JSONException {
		int content;
		StringBuilder sb = new StringBuilder();
		while ((content = stream.read()) != -1) {
			sb.append((char) content);
		}

		JSONObject jsonObject = new JSONObject(sb.toString());
		return jsonObject;
	}

	/**
	 * @param resourceAsStream
	 * @throws JSONException
	 * @throws IOException
	 */
	private void initCommunityModels(InputStream resourceAsStream)
			throws IOException, JSONException {
		JSONObject jsonObject = parseInputStream(resourceAsStream);
		Iterator<String> keys = jsonObject.keys();
		while (keys.hasNext()) {
			String community = keys.next();
			JSONArray allModels = jsonObject.getJSONArray(community);
			System.out.println("community:" + community);
			System.out.println("models:" + allModels.toString());
			TitanService.initModelsForCommunity(community, allModels);
		}

	}

	/**
	 * @param resourceAsStream
	 * @throws JSONException
	 * @throws IOException
	 */
	private void initCommunityModelAsVerticesSQLs(InputStream resourceAsStream)
			throws IOException, JSONException {
		JSONObject jsonObject = parseInputStream(resourceAsStream);
		Iterator<String> keys = jsonObject.keys();
		while (keys.hasNext()) {
			String community = keys.next();
			JSONArray allModels = jsonObject.getJSONArray(community);
			System.out.println("community:" + community);
			System.out.println("models:" + allModels.toString());
			TitanService.setModelVerticesInitSQLForCommunity(community, allModels);
		}

	}

	/**
	 * @param resourceAsStream
	 * @throws JSONException
	 * @throws IOException
	 */
	private void initCommunityModelAsEdgesSQLs(InputStream resourceAsStream)
			throws IOException, JSONException {
		JSONObject jsonObject = parseInputStream(resourceAsStream);
		Iterator<String> keys = jsonObject.keys();
		while (keys.hasNext()) {
			String community = keys.next();
			JSONArray allModels = jsonObject.getJSONArray(community);
			System.out.println("community:" + community);
			System.out.println("models:" + allModels.toString());
			TitanService.setModelEdgesInitSQLForCommunity(community, allModels);
		}

	}

	/**
	 * @param initParameter
	 * @param resourceAsStream
	 * @throws JSONException
	 * @throws IOException
	 */
	private void initCommunityDatabase(String env, InputStream resourceAsStream)
			throws IOException, JSONException {
		JSONObject jsonObject = parseInputStream(resourceAsStream);
		Iterator<String> keys = jsonObject.keys();
		while (keys.hasNext()) {
			String community = keys.next();
			JSONObject configure = jsonObject.getJSONObject(community);
			System.out.println("community:" + community);
			System.out.println("json:" + configure.toString());
			TitanService.initDatabaseConfigureForCommunity(community,
					configure.getJSONObject(env));
		}
	}

	/**
	 * @param initParameter
	 * @param resourceAsStream
	 * @throws JSONException 
	 * @throws IOException 
	 */
	private void initCommunityHashcode(String env,
			InputStream resourceAsStream) throws IOException, JSONException {
		JSONObject jsonObject = parseInputStream(resourceAsStream);
		Iterator<String> keys = jsonObject.keys();
		while (keys.hasNext()) {
			String community = keys.next();
			JSONObject configure = jsonObject.getJSONObject(community);
			System.out.println("community:" + community);
			System.out.println("json:" + configure.toString());
			TitanService.initHashcodeForCommunity(community,
					configure.getString(env));
		}

	}

}
