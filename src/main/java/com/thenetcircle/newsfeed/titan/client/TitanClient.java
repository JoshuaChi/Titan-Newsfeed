package com.thenetcircle.newsfeed.titan.client;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.elasticsearch.common.joda.time.DateTime;

import com.thenetcircle.newsfeed.base.client.IClient;
import com.thenetcircle.newsfeed.base.client.exceptions.IllegalEdgeException;
import com.thenetcircle.newsfeed.base.client.exceptions.IllegalFieldNameException;
import com.thenetcircle.newsfeed.base.client.exceptions.KeyIndexExistsException;
import com.thenetcircle.newsfeed.base.client.exceptions.LabelIndexExistsException;
import com.thenetcircle.newsfeed.base.client.exceptions.VertexExistsException;
import com.thenetcircle.newsfeed.base.client.exceptions.VertexNotExistsException;
import com.thenetcircle.newsfeed.base.model.AModel;
import com.thenetcircle.newsfeed.titan.TitanService;
import com.thenetcircle.newsfeed.titan.client.exceptions.IllegalStartVertexDefinitionException;
import com.thenetcircle.newsfeed.titan.client.exceptions.InvilidatePathEdgeDefinitionException;
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
import com.thenetcircle.newsfeed.titan.model.DatabaseConfigure;
import com.thenetcircle.newsfeed.titan.model.NewsfeedConstant;
import com.thenetcircle.newsfeed.titan.model.TitanModel;
import com.thenetcircle.newsfeed.titan.model.init.NewsfeedEdge;
import com.thenetcircle.newsfeed.titan.model.init.NewsfeedVertex;
import com.thenetcircle.newsfeed.titan.model.pojo.CommunityModelField;
import com.thenetcircle.newsfeed.titan.model.pojo.CommunityModelFields;
import com.thenetcircle.newsfeed.titan.model.pojo.criteria.FilterCriteria;
import com.thenetcircle.newsfeed.titan.model.pojo.criteria.NewfeedPathEdgeCriteria;
import com.thenetcircle.newsfeed.titan.model.pojo.criteria.NewfeedPathPropertyCriteria;
import com.thenetcircle.newsfeed.titan.model.pojo.criteria.NewsfeedPathCriteria;
import com.thenetcircle.newsfeed.titan.model.pojo.criteria.NewsfeedPathPropertyKeyValueCriteria;
import com.thenetcircle.newsfeed.titan.model.pojo.criteria.NewsfeedStartCriteria;
import com.thinkaurelius.titan.core.KeyMaker;
import com.thinkaurelius.titan.core.LabelMaker;
import com.thinkaurelius.titan.core.Order;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanKey;
import com.thinkaurelius.titan.core.TitanTransaction;
import com.thinkaurelius.titan.core.TitanType;
import com.thinkaurelius.titan.core.TitanVertex;
import com.thinkaurelius.titan.core.attribute.Geo;
import com.thinkaurelius.titan.core.attribute.Geoshape;
import com.thinkaurelius.titan.core.util.TitanCleanup;
import com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration;
import com.thinkaurelius.titan.graphdb.database.StandardTitanGraph;
import com.thinkaurelius.titan.graphdb.types.StandardKeyMaker;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.ElementHelper;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONMode;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONUtility;
import com.tinkerpop.blueprints.util.wrappers.batch.BatchGraph;
import com.tinkerpop.blueprints.util.wrappers.batch.VertexIDType;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.structures.Pair;

public class TitanClient implements IClient {
	// if commit fails due to unique vertex issue, we will retry
	private final static int COMMIT_RETRIES = 5;

	private static final Logger logger = Logger.getLogger(TitanClient.class
			.getName());

	protected TitanGraph graph = null;

	public TitanGraph getGraph() {
		return graph;
	}

	public TitanClient(TitanGraph titanGraph) {
		logger.setLevel(Level.DEBUG);

		graph = titanGraph;
	}

	public boolean createVertex(String community, String model,
			JSONObject fieldsJSONObject) throws VertexExistsException,
			VertexIdNotExistsException, IllegalFieldNameException,
			KeyIndexTypeNotDefinedException {
		graph.rollback();
		logger.debug(String.format("\n\n\n"));
		logger.debug(String.format("=====CREATE Vertex====="));

		try {
			HashMap<String, CommunityModelFields> models = TitanService
					.getAllModelsByCommunity(community);

			// get all legal fields for this model & community
			// ArrayList<String> fields = models.get(model).getFields();
			ArrayList<String> fields = TitanService
					.getLegallFieldNamesByCommunityAndModel(community, model);

			TitanVertex vertex = (TitanVertex) graph.addVertex(null);
			if (false == this.hasIndex(AModel.IID)) {
				throw new KeyIndexTypeNotDefinedException();
			}
			// fields must contain field "id"
			if (false == fieldsJSONObject.has(AModel.COMMUNITY_ID)) {
				throw new VertexIdNotExistsException();
			}

			Iterator<String> keys = fieldsJSONObject.keys();
			while (keys.hasNext()) {
				String fieldName = keys.next();

				// validate field before continue
				if (!fields.contains(fieldName)) {
					throw new IllegalFieldNameException("FieldName: "
							+ fieldName
							+ " is illegal. Pelase double check schema.yml!");
				}

				JSONObject fieldProperties = fieldsJSONObject
						.getJSONObject(fieldName);

				if (fieldName.equals(AModel.COMMUNITY_ID)) {
					String storedId = AModel.getFormatedIID(model,
							fieldProperties.getInt("value"));
					logger.debug(String.format("ID:%s-IID:%s", vertex.getID(),
							fieldProperties.getInt("value")));
					TitanVertex toBeCheckedVertex = retrieveExistingVertex(storedId);

					if (null != toBeCheckedVertex) {
						throw new VertexExistsException();
					} else {
						vertex.addProperty(AModel.IID, storedId);
					}
				} else {
					vertex = addPropertyForVertex(vertex, fieldName,
							fieldProperties);
				}
			}

			// append two fields for titan inside usages
			vertex.addProperty(TitanModel.PROPERTY_CREATED_AT, DateTime.now()
					.getMillis());
			vertex.addProperty(TitanModel.PROPERTY_MODEL, model);
		} catch (VertexExistsException e1) {
			throw e1;
		} catch (VertexIdNotExistsException e2) {
			throw e2;
		} catch (IllegalFieldNameException e3) {
			throw e3;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug(String.format(" starting commit..."));
			graph.commit();
			logger.debug(String.format(" finish commit..."));

		}
		logger.debug(String.format("======C====="));
		return true;
	}

