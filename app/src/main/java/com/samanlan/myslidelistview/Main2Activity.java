package com.samanlan.myslidelistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "123";
    MySlideListView lv;
    List<Integer> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        lv= (MySlideListView) findViewById(R.id.lv);
        list=new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        myAdapter adapter=new myAdapter();
        lv.setAdapter(adapter);
    }

    public class myAdapter extends BaseAdapter{

        LayoutInflater layoutInflater;

        public myAdapter() {
            Log.e(TAG, "myAdapter: "+list.size() );
            this.layoutInflater = LayoutInflater.from(Main2Activity.this);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if (convertView==null) {
                viewHolder=new ViewHolder();
                convertView=layoutInflater.inflate(R.layout.listview_item,null);
                MySlideViewGroup v=((MySlideViewGroup)convertView);
                if (v.isCanSlideLeft())
                    viewHolder.leftView=v.getChildAt(v.getLeftViewIndex());
                if (v.isCanSlideRight())
                    viewHolder.rightView=v.getChildAt(v.getRightViewIndex());
                if (v.isCanSlideUp())
                    viewHolder.topView=v.getChildAt(v.getTopViewIndex());
                if (v.isCanSlideDown())
                    viewHolder.bottomView=v.getChildAt(v.getBottomViewIndex());
                viewHolder.centerView=v.getChildAt(v.getCenterViewIndex());
                convertView.setTag(viewHolder);
            }else {
                viewHolder=(ViewHolder) convertView.getTag();
            }
            ((TextView)convertView.findViewById(R.id.text)).setText(position+"");
            return convertView;
        }

        class ViewHolder{
            View leftView;
            View rightView;
            View topView;
            View bottomView;
            View centerView;
        }
    }

    public void buttonClick(View v){
        System.out.println(((Button)v).getText());
        Toast.makeText(Main2Activity.this, ((TextView)v).getText(), Toast.LENGTH_SHORT).show();
    }
}
