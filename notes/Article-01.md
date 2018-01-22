# Lesson: Create Project Sunshine

## Index
- [MinSDK](https://github.com/fireflyfif/android-dev-challenge/blob/master/ud851-Exercises-student/Lesson01-Favorite-Toys/Article-01.md#minsdk)
- [TargetSDK](https://github.com/fireflyfif/android-dev-challenge/blob/master/ud851-Exercises-student/Lesson01-Favorite-Toys/Article-01.md#targetsdk)
- [Android Debug Bridge](https://github.com/fireflyfif/android-dev-challenge/blob/master/ud851-Exercises-student/Lesson01-Favorite-Toys/Article-01.md#android-debug-bridge)
- [Constraint Layout](https://github.com/fireflyfif/android-dev-challenge/blob/master/ud851-Exercises-student/Lesson01-Favorite-Toys/Article-01.md#constraint-layout-tutorial)

## MinSDK
Default settings: `minSdkVersion 14`

The minSDK is the lowest SDK level that the app can run on. We can choose what level of devices to support. Setting the minSDK acts as a filter -- Google Play won’t show the app on devices running an Android platform version lower than the minimum SDK version.

## TargetSDK
Default settings: `targets the latest release`

The targetSDK is NOT a high pass filter -- it’s used only to declare which platform version we've tested our app on. An app targeted to a certain API or Android version will continue to be forward compatible on future releases -- the platform uses the target SDK values in case a future release makes a significant change to expected behavior, ensuring our app doesn’t break when a user’s phone gets upgraded. 

## Android Debug Bridge
ADB is a command line utility included with Android's SDK. [Link](https://developer.android.com/studio/command-line/adb.html?utm_source=udacity&utm_medium=mooc&utm_term=android&utm_content=adb&utm_campaign=training#wireless)

### Connect to a device over **Wi-Fi**

1. Connect the Android device and adb host computer to a common Wi-Fi network accessible to both. Beware that not all access points are suitable; we might need to use an access point whose firewall is configured properly to support adb.
2. Connect the device to the host computer with a USB cable.
3. Set the target device to listen for a TCP/IP connection on port 5555.

`adb tcpip 5555`

4. Disconnect the USB cable from the target device.
5. Connect to the device by its IP address.

`adb connect device_ip_address`

6. Confirm that the host computer is connected to the target device:

``` java
adb devices
List of devices attached
device_ip_address:5555 device
```

## Constraint Layout Tutorial
[Link to the tutorial](https://codelabs.developers.google.com/codelabs/constraint-layout/#0)

### The Infer Constraints tool

The **Infer Constraints** tool infers, or figures out, the constraints we need to match a rough layout of elements. It works by taking into account the positions and sizes of the elements. Drag elements to the layout in the positions we want them, and use the Infer Constraints tool to automatically create the constraint connections.

#### What's the difference between *Inference* and *Autoconnect*?
The **Infer Constraints**  tool calculates and sets constraints for all of the elements in a layout, rather than just the selected element. It bases its calculations on inferred relationships between the elements.

The Autoconnect  tool creates constraint connections for a selected element to the element's parent.

As soon as we constrain one dimension to be `match_constraint`, the *Toggle Aspect Ratio Constraint* option appears in the inspector view in the top left corner of the square.

Click the *Toggle Aspect Ratio Constraint* option. The ratio entry box appears below the bottom right corner of the square:

By using *ratios* we can ensure our designs stay perfect while allowing images to be resized on different device screens.

**Barriers** allow us to specify a constraint based on multiple UI elements. we'll want to use barriers any time that multiple elements could dynamically change their size based on user input or language.
