package com.example.androiddemo.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.androiddemo.ipc.aidl.myaidl.Stub;

public class RemoteService extends Service {

    private Stub mBinder = new Stub() {
        @Override
        public User getUser(String name) throws RemoteException {
            return new User(name, "123456");
        }
    };

    public RemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
