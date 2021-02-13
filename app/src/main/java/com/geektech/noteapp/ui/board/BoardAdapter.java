package com.geektech.noteapp.ui.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.geektech.noteapp.OnItemClickListener;
import com.geektech.noteapp.R;

import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

    private List<BoardModel> listBoardModels;
    private LayoutInflater layoutInflater;

    private Context context;
    private OnItemClickListener onItemClickListener;

    public BoardAdapter(Context context, List<BoardModel> listBoardModels) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.listBoardModels = listBoardModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pager_board, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listBoardModels.get(position));
    }

    @Override
    public int getItemCount() {
        return listBoardModels.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle, textDesc;
        private LottieAnimationView lottieAnimationView;
        private Button buttonGetStarted;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lottieAnimationView = itemView.findViewById(R.id.lottie_animation_view);
            textDesc = itemView.findViewById(R.id.text_desc);
            textTitle = itemView.findViewById(R.id.text_title);
            buttonGetStarted = itemView.findViewById(R.id.button_get_started);
            buttonGetStarted.setOnClickListener(v ->
                    onItemClickListener.onClick(getAdapterPosition()));
        }

        public void bind(BoardModel boardModel) {
     //TODO:   3rd Home Work - init. views & showing button on the 3rd page
            buttonGetStarted.setVisibility(View.GONE);
            textTitle.setText(boardModel.getTitle());
            textDesc.setText(boardModel.getTitleBelow());
            lottieAnimationView.setAnimation(boardModel.getImages());
            if (getAdapterPosition() == listBoardModels.size() - 1) {
                buttonGetStarted.setVisibility(View.VISIBLE);
            }
        }
    }
}
