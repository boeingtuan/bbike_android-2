package iamhuy.bbike;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Nguyen on 11/10/2015.
 */
public class ListLocationAdapter {// extends RecyclerView.Adapter<ListLocationAdapter.ViewHolder> {

    Context mContext;
    public ListLocationAdapter(Context context) {
        this.mContext = context;
    }

   // @Override
//    public int getItemCount() {
//        return new PlaceData().placeList().size();
//    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout placeHolder;
        public LinearLayout placeNameHolder;
        public TextView placeName;
        public ImageView placeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            placeName = (TextView) itemView.findViewById(R.id.placeName);
            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            placeImage = (ImageView) itemView.findViewById(R.id.placeImage);
        }
    }
}
