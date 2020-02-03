package com.project.dreamsquad.trackmykid.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.others.PrefManager;

/**
 * Created by a_man on 26-03-2018.
 */

public class CreateProfileFragment extends DialogFragment {
    private PrefManager pref;
    private CreateProfileDismiss createProfileDismiss;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View promptsView = inflater.inflate(R.layout.profile_new_dialog_layout, container);
        ImageView edit = (ImageView) promptsView.findViewById(R.id.edit);
        final EditText name = (EditText) promptsView.findViewById(R.id.name);
        final EditText relation = (EditText) promptsView.findViewById(R.id.relation);
        final EditText contact = (EditText) promptsView.findViewById(R.id.contact_number);
        final EditText mail = (EditText) promptsView.findViewById(R.id.email_add);
        pref = new PrefManager(getContext());
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.setPName(name.getText().toString());
                pref.setRelation(relation.getText().toString());
                pref.setPContact(contact.getText().toString());
                pref.setEmail(mail.getText().toString());
                dismiss();
            }
        });
        return promptsView;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        createProfileDismiss.onDismiss();
        super.onDismiss(dialog);
    }

    public static CreateProfileFragment newInstance(CreateProfileDismiss createProfileDismiss) {
        CreateProfileFragment fragment = new CreateProfileFragment();
        fragment.createProfileDismiss = createProfileDismiss;
        return fragment;
    }

    public interface CreateProfileDismiss {
        void onDismiss();
    }

}
