package com.thenetcircle.newsfeed.test;




/**
 * social graph operations
 * 
 */
public interface NewsfeedOperations {

	/**
	 * create a user
	 * 
	 * @param id
	 *            unique identifier for the new user
	 * @param name
	 *            user name
	 * @return true - user created successfully<br>
	 *         false - user identifier in use
	 */
	boolean createUser(String id, String name, int gender, int age,
			int membership, long lastLogin, String country, float lat, float lon);

	
	/**
	 * user be a friend of another user
	 * 
	 * @param userId
	 *            identifier of the user
	 * @param targetId
	 *            identifier of the target user
	 * @return 0 - user is now following<br>
	 *         error code - if the registration failed
	 */
	int beFriend(String userId, String targetId);
	
	int uploadAvatar(String avatarId, long timestamp, String ownerId, int[] tags);
	
	int uploadPhoto(String photoId, long timestamp, String ownerId, int[] tags);

	int createEvent(String eventId, String subject, String content, long timestamp, String authorId, int[] tags);

	int createBlog(String blogId, String subject, String content, long timestamp, String authorId, int[] tags);

	int commentBlog(String blogId, String commentId, String comment, long timestamp, String userId, int[] tags);
	
	int upgradeMembership(String userId, String membershipType, long timestamp, int[] tags);
}
