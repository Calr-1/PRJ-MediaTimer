package com.example.timer_10;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ImageButton addButton;
    private TimersWrapper wrapper;
    private LinearLayout originalLayout;
    private int id;
    private View.OnDragListener onDragEventListener;

    private ArrayList<LinearLayout> layouts;
    private int totalSize;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        totalSize=0;
        wrapper = TimersWrapper.getInstance();

        ArrayList<TimerClass> savedTimer = TimersWrapper.getSavedObjectFromPreference(this.getApplicationContext(), "wrapperPref", "wrapper");
        if(savedTimer!=null){
            wrapper.timer=savedTimer;
        }
        TimersWrapper.loadTheme(this);
        setContentView(R.layout.activity_main);


        registerForContextMenu(findViewById(R.id.addButton));

        layouts = new ArrayList<>();
        originalLayout = findViewById(R.id.timerLayout);
        addTimerLine();
        id = 1;


        onDragEventListener = (view, event) -> {
            final int action = event.getAction();
            String dropped = "NO";
            ArrayList<TimerFragment> timers = wrapper.getTimerFragments();

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //can handle the data
                    return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED://when the view enters the boundaries
                case DragEvent.ACTION_DRAG_EXITED://when the view leaves the boundaries
                    return true;
                case DragEvent.ACTION_DROP:
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    //String dragData = item.getText().toString();
                    //Toast.makeText(getApplicationContext(), dragData, Toast.LENGTH_SHORT).show();

                    view.invalidate();

                    LinearLayout owner = (LinearLayout) event.getLocalState();
                    View v = (View) owner.getChildAt(0);
                    Toast.makeText(getApplicationContext(), "Dragged from " + owner.getId(), Toast.LENGTH_SHORT).show();
                    int timer1index = owner.getId();
                    TimerFragment timer1 = wrapper.getTimerFragmentByIndex(timer1index - 1);

                    LinearLayout destination = (LinearLayout) view;
                    Toast.makeText(getApplicationContext(), "Dropped at " + destination.getId(), Toast.LENGTH_SHORT).show();
                    int timer2index = destination.getId();
                    TimerFragment timer2 = wrapper.getTimerFragmentByIndex(timer2index - 1);
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
                    wrapper.setTimerFragments(timers);
                    return true;
                default:
                    break;
            }
            return false;
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(totalSize!=wrapper.timer.size()){
            loadExistingTimers();}
    }

    private void addTimerLine() {
        View child = getLayoutInflater().inflate(R.layout.table_line_timers, null);
        originalLayout.addView(child);
    }

    private void loadExistingTimers() {

        for(int i = 0; i<wrapper.timer.size();i++) {

            int lines = originalLayout.getChildCount();
            LinearLayout og = (LinearLayout) ((LinearLayout) originalLayout.getChildAt(lines - 1)).getChildAt(0);

            if (og.getChildAt(0).getVisibility() == View.INVISIBLE) {
                LinearLayout layout = (LinearLayout) og.getChildAt(0);
                layout.setVisibility(View.VISIBLE);
                TimerFragment frag = new TimerFragment(5, wrapper.timer.get(i), "test");
                wrapper.addTimerFragment(frag);
                layout.setId(id);
                id++;
                getSupportFragmentManager().beginTransaction()
                        .add(layout.getId(), frag)
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
                totalSize++;

            } else if (og.getChildAt(1).getVisibility() == View.INVISIBLE) {
                LinearLayout layout = (LinearLayout) og.getChildAt(1);
                layout.setVisibility(View.VISIBLE);
                TimerFragment frag = new TimerFragment(5, wrapper.timer.get(i), "test");
                wrapper.addTimerFragment(frag);
                layout.setId(id);
                id++;
                getSupportFragmentManager().beginTransaction()
                        .add(layout.getId(), frag)
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
                totalSize++;

            } else {
                addTimerLine();
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
                addTimerFragment();
                return true;
            case R.id.create_group_option:
                addGroupFragment();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options_menu, menu);
        return true;
    }
    private void addTimerFragment() {

        int lines = originalLayout.getChildCount();
        LinearLayout og = (LinearLayout) ((LinearLayout) originalLayout.getChildAt(lines - 1)).getChildAt(0);

        if (og.getChildAt(0).getVisibility() == View.INVISIBLE) {
            LinearLayout layout = (LinearLayout) og.getChildAt(0);
            layout.setVisibility(View.VISIBLE);
            TimerFragment frag = new TimerFragment(1);
            wrapper.addTimerFragment(frag);
            layout.setId(id);
            id++;
            getSupportFragmentManager().beginTransaction()
                    .add(layout.getId(), frag)
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
            wrapper.timer.add((frag).getTimerClass());
            TimersWrapper.saveObjectToSharedPreference(this.getApplicationContext(),"wrapperPref", "wrapper", wrapper.timer);
            totalSize++;


        } else if (og.getChildAt(1).getVisibility() == View.INVISIBLE) {
            LinearLayout layout = (LinearLayout) og.getChildAt(1);
            layout.setVisibility(View.VISIBLE);
            TimerFragment frag = new TimerFragment(1);
            wrapper.addTimerFragment(frag);
            layout.setId(id);
            id++;
            getSupportFragmentManager().beginTransaction()
                    .add(layout.getId(), frag)
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
            wrapper.timer.add((frag).getTimerClass());
            TimersWrapper.saveObjectToSharedPreference(this.getApplicationContext(),"wrapperPref", "wrapper", wrapper.timer);
            totalSize++;

        } else {
            addTimerLine();
            addTimerFragment();
        }

    }

    private void addGroupFragment() {
        int lines = originalLayout.getChildCount();
        LinearLayout og = (LinearLayout) ((LinearLayout) originalLayout.getChildAt(lines - 1)).getChildAt(0);
        if (og.getChildAt(0).getVisibility() == View.INVISIBLE) {
            LinearLayout layout = (LinearLayout) og.getChildAt(0);
            layout.setVisibility(View.VISIBLE);
            TimerGroupFragment frag = new TimerGroupFragment(1);
            wrapper.addTimerGroupFragment( frag);
            layout.setId(id);
            id++;
            getSupportFragmentManager().beginTransaction()
                    .add(layout.getId(), frag)
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
        } else if (og.getChildAt(1).getVisibility() == View.INVISIBLE) {
            LinearLayout layout = (LinearLayout) og.getChildAt(1);
            layout.setVisibility(View.VISIBLE);
            TimerGroupFragment frag = new TimerGroupFragment(1);
            wrapper.addTimerGroupFragment( frag);
            layout.setId(id);
            id++;
            getSupportFragmentManager().beginTransaction()
                    .add(layout.getId(), frag)
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
            //wrapper.timer.add(((TimerGroupFragment)frag).getGroup());
            TimersWrapper.saveObjectToSharedPreference(this.getApplicationContext(),"wrapperPref", "wrapper", wrapper.timer);
        } else {
            addTimerLine();
            addGroupFragment();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }


}
