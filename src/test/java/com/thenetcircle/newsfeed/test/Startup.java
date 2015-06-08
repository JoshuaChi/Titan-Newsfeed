package com.thenetcircle.newsfeed.test;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import com.thenetcircle.newsfeed.test.impl.NewsfeedOperationImpl;
import com.thenetcircle.newsfeed.titan.model.Property;
import com.thenetcircle.newsfeed.titan.model.Property.Activity;
import com.thenetcircle.newsfeed.titan.model.Property.Time;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanMultiVertexQuery;
import com.thinkaurelius.titan.core.TitanVertex;
import com.thinkaurelius.titan.core.attribute.Geo;
import com.thinkaurelius.titan.core.attribute.Geoshape;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.structures.Pair;

public class Startup {
	public static void main(String[] args) {
		/**
		 * init graph
		 */
		Configuration conf = new BaseConfiguration();
		conf.setProperty("storage.backend", "cassandra");
		conf.setProperty("storage.keyspace", "titan142");
		conf.setProperty("storage.hostname", "127.0.0.1");
		conf.setProperty("storage.cassandra-config-dir",
				"config/cassandra.yaml");
		conf.setProperty("storage.index.search.backend", "elasticsearch");
		conf.setProperty("storage.index.search.directory", "/tmp/searchindex136");
		conf.setProperty("storage.index.search.client-only", "false");
		conf.setProperty("storage.index.search.local-mode", "true");

		TitanGraph g = TitanFactory.open(conf);
		/**
		 * Use it only when the key index were updated
		 */
		/**
		 * g.shutdown(); TitanCleanup.clear(g);
		 */
		NewsfeedOperationImpl graphity = new NewsfeedOperationImpl(g);

		/**
		 * prepare users
		 */

		if (!(graphity.createUser("user-1", "Client1", 1, 30, 0, 1392100133,
				"DE", 48.3972222f, 10.4388889f)
				&& graphity.createUser("user-2", "Client2", 1, 34, 0,
						1392101033, "DE", 51.2811111f, 9.6186111f)
				&& graphity.createUser("user-3", "Client3", 1, 31, 0,
						1392101133, "DE", 48.6666667f, 9.2166667f)
				&& graphity.createUser("user-4", "Escort1", 2, 30, 0,
						1392101133, "DE", 50.4666667f, 7.7333333f)
				&& graphity.createUser("user-5", "Escort2", 2, 78, 0,
						1392001133, "DE", 52.5166667f, 10.6333333f)
				&& graphity.createUser("user-6", "Escort3", 2, 30, 0,
						1392001133, "DE", 52.2666667f, 10.2f)
				&& graphity.createUser("user-7", "Company1", 3, 50, 0,
						1391101133, "DE", 47.5907676194931f, 8.12953948974609f)
				&& graphity.createUser("user-8", "Company2", 3, 40, 0,
						1391101133, "DE", 52.25f, 9.9666667f)
				&& graphity.createUser("user-9", "Company3", 3, 30, 0,
						1392100133, "DE", 49.9738889f, 9.1491667f)
				&& graphity.createUser("user-10", "ClientPaidMembership", 1,
						18, 1, 1382101133, "DE", 48.3972222f, 10.4388889f)
				&& graphity.createUser("user-11", "EscortPaidMembership", 2,
						32, 1, 1392101133, "DE", 48.3972222f, 10.4388889f) && graphity
					.createUser("user-12", "CompanyPaidMembership", 3, 19, 1,
							1392101133, "DE", 48.3972222f, 10.4388889f))) {
			System.err
					.println("failed to create test users or creared already!");
		}

		/**
		 * 1 be a friend of 3,4,5,6,7,8,9,10,11;
		 * 
		 * 2 be a friend of 3,4,5;
		 * 
		 * 3 create an event(@1);
		 * 
		 * 4 post blog-1(@3);
		 * 
		 * 6 post blog-2(@2);
		 * 
		 * 7 post blog-3(@5);
		 * 
		 * 5 write comment-1(@4) for 4's blog-1
		 * 
		 * 6 upgrade her membership;
		 * 
		 * 7 upgrade her membership;
		 * 
		 * 1 upgrade his membership;
		 */
		if (graphity.beFriend("user-1", "user-3") != 0) {
			System.err.println("failed to be friend!");
		}
		if (graphity.beFriend("user-1", "user-4") != 0) {
			System.err.println("failed to be friend!");
		}
		if (graphity.beFriend("user-1", "user-5") != 0) {
			System.err.println("failed to be friend!");
		}
		if (graphity.beFriend("user-1", "user-6") != 0) {
			System.err.println("failed to be friend!");
		}
		if (graphity.beFriend("user-1", "user-7") != 0) {
			System.err.println("failed to be friend!");
		}
		if (graphity.beFriend("user-1", "user-8") != 0) {
			System.err.println("failed to be friend!");
		}
		if (graphity.beFriend("user-1", "user-9") != 0) {
			System.err.println("failed to be friend!");
		}
		if (graphity.beFriend("user-1", "user-10") != 0) {
			System.err.println("failed to be friend!");
		}
		if (graphity.beFriend("user-1", "user-11") != 0) {
			System.err.println("failed to be friend!");
		}
		if (graphity.beFriend("user-2", "user-3") != 0) {
			System.err.println("failed to be friend!");
		}
		if (graphity.beFriend("user-2", "user-4") != 0) {
			System.err.println("failed to be friend!");
		}
		if (graphity.beFriend("user-2", "user-5") != 0) {
			System.err.println("failed to be friend!");
		}

		int[] eventTags = { Property.Activity.TAG_EVENT,
				Property.Activity.TAG_10YEAR_CELEBRATION };
		int[] blogTags = { Property.Activity.TAG_BLOG };
		int[] blogCelebrationTags = { Property.Activity.TAG_BLOG,
				Property.Activity.TAG_10YEAR_CELEBRATION };
		int[] blogCommentTags = { Property.Activity.TAG_BLOG_COMMENT };
		int[] avatarTags = { Property.Activity.TAG_AVATAR };
		int[] photoTags = { Property.Activity.TAG_PHOTO,
				Property.Activity.TAG_10YEAR_CELEBRATION };
		int[] membershipTags = { Property.Activity.TAG_MEMBERSHIP };
		int[] membershipCelebrationTags = { Property.Activity.TAG_MEMBERSHIP,
				Property.Activity.TAG_10YEAR_CELEBRATION };

		if (graphity
				.createEvent(
						"event-1",
						"event subject",
						"The interactive shell also features tab completion for functions, "
								+ "constants, class names, variables, static method calls and class "
								+ "constants.Example #2 Tab completionPressing the tab key twice when "
								+ "there are multiple possible completions will result in a list of these "
								+ "completions: ", 1, "user-3", eventTags) != 0) {
			System.err.println("failed to createEvent or exists already");
		}

		if (graphity
				.createBlog(
						"blog-1",
						"Blog subject1",
						"I'm developing a Java package that makes basic "
								+ "HTTP requests (GET, POST, PUT, and DELETE). Right now, I'm having it just print "
								+ "the output of the request. I would like to store it in a field, but I'm not sure"
								+ " if String supports large amounts of text. Is there a data type for large amounts"
								+ " of text, or is there a reasonable alternative to it? Right now, because I'm just"
								+ " printing it, I can't do anything with the data that is returned (like parse it, "
								+ "if it's JSON).", 3, "user-4", blogTags) != 0) {
			System.err.println("failed to createBlog or exists already");
		}

		if (graphity
				.createBlog(
						"blog-2",
						"Blog subject2",
						"I'm developing a Java package that makes basic "
								+ "HTTP requests (GET, POST, PUT, and DELETE). Right now, I'm having it just print "
								+ "the output of the request. I would like to store it in a field, but I'm not sure"
								+ " if String supports large amounts of text. Is there a data type for large amounts"
								+ " of text, or is there a reasonable alternative to it? Right now, because I'm just"
								+ " printing it, I can't do anything with the data that is returned (like parse it, "
								+ "if it's JSON).", 2, "user-6",
						blogCelebrationTags) != 0) {
			System.err.println("failed to createBlog or exists already");
		}

		if (graphity
				.createBlog(
						"blog-3",
						"Blog subject3",
						"I'm developing a Java package that makes basic "
								+ "HTTP requests (GET, POST, PUT, and DELETE). Right now, I'm having it just print "
								+ "the output of the request. I would like to store it in a field, but I'm not sure"
								+ " if String supports large amounts of text. Is there a data type for large amounts"
								+ " of text, or is there a reasonable alternative to it? Right now, because I'm just"
								+ " printing it, I can't do anything with the data that is returned (like parse it, "
								+ "if it's JSON).", 5, "user-7",
						blogCelebrationTags) != 0) {
			System.err.println("failed to createBlog or exists already");
		}

		if (graphity
				.commentBlog(
						"blog-1",
						"comment-1",
						"If you are performing a single set of "
								+ "operations on the data, you can stream it through a "
								+ "pipeline and not even store the entire data in memory "
								+ "at any time. It can also boost performance as work can "
								+ "begin upon the first character rather than after the last "
								+ "is received. Check out CharSequence.", 4,
						"user-5", blogCommentTags) != 0) {
			System.err.println("failed to commentBlog or exists already");
		}

		if (graphity.upgradeMembership("user-6", "membership-1", 11L,
				membershipTags) != 0) {
			System.err.println("failed to upgradeMembership or exists already");
		}

		if (graphity.upgradeMembership("user-7", "membership-2", 10L,
				membershipTags) != 0) {
			System.err.println("failed to upgradeMembership or exists already");
		}

		if (graphity.upgradeMembership("user-1", "membership-3", 40L,
				membershipCelebrationTags) != 0) {
			System.err.println("failed to upgradeMembership or exists already");
		}

		if (graphity.upgradeMembership("user-10", "membership-1", 30L,
				membershipCelebrationTags) != 0) {
			System.err.println("failed to upgradeMembership or exists already");
		}

		if (graphity.upgradeMembership("user-11", "membership-1", 20L,
				membershipCelebrationTags) != 0) {
			System.err.println("failed to upgradeMembership or exists already");
		}

		if (graphity.upgradeMembership("user-9", "membership-1", 14L,
				membershipCelebrationTags) != 0) {
			System.err.println("failed to upgradeMembership or exists already");
		}

		Iterable<Vertex> users = g.getVertices(Property.User.ID, "user-1");

		System.out
				.println("========================case 1=========================");
		System.out
				.println("=random loop user-1's friends activities by using TitanMultiVertexQuery.=");
		// random loop user-1's friends activities by using
		// TitanMultiVertexQuery.
		TitanMultiVertexQuery mq = g.multiQuery();
		Iterator<Vertex> vFriends = users.iterator().next()
				.getVertices(Direction.OUT, EdgeType.BEFRIEND).iterator();
		while (vFriends.hasNext()) {
			Vertex next = vFriends.next();
			System.out.println("friend: "
					+ next.getProperty(Property.User.NAME));
			Iterator<Vertex> vActivities = next.getVertices(Direction.OUT,
					EdgeType.USER_ACTIVITY).iterator();

			while (vActivities.hasNext()) {
				TitanVertex friendEvent = (TitanVertex) vActivities.next();
				mq.addVertex(friendEvent);
			}

		}
		Map<TitanVertex, Iterable<TitanVertex>> map = mq.vertices();

		for (Map.Entry<TitanVertex, Iterable<TitanVertex>> entry : map
				.entrySet()) {
			System.out.println(entry.getKey().getProperties().iterator().next()
					.toString());
			System.out.println("id:" + entry.getKey().getID() + " created @"
					+ entry.getKey().getProperty(Property.Time.TIMESTAMP));
		}

		System.out
				.println("========================case 2=========================");
		System.out
				.println("=test if we can sorted activities of user 1's friends limit 3 start from 1=");
		// test if we can sorted activities of user 1's friends limit 3 start
		// from 1
		int start = 1;
		int limit = 3;
		GremlinPipeline<Object, Object> gp = new GremlinPipeline<Object, Object>(
				g).start(g.getVertices(Property.User.ID, "user-1"));
		Iterator<Vertex> result = gp.out(EdgeType.BEFRIEND)
				.out(EdgeType.USER_ACTIVITY)
				.order(new PipeFunction<Pair<Vertex, Vertex>, Integer>() {
					@Override
					public Integer compute(Pair<Vertex, Vertex> argument) {
						Integer iB = new Integer(argument.getB()
								.getProperty(Property.Time.TIMESTAMP)
								.toString());
						Integer iA = new Integer(argument.getA()
								.getProperty(Property.Time.TIMESTAMP)
								.toString());
						return iB.compareTo(iA);
					}

				}).range(start, limit).toList().iterator();

		while (result.hasNext()) {
			Vertex n = result.next();
			System.out.println(n.getProperty(Activity.TYPE) + "@"
					+ n.getProperty(Time.TIMESTAMP));
		}

		System.out
				.println("========================case 3=========================");
		System.out
				.println("=basic geo search with center from user-1' geo location with gender=1=");
		// basic geo search with center from user-1' geo location with gender=1
		Iterator<Vertex> nearMe = g
				.query()
				.has(Property.User.GEO, Geo.WITHIN,
						Geoshape.circle(48.3972222f, 10.4388889f, 200.00))
				.has(Property.User.GENDER, 1).vertices().iterator();
		while (nearMe.hasNext()) {
			Vertex n = nearMe.next();
			System.out.println(n.getProperty(Property.User.NAME));
		}

		System.out
				.println("========================case 4=========================");
		System.out
				.println("=basic geo search for user-1's client friends with center from user-1' geo location=");
		// basic geo search for user-1's client friends with center from user-1'
		// geo location
		GremlinPipeline<Object, Object> gp2 = new GremlinPipeline<Object, Object>(
				g).start(g.getVertices(Property.User.ID, "user-1"));
		Iterator<? extends Element> nearMeMyFriend = gp2
				.out(EdgeType.BEFRIEND)
				.has(Property.User.GEO, Geo.WITHIN,
						Geoshape.circle(48.3972222f, 10.4388889f, 50.00))
				.has(Property.User.GENDER, 1).toList().iterator();
		while (nearMeMyFriend.hasNext()) {
			Element n = nearMeMyFriend.next();
			System.out.println(n.getProperty(Property.User.NAME));
		}

		System.out
				.println("========================case 5=========================");
		System.out
				.println("=get all category with blog and sub category with 10 years celebration of user-1's friends' activities=");
		// get all category with blog and sub category with 10 years celebration
		// of user-1's friends' activities
		GremlinPipeline<Object, Object> gp3 = new GremlinPipeline<Object, Object>(
				g).start(g.getVertices(Property.User.ID, "user-1"));
		Iterator<? extends Element> feeds = gp3.out(EdgeType.BEFRIEND)
				.out(EdgeType.USER_ACTIVITY).filter(new PipeFunction<Vertex, Boolean>() {
					public Boolean compute(Vertex v) {
						byte[] bitA = v.getProperty(Property.Activity.TAG);
						BitSet bitset = Tools.fromByteArray(bitA);
						if(bitset != null){
							BitSet toBeChecked = new BitSet();
							toBeChecked.set(Property.Activity.TAG_BLOG);
							toBeChecked.set(Property.Activity.TAG_10YEAR_CELEBRATION);
							bitset.and(toBeChecked);
							if (!bitset.isEmpty()) {
								return true;
							}	
						}
						
						return false;
					}
				}).toList().iterator();
		while (feeds.hasNext()) {
			Element n = feeds.next();
			System.out.println(n.getProperty(Property.Activity.TYPE) + "@"
					+ n.getProperty(Property.Time.TIMESTAMP));
		}
	}
}