package com.example.flashcard.adapter;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcard.R;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    private List<Pair<String, String>> topics = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<String, String> flashcard = topics.get(position);
        holder.bind(flashcard);

        holder.removeVocabularyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topics.remove(flashcard);
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public void addFlashcard() {
        topics.add(new Pair<>("", ""));
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText editTextVocabulary;
        private EditText editTextMeaning;
        private ImageButton removeVocabularyBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextVocabulary = itemView.findViewById(R.id.editTextVocabulary);
            editTextMeaning = itemView.findViewById(R.id.editTextMeaning);
            removeVocabularyBtn = itemView.findViewById(R.id.removeVocabularyBtn);
        }

        public void bind(Pair<String, String> flashcard) {
            editTextVocabulary.setText(flashcard.first);
            editTextMeaning.setText(flashcard.second);
        }
    }
}