package com.example.androiddemo.ipc.aidl.myaidl;

import android.os.IInterface;
import android.os.RemoteException;

import com.example.androiddemo.ipc.aidl.User;

/**
 * 暴露服务端方法的接口
 * Created by 陈健宇 at 2019/10/21
 */
public interface IUserManager extends IInterface {

    User getUser(String name) throws RemoteException;

}
