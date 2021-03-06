package me.barrasso.android.volume.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import me.barrasso.android.volume.LogUtils;
import me.barrasso.android.volume.R;
import me.barrasso.android.volume.utils.Accountant;

public class ProCheckBoxPreference extends CheckBoxPreference
        implements Preference.OnPreferenceChangeListener {

    private Accountant mAccountant;
    private String sku;

    public ProCheckBoxPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAccountant = Accountant.getInstance(getContext());
        init(attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProCheckBoxPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mAccountant = Accountant.getInstance(getContext());
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        LogUtils.LOGI(ProCheckBoxPreference.class.getSimpleName(), "init(" + defStyle + ')');
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProCheckBoxPreference, defStyle, 0);
        try {
            sku = a.getString(R.styleable.ProCheckBoxPreference_sku);
        } finally {
            a.recycle();
        }
        setOnPreferenceChangeListener(this);
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        LogUtils.LOGI(ProCheckBoxPreference.class.getSimpleName(), "onBindView(" + sku + ')');
        View banner = view.findViewById(R.id.ribbon);
        if (null != mAccountant && !TextUtils.isEmpty(sku) && mAccountant.has(sku) == Boolean.TRUE) {
            if (null != banner) {
                LogUtils.LOGI(ProCheckBoxPreference.class.getSimpleName(), "Removing Go Pro banner.");
                banner.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference pref, Object value) {
        mAccountant = Accountant.getInstance(getContext());
        LogUtils.LOGI(ProCheckBoxPreference.class.getSimpleName(),
                "onPreferenceChange(" + String.valueOf(value) + "), mAccountant = " + mAccountant);
        if (mAccountant.has(sku) == Boolean.TRUE) {
            return true;
        } else {
            mAccountant.buy((Activity) getContext(), sku);
            return false;
        }
    }
}