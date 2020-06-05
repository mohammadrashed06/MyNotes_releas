package com.example.mynotes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder>  {
    private Context context;
    private List<Note> notesList;


    public NoteAdapter(Context context, List<Note> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_row,parent,false);
        return new NoteHolder(row);
    }

    @Override
    public void onBindViewHolder(final NoteHolder holder, int position) {
        final Note note = notesList.get(position);
        holder.noteTitleTv.setText(note.getNoteTitle());
        holder.noteContentTv.setText(note.getNoteContent());
        holder.noteContentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2= new Intent(context,AddNoteActivity.class);
                intent2.putExtra("id",note.getId());
                context.startActivity(intent2);
            }
        });
        holder.noteTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AddNoteActivity.class);
                intent.putExtra("id",note.getId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    static class NoteHolder extends RecyclerView.ViewHolder{
        TextView noteTitleTv;
        TextView noteContentTv;
        public NoteHolder(View itemView) {
            super(itemView);
            noteTitleTv =  itemView.findViewById(R.id.Note_View);
            noteContentTv = itemView.findViewById(R.id.Content_View);

        }


    }
}
