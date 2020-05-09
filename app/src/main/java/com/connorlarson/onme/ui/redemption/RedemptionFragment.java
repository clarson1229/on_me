package com.connorlarson.onme.ui.redemption;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.connorlarson.onme.MainActivity;
import com.connorlarson.onme.R;

public class RedemptionFragment extends Fragment {

    private static final String TAG = "Redemption Page";
    private View mView;

    // data vars
    private MainActivity activity;
    private String userId;
    private static final int MODAL_REQUEST_CODE = 0;
    private ListView redemptionListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_redemption, container, false);
        redemptionListView = mView.findViewById(R.id.redemptionRecycle);

        activity = (MainActivity) getActivity();
        userId = activity.getUserID();
        init();
        return mView;
    }

    private void init() {
        // todo start here
    }
}