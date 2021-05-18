package com.example.timer_10;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wrapper = TimersWrapper.getInstance();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TimersWrapper.loadTheme(this);
        setContentView(R.layout.activity_main);
        addButton = findViewById(R.id.addButton);
        registerForContextMenu(addButton);
        addButton.setOnClickListener(v -> addButton.showContextMenu());
        addButton.setOnLongClickListener(v -> true);

        loadExistingTimers();

        originalLayout = findViewById(R.id.timerLayout);
        addTimerLine();

        id = 1;

        onDragEventListener = new View.OnDragListener() {

            @Override
            public boolean onDrag(View view, DragEvent event) {
                final int action = event.getAction();

                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED://can handle the data
                        return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
                    case DragEvent.ACTION_DRAG_ENTERED://when the view enters the boundries
                        view.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED://when the view leaves the boundries
                        view.invalidate();
                        return true;
                    case DragEvent.ACTION_DROP:
                        ClipData.Item item = event.getClipData().getItemAt(0);
                        String dragData = item.getText().toString();
                        Toast.makeText(getApplicationContext(), dragData, Toast.LENGTH_SHORT).show();

                        view.invalidate();

                        View v = (View) event.getLocalState();
                        LinearLayout owner = (LinearLayout) v.getParent();
                        owner.removeView(v);


                        ConstraintLayout destination_child = (ConstraintLayout) view;
                        LinearLayout destination = (LinearLayout) destination_child.getParent();
                        Toast.makeText(getApplicationContext(), destination.getId()+"", Toast.LENGTH_SHORT).show();
                        //LinearLayout destination = (LinearLayout) view;
                        View destinationView = destination.getChildAt(0);
                        destination.removeView(destinationView);
                        owner.addView(destinationView);
                        destination.addView(v);
                        v.setVisibility(View.VISIBLE);
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        view.invalidate();
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

        for (int index = 0; index < wrapper.getTimerFragments().size(); index++) {
            TimerFragment frag = new TimerFragment(4, wrapper.getSpecificIndividualTimerByIndex(index));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.timerLayout, frag)
                    .commit();
            wrapper.setSpecificIndividualTimerFragment(index, frag);
        }
        for (int index = 0; index < wrapper.getGroupsOfTimersFragment().size(); index++) {
            TimerGroupFragment group = wrapper.getGroupsOfTimersFragmentByIndex(index);
            if (group.getAssociatedGroup() == null) {
                TimerGroupFragment frag = new TimerGroupFragment(4, wrapper.getGroupsOfTimers().get(index));
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.timerLayout, frag)
                        .commit();
                wrapper.setGroupOfTimersFragment(index, frag);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.change_theme_option) {

            Intent intent = new Intent(this, ChangeAppThemeActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            frag.getLayout().setOnLongClickListener(new View.OnLongClickListener() {
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
            frag.getLayout().setOnLongClickListener(new View.OnLongClickListener() {
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
            frag.getLayout().setOnDragListener(onDragEventListener);
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
            wrapper.addTimerGroupFragment(frag);
            layout.setId(id);
            id++;
            getSupportFragmentManager().beginTransaction()
                    .add(layout.getId(), frag)
                    .commitNow();
            frag.getLayout().setOnLongClickListener(new View.OnLongClickListener() {
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
            frag.getLayout().setOnDragListener(onDragEventListener);
        } else if (og.getChildAt(1).getVisibility() == View.INVISIBLE) {
            LinearLayout layout = (LinearLayout) og.getChildAt(1);
            layout.setVisibility(View.VISIBLE);
            TimerGroupFragment frag = new TimerGroupFragment(1);
            wrapper.addTimerGroupFragment(frag);
            layout.setId(id);
            id++;
            getSupportFragmentManager().beginTransaction()
                    .add(layout.getId(), frag)
                    .commitNow();
            frag.getLayout().setOnLongClickListener(new View.OnLongClickListener() {
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
            frag.getLayout().setOnDragListener(onDragEventListener);
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
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }

}
