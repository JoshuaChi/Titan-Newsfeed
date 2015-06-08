curl -XPUT http://localhost:8080/demo/rest/newsfeed/create/keys/community1 -H 'Content-type:application/json' -d '
[{
	"key": "iid",
	"type": "string",
	"index": ["vertex", "edge", "single", "unique"]
},
{
	"key": "time",
	"type": "string",
	"index": ["vertex", "edge"]
}]
'

curl -XGET http://localhost:8080/demo/rest/newsfeed/has/property/key/community1/iid


curl -XPUT http://localhost:8080/demo/rest/newsfeed/create/labels/community1 -H 'Content-type:application/json' -d '
[{
	"name": "upload_image"
}]
'

curl -XPUT  http://localhost:8080/demo/rest/newsfeed/delete/vertex/community1/user/1

curl -XPUT http://localhost:8080/demo/rest/newsfeed/create/vertices/community1/user -H 'Content-type:application/json' -d '
[{ 
	"id": {
	  "type": "int",
	  "value": 1
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
}]'

curl -XPUT  http://localhost:8080/demo/rest/newsfeed/delete/vertex/community1/images/100

curl -XPUT http://localhost:8080/demo/rest/newsfeed/create/vertices/community1/images -H 'Content-type:application/json' -d '
[{ 
	"id": {
	  "type": "int",
	  "value": 100
	}, 
	"ownerid": {
	  "type": "int",
	  "value": 1
	},
	"filename": {
	  "type": "string",
	  "value": "avatar"
	}
}]'


curl -XPUT http://localhost:8080/demo/rest/newsfeed/create/edges/community1 -H 'Content-type:application/json' -d '
[
  {
    "from": {"id": "1", "model": "user"},
    "to": {"id": "100", "model": "images"},
    "label": "upload_image"
  }
]'

curl -XGET http://localhost:8080/demo/rest/newsfeed/get/edges/community1/user/1

curl -XDELETE http://localhost:8080/demo/rest/newsfeed/delete/edges/community1 -H 'Content-type:application/json' -d '
[
  {
    "vertex": {"id": "1", "model": "user"},
    "direction": 1,
    "label": "upload_image"
  }
]'