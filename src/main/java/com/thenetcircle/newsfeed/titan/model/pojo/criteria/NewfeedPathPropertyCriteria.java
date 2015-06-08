package com.thenetcircle.newsfeed.titan.model.pojo.criteria;

public class NewfeedPathPropertyCriteria {

	public short getType() {
		return type;
	}
	public void setType(short type) {
		this.type = type;
	}
	public NewsfeedPathPropertyKeyValueCriteria getValue() {
		return value;
	}
	public void setValue(NewsfeedPathPropertyKeyValueCriteria value) {
		this.value = value;
	}
	private short type;
	private NewsfeedPathPropertyKeyValueCriteria value;

}
