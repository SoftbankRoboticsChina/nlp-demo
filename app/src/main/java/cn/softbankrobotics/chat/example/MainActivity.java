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

package cn.softbankrobotics.chat.example;

import android.os.Bundle;
import android.util.Log;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.softbankrobotics.nlp.R;

import cn.softbankrobotics.chat.iflytek.IFlytekChatbot;

/**
 *
 * Example of IFlytekNlp.
 *
 */
public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {
    private static final String TAG = "MainActivity";
    private Chat mChat;
    private Say mSay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // QiSDK注册。
        QiSDK.register(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        handleRobotGained(qiContext);
    }

    private void handleRobotGained(QiContext qiContext){
        mSay = SayBuilder.with(qiContext)
                .withText(getResources().getString(R.string.greeting))
                .build();
        mSay.async().run().andThenConsume(consume -> {
            // 自定义讯飞Chatbot。
            IFlytekChatbot iflytekChatbot = new IFlytekChatbot(qiContext, this);
            // 创建chat。
            mChat = ChatBuilder.with(qiContext)
                    .withChatbot(iflytekChatbot)
                    .build();

            Future<Void> chatFuture = mChat.async().run();
            chatFuture.thenConsume(future -> {
                if (future.hasError()) {
                    String message = "finished with error.";
                    Log.e(TAG, message, future.getError());
                } else if (future.isSuccess()) {
                    Log.i(TAG, "run iflytekChatbot successful");
                } else if (future.isDone()) {
                    Log.i(TAG, "run iflytekChatbot isDone");
                }
            });
        });
    }

    @Override
    public void onRobotFocusLost() {
        Log.e(TAG, "onRobotFocusLost");
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        Log.e(TAG, "onRobotFocusRefused  " + reason);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // QiSDK注销。
        QiSDK.unregister(this, this);
    }
}
