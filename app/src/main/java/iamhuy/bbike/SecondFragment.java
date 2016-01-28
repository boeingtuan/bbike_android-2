package iamhuy.bbike;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SecondFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SearchView mSearch;
    private Spinner spinner;
    private GoogleMap map;

    private LatLng currLoc;
    private List<Location> locations;

    JSONParser jsonParser = new JSONParser();
    private static final String url_search = "http://bbike.xyz/api/v1/search";
    private static final String url_location = "http://bbike.xyz/api/v1/location/";

    JSONObject jsonResult;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spinner = (Spinner) getActivity().findViewById(R.id.city_list);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.option_search_array, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mSearch = (SearchView) getActivity().findViewById(R.id.search_bar);
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("bbike search", "onQueryTextSubmit ok");
                new Search(spinner.getSelectedItemPosition(), mSearch.getQuery()).execute();
                map.clear();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        Log.d("bbike fm", "onActivityCreated " + mapFragment);

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.list);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        /*locations = new ArrayList<>();
        locations.add(new Location(1, "Tuan", "175 Vo Thi Sau", 7.5, 10));
        locations.add(new Location(1, "Tuan1", "175 Vo Thi Sau", 7.5, 10));
        locations.add(new Location(1, "Tuan2", "175 Vo Thi Sau", 7.5, 10));

        //just test
        RVAdapter mRVadapter = new RVAdapter(locations);
        mRecyclerView.setAdapter(mRVadapter);*/
    }

    class Search extends AsyncTask<String, String, String> {

        private int type;
        private String query;
        ProgressDialog progDailog;

        Search(int type, CharSequence query) {
            Charset charset = Charset.forName("UTF-8"); ;
            byte[] bytes = query.toString().getBytes(charset);
            CharSequence seq2 = new String(bytes, charset);

            Log.d("bbike search", String.valueOf(type) + seq2);

            this.type = type;
            this.query = seq2.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(getContext());
            progDailog.setMessage("Chờ tí nhá ...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
        }

        @Override
        protected String doInBackground(String... agrs) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("type", String.valueOf(type)+1));
            params.add(new BasicNameValuePair("keyword", query));
            switch (type) {
                case 0:
                    params.add(new BasicNameValuePair("latitude", String.valueOf(currLoc.latitude)));
                    params.add(new BasicNameValuePair("longtitude", String.valueOf(currLoc.longitude)));
                    params.add(new BasicNameValuePair("range", "10000"));
                    if (!query.equals("")) params.add(new BasicNameValuePair("keyword", query));
                    break;
                case 1:
                    params.add(new BasicNameValuePair("keyword", query));
                    break;
                case 2:
                    params.add(new BasicNameValuePair("keyword", query));
                    break;
            }

            jsonResult = jsonParser.makeHttpRequest(url_search, "POST", params);
            try {
                Log.d("bbike search", jsonResult.toString());
                locations = new ArrayList<>();
                for (int i = 0; i < jsonResult.optJSONArray("locationId").length(); i++) {
                    JSONObject jsonItem = jsonParser.makeHttpRequest(url_location +
                            String.valueOf(jsonResult.optJSONArray("locationId").get(i)), "GET", new ArrayList<NameValuePair>());
                    JSONObject jsonDetail = jsonItem.optJSONArray("location").getJSONObject(0);
                    locations.add(new Location(jsonDetail.optInt("id"), jsonDetail.optString("name"), jsonDetail.optString("address"),
                            jsonDetail.optInt("rateSum") / (jsonDetail.optInt("rateTurnNum") + 1), jsonDetail.optInt("likeNum"),
                            jsonDetail.optDouble("x"), jsonDetail.optDouble("y")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progDailog.dismiss();
            final RVAdapter mRVadapter = new RVAdapter(locations, map, new RVAdapter.LocationViewHolder.LocationViewHolderListener() {
                @Override
                public void onIconClicked(RVAdapter.LocationViewHolder clickedViewHolder) {
                    int itemPosition = clickedViewHolder.getAdapterPosition();
                    map.clear();
                    map.addMarker(new MarkerOptions().position(new LatLng(locations.get(itemPosition).latitude, locations.get(itemPosition).longitude)).title(locations.get(itemPosition).name));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locations.get(itemPosition).latitude, locations.get(itemPosition).longitude), 16.0f));

                    showRouteDirection(currLoc, new LatLng(locations.get(itemPosition).latitude, locations.get(itemPosition).longitude));
                }
            });
            mRecyclerView.setAdapter(mRVadapter);
            mRecyclerView.requestFocus();
        }
    }

    private void showRouteDirection(LatLng currLoc, LatLng destLoc) {
        String url = getDirectionsUrl(currLoc, destLoc);
        DrawTask drawTaskTask = new DrawTask();
        drawTaskTask.execute(url);
    }

    private class DrawTask extends AsyncTask<String, Void, List<List<HashMap<String, String>>>>{

        // Downloading data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... url) {
            JSONObject data;
            List<List<HashMap<String, String>>> routes = null;
            data = new JSONParser().makeHttpRequest(url[0], "GET", new ArrayList<NameValuePair>());
            DirectionsJSONParser parser = new DirectionsJSONParser();

            routes = parser.parse(data);
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            super.onPostExecute(result);
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);

        }
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    @Override
    public void onDestroyView() {
        Fragment f = getFragmentManager().findFragmentById(R.id.map);
        if (f != null) {
            getFragmentManager().beginTransaction().remove(f).commit();
        }

        super.onDestroyView();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(android.location.Location location) {
                if (currLoc == null) {
                    currLoc = new LatLng(location.getLatitude(), location.getLongitude());
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 16.0f));
                }
                else
                    currLoc = new LatLng(location.getLatitude(), location.getLongitude());
            }
        };
        map.setOnMyLocationChangeListener(myLocationChangeListener);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
