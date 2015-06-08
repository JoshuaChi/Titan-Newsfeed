package com.thenetcircle.newsfeed.titan.model;

public class NewsfeedConstant {
	public static final int VERTEX_PATH = 1;// "vertex";
	public static final int EDGE_PATH = 2;// "edge";
	public static final int INDEX_PATH = 3;// "index";


	public static final short FILTER_DEFAULT_LIMIT = 1;
	public static final short FILTER_INVILIDATE_OFFSET = -1;
	public static final short PATH_INVILIDATE_DIRECTION = -1;
	public static final short PATH_DIRECTION_OUT = 1;
	public static final short PATH_DIRECTION_IN = 2;
	public static final short PATH_DIRECTION_BOTH = 3;

	public static final short ORDER_DIRECTION_ASC = 1;
	public static final short ORDER_DIRECTION_DESC = 2;

	public static final short PROPERTY_FILETER_HAS = 1;
	public static final short PROPERTY_FILETER_HAS_NOT = 2;
	public static final short PROPERTY_FILETER_HAS_TAGS = 4;
	public static final short PROPERTY_FILETER_EXCLUDE_TAGS = 8;
	public static final short PROPERTY_FILETER_GEO_INTERSECT = 16;
	public static final short PROPERTY_FILETER_GEO_DISJOINT = 32;
	public static final short PROPERTY_FILETER_GEO_WITHIN = 64;
	

	public static final String PROPERTY_TAGS = "tags";
	public static final String PROPERTY_GEO = "geo";
	

	public static final int VERTEX_SET_PROPERTY = 1;
	public static final int VERTEX_DELETE_PROPERTY = -1;

}
