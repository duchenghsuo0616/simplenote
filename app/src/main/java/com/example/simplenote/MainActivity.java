package com.example.simplenote;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * 主页
 * 四个底部
 * 使用tabhost
 * 这里
 * Activity  +Fragment
 */
public class MainActivity extends FragmentActivity{
    private FragmentTabHost tabHost;
    private View view = null;
    private LayoutInflater Inflater;
    private List<Tab> list = new ArrayList<>(3);
    private ViewPager viewPager;
    private List<Fragment> FragmentList = new ArrayList<>(3);

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
     };




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Inflater = LayoutInflater.from(this);
        //初始化tab的方法
        initTab();
        //requestPower();
        //requestPhonePower();
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
//        if(Build.VERSION.SDK_INT>=23)
//        {
//            if(Settings.canDrawOverlays(this))
//            {
//                //有悬浮窗权限开启服务绑定 绑定权限
//                Intent intent = new Intent(MainActivity.this, AlarmService.class);
//                startService(intent);
//
//            }else{
//                //没有悬浮窗权限m,去开启悬浮窗权限
//                try{
//                    Intent  intent=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//
//            }
//        }



       /* final WindowManager.LayoutParams   params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            params.type= WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        }else {
            params.type =  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if(!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(intent);
                return;
            } else {
//绘ui代码, 这里说明6.0系统已经有权限了
            }
        } else {
//绘ui代码,这里android6.0以下的系统直接绘出即可
        }*/
    }
    //todo 初始化资源 Tabhost
    private void initTab() {
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        //TODO 绑定 Viewpager  ji加分项
        tabHost.setup(this,getSupportFragmentManager(),R.id.view_pager);
        //TODO 添加资源 初始化三个页面 加进Tabhost
        Tab tabmain = new Tab(R.string.home, R.drawable.ic_write_normal, Fragment1.class);
        Tab tabcontact = new Tab(R.string.daojishi, R.drawable.ic_check_normal, Fragment2.class);
        Tab tabmine = new Tab(R.string.tubiao, R.drawable.ic_setting_normal, Fragment3.class);
        list.add(tabmain);
        list.add(tabcontact);
        list.add(tabmine);
   //加入到Tabhost
        for (Tab tab : list) {
            TabHost.TabSpec spec= tabHost.newTabSpec(getString(tab.getTitle()))//
                    .setIndicator(getItemView(tab));
            tabHost.addTab(spec,tab.getFragment(),null);
        }
        //清除分界线
        tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        //初始化viewpage 加分的
        initFragment();
    }

    /**
     * 初始化ViewPager 加分
     */
    private void initFragment() {
        //Todo            添加四个fragment
        FragmentList.add(new Fragment1());
        FragmentList.add(new Fragment2());
        FragmentList.add(new Fragment3());
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        //设置适配器
        viewPager.setAdapter(new MyViewPager(getSupportFragmentManager(),FragmentList));
        //设置界面滑动监听事件
        viewPager.addOnPageChangeListener(new MyPageChangeListener(tabHost));
        //设置viewpage缓存
        viewPager.setOffscreenPageLimit(3);
        //设置TabHost的点击切换监听事件
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int position  =tabHost.getCurrentTab();
                viewPager.setCurrentItem(position);
            }
        });
    }
    //TODO 给indicator配置View
    private View getItemView(Tab tab) {
        view = Inflater.inflate(R.layout.itemview, null);
        ImageView img = (ImageView) view.findViewById(R.id.iv);
        TextView textview = (TextView) view.findViewById(R.id.tv);
        img.setImageResource(tab.getImg());
        textview.setText(tab.getTitle());
        return view;
    }
    public void requestPower() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW)!= PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},2);

        }
    }
    public void requestPhonePower() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS},1);

        }
    }

}