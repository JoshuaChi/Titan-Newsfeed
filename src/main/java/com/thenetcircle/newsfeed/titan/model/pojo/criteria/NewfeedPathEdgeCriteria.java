/**
 * 
 */
package com.thenetcircle.newsfeed.titan.model.pojo.criteria;

import com.thenetcircle.newsfeed.titan.model.NewsfeedConstant;

/**
 * @author jchi
 *
 */
public class NewfeedPathEdgeCriteria {

	public short getDirection() {
		return direction;
	}
	public void setDirection(short direction) {
		this.direction = direction;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	private short direction = NewsfeedConstant.PATH_INVILIDATE_DIRECTION;
	private String label = null;
}
