var map;
var map_center;
var coords = new Array();
var entity_to_color = {};
// In the general vicinity of New York City.
var DEFAULT_POSITION = {
		coords: {
			latitude: 40,
			longitude: -74
		}
};

// START of methods IE7 doesn't implement.
if (!Array.prototype.indexOf) {
	Array.prototype.indexOf = function(obj, start) {
		for (var i = (start || 0), j = this.length; i < j; i++) {
			if (this[i] === obj) { return i; }
		}
		return -1;
	}
}

if (!Array.prototype.lastIndexOf) {
	Array.prototype.lastIndexOf = function(obj, start) {
		var lastIndex = -1;
		for (var i = (start || 0), j = this.length; i < j; i++) {
			if (this[i] === obj) { 
				lastIndex =  i;
			}
		}
		return lastIndex;
	}
}

if(!String.prototype.substring) {
	String.prototype.substring = function(start, end) {
		var returnString = "";
		for(var i = start; i < end; i++) {
			returnString += this.charAt(i);
		}
		return returnString;
	}
}

if(!String.prototype.trim) {
	String.prototype.trim= function(){
		return this.replace(/^\s+|\s+$/g, '');
	};
}
//END of methods IE7 does not implement.

/**
 * Get the individuals position on the map. New York City by default.
 */
function getLocation() {
	if (window.navigator.geolocation) {
		window.navigator.geolocation.getCurrentPosition(showPosition);
	} else {
		showPosition(DEFAULT_POSITION);
	}
}

/**
 * Display the persons location. Callback from the getLocation method. Calls other methods.
 * @param position
 * 			position of person in latitude and longitude.
 */
function showPosition(position) {
	currentLocation = { lat: position.coords.latitude, lng: position.coords.longitude};
	getMap(currentLocation);
	getAlerts(currentLocation, map);
}

/**
 * Places marker in person's location and creates map.
 * @param location
 * 			location of individual.
 */
function getMap(location) {
	map_center = location;
	var mapOptions = {
			center: location,
			zoom: 4
	};

	map = new google.maps.Map(document.getElementById('map-canvas'),
			mapOptions);

	google.maps.event.addListener(map, 'dragend', function(event) {
		map_center = this.getCenter();
	})

	// Person marker. It's a white arrow with a blue outline.
	var marker = new google.maps.Marker({
		position: location,
		icon : {
			path: google.maps.SymbolPath.BACKWARD_CLOSED_ARROW,
			scale: 4,
			fillColor: "white",
			strokeColor: "blue",
			fillOpacity: 1,
			heading: "home"
		},
		map: map
	});
}

/**
 * Get the alerts from service.
 * @param location
 * 			location of person. LatLng.
 * @param map
 * 			the map to put the markers on.
 */
function getAlerts(location, map) {
	var url = "/api/news?" + encodeURI("lat=" + location.lat + "&lng=" + location.lng);
	var xhr = createCORSRequest('GET', url);
	if (!xhr) {
		throw new Error('CORS not supported');
	}
	xhr.onload = function() {
		var responseText = xhr.responseText;
		var json;
		if (typeof JSON != "undefined") {
			json = JSON.parse(responseText);
		} else if ( typeof jsonParse != "undefined"){
			json = jsonParse(responseText);
		} else {
			throw new Error('Unable to parse JSON.');
		}
		displayAlerts(json, map);
	}
	xhr.onerror = function() {
		console.log(xhr.responseText);
	}
	xhr.send();
}

/**
 * Call back of getAlerts method.
 * 
 * @param alerts
 * 			The alerts json.
 * @param map
 * 			The map to put the markers on.
 */
function displayAlerts(alerts, map) {
	var distance = alerts["newsDistance"];
	var news_items = alerts["newsItems"];
	var east = map.getCenter().lng();
	var west = map.getCenter().lng();
	var south = map.getCenter().lat();
	var north = map.getCenter().lat();

	for(var i = 0; i < news_items.length; i++) {
		var item = news_items[i];

		var persons = item["personNames"];
		var organizations = item["orgNames"];
		var url = item["url"];
		var headline = item["headline"];
		var lat = parseFloat(item["lat"]);
		var lng = parseFloat(item["lng"]);
		var is_closest = item["isClosest"];

		var coord = lat + "," + lng;
		while(coords.indexOf(coord) > -1) {
			lng += Math.random() * .5 - .25;
			lat += Math.random() * .5 - .25;
			coord = lat + "," + lng;
		}
		coords.push(coord);

		var lat_lng = {"lat":lat, "lng":lng};
		var marker = new google.maps.Marker({
			position: lat_lng,
			map: map
		});

		var content_string =  "<h5>" + headline + "</h5><ul>";

		if(persons.length > 0) {
			content_string += "<li>Persons:" + persons + "</li>";
		}

		if(organizations.length > 0) {
			content_string += "<li>Organizations:" + organizations + "</li>";
		}

		content_string += "</ul>";

		var info = new google.maps.InfoWindow({
			content: content_string,
			disableAutoPan: true
		});



		if(is_closest) {
			// populate closest news.
			placeNews(url);
			placeMetadata(item);
			
			// We are guaranteed this code will only run one time and since east==west and north==south this should work.
			if(lng < west) {
				west = lng;
			} else {
				east = lng;
			}
			if(lat < south) {
				south = lat;
			} else {
				north = lat;
			}
		}
		// Yay Javascript. item and info will be used in the listeners.
		marker.data = item;
		marker.infowindow = info;

		addListeners(marker);
	}
	
	var angle_lng = east - west;
	var angle_lat = north - south;
	var pixelWidth = document.getElementById('map-canvas').clientWidth;
	var pixelHeight = document.getElementById('map-canvas').clientHeight;
	
	map.setZoom(getZoomValue(angle_lng,angle_lat,pixelWidth,pixelHeight));
}

