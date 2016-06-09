package com.mstc.db;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DataBaseService extends Service {
    DataBaseHelper dataBaseHelper;
    public DataBaseService() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(dataBaseHelper != null){
            dataBaseHelper.close();
        }
    }
}
