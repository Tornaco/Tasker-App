package dev.tornaco.tasker.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dev.tornaco.tasker.app.R;

/**
 * Created by Nick on 2017/6/13 13:56
 */

public class TwoLinesViewHolder extends RecyclerView.ViewHolder {

    private TextView lineOneTextView;

    private TextView lineTwoTextView;

    private ImageView imageView;

    public TwoLinesViewHolder(View itemView) {
        super(itemView);
        lineOneTextView = (TextView) itemView.findViewById(android.R.id.title);
        lineTwoTextView = (TextView) itemView.findViewById(android.R.id.text1);
        imageView = (ImageView) itemView.findViewById(R.id.icon);
    }

    public TextView getLineOneTextView() {
        return lineOneTextView;
    }

    public TextView getLineTwoTextView() {
        return lineTwoTextView;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
