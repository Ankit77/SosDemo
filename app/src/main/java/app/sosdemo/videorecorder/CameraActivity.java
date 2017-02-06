/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.sosdemo.videorecorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import app.sosdemo.R;

public class CameraActivity extends AppCompatActivity {

    private String ticket;

    public String getTicket() {
        return ticket;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (getIntent().getExtras() != null) {
            ticket = getIntent().getExtras().getString("TICKET");
        }
        if (!TextUtils.isEmpty(ticket)) {
            if (null == savedInstanceState) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, Camera2VideoFragment.newInstance())
                        .commit();
            }
        } else {
            finish();
        }
    }

    public void finishRecord(String path) {
        Intent intent = new Intent();
        intent.putExtra("FILEPATH", path);
        setResult(2, intent);
        finish();//finishing activity
    }
}
