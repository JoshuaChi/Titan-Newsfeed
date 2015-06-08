package com.thenetcircle.newsfeed.test.impl;

import java.util.BitSet;

import com.thenetcircle.newsfeed.test.EdgeType;
import com.thenetcircle.newsfeed.test.NewsfeedOperations;
import com.thenetcircle.newsfeed.titan.model.Property;
import com.thenetcircle.newsfeed.titan.model.Property.Activity;
import com.thinkaurelius.titan.core.Order;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanKey;
import com.thinkaurelius.titan.core.TitanLabel;
import com.thinkaurelius.titan.core.TitanTransaction;
import com.thinkaurelius.titan.core.TitanType;
import com.thinkaurelius.titan.core.TitanVertex;
import com.thinkaurelius.titan.core.attribute.Geoshape;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * write optimized Graphity implementation for Titan
 * 
 */
public class NewsfeedOperationImpl implements NewsfeedOperations {

	private TitanGraph graph = null;

	public TitanGraph getGraph() {
		return graph;
	}
	/**
	 * create a write optimized Graphity instance for Titan
	 * 
	 * @param titanGraph
	 *            Titan graph to operate on
	 */
	public NewsfeedOperationImpl(TitanGraph titanGraph) {
		graph = titanGraph;

		TitanType indexExists = graph.getType(Property.User.ID);

		// indexes can only be created once.
		if (indexExists == null) {
			// user index
			graph.makeKey(Property.User.ID).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();
			graph.makeKey(Property.User.NAME).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class)
					.indexed("search", Edge.class).make();
			graph.makeKey(Property.User.GEO).dataType(Geoshape.class)
					.indexed("search", Edge.class).make();

			// blog index
			graph.makeKey(Property.Blog.ID).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();

			graph.makeKey(Property.BlogComment.ID).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();

			graph.makeKey(Property.Avatar.ID).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();

			graph.makeKey(Property.Photo.ID).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();
			graph.makeKey(Property.Event.ID).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();

			graph.makeKey(Property.Membership.TYPE).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();

			// edge sort index
			TitanKey time = graph.makeKey(Property.Time.TIMESTAMP)
					.dataType(Integer.class).make();
			graph.makeLabel(EdgeType.BEFRIEND).sortKey(time)
					.sortOrder(Order.DESC).make();
			graph.makeLabel(EdgeType.USER_ACTIVITY).sortKey(time)
					.sortOrder(Order.DESC).make();
			graph.makeLabel(EdgeType.BLOG_COMMENT).sortKey(time)
					.sortOrder(Order.DESC).make();
			graph.commit();
		}

	}

	public boolean createUser(String id, String name, int i, int j, int k,
			long lastLogin, String country, float lat, float lon) {

		if (this.isUserCreated(id)) {
			return true;
		}

		TitanTransaction tx = graph.newTransaction();
		TitanVertex user = tx.addVertex();

		user.setProperty(Property.User.ID, id);
		user.setProperty(Property.User.NAME, name);
		user.setProperty(Property.User.GENDER, i);
		user.setProperty(Property.User.AGE, j);
		user.setProperty(Property.User.MEMBERSHIP, k);
		user.setProperty(Property.User.LASTLOGIN, lastLogin);
		user.setProperty(Property.User.COUNTRY, country);
		user.setProperty(Property.User.LAT, String.format("%.7f", lat));
		user.setProperty(Property.User.LON, String.format("%.7f", lon));
		user.setProperty(Property.User.GEO, Geoshape.point(lat, lon));

		tx.commit();
		return true;
	}

	@Override
	public int beFriend(String userId, String targetId) {
		TitanTransaction tx = graph.newTransaction();
		TitanVertex vUser = this.getUserById(tx, userId);
		TitanVertex vTarget = this.getUserById(tx, targetId);

		if (vUser == null) {
			// TODO: follower missing
			return 0;
		}
		if (vTarget == null) {
			// TODO: target missing
			return -1;
		}

		if (this.isFriend(vUser, vTarget)) {
			// TODO: already friend with each other
			return 0;
		}

		vUser.addEdge(EdgeType.BEFRIEND, vTarget);
		tx.commit();
		return 0;
	}

	@Override
	public int uploadAvatar(String avatarId, long timestamp, String ownerId,
			int[] tags) {
		if (this.isUploadedAvatar(avatarId)) {
			return 0;
		}

		TitanTransaction tx = graph.newTransaction();
		TitanVertex vAvatar = tx.addVertex();
		TitanVertex vOwner = this.getUserById(tx, ownerId);

		if (vOwner == null) {
			// TODO: owner missing
			return -1;
		}
		vAvatar.setProperty(Activity.TYPE, Activity.TYPE_UPLOAD_AVATAR);
		vAvatar.setProperty(Property.Activity.TAG, this.getBitset(tags).toByteArray());
		vAvatar.setProperty(Property.Avatar.ID, avatarId);
		vAvatar.setProperty(Property.Time.TIMESTAMP, timestamp);
		TitanLabel label = (TitanLabel) graph.getType(EdgeType.USER_ACTIVITY);

		vOwner.addEdge(label, vAvatar);
		tx.commit();
		return 0;
	}

	@Override
	public int uploadPhoto(String photoId, long timestamp, String ownerId,
			int[] tags) {
		if (this.isUploadedPhoto(photoId)) {
			return 0;
		}

		TitanTransaction tx = graph.newTransaction();
		TitanVertex vPhoto = tx.addVertex();
		TitanVertex vOwner = this.getUserById(tx, ownerId);

		if (vOwner == null) {
			// TODO: owner missing
			return -1;
		}
		vPhoto.setProperty(Activity.TYPE, Activity.TYPE_UPLOAD_PHOTO);
		vPhoto.setProperty(Property.Activity.TAG, this.getBitset(tags).toByteArray());
		vPhoto.setProperty(Property.Photo.ID, photoId);
		vPhoto.setProperty(Property.Time.TIMESTAMP, timestamp);

		TitanLabel label = (TitanLabel) graph.getType(EdgeType.USER_ACTIVITY);

		vOwner.addEdge(label, vPhoto);
		tx.commit();
		return 0;
	}

	@Override
	public int createEvent(String eventId, String subject, String content,
			long timestamp, String authorId, int[] tags) {
		if (this.isCreatedEvent(eventId)) {
			return 0;
		}
		TitanTransaction tx = graph.newTransaction();
		TitanVertex vEvent = tx.addVertex();
		TitanVertex vAuthor = this.getUserById(tx, authorId);

		if (vAuthor == null) {
			// TODO: author missing
			return -1;
		}
		vEvent.setProperty(Activity.TYPE, Activity.TYPE_EVENT);
		vEvent.setProperty(Property.Activity.TAG, this.getBitset(tags).toByteArray());
		vEvent.setProperty(Property.Event.ID, eventId);
		vEvent.setProperty(Property.Event.SUBJECT, subject);
		vEvent.setProperty(Property.Event.CONTENT, content);
		vEvent.setProperty(Property.Time.TIMESTAMP, timestamp);

		TitanLabel label = (TitanLabel) graph.getType(EdgeType.USER_ACTIVITY);

		vAuthor.addEdge(label, vEvent);
		tx.commit();
		return 0;
	}

	@Override
	public int createBlog(String blogId, String subject, String content,
			long timestamp, String authorId, int[] tags) {
		if (this.isCreatedBlog(blogId)) {
			return 0;
		}
		TitanTransaction tx = graph.newTransaction();
		TitanVertex vBlog = tx.addVertex();
		TitanVertex vAuthor = this.getUserById(tx, authorId);

		if (vAuthor == null) {
			// TODO: author missing
			return -1;
		}
		vBlog.setProperty(Activity.TYPE, Activity.TYPE_BLOG);
		vBlog.setProperty(Property.Activity.TAG, this.getBitset(tags).toByteArray());
		vBlog.setProperty(Property.Blog.ID, blogId);
		vBlog.setProperty(Property.Blog.SUBJECT, subject);
		vBlog.setProperty(Property.Blog.CONTENT, content);
		vBlog.setProperty(Property.Time.TIMESTAMP, timestamp);

		TitanLabel label = (TitanLabel) graph.getType(EdgeType.USER_ACTIVITY);

		vAuthor.addEdge(label, vBlog);
		tx.commit();
		return 0;
	}

	@Override
	public int commentBlog(String blogId, String commentId, String comment,
			long timestamp, String userId, int[] tags) {
		if (this.isCreatedComment(commentId)) {
			return 0;
		}
		TitanTransaction tx = graph.newTransaction();
		TitanVertex vComment = tx.addVertex();
		TitanVertex vBlog = this.getBlogById(tx, blogId);
		TitanVertex vAuthor = this.getUserById(tx, userId);

		if (vBlog == null) {
			// TODO: original blog missing
			return -1;
		}
		vComment.setProperty(Activity.TYPE, Activity.TYPE_BLOG_COMMENT);
		vComment.setProperty(Property.Activity.TAG, this.getBitset(tags).toByteArray());
		vComment.setProperty(Property.BlogComment.ID, commentId);
		vComment.setProperty(Property.BlogComment.COMMENT, comment);
		vComment.setProperty(Property.Time.TIMESTAMP, timestamp);

		TitanLabel label = (TitanLabel) graph.getType(EdgeType.BLOG_COMMENT);

		vBlog.addEdge(label, vComment);

		TitanLabel label2 = (TitanLabel) graph.getType(EdgeType.USER_ACTIVITY);

		vAuthor.addEdge(label2, vComment);
		tx.commit();
		return 0;
	}

	@Override
	public int upgradeMembership(String userId, String membershipType,
			long timestamp, int[] tags) {
		if (this.hasMembership(userId, membershipType)) {
			return 0;
		}
		TitanTransaction tx = graph.newTransaction();
		TitanVertex vMembership = null;
		try {
			vMembership = this.getMembershipByType(tx, membershipType);
		} catch (Exception exception) {
			vMembership = tx.addVertex();
			vMembership.setProperty(Activity.TYPE,
					Activity.TYPE_UPGRADE_MEMBERSHIP);
			vMembership.setProperty(Property.Activity.TAG, this.getBitset(tags).toByteArray());
			vMembership.setProperty(Property.Membership.TYPE, membershipType);
			vMembership.setProperty(Property.Time.TIMESTAMP, timestamp);
		}

		TitanVertex vUser = this.getUserById(tx, userId);

		if (vUser == null) {
			// TODO: original blog missing
			return -1;
		}

		vUser.addEdge(EdgeType.USER_ACTIVITY, vMembership);
		tx.commit();
		return 0;
	}

	/**
	 * get a single user by its identifier
	 * 
	 * @param id
	 *            identifier of the user wanted
	 * @return TitanVertex representing the user wanted<br>
	 *         null - if no user with such identifier
	 */
	protected TitanVertex getUserById(TitanTransaction tx, String id) {
		return tx.getVertices((TitanKey) graph.getType(Property.User.ID), id)
				.iterator().next();
	}

	protected TitanVertex getBlogById(TitanTransaction tx, String blogId) {
		return tx
				.getVertices((TitanKey) graph.getType(Property.Blog.ID), blogId)
				.iterator().next();
	}

	protected TitanVertex getMembershipByType(TitanTransaction tx,
			String membershipType) {
		return tx
				.getVertices(
						(TitanKey) graph.getType(Property.Membership.TYPE),
						membershipType).iterator().next();
	}

	protected boolean isUserCreated(String id) {
		TitanTransaction t = graph.buildTransaction().start();
		TitanVertex isCreated = t.getVertex(Property.User.ID, id);
		t.commit();
		if (isCreated != null) {
			return true;
		}
		return false;
	}

	protected boolean isUploadedAvatar(String avatarId) {
		TitanTransaction t = graph.buildTransaction().start();
		TitanVertex isCreated = t.getVertex(Property.Avatar.ID, avatarId);
		t.commit();
		if (isCreated != null) {
			return true;
		}
		return false;
	}

	protected boolean isUploadedPhoto(String photoId) {
		TitanTransaction t = graph.buildTransaction().start();
		TitanVertex isCreated = t.getVertex(Property.Photo.ID, photoId);
		t.commit();
		if (isCreated != null) {
			return true;
		}
		return false;
	}

	protected boolean isCreatedEvent(String eventId) {
		TitanTransaction t = graph.buildTransaction().start();
		TitanVertex isCreated = t.getVertex(Property.Event.ID, eventId);
		t.commit();
		if (isCreated != null) {
			return true;
		}
		return false;
	}

	protected boolean isCreatedBlog(String blogId) {
		TitanTransaction t = graph.buildTransaction().start();
		TitanVertex isCreated = t.getVertex(Property.Blog.ID, blogId);
		t.commit();
		if (isCreated != null) {
			return true;
		}
		return false;
	}

	protected boolean isCreatedComment(String commentId) {
		TitanTransaction t = graph.buildTransaction().start();
		TitanVertex isCreated = t.getVertex(Property.BlogComment.ID, commentId);
		t.commit();
		if (isCreated != null) {
			return true;
		}
		return false;
	}

	protected boolean hasMembership(String userId, String membershipType) {
		TitanTransaction t = graph.buildTransaction().start();
		TitanVertex isCreated = t.getVertex(Property.Membership.TYPE,
				membershipType);
		t.commit();
		if (isCreated != null) {
			return true;
		}
		return false;
	}

	protected boolean isFriend(Vertex user, Vertex target) {
		for (Vertex followed : user.getVertices(Direction.OUT,
				EdgeType.BEFRIEND)) {
			if (followed.equals(target)) {
				return true;
			}
		}
		return false;
	}

	private BitSet getBitset(int[] tags) {
		//set bitset
		BitSet bits = new BitSet();
		for (Integer i : tags) {
			bits.set(i);
		}
		return bits;
	}
}
