package com.example.mynotes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    Realm realm;
    FloatingActionButton FAB;
    NoteAdapter adapter;
    RecyclerView recyclerView;
    RealmResults<Note> results;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init realm databeas
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();


        //floating action button settings
        FAB = findViewById(R.id.FAB);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });


        //recyclerview settings

        results = realm.where(Note.class).findAll();
        recyclerView = findViewById(R.id.rv);
        adapter = new NoteAdapter(this, results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        //divider settings
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divide));
        recyclerView.addItemDecoration(dividerItemDecoration);
        onResume();
        enableSwipeToDeleteAndUndo();
        runAnimation(recyclerView,0);




    }

    //animation for recyclerview
    private void runAnimation(RecyclerView recyclerView, int type) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller ;
        if (type==0) {
            controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);
            recyclerView.setLayoutAnimation(controller);
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();

        }



    }
    //swipe to delete

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleatCallback swipeToDeleteCallback = new SwipeToDeleatCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Toast.makeText(getApplicationContext(),"Successfully deleted",Toast.LENGTH_SHORT).show();
                final int position = viewHolder.getAdapterPosition();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        results.get(position).deleteFromRealm();
                    }
                });
                adapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);





    }
    //to refresh recyclerview on save note
    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }


}
