package atos.net.interestingplaces.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import atos.net.interestingplaces.R;
import atos.net.interestingplaces.pojo.PlaceOfInterest;

/**
 * Created by a551481 on 03-08-2015.
 */
public class POIListAdapter extends BaseAdapter implements Filterable {

    private Context               mContext;
    private List<PlaceOfInterest> mFilteredList;
    private List<PlaceOfInterest> mOriginalList;

    /**
     * <p>Returns a filter that can be used to constrain data with a filtering
     * pattern.</p>
     * <p/>
     * <p>This method is usually implemented by {@link Adapter}
     * classes.</p>
     *
     * @return a filter used to constrain data
     */
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(final CharSequence constraint) {
                mFilteredList = filterList(constraint);
                FilterResults         filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                filterResults.count = mFilteredList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(final CharSequence constraint, final FilterResults results) {
                mFilteredList = (ArrayList<PlaceOfInterest>) results.values;
                POIListAdapter.this.notifyDataSetChanged();
            }

            private List<PlaceOfInterest> filterList(CharSequence charSequence){
                if(0 == charSequence.length()){
                    return mOriginalList;
                }
                List<PlaceOfInterest> filteredList = new ArrayList<>();
                for(PlaceOfInterest placeOfInterest : mFilteredList){
                    String title = placeOfInterest.getTitle().toLowerCase();
                    if(title.contains(charSequence.toString().toLowerCase())){
                        filteredList.add(placeOfInterest);
                    }
                }
                return  filteredList;
            }
        };


        return filter;
    }

    private class ViewHolder {
        public TextView tv_title;
    }

    public POIListAdapter(Context context,List<PlaceOfInterest> list) {
        mContext = context;
        mFilteredList = list;
    }

    public void setFilteredList(final List<PlaceOfInterest> filteredList) {
        mFilteredList = filteredList;
        /**
         * Save the original list.
         */
        mOriginalList = filteredList;
    }

    public List<PlaceOfInterest> getFilteredList() {
        return mFilteredList;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        int count = 0;
        if(null != mFilteredList){
            count = mFilteredList.size();
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
        if(null != mFilteredList){
            obj = mFilteredList.get(position);
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
        if(null != mFilteredList){
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
