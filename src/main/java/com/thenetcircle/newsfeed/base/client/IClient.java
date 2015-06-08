package com.thenetcircle.newsfeed.base.client;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.thenetcircle.newsfeed.base.client.exceptions.IllegalEdgeException;
import com.thenetcircle.newsfeed.base.client.exceptions.IllegalFieldNameException;
import com.thenetcircle.newsfeed.base.client.exceptions.KeyIndexExistsException;
import com.thenetcircle.newsfeed.base.client.exceptions.LabelIndexExistsException;
import com.thenetcircle.newsfeed.base.client.exceptions.VertexExistsException;
import com.thenetcircle.newsfeed.base.client.exceptions.VertexNotExistsException;
import com.thenetcircle.newsfeed.titan.client.exceptions.IllegalStartVertexDefinitionException;
import com.thenetcircle.newsfeed.titan.client.exceptions.KeyIndexTypeNotDefinedException;
import com.thenetcircle.newsfeed.titan.client.exceptions.ModelIIDNotCreatedException;
import com.thenetcircle.newsfeed.titan.client.exceptions.NoPathEdgeDefiendException;
import com.thenetcircle.newsfeed.titan.client.exceptions.NoStartVertexFoundException;
import com.thenetcircle.newsfeed.titan.client.exceptions.SortKeyNotExistsException;
import com.thenetcircle.newsfeed.titan.client.exceptions.UnknownEdgeDirectionException;
import com.thenetcircle.newsfeed.titan.client.exceptions.UnknownKeyIndexException;
import com.thenetcircle.newsfeed.titan.client.exceptions.UnknownKeyIndexTypeException;
import com.thenetcircle.newsfeed.titan.client.exceptions.UnknownOrderDirectionException;
import com.thenetcircle.newsfeed.titan.client.exceptions.VertexIdNotExistsException;
import com.thenetcircle.newsfeed.titan.model.pojo.criteria.FilterCriteria;

public interface IClient {

	/**
	 * Create graph vertex with properties defined in JSON format
	 * input<@fieldsJsonObject>.
	 * 
	 * @param community
	 * @param model
	 * @param fieldsJsonObject
	 * @return boolean
	 * @reserved keywords: iid/created_at/model
	 * @throws VertexExistsException
	 *             when vertex exists
	 * @throws IllegalFieldNameException
	 * @throws VertexIdNotExistsException
	 *             - when create vertex, we expect there is id field
	 * @throws KeyIndexTypeNotDefinedException
	 */
	public boolean createVertex(String community, String model,
			JSONObject fieldsJsonObject) throws VertexExistsException,
			IllegalFieldNameException, VertexIdNotExistsException,
			KeyIndexTypeNotDefinedException;

	/**
	 * We rely on all vertices have iid field.
	 * 
	 * Create edge with <edgeLabel> between <fromKeyIndexValue> and
	 * <toKeyIndexValue>
	 * 
	 * @param fromKeyIndexValue
	 * @param fromModel
	 * @param toKeyIndexValue
	 * @param toModel
	 * @param edgeLabel
	 * @return boolean
	 * @throws IllegalEdgeException
	 * @throws VertexNotExistsException
	 */
	public boolean createEdge(int fromKeyIndexValue, String fromModel,
			int toKeyIndexValue, String toModel, String edgeLabel)
			throws IllegalEdgeException, VertexNotExistsException;

	/**
	 * 
	 * @param key
	 * @param dataType
	 * @param jsonArray
	 * @return boolean
	 * @throws IllegalArgumentException
	 * @throws JSONException
	 * @throws KeyIndexExistsException
	 * @throws UnknownKeyIndexTypeException
	 * @throws UnknownKeyIndexException
	 */
	public boolean createKey(String key, String dataType, JSONArray jsonArray)
			throws IllegalArgumentException, KeyIndexExistsException,
			UnknownKeyIndexTypeException, UnknownKeyIndexException;

