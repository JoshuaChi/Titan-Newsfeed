/**
 * 
 */
package com.thenetcircle.newsfeed.titan.model.pojo;

import java.util.ArrayList;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * @author jchi
 * 
 */
public class CommunityModelFields {	
	public ArrayList<String> getFieldNames() {
		ArrayList<String> list = new ArrayList<String>();
		for( int i = 0; i < fields.size(); i++){
			CommunityModelField field = fields.get(i);
			list.add(field.getName());
		}
		return list;
	}
	
	
	public void setFields(JSONArray fields) throws JSONException {
		ArrayList<CommunityModelField> list = new ArrayList<CommunityModelField>();
		for( int i = 0; i < fields.length(); i++){
			JSONObject fieldObj = fields.getJSONObject(i);
			CommunityModelField field = new CommunityModelField();
			field.setName(fieldObj.getString("name"));
			field.setType(fieldObj.getString("type"));
			list.add(field);
		}
		this.fields = list;
	}

	public ArrayList<CommunityModelField> getFields() {
		return fields;
	}

	private ArrayList<CommunityModelField> fields;
}
