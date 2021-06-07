package com.example.timer_10;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private TimersWrapper wrapper;
    private LinearLayout originalLayout;
    private int id;
    private View.OnDragListener onDragEventListener;
    private ArrayList<LinearLayout> layouts;
    private int carlos;

    @Override
    public void onStart() {
        super.onStart();
        Log.e("xpto", String.valueOf(wrapper.load));
        if(carlos!=wrapper.timers.size()) {
            loadExistingTimers();
            wrapper.load = false;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wrapper = TimersWrapper.getInstance();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TimersWrapper.loadTheme(this);
        setContentView(R.layout.activity_main);
        registerForContextMenu(findViewById(R.id.addButton));

        layouts = new ArrayList<LinearLayout>();
        originalLayout = findViewById(R.id.timerLayout);
        addTimerLine();
        id = 1;
        carlos = 0;
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Log.d("ENTROU 1",String.valueOf(item.getItemId()));

                        switch (item.getItemId()) {
                            case R.id.favouriteButton:
                                break;
                            case R.id.addButton:
                                findViewById(R.id.addButton).showContextMenu();
                                break;
                            case R.id.optionsButton:
                                wrapper.load = true;
                                Intent intent = new Intent(MainActivity.this, ChangeAppThemeActivity.class);
                                startActivityForResult(intent, 1);
                                break;
                        }
                        return false;
                    }
                });

        bottomNav.setOnNavigationItemReselectedListener(
                new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected(@NonNull MenuItem item) {
                        Log.d("ENTROU 2",String.valueOf(item.getItemId()));

                        switch (item.getItemId()) {
                            case R.id.favouriteButton:
                                break;
                            case R.id.addButton:
                                findViewById(R.id.addButton).showContextMenu();
                                break;
                            case R.id.optionsButton:
                                Intent intent = new Intent(MainActivity.this, ChangeAppThemeActivity.class);
                                startActivityForResult(intent, 1);
                                break;
                        }
                    }
                });



        onDragEventListener = new View.OnDragListener() {

            @Override
            public boolean onDrag(View view, DragEvent event) {
                final int action = event.getAction();
                String dropped = "NO";
                ArrayList<Timer> timers = wrapper.getTimers();

                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        //can handle the data
                        return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
                    case DragEvent.ACTION_DRAG_LOCATION:
                        return true;
                    case DragEvent.ACTION_DRAG_ENTERED://when the view enters the boundries
                    case DragEvent.ACTION_DRAG_EXITED://when the view leaves the boundries
                        return true;
                    case DragEvent.ACTION_DROP:
                        ClipData.Item item = event.getClipData().getItemAt(0);
                        String dragData = item.getText().toString();
                        //Toast.makeText(getApplicationContext(), dragData, Toast.LENGTH_SHORT).show();

                        view.invalidate();
                        LinearLayout owner = (LinearLayout) event.getLocalState();
                        View v = (View) owner.getChildAt(0);
                        Toast.makeText(getApplicationContext(), "Dragged from " + owner.getId(), Toast.LENGTH_SHORT).show();
                        int timer1index = owner.getId();
                        Timer timer1 = wrapper.getTimerFragmentByIndex(timer1index - 1);

                        LinearLayout destination = (LinearLayout) view;
                        Toast.makeText(getApplicationContext(), "Dropped at " + destination.getId(), Toast.LENGTH_SHORT).show();
                        int timer2index = destination.getId();
                        Timer timer2 = wrapper.getTimerFragmentByIndex(timer2index - 1);
                        View destinationView = destination.getChildAt(0);
                        destination.removeView(destinationView);
                        owner.removeView(v);
                        destination.addView(v);
                        owner.addView(destinationView);
                        dropped = "YES";
                        v.setOnLongClickListener(new View.OnLongClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public boolean onLongClick(View view) {
                                String clipText = "This is our clipData text";
                                ClipData.Item item = new ClipData.Item(clipText);
                                String[] mimeTypes;
                                mimeTypes = new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN};
                                ClipData data = new ClipData(clipText, mimeTypes, item);

                                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(destination);
                                destination.startDragAndDrop(data, dragShadowBuilder, destination, 0);
                                destination.setVisibility(View.INVISIBLE);
                                return true;
                            }
                        });
                        destinationView.setOnLongClickListener(new View.OnLongClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public boolean onLongClick(View view) {
                                String clipText = "This is our clipData text";
                                ClipData.Item item = new ClipData.Item(clipText);
                                String[] mimeTypes;
                                mimeTypes = new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN};
                                ClipData data = new ClipData(clipText, mimeTypes, item);

                                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(owner);
                                owner.startDragAndDrop(data, dragShadowBuilder, owner, 0);
                                owner.setVisibility(View.INVISIBLE);
                                return true;
                            }
                        });
                        timers.set(timer1index - 1, timer2);
                        timers.set(timer2index - 1, timer1);
                        //v.setVisibility(View.VISIBLE);
                        //destinationView.setVisibility(View.VISIBLE);
                        //owner.setVisibility(View.VISIBLE);
                        //destination.setVisibility(View.VISIBLE);
                        //owner.setOnDragListener(onDragEventListener);
                        //destination.setOnDragListener(onDragEventListener);
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        view.invalidate();
                        if (dropped.equals("NO")) {
                            owner = (LinearLayout) event.getLocalState();
                            destination = (LinearLayout) view;
                            timer1index = owner.getId();
                            timer2index = destination.getId();
                            timer1 = wrapper.getTimerFragmentByIndex(timer1index - 1);
                            timer2 = wrapper.getTimerFragmentByIndex(timer2index - 1);

                            timers.set(timer1index - 1, timer1);
                            timers.set(timer2index - 1, timer2);

                            owner.setVisibility(View.VISIBLE);
                            destination.setVisibility(View.VISIBLE);
                            owner.setOnDragListener(onDragEventListener);
                            destination.setOnDragListener(onDragEventListener);
                        }
                        wrapper.setTimers(timers);
                        return true;
                    default:
                        break;
                }
                return false;
            }

        };
    }

    private void addTimerLine() {
        View child = getLayoutInflater().inflate(R.layout.table_line_timers, null);
        originalLayout.addView(child);
    }

    private void loadExistingTimers() {
        int timerIdx = 0;
        int groupIdx = 0;
        for (int index = 0; index < wrapper.getTimers().size(); index++) {
            Log.e("123", String.valueOf(wrapper.load));
            Timer frag = wrapper.getTimers().get(index);
            int type = frag.getType();
            if(type == 1){
                TimerFragment fragmento = new TimerFragment(4, ((TimerFragment)frag).getTimerClass());
                wrapper.setSpecificIndividualTimerFragment(timerIdx,fragmento);
                addTimerFragment(fragmento);
                timerIdx++;
            }
            if(type == 2){
                TimerGroupFragment fragmento = new TimerGroupFragment(4, ((TimerGroupFragment)frag).getTimerGroupClass());
                wrapper.setGroupOfTimersFragment(groupIdx, fragmento);
                addGroupFragment(fragmento);
                groupIdx++;
            }

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.choose_type_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_timer_option:
                addTimerFragment(null);
                return true;
            case R.id.create_group_option:
                addGroupFragment(null);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    private void addTimerFragment(TimerFragment frag) {

        int lines = originalLayout.getChildCount();
        LinearLayout og = (LinearLayout) ((LinearLayout) originalLayout.getChildAt(lines - 1)).getChildAt(0);
        if (og.getChildAt(0).getVisibility() == View.INVISIBLE) {
            addToLayout(og, 0, frag, false);
        } else if (og.getChildAt(1).getVisibility() == View.INVISIBLE) {
            addToLayout(og, 1, frag, false);
        } else {
            addTimerLine();
            addTimerFragment(frag);
        }
    }

    private void addGroupFragment(TimerGroupFragment frag) {
        int lines = originalLayout.getChildCount();
        LinearLayout og = (LinearLayout) ((LinearLayout) originalLayout.getChildAt(lines - 1)).getChildAt(0);
        if (og.getChildAt(0).getVisibility() == View.INVISIBLE) {
            addToLayout(og, 0, frag, true);
        } else if (og.getChildAt(1).getVisibility() == View.INVISIBLE) {
            addToLayout(og, 1, frag, true);
        } else {
            addTimerLine();
            addGroupFragment(frag);
        }

    }

    private void addToLayout(LinearLayout og, int index, Timer frag, boolean group){
        LinearLayout layout = (LinearLayout) og.getChildAt(index);
        layout.setVisibility(View.VISIBLE);
        layout.setId(id);
        id++;
        if(frag == null){
            if(group){
                frag = new TimerGroupFragment(1);
                wrapper.addTimerGroupFragment((TimerGroupFragment) frag);
            }
            else{
                frag = new TimerFragment(1);
                wrapper.addTimerFragment((TimerFragment) frag);
            }
        }
        getSupportFragmentManager().beginTransaction()
                .add(layout.getId(), (Fragment) frag) /////////////////
                .commitNow();
        ConstraintLayout f = (ConstraintLayout) layout.getChildAt(0);
        f.setTag("Fragment" + id);
        f.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onLongClick(View view) {
                String clipText = "This is our clipData text";
                ClipData.Item item = new ClipData.Item(clipText);
                String[] mimeTypes;
                mimeTypes = new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData data = new ClipData(clipText, mimeTypes, item);

                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(layout);
                layout.startDragAndDrop(data, dragShadowBuilder, layout, 0);
                layout.setVisibility(View.INVISIBLE);
                return true;
            }
        });
        layout.setOnDragListener(onDragEventListener);
        layouts.add(layout);
        carlos++;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                wrapper.load = true;
                Log.e("load", String.valueOf(wrapper.load));
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }

}
