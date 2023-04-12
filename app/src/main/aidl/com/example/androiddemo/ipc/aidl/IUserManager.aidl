// IUserManager.aidl
package com.example.androiddemo.ipc.aidl;

import com.example.androiddemo.ipc.aidl.User;

interface IUserManager {

    User getUser(String name);
}