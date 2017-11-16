package com.daledawson.products.somedemo;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import com.daledawson.products.somedemo.Utils.PermissionUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/11/14/014.
 */

public class NoticeActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_activity);
        Button sendNotice = (Button) findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_notice:
                initNotice();
                break;
            default:
                break;
        }
    }

    private void initNotice() {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, NoticeActivity.class), 0);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("This is content title")//设置通知标题
                //设置通知正文内容，正文过长会显示省略号
                .setContentText("This is content text This is content text This is content text This is content text This is content text This is content text ")
                .setWhen(System.currentTimeMillis())//通知被创建的时间
                .setSmallIcon(R.mipmap.ic_launcher_round)//设置通知的小图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))//设置通知的大图标
                .setContentIntent(pi)//点击后跳转的活动
                .setAutoCancel(true)//点击通知后通知自动取消//或者在跳入的活动中通过manage.cancel(id)来取消
                .setSound(Uri.fromFile(new File("/system/media/audio/notifications/Whisper.ogg")))//设置通知铃声
                .setVibrate(new long[]{0, 1000, 1000, 1000})//通知震动，需要手动设置权限permission.VIBRATE
                .setLights(Color.GREEN, 1000, 1000)//设置通知LED灯闪烁
//                .setDefaults(NotificationCompat.DEFAULT_ALL)//设置通知默认系统状态
                //设置通知正文内容，正文过长也会全部显示
                .setStyle(new NotificationCompat.BigTextStyle().bigText("This is content text This is content text This is content text This is content text This is content text This is content text "))
                //将正文内容设置成一张大图片
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.big_image)))
                .setPriority(NotificationCompat.PRIORITY_MAX)//设置通知为最高重要程度
                .build();
        manager.notify(1, notification);
    }
}