/**
 * Calculates the zoom value to use.
 * 
 * @param angle_lng
 * 			angle along longitude.
 * @param angle_lat
 * 			angle along latitude.
 * @param pixelWidth
 * 			pixel width of container.
 * @param pixelHeight
 * 			pixel height of container.
 * @return the zoom value.
 */
function getZoomValue(angle_lng,angle_lat,pixelWidth, pixelHeight) {
	var GLOBE_WIDTH = 256; // a constant in Google's map projection

	// Longitude goes from 180 to -180
	if(angle_lng < 0) {
		angle_lng += 360;
	}

	// Latitude goes from -90 to 90
	if(angle_lat < 0) {
		angle_lat += 180;
	}

	return Math.max(
			// calculate zoom with closest point and show 4 times as much space
			Math.min(
					Math.round(Math.log(pixelWidth * 360 / angle_lng / GLOBE_WIDTH) / Math.LN2) - 2,
					Math.round(Math.log(pixelHeight * 180 / angle_lat / GLOBE_WIDTH) / Math.LN2) - 2)
					, 1);
}

/**
 * Adds listeners to the marker.
 * @param marker
 * 			the marker to put the listeners on.
 */
function addListeners(marker) {
	google.maps.event.addListener(marker, 'click', function(event) {
		var item = this.data;
		var url = item["url"];
		placeNews(url);
		placeMetadata(item);
	});

	google.maps.event.addListener(marker, 'mouseover', function(event) {
		this.infowindow.open(map, this);
	});

	google.maps.event.addListener(marker, 'mouseout', function(event) {
		this.infowindow.close();
	});
}

/**
 * Places the news in the iframe.
 * @param url
 * 			the url.
 */
function placeNews(url) {
	var news_container = document.getElementById("news-article");
	news_container.src = url;
}

/**
 * Places metadata in the news-data div.
 * @param item
 * 			The JSON for the item to place in the news-data.
 */
function placeMetadata(item) {
	var news_data = document.getElementById("news-data");

	news_data.innerHTML = "";
	var entities = item["ents"];
	var relationships = item["rels"];

	if( entities.length > 0 ) {
		news_data.appendChild(createHeadlineElement("ENTITIES"));
		news_data.appendChild(generateEntityTable(entities));
	}
	if ( relationships.length > 0 ) {
		news_data.appendChild(createHeadlineElement("RELATIONSHIPS"));
		news_data.appendChild(generateRelationshipTable(relationships));
	}
}

/**
 * Used for tables titles.
 * @param headline
 * 			The headline.
 * @returns headline.
 */
function createHeadlineElement(headline) {
	var entity_headline = document.createElement("h3");
	entity_headline.innerHTML = headline;
	return entity_headline;
}

/**
 * @param entities
 * 			A list of entities formatted: Entity ( entityType )
 * @returns a table with columns: Entity | entityType
 */
function generateEntityTable(entities) {
	var table = document.createElement("table");
	var tbody = document.createElement("tbody");
	table.appendChild(tbody);

	for(var i = 0; i < entities.length; i++) {
		var table_row = document.createElement("tr");
		var table_data1 = document.createElement("td");
		var table_data2 = document.createElement("td");
		var entity = entities[i];
		var indexOpenParen = entity.indexOf('(');
		var indexClosedParen = entity.indexOf(')');

		var entityString = entity.substring(0, indexOpenParen).trim();
		var entityType = entity.substring(indexOpenParen + 1, indexClosedParen).trim();

		table_data1.innerHTML = entityString;
		table_data2.innerHTML = entityType;
		table_row.appendChild(table_data1);
		table_row.appendChild(table_data2);
		// table_row.innerHTML = "<td>" + entityString + "</td><td>" + entityType + "</td>";
		tbody.appendChild(table_row);
	}
	return table;
}

/**
 * @param relationships
 * 			A list of relationships formatted: Entity --- ( relationship ) --- Entity
 * @returns a table with columns: entity1 | relationship | entity2
 */
