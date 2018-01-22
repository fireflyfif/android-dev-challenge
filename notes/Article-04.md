# Lesson: Background Tasks

## Index
- [Services](https://github.com/fireflyfif/android-dev-challenge/blob/master/notes/Article-04.md#services)
- [Pending Intent](https://github.com/fireflyfif/android-dev-challenge/blob/master/notes/Article-04.md#pending-intent)
- [Foreground Service](https://github.com/fireflyfif/android-dev-challenge/blob/master/notes/Article-04.md#foreground-service)
- [Application Priority](https://github.com/fireflyfif/android-dev-challenge/blob/master/notes/Article-04.md#application-priority)
- [Google Play Services](https://github.com/fireflyfif/android-dev-challenge/blob/master/notes/Article-04.md#google-play-services)
- [Broadcast Receiver](https://github.com/fireflyfif/android-dev-challenge/blob/master/notes/Article-04.md#broadcast-receiver)
- [Getting the Current Battery State](https://github.com/fireflyfif/android-dev-challenge/blob/master/notes/Article-04.md#getting-the-current-battery-state)

## Resources
- [Background Operations](https://developer.android.com/guide/background/index.html)
- [Services](https://developer.android.com/guide/components/services.html)
- [Background Processing in Android](http://www.vogella.com/tutorials/AndroidBackgroundProcessing/article.html)
- [Pending Intent](https://developer.android.com/reference/android/app/PendingIntent.html)

## Overview
By default, application code runs in the main thread. Every statement is therefore executed in sequence. If you perform a long lasting operation, the application blocks until the corresponding operation has finished.

To provide a good user experience all potentially slow running operations in an Android application should run asynchronously. This can be archived via concurrency constructs of the Java language or of the Android framework. Potentially slow operations are for example network, file and database access and complex calculations.

## Services
A *Service* is an application component that can perform long-running operations in the background, and it does not provide a user interface. Another application component can start a service, and it continues to run in the background even if the user switches to another application. Additionally, a component can bind to a service to interact with it and even perform interprocess communication (IPC). For example, a service can handle network transactions, play music, perform file I/O, or interact with a content provider, all from the background.

There are three ways to start a service:

1. **Manually** start a service
2. **Schedule** a service
3. **Bind** to a service

- **Foreground**
A foreground service performs some operation that is noticeable to the user. For example, an audio app would use a foreground service to play an audio track. Foreground services must display a status bar icon. Foreground services continue running even when the user isn't interacting with the app.

- **Background**
A background service performs an operation that isn't directly noticed by the user. For example, if an app used a service to compact its storage, that would usually be a background service.

- **Bound**
A service is bound when an application component binds to it by calling `bindService()`. A bound service offers a client-server interface that allows components to interact with the service, send requests, receive results, and even do so across processes with interprocess communication (IPC). A bound service runs only as long as another application component is bound to it. Multiple components can bind to the service at once, but when all of them unbind, the service is destroyed.

From a high level, **Services** like Activities, have a **lifecycle**. The service is created when a context, such as an activity, calls `startService`, passing in an intent. This triggers the service's `onCreate` method. After that, `onStartCommand` is called. Inside this method is where we should do the whole service logic. When our service is done, we can signal this by calling `stopSelf`. Calling `stopSelf` will then trigger `onDestroy` and the service will be destroyed.

### IntentService
This is a subclass of `Service` that uses a worker thread to handle all of the start requests, one at a time. 
**`IntentService`** runs in a separate background thread altogether. 

First, we create a class that extends from `IntentService`. Inside that class, we will specify what our `IntentService` should do in the background by overwriting the `onHandleIntent()` method. Then we simply create an Intent and pass in that service class. Finally, to start it, we just call `startService` as opposed to `startActivity`. Inside `onHandleIntent` is where we put the code that will run in the background. 

```java
public class WaterReminderIntentService extends IntentService {

    public WaterReminderIntentService() {
        super("WaterReminderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Get the action from the Intent that started this Service
        String action = intent.getAction();

        ReminderTasks.executeTask(this, action);
    }
}
```

> It's important to note that all `IntentService` requests are handled in a single background thread, and they are issued in order.

Then we start our service from wherever we want it to be executed within our application, for example from our **MainActivity.java**:


```java
public void incrementWater(View view) {
 
        // Create an explicit intent for WaterReminderIntentService
        Intent intentWaterReminder = new Intent(this, WaterReminderIntentService.class);

        // Set the action of the intent to ACTION_INCREMENT_WATER_COUNT
        intentWaterReminder.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);

        // Call startService and pass the explicit intent you just created
        startService(intentWaterReminder);
    }
```

## Pending Intent

Any notification in Android is displayed by a **System Service** called `NotificationManager`. 
A system service is a service that starts by the Android system itself, and is not part of the application processes, which means, if we want the `NotificationManager` to launch an activity in our application, we need to set the permissions.

A [PendingIntent](https://developer.android.com/reference/android/app/PendingIntent.html) is a wrapper around a regular intent, that is designed to be used by another application. The `PendingIntent` gives that application the ability to perform actions. It can launch services, private activities, and broadcast protected intents, even if your application is no longer running.

```java
    private static Action drinkWaterAction(Context context) {
        Intent incrementWaterCountIntent = new Intent(context, 
            WaterReminderIntentService.class);

        incrementWaterCountIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);

        PendingIntent incrementWaterPendingIntent = PendingIntent.getService(
                context,
                ACTION_DRINK_PENDING_INTENT_ID,
                incrementWaterCountIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Action drinkWaterAction = new Action(R.drawable.ic_local_drink_black_24px,
                "I did it!",
                incrementWaterPendingIntent);

        return drinkWaterAction;
    }
```

# Foreground Service

A **Foreground service** is a service that the user is actively aware of, because Android requires that service post a non-dismissible ongoing notification. They are typically used to show the user the real-time progress of a long-running operation. 

In addition, because the service is doing something very visible and likely important for the user, Android will prioritize not shutting these services down if the system is memory constrained.

# Application Priority

App priority is divided into four general buckets, **Critical**, **High**, **Medium**, and **Low**. 

- **Critical Apps** are those which are active. They're in the foreground interacting with the user. That includes activities in the foreground and apps running a foreground service.
An activity in the foreground is slightly more important than a foreground service only on recent Android releases.
- **High Priority Apps**: Visible activities are high priority, while less impactful than killing a foreground app or foreground service, destroying visible activities is still quite undesirable, because users will most likely notice it.
- **Medium Priority**: Service Processes are important, but they can be killed much of the time without impacting the user directly. The system tries to keep them alive and will even try to restart sticky services.
- **Low Priority**: Apps that are running in the background are either completely empty apps with no active Android component or apps with a stopped component, but not yet destroyed.

![Application Priority](https://github.com/fireflyfif/android-dev-challenge/blob/master/assets/priority.jpg)

**Android resource management:**

1. Android will keep all apps that interact with the user running smoothly.
2. Android will keep all apps with visible activities followed by services running, unless doing so violates the first law.
3. Android will keep all apps in the background running, unless this violates the first or second law.


# Google Play Services

[Google Play Services](https://developers.google.com/android/guides/overview) is app that Google maintains which comes pre-installed on and runs in the background on many phones. It is essentially a collection of Services that an app can use to leverage the power of Google products. If the user has the Google Play Services apk installed (and many do) you can use Google Play Services Libraries to easily do things like use the Places API to know where the user is or integrate Google sign in. 

`FirebaseJobDispatcher` is one of the many services that can take advantage of via Google Play Services.

## Add `FirebaseJobDispatcher`

1. Add the Gradle Dependency
`compile 'com.firebase:firebase-jobdispatcher:0.5.2'`

2. Create a new task for the `ReminderTasks` (creates a charging reminder and updates the charging count on the screen)
```java
public class ReminderTasks {

    static final String ACTION_CHARGING_REMINDER = "charging-reminder";

    public static void executeTask(Context context, String action) {
        if (ACTION_INCREMENT_WATER_COUNT.equals(action)) {
            incrementWaterCount(context);
        } else if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            NotificationUtils.clearAllNotifications(context);
        } else if (ACTION_CHARGING_REMINDER.equals(action)) {
            issueChargingReminder(context);
        }
    }

    // Create an additional task for issuing a charging reminder notification.
    private static void issueChargingReminder(Context context) {
        PreferenceUtilities.incrementChargingReminderCount(context);
        NotificationUtils.remindUserBecauseCharging(context);
    }
```
3. Create a new Service that extends from `JobService` (this will run the charging `ReminderTask`)
```java
public class WaterReminderFirebaseJobService extends JobService{

    public AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        // By default, jobs are executed on the main thread, so make an anonymous  
        // class extending AsyncTask called mBackgroundTask.

        mBackgroundTask = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                // Use ReminderTasks to execute the new charging reminder task you made, use
                // this service as the context (WaterReminderFirebaseJobService.this) and return null
                // when finished
                Context context = WaterReminderFirebaseJobService.this;
                ReminderTasks.executeTask(context, ReminderTasks.ACTION_CHARGING_REMINDER);

                return null;
            }

            // Override onPostExecute and called jobFinished. 
            // Pass the job parameters and false to jobFinished. This will inform the
            // JobManager that your job is done and that you do not want to reschedule the job.
            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };

        // Execute the AsyncTask
        mBackgroundTask.execute();

        // Return true
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        // If mBackgroundTask is valid, cancel it
        if (mBackgroundTask != null) {mBackgroundTask.cancel(true);}
        // Return true to signify the job should be retried
        return true;
    }
}

```
4. Add the `JobService` to the manifest
```xml
    <service android:name=".sync.WaterReminderFirebaseJobService"
            android:exported="false">
            <intent-filter>
                <action android:name="com.firebase.jobdispatcher.ACTION_EXECUTE"/>
            </intent-filter>
    </service>
```

5. Schedule with `FirebaseJobDispatcher`
```java
public class ReminderUtilities {

    /*
     * Interval at which to remind the user to drink water. Use TimeUnit for convenience, rather
     * than writing out a bunch of multiplication ourselves and risk making a silly mistake.
     */
    private static final int REMINDER_INTERVAL_MINUTES = 15;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;

    private static final String REMINDER_JOB_TAG = "hydration_reminder_tag";

    private static boolean sInitialized;

    // Create a synchronized, public static method called scheduleChargingReminder that takes
    // in a context. This method will use FirebaseJobDispatcher to schedule a job that repeats roughly
    // every REMINDER_INTERVAL_SECONDS when the phone is charging. 
    synchronized public static void scheduleChargingReminder(@NonNull final Context context) {

        // If the job has already been initialized, return
        if (sInitialized) return;

        // Create a new GooglePlayDriver
        Driver driver = new GooglePlayDriver(context);

        // Create a new FirebaseJobDispatcher with the driver
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job constraintReminderJob = dispatcher.newJobBuilder()
                /* The Service that will be used to write to preferences */
                .setService(WaterReminderFirebaseJobService.class)
                /*
                 * Set the UNIQUE tag used to identify this Job.
                 */
                .setTag(REMINDER_JOB_TAG)
                /*
                 * Network constraints on which this Job should run. In this app, we're using the
                 * device charging constraint so that the job only executes if the device is
                 * charging.
                 *
                 * In a normal app, it might be a good idea to include a preference for this,
                 * as different users may have different preferences on when you should be
                 * syncing your application's data.
                 */
                .setConstraints(Constraint.DEVICE_CHARGING)
                /*
                 * setLifetime sets how long this job should persist. The options are to keep the
                 * Job "forever" or to have it die the next time the device boots up.
                 */
                .setLifetime(Lifetime.FOREVER)
                /*
                 * We want these reminders to continuously happen, so we tell this Job to recur.
                 */
                .setRecurring(true)
                /*
                 * We want the reminders to happen every 15 minutes or so. The first argument for
                 * Trigger class's static executionWindow method is the start of the time frame
                 * when the
                 * job should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 */
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                /*
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.
                 */
                .setReplaceCurrent(true)
                /* Once the Job is ready, call the builder's build method to return the Job */
                .build();

        // Use dispatcher's schedule method to schedule the job
        /* Schedule the Job with the dispatcher */
        dispatcher.schedule(constraintReminderJob);

        // Set sInitialized to true to mark that we're done setting up the job
        /* The job has been initialized */
        sInitialized = true;
    }
}
```
In **MainActivity.class**
```java
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       ...

        // Schedule the charging reminder
        ReminderUtilities.scheduleChargingReminder(this);
    }
```

## Broadcast Receiver

A broadcast receiver is a core Android component that enables applications to receive intents that are broadcast by the system or by other applications.
A broadcast receiver can be triggered even when the app is not running. 

We specify which request the receiver is interested in, using something known as an `IntentFilter` . An `IntentFilter` is an expression that says what intents should trigger your component. It should be noted that `IntentFilter`  are not specific to broadcast receivers.

For example, the first activity in an app always has an `IntentFilter`. This intent filter uses the action `MAIN`, and the category `LAUNCHER`, as seen here:
```xml
    <intent-filter>
       <action android:name="android.intent.action.MAIN"/>

       <category android:name="android.intent.category.LAUNCHER"/>
    </intent-filter>
```

If we want to create a `BroadcastReceiver`, there are basically two ways to do it, either statically or dynamically.

- **Static Broadcast Receivers** - triggered whenever the broadcast intent occurs, even if the app is offline; registered in the manifest

- **Dynamic Broadcast Receivers** - tied to the app's lifecycle; instead of defining the receiver in the manifest, you register and un-register the receiver during the activity's life cycle methods. 
`onResume()` --> `registerReceiver();` `onPause()` --> `unregisteredReceiver()`

Generally, if possible, it's better to create a dynamic broadcast receiver, or to use job scheduling. Relying on static broadcast receivers can be problematic. So if we need to execute code when the app isn't running, we should schedule a `Job` if possible.

### Dynamic Broadcast Receiver - Show When Charging

Do this in **MainActivity.class**
- Create a method that changes the plug image from gray to pink, when charging

```java
private void showCharging(boolean isCharging) {
        if (isCharging) {
            mChargingImageView.setImageResource(R.drawable.ic_power_pink_80px);
        } else {
            mChargingImageView.setImageResource(R.drawable.ic_power_grey_80px);
        }
    }
```
- Make an Intent filter for charging status
```java
IntentFilter mChargingIntentFilter;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       ...

        // Create and instantiate a new instance variable for your ChargingBroadcastReceiver
        // and an IntentFilter
        mChargingIntentFilter = new IntentFilter();

        // Call the addAction method on your intent filter and add Intent.ACTION_POWER_CONNECTED
        // and Intent.ACTION_POWER_DISCONNECTED. This sets up an intent filter which will trigger
        // when the charging state changes.
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
    }
```
- Make a Broadcast Receiver that updates the color

```java
private class ChargingBroadcastReceiver extends BroadcastReceiver {
        // Override onReceive to get the action from the intent and see if it matches the
        // Intent.ACTION_POWER_CONNECTED
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean isCharging = (action.equals(Intent.ACTION_POWER_CONNECTED));

            // Update the UI using the showCharging method you wrote
            showCharging(isCharging);
        }
    }
```
- Register the Broadcast Receiver with Intent Filter
We register the Broadcast Receiver in `onResume()`, because it only needs to update the UI when the Activity is in the Foreground.
```java
BroadcastReceiver mChargingReceiver;

 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       ...

        mChargingReceiver = new ChargingBroadcastReceiver();
    }
```


```java
 @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mChargingReceiver, mChargingIntentFilter);
    }
```

- Cleanup the Broadcast Receiver in `onPause()`
```java
@Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mChargingReceiver);
    }
```

## Getting the Current Battery State

Our app adds and removes the dynamic broadcast receiver in `onResume` and `onPause`. When the app is not visible, the plug's image will not update. This can lead to the plug sometimes having the incorrect image when the app starts.

![lifecycle](https://github.com/fireflyfif/android-dev-challenge/blob/master/assets/lifecycle.png)

Now we could move the code to dynamically add and remove the broadcast receiver in different lifecycle methods, for example onCreate and onDestroy, but this would cause us to waste cycles swapping around an image which isn't even on screen. A better approach is to check what the current battery state is when the app resumes and update the image accordingly.

There are two ways to do this, depending on whether you're on API level 23+ or before.

### Getting Charging State on API level 23+
To get the current state of the battery on API level 23+, simply use the battery manager system service:

```java
BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
boolean isCharging = batteryManager.isCharging();
```
### Getting Charging State with a Sticky Intent
Prior to Android 23+ we needed to use a sticky intent to get battery state. As we've seen, a normal, broadcasted intent will be broadcasted, possibly caught by an intent filter, and then disspear after it is processed. 
A **Sticky Intent** is a broadcast intent that sticks around, allowing our app to access it at any point and get information from the broadcasted intent. In Android, a sticky intent is where the current battery state is saved.

```java
IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
Intent batteryStatus = context.registerReceiver(null, ifilter);
```
The intent filter here is the intent filter for the sticky intent Intent.ACTION_BATTERY_CHANGED. The `registerReceiver` method will return an intent, and it is that intent which has all of the battery information, which we can use:

```java
boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
```
```java 
@Override
    protected void onResume() {
        super.onResume();

        // Check if you are on Android M or later, if so...
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Get a BatteryManager instance using getSystemService()
            BatteryManager manager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            // Call isCharging on the battery manager and pass the result on to your show
            // charging method
            showCharging(manager.isCharging());
        // If your user is not on M+, then...
        } else {
            //  Create a new intent filter with the action ACTION_BATTERY_CHANGED. 
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

            // Set a new Intent object equal to what is returned by registerReceiver, passing in null
            // for the receiver. Pass in your intent filter as well. Passing in null means that you're
            // getting the current state of a sticky broadcast - the intent returned will contain the
            // battery information you need.
            Intent batteryStatus = registerReceiver(null, intentFilter);

            // Get the integer extra BatteryManager.EXTRA_STATUS. Check if it matches
            // BatteryManager.BATTERY_STATUS_CHARGING or BatteryManager.BATTERY_STATUS_FULL. 
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            // Update the UI using your showCharging method
            showCharging(isCharging);
        }
```

