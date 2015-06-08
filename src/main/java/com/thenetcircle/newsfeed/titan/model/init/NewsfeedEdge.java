/**
 * 
 */
package com.thenetcircle.newsfeed.titan.model.init;


/**
 * @author jchi
 * 
 */
public class NewsfeedEdge {

	public void push(String fromModel, String fromSQL, String toModel, String toSQL,
			int direction, String fromId, String toId) {
		NewsfeedVertex fromTemp = new NewsfeedVertex();
		fromTemp.setModel(fromModel);
		fromTemp.setSql(fromSQL);

		NewsfeedVertex toTemp = new NewsfeedVertex();
		toTemp.setModel(toModel);
		toTemp.setSql(toSQL);
		this.from = fromTemp;
		this.to = toTemp;
		this.direction = direction;
		this.fromId = fromId;
		this.toId = toId;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public NewsfeedVertex getFrom() {
		return from;
	}

	public void setFrom(NewsfeedVertex from) {
		this.from = from;
	}

	public NewsfeedVertex getTo() {
		return to;
	}

	public void setTo(NewsfeedVertex to) {
		this.to = to;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	private NewsfeedVertex from;
	private NewsfeedVertex to;
	private int direction;
	private String fromId;
	private String toId;
}
