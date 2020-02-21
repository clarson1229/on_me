package com.connorlarson.onme.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.connorlarson.onme.R;

public class ProfileFragment extends Fragment {

    private ProfileViewModal ProfileViewModal;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModal =
                ViewModelProviders.of(this).get(ProfileViewModal.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView textView = root.findViewById(R.id.text_profile);
        final TextView profileName = root.findViewById(R.id.text_profile_name);
        final TextView profileEmail = root.findViewById(R.id.text_profile_email);

        ProfileViewModal.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        ProfileViewModal.getProfileName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                profileName.setText(s);
            }
        });
        ProfileViewModal.getProfileEmail().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                profileEmail.setText(s);
            }
        });

        return root;
    }

//    Todo make data base calls to propigate the scroll views with data
//    Todo set up the scrollviews so they can recieve data.
}