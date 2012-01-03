package com.android.weather.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.weather.R;
import com.android.weather.utils.WeatherIconResourceMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.SimpleAdapter.ViewBinder;

public class WeatherInfoItemAdapter extends SimpleAdapter {
	
	private int[] mTo;
    private String[] mFrom;
    private ViewBinder mViewBinder;

    private List<? extends Map<String, ?>> mData;

    private int mResource;
    private int mDropDownResource;
    private LayoutInflater mInflater;
    private HashMap<String, Integer> resMap = null;
    private WeatherIconResourceMap weaObj;

	public WeatherInfoItemAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to,WeatherIconResourceMap weaObj,HashMap<String, Integer> resMap) {
		super(context, data, resource, from, to);
		mData = data;
        mResource = mDropDownResource = resource;
        mFrom = from;
        mTo = to;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resMap = resMap;
        this.weaObj = weaObj;
		// TODO Auto-generated constructor stub
	}

	//	public void setViewImage(ImageView v, Bitmap data) {
//		// TODO Auto-generated method stub
//		v.setImageBitmap(data);
//	}
	
	 public void setViewImage(ImageView v, int value) {
        v.setImageResource(value);
     }
	
	 public void setViewImage(ImageView v, String value) {
	        try {
	            v.setImageResource(Integer.parseInt(value));
	        } catch (NumberFormatException nfe) {
	            v.setImageURI(Uri.parse(value));
	        }
	    }
	 
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
    	return createViewFromResource(position, convertView, parent, mResource);
	}
    
    private View createViewFromResource(int position, View convertView,
            ViewGroup parent, int resource) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(resource, parent, false);
        } else {
            v = convertView;
        }

        bindView(position, v);

        return v;
    }
    
    
	private void bindView(int position, View view) {
        final Map dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }

        final ViewBinder binder = mViewBinder;
        final String[] from = mFrom;
        final int[] to = mTo;
        final int count = to.length;

        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {
                final Object data = dataSet.get(from[i]);
                String text = data == null ? "" : data.toString();
                if (text == null) {
                    text = "";
                }

                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, data, text);
                }

                if (!bound) {
                    if (v instanceof Checkable) {
                        if (data instanceof Boolean) {
                            ((Checkable) v).setChecked((Boolean) data);
                        } else if (v instanceof TextView) {
                            // Note: keep the instanceof TextView check at the bottom of these
                            // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                            setViewText((TextView) v, text);
                        } else {
                            throw new IllegalStateException(v.getClass().getName() +
                                    " should be bound to a Boolean, not a " +
                                    (data == null ? "<unknown type>" : data.getClass()));
                        }
                    } else if (v instanceof TextView) {
                        // Note: keep the instanceof TextView check at the bottom of these
                        // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                        setViewText((TextView) v, text);
                    } else if (v instanceof ImageView) {
                        if (data instanceof Integer) {
                            setViewImage((ImageView) v, (Integer) data);                            
                        } else {
//                            setViewImage((ImageView) v, (Bitmap)data);
//                            setViewImage((ImageView) v, text);
                            setViewImage((ImageView) v, weaObj.getIconResourceId(text));
                        }
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a " +
                                " view that can be bounds by this SimpleAdapter");
                    }
                }
            }
        }
    }
	
	

}
