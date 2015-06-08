/**
 * 
 */
package com.thenetcircle.newsfeed.titan.model.init;

/**
 * @author jchi
 *
 */
public class NewsfeedVertex {
	private String model;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	private String sql;
}
