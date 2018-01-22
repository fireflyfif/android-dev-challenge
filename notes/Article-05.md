# Lesson: Polishing the UI

## Index

## Android Design Principles
Users judge the quality of your app within the first 30 seconds. It may not be fair, but, a disproportionate amount of that judgement will be based  not on the functionality, but on the visual aesthetics.
- Does it look polished? 
- Does it look professional?
- How easy is it to use?

More than just looking pretty,  the entire user experience is critically important.

>It should be fun to use.

>It should surprise in delightful ways through subtle animations and smooth transitions that contribute to a feeling of power and effortlessness.

>It should let users touch and interact with objects directly rather than having to use buttons and menus.

>It should use rich imagery and pictures in place of lots of words and long sentences.

## Visual Mocks and Keylines

### Mock up
A mock up is a model of an Android app that's used for design evaluation. It's usually a picture or animation of the final app. And detailed mock ups include specific colors to use, and markings called **keylines**.

### Keylines
Keylines are used to specify the exact size and spacing for components in an app layout.
Material design for Android is a set of principles and guides for creating useful and beautiful visuals and interaction across platforms and devices.

## Styles and Themes

### Styles
A style is an xml resource file, found in the values folder, where you can set all of these properties in one place. Then later we can apply that one style to any view we want.

### Themes
A theme is just a style that's applied to an entire activity or application and not just one view.

### Responsive Design
Responsive design is the act of creating designs that respond to and work well on a variety of screen sizes, shapes and orientation.

## Screen Density
Density is the number of pixels in the physical area of the screen, and it's often measured in dots per inch, or `dpi`. There are five density ranges for Android, which most Android devices fall into.

### Density Buckets
There is:
- **mdpi** (medium) ~ 160dpi
- **hdpi** (high) ~240dpi
- **xhdpi** (extra-high) ~320dpi
- **xxhdpi** (extra-extra-high) ~480dpi
- **xxxhdpi** (extra-extra-extra-high) ~640dpi

### Density-Independent Pixels
Density-independent pixels are the same physical size on each device. So in general,  dps  are used for spacing and images, so that we can design for size and not pixel by pixel.

![Density Buckets](https://github.com/fireflyfif/android-dev-challenge/blob/master/assets/density_buckets.png)

Providing correctly sized images for common density buckets helps us achieve good graphical quality and performance across different devices.

### Smallest Width Qualifier
By using the smallest width qualifier on new layout folders, Android lets us create alternate layout resources for each of these devices based on their size, and it lets us pick which default layout resources to override.

## Color State List Resource
A `ColorStateList` is an object you can define in XML that you can apply as a color, but will actually change colors, depending on the state of the View object to which it is applied. For example, a Button widget can exist in one of several different states (pressed, focused, or neither) and, using a color state list, you can provide a different color during each state.

We can describe the state list in an XML file. Each color is defined in an `<item>` element inside a single `<selector>` element. Each `<item>` uses various attributes to describe the state in which it should be used.

```xml
<selector xmlns:android="http://schemas.android.com/apk/res/android">
   <item android:drawable="@color/colorPrimaryDark"   android:state_pressed="true" />
   <item android:drawable="@color/colorPrimaryDark"   android:state_activated="true" />
   <item android:drawable="@color/colorPrimaryDark" android:state_selected="true" />
   <item android:drawable="@color/colorPrimary" />
</selector>
```

```xml
<!-- This selector works everywhere, but does not support ripple -->
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@color/activated"
          android:state_activated="true"
          android:state_focused="false"/>

    <item android:drawable="@color/background_material_light"
          android:state_window_focused="false"/>
    <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
      - Even though these two point to the same resource, have two states so the drawable will   -
      - invalidate itself when coming out of pressed state.                                      -
      - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
    <item android:drawable="@drawable/abc_list_selector_holo_light"
          android:state_enabled="false"
          android:state_focused="true"
          android:state_pressed="true"/>

    <item android:drawable="@drawable/abc_list_selector_holo_light"
          android:state_enabled="false"
          android:state_focused="true"/>

    <item android:drawable="@drawable/abc_list_selector_holo_light"
          android:state_focused="true"
          android:state_pressed="true"/>

    <item android:drawable="@drawable/abc_list_selector_holo_light"
          android:state_focused="false"
          android:state_pressed="true"/>

    <item android:drawable="@drawable/abc_list_selector_holo_light"
          android:state_focused="true"/>

    <!-- Default state with no interaction -->
    <item android:drawable="@color/background_material_light"/>
</selector>
```




