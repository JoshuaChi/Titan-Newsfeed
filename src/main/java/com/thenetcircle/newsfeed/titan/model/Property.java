package com.thenetcircle.newsfeed.titan.model;

import java.util.BitSet;

/**
 * properties used in social graphs
 * 
 * 
 */
public class Property {

	/**
	 * properties used for user vertices
	 * 
	 * 
	 */
	public static class User {

		/**
		 * unique user identifier
		 */
		public static final String ID = "user_id";

		/**
		 * user name
		 */
		public static final String NAME = "user_name";

		/**
		 * user gender
		 */
		public static final String GENDER = "user_gender";

		/**
		 * user age
		 */
		public static final String AGE = "user_age";

		/**
		 * user membership
		 */
		public static final String MEMBERSHIP = "user_membership";

		/**
		 * user lastlogin
		 */
		public static final String LASTLOGIN = "user_lastlogin";

		public static final String COUNTRY = "country";

		public static final String LAT = "lat";

		public static final String LON = "lon";

		public static final String GEO = "geo";

	}

	public static class Time {
		public static final String TIMESTAMP = "timestamp";
	}

	public static class Activity {
		public static final String TYPE = "activity_type";
		public static final String TYPE_EVENT = "event";
		public static final String TYPE_BLOG = "blog";
		public static final String TYPE_BLOG_COMMENT = "blog_comment";
		public static final String TYPE_UPLOAD_AVATAR = "upload_avatar";
		public static final String TYPE_UPLOAD_PHOTO = "upload_photo";
		public static final String TYPE_UPGRADE_MEMBERSHIP = "upgrade_membership";

		public static final String TAG = "activity_tag";
		public static final int TAG_EVENT = 1;
		public static final int TAG_BLOG = 2;
		public static final int TAG_BLOG_COMMENT = 4;
		public static final int TAG_AVATAR = 8;
		public static final int TAG_PHOTO = 16;
		public static final int TAG_MEMBERSHIP = 32;
		public static final int TAG_10YEAR_CELEBRATION = 64;

		public static BitSet getTagByOffset(int offset) {
			BitSet bits = new BitSet();
			bits.set(offset);
			return bits;
		}
	}

	public static class Membership {
		public static final String TYPE = "membership_type";
	}

	public static class Avatar {
		public static final String ID = "avatar_id";
		// public static final String TIMESTAMP = "timestamp";
	}

	public static class Photo {
		public static final String ID = "photo_id";
		// public static final String TIMESTAMP = "timestamp";
	}

	public static class Event {
		public static final String ID = "event_id";
		public static final String SUBJECT = "event_subject";
		public static final String CONTENT = "event_content";
		// public static final String TIMESTAMP = "timestamp";
	}

	public static class Blog {
		public static final String ID = "blog_id";
		public static final String SUBJECT = "blog_subject";
		public static final String CONTENT = "blog_content";
		// public static final String TIMESTAMP = "timestamp";
	}

	public static class BlogComment {
		public static final String ID = "comment_id";
		public static final String BLOG_ID = "blog_id";
		public static final String COMMENT = "comment";
		// public static final String TIMESTAMP = "timestamp";
	}

}
