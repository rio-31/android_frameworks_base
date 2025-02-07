/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.qs;

import android.annotation.ColorInt;
import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.settingslib.Utils;
import com.android.settingslib.graph.SignalDrawable;
import com.android.systemui.R;

public class QSCarrier extends LinearLayout {

    private View mMobileGroup;
    private TextView mCarrierText;
    private ImageView mMobileSignal;
    private ImageView mMobileRoaming;
    private TextView mMobileType;
    // A View with just a space to separate the carrier label and type of signal
    private TextView mSpacer;
    private ImageView mVolte;

    public QSCarrier(Context context) {
        super(context);
    }

    public QSCarrier(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QSCarrier(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public QSCarrier(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMobileGroup = findViewById(R.id.mobile_combo);
        mMobileSignal = findViewById(R.id.mobile_signal);
        mMobileRoaming = findViewById(R.id.mobile_roaming);
        mMobileType = findViewById(R.id.mobile_type);
        mSpacer = findViewById(R.id.spacer);
        mVolte = findViewById(R.id.mobile_volte);
        mCarrierText = findViewById(R.id.qs_carrier_text);
    }

    public void updateState(QSCarrierGroup.CellSignalState state) {
        mMobileGroup.setVisibility(state.visible ? View.VISIBLE : View.GONE);
        if (state.visible) {
            mMobileRoaming.setVisibility(state.roaming ? View.VISIBLE : View.GONE);
            mMobileSignal.setImageDrawable(new SignalDrawable(mContext));
            mMobileSignal.setImageLevel(state.mobileSignalIconId);

            StringBuilder contentDescription = new StringBuilder();
            if (state.contentDescription != null) {
                contentDescription.append(state.contentDescription).append(", ");
            }

            if (state.typeId != 0 && state.typeContentDescription != null) {
                mMobileType.setText(state.typeContentDescription);
                mMobileType.setContentDescription(state.typeContentDescription);
                mMobileType.setVisibility(View.VISIBLE);
                mSpacer.setVisibility(View.VISIBLE);
            } else {
                mMobileType.setVisibility(View.GONE);
                mSpacer.setVisibility(View.GONE);
            }

            if (state.volteId != 0) {
                mVolte.setImageResource(state.volteId);
                mVolte.setVisibility(View.VISIBLE);
            } else {
                mVolte.setVisibility(View.GONE);
            }

            if (state.roaming) {
                contentDescription
                        .append(mContext.getString(R.string.data_connection_roaming))
                        .append(", ");
            }
            // TODO: show mobile data off/no internet text for 5 seconds before carrier text
            if (hasValidTypeContentDescription(state.typeContentDescription)) {
                contentDescription.append(state.typeContentDescription);
            }
            mMobileSignal.setContentDescription(contentDescription);
        }
    }

    private boolean hasValidTypeContentDescription(String typeContentDescription) {
        return TextUtils.equals(typeContentDescription,
                mContext.getString(R.string.data_connection_no_internet))
                || TextUtils.equals(typeContentDescription,
                mContext.getString(R.string.cell_data_off_content_description))
                || TextUtils.equals(typeContentDescription,
                mContext.getString(R.string.not_default_data_content_description));
    }

    public void setCarrierText(CharSequence text) {
        mCarrierText.setText(text);
    }

    void setTint(@ColorInt int tintColor) {
        ColorStateList colorForegroundStateList = ColorStateList.valueOf(tintColor);
        mMobileType.setTextColor(colorForegroundStateList);
        mVolte.setImageTintList(colorForegroundStateList);
        mMobileRoaming.setImageTintList(colorForegroundStateList);
        mMobileSignal.setImageTintList(colorForegroundStateList);
        mCarrierText.setTextColor(tintColor);
    }
}
