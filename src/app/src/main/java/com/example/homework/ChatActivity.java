package com.example.homework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ChatActivity extends AppCompatActivity {

    private List<Map<String, Object>> data = new ArrayList<>();

    private RecyclerView recyclerView;

    private TextView textView1;
    
    private ImageView back, more;
    
    private Button button1;

    private EditText editText;

    private LinearLayout linearLayout;

    private Boolean isSend = false;

    private Map<String, Object> result = new HashMap<>();

    private Intent intent;

    private String name;

    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        linearLayout = findViewById(R.id.activity_chat_layout);
        recyclerView = findViewById(R.id.activity_chat_recyclerView);
        textView1 = findViewById(R.id.activity_chat_textView1);
        back = findViewById(R.id.activity_chat_imageView_back);
        more = findViewById(R.id.activity_chat_imageView_more);
        button1 = findViewById(R.id.activity_chat_button);
        editText = findViewById(R.id.activity_chat_editText);

        intent = getIntent();
        Bundle bundle = intent.getExtras();
        byte[] avatars = bundle.getByteArray("avatars");
        name = bundle.getString("name");
        String message = bundle.getString("message");
        String time = bundle.getString("time");
        String type = bundle.getString("type");
        int requestCode = bundle.getInt("requestCode");

        if(requestCode == 1)
        {
            textView1.setText(name);

            Map<String, Object> map = new HashMap<>();
            map.put("avatars", avatars);
            map.put("message", message);
            map.put("time", time);
            map.put("type", type);
            data.add(map);

            adapter = new MessageAdapter(this, data);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
        }

        setbackListener();
        setEditTextListener();
        setSendMsgListener();
    }

    /**
     * 监听输入框是否失去焦点进行按钮的切换
     */
    private void setEditTextListener() {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                {
                    more.setVisibility(View.GONE);
                    button1.setVisibility(View.VISIBLE);
                }else
                {
                    more.setVisibility(View.VISIBLE);
                    button1.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setSendMsgListener() {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().trim().equals(""))
                {
                    isSend = true;
                    result.put("message", editText.getText().toString());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                    result.put("time", simpleDateFormat.format(new Date()));
                    result.put("type", "I");
                    data.add(result);
                    //清楚输入的聊天信息并失去焦点
                    editText.setText("");
                    editText.clearFocus();
                    adapter.notifyItemInserted(data.size() - 1);
                }
            }
        });
    }

    /**
     * 返回主菜单
     */
    private void setbackListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果发送了新的消息,返回主界面时要更新信息
                int resultCode = 0;
                Bundle bundle = new Bundle();
                if(isSend)
                {
                    resultCode = 1;
                    bundle.putString("name", name);
                    bundle.putString("message", result.get("message").toString());
                    bundle.putString("time", result.get("time").toString());
                    bundle.putInt("resultCode", 1);
                }
                else
                {
                    bundle.putInt("resultCode", 0);
                }
                intent.putExtras(bundle);
                setResult(resultCode, intent);
                finish();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                //关闭键盘,并让输入框失去焦点
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 隐藏键盘
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}