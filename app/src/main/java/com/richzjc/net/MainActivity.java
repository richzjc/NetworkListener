package com.richzjc.net;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.richzjc.netannotation.NetAvailable;
import com.richzjc.netannotation.NetChange;
import com.richzjc.netannotation.NetLose;
import com.richzjc.netannotation.nettype.NetType;
import com.richzjc.network.NetManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import app.NetChanger;

public class MainActivity extends AppCompatActivity {

    @NetLose
    public void test(){
        Toast.makeText(this, "没有网络了", Toast.LENGTH_LONG).show();
    }

    @NetAvailable
    public void test1(){
        Toast.makeText(this, "连接上网络了", Toast.LENGTH_LONG).show();
    }

    @NetChange(netType = NetType.AUTO)
    public void test2(){
        Toast.makeText(this, "有网络了Auto", Toast.LENGTH_LONG).show();
    }

    @NetChange(netType = NetType.WIFI)
    public void test3(){
        Toast.makeText(this, "有网络了Wife", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetManager.addIndex(new NetChanger());
        NetManager.init(this);
        NetManager.bind(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetManager.unBind(this);
    }
}
