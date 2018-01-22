# Lesson: Connect to the Internet

## Index

- [Logging](https://github.com/fireflyfif/android-dev-challenge/blob/master/ud851-Exercises-student/Lesson02-GitHub-Repo-Search/Article-02.md#logging)
- [Resources](https://github.com/fireflyfif/android-dev-challenge/blob/master/ud851-Exercises-student/Lesson02-GitHub-Repo-Search/Article-02.md#resources)
- [Menus](https://github.com/fireflyfif/android-dev-challenge/blob/master/ud851-Exercises-student/Lesson02-GitHub-Repo-Search/Article-02.md#menus)
- [Build URL](https://github.com/fireflyfif/android-dev-challenge/blob/master/ud851-Exercises-student/Lesson02-GitHub-Repo-Search/Article-02.md#build-url)
- [Fetching HTTP Request](https://github.com/fireflyfif/android-dev-challenge/blob/master/ud851-Exercises-student/Lesson02-GitHub-Repo-Search/Article-02.md#fetching-http-request)
- [Permissions](https://github.com/fireflyfif/android-dev-challenge/blob/master/ud851-Exercises-student/Lesson02-GitHub-Repo-Search/Article-02.md#permissions)
- [Thread Basics](https://github.com/fireflyfif/android-dev-challenge/blob/master/ud851-Exercises-student/Lesson02-GitHub-Repo-Search/Article-02.md#thread-basics)
- [AsyncTask](https://github.com/fireflyfif/android-dev-challenge/blob/master/ud851-Exercises-student/Lesson02-GitHub-Repo-Search/Article-02.md#asynctask)
- [JSON Format](https://github.com/fireflyfif/android-dev-challenge/blob/master/ud851-Exercises-student/Lesson02-GitHub-Repo-Search/Article-02.md#json-format)

## Logging
Reference: 
- [Log](https://developer.android.com/reference/android/util/Log.html)

Developers use log messages to communicate information about their applications. These messages can be fairly trivial 
like noting that an application has refreshed some data or indicate deeper problems like an error that crashes the application.

### Levels

- `Log.e(String, String)` (**ERROR**)
- `Log.w(String, String)` (**WARN**)
- `Log.i(String, String)` (**INFO**)
- `Log.d(String, String)` (**DEBUG**)
- `Log.v(String, String)` (**VERBOSE**)

Example:
A good convention is to declare a **TAG** constant in our class:
```java
private static final String TAG = "MyActivity";
Log.v(TAG, "index=" + i);
```

In terms of increasing verboseness in severity, **ERROR**, **WARN**, and **INFO** level messages are always preserved in release versions.
- Use **ERROR** to log obvious errors.
- **WARN** is best suited for things that don't make the  application **ERROR** or crash but remain a concern.
- **INFO** is commonly used for purely informational messages, like being connected to the Internet.
- Use **DEBUG** and **VERBOSE** log messages during development.
- **WTF** - Android has another very special logging level that is more severe than ERROR. It is called WTF for What a Terrible Failure. 
The WTF level is for errors that should never, ever, ever happen.

## Resources

Reference: 
- [Providing Resources](https://developer.android.com/guide/topics/resources/providing-resources.html)

#### What is the res Directory?
The **res** directory is where we should put things such as images, strings, and layouts. It's included in every Android project.

Inside of the **res** directory, are sub folder for the following types of resources. Here are some examples.

#### Different Resource Directories

| Name         | What's Stored Here |
| ------------ |:---------------------:|
| values       | XML files that contain simple values, such as string or integers |
| drawable     | A bunch of visual files, including Bitmap file types and shapes. |
| layouts      | XML layouts for our app |

#### Other Resource Types

| Name         | What's Stored Here |
| ------------ |:---------------------:|
| animator       | XML files for property animations |
| anim     | XML files for tween animations |
| color      | XML files that define state list colors |
| mipmap | Drawable files for launcher icons |
| menu     | XML files that define application menus |
| raw      | Resource file for arbitrary files saved in their raw form. For example, we could put audio files here. |
| xml | Arbitrary XML; if we have XML configuration files, this is a good place to put them |

#### Why Resources

We should always keep things like images and layouts separate in the res folder. Keeping resource files and values independent helps us 
easily maintain them if we need to update, say, all our button images to match a new style. The Android Framework also easily allows 
for alternative resources that support specific device configurations such as different languages or screen sizes. Providing a 
customized experience for users from different locations or on different devices becomes increasingly important as more of the world 
comes online and more devices come on the market. We will see how to provide alternate resources for different configurations and 
locals later in this course.

#### Using Resources in XML and Java

We've already seen resources in action. For example, in the MainActivity, we have already seen usage of resources. When we say 
`setContentView(R.layout.activity_main)`, we are referencing a resource (the `activity_main.xml`) file to use as the layout of 
`MainActivity`. 
That magical looking `R.layout` part of the expression above is actually a static class that is generated for us to reference 
resources in Java code.

#### Working with `strings.xml`

In Java, we can get a String saved in **res** -> **values** -> **strings.xml** by calling the `getString` method. If we’re in an Activity, 
we can just call `getString`, and pass in the String resource ID. The String resource ID can be found in the **strings.xml**
XML. For example, let's look at Sunshine's strings.xml file:

```XML
   <string name="today">Today</string>

    <!-- For labelling tomorrow's forecast [CHAR LIMIT=15] -->
    <string name="tomorrow">Tomorrow</string>

    <!-- Date format [CHAR LIMIT=NONE] -->
    <string name="format_full_friendly_date">
        <xliff:g id="month">%1$s</xliff:g>, <xliff:g id="day">%2$s</xliff:g>
    </string>
  ```
  
The id of the String with the value "Today" is `today` and the id of the String with the value `<xliff:g id="month">%1$s</xliff:g>, 
<xliff:g id="day">%2$s</xliff:g>` is `format_full_friendly_date`

If you wanted to reference the **Today** string, we would reference it in Java by doing something like this:

```java
String myString = getString(R.string.today);
```

In XML, we can access a String by using the `@string` accessor method. For the same String defined above, we could access it like this:

```java
<TextView text=”@string/today” />
```

## Menus

Reference: 
- [Menu API](https://developer.android.com/guide/topics/ui/menus.html)

Menus are a common user interface component in many types of applications. To provide a familiar and consistent user experience, 
we should use the Menu APIs to present user actions and other options in your activities.

Android provides a standard XML format to define menu items. Instead of building a menu in our activity's code, we should define a 
menu and all its items in an XML menu resource. Like this:

```XML
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/search"
          android:icon="@drawable/ic_search"
          android:title="@string/search"
          android:showAsAction="ifRoom"/>
</menu>
```

In code, to specify the options menu for an activity, we need to override `onCreateOptionsMenu()`. In this method, we can inflate our 
**menu** resource.

```java
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_name, menu);
    return true;
}
```

When the user selects an item from the options menu the system calls `onOptionsItemSelected()` method. This method passes the 
**MenuItem** selected. We can identify the item by calling `getItemId()`, which returns the unique ID for the menu item. We need to match this ID against known menu items to perform the appropriate action. 

For example:

```java
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
        case R.id.search:
            newSearch();
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
}
```

When we successfully handle a menu item, return true. If we don't handle the menu item, we should call the superclass implementation 
of `onOptionsItemSelected()`which returns `false`.

## Build URL

Resources:
- [Uri.Builder](https://developer.android.com/reference/android/net/Uri.Builder.html)
- [Android URI](https://developer.android.com/reference/java/net/URI.html)

#### Android URI
Uniform Resource Identifier (URI)

```java
URL url = null;
try{
   url = new URL(builtUri.toString());
} catch (MalformedURLException e) {
   e.printStackTrace();
}
```

A **URI** is a *Uniform Resource Identifier* while a **URL** is a *Uniform Resource Locator*. 

**Hence every URL is a URI, abstractly speaking, but not every URI is a URL.**

#### URI syntax and components

At the highest level a URI reference (hereinafter simply "URI") in string form has the syntax

`[scheme:]scheme-specific-part[#fragment]`

where square brackets [...] delineate optional components and the characters : and # stand for themselves.

#### Uri.Builder 

Example:

```java
Uri builtUri =
       Uri.parse(GITHUB_BASE_URL).buildUpon()
       .appendQueryParameter(PARAM_QUERY, githubSearchQuery)
       .appendQueryParameter(PARAM_SORT, sortBy)
       .build();
```

## Fetching HTTP Request

References:
- [Connecting to the Internet](https://developer.android.com/training/basics/network-ops/connecting.html)
- [HttpURLConnection](https://developer.android.com/reference/java/net/HttpURLConnection.html)
- [OkHttp](http://square.github.io/okhttp/)
- [Read/Convert an InputStream to a String](https://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string)

### Add Permission 

Now we are going to connect our app with Internet to get the data needed to show in our app. So the first part would be get the permission so we can access the Internet. Add an <uses-permission> to our app manifest:

`<uses-permission android:name="android.permission.INTERNET" />`

Example:

```java
public static String getResponseFromHttpUrl(URL url) throws IOException {
   HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
   try {
       InputStream in = urlConnection.getInputStream();

       Scanner scanner = new Scanner(in);
       scanner.useDelimiter("\\A");

       boolean hasInput = scanner.hasNext();
       if (hasInput) {
           return scanner.next();
       } else {
           return null;
       }
   } finally {
       urlConnection.disconnect();
   }
}
```

First we create the **Http URL** connection object. Then we get an `InputStream` from the `openConnection`. Next, we have to read the contents of this input stream. There are many ways to do this in Java, but we've chosen to do using **Scanner** (used to tokenize Streams, because it's simple and relatively fast). By setting the delimiter to `\A`, beginning of the Stream. We force the scanner to read the entire contents of the stream into the next token stream. It also handles the character encoding for us. Specifically, it translates from UTF-8 which is the default encoding for JSON and JavaScript to UTF-16, the format used by android. 

[Scanner](http://download.oracle.com/javase/8/docs/api/java/util/Scanner.html) iterates over tokens in the stream, and in this case we separate tokens using "beginning of the input boundary" (\A) thus giving us only one token for the entire contents of the stream.
Note, if you need to be specific about the input stream’s encoding, you can provide the second argument to Scanner constructor that indicates what charset to use (e.g. "UTF-8").

## Permissions
References:
- [System Permissions](https://developer.android.com/guide/topics/permissions/index.html)

By default, no app can access sensitive data or perform actions that could adversely affect other apps, the OS or users. Things like accessing the Internet, getting the user's location or 
reading or modifying the contacts database.

Certain sensitive informations must be acknowledged by the user when your application is running. The best practice is to request the absolute minimum number of permissions.

To make use of protected features of the device, you must include one or more `<uses-permission>` tags in your app manifest. For example:

```XML
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.android.app.myapp" >
    <!-- Permission for accessing the Internet 
    Allows applications to open network sockets. -->
    <uses-permission android:name="android.permission.INTERNET" />
    ...
</manifest>
```

Beginning in Android 6.0 (API level 23), users grant permissions to apps while the app is running, not when they install the app.

## Thread Basics

References:
- [Processes and Threads](https://developer.android.com/guide/components/processes-and-threads.html)
- [Android Threading: All You Need to Know](https://www.toptal.com/android/android-threading-all-you-need-to-know)

Each Android app can be divided into multiple threads of execution, that can all run concurrently. On modern Android devices, these threads of execution can be scheduled by the OS to run on different CPU cores. But the system might choose to time slice the amount of single CPU which means running each for a period of time. 

Android apps have a single user interface thread. When an application is launched in Android, it creates the first thread of execution, known as the main thread or UI thread. The main thread is responsible for dispatching events to the appropriate user interface widgets as well as communicating with components from the Android UI toolkit. 

Network operations and database calls, as well as loading of certain components, are common examples of operations that one should avoid in the main thread. When they are called in the main thread, they are called synchronously, which means that the UI will remain completely unresponsive until the operation completes. For this reason, they are usually performed in separate threads, which thereby avoids blocking the UI while they are being performed.

Android provides many ways of creating and managing threads, and many third-party libraries exist that make thread management a lot more pleasant.


## AsyncTask

**AsyncTask** allows us to run a task on a background thread while publishing results to the UI thread. The UI thread has a message queue and a handler that allows us to send a process runnable objects and messages, often from other threads. **AsyncTasks** should ideally be used for short operations (a few seconds at the most.) 

**AsyncTask** is a generic class. Meaning that it takes **Parameterized types** in its constructor.

The three types used by an **AsyncTask** are the following:

- **Params** - Parameter type sent to the task upon execution
- **Progress** - Type published to update progress during the background computation
- **Result** - The type of the result of the background computation

These three parameters correspond to three primary functions you can override in **AsyncTask**:

- `onPreExecute()` - invoked on the UI thread before the task is executed. This step is normally used to setup the task, for instance by showing a progress bar in the user interface.
1. `doInBackground(Params...)` -  invoked on the background thread immediately after `onPreExecute()` finishes executing. This step is used to perform background computation that can take a long time. The parameters of the asynchronous task are passed to this step. The result of the computation must be returned by this step and will be passed back to the last step. This step can also use `publishProgress(Progress...)` to publish one or more units of progress. These values are published on the UI thread, in the `onProgressUpdate(Progress...)` step.
2. `onProgressUpdate(Progress...)` - invoked on the UI thread after a call to publishProgress(Progress...). The timing of the execution is undefined. This method is used to display any form of progress in the user interface while the background computation is still executing. For instance, it can be used to animate a progress bar or show logs in a text field.
3. `onPostExecute(Result)` - invoked on the UI thread after the background computation finishes. The result of the background computation is passed to this step as a parameter.

Example:

```java
public class GithubQueryTask extends AsyncTask<URL, Void, String> {

   @Override
   protected String doInBackground(URL... urls) {
       URL searchUrl = urls[0];
       String githubSearchResults = null;
       try {
           githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
       } catch (IOException e) {
           e.printStackTrace();
       }
       return githubSearchResults;
   }

   @Override
   protected void onPostExecute(String s) {
       if (s != null && !s.equals("")) {
           mSearchResultsTextView.setText(s);
       }
       super.onPostExecute(s);
   }
}
```

Once created, a task is executed very simply:

`new GithubQueryTask().execute(githubSearchUrl);`

**AsyncTask**, however, falls short if we need our deferred task to run beyond the lifetime of the Activity/Fragment. It is worth noting that even something as simple as screen rotation can cause the activity to be destroyed.


## JSON Format

**Java Script Object Notation** - it’s the primary method for data exchange on the web, because its format is syntactically identical to the code of creating JavaScript objects. 

We use it in many other places, because like XML it’s human readable, but it is also more compact, easier to write and allows for the declaration of arrays.

Example:
```JSON
{
   "temp": {
      "min":"11.34",
      "max":"19.01"
   },
   "weather": {
      "id":"801",
      "condition":"Clouds",
      "description":"few clouds"
   },
   "pressure":"1023.51",
   "humidity":"87"
}
```