	/**
	 * @param vertex
	 * @param fieldName
	 * @param fieldProperties
	 * @throws JSONException
	 */
	private TitanVertex addPropertyForVertex(TitanVertex vertex,
			String fieldName, JSONObject fieldProperties) throws JSONException {
		String type = fieldProperties.getString("type");
		switch (type) {
		case "int":
			vertex.addProperty(fieldName, fieldProperties.getInt("value"));
			break;
		case "long":
			vertex.addProperty(fieldName, fieldProperties.getLong("value"));
			break;
		case "double":
			vertex.addProperty(fieldName,
					String.format("%.7f", fieldProperties.getDouble("value")));
			break;
		case "string":
			vertex.addProperty(fieldName, fieldProperties.getString("value"));
			break;
		case "boolean":
			vertex.addProperty(fieldName, fieldProperties.getBoolean("value"));
			break;
		case "geo":
			JSONArray geoPair = fieldProperties.getJSONArray("value");
			vertex.addProperty(fieldName,
					Geoshape.point(geoPair.getDouble(0), geoPair.getDouble(1)));
			break;
		default:
			;
		}
		return vertex;
	}

	/**
	 * known issue with cassendra:
	 * http://wiki.apache.org/cassandra/DistributedDeletes
	 * 
	 * @param storedId
	 * @return TitanVertex
	 */
	private TitanVertex retrieveExistingVertex(String storedId) {
		if (this.hasPropertyKey(AModel.IID)
				&& graph.getVertices(AModel.IID, storedId).iterator().hasNext()) {
			TitanVertex vertex = (TitanVertex) graph
					.getVertices(AModel.IID, storedId).iterator().next();
			graph.commit();
			return vertex;
		}

		return null;
	}

