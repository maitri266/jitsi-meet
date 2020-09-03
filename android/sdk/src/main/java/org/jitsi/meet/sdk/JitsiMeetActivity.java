/*
 * Copyright @ 2019-present 8x8, Inc.
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

package org.jitsi.meet.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.facebook.react.modules.core.PermissionListener;

import org.jitsi.meet.sdk.log.JitsiMeetLogger;

import java.util.Map;


/**
 * A base activity for SDK users to embed. It uses {@link JitsiMeetFragment} to do the heavy
 * lifting and wires the remaining Activity lifecycle methods so it works out of the box.
 */
public class JitsiMeetActivity extends FragmentActivity
        implements JitsiMeetActivityInterface, JitsiMeetViewListener {

    protected static final String TAG = JitsiMeetActivity.class.getSimpleName();

    private static final String ACTION_JITSI_MEET_CONFERENCE = "org.jitsi.meet.CONFERENCE";
    private static final String JITSI_MEET_CONFERENCE_OPTIONS = "JitsiMeetConferenceOptions";

    protected static JitsiMeetView currentJitsiView = null;
    protected static Context currentCallingContext;
    protected static FragmentActivity thisActivity;
    protected static boolean isHidden = false;

    // Helpers for starting the activity
    //

    public static void launch(Context context, JitsiMeetConferenceOptions options) {
        Intent intent = new Intent(context, JitsiMeetActivity.class);
        intent.setAction(ACTION_JITSI_MEET_CONFERENCE);
        intent.putExtra(JITSI_MEET_CONFERENCE_OPTIONS, options);
        setCurrentCallingContext(context);
        getCurrentCallingContext().startActivity(intent);
    }

    public static void launch(Context context, String url) {
        JitsiMeetConferenceOptions options
            = new JitsiMeetConferenceOptions.Builder().setRoom(url).build();
        setCurrentCallingContext(context);
        launch(getCurrentCallingContext(), options);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showPipWindow(){
        if(thisActivity==null)
            return;
        //@cobrowsing log showPipWindow
        JitsiMeetLogger.d(TAG+" cobrowsing-showPipWindow: ");
        //@cobrowsing log showPipWindow
        JitsiMeetLogger.d(TAG+" cobrowsing-showPipWindow: isPiPEnabled "+ thisActivity.isInPictureInPictureMode());
        if(isHidden){
            //@cobrowsing log showPipWindow
            JitsiMeetLogger.d(TAG+" cobrowsing-showPipWindow: isHiddenCurrently "+thisActivity);
            try {
                getCurrentCallingContext().startActivity(new Intent(getCurrentCallingContext(), thisActivity.getClass())
                    .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                JitsiMeetLogger.d(TAG+" cobrowsing-showPipWindow: isPiPEnabled try "+ thisActivity.isInPictureInPictureMode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                getCurrentJitsiView().enterPictureInPicture();
            }
        },150);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void hidePipWindow(Context context){
        Log.d(TAG, "hidePipWindow() called with: context = [" + context + "]");
        if(thisActivity==null)
            return;
        //@cobrowsing log hidePipWindow
        JitsiMeetLogger.d(TAG+" cobrowsing-hidePipWindow: ");
        //@cobrowsing log hidePipWindow
        JitsiMeetLogger.d(TAG+" cobrowsing-hidePipWindow: isPiPEnabled "+thisActivity.isInPictureInPictureMode());
        if(true){
            //@cobrowsing log hidePipWindow
            JitsiMeetLogger.d(TAG+" cobrowsing-hidePipWindow: isShowingCurrently : "+thisActivity);
            try {
                context.startActivity(new Intent(context, thisActivity.getClass())
                    .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));

                thisActivity.startActivity(new Intent(thisActivity,context.getClass()));
                //@cobrowsing log hidePipWindow
                JitsiMeetLogger.d(TAG+" cobrowsing-hidePipWindow: isPiPEnabled try "+thisActivity.isInPictureInPictureMode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void launchJitsiMeetView(Context context){
        Log.d(TAG, "launchJitsiMeetView() called with: context = [" + context + "]");
        if(thisActivity==null)
            return;
        JitsiMeetLogger.d(TAG+" cobrowsing-hidePipWindow: ");
        //@cobrowsing log hidePipWindow
        JitsiMeetLogger.d(TAG+" cobrowsing-hidePipWindow: isPiPEnabled "+thisActivity.isInPictureInPictureMode());
        if(true){
            //@cobrowsing log hidePipWindow
            JitsiMeetLogger.d(TAG+" cobrowsing-hidePipWindow: isShowingCurrently : "+thisActivity);
            try {
                context.startActivity(new Intent(context, thisActivity.getClass())
                    .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                //@cobrowsing log hidePipWindow
                JitsiMeetLogger.d(TAG+" cobrowsing-hidePipWindow: isPiPEnabled try "+thisActivity.isInPictureInPictureMode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean isPiPEnabled(){
        JitsiMeetLogger.d(TAG + " cobrowsing-isPiPEnabled: return Value thisActivity" + thisActivity);
        if(thisActivity==null){
            return false;
        }
        else {
            JitsiMeetLogger.d(TAG + " cobrowsing-isPiPEnabled: return Value " + thisActivity.isInPictureInPictureMode());
            return thisActivity.isInPictureInPictureMode();
        }
    }
    public static FragmentActivity getThisActivity() {
        return thisActivity;
    }

    public static void setThisActivity(FragmentActivity thisActivity) {
        JitsiMeetActivity.thisActivity = thisActivity;
    }


    public static boolean isHidden() {
        return isHidden;
    }

    public static void setIsHidden(boolean isHidden) {
        JitsiMeetActivity.isHidden = isHidden;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        isHidden = false;
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
        isHidden = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
        Log.d(TAG, "onResume: Current Jitsi View "+getJitsiView());
        currentJitsiView = getJitsiView();
        thisActivity = this;
        isHidden = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() called");
        isHidden = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
        Log.d(TAG, "onResume: Current Jitsi View "+getJitsiView());
        currentJitsiView = getJitsiView();
        thisActivity = this;
        isHidden = false;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    public static Context getCurrentCallingContext() {
        return currentCallingContext;
    }

    public static void setCurrentCallingContext(Context currentCallingContext) {
        JitsiMeetActivity.currentCallingContext = currentCallingContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_jitsi_meet);

        // Listen for conference events.
        getJitsiView().setListener(this);

        if (!extraInitialize()) {
            initialize();
        }
    }

    @Override
    public void onDestroy() {
        // Here we are trying to handle the following corner case: an application using the SDK
        // is using this Activity for displaying meetings, but there is another "main" Activity
        // with other content. If this Activity is "swiped out" from the recent list we will get
        // Activity#onDestroy() called without warning. At this point we can try to leave the
        // current meeting, but when our view is detached from React the JS <-> Native bridge won't
        // be operational so the external API won't be able to notify the native side that the
        // conference terminated. Thus, try our best to clean up.
        thisActivity = null;
        setCurrentCallingContext(null);
        isHidden = true;
        leave();
        if (AudioModeModule.useConnectionService()) {
            ConnectionService.abortConnections();
        }
        JitsiMeetOngoingConferenceService.abort(this);

        super.onDestroy();
    }

    @Override
    public void finish() {
        leave();

        super.finish();
    }

    // Helper methods
    //

    public JitsiMeetView getJitsiView() {
        JitsiMeetFragment fragment
            = (JitsiMeetFragment) getSupportFragmentManager().findFragmentById(R.id.jitsiFragment);
        return fragment.getJitsiView();
    }

    public void join(@Nullable String url) {
        JitsiMeetConferenceOptions options
            = new JitsiMeetConferenceOptions.Builder()
                .setRoom(url)
                .build();
        join(options);
    }

    public void join(JitsiMeetConferenceOptions options) {
        getJitsiView().join(options);
    }

    public void leave() {
        getJitsiView().leave();
    }

    private @Nullable JitsiMeetConferenceOptions getConferenceOptions(Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            if (uri != null) {
                return new JitsiMeetConferenceOptions.Builder().setRoom(uri.toString()).build();
            }
        } else if (ACTION_JITSI_MEET_CONFERENCE.equals(action)) {
            return intent.getParcelableExtra(JITSI_MEET_CONFERENCE_OPTIONS);
        }

        return null;
    }

    /**
     * Helper function called during activity initialization. If {@code true} is returned, the
     * initialization is delayed and the {@link JitsiMeetActivity#initialize()} method is not
     * called. In this case, it's up to the subclass to call the initialize method when ready.
     *
     * This is mainly required so we do some extra initialization in the Jitsi Meet app.
     *
     * @return {@code true} if the initialization will be delayed, {@code false} otherwise.
     */
    protected boolean extraInitialize() {
        return false;
    }

    protected void initialize() {
        // Join the room specified by the URL the app was launched with.
        // Joining without the room option displays the welcome page.
        join(getConferenceOptions(getIntent()));
    }

    // Activity lifecycle methods
    //

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        JitsiMeetActivityDelegate.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
//        JitsiMeetActivityDelegate.onBackPressed();
        Intent intent = new Intent(thisActivity, getCurrentCallingContext().getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        JitsiMeetConferenceOptions options;

        if ((options = getConferenceOptions(intent)) != null) {
            join(options);
            return;
        }

        JitsiMeetActivityDelegate.onNewIntent(intent);
    }

    @Override
    protected void onUserLeaveHint() {
        //@cobrowsing log onUserLeaveHint
        JitsiMeetLogger.d(TAG+" cobrowsing-onUserLeaveHint: PIP event ");
        // getJitsiView().enterPictureInPicture();
//        onBackPressed();
    }

    // JitsiMeetActivityInterface
    //

    @Override
    public void requestPermissions(String[] permissions, int requestCode, PermissionListener listener) {
        JitsiMeetActivityDelegate.requestPermissions(this, permissions, requestCode, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // JitsiMeetViewListener
    //

    public static JitsiMeetView getCurrentJitsiView() {
        return currentJitsiView;
    }

    public static void setCurrentJitsiView(JitsiMeetView currentJitsiView) {
        JitsiMeetActivity.currentJitsiView = currentJitsiView;
    }

    @Override
    public void onConferenceJoined(Map<String, Object> data) {
        JitsiMeetLogger.i("Conference joined: " + data);
        // Launch the service for the ongoing notification.
        JitsiMeetOngoingConferenceService.launch(this);
    }

    @Override
    public void onConferenceTerminated(Map<String, Object> data) {
        JitsiMeetLogger.i("Conference terminated: " + data);
        finish();
    }

    @Override
    public void onConferenceWillJoin(Map<String, Object> data) {
        JitsiMeetLogger.i("Conference will join: " + data);
    }
}
