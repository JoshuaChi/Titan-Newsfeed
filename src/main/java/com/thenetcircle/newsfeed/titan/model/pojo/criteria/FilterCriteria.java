package com.thenetcircle.newsfeed.titan.model.pojo.criteria;

import com.thenetcircle.newsfeed.titan.model.NewsfeedConstant;


public class FilterCriteria {

	public String getOrderKey() {
		return orderKey;
	}

	public void setOrderKey(String orderKey) {
		this.orderKey = orderKey;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public NewsfeedPathCriteria[] getPaths() {
		return paths;
	}

	public void setPaths(NewsfeedPathCriteria[] paths) {
		this.paths = paths;
	}

	public boolean getOrder() {
		return order;
	}

	public void setOrder(boolean order) {
		this.order = order;
	}

	public NewsfeedStartCriteria getStart() {
		return start;
	}

	public void setStart(NewsfeedStartCriteria start) {
		this.start = start;
	}

	private NewsfeedStartCriteria start = null;
	private String orderKey = null;
	private String orderDirection = null;
	private int offset = NewsfeedConstant.FILTER_INVILIDATE_OFFSET;
	private int limit = NewsfeedConstant.FILTER_DEFAULT_LIMIT;
	private NewsfeedPathCriteria[] paths = new NewsfeedPathCriteria[]{};
	private boolean order = true;
}
