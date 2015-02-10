package com.ibm.watson.newsanalyst;

import java.util.Locale;

//import android.location.Location;




/**
 * 
 * @author Bruce Slawson <bruce.slawson@gmail.com>
 *
 */
public class GeoUtils {
	public enum Units {MILES, KILOMETERS, NAUTICAL_MILES};
	
	
	private GeoUtils() {
		// prevent instantiation
	}

	
	/**
	 * Calculates the distance between two locations (latitudes and longitudes).
	 * 
	 * @param lat1 Location 1 latitude
	 * @param lon1 Location 1 longitude
	 * @param lat2 Location 2 latitude
	 * @param lon2 Location 2 longitude
	 * @param unit Unit of distance. Miles, kilometers, or nautical miles.
	 * @return Distance between location 1 and location 2.
	 */
	public static double distance(double lat1, double lon1, double lat2, double lon2, Units unit) {
		double theta = lon1 - lon2;
		double distance = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		distance = Math.acos(distance);
		distance = rad2deg(distance);
		distance = distance * 60 * 1.1515;
		
		if (unit == Units.KILOMETERS) {
			distance = distance * 1.609344;
		} else if (unit == Units.NAUTICAL_MILES) {
			distance = distance * 0.8684;
		}
		
		return distance;
	}

	
	/**
	 * Calculates the distance between two locations (latitudes and longitudes).
	 * 
	 * @param lat1 Location 1 latitude
	 * @param lon1 Location 1 longitude
	 * @param lat2 Location 2 latitude
	 * @param lon2 Location 2 longitude
	 * @param unit Unit of distance. Miles, kilometers, or nautical miles.
	 * @return Distance between location 1 and location 2.
	 */
//	public static double distance2(double lat1, double lon1, double lat2, double lon2, Units unit) {
//		float[] results = new float[3];
//		Location.distanceBetween(lat1, lon1, lat2, lon2, results);
//		double distance = results[0] / 1000.0; // kilometers
//		
//		if (unit == Units.MILES) {
//			distance = distance * 0.621371;
//		} else if (unit == Units.NAUTICAL_MILES) {
//			distance = distance * 0.539957;
//		}
//		
//		return distance;
//	}

	
	
	
	/**
	 * Format the distance for printing
	 * 
	 * @param distance
	 * @return Formatted string
	 */
	public static String distanceString(double distance) {
		return String.format(new Locale("en", "US"), "%.3f", distance);
	}
	
	
	public static double milesToKilometers(double miles) {	
		return (miles / 0.621371);
	}
	
	public static double kilometersToMiles(double kilometers) {	
		return (kilometers * 0.621371);
	}	
	
	
	/**
	 * This function converts decimal degrees to radians 
	 * 
	 * @param deg
	 * @return
	 */
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	
	}

	
	/**
	 * This function converts radians to decimal degrees 
	 * 
	 * @param rad
	 * @return
	 */
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	
}
