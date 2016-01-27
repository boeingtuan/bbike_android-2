package iamhuy.bbike;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tuân on 27/01/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.LocationViewHolder>{
    private List<Location> locations;

    RVAdapter(List<Location> locations) {
        this.locations = locations;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_search_item, viewGroup, false);
        LocationViewHolder pvh = new LocationViewHolder(v);
        return pvh;
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

        public LocationViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            locationView = (ImageView) itemView.findViewById(R.id.location_photo);
            locationName = (TextView) itemView.findViewById(R.id.location_name);
            address = (TextView) itemView.findViewById(R.id.address);
            likeNum = (TextView) itemView.findViewById(R.id.like_num);
            comNum = (TextView) itemView.findViewById(R.id.comment_num);
            distance = (TextView) itemView.findViewById(R.id.distance);
            rate = (TextView) itemView.findViewById(R.id.rate);
        }
    }
}
