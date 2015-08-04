package atos.net.interestingplaces.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import atos.net.interestingplaces.R;
import atos.net.interestingplaces.pojo.PlaceOfInterest;

/**
 * Created by a551481 on 03-08-2015.
 */
public class POIListAdapter extends BaseAdapter {

    private Context mContext;
    private List<PlaceOfInterest> mList;

    private class ViewHolder {
        public TextView tv_title;
    }

    public POIListAdapter(Context context,List<PlaceOfInterest> list) {
        mContext = context;
        mList = list;
    }

    public void setList(final List<PlaceOfInterest> list) {
        mList = list;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        int count = 0;
        if(null != mList){
            count = mList.size();
        }
        return count;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     *
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(final int position) {
        PlaceOfInterest obj = null;
        if(null != mList){
            obj = mList.get(position);
        }
        return obj;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     *
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(final int position) {
        /* int id = -1;
        if(null != mList){
            PlaceOfInterest obj = (PlaceOfInterest) getItem(position);
            id = obj.getId();
        }*/
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     *
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        View view = convertView;
        PlaceOfInterest placeOfInterest = null;
        if(null == convertView){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.list_row,null,false);
            holder = new ViewHolder();
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        placeOfInterest = (PlaceOfInterest) getItem(position);
        holder.tv_title.setText(placeOfInterest.getTitle());

        return view;
    }
}
