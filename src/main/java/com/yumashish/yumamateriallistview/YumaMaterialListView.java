package com.yumashish.yumamateriallistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.view.menu.MenuBuilder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

/**
 * Created by lightning on 1/6/16.
 */
public class YumaMaterialListView extends ExpandableListView implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener, AdapterView.OnItemLongClickListener {
    public static String TAG = "YumaMaterialListView";
    YumaMaterialListViewAdapter mAdapter;
    Context mContext;

    public interface RespondToID {
        boolean Respond(int id);
    }

    RespondToID mOnClick, mOnLongClick;

    public YumaMaterialListView(Context context) {
        super(context);
    }

    public void setDefaultResponders() {
        mOnClick = new RespondToID() {
            @Override
            public boolean Respond(int id) {
                Log.i(TAG, "Clicked on ID: " + mContext.getResources().getResourceName(id));
                return false;
            }
        };
        mOnLongClick = new RespondToID() {
            @Override
            public boolean Respond(int id) {
                Log.i(TAG, "Long Clicked on ID: " + mContext.getResources().getResourceName(id));
                return false;
            }
        };
    }

    public void setOnClickResponder(RespondToID responder) {
        mOnClick = responder;
    }

    public void setOnLongClickResponder(RespondToID reponder) {
        mOnLongClick = reponder;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return mOnClick.Respond(((MenuItem) mAdapter.getGroup(groupPosition)).getSubMenu().getItem(childPosition).getItemId());
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        MenuItem item = ((MenuItem) mAdapter.getGroup(groupPosition));
        if(item == null) return false;
        return mOnClick.Respond(item.getItemId());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final long packedPosition = getExpandableListPosition(position);
        int itemType = ExpandableListView.getPackedPositionType(packedPosition);
        if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
            int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
            return mOnLongClick.Respond(((MenuItem) mAdapter.getGroup(groupPosition))
                    .getSubMenu().getItem(childPosition).getItemId());
        } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
            MenuItem item = ((MenuItem) mAdapter.getGroup(groupPosition));
            if(item == null) return false;
            return mOnLongClick.Respond(item.getItemId());
        } else {
            return false;
        }
    }

    class StyleInfo {
        int seperatorTextColor, groupTextColor, childTextColor,
                seperatorBackgroundColor, groupBackgroundColor, childBackgroundColor;
        float seperatorTextSize, groupTextSize, childTextSize;
    }

    public YumaMaterialListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.YumaMaterialListView);

        StyleInfo sinfo = new StyleInfo();
        sinfo.seperatorTextColor = attrArray.getColor(R.styleable.YumaMaterialListView_ysSeperatorTextColor, 0);
        sinfo.groupTextColor = attrArray.getColor(R.styleable.YumaMaterialListView_ysGroupTextColor, 0);
        sinfo.childTextColor = attrArray.getColor(R.styleable.YumaMaterialListView_ysChildTextColor, 0);
        sinfo.seperatorBackgroundColor = attrArray.getColor(R.styleable.YumaMaterialListView_ysSeperatorBackgroundColor, 0);
        sinfo.groupBackgroundColor = attrArray.getColor(R.styleable.YumaMaterialListView_ysGroupBackgroundColor, 0);
        sinfo.childBackgroundColor = attrArray.getColor(R.styleable.YumaMaterialListView_ysChildBackgroundColor, 0);
        sinfo.seperatorTextSize = attrArray.getDimension(R.styleable.YumaMaterialListView_ysSeperatorTextSize, -1);
        sinfo.groupTextSize = attrArray.getDimension(R.styleable.YumaMaterialListView_ysGroupTextSize, -1);
        sinfo.childTextSize = attrArray.getDimension(R.styleable.YumaMaterialListView_ysChildTextSize, -1);
        try {
            int menuId = attrArray.getResourceId(R.styleable.YumaMaterialListView_ysMenuItems, 0);
            if(menuId > 0) {
                Menu menu = new MenuBuilder(context);
                MenuInflater mi = new MenuInflater(context);
                mi.inflate(menuId, menu);

                int specialViewId = attrArray.getResourceId(R.styleable.YumaMaterialListView_ysSpecialView, 0);

                //get the seperator array
                CharSequence[] seperators = attrArray.getTextArray(R.styleable.YumaMaterialListView_ysMenuSeperators);

                mAdapter = new YumaMaterialListViewAdapter(context, menu, specialViewId, seperators, sinfo);
                setAdapter(mAdapter);
            }
        } finally {
            attrArray.recycle();
        }
        setGroupIndicator(null);
        setOnChildClickListener(this);
        setOnGroupClickListener(this);
        setOnItemLongClickListener(this);
        setDefaultResponders();
    }
}
