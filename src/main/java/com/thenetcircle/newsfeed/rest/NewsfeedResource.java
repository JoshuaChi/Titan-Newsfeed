package com.thenetcircle.newsfeed.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceContext;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.thenetcircle.newsfeed.base.client.AResult;
import com.thenetcircle.newsfeed.base.client.exceptions.BaseException;
import com.thenetcircle.newsfeed.titan.TitanService;
import com.thenetcircle.newsfeed.titan.model.TitanModel;
import com.thenetcircle.newsfeed.titan.model.pojo.criteria.FilterCriteria;

@Path("/newsfeed")
@Produces({ MediaType.APPLICATION_JSON })
public class NewsfeedResource {

	@Resource
	private WebServiceContext context;

	private static final String URI_FILTER = "/filter/{community}";

	private static final String URI_CREATE_VERTICES = "/create/vertices/{community}/{model}";
	private static final String URI_CREATE_VERTEX = "/create/vertex/{community}/{model}";
	private static final String URI_UPDATE_VERTEX = "/update/vertex/{community}/{model}/{id}";

	private static final String URI_CREATE_EDGES = "/create/edges/{community}";
	private static final String URI_CREATE_EDGE = "/create/edge/{community}";

	private static final String URI_CREATE_LABEL = "/create/label/{community}";
	private static final String URI_CREATE_LABELS = "/create/labels/{community}";

	private static final String URI_CREATE_KEYS = "/create/keys/{community}";
	private static final String URI_CREATE_KEY = "/create/key/{community}";

	private static final String URI_DELETE_VERTEX = "/delete/vertex/{community}/{model}/{id}";
	private static final String URI_DELETE_EDGE = "/delete/edge/{community}";
	private static final String URI_DELETE_EDGES = "/delete/edges/{community}";

	private static final String URI_GET_VERTEX = "/get/vertex/{community}/{model}/{id}";
	private static final String URI_GET_EDGES = "/get/edges/{community}/{model}/{vertex_id}";

	private static final String URI_HAS_PROPERTY_KEY = "/has/property/key/{community}/{name}";
	private static final String URI_HAS_LABEL = "/has/label/{community}/{name}";

	private static final String URI_VERTEX_INIT = "/init/vertex/{community}/{model}/{start}/{end}/{batch}/{hashcode}";

	private static final String URI_EDGE_INIT = "/init/edge/{community}/{edge}/{start}/{end}/{batch}/{hashcode}";

	private static final String URI_CLEAR_GRAPH = "/clear/graph/{community}/{hashcode}";

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String healthyPlainTextCheck() {
		return "Newsfeed service is running";
	}

