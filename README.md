# YumaMaterialListView

A Fast way to make list views for navigation drawers

###Example of usage in layout XML:

><com.yumashish.yumamateriallistview.YumaMaterialListView
>            android:id="@+id/drawer_listview"
>            android:layout_width="match_parent"
>            android:layout_height="match_parent"
>            android:layout_gravity="start"
>            android:background="@color/colorPrimary"
>            android:divider="@android:color/transparent"
>            android:dividerHeight="0dp"
>            app:ysGroupTextColor="#fff"
>            app:ysSeperatorTextColor="#ff0"
>            app:ysChildTextColor="#0ff"
>            app:ysSeperatorTextSize="10sp"
>            app:ysGroupTextSize="12sp"
>            app:ysChildTextSize="12dp"
>            app:ysMenuItems="@menu/drawer_menu"
>            app:ysSpecialView="@layout/demo_special"
>            app:ysMenuSeperators="@array/drawer_categories_seperators">
></com.yumashish.yumamateriallistview.YumaMaterialListView>

###Attributes:

ysMenuItems: The menu where the items for this listview are defined.
ysSpecialView: This is a view you can put on top of the actual menu list (User information, for example)
ysMenuSeperaters: The seperators are smaller than normal cells/items
{$} := (Seperator|Group|Child)
ys{$}TextColor: The text color of all the {$}
ys{$}TextSize: The text size of all the {$}
ys{$}BackgroundColor: The background color of all the {$}

###Example of Menu:<drawer_menu.xml>

><?xml version="1.0" encoding="utf-8"?>
><menu xmlns:android="http://schemas.android.com/apk/res/android" xmlns:yuma="http://schemas.android.com/apk/res-auto">
>    <item android:id="@+id/first" android:icon="@android:drawable/ic_delete" android:title="First" />
>    <item android:id="@+id/second" android:icon="@android:drawable/ic_delete" android:title="Second" />
>    <item android:id="@+id/third" android:icon="@android:drawable/ic_delete" android:title="Third" />
>    <item android:id="@+id/fourth" android:icon="@android:drawable/ic_delete" android:title="Fourth" />
>    <item android:id="@+id/fifth" android:icon="@android:drawable/ic_delete" android:title="Fifth" />
>    <item android:id="@+id/sixth" android:icon="@android:drawable/ic_delete" android:title="Sixth">
>        <menu>
>            <item android:id="@+id/sixth_child" android:icon="@android:drawable/ic_dialog_alert" android:title="Child One"></item>
>        </menu>
>    </item>
></menu>

###Handling Menu Touch and Long Touch

Implement and set the RespondToID interface's Respond method. Respond accepts an int which represents the menu item's ID. There are default implementations that will will send the name of the menu item to Log.i.

>YumaMaterialListView v = (...get reference...);
>v.setOnClickResponder(new RespondToID() {
>    boolean Respond(int id) {
>        switch(id) {
>            case R.menu.first:
>                  //TODO handle click event on menu item first
>                  return true;
>            default:
>                  return true;
>            }
>    }
>});>

>v.setOnLongClickResponder(new RespondToID() {
>    boolean Respond(int id) {
>        switch(id) {
>            case R.menu.first:
>                  //TODO handle long click event on menu item first
>                  //TODO remember to consume the long click to prevent registering the click event as well
>                  return true;
>            default:
>                  return false;
>            }
>    }
>});

