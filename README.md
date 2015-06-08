Newsfeed Service
=========

<pre>
+--------------------+             +---------------------+ 
|                    |             |                     | 
|    Poppen.de       |             |  Newsfeed Service   | 
|                    |             |                     | 
+--------------------+             +---------------------+ 
|                    | Http(Restful)    Titan Graph      | 
|                    +-----------> |    Database         | 
|  tncNewsfeedPlugin |             +---------+-----------+ 
|                    |    JSON     |         |           | 
|                    | <-----------+         |           | 
|                    |             |Storage: |Index:     | 
|                    |             |Cassandra|Elasticsearch
|                    |             |         |           | 
|                    |             |         |           | 
+--------------------+             +---------+-----------+ 
</pre>


##Goal

Provider restful service to community.

##Cases

###Create vertex:
- Method: PUT
- Return: int
    - 1: success
    - 0: fail
    - bigger than 1000: exception code
- ACCEPT: JSON
- URI: create/vertex/$community/$model
	- $model: user/event/photo/avatar 
- PUT JSON FORMAT:
	- $fieldName: reserved keywords: iid/geo/lat/lon
	- $type: int/long/string/float
    - $key_index_type: string
    - $index_array: "vertex", "edge", "unique", "search-vertex", "search-edge"
- NOTE:
	1. "is_key_index" can be used on field "iid" when create vertex. It will be ignored when attached with other fields;
    2. if "is_key_index" provided, we will check if it exists or not firstly,
if the key is already there, we will ignore it. Otherwise we will create the key index.
 
<pre>
{ 
	"$fieldName": {
	  "type": "$type",
	  "value": $value,
      "is_key_index": {
	    "data_type": $key_index_type
        "index": $index_array
	  }
	}
	...
}
</pre>
- Example for simple query:
<pre>
curl -XPUT http://localhost:8080/demo/rest/newsfeed/create/vertex/community1/user -H 'Content-type:application/json' -d '
{ 
	"iid": {
	  "type": "int",
	  "value": 10090,
	  "is_key_index": {
		"data_type": "string",
		"index": ["vertex", "edge", "unique", "search-vertex", "search-edge"]
	  }
	}, 
	"name": {
	  "type": "string",
	  "value": "Robby"
	},
	"gender": {
	  "type": "int",
	  "value": 1
	}, 
	"membership": {
	  "type": "int",
	  "value": 2
	}, 
	"lastlogin": {
	  "type": "string",
	  "value": "2014-03-13 13:05:02"
	}, 
	"country": {
	  "type": "string",
	  "value": "DE"
	},
	"lat": {
	  "type": "double",
	  "value": 54.00694
	},
	"lon": {
	  "type": "double",
	  "value": 61.18889
	},
	"geo": {
	  "type": "geo",
	  "value": [54.00694, 61.18889]
	}
}
</pre>


- Method: POST
- Return: BOOLEAN
- Format: /create/vertex/$model_name/id/$id/$property_name1/$property_value1/.../$property_nameN/$property_valueN
	- $model_name: user/event/photo/avatar 
	- $id - java type: string, should be unique cross whole graph
	- $property_nameX - java type: string
	- property_valueX - java type: int/long/string/float
- Example:
<pre>
/create/vertex/user/id/user_client1/name/Client1/gender/1/age/30/payclass/0/lastlogin/1392100133/country/DE/lat/48.3972222/lon/10.4388889
</pre>

<pre>
graphity.createUser("user-1", "Client1", 1, 30, 0, 1392100133,"DE", 48.3972222f, 10.4388889f)
</pre>

###Create edge:

- Method: POST
- Return: BOOLEAN
- Format: /create/edge/$model_nameX/$idX/$model_nameY/$idY/label/$edge_label
	- $model_name: user/event/photo/avatar 
	- $id(uniqiue id for this vertex) - java type: int
	- $edge_label - java type: string
- Example:
<pre>
/create/edge/user/user-1/user/user-3/label/befriend
</pre>

<pre>
if (graphity.beFriend("user-1", "user-3") != 0) {
	System.err.println("failed to be friend!");
}
</pre>

###Create key index:

- Method: POST
- Return: BOOLEAN
- Format: /create/key_index/$model_name/$data_key/$data_type/unique/$unique_value/indexed/$value1/indexed/$value2/../indexed/$valueN/indexed_search/$indexed_search_value1/.../indexed_search/$indexed_search_valueN
	- $model_name: user/event/photo/avatar 
	- $data_key - java type: String
	- $data_type: clazz Data type to be configured
	- $unique_value: 1/0
	- $valueX - java String vertex/edge
