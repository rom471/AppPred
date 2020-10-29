package com.rom471.db;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;

import static android.content.Context.BATTERY_SERVICE;

public class DBUtils {
    //给record附加电池信息
    public static void storeBatteryInfo(Context context, Record record){
        BatteryManager manager = (BatteryManager)context.getSystemService(BATTERY_SERVICE);

        record.setBattery(manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY));
        record.setCharging(manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW));
    }
    //给record附加网络信息
    public static void storeNetworkInfo(Context context, Record record){
        int net_state=0;
        ConnectivityManager manager= (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //判断NetworkInfo对象是否为空
        if (networkInfo != null) {

            if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){//使用数据
                net_state=1;
            }
            else if(networkInfo.getType()==ConnectivityManager.TYPE_WIFI){//使用wifi
                net_state=2;
            }
        }
        record.setNet(net_state);

    }

}
