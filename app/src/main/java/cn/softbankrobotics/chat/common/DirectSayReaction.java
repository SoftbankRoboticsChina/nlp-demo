/*
 * Copyright [2019] [SoftBank Robotics China Corp]
 *
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

import android.util.Log;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.object.conversation.BaseChatbotReaction;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.conversation.SpeechEngine;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 * The ChatbotReaction of empty.
 */
public class DirectSayReaction extends BaseChatbotReaction {
    private static final String TAG = "DirectSayReaction";
    private String answer;
    private Future<Void> fSay;

    public DirectSayReaction(final QiContext context, String answer) {
        super(context);
        this.answer = answer;
    }

    @Override
    public void runWith(SpeechEngine speechEngine) {
        Say say = SayBuilder.with(speechEngine)
                .withText(answer)
                .build();
        fSay = say.async().run();

        try {
            fSay.get();
        } catch (ExecutionException e) {
            Log.e(TAG, "Error during Say", e);
        } catch (CancellationException e) {
            Log.i(TAG, "Interruption during Say");
        }
    }

    @Override
    public void stop() {
        if (fSay != null) {
            fSay.cancel(true);
        }
    }
}
