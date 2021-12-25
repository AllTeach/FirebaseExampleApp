package com.example.firebaseexampleapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseUIPostAdapter extends FirestoreRecyclerAdapter<Post,FirebaseUIPostAdapter.ViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private FBStorage fbStorage;
    private onViewClickListener clickListener;

    public interface onViewClickListener
    {
        public void onViewClicked(DocumentSnapshot documentSnapshot,int position);
    }

    public FirebaseUIPostAdapter(@NonNull FirestoreRecyclerOptions<Post> options) {
        super(options);
        fbStorage = new FBStorage();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public final TextView tvPostTitle;
        public final TextView tvPostData;
        public final ImageView ivPostPhoto;
        public final ConstraintLayout mainRow;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
         //   view.setOnClickListener(this);
         //   view.setOnLongClickListener(this);
            tvPostTitle = view.findViewById(R.id.tvPostName);
            tvPostData = view.findViewById(R.id.tvPostsData);
            ivPostPhoto = view.findViewById(R.id.ivPostPhoto);
            mainRow = view.findViewById(R.id.mainRow);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && clickListener!=null)
                        clickListener.onViewClicked(getSnapshots().getSnapshot(position),position);
                }
            });

        }


    }
    @Override
    protected void onBindViewHolder(@NonNull FirebaseUIPostAdapter.ViewHolder holder, int position, @NonNull Post model) {
// Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.tvPostTitle.setText(model.getTitle());
        holder.tvPostData.setText(model.getBody());

        // get the image from the firebase storage
        // can use also picasso - very convenient
        fbStorage.downloadImageFromStorage(holder.ivPostPhoto, model);

        holder.mainRow.setBackgroundColor(Color.parseColor("#4CAF50"));




    }
    public void deleteItem(int position)
    {
        // get Document reference and delete item
        DocumentReference ref = getSnapshots().getSnapshot(position).getReference();
        ref.delete();

        // no need for notify data changed - Recycler View adapter handles it all
    }

    public void setViewClickedListener(onViewClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FirebaseUIPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.rv_row_layout, parent, false);

        // Return a new holder instance
      //  AllPostsAdapter.ViewHolder viewHolder = new FirebaseUIPostAdapter.ViewHolder(contactView);
        return new ViewHolder(contactView);
    }
}
