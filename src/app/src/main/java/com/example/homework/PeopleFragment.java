package com.example.homework;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeopleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private View view;
    private RecyclerView fragment_people_recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PeopleAdapter peopleAdapter;
    private Context context;
    private List<Map<String, Object>> peopleData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_people, container, false);

        fragment_people_recyclerView = view.findViewById(R.id.fragment_people_recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.fragment_people_swipeRefreshLayout);
        context = getContext();

        initData();
        configSwipeRefreshLayout();

        peopleAdapter = new PeopleAdapter(context, peopleData);
        peopleAdapter.setOnItemClickListener(new PeopleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Map<String, Object> data) {
                ImageView avatars = view.findViewById(R.id.layout_chat_item_imageView);
                TextView name = view.findViewById(R.id.layout_chat_item_textView1);
                TextView message = view.findViewById(R.id.layout_chat_item_textView2);
                TextView time = view.findViewById(R.id.layout_chat_item_textView3);

                //跳转
                Intent intent = new Intent(context, PeopleActivity.class);
                Bundle bundle = new Bundle();

                //将图片转换成字节数组传递
                Bitmap bitmap = DrawableUtil.drawableToBitmap(avatars.getDrawable());
                byte[] bytes = DrawableUtil.bitmapToBytes(bitmap);

                bundle.putByteArray("avatars", bytes);
                bundle.putString("name", name.getText().toString());
                bundle.putString("message", message.getText().toString());
                bundle.putString("time", time.getText().toString());
                if(message.getText().toString().contains("我:"))
                {
                    bundle.putString("type", "I");
                }
                else
                {
                    bundle.putString("type", "others");
                }
                bundle.putInt("requestCode", 1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.VERTICAL);
        fragment_people_recyclerView.setLayoutManager(manager);
        fragment_people_recyclerView.setAdapter(peopleAdapter);
        return view;
    }

    private void configSwipeRefreshLayout() {
        swipeRefreshLayout.setSize(CircularProgressDrawable.LARGE);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.system_accent1_200, android.R.color.system_neutral2_300);
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1)
        {
            Bundle bundle = data.getExtras();
            String name = bundle.getString("name");
            String message = bundle.getString("message");
            String time = bundle.getString("time");
            //更新聊天记录
            for (int i = 0; i < peopleData.size(); i++) {
                if(peopleData.get(i).get("name").toString().equals(name))
                {
                    peopleData.get(i).put("message", "我:" + message);
                    peopleData.get(i).put("time", time);
                    peopleAdapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    private void initData() {
        String[] name = {"[客户]项目经理Jack", "[客户]腾讯HR_Amy", "[职业人]爱画画的小喵", "[职业人]（暂时不接单）论文职业代写", "[职业人]安卓攻城狮"};
        String[] message = {"招聘后端开发人员，名额有限，速来call我！", "目前腾讯岗位较少，只招大神！", "接大量订单，本人广美毕业，曾获得3项国家级美术大奖！",
                "此人暂无信息", "阿里巴巴刚毕业的35岁同学，有丰富的前端开发经验"};
        Integer[] avatars = {R.drawable.avatars1, R.drawable.avatars2, R.drawable.avatars3,
                R.drawable.avatars4, R.drawable.avatars5};
        String[] time = {"刚刚来过", "刚刚来过", "1天前来过", "5天前来过", "7天前来过"};
        for (int i = 0; i < name.length; i++) {
            HashMap<String, Object> d = new HashMap<>();
            d.put("name", name[i]);
            d.put("message", message[i]);
            d.put("time", time[i]);
            d.put("avatars", avatars[i]);
            peopleData.add(d);
        }
    }

    @Override
    public void onRefresh() {
        //延时2s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }
}