package com.project.dreamsquad.trackmykid.others;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.models.Profile;

import java.util.List;

/**
 * Created by this pc on 16-05-17.
 */

public class ProfileRecycleGrid extends RecyclerView.Adapter<ProfileRecycleGrid.MyHolder> {

    public RecyclerView re;
    private List<Profile> dataSet ;
    public Context context=null;
    AlertDialog alertDialog;

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        TextView relation;
        TextView phone_no;
        TextView email;
        ImageView profile_image;
        ImageView edit;

        public MyHolder(View itemView)
        {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.relation = (TextView) itemView.findViewById(R.id.relation);
            this.phone_no=(TextView)itemView.findViewById(R.id.contact_number);
            this.email=(TextView) itemView.findViewById(R.id.email_add);
            this.profile_image=(ImageView)itemView.findViewById(R.id.img_profile);
            this.edit=(ImageView)itemView.findViewById(R.id.edit);


        }
    }

    public ProfileRecycleGrid(List<Profile> dataSet, Context context) {

        this.dataSet = dataSet;
        this.context = context;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_card, parent, false);
        ProfileRecycleGrid.MyHolder myHolder=new ProfileRecycleGrid.MyHolder(view);
        re = (RecyclerView) parent.findViewById(R.id.profile_grid);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        TextView name = holder.name;
        TextView relation = holder.relation;
        TextView phone_no=holder.phone_no;
        TextView email=holder.email;
        ImageView profile_image=holder.profile_image;
        ImageView edit=holder.edit;
        name.setText(dataSet.get(position).getName());
        relation.setText(dataSet.get(position).getRelation());
        phone_no.setText(dataSet.get(position).getPhone_no());
        email.setText(dataSet.get(position).getEmail());

        Glide.with(context).load(dataSet.get(position).getImage())
                .apply(new RequestOptions()
                        .centerCrop()
                        .override(500,500)
                        .bitmapTransform(new CircleTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .thumbnail(0.5f)

                .into(profile_image);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.profile_new_dialog_layout, null);
                ImageView edit=(ImageView)promptsView.findViewById(R.id.edit);
                final EditText name=(EditText)promptsView.findViewById(R.id.name);
                final EditText relation=(EditText)promptsView.findViewById(R.id.relation);
                final EditText contact=(EditText)promptsView.findViewById(R.id.contact_number);
                final EditText mail=(EditText)promptsView.findViewById(R.id.email_add);
                ImageView img_profile=(ImageView)promptsView.findViewById(R.id.img_profile);
                img_profile.setPadding(0,0,0,0);

                Glide.with(context).load(dataSet.get(position).getImage())
                        .apply(new RequestOptions()
                                .centerCrop()
                                .override(500,500)
                                .bitmapTransform(new CircleTransform(context))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                        )
                        .thumbnail(0.5f)
                        .into(img_profile);


                name.setText(dataSet.get(position).getName());
                relation.setText(dataSet.get(position).getRelation());
                contact.setText(dataSet.get(position).getPhone_no());
                mail.setText(dataSet.get(position).getEmail());


                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                    }
                });

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
                alertDialogBuilder.setView(promptsView);
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
