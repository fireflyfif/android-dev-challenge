# Lesson : RecyclerView

## Index
- [Overview](https://github.com/fireflyfif/android-dev-challenge/new/master/ud851-Exercises-student/Lesson03-Green-Recycler-View#overview)
- [RecyclerView Cheat Sheet](https://github.com/fireflyfif/android-dev-challenge/new/master/ud851-Exercises-student/Lesson03-Green-Recycler-View#recyclerview-cheat-sheet)

## Resources
- [Using the RecyclerView](https://guides.codepath.com/android/using-the-recyclerview)
- [A First Glance at Android’s RecyclerView](https://www.grokkingandroid.com/first-glance-androids-recyclerview/)

## Overview
The `RecyclerView` is a new `ViewGroup` that is prepared to render any adapter-based view in a similar way. It is supposed to be the successor of `ListView` and `GridView`, and it can be found in the latest `support-v7` version. One of the reasons is that `RecyclerView` has a more extensible framework, especially since it provides the ability to implement both horizontal and vertical layouts. We use the `RecyclerView` widget when we have data collections whose elements change at runtime based on user action or network events.

If we want to use a `RecyclerView`, we will need to work with the following:

- `RecyclerView.Adapter` - To handle the data collection and bind it to the view
- `LayoutManager` - Helps in positioning the items
- `ItemAnimator` - Helps with animating the items for common operations such as Addition or Removal of item

Recycling of views is a very useful approach. It saves CPU resources in that we do not have to inflate new views all the time and it saves memory in that it doesn't keep plenty of invisible views around.

## RecyclerView Cheat Sheet

1. Add latest `RecyclerView` support to dependencies in the build gradle.

	`compile 'com.android.support:recyclerview-v7:26.1.0'`

2. Add a `RecyclerView` xml with an id to the activity layout xml.
	
```XML
  <android.support.v7.widget.RecyclerView
  	 android:id="@+id/rv_numbers"
  	 android:layout_width="match_parent"
   	 android:layout_height="match_parent"/>
```

3. Create a layout resource xml file for the `RecyclerView` item and give it an id.
4. Add the Views needed (such as TextViews, ImageViews) to be displayed in to the item xml and give them id’s.
5. Create a separate Adapter class that extends `RecyclerView.Adapter<MyAdapter.MyViewHolder>`
	
```java
public class GreenAdapter extends   RecyclerView.Adapter<GreenAdapter.NumberViewHolder> {}
```
	
6. Create a class that that extends `RecyclerView.Viewholder` and add it as an inner class of the Adapter class.

```java
class NumberViewHolder extends RecyclerView.ViewHolder {}
```

7. In the `viewHolder` class create variables for the item views .

```java
TextView listItemNumberView;
```

8. Create a `viewHolder` constructor that takes view as a parameter.

```java
public NumberViewHolder (View itemView) {}
```

9. In the `viewHolder` constructor call super then bind the item views variables to the item xml.

```java
super(itemView);
listItemNumberView = (TextView) itemView.findViewById(R.id.tv_item_number);
```

10. In the `viewHolder` create a method called `void bind(int listIndex)` { Set the itemViews values to the values of the object at listIndex}

```java
void bind(int listIndex) {
   listItemNumberView.setText(String.valueOf(listIndex));
```

11. In the Adaptor class create a variable for the DataSource.

```java
private int mNumberItems;
```

12. In the Adaptor class create a constructor that has a parameter of DataSource and set the variable of DataSource to the parameter.

```java
public GreenAdapter(int numberOfItems) {
   // TODO (3) Store the numberOfItems parameter in mNumberItems
   mNumberItems = numberOfItems;
}
```

13. In the Adaptor class create a setter for the DataSource variable and call `notifyDataSetChanged` within the setter to refresh `RecyclerView`.

14. In the Adaptor class override `onCreateViewHolder(ViewGroup parent, int viewType)`.

```java
@Override
public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {}
```

15. In the `onCreateViewHolder` method get view by using 
`View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.forcast_list_item, parent, false);`. 
Then return new `MyViewHolder(v)`;.

```java
Context context = parent.getContext();
int layoutId = R.layout.number_list_item;
LayoutInflater inflater = LayoutInflater.from(context);
boolean shouldAttachToParentImmediately = false;

View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
NumberViewHolder viewHolder = new NumberViewHolder(view);

return viewHolder;
```

16. In the Adaptor class override the `getItemCount` method.
17. In the `getItemCount` method check the dataSource for null if not return its length.

```java
@Override
public int getItemCount() {
   return mNumberItems;
}
```

18. In the Adaptor class override the `onBindViewholder` method and call `holder.bind(position)`;

```java
@Override
public void onBindViewHolder(NumberViewHolder holder, int position) {
      holder.bind(position);
}
```

19. In the Activity class create a variable for the DataSource. Don’t forget to initialise it at some point .

20. In the Activity create a variable for the adapter and one for the `RecyclerView`.

```java
private GreenAdapter mAdapter;
private RecyclerView mNumbersList;
```

21. In the Activity `onCreate` method set the `RecyclerView` variable to `RecyclerView XML` using `findViewById`.

```java
mNumbersList = findViewById(R.id.rv_numbers);
```

22. In the Activity `onCreate` method create a new `LayoutManager` and set it on the `RecyclerView`.

```java
LinearLayoutManager layoutManager = new LinearLayoutManager(this);
mNumbersList.setLayoutManager(layoutManager);
```

23. In the Activity `onCreate` method `SetHasfixedSize = true` on the `RecyclerView` to designate that the contents of the `RecyclerView` won't change an item's size

```java
mNumbersList.setHasFixedSize(true);
```

24. In the Activity `onCreate` method set the adapter variable to a new instance of your adapter class passing in the dataSource.

```java
mAdapter = new GreenAdapter(NUM_LIST_ITEMS);
```

25. In the Activity `onCreate` method set the adapter on the `RecyclerView`.

```java
mNumbersList.setAdapter(mAdapter);
```
