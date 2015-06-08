package com.thenetcircle.newsfeed.titan.model.pojo.criteria;

public class NewsfeedPathCriteria {

	public NewfeedPathEdgeCriteria getEdge() {
		return edge;
	}

	public void setEdge(NewfeedPathEdgeCriteria edge) {
		this.edge = edge;
	}

	public NewfeedPathPropertyCriteria[] getProperties() {
		return properties;
	}

	public void setProperties(NewfeedPathPropertyCriteria[] properties) {
		this.properties = properties;
	}

	private NewfeedPathEdgeCriteria edge = null;
	private NewfeedPathPropertyCriteria[] properties = null;

}
