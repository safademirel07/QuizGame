package com.safademirel.quizgame.Adapters;

/**
 * Created by SAFA on 4.7.2017.
 */

import android.content.Context;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.safademirel.quizgame.R;
import com.safademirel.quizgame.Models.New;

import java.util.ArrayList;
        import java.util.List;



/**
 * Created by SAFA on 5.6.2016.
 */
public class NewsAdapter extends BaseAdapter {

    private Context context;
    private final List<New> rowNews;
    private LayoutInflater inflater;


    public NewsAdapter(Context context, List<New> rowNews) {
        this.context = context;
        this.rowNews = rowNews;
    }

    public NewsAdapter() {
        this.inflater = inflater;
        this.rowNews = new ArrayList<>();
    }



    @Override
    public int getCount() {
        return rowNews.size();
    }

    @Override
    public Object getItem(int position) {
        return rowNews.get(position);
    }

    @Override
    public long getItemId(int position) { return rowNews.indexOf(getItem(position)); }


    private class ViewHolder {
        TextView title;
        TextView description;
        TextView date;
        //RelativeLayout relativeRowNews;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_news, null);
            viewHolder = new ViewHolder();
            //viewHolder.relativeRowNews = (RelativeLayout) convertView.findViewById(R.id.relativeRowNews);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitleRow);
            viewHolder.description = (TextView) convertView.findViewById(R.id.tvDescRow);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tvDateRow);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //viewHolder.relativeRowNews.setBackground(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.rounded2, null));
        viewHolder.title.setText(rowNews.get(position).getTitle());
        viewHolder.description.setText(rowNews.get(position).getDescription());
        viewHolder.date.setText(rowNews.get(position).getDate());
        /*
        viewHolder.image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtistsActivity.startPopup(context);
            }
        });
        */
        return convertView;
    }
}
