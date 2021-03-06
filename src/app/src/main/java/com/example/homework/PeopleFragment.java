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

                //??????
                Intent intent = new Intent(context, PeopleActivity.class);
                Bundle bundle = new Bundle();

                //????????????????????????????????????
                Bitmap bitmap = DrawableUtil.drawableToBitmap(avatars.getDrawable());
                byte[] bytes = DrawableUtil.bitmapToBytes(bitmap);

                bundle.putByteArray("avatars", bytes);
                bundle.putString("name", name.getText().toString());
                bundle.putString("message", message.getText().toString());
                bundle.putString("time", time.getText().toString());
                if(message.getText().toString().contains("???:"))
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
            //??????????????????
            for (int i = 0; i < peopleData.size(); i++) {
                if(peopleData.get(i).get("name").toString().equals(name))
                {
                    peopleData.get(i).put("message", "???:" + message);
                    peopleData.get(i).put("time", time);
                    peopleAdapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    private void initData() {
        String[] name = {"[??????]????????????Jack", "[??????]??????HR_Amy", "[?????????]??????????????????", "[?????????]???????????????????????????????????????", "[?????????]???????????????"};
        String[] message = {"????????????????????????????????????????????????call??????", "??????????????????????????????????????????", "????????????????????????????????????????????????3???????????????????????????",
                "??????????????????", "????????????????????????35??????????????????????????????????????????"};
        Integer[] avatars = {R.drawable.avatars1, R.drawable.avatars2, R.drawable.avatars3,
                R.drawable.avatars4, R.drawable.avatars5};
        String[] time = {"????????????", "????????????", "1????????????", "5????????????", "7????????????"};
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
        //??????2s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }
}