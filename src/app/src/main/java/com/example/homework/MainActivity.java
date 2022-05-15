package com.example.homework;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FragmentManager fm = getSupportFragmentManager();
    private LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;
    private ImageView imageView1,imageView2,imageView3,imageView4;

    private Fragment chat = new ChatFragment();
    private Fragment people = new PeopleFragment();
    private Fragment find = new FindFragment();
    private Fragment personal = new PersonalFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();
        initImageView();
        showFragment(0);

        linearLayout1.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        linearLayout3.setOnClickListener(this);
        linearLayout4.setOnClickListener(this);
    }

    private void initFragment() {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fragment_content, chat);
        transaction.add(R.id.fragment_content, people);
        transaction.add(R.id.fragment_content, find);
        transaction.add(R.id.fragment_content, personal);
        transaction.commit();
    }

    private void initImageView() {
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);

        linearLayout1 = findViewById(R.id.layout1);
        linearLayout2 = findViewById(R.id.layout2);
        linearLayout3 = findViewById(R.id.layout3);
        linearLayout4 = findViewById(R.id.layout4);
    }

    private void hideFragment(FragmentTransaction transaction) {
        transaction.hide(chat);
        transaction.hide(people);
        transaction.hide(find);
        transaction.hide(personal);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        hideFragment(fragmentTransaction);
        resetImg();
        switch (view.getId()) {
            case R.id.layout1:
                showFragment(0);
                break;
            case R.id.layout2:
                showFragment(1);
                break;
            case R.id.layout3:
                showFragment(2);
                break;
            case R.id.layout4:
                showFragment(3);
                break;
            default:
                break;
        }
    }

    private void resetImg() {    //调用灰色的图片，以让点击过后的图片回复原色
        imageView1.setImageResource(R.drawable.chat_normal);
        imageView2.setImageResource(R.drawable.people_normal);
        imageView3.setImageResource(R.drawable.find_normal);
        imageView4.setImageResource(R.drawable.personal_normal);

    }

    private void showFragment(int i) {    //控制图片颜色的变换，其意义是点击一个图片之后该图片就会变亮
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                transaction.show(chat);
                imageView1.setImageResource(R.drawable.chat_active);
                break;
            case 1:
                transaction.show(people);
                imageView2.setImageResource(R.drawable.people_active);
                break;
            case 2:
                transaction.show(find);
                imageView3.setImageResource(R.drawable.find_active);
                break;
            case 3:
                transaction.show(personal);
                imageView4.setImageResource(R.drawable.personal_active);
                break;
            default:
                break;
        }
        transaction.commit();
    }
}