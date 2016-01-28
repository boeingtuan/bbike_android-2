package iamhuy.bbike;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by Tuân on 27/01/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.LocationViewHolder>{
    private List<Location> locations;
    private GoogleMap map;
    private LocationViewHolder.LocationViewHolderListener mViewHolderListener;

    RVAdapter(List<Location> locations, GoogleMap map, LocationViewHolder.LocationViewHolderListener listener) {
        this.locations = locations;
        this.map = map;
        this.mViewHolderListener = listener;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_search_item, viewGroup, false);
        LocationViewHolder pvh = new LocationViewHolder(v, mViewHolderListener);

        return pvh;
    }

    public Object getItemAtPosition(int position) {
        try {
            return locations.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(LocationViewHolder locationViewHolder, int i) {
        locationViewHolder.locationName.setText(locations.get(i).name);
        locationViewHolder.address.setText(locations.get(i).address);
        locationViewHolder.rate.setText(String.valueOf(locations.get(i).rate));
        locationViewHolder.likeNum.setText(String.valueOf(locations.get(i).likeNum));
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView locationView;
        TextView locationName;
        TextView address;
        TextView likeNum;
        TextView comNum;
        TextView distance;
        TextView rate;

        ImageView icon;

        public LocationViewHolder(View itemView, final LocationViewHolderListener listener) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            locationView = (ImageView) itemView.findViewById(R.id.location_photo);
            locationName = (TextView) itemView.findViewById(R.id.location_name);
            address = (TextView) itemView.findViewById(R.id.address);
            likeNum = (TextView) itemView.findViewById(R.id.like_num);
            comNum = (TextView) itemView.findViewById(R.id.comment_num);
            distance = (TextView) itemView.findViewById(R.id.distance);
            rate = (TextView) itemView.findViewById(R.id.rate);

            icon = (ImageView) itemView.findViewById(R.id.ping);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onIconClicked(LocationViewHolder.this);
                }
            });
        }

        public interface LocationViewHolderListener {
            void onIconClicked(LocationViewHolder clickedViewHolder);
        }
    }
}
