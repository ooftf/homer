package com.ooftf.homer.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ooftf.homer.lib.IpcClient;
import com.ooftf.homer.lib.IpcConst;
import com.ooftf.homer.lib.aidl.IpcResponseBody;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IpcClient.request(IpcClient.getBaseUri(IpcConst.MAIN_HOST).path("/first").build(), null).subscribe(new Consumer<IpcResponseBody>() {
                    @Override
                    public void accept(IpcResponseBody ipcResponseBody) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
            }
        });

    }
}