	/**
	 * 
	 * @param name
	 * @param sortKeyName
	 *            - default: time
	 * @param sortOrder
	 *            : default 2(1-asc; 2-desc)
	 * @return boolean
	 * @throws LabelIndexExistsException
	 * @throws SortKeyNotExistsException
	 * @throws UnknownOrderDirectionException
	 */
	public boolean createLabel(String name, String sortKeyName, int sortOrder)
			throws LabelIndexExistsException, SortKeyNotExistsException,
			UnknownOrderDirectionException;

	/**
	 * 
	 * @param community
	 * @param filterCriteria
	 * @param showTypes
	 *            will choose GraphSONMode.EXTENDED or GraphSONMode.NORMAL;
	 * @return JSONObject
	 * @throws KeyIndexTypeNotDefinedException
	 * @throws NoPathEdgeDefiendException
	 * @throws IllegalStartVertexDefinitionException
	 * @throws NoStartVertexFoundException
	 */
	public JSONObject filter(String community, FilterCriteria filterCriteria,
			boolean showTypes) throws KeyIndexTypeNotDefinedException,
			NoPathEdgeDefiendException, IllegalStartVertexDefinitionException,
			NoStartVertexFoundException;

	/**
	 * 
	 * @param name
	 * @return boolean
	 */
	public boolean hasPropertyKey(String name);

	/**
	 * 
	 * @param name
	 * @return boolean
	 */
	public boolean hasLabel(String name);

	/**
	 * 
	 * @param name
	 * @return boolean
	 */
	public boolean hasIndex(String name);

	/**
	 * Delete vertex based on model & id
	 * 
	 * @param community
	 * @param model
	 * @param id
	 * @return boolean; true - success when vertex doesn't exist or delete
	 *         successfully!
	 * @throws ModelIIDNotCreatedException
	 * @throws VertexNotExistsException
	 */
	public boolean deleteVertex(String community, String model, int id)
			throws ModelIIDNotCreatedException, VertexNotExistsException;

	/**
	 * Get vertex by <community>, <model> and vertex <id>
	 * 
	 * @param community
	 * @param model
	 * @param id
	 * @param showTypes
	 *            will choose GraphSONMode.EXTENDED or GraphSONMode.NORMAL;
	 * @return JSONObject if exists or null if not exists or catch exception
	 */
	public JSONObject getVertex(final String community, final String model,
			int id, final boolean showTypes);

	/**
	 * Update Vertex properties
	 * 
	 * @param community
	 * @param model
	 * @param id
	 * @param showTypes
	 * @param properties
	 * @return JSONObject
	 */
	public JSONObject updateVertex(final String community, final String model,
			int id, final boolean showTypes, JSONArray properties);

	/**
	 * Get all edges start from <model>-<vertexId>
	 * 
	 * @param community
	 * @param model
	 * @param vertexId
	 * @return JSONObject
	 */
	public JSONObject getEdges(String community, String model, int vertexId);

	/**
	 * Delete edges <direction> from Vertex<fromModel>:<fromId>
	 * 
	 * @param vertexId
	 *            "iid"'s value
	 * @param vertexModel
	 * @param direction
	 *            PATH_DIRECTION_OUT = 1; PATH_DIRECTION_IN = 2;
	 *            PATH_DIRECTION_BOTH = 3;
	 * @param label
	 * @return boolean
	 * @throws ModelIIDNotCreatedException
	 * @throws UnknownEdgeDirectionException
	 * @throws VertexNotExistsException
	 */
	public boolean deleteEdges(int vertexId, String vertexModel, int direction,
			String label) throws ModelIIDNotCreatedException,
			UnknownEdgeDirectionException, VertexNotExistsException;

	/**
	 * Init community models before trying to use newsfeed service.
	 * 
	 * @param community
	 * @param model
	 * @param start
	 *            - sql offset
	 * @param end
	 *            - sql end
	 * @param batch
	 *            - batch size for both sql and graph bulk load;
	 * @return boolean
	 */
	public boolean initVertex(String community, String model, int start,
			int end, int batch);

	/**
	 * Init community edges according to edge definition(JSON)
	 * 
	 * @param community
	 * @param edgeLabel
	 * @param start
	 * @param end
	 * @param batch
	 * @return boolean
	 */
	public boolean initEdge(String community, String edgeLabel, int start,
			int end, int batch);

}
