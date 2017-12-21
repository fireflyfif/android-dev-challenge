# Lesson: Connect to the Internet

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
A good convention is to declare a **TAG** constant in your class:
```java
private static final String TAG = "MyActivity";
Log.v(TAG, "index=" + i);
```

In terms of increasing verboseness in severity, **ERROR**, **WARN**, and **INFO** level messages are always preserved in release versions.
- Use **ERROR** to log obvious errors.
- **WARN** is best suited for things that don't make your application **ERROR** or crash but remain a concern.
- **INFO** is commonly used for purely informational messages, like being connected to the Internet.
- Use **DEBUG** and **VERBOSE** log messages during development.
- **WTF** - Android has another very special logging level that is more severe than ERROR. It is called WTF for What a Terrible Failure. 
The WTF level is for errors that should never, ever, ever happen.

### Resources
Reference: 
- [Providing Resources](https://developer.android.com/guide/topics/resources/providing-resources.html)

#### What is the res Directory?
The **res** directory is where we should put things such as images, strings, and layouts. It's included in every Android project.

Inside of the **res** directory, are sub folder for the following types of resources. Here are some examples.

**Different Resource Directories**

| Name         | What's Stored Here |
| ------------ |:---------------------:|
| values       | XML files that contain simple values, such as string or integers |
| drawable     | A bunch of visual files, including Bitmap file types and shapes. |
| layouts      | XML layouts for your app |

**Other Resource Types**

| Name         | What's Stored Here |
| ------------ |:---------------------:|
| animator       | XML files for property animations |
| anim     | XML files for tween animations |
| color      | XML files that define state list colors |
| mipmap | Drawable files for launcher icons |
| menu     | XML files that define application menus |
| raw      | Resource file for arbitrary files saved in their raw form. For example, you could put audio files here. |
| xml | Arbitrary XML; if you have XML configuration files, this is a good place to put them |

**Why Resources**

We should always keep things like images and layouts separate in the res folder. Keeping resource files and values independent helps us 
easily maintain them if we need to update, say, all your button images to match a new style. The Android Framework also easily allows 
for alternative resources that support specific device configurations such as different languages or screen sizes. Providing a 
customized experience for users from different locations or on different devices becomes increasingly important as more of the world 
comes online and more devices come on the market. We will see how to provide alternate resources for different configurations and 
locals later in this course.

**Using Resources in XML and Java**

We've already seen resources in action. For example, in the MainActivity, we have already seen usage of resources. When we say 
`setContentView(R.layout.activity_main)`, we are referencing a resource (the `activity_main.xml`) file to use as the layout of 
`MainActivity`. 
That magical looking `R.layout` part of the expression above is actually a static class that is generated for us to reference 
resources in Java code.

**Working with `strings.xml`**

In Java, we can get a String saved in **res** -> **values** -> **strings.xml** by calling the `getString` method. If we’re in an Activity, 
we can just call `getString`, and pass in the String resource ID. The String resource ID can be found in the **strings.xml**
XML. For example, let's look at Sunshine's strings.xml file:

```java
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

### Menus

Reference: 
- [Menu API](https://developer.android.com/guide/topics/ui/menus.html)

Menus are a common user interface component in many types of applications. To provide a familiar and consistent user experience, 
we should use the Menu APIs to present user actions and other options in your activities.

Android provides a standard XML format to define menu items. Instead of building a menu in our activity's code, we should define a 
menu and all its items in an XML menu resource. Like this:

```java
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

When the user selects an item from the options menu the system calls `onOptionsItemSelected()` method. This method passes the **MenuItem**
selected. We can identify the item by calling `getItemId()`, which returns the unique ID for the menu item. We need to match this ID 
against known menu items to perform the appropriate action. 
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

### Build URL

Resources:
- [Uri.Builder](https://developer.android.com/reference/android/net/Uri.Builder.html)
- [Android URI](https://developer.android.com/reference/java/net/URI.html)

**Android URI**
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

**URI syntax and components**

At the highest level a URI reference (hereinafter simply "URI") in string form has the syntax

`[scheme:]scheme-specific-part[#fragment]`

where square brackets [...] delineate optional components and the characters : and # stand for themselves.

**Uri.Builder**

Example:

```java
Uri builtUri =
       Uri.parse(GITHUB_BASE_URL).buildUpon()
       .appendQueryParameter(PARAM_QUERY, githubSearchQuery)
       .appendQueryParameter(PARAM_SORT, sortBy)
       .build();
```


