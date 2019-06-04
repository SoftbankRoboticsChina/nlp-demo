/*
 * Copyright [2019] [SoftBank Robotics China Corp]
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.softbankrobotics.chat.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * The manager of preference.
 */
public class PreferencesManager {
    private static final String TAG = "PreferencesManager";
    private static PreferencesManager mInstance;
    private final SharedPreferences mPref;
    private final String IFLYTEK_NLP_ANSWER = "iflytek_nlp_answer";

    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return mInstance;
    }

    // 获取存储讯飞nlp返回的answer。
    public String getIFlytekNlpAnswer() {
        return mPref.getString(IFLYTEK_NLP_ANSWER, "");
    }

    // 存储讯飞nlp返回的answer。
    public void setIFlytekNlpAnswer(String answer) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(IFLYTEK_NLP_ANSWER, answer);
        editor.commit();
    }


}
