package com.samanlan.myslidelistview;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button leftButton;
    MySlideViewGroup my;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        my= (MySlideViewGroup) findViewById(R.id.my);
        my.removeMyViewByTag("RIGHT");
        leftButton= (Button) findViewById(R.id.leftButton);
        //rightButton= (Button) findViewById(R.id.rightButton);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });
        MySlideViewGroup a=new MySlideViewGroup(this);
        a.addMyView(R.layout.left,"LEFT",new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,200));
        a.addMyView(R.layout.center,"CENTER",new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200));
        ((ViewGroup)findViewById(R.id.addHere)).addView(a, ViewGroup.LayoutParams.MATCH_PARENT,200);
        //a.removeMyViewByTag("LEFT");
    }

    public void textClick(View v){
        System.out.println(((TextView)v).getText());
        Toast.makeText(MainActivity.this, ((TextView)v).getText(), Toast.LENGTH_SHORT).show();
    }

    public void buttonClick(View v){
        System.out.println(((Button)v).getText());
        Toast.makeText(MainActivity.this, ((TextView)v).getText(), Toast.LENGTH_SHORT).show();
    }
}
