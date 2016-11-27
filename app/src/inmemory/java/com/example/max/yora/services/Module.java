package com.example.max.yora.services;

import android.util.Log;

import com.example.max.yora.infrastructure.YoraApplication;

public class Module {
    public static void register(YoraApplication application) {
        Log.e("Module", "IN MEMORY REGISTER METHOD CALLED");
        new InMemoryAccountService(application);
    }
}