	/**
	 * When creating a vertex, user can specify which field will be key indexed
	 * 
	 * @param fieldName
	 * @param isKeyJSONObject
	 * @throws KeyIndexTypeNotDefinedException
	 */
	private void attachKeyIndex(String fieldName, JSONObject isKeyJSONObject)
			throws KeyIndexTypeNotDefinedException {
		if (false == hasIndex(fieldName)) {
			try {
				// type, index
				String keyDatatype = isKeyJSONObject.getString("type");
				JSONArray indexJSONArray = isKeyJSONObject
						.getJSONArray("index");
				this.createKey(fieldName, keyDatatype, indexJSONArray);

			} catch (KeyIndexExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownKeyIndexTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownKeyIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (IllegalArgumentException iae) {
				throw new KeyIndexTypeNotDefinedException();
			} catch (JSONException e) {
				e.printStackTrace();
				throw new KeyIndexTypeNotDefinedException();
			}
		}

	}

	public boolean createEdge(int fromKeyIndexValue, String fromModel,
			int toKeyIndexValue, String toModel, String edgeLabel)
			throws IllegalEdgeException, VertexNotExistsException {

		graph.rollback();

		logger.debug(String.format("\n\n\n"));
		logger.debug(String.format("=====CREATE Edge====="));

		if (0 >= fromKeyIndexValue || 0 >= toKeyIndexValue || null == edgeLabel) {
			throw new IllegalEdgeException("Missed key-value pairs!");
		}

		final TitanTransaction tx = graph.newTransaction();
		try {
			TitanKey iidKey = tx.getPropertyKey(AModel.IID);

			// Check if key exists
			if (null == iidKey || iidKey.isEdgeLabel() == true
					|| iidKey.isRemoved()) {
				throw new IllegalEdgeException(
						"Edge label is not created or has been removed!");
			}

			TitanVertex fromVertex = tx.getVertex(iidKey,
					AModel.getFormatedIID(fromModel, fromKeyIndexValue));
			TitanVertex toVertex = tx.getVertex(iidKey,
					AModel.getFormatedIID(toModel, toKeyIndexValue));

			// If vertex doesn't exist
			if (fromVertex == null || toVertex == null) {
				throw new VertexNotExistsException(
						"from or to Vertex does not exists!");
			}
			logger.debug(String.format("from: %s - to: %s", fromVertex.getID(),
					toVertex.getID()));
			fromVertex.addEdge(edgeLabel, toVertex);

		} catch (IllegalEdgeException e1) {
			throw e1;
		} catch (VertexNotExistsException e2) {
			throw e2;
		} catch (Exception e3) {
			e3.printStackTrace();
		} finally {
			logger.debug(String.format(" starting commit..."));
			tx.commit();
			logger.debug(String.format(" finish commit..."));
		}

		logger.debug(String.format("====C===="));
		return true;
	}

	public boolean createKey(String key, String keyIndexType,
			JSONArray jsonArray) throws IllegalArgumentException,
			KeyIndexExistsException, UnknownKeyIndexTypeException,
			UnknownKeyIndexException {

		graph.rollback();

		logger.debug(String.format("\n\n\n"));
		logger.debug(String.format("=====Create Key====="));

		TitanTransaction tx = graph.newTransaction();
		try {
			if (this.hasPropertyKey(key)) {
				logger.debug(String.format("key was there already! : %s", key));
				throw new KeyIndexExistsException(String.format(
						"key was there already! : %s", key));
			}

			KeyMaker keyMaker = tx.makeKey(key);
			switch (keyIndexType) {
			case "string":
				keyMaker = keyMaker.dataType(String.class);
				break;
			case "int":
				keyMaker = keyMaker.dataType(Integer.class);
				break;
			case "long":
				keyMaker = keyMaker.dataType(Long.class);
				break;
			case "double":
				keyMaker = keyMaker.dataType(Double.class);
				break;
			case "boolean":
				keyMaker = keyMaker.dataType(Boolean.class);
				break;
			case "geo":
				keyMaker = keyMaker.dataType(Geoshape.class);
				break;
			default:
				throw new UnknownKeyIndexTypeException(keyIndexType);
			}

			for (int i = 0; i < jsonArray.length(); i++) {
				String index = jsonArray.getString(i);

				logger.debug(String.format("key type : %s", index));

				switch (index) {
				case "vertex":
					keyMaker = keyMaker.indexed(Vertex.class);
					break;
				case "edge":
					keyMaker = keyMaker.indexed(Edge.class);
					break;
				case "unique":
					keyMaker = (StandardKeyMaker) keyMaker.unique();
					break;
				case "single":
					keyMaker = (StandardKeyMaker) keyMaker.single();
					break;
				case "search-vertex":
					keyMaker = keyMaker.indexed("search", Vertex.class);
					break;
				case "search-edge":
					keyMaker = keyMaker.indexed("search", Edge.class);
					break;
				default:
					throw new UnknownKeyIndexException(index);
				}
			}
			keyMaker.make();

		} catch (KeyIndexExistsException e4) {
			throw e4;
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
			throw e1;
		} catch (UnknownKeyIndexTypeException e2) {
			throw e2;
		} catch (UnknownKeyIndexException e3) {
			throw e3;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.commit();
			graph.commit();
		}

		logger.debug(String.format("====C K I===="));
		return true;
	}

	public boolean createLabel(String name, String sortKeyName, int sortOrder)
			throws LabelIndexExistsException, SortKeyNotExistsException,
			UnknownOrderDirectionException {

		graph.rollback();

		logger.debug(String.format("\n\n\n"));
		logger.debug(String.format("=====Create Label Index====="));

		try {
			if (this.hasLabel(name)) {
				logger.debug(String.format("label was there already! : %s",
						name));
				throw new LabelIndexExistsException(String.format(
						"key was there already! : %s", name));
			}

			TitanType type = graph.getType(sortKeyName);
			if (false == type.isPropertyKey()) {
				throw new SortKeyNotExistsException(
						String.format(
								"%s not key index!Please make sure sort key is created!",
								sortKeyName));
			}
			LabelMaker maker = graph.makeLabel(name);

			if (sortKeyName != null) {
				maker = maker.sortKey(type);
				switch (sortOrder) {
				case NewsfeedConstant.ORDER_DIRECTION_ASC:
					maker = maker.sortOrder(Order.ASC);
					break;
				case NewsfeedConstant.ORDER_DIRECTION_DESC:
					maker = maker.sortOrder(Order.DESC);
					break;
				default:
					throw new UnknownOrderDirectionException(String.format(
							"sortOrder: %s unknown!", sortOrder));
				}
			}

			maker.make();
		} catch (LabelIndexExistsException e) {
			throw e;
		} catch (SortKeyNotExistsException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			graph.commit();
		}

		return true;
	}

	public JSONObject filter(String community, FilterCriteria filterCriteria,
			boolean showTypes) throws KeyIndexTypeNotDefinedException,
			NoPathEdgeDefiendException, IllegalStartVertexDefinitionException,
			NoStartVertexFoundException {
		graph.rollback();

		logger.debug(String.format("\n\n\n"));
		logger.debug(String.format("=====Filter====="));

		JSONObject r = new JSONObject();
		try {
			boolean fromSingleVertex = false;
			if (false == this.hasIndex(AModel.IID)) {
				throw new KeyIndexTypeNotDefinedException();
			}

			GremlinPipeline<Object, Vertex> lgp = null;

			if (filterCriteria.getStart() != null) {
				NewsfeedStartCriteria start = filterCriteria.getStart();

				Vertex startVertex = getStartVertex(start);
				if (startVertex == null) {
					throw new NoStartVertexFoundException();
				}

				lgp = new GremlinPipeline<Object, Vertex>(graph).start(
						startVertex).cast(Vertex.class);
				fromSingleVertex = true;
			} else {
				lgp = new GremlinPipeline<Object, Object>(graph).V();
			}

			NewsfeedPathCriteria[] paths = filterCriteria.getPaths();

			for (int i = 0; i < paths.length; i++) {
				NewsfeedPathCriteria path = paths[i];
				// validate if edge defined for single vertex as start point
				// filter
				if (true == fromSingleVertex && null == path.getEdge()) {
					throw new NoPathEdgeDefiendException();
				}

				if (null != path.getEdge()) {
					lgp = direct(lgp, path.getEdge());
				}

				NewfeedPathPropertyCriteria[] properties = path.getProperties();

				if (null != properties) {
					for (int j = 0; j < properties.length; j++) {
						lgp = has(lgp, properties[j].getType(),
								properties[j].getValue());
					}
				}
			}

			if (filterCriteria.getOrder()) {
				lgp = order(lgp);
			}

			if (filterCriteria.getOffset() != NewsfeedConstant.FILTER_INVILIDATE_OFFSET) {
				lgp = lgp.range(filterCriteria.getOffset(),
						filterCriteria.getLimit());
			}
			List<Vertex> result = lgp.toList();

			r = TitanResult.transferVerticesToJSONObject(community,
					result.iterator(), showTypes);
		} catch (IllegalStartVertexDefinitionException e) {
			throw e;
		} catch (NoStartVertexFoundException e) {
			throw e;
		} catch (NoPathEdgeDefiendException e) {
			throw e;
		} catch (KeyIndexTypeNotDefinedException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			graph.commit();
		}

		logger.debug(String.format("=====F=====\n\n"));
		return r;
	}

	/**
	 * @param start
	 * @return
	 * @throws IllegalStartVertexDefinitionException
	 * @throws NoStartVertexFoundException
	 */
	private Vertex getStartVertex(NewsfeedStartCriteria start)
			throws IllegalStartVertexDefinitionException {
		if (start.getModel() == null || start.getId() < 1) {
			throw new IllegalStartVertexDefinitionException();
		}

		logger.debug(String.format("...looking for start vertex: %s",
				AModel.getFormatedIID(start.getModel(), start.getId())));
		Vertex startVertex = null;
		try {
			Iterator<Vertex> startVertexIterator = graph.getVertices(
					AModel.IID,
					AModel.getFormatedIID(start.getModel(), start.getId()))
					.iterator();
			if (startVertexIterator.hasNext()) {
				startVertex = startVertexIterator.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return startVertex;
	}

	protected GremlinPipeline<Object, Vertex> has(
			GremlinPipeline<Object, Vertex> gPipeline, short key,
			final NewsfeedPathPropertyKeyValueCriteria kv) {
		GremlinPipeline<Object, Vertex> result = null;
		Object[] geoAry;
		switch (key) {
		case NewsfeedConstant.PROPERTY_FILETER_HAS:
			result = gPipeline.has(kv.getKey(), kv.getValue()).cast(
					Vertex.class);
			break;
		case NewsfeedConstant.PROPERTY_FILETER_HAS_NOT:
			result = gPipeline.hasNot(kv.getKey(), kv.getValue()).cast(
					Vertex.class);
			break;
		case NewsfeedConstant.PROPERTY_FILETER_HAS_TAGS:
			result = gPipeline.filter(new PipeFunction<Vertex, Boolean>() {
				public Boolean compute(Vertex v) {
					int vertexTagsSum = v
							.getProperty(NewsfeedConstant.PROPERTY_TAGS);
					if (vertexTagsSum > 0) {
						int compareTagsSum = (int) kv.getValue();
						if ((vertexTagsSum & compareTagsSum) == compareTagsSum) {
							return true;
						}
					}
					return false;
				}
			});
			break;
		case NewsfeedConstant.PROPERTY_FILETER_EXCLUDE_TAGS:
			result = gPipeline.filter(new PipeFunction<Vertex, Boolean>() {
				public Boolean compute(Vertex v) {
					int vertexTagsSum = v
							.getProperty(NewsfeedConstant.PROPERTY_TAGS);
					if (vertexTagsSum > 0) {
						int compareTagsSum = (int) kv.getValue();
						if ((vertexTagsSum & compareTagsSum) > 0) {
							return false;
						}
					}
					return true;
				}
			});
			break;
		case NewsfeedConstant.PROPERTY_FILETER_GEO_INTERSECT:
		case NewsfeedConstant.PROPERTY_FILETER_GEO_DISJOINT:
		case NewsfeedConstant.PROPERTY_FILETER_GEO_WITHIN:
			ArrayList<Double> geoAryList = (ArrayList<Double>) kv.getValue();
			geoAry = geoAryList.toArray();
			result = geo(gPipeline, key, Double.valueOf(geoAry[0].toString()),
					Double.valueOf(geoAry[1].toString()),
					Double.valueOf(geoAry[2].toString())).cast(Vertex.class);
			break;
		}
		return result;
	}

	protected GremlinPipeline<Object, Vertex> direct(
			GremlinPipeline<Object, Vertex> gPipeline,
			NewfeedPathEdgeCriteria edge)
			throws InvilidatePathEdgeDefinitionException {
		short direction = edge.getDirection();
		if (NewsfeedConstant.PATH_INVILIDATE_DIRECTION == direction) {
			throw new InvilidatePathEdgeDefinitionException(
					"No path edge direction defined!");
		}
		String label = edge.getLabel();
		if (null == label) {
			throw new InvilidatePathEdgeDefinitionException(
					"No path edge label defined!");
		}
		GremlinPipeline<Object, Vertex> result = null;
		switch (direction) {
		case NewsfeedConstant.PATH_DIRECTION_OUT:
			result = gPipeline.out(label);
			break;
		case NewsfeedConstant.PATH_DIRECTION_IN:
			result = gPipeline.in(label);
			break;
		case NewsfeedConstant.PATH_DIRECTION_BOTH:
			result = gPipeline.both(label);
			break;
		default:
			throw new InvilidatePathEdgeDefinitionException(
					"Illegal path edge direction!");
		}
		return result;
	}

	protected GremlinPipeline<Object, ? extends Element> geo(
			GremlinPipeline<Object, Vertex> gPipeline, short relationship,
			double lat, double lon, double distance) {
		GremlinPipeline<Object, ? extends Element> result = null;
		switch (relationship) {
		case NewsfeedConstant.PROPERTY_FILETER_GEO_WITHIN:
			result = gPipeline.has(NewsfeedConstant.PROPERTY_GEO, Geo.WITHIN,
					Geoshape.circle(lat, lon, distance));
			break;
		case NewsfeedConstant.PROPERTY_FILETER_GEO_INTERSECT:
			result = gPipeline.has(NewsfeedConstant.PROPERTY_GEO,
					Geo.INTERSECT, Geoshape.circle(lat, lon, distance));
			break;
		case NewsfeedConstant.PROPERTY_FILETER_GEO_DISJOINT:
			result = gPipeline.has(NewsfeedConstant.PROPERTY_GEO, Geo.DISJOINT,
					Geoshape.circle(lat, lon, distance));
			break;
		default:
			;
		}
		return result;
	}

	/**
	 * Currently just support order by timestamp
	 * 
	 * @param gPipeline
	 * @param key
	 * @param direction
	 * @return GremlinPipeline<Object, Vertex>
	 */
	protected GremlinPipeline<Object, Vertex> order(
			GremlinPipeline<Object, Vertex> gPipeline) {
		return gPipeline
				.order(new PipeFunction<Pair<Vertex, Vertex>, Integer>() {
					@Override
					public Integer compute(Pair<Vertex, Vertex> argument) {
						Long iB = new Long(argument.getB()
								.getProperty(TitanModel.PROPERTY_CREATED_AT)
								.toString());
						Long iA = new Long(argument.getA()
								.getProperty(TitanModel.PROPERTY_CREATED_AT)
								.toString());
						return iB.compareTo(iA);
					}

				});
	}

	public boolean hasPropertyKey(String name) {
		boolean result = false;
		try {
			TitanType typeName = graph.getType(name);
			if (typeName != null && typeName.isPropertyKey()) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			graph.commit();
		}

		return result;
	}

	public boolean hasLabel(String name) {
		boolean result = false;
		try {
			TitanType typeName = graph.getType(name);
			if (typeName != null && typeName.isEdgeLabel()) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			graph.commit();
		}
		return result;
	}

	public boolean hasIndex(String name) {
		TitanType typeName = graph.getType(name);
		graph.commit();
		if (typeName != null) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thenetcircle.newsfeed.base.client.IClient#deleteVertex(java.lang.
	 * String, java.lang.String, int)
	 */
	@Override
	public boolean deleteVertex(String community, String model, int id)
			throws ModelIIDNotCreatedException, VertexNotExistsException {
		logger.debug(String.format("\n\n\n"));
		logger.debug(String.format("=====DELETE====="));

		TitanTransaction t = graph.newTransaction();
		try {
			if (this.hasPropertyKey(AModel.IID) == false) {
				logger.debug(String
						.format("no key index iid found inside graph"));
				throw new ModelIIDNotCreatedException();
			}

			Iterator<Vertex> vs = t.getVertices(AModel.IID,
					AModel.getFormatedIID(model, id)).iterator();
			if (false == vs.hasNext()) {
				logger.debug(String.format("Vertex(%s) not exists!",
						AModel.getFormatedIID(model, id)));
				throw new VertexNotExistsException();
			}
			final TitanVertex vertex = (TitanVertex) vs.next();
			logger.debug(String.format("delete vertex(ID:%s-IID:%s)",
					vertex.getID(), id));

			t.removeVertex(vertex);
			logger.debug(String.format(" waiting for commit..."));

			// new TransactionRetryHelper.Builder<Vertex>(graph)
			// .perform(new TransactionWork<Vertex>() {
			// @Override
			// public Vertex execute(final TransactionalGraph graph)
			// throws Exception {
			// logger.error(String.format("!!retry @%s!!",
			// DateTime.now().toString()));
			//
			// graph.removeVertex(vertex);
			// return null;
			// }
			// }).build().exponentialBackoff(COMMIT_RETRIES);
		} catch (ModelIIDNotCreatedException e1) {
			logger.debug(String
					.format(" ERROR:: ModelIIDNotCreatedException..."));
			throw e1;
		} catch (VertexNotExistsException e2) {
			logger.debug(String.format(" ERROR:: VertexNotExistsException..."));
			throw e2;
		} catch (Exception e) {
			logger.debug(String.format(" ERROR:: Exception..."));
			e.printStackTrace();
		} finally {
			logger.debug(String.format(" starting commit..."));
			t.commit();
			logger.debug(String.format(" finish commit..."));
		}
		logger.debug(String.format("======D====="));
		return true;
	}

	public JSONObject getVertex(final String community, final String model,
			int id, final boolean showTypes) {
		logger.debug(String.format("\n\n\n"));
		logger.debug(String.format("=====Get Vertex====="));
		JSONObject result = null;
		TitanTransaction transaction = graph.newTransaction();
		final GraphSONMode mode = showTypes ? GraphSONMode.EXTENDED
				: GraphSONMode.NORMAL;
		try {
			if (this.hasPropertyKey(AModel.IID) == false) {
				logger.debug(String
						.format("no key index iid found inside graph"));
				return result;
			}

			logger.debug(String.format("searching vertex: %d.", id));
			TitanVertex vertex = transaction.getVertex(AModel.IID,
					AModel.getFormatedIID(model, id));
			if (vertex != null) {
				ArrayList<String> legallFields = TitanService
						.getLegallFieldNamesByCommunityAndModel(community,
								model);
				result = GraphSONUtility.jsonFromElement(vertex,
						new HashSet<String>(legallFields), mode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			transaction.commit();
		}

		logger.debug(String.format("=====G V===="));
		return result;
	}

	public JSONObject updateVertex(final String community, final String model,
			int id, final boolean showTypes, JSONArray properties) {
		logger.debug(String.format("\n\n\n"));
		logger.debug(String.format("=====Update Vertex====="));
		JSONObject result = null;
		TitanTransaction transaction = graph.newTransaction();
		final GraphSONMode mode = showTypes ? GraphSONMode.EXTENDED
				: GraphSONMode.NORMAL;
		try {
			if (this.hasPropertyKey(AModel.IID) == false) {
				logger.debug(String
						.format("no key index iid found inside graph"));
				return result;
			}

			logger.debug(String.format("searching vertex: %d.", id));
			TitanVertex vertex = transaction.getVertex(AModel.IID,
					AModel.getFormatedIID(model, id));
			if (vertex != null) {
				for (int i = 0; i < properties.length(); i++) {
					JSONObject property = properties.getJSONObject(i);
					String type = property.getString("type");
					String key = property.getString("key");
					int action = NewsfeedConstant.VERTEX_SET_PROPERTY;
					try {
						action = property.getInt("action");
					} catch (JSONException e) {
						// We will use default value for action
					}
					if (NewsfeedConstant.VERTEX_SET_PROPERTY == action) {
						String value = "value";
						Object v = null;
						switch (type) {
						case "int":
							v = property.getInt(value);
							break;
						case "long":
							v = property.getLong(value);
							break;
						case "double":
							v = String
									.format("%.7f", property.getDouble(value));
							break;
						case "string":
							v = property.getString(value);
							break;
						case "boolean":
							v = property.getBoolean(value);
							break;
						case "geo":
							v = Geoshape.point(property.getDouble("lat"),
									property.getDouble("lon"));
							break;
						default:
							v = "";
						}
						vertex.setProperty(key, v);
					} else {
						vertex.removeProperty(key);
					}

				}
				ArrayList<String> legallFields = TitanService
						.getLegallFieldNamesByCommunityAndModel(community,
								model);
				result = GraphSONUtility.jsonFromElement(vertex,
						new HashSet<String>(legallFields), mode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			transaction.commit();
		}

		logger.debug(String.format("=====U V===="));
		return result;
	}

	public JSONObject getEdges(String community, String model, int vertexId) {
		logger.debug(String.format("\n\n\n"));
		logger.debug(String.format("=====Get Edge====="));

		JSONObject result = null;
		TitanTransaction transaction = graph.newTransaction();

		try {
			if (this.hasPropertyKey(AModel.IID) == false) {
				logger.debug(String
						.format("no key index iid found inside graph"));
				return result;
			}

			logger.debug(String.format("searching vertex edges: %d.", vertexId));
			result = TitanResult.getEdgesJSON(transaction.getVertex(AModel.IID,
					AModel.getFormatedIID(model, vertexId)));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			transaction.commit();
		}

		logger.debug(String.format("=====G E===="));
		return result;
	}

	public boolean deleteEdges(int vertexId, String vertexModel, int direction,
			String label) throws ModelIIDNotCreatedException,
			UnknownEdgeDirectionException, VertexNotExistsException {
		graph.rollback();

		logger.debug(String.format("\n\n\n"));
		logger.debug(String.format("=====delete Edges====="));

		TitanTransaction transaction = graph.newTransaction();

		try {
			if (this.hasPropertyKey(AModel.IID) == false) {
				throw new ModelIIDNotCreatedException();
			}

			logger.debug(String.format("searching vertex edges: %d.", vertexId));

			TitanVertex vertex = transaction.getVertex(AModel.IID,
					AModel.getFormatedIID(vertexModel, vertexId));
			if (null == vertex) {
				throw new VertexNotExistsException(String.format(
						"id: %d; model: %s", vertexId, vertexModel));
			}
			Iterator<Edge> edges = null;
			switch (direction) {
			case NewsfeedConstant.PATH_DIRECTION_IN:
				edges = vertex.getEdges(Direction.IN, label).iterator();
				break;
			case NewsfeedConstant.PATH_DIRECTION_OUT:
				edges = vertex.getEdges(Direction.OUT, label).iterator();
				break;
			case NewsfeedConstant.PATH_DIRECTION_BOTH:
				edges = vertex.getEdges(Direction.BOTH, label).iterator();
				break;
			default:
				throw new UnknownEdgeDirectionException(String.format("%s",
						direction));
			}

			while (edges.hasNext()) {
				Edge edge = edges.next();
				edge.remove();
			}

		} catch (ModelIIDNotCreatedException e1) {
			throw e1;
		} catch (VertexNotExistsException e2) {
			throw e2;
		} catch (UnknownEdgeDirectionException e3) {
			throw e3;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			transaction.commit();
		}

		logger.debug(String.format("=====D E===="));
		return true;
	}

	@Override
	public boolean initVertex(String community, String model, int start,
			int end, int batch) {
		logger.debug(String.format("\n\n\n"));
		logger.debug(String.format("=====init vertex====="));

		int batchSize = batch;
		BatchGraph bgraph = new BatchGraph(graph, VertexIDType.STRING,
				batchSize);
		bgraph.setVertexIdKey("iid");
		bgraph.setLoadingFromScratch(true);

		Connection conn = null;
		ArrayList<CommunityModelField> legalFields = TitanService
				.getLegallFieldsByCommunityAndModel(community, model);

		try {
			DatabaseConfigure dbc = TitanService
					.getDatabaseConfigureByCommunity(community);
			String userName = dbc.getUsername();
			String password = dbc.getPassword();
			String url = String.format("jdbc:mysql://%s/%s", dbc.getHost(),
					dbc.getDatabase());

			logger.debug(String
					.format("DB: username: %s; password: %s; url: %s; start: %d, end: %d; batch: %d",
							userName, password, url, start, end, batch));

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, userName, password);
			logger.debug(String.format("Database connection established"));

			Statement s = conn.createStatement();
			ResultSet resultSet = s.getResultSet();

			int limit = batchSize;
			int offset = start;

			while (offset < end) {
				if (offset + limit > end) {
					limit = end - offset;
				}
				s.executeQuery(TitanService.getInitSQLForCommunityModel(
						community, model)
						+ " limit "
						+ limit
						+ " offset "
						+ offset);
				resultSet = s.getResultSet();

				if (resultSet == null) {
					logger.debug(String.format("result set is null."));
					break;
				}
				while (resultSet.next()) {
					addVertex(bgraph, model, legalFields, resultSet);
				}

				offset += limit;
				logger.debug(String.format("new offset %d", offset));
			}

			resultSet.close();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					bgraph.commit();
					conn.close();
					System.out.println("Database connection terminated");
				} catch (Exception e) { /* ignore close errors */
				}
			}
		}

		logger.debug(String.format("=====I V===="));
		return true;
	}

	/**
	 * @param community
	 * @param model
	 * @param edge
	 * @param start
	 * @param end
	 * @param batch
	 * @return boolean
	 */
	public boolean initEdge(String community, String edgeLabel, int start,
			int end, int batch) {

		logger.debug(String.format("\n\n\n"));
		logger.debug(String.format("=====init edge====="));

		int batchSize = batch;
		BatchGraph bgraph = new BatchGraph(graph, VertexIDType.STRING,
				batchSize);
		bgraph.setVertexIdKey("iid");
		bgraph.setLoadingFromScratch(false);

		Connection conn = null;
		NewsfeedEdge edge = TitanService.getEdgeFromCommunityModel(community,
				edgeLabel);
		NewsfeedVertex from = edge.getFrom();
		NewsfeedVertex to = edge.getTo();
		String fromId = edge.getFromId();
		String toId = edge.getToId();
		int direction = edge.getDirection();
		ArrayList<CommunityModelField> fromLegalFields = TitanService
				.getLegallFieldsByCommunityAndModel(community, from.getModel());
		ArrayList<CommunityModelField> toLegalFields = TitanService
				.getLegallFieldsByCommunityAndModel(community, to.getModel());

		try {
			DatabaseConfigure dbc = TitanService
					.getDatabaseConfigureByCommunity(community);
			String userName = dbc.getUsername();
			String password = dbc.getPassword();
			String url = String.format("jdbc:mysql://%s/%s", dbc.getHost(),
					dbc.getDatabase());

			logger.debug(String
					.format("DB: username: %s; password: %s; url: %s; start: %d, end: %d; batch: %d",
							userName, password, url, start, end, batch));

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, userName, password);
			logger.debug(String.format("Database connection established"));

			Statement s = conn.createStatement();
			ResultSet resultSet = s.getResultSet();

			int limit = batchSize;
			int offset = start;

			while (offset < end) {
				if (offset + limit > end) {
					limit = end - offset;
				}
				s.executeQuery(TitanService.getFromVertexSQLForCommunityModel(
						community, edgeLabel)
						+ " limit "
						+ limit
						+ " offset "
						+ offset);
				resultSet = s.getResultSet();

				if (resultSet == null) {
					logger.debug(String.format("result set is null."));
					break;
				}
				while (resultSet.next()) {
					addEdge(conn, bgraph, resultSet, edgeLabel, from, to,
							fromId, toId, direction, fromLegalFields,
							toLegalFields);
				}

				offset += limit;
				logger.debug(String.format("new offset %d", offset));
			}

			resultSet.close();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					bgraph.commit();
					conn.close();
					System.out.println("Database connection terminated");
				} catch (Exception e) { /* ignore close errors */
				}
			}
		}

		logger.debug(String.format("=====I E===="));
		return true;
	}

	private Vertex addVertex(BatchGraph bgraph, String model,
			ArrayList<CommunityModelField> fieldList, ResultSet resultSet)
			throws SQLException {
		int id = resultSet.getInt("id");
		String storedId = AModel.getFormatedIID(model, id);
		// Vertex vertex = bgraph.getVertex(storedId);
		// if (null == vertex) {
		// vertex = bgraph.addVertex(storedId);
		// }
		Vertex vertex = bgraph.addVertex(storedId);
		logger.debug(String.format("addVertex: %s", storedId));
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put(TitanModel.PROPERTY_CREATED_AT, DateTime.now().getMillis());
		map.put(TitanModel.PROPERTY_MODEL, model);

		for (int i = 0; i < fieldList.size(); i++) {
			CommunityModelField field = fieldList.get(i);
			String fieldName = field.getName();
			if (fieldName.equals(AModel.COMMUNITY_ID)) {
				map.put(AModel.IID, storedId);
			} else {
				String type = field.getType();
				Object v = null;
				switch (type) {
				case "int":
					v = resultSet.getInt(fieldName);
					break;
				case "long":
					v = resultSet.getLong(fieldName);
					break;
				case "double":
					v = String.format("%.7f", resultSet.getDouble(fieldName));
					break;
				case "string":
					v = resultSet.getString(fieldName);
					break;
				case "boolean":
					v = resultSet.getBoolean(fieldName);
					break;
				case "geo":
					v = Geoshape.point(resultSet.getDouble("lat"),
							resultSet.getDouble("lon"));
					break;
				default:
					v = "";
				}
				if (false == resultSet.wasNull()) {
					map.put(fieldName, v);
				}
			}
		}

		ElementHelper.setProperties(vertex, map);
		return vertex;
	}

	private Edge addEdge(Connection conn, BatchGraph bgraph,
			ResultSet resultSet, String edgeLabel, NewsfeedVertex from,
			NewsfeedVertex to, String fromId, String toId, int direction,
			ArrayList<CommunityModelField> fromLegalFields,
			ArrayList<CommunityModelField> toLegalFields) throws Exception {

		int fromIdValue = resultSet.getInt(fromId);
		int toIdValue = resultSet.getInt(toId);

		String graphFromId = AModel
				.getFormatedIID(from.getModel(), fromIdValue);
		Vertex fromVertex = bgraph.getVertex(graphFromId);
		if (null == fromVertex) {
			fromVertex = retrieveFromDBAndCreateInGraphDB(conn, bgraph, fromLegalFields,
					from.getModel(), from.getSql(), fromIdValue);
		}
		String graphToId = AModel.getFormatedIID(to.getModel(), toIdValue);
		Vertex toVertex = bgraph.getVertex(graphToId);
		if (null == toVertex) {
			toVertex = retrieveFromDBAndCreateInGraphDB(conn, bgraph, toLegalFields,
					to.getModel(), to.getSql(), toIdValue);
		}
		
		if(null == fromVertex) {
			throw new Exception(String.format("from(%s-%d) or to(%s-%d) vertex are null!", from.getModel(), fromIdValue,
					to.getModel(), toIdValue));
		}

		Edge graphEdge = null;
		
		if(null ==  toVertex) {
			return null;
		}

		switch (direction) {
		case NewsfeedConstant.PATH_DIRECTION_IN:
			graphEdge = bgraph.addEdge(null, fromVertex, toVertex, edgeLabel);
			break;
		case NewsfeedConstant.PATH_DIRECTION_OUT:
			graphEdge = bgraph.addEdge(null, toVertex, fromVertex, edgeLabel);
			break;
		case NewsfeedConstant.PATH_DIRECTION_BOTH:
			graphEdge = bgraph.addEdge(null, fromVertex, toVertex, edgeLabel);
			graphEdge = bgraph.addEdge(null, toVertex, fromVertex, edgeLabel);
			break;
		default:
		}
		return graphEdge;
	}

	/**
	 * @param sql
	 * @param fromIdValue
	 * @return
	 */
	private Vertex retrieveFromDBAndCreateInGraphDB(Connection conn,
			BatchGraph bgraph, ArrayList<CommunityModelField> legalFields,
			String model, String sql, int idValue) {
		Statement s;
		Vertex vertex = null;
		try {
			s = conn.createStatement();

			ResultSet resultSet = s.getResultSet();
			s.executeQuery(sql + " where id = " + idValue);
			resultSet = s.getResultSet();

			if (resultSet == null) {
				logger.debug(String.format("result set is null."));
				return null;
			}
			try {
				vertex = addVertex(bgraph, model, legalFields, resultSet);
			} finally {
				bgraph.commit();
			}

			resultSet.close();
			s.close();
		} catch (SQLException e) {
			vertex = null;
		} finally {
			vertex = null;
		}
		return vertex;
	}

	/**
	 * @param community
	 * @return
	 */
	public boolean clearGraph(String community) {
		logger.debug(String.format("\n\n\n"));
		logger.debug(String.format("=====clear graph====="));
		StandardTitanGraph g = (StandardTitanGraph)graph;
		final GraphDatabaseConfiguration config = g.getConfiguration();
		graph.shutdown();
		TitanCleanup.clear(graph);
		graph = TitanFactory.open((Configuration)config);
		logger.debug(String.format("===== end clear graph====="));
		
		return true;
	}
}
