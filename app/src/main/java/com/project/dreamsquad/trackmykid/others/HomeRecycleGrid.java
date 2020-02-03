package com.project.dreamsquad.trackmykid.others;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.models.Student;

import java.util.List;

/**
 * Created by this pc on 12-05-17.
 */

public class HomeRecycleGrid extends RecyclerView.Adapter<HomeRecycleGrid.MyHolder> {

    public RecyclerView re;
    private List<Student> dataSet;
    public Context context = null;
    VenueAdapterClickCallbacks venueAdapterClickCallbacks;


    public class MyHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView school;
        TextView status;
        ImageView image;

        public MyHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.student_name);
            this.school = (TextView) itemView.findViewById(R.id.school);
            this.status = (TextView) itemView.findViewById(R.id.status);
            this.image = (ImageView) itemView.findViewById(R.id.student_image);
        }
    }

    public HomeRecycleGrid(Context c, List<Student> data, VenueAdapterClickCallbacks venueAdapterClickCallback) {

        this.dataSet = data;
        this.venueAdapterClickCallbacks = venueAdapterClickCallback;
        context = c;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_home_card, parent, false);
        MyHolder myNewsHolder = new MyHolder(view);
        re = (RecyclerView) parent.findViewById(R.id.card_grid);
        return myNewsHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        TextView name = holder.name;
        TextView school = holder.school;
        TextView status = holder.status;
        ImageView image = holder.image;
        name.setText(dataSet.get(position).getName());


//        String p=dataSet.get(position).urlToImage;
//
//        if(p!=null) {
//            setImageOnCard(context,image,p);
//        }
        school.setText(dataSet.get(position).getSchool());
        status.setText(dataSet.get(position).getStatus());


        Glide.with(context).load(R.drawable.pi)
                .apply(new RequestOptions()
                        .dontAnimate()
                        .centerCrop()
                        .override(500,500)
                        .bitmapTransform(new RoundedCornersTransformation(context, 10, 0, RoundedCornersTransformation.CornerType.TOP))
                        .placeholder(R.drawable.placeholder))
                .into(image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                venueAdapterClickCallbacks.onCardClick(dataSet.get(position).getName());

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface VenueAdapterClickCallbacks {
        void onCardClick(String p);

    }


}