function generateRelationshipTable(relationships) {
	var table = document.createElement("table");
	var tbody = document.createElement("tbody");
	table.appendChild(tbody);

	for(var i = 0; i < relationships.length; i++) {
		var table_row = document.createElement("tr");
		var table_data1 = document.createElement("td");
		var table_data2 = document.createElement("td");
		var table_data3 = document.createElement("td");
		var relationship = relationships[i];

		var indexFirstDashes = relationship.indexOf('---');
		var indexLastDashes = relationship.lastIndexOf('---');
		var indexOpenParen = relationship.indexOf('(');
		var indexClosedParen = relationship.indexOf(')');

		var entityString1 = relationship.substring(0, indexFirstDashes).trim();
		var entityRelationship = relationship.substring(indexOpenParen + 1, indexClosedParen).trim();
		var entityString2 = relationship.substring(indexLastDashes + 3, relationship.length).trim();

		table_data1.innerHTML = entityString1;
		table_data2.innerHTML = entityRelationship;
		table_data3.innerHTML = entityString2;

		table_row.appendChild(table_data1);
		table_row.appendChild(table_data2);
		table_row.appendChild(table_data3);
		tbody.appendChild(table_row);
	}

	return table;
}

/**
 * initialize the window.
 */
function initialize() {
	window.onresize = function(event) {
		positionContainerElements();
		map.setCenter(map_center);
	};

	positionContainerElements();
	getLocation();
}
google.maps.event.addDomListener(window, 'load', initialize);

/**
 * Generates the CORS Request
 * @param method
 * 			The HTTP method to call. GET, POST, PUT, DELETE
 * @param url
 * 			The url to call. Note URL should have CORS enabled.
 * @returns object to call CORS request or null if browser doesn't support CORS request.
 */
function createCORSRequest(method, url) {
	var xhr;
	if (typeof XMLHttpRequest != "undefined" ) {
		xhr = new XMLHttpRequest();
		if ("withCredentials" in xhr) {

			// Check if the XMLHttpRequest object has a "withCredentials" property.
			// "withCredentials" only exists on XMLHTTPRequest2 objects.
			xhr.open(method, url, true);

		} else if (typeof XDomainRequest != "undefined") {
			// Otherwise, check if XDomainRequest.
			// XDomainRequest only exists in IE, and is IE's way of making CORS requests.
			// IE9 and below don't implement withCredentials.
			xhr = new XDomainRequest();
			xhr.open(method, url);
		} else {
			xhr = null;
		}
	} else if (typeof XDomainRequest != "undefined") {

		// Otherwise, check if XDomainRequest.
		// XDomainRequest only exists in IE, and is IE's way of making CORS requests.
		xhr = new XDomainRequest();
		xhr.open(method, url);

	} else {

		// Otherwise, CORS is not supported by the browser.
		xhr = null;

	}
	return xhr;
}

/**
 * For ease of use we move the elements around.
 */
function positionContainerElements() {
	var container = document.getElementById("container");
	var height = container.clientHeight;
	var width = container.clientWidth;

	if (height > width) {
		placeVertical(height, width);
	} else {
		placeHorizontal(height, width);
	}
}

/**
 * Vertical Layout.
 * 
 * @param height
 * 			Height of the window.
 * @param width
 * 			Width of the window.
 */
function placeVertical(height, width) {
	var map = document.getElementById("map-canvas");
	var news_metadata = document.getElementById("news-data");
	var news_article = document.getElementById("news-article");

	var page_center = width/2;
	
	// Top Left
	map.style.left = "0px";
	map.style.top = "0px";
	map.style.height = page_center + "px";
	map.style.width = page_center + "px";

	// Top Right
	news_metadata.style.left = page_center + "px";
	news_metadata.style.top = "0px";
	news_metadata.style.height = page_center + "px";
	news_metadata.style.width = page_center + "px";

	// Bottom Half
	news_article.style.left = "0px";
	news_article.style.top = page_center + "px";
	news_article.style.height = (height - page_center) + "px";
	news_article.style.width = width + "px";
}

/**
 * Horizontal Layout.
 * 
 * @param height
 * 			Height of the window.
 * @param width
 * 			Width of the window.
 */
function placeHorizontal(height, width) {
	var map = document.getElementById("map-canvas");
	var news_metadata = document.getElementById("news-data");
	var news_article = document.getElementById("news-article");

	var page_center = height/2;

	// Top Left
	map.style.left = "0px";
	map.style.top = "0px";
	map.style.height = page_center + "px";
	map.style.width = page_center + "px";

	// Bottom Left
	news_metadata.style.left = "0px";
	news_metadata.style.top = page_center + "px";
	news_metadata.style.height = page_center + "px";
	news_metadata.style.width = page_center + "px";

	//Right Half
	news_article.style.left = page_center + "px";
	news_article.style.top = "0px";
	news_article.style.height = height + "px";
	news_article.style.width = (width - page_center) + "px";
}