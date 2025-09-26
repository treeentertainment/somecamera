
package tech.treeentertainment.camera;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PropertyAdapter<T> extends BaseAdapter {

    private final int imageViewResourceId;
    private final T[] items;
    private final LayoutInflater mInflater;
    private int currentPosition;
    private int selectedBackgroundColor;

    public PropertyAdapter(Context context, T[] images) {
        this(context, 0, images);
    }

    public PropertyAdapter(Context context, int imageViewResourceId, T[] images) {
        this.imageViewResourceId = imageViewResourceId;
        this.items = images;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        currentPosition = -1;
        selectedBackgroundColor = context.getResources().getColor(R.color.primaryColor);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        View subView = null;

        if (convertView == null) {
            view = mInflater.inflate(imageViewResourceId, parent, false);
        } else {
            view = convertView;
        }

        T item = items[position];

        try {
            if (item instanceof CharSequence) {
                subView = view.findViewById(android.R.id.text1);
                ((TextView) subView).setText(item.toString());
            } else if (item instanceof Drawable) {
                subView = view.findViewById(android.R.id.icon);
                ((ImageView) subView).setImageDrawable((Drawable) item);
            }
        } catch (ClassCastException e) {
            Log.e("ImageAdapter", "You must supply a resource ID for a ImageView");
            throw new IllegalStateException("ImageAdapter requires the resource ID to be a ImageView", e);
        }

        if (position == currentPosition) {
            view.setBackgroundColor(selectedBackgroundColor);
        }

        return view;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == currentPosition ? 1 : 0;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        notifyDataSetInvalidated();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