	@GET
	@Produces(MediaType.TEXT_XML)
	public String healthyXMLCheck() {
		return "<?xml version=\"1.0\"?>" + "<running> Newsfeed service"
				+ "</hello>";
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String healthyHtmlCheck() {
		return "<html> " + "<title>" + "Newsfeed service" + "</title>"
				+ "<body><b>" + "Newsfeed service is running" + "</b></body>"
				+ "</html> ";
	}

	@Path(URI_CREATE_VERTEX)
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public int createVertex(@PathParam("community") final String community,
			@PathParam("model") final String model, final InputStream stream) {
		try {
			JSONObject fields = new JSONObject(transferInputStreamToJsonString(
					stream).toString());

			boolean result = TitanService.getClientByCommunity(community)
					.createVertex(community, model, fields);
			if (result) {
				return AResult.SUCCESS;

			} else {
				return AResult.FAILED;
			}
		} catch (BaseException e) {
			return e.getErrorCode();
		} catch (Exception e) {
			return AResult.GENERAL_ERROR_CODE;
		}
	}

	@Path(URI_UPDATE_VERTEX)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateVertex(@PathParam("community") final String community,
			@PathParam("model") final String model,
			@PathParam("id") final int id, final InputStream stream) {

		JSONObject result = null;
		try {
			JSONArray fields = new JSONArray(transferInputStreamToJsonString(
					stream).toString());

			result = TitanService.getClientByCommunity(community).updateVertex(
					community, model, id, false, fields);

		} catch (Exception e) {
			e.printStackTrace();
			return String.valueOf(AResult.FAILED);
		}

		if (null != result) {
			return result.toString();
		} else {
			return String.valueOf(AResult.FAILED);
		}
	}

	@Path(URI_CREATE_VERTICES)
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createVertices(
			@PathParam("community") final String community,
			@PathParam("model") final String model, final InputStream stream) {
		JSONArray jsonResult = new JSONArray();
		try {
			JSONArray array = new JSONArray(transferInputStreamToJsonString(
					stream).toString());
			for (int i = 0; i < array.length(); i++) {

				JSONObject fields = array.getJSONObject(i);
				try {
					boolean result = TitanService.getClientByCommunity(
							community).createVertex(community, model, fields);
					if (result) {
						jsonResult.put(AResult.SUCCESS);
					} else {
						jsonResult.put(AResult.FAILED);
					}
				} catch (BaseException e) {
					jsonResult.put(e.getErrorCode());
				} catch (Exception e) {
					e.printStackTrace();
					jsonResult.put(AResult.GENERAL_ERROR_CODE);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.ok(jsonResult.toString(), MediaType.APPLICATION_JSON)
				.build();
	}

	private StringBuilder transferInputStreamToJsonString(
			final InputStream stream) throws UnsupportedEncodingException,
			IOException {
		final StringBuilder jsonQueryString = new StringBuilder();
		int intchar;
		Reader reader = new InputStreamReader(stream, "UTF-8");

		while (-1 != (intchar = reader.read())) {
			jsonQueryString.append((char) intchar);
		}
		return jsonQueryString;
	}

	@Path(URI_CREATE_EDGES)
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createEdges(@PathParam("community") final String community,
			final InputStream stream) {

		String json = "[";
		try {
			JSONArray array = new JSONArray(transferInputStreamToJsonString(
					stream).toString());

			for (int i = 0; i < array.length(); i++) {
				String part = "{";
				JSONObject object = array.getJSONObject(i);
				JSONObject from = object.getJSONObject("from");
				JSONObject to = object.getJSONObject("to");
				String edgeType = object.getString("label");

				int response = AResult.GENERAL_ERROR_CODE;
				try {
					boolean result = TitanService.getClientByCommunity(
							community).createEdge(from.getInt("id"),
							from.getString("model"), to.getInt("id"),
							to.getString("model"), edgeType);

					if (result) {
						response = AResult.SUCCESS;
					} else {
						response = AResult.FAILED;
					}

				} catch (BaseException e) {
					response = e.getErrorCode();
				} catch (Exception e) {
					response = AResult.GENERAL_ERROR_CODE;
				}
				part += "from:" + from.get("id") + ", to:" + to.get("id")
						+ ", result: " + response;
				part += "}";
				json += part;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		json += "]";
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}

	@Path(URI_CREATE_EDGE)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public int createEdge(@PathParam("community") final String community,
			final InputStream stream) {

		int response = AResult.GENERAL_ERROR_CODE;

		try {
			JSONObject object = new JSONObject(transferInputStreamToJsonString(
					stream).toString());
			JSONObject from = object.getJSONObject("from");
			JSONObject to = object.getJSONObject("to");
			String edgeType = object.getString("label");

			boolean result = TitanService.getClientByCommunity(community)
					.createEdge(from.getInt("id"), from.getString("model"),
							to.getInt("id"), to.getString("model"), edgeType);

			if (result) {
				response = AResult.SUCCESS;
			} else {
				response = AResult.FAILED;
			}
		} catch (BaseException e) {
			response = e.getErrorCode();
		} catch (Exception e) {
			response = AResult.GENERAL_ERROR_CODE;
		}
		return response;
	}

	@Path(URI_CREATE_KEYS)
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createKeys(@PathParam("community") final String community,
			final InputStream stream) {
		String json = "[";
		try {
			JSONArray array = new JSONArray(transferInputStreamToJsonString(
					stream).toString());

			for (int i = 0; i < array.length(); i++) {
				String part = "{";
				JSONObject properties = array.getJSONObject(i);
				String key = properties.getString("key");
				int response = AResult.GENERAL_ERROR_CODE;
				try {
					boolean result = TitanService.getClientByCommunity(
							community).createKey(key,
							properties.getString("type"),
							properties.getJSONArray("index"));
					if (result) {
						response = AResult.SUCCESS;
					} else {
						response = AResult.FAILED;
					}
				} catch (BaseException e) {
					response = e.getErrorCode();
				} catch (Exception e) {
					response = AResult.GENERAL_ERROR_CODE;
				}
				part += "key:" + key + ", result: " + response;
				part += "}";
				json += part;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		json += "]";
		return Response.ok(json, MediaType.APPLICATION_JSON).build();

	}

	@Path(URI_CREATE_KEY)
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public int createKey(@PathParam("community") final String community,
			final InputStream stream) {
		int response = AResult.GENERAL_ERROR_CODE;
		try {
			JSONObject properties = new JSONObject(
					transferInputStreamToJsonString(stream).toString());
			String key = properties.getString("key");

			boolean result = TitanService.getClientByCommunity(community)
					.createKey(key, properties.getString("type"),
							properties.getJSONArray("index"));
			if (result) {
				response = AResult.SUCCESS;
			} else {
				response = AResult.FAILED;
			}

		} catch (BaseException e) {
			response = e.getErrorCode();
		} catch (Exception e) {
			response = AResult.GENERAL_ERROR_CODE;
		}
		return response;
	}

	@Path(URI_CREATE_LABEL)
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public int createLabel(@PathParam("community") final String community,
			final InputStream stream) {
		int response = AResult.GENERAL_ERROR_CODE;
		try {
			JSONObject properties = new JSONObject(
					transferInputStreamToJsonString(stream).toString());
			System.out.println(properties);
			boolean result = TitanService.getClientByCommunity(community)
					.createLabel(properties.getString("name"),
							TitanModel.LABEL_DEFAULT_SORT_BY_KEY,
							TitanModel.LABEL_DEFAULT_SORT_ORDER);
			if (result) {
				response = AResult.SUCCESS;
			} else {
				response = AResult.FAILED;
			}
		} catch (BaseException e) {
			response = e.getErrorCode();
		} catch (Exception e) {
			response = AResult.GENERAL_ERROR_CODE;
		}

		return response;
	}

	@Path(URI_CREATE_LABELS)
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createLabels(
			@PathParam("community") final String community,
			final InputStream stream) {
		String json = "[";
		try {
			JSONArray array = new JSONArray(transferInputStreamToJsonString(
					stream).toString());

			for (int i = 0; i < array.length(); i++) {
				String part = "{";
				JSONObject properties = array.getJSONObject(i);
				String name = properties.getString("name");
				int response = AResult.GENERAL_ERROR_CODE;
				try {
					boolean result = TitanService.getClientByCommunity(
							community).createLabel(
							properties.getString("name"),
							TitanModel.LABEL_DEFAULT_SORT_BY_KEY,
							TitanModel.LABEL_DEFAULT_SORT_ORDER);
					if (result) {
						response = AResult.SUCCESS;
					} else {
						response = AResult.FAILED;
					}
				} catch (BaseException e) {
					response = e.getErrorCode();
				} catch (Exception e) {
					response = AResult.GENERAL_ERROR_CODE;
				}
				part += "name:" + name + ", result: " + response;
				part += "}";
				json += part;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		json += "]";
		return Response.ok(json, MediaType.APPLICATION_JSON).build();

	}

	@Path(URI_FILTER)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String filter(@PathParam("community") final String community,
			FilterCriteria filterCriteria) {
		JSONObject result = new JSONObject();

		String errorCode = "error";
		try {
			result = TitanService.getClientByCommunity(community).filter(
					community, filterCriteria, false);
		} catch (BaseException e) {
			try {
				result.put(errorCode, e.getErrorCode());
			} catch (JSONException je) {
				je.printStackTrace();
			}
		} catch (Exception e) {
			try {
				result.put(errorCode, AResult.GENERAL_ERROR_CODE);
			} catch (JSONException je) {
				je.printStackTrace();
			}
		}
		return result.toString();
	}

	@Path(URI_DELETE_VERTEX)
	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	public int deleteVertex(@PathParam("community") final String community,
			@PathParam("model") final String model,
			@PathParam("id") final int id) {
		int response = AResult.GENERAL_ERROR_CODE;
		try {
			boolean result = TitanService.getClientByCommunity(community)
					.deleteVertex(community, model, id);
			if (result) {
				response = AResult.SUCCESS;

			} else {
				response = AResult.FAILED;
			}
		} catch (BaseException e) {
			response = e.getErrorCode();
		} catch (Exception e) {
			response = AResult.GENERAL_ERROR_CODE;
		}
		return response;
	}

	@Path(URI_GET_VERTEX)
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public String getVertex(@PathParam("community") final String community,
			@PathParam("model") final String model,
			@PathParam("id") final int id) {
		JSONObject jsonObject = TitanService.getClientByCommunity(community)
				.getVertex(community, model, id, false);
		if (null != jsonObject) {
			return jsonObject.toString();
		} else {
			return "";
		}
	}

	@Path(URI_GET_EDGES)
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public String getEdges(@PathParam("community") final String community,
			@PathParam("model") final String model,
			@PathParam("vertex_id") final int vertexId) {
		JSONObject jsonObject = TitanService.getClientByCommunity(community)
				.getEdges(community, model, vertexId);
		if (null != jsonObject) {
			return jsonObject.toString();
		} else {
			return "";
		}
	}

	@Path(URI_HAS_PROPERTY_KEY)
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public int hasPropertyKey(@PathParam("community") final String community,
			@PathParam("name") final String name) {
		boolean result = TitanService.getClientByCommunity(community)
				.hasPropertyKey(name);
		if (result) {
			return AResult.SUCCESS;

		} else {
			return AResult.FAILED;
		}
	}

	@Path(URI_HAS_LABEL)
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public int hasLabel(@PathParam("community") final String community,
			@PathParam("name") final String name) {
		boolean result = TitanService.getClientByCommunity(community).hasLabel(
				name);
		if (result) {
			return AResult.SUCCESS;

		} else {
			return AResult.FAILED;
		}
	}

	@Path(URI_DELETE_EDGES)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteEdges(@PathParam("community") final String community,
			final InputStream stream) {

		JSONArray jsonAry = new JSONArray();
		try {
			JSONArray array = new JSONArray(transferInputStreamToJsonString(
					stream).toString());

			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObj = new JSONObject();
				JSONObject object = array.getJSONObject(i);
				JSONObject vertex = object.getJSONObject("vertex");
				int direction = object.getInt("direction");
				String label = object.getString("label");

				int response = AResult.GENERAL_ERROR_CODE;
				try {
					boolean result = TitanService.getClientByCommunity(
							community).deleteEdges(vertex.getInt("id"),
							vertex.getString("model"), direction, label);

					if (result) {
						response = AResult.SUCCESS;
					} else {
						response = AResult.FAILED;
					}

				} catch (BaseException e) {
					response = e.getErrorCode();
				} catch (Exception e) {
					response = AResult.GENERAL_ERROR_CODE;
					e.printStackTrace();
				}
				jsonObj.put("id", vertex.getInt("id"));
				jsonObj.put("model", vertex.getString("model"));
				jsonObj.put("direction", direction);
				jsonObj.put("label", label);
				jsonObj.put("result", response);
				jsonAry.put(jsonObj);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jsonAry.toString(), MediaType.APPLICATION_JSON)
				.build();
	}

	@Path(URI_DELETE_EDGE)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public int deleteEdge(@PathParam("community") final String community,
			final InputStream stream) {
		int response = AResult.GENERAL_ERROR_CODE;
		try {
			JSONObject object = new JSONObject(transferInputStreamToJsonString(
					stream).toString());
			JSONObject vertex = object.getJSONObject("vertex");
			int direction = object.getInt("direction");
			String label = object.getString("label");

			boolean result = TitanService.getClientByCommunity(community)
					.deleteEdges(vertex.getInt("id"),
							vertex.getString("model"), direction, label);

			if (result) {
				response = AResult.SUCCESS;
			} else {
				response = AResult.FAILED;
			}

		} catch (BaseException e) {
			response = e.getErrorCode();
		} catch (Exception e) {
			response = AResult.GENERAL_ERROR_CODE;
		}
		return response;
	}

	@Path(URI_VERTEX_INIT)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public int initVertex(@PathParam("community") final String community,
			@PathParam("model") final String model,
			@PathParam("start") final int start,
			@PathParam("end") final int end,
			@PathParam("batch") final int batch,
			@PathParam("hashcode") final String hashcode) {
		System.out.println("hashcode: "
				+ TitanService.getHashcodeByCommunity(community));
		if (!TitanService.getHashcodeByCommunity(community).equals(hashcode)) {
			return AResult.FAILED;
		}

		if (start < 0 || end < 0 || start > end || batch < 0) {
			return AResult.FAILED;
		}

		boolean result = TitanService.getClientByCommunity(community).initVertex(
				community, model, start, end, batch);
		if (result) {
			return AResult.SUCCESS;

		} else {
			return AResult.FAILED;
		}
	}

	@Path(URI_EDGE_INIT)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public int initEdge(@PathParam("community") final String community,
			@PathParam("edge") final String edge,
			@PathParam("start") final int start,
			@PathParam("end") final int end,
			@PathParam("batch") final int batch,
			@PathParam("hashcode") final String hashcode) {
		System.out.println("hashcode: "
				+ TitanService.getHashcodeByCommunity(community));
		if (!TitanService.getHashcodeByCommunity(community).equals(hashcode)) {
			return AResult.FAILED;
		}

		if (start < 0 || end < 0 || start > end || batch < 0) {
			return AResult.FAILED;
		}

		boolean result = TitanService.getClientByCommunity(community).initEdge(
				community, edge, start, end, batch);
		if (result) {
			return AResult.SUCCESS;

		} else {
			return AResult.FAILED;
		}
	}
	

	@Path(URI_CLEAR_GRAPH)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public int clearGraph(@PathParam("community") final String community,
			@PathParam("hashcode") final String hashcode) {
		System.out.println("hashcode: "
				+ TitanService.getHashcodeByCommunity(community));
		if (!TitanService.getHashcodeByCommunity(community).equals(hashcode)) {
			return AResult.FAILED;
		}

		boolean result = TitanService.getClientByCommunity(community).clearGraph(
				community);
		if (result) {
			return AResult.SUCCESS;

		} else {
			return AResult.FAILED;
		}
	}
}
