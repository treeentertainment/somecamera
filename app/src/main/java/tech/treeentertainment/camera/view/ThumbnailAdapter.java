package tech.treeentertainment.camera.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tech.treeentertainment.camera.R;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ViewHolder> {

    private final List<Integer> handles = new ArrayList<>(10);
    private final List<Bitmap> thumbnails = new ArrayList<>(10);
    private final List<String> filenames = new ArrayList<>(10);
    private final LayoutInflater inflater;
    private boolean visibleFilename;
    private int maxNumPictures;

    // 클릭 리스너
    public interface OnItemClickListener {
        void onItemClick(int handle);
    }

    private OnItemClickListener listener;

    public ThumbnailAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setShowFilename(boolean visible) {
        this.visibleFilename = visible;
        notifyDataSetChanged();
    }

    public void setMaxNumPictures(int maxNumPictures) {
        this.maxNumPictures = maxNumPictures;
        checkListSizes();
        notifyDataSetChanged();
    }

    private void checkListSizes() {
        while (handles.size() > maxNumPictures) {
            handles.remove(0);
            thumbnails.get(0).recycle();
            thumbnails.remove(0);
            filenames.remove(0);
        }
    }

    public void addFront(int objectHandle, String filename, Bitmap thumbnail) {
        if (maxNumPictures == 0) {
            if (thumbnail != null) thumbnail.recycle();
            return;
        }
        for (Integer i : handles) {
            if (i == objectHandle) return;
        }
        handles.add(objectHandle);
        thumbnails.add(thumbnail);
        filenames.add(filename);
        checkListSizes();
        notifyDataSetChanged();
    }

    public int getItemHandle(int position) {
        return handles.get(handles.size() - 1 - position);
    }

    private Bitmap getItemBitmap(int position) {
        return thumbnails.get(thumbnails.size() - 1 - position);
    }

    private String getItemFilename(int position) {
        return filenames.get(filenames.size() - 1 - position);
    }

    @NonNull
    @Override
    public ThumbnailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.thumbnail_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThumbnailAdapter.ViewHolder holder, int position) {
        holder.image.setImageBitmap(getItemBitmap(position));

        if (visibleFilename) {
            holder.text.setText(getItemFilename(position));
            holder.text.setVisibility(View.VISIBLE);
        } else {
            holder.text.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(getItemHandle(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return handles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image1);
            text = itemView.findViewById(android.R.id.text1);
        }
    }
}
