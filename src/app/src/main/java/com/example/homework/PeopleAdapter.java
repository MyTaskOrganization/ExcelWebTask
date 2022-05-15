package com.example.homework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.FindViewHolder> {

    private Context context;

    private List<Map<String, Object>> data;

    private OnItemClickListener onItemClickListener;

    public PeopleAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        this.data = data;
    }

    /**
     * 定义 RecyclerView 选项单击事件的回调接口
     */
    public interface OnItemClickListener{
        void onItemClick(View view, Map<String, Object> data);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public FindViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FindViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_chat_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FindViewHolder holder, int position) {
        holder.textView1.setText(data.get(position).get("name").toString());
        holder.textView2.setText(data.get(position).get("message").toString());
        holder.textView3.setText(data.get(position).get("time").toString());
        holder.imageView.setImageResource(Integer.parseInt(data.get(position).get("avatars").toString()));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class FindViewHolder extends RecyclerView.ViewHolder {

        TextView textView1, textView2, textView3;

        ImageView imageView;

        public FindViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.layout_chat_item_textView1);
            textView2 = itemView.findViewById(R.id.layout_chat_item_textView2);
            textView3 = itemView.findViewById(R.id.layout_chat_item_textView3);
            imageView = itemView.findViewById(R.id.layout_chat_item_imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(v, data.get(getLayoutPosition()));
                    }
                }
            });
        }
    }
}
