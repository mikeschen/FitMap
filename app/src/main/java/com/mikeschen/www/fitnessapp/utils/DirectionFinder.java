package com.mikeschen.www.fitnessapp.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.mikeschen.www.fitnessapp.Constants;
import com.mikeschen.www.fitnessapp.models.Distance;
import com.mikeschen.www.fitnessapp.models.Duration;
import com.mikeschen.www.fitnessapp.models.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DirectionFinder {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = Constants.GOOGLE_MAPS_KEY;
    private DirectionFinderListener listener;
    private String origin;
    private String destination;
    private List<Route> routes;
    private int routeCount;
    private int currentCount;
    private double originLat;
    private double originLong;
    private double destinationLat;
    private double destinationLong;
    private double shortestDistance;
    private double wayPointDistance;
    private double distance1;

    public DirectionFinder(DirectionFinderListener listener, String origin, String destination) {
        this.listener = listener;
        this.origin = origin;
        this.destination = destination;
        this.routes = new ArrayList<>();
        this.routeCount = 2;
        this.currentCount = 0;
        this.wayPointDistance = 0;
    }

    public void execute() throws UnsupportedEncodingException {
        routes.clear();
        listener.onDirectionFinderStart();

        new DownloadRawData().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        String urlDestination = URLEncoder.encode(destination, "utf-8");
        return DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&mode=walking&key=" + GOOGLE_API_KEY;
    }

    private String createSecondUrl() throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        String urlDestination = URLEncoder.encode(destination, "utf-8");
        String urlWaypoint = calculateWaypoint();
        return DIRECTION_URL_API + "origin=" + urlOrigin + "&waypoints=" + urlWaypoint + "&destination=" + urlDestination + "&mode=walking&key=" + GOOGLE_API_KEY;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSon(String data) throws JSONException {
        Log.d("json", data);
        if (data == null)
            return;

        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            Route route = new Route();

            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
            JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");
            int totalDistance;
            int totalDuration;
            if(currentCount > 0) {
                JSONObject jsonLeg2 = jsonLegs.getJSONObject(1);
                JSONObject jsonDistance2 = jsonLeg2.getJSONObject("distance");
                JSONObject jsonDuration2 = jsonLeg2.getJSONObject("duration");
                totalDistance = jsonDistance.getInt("value") + jsonDistance2.getInt("value");
                totalDuration = jsonDuration.getInt("value") + jsonDuration2.getInt("value");
                destinationLat = jsonLeg2.getJSONObject("end_location").getDouble("lat");
                destinationLong = jsonLeg2.getJSONObject("end_location").getDouble("lng");
            } else {
                totalDistance = jsonDistance.getInt("value");
                totalDuration = jsonDuration.getInt("value");
                destinationLat = jsonEndLocation.getDouble("lat");
                destinationLong = jsonEndLocation.getDouble("lng");
            }
            Log.d("currentCount!!", currentCount +  "");
            route.duration = new Duration(jsonDuration.getString("text"), totalDistance);
            route.distance = new Distance(jsonDistance.getString("text"), totalDuration);
            Log.d("routeDistance!!", route.distance.text + "");
            route.endAddress = jsonLeg.getString("end_address");
            route.startAddress = jsonLeg.getString("start_address");
            originLat = jsonStartLocation.getDouble("lat");
            originLong = jsonStartLocation.getDouble("lng");
            route.startLocation = new LatLng(originLat, originLong);
            route.endLocation = new LatLng(destinationLat, destinationLong);
            route.points = decodePolyLine(overview_polylineJson.getString("points"));

            if(currentCount == 0) {
                shortestDistance = jsonDistance.getInt("value");
            } else {
                Log.d("distanceDiff", ""+Math.abs(totalDistance - shortestDistance));
                if(Math.abs(totalDistance - shortestDistance) < 200){
                    wayPointDistance += .001;
                    try {
                        new DownloadRawData().execute(createSecondUrl());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return;
                } else {
                    wayPointDistance = 0;
                }
            }
            routes.add(route);
        }

        currentCount++;
        if(currentCount == routeCount) {
            listener.onDirectionFinderSuccess(routes);
        } else {
            try {
                new DownloadRawData().execute(createSecondUrl());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private String calculateWaypoint() {
        double midLat = (originLat + destinationLat)/2;
        double midLong = (originLong + destinationLong)/2;
        double latDiff = originLat - destinationLat;
        double lngDiff = originLong - destinationLong;

        distance1 = Math.sqrt((latDiff*latDiff) + (lngDiff*lngDiff));
        if(wayPointDistance == 0) {
            wayPointDistance = distance1 / 3;
        }

        double angle = Math.atan2(latDiff,lngDiff);
        double theta =  (2*Math.PI) - angle;
        Log.d("atanguts", (originLat - destinationLat)/(originLong - destinationLong)+"");
        Log.d("theta", theta+"");
        double wayPointLat;
        double wayPointLong;
            wayPointLat = midLat + wayPointDistance * Math.cos(theta);
            wayPointLong = midLong + wayPointDistance * Math.sin(theta);
        Log.d("verticalDistance", wayPointDistance * Math.sin(theta)+"");
        Log.d("horizontalDistance", wayPointDistance * Math.cos(theta)+"");
        return wayPointLat + "," + wayPointLong;
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }
        return decoded;
    }
}

