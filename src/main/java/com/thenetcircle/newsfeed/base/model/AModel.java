package com.thenetcircle.newsfeed.base.model;

public abstract class AModel {

	//reserved model field names
	public static final String IID = "iid";
	public static final String PROPERTY_CREATED_AT="created_at";
	public static final String PROPERTY_MODEL="model";
	
	public static final String COMMUNITY_ID = "id";
	public static final String IID_FORMAT = "%s-%d";
	
	public static String getFormatedIID(String modelName, int id){
		return String.format(IID_FORMAT, modelName, id);
	}
	
	public static String[] getReservedModelFields(){
		return new String[]{
				IID,
				PROPERTY_CREATED_AT,
				PROPERTY_MODEL
		};
	}
}
