package com.example.flashcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcard.model.topic.Topic;
import com.example.flashcard.utils.CustomOnItemClickListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.example.flashcard.R;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {
    private Context mContext;
    private List<Topic> topics;
    private int layout;
    private CustomOnItemClickListener customOnItemClickListener;

    public TopicAdapter(Context mContext, List<Topic> topics, int layout, CustomOnItemClickListener customOnItemClickListener) {
        this.mContext = mContext;
        this.topics = topics;
        this.layout = layout;
        this.customOnItemClickListener = customOnItemClickListener;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layout, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {
        Topic topic = topics.get(holder.getAdapterPosition());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customOnItemClickListener.onTopicClick(topic);
            }
        });
        holder.topicNameTxt.setText(topic.getTopicName());
        holder.topicTermsCount.setText(topic.getVocabularyCount() + " Vocabulary");
        //holder.topicOwnerNameTxt.setText(topic.getOwnerId());
        //holder.learnerCount.setText(topic.getUserId().size() + " Learner");
        //Picasso.get().load(topic.getOwnerId().getProfileImage()).into(holder.topicOwnerImg);
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView topicNameTxt;
        TextView topicTermsCount;
        ShapeableImageView topicOwnerImg;
        TextView topicOwnerNameTxt;
        TextView learnerCount;

        public TopicViewHolder(View itemView) {
            super(itemView);
            topicNameTxt = itemView.findViewById(R.id.topicItemNameTxt);
            topicTermsCount = itemView.findViewById(R.id.termsCount);
            topicOwnerImg = itemView.findViewById(R.id.topicUserImg);
            topicOwnerNameTxt = itemView.findViewById(R.id.topicUserName);
            learnerCount = itemView.findViewById(R.id.learnerCount);
        }
    }
}
