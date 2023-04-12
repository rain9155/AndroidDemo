package com.example.androiddemo.ipc.aidl.myaidl;

import android.os.IBinder;

import com.example.androiddemo.ipc.aidl.User;

/**
 * Binder本地代理类
 * Created by 陈健宇 at 2019/10/21
 */
public class Proxy implements IUserManager {

    private IBinder mRemote;

    Proxy(IBinder remote) {
        mRemote = remote;
    }

    @Override
    public IBinder asBinder() {
        return mRemote;
    }

    @Override
    public User getUser(String name) throws android.os.RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        User _result;
        try {
            _data.writeInterfaceToken(Stub.DESCRIPTOR);//这里引用我们自己写的Stub的DESCRIPTOR
            _data.writeString(name);
            mRemote.transact(Stub.TRANSACTION_getUser, _data, _reply, 0);//这里引用我们自己写的Stub的TRANSACTION_getUser
            _reply.readException();
            if ((0 != _reply.readInt())) {
                _result = User.CREATOR.createFromParcel(_reply);
            } else {
                _result = null;
            }
        } finally {
            _reply.recycle();
            _data.recycle();
        }
        return _result;
    }

}
