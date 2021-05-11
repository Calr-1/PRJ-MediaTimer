package com.example.timer_10;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;


public class timer_group extends Fragment {

    private TimersWrapper wrapper;
    private ConstraintLayout layout;
    private TimerGroup group;
    private TimerGroup associatedGroup;
    private int type;


    public timer_group(int type) {
        this.type = type;
    }

    public timer_group(int type, TimerGroup associatedGroup) {
        this.type = type;
        this.associatedGroup = associatedGroup;
    }

    public timer_group(int type, TimerGroup group, TimerGroup associatedGroup) {
        this.type = type;
        this.group = group;
        this.associatedGroup = associatedGroup;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wrapper = TimersWrapper.getInstance();

        if (type == 1) {
            group = new TimerGroup("Timer group " + (wrapper.getGroupsOfTimers().size() + 1));
            wrapper.addGroupOfTimers(group);
        }
        if (type == 2) {
            group = new TimerGroup("Timer group " + (associatedGroup.getNumberOfGroups() + 1));
            associatedGroup.addTimerGroup(group);
        }

        updateName();

        layout = getView().findViewById(R.id.frameLayout);

        layout.setOnClickListener(v -> {
            int index = wrapper.getIndexOfGroupOfTimers(group);
            Intent intent = new Intent(getActivity(), group.class);
            intent.putExtra("groupIndex", index);
            startActivityForResult(intent, 1);

        });
    }

    private void updateName() {
        TextView tv = getView().findViewById(R.id.timerFragName);
        tv.setText(group.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                updateName();

            }
        }
    }
}