package com.yumashish.yumamateriallistview;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;

/**
 * Created by lightning on 1/6/16.
 */
public class YumaMaterialListViewAdapter implements ExpandableListAdapter {
    public static long Combiner = 10000;
    Context mContext;
    Menu mMenu;
    int mSpecialViewId;
    HashSet<String> mSeperators;
    LayoutInflater mInflater;
    YumaMaterialListView.StyleInfo mStyleInfo;
    View specialView;

    public YumaMaterialListViewAdapter(Context context, Menu menu, int specialViewId, CharSequence[] seperators, YumaMaterialListView.StyleInfo styleInfo) {
        mContext = context;
        mMenu = menu;
        mSpecialViewId = specialViewId;
        mStyleInfo = styleInfo;

        mSeperators = new HashSet();
        for(CharSequence seperator : seperators) {
            mSeperators.add(seperator.toString());
        }

        mInflater = LayoutInflater.from(context);
    }

    public View getSpecialView() {
        return specialView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return (mSpecialViewId > 0) ? mMenu.size() + 1 : mMenu.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(mSpecialViewId > 0) {
            if(groupPosition == 0) return 0;
            groupPosition--;
        }
        MenuItem group = mMenu.getItem(groupPosition);
        if(group.hasSubMenu()) {
            return group.getSubMenu().size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        if(mSpecialViewId > 0) {
            if(groupPosition == 0) return null;
            groupPosition--;
        }
        return mMenu.getItem(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(mSpecialViewId > 0) {
            if(groupPosition == 0) return null;
            groupPosition--;
        }
        return mMenu.getItem(groupPosition).getSubMenu().getItem(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getCombinedChildId(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {


            if(mSpecialViewId > 0) {
                if(groupPosition == 0) {
                    specialView = mInflater.inflate(mSpecialViewId, parent, false);
                    return specialView;
                }
            }

            MenuItem item = (MenuItem) getGroup(groupPosition);
            //find if seperator
            if (mSeperators.size() > 0 && mSeperators.contains(mContext.getResources().getResourceName(item.getItemId()).split("/")[1])) {
                convertView = mInflater.inflate(R.layout.group_seperator_item, parent, false);
                TextView title = (TextView) convertView.findViewById(R.id.seperator_title);
                title.setText(item.getTitle());
                if (mStyleInfo.seperatorTextSize > -1)
                    title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mStyleInfo.seperatorTextSize);
                if (mStyleInfo.seperatorTextColor != 0)
                    title.setTextColor(mStyleInfo.seperatorTextColor);
                if (mStyleInfo.seperatorBackgroundColor != 0)
                    convertView.setBackgroundColor(mStyleInfo.seperatorBackgroundColor);
            } else {
                convertView = mInflater.inflate(R.layout.group_normal_item, parent, false);
                TextView title = (TextView) convertView.findViewById(R.id.group_title);
                title.setText(item.getTitle());
                if (mStyleInfo.groupTextSize > -1)
                    title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mStyleInfo.groupTextSize);
                if (mStyleInfo.groupTextColor != 0)
                    title.setTextColor(mStyleInfo.groupTextColor);
                if (mStyleInfo.groupBackgroundColor != 0)
                    convertView.setBackgroundColor(mStyleInfo.groupBackgroundColor);
                ((ImageView) convertView.findViewById(R.id.group_icon)).setImageDrawable(item.getIcon());
            }
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) {
            MenuItem item = (MenuItem) getChild(groupPosition, childPosition);

            convertView = mInflater.inflate(R.layout.child_item, parent, false);
            TextView title = (TextView) convertView.findViewById(R.id.child_title);
            title.setText(item.getTitle());
            if(mStyleInfo.childTextSize > -1)
                title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mStyleInfo.childTextSize);
            if(mStyleInfo.childTextColor != 0)
                title.setTextColor(mStyleInfo.childTextColor);
            if(mStyleInfo.childBackgroundColor != 0)
                convertView.setBackgroundColor(mStyleInfo.childBackgroundColor);
            ((ImageView) convertView.findViewById(R.id.child_icon)).setImageDrawable(item.getIcon());
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return mMenu.size() > 0;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        //nothing
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        //nothing
    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return (groupId * Combiner) + childId;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return groupId;
    }

    public long getGroupIdFromCombinedId(long combinedId) {
        return (int) (combinedId / Combiner);
    }

    public long getChildIdFromCombinedId(long combinedId) {
        return (int) (combinedId % Combiner);
    }
}
