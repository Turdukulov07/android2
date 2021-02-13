package com.geektech.noteapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geektech.noteapp.models.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context context;
    public ArrayList<Note> list;
    private OnItemClickListener onItemClickListener;
    private Note note;

    public NoteAdapter(Context context) {
        this.context = context;
        // then, here the miracle happened
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_note, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void addItem(Note note) {
        this.note = note;
        list.add(note);
        notifyItemInserted(list.indexOf(note));
    }

    public void setList(List<Note> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public Note getItem(int position) {
        return list.get(position);
    }

    public void updateItem(int pos, Note note) {
        list.set(pos, note);
        notifyItemChanged(pos);
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle, textDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v ->
                    onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnLongClickListener(v -> {
                onItemClickListener.onLongClick(getAdapterPosition());
                return true;
            });
            textTitle = itemView.findViewById(R.id.text_title);
            textDate = itemView.findViewById(R.id.text_date);
        }

        public void bind(Note note) {
            textDate.setText(note.getDate());
            textTitle.setText(note.getTitle());
        }
    }
}
