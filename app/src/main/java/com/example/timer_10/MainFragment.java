package com.example.timer_10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class MainFragment extends Fragment {

    private Button addButton;
    private TimersWrapper wrapper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        wrapper = TimersWrapper.getInstance();
        addButton = getView().findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> addFragment());
    }

    private void addFragment() {

        timerFragment frag = new timerFragment(1);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.timerLayout, frag)
                .commit();
        wrapper.addTimerFragment(frag);

    }
}