package com.example.firebaseexampleapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class AllPostsAdapter extends RecyclerView.Adapter<AllPostsAdapter.ViewHolder> {

    private ArrayList<Post> postArrayList;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        public final TextView tvPostTitle;
        public final TextView tvPostData;
        public final ImageView ivPostPhoto;
        public final ConstraintLayout mainRow;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            tvPostTitle = view.findViewById(R.id.tvPostName);
            tvPostData = view.findViewById(R.id.tvPostsData);
            ivPostPhoto = view.findViewById(R.id.ivPostPhoto);
            mainRow = view.findViewById(R.id.mainRow);

        }

        @Override
        public boolean onLongClick(View view) {

            postArrayList.remove(getAdapterPosition());
            notifyDataSetChanged();
            return true;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(itemView.getContext(), "Click "+ getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }
    }

    private FBStorage fbStorage;
    public AllPostsAdapter(ArrayList<Post> dataSet) {
        postArrayList = dataSet;
         fbStorage = new FBStorage();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.rv_row_layout, viewGroup, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder,  int position)
    {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Post p = postArrayList.get(position);
        viewHolder.tvPostTitle.setText(p.getTitle());
        viewHolder.tvPostData.setText(p.getBody());

        // get the image from the firebase storage
        // can use also picasso - very convenient
        fbStorage.downloadImageFromStorage(viewHolder.ivPostPhoto, p);



        //viewHolder.ivPostPhoto.setImageResource(R.drawable.n1);
        if(position%2==0)
            viewHolder.mainRow.setBackgroundColor(Color.parseColor("#4CAF50"));
        else
            viewHolder.mainRow.setBackgroundColor(Color.parseColor("#FFC8E4A9"));

        viewHolder.getAdapterPosition();
        /*viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Click "+viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return postArrayList.size();
    }
}