- Example:
<pre>
/create/key_index/user/user_id/string/unique/1/indexed/vertex/indexed/edge/indexed_search/vertex
</pre>

<pre>
graph.makeKey(Property.User.ID).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();
</pre>

###Create label index:

- Method: POST
- Return: BOOLEAN
- Format: /create/label_index/$label_name/sort/$key/order/$order_value
	- $label_name - java type: string
	- $key - _make sure key is created before used_
	- $order_value - desc/asc
- Example:
<pre>
step 1: /create/key_index/time/timestamp/int
step 2: /create/label_index/befriend/sort/time/order/desc
</pre>

<pre>
step 1: TitanKey time = graph.makeKey(Property.Time.TIMESTAMP).dataType(Integer.class).make();
step 2: graph.makeLabel(EdgeType.BEFRIEND).sortKey(time).sortOrder(Order.DESC).make();
</pre>

###Filter graph:
- Method: PUT
- Return: JSON
- ACCEPT: JSON
- URI: /filter/$community
- PUT JSON FORMAT:
<pre> 
{ 
	"start": {"key": $theKeyOfVertex, "value": $theValueOfTheVertex},
	"paths": [
		{
			"direction": java type: int, options: 1(out)|2(in)|3(both),
			"edgeLabel": java type: string,
			"properties": [
				{	
					"type": java type: int, options: 1(has)|2(has_not)|4(has_tags)|8(exclude_tags)|16(geo_intersect)|32(disjoint)|64(within), 
					"value": {
						"key": java type: string, options: "tags"/"geo"/"user_gender"/..., 
						"value": 
							<if tag> java type: ArrayList, e.g.[$tagBitValue1, $tagBitValue2, ...];
							<if geo> java type: ArrayList, e.g.[$center_lat, $center_long,$distance];
							<if other> java type: String/Integer
						}
				}
			],
			"offset": java type: int, starts from 1, 
			"limit": java type: int, starts from 1, 
			"order": java type: boolean, default: true, order by timestamp desc
		}
	]
}
</pre>

- Example for simple query:
<pre>
curl -XPUT http://localhost:8080/demo/rest/newsfeed/filter/community1 -H 'Content-type:application/json' -d '
{ 
"start": {"key": "user_id", "value": "user-1"}, 
"paths": [{
	"direction": 1,
	"edgeLabel": "be_friend",
	"properties": []
	},
	{
	"direction": 1,
	"edgeLabel": "user_activity",
	"properties": []
	}
	],
	"order": true,
	"offset": 1, 
	"limit": 12 
}'
</pre>
- Example for tag filtering:
<pre>
curl -XPUT http://localhost:8080/demo/rest/newsfeed/filter/community1 -H 'Content-type:application/json' -d '
{ 
"start": {"key": "user_id", "value": "user-1"}, 
"paths": [{
	"direction": 1,
	"edgeLabel": "be_friend",
	"properties": []
	},
	{
	"direction": 1,
	"edgeLabel": "user_activity",
	"properties": [ {"type": 4, "value": {"key":"tags", "value":[2, 64]}} ]
	}
	], 
	"order": true,
	"offset": 1, 
	"limit": 12 
}'
</pre>
- Example for geo within:
<pre>
curl -XPUT http://localhost:8080/demo/rest/newsfeed/filter/community1 -H 'Content-type:application/json' -d '
{ 
"start": {"key": "user_id", "value": "user-1"}, 
"paths": [{
	"direction": 1,
	"edgeLabel": "be_friend",
	"properties": [{"type": 64, "value": {"key":"tags", "value":[48.3972222, 10.4388889, 50.00]}}, {"type": 1, "value": {"key": "user_gender", "value": 1}}]
	}], 
	"offset": 1, 
	"limit": 12,
	"order": false
}'
</pre>


##Setup
- titan-all-0.4.2
- jdk7
- elasticsearch 0.90.10
- cassandra 2.0.3


##Tips
 - switch between jdk6 and jdk7 @macos:

> export JAVA_HOME=$(/usr/libexec/java_home -v 1.7)
export JAVA_HOME=$(/usr/libexec/java_home -v 1.6)

 - When switch between cassandra 2.0.3 and 1.2.2
Do not forget to delete saved_caches_directory and data_file_directories
