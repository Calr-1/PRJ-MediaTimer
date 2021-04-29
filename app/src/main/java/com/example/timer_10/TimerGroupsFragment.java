package com.example.timer_10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;


public class TimerGroupsFragment extends Fragment implements Serializable {

    private TimersWrapper wrapper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_timer_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        wrapper = TimersWrapper.getInstance();

        ImageButton addGroupButton = getView().findViewById(R.id.addGroupButton);
        TextView loadTimers = getView().findViewById(R.id.textView);

        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createGroup()) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Group created!", Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Failed creating the group... Make sure the name is chosen.", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }

    public boolean createGroup() {
        TextView nameTv = (TextView) getView().findViewById(R.id.addGroupName);
        String groupName = nameTv.getText().toString();
        if (!groupName.isEmpty()) {
            TimerGroup group = new TimerGroup(groupName);
            addGroupToUi(group);
            wrapper.addGroupOfTimers(group);
            return true;
        }
        return false;
    }

    public void addGroupToUi(TimerGroup group) {

        LinearLayout groupLayout = getView().findViewById(R.id.groupsLinearLayout);
        TextView tv = new TextView(getView().getContext());
        String viewableText = group.getName() + " - (" + group.getNumberOfTimers() + ")";
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp2.setMargins(5, 5, 5, 5);
        tv.setLayoutParams(lp2);
        tv.setTextSize(30f);
        tv.setTextColor(ContextCompat.getColor(getView().getContext(), R.color.black));
        tv.setText(viewableText);
        tv.setBackground(
                getResources().getDrawable(R.drawable.top_round_corners_no_border));
        tv.setTextColor(ContextCompat.getColor(getView().getContext(), R.color.palette_blue));
        groupLayout.addView(tv);


    }


}