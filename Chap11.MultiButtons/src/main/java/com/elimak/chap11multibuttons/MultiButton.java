package com.elimak.chap11multibuttons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

/**
 * Created by elimak on 10/11/13.
 */
public class MultiButton extends LinearLayout implements OnClickListener {

    private static final int DEFAULT = -1;

    private CompoundButton mSelectedButton;
    private OnMultiButtonCheckedListener mListener;


// ****************** //
// Interface          //
// ****************** //

    public interface OnMultiButtonCheckedListener{
        public void onMultiButtonChecked(CompoundButton checked, CompoundButton unchecked);
    }

// ****************** //
// Constructors       //
// ****************** //

    public MultiButton(Context context, int textArrayResourceId) {
        super(context);
        init(context, null, textArrayResourceId);
    }

    public MultiButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    @SuppressLint("NewApi")
    public MultiButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, -1);
    }

// ****************** //
// Override methods   //
// ****************** //

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)){
            super.onRestoreInstanceState(state);
            return;
        }

        final SavedState ourState = (SavedState) state;
        super.onRestoreInstanceState(ourState.getSuperState());
        mSelectedButton.setChecked(false);
        mSelectedButton = (CompoundButton) getChildAt(ourState.selectedPosition);
        mSelectedButton.setChecked(true);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState ourState = new SavedState(superState);
        final int childCount = getChildCount();

        for(int i=0;i < childCount; i++){
            if(getChildAt(i)== mSelectedButton){
                ourState.selectedPosition = i;
                break;
            }
        }
        return ourState;
    }

    // ****************** //
// Private methods    //
// ****************** //

/**
 * Initialization
 * ----------------
 */
    private void init(Context context, AttributeSet attrs, int textArrayResourceId) {
        setSaveEnabled(true); // keep the state of the component

        final CharSequence[] textArray;
        int buttonBackground = DEFAULT;
        int buttonBackgroundEnd = DEFAULT;

        ColorStateList textColors = null;

        if(attrs == null){
            textArray = context.getResources().getTextArray(textArrayResourceId);
        }
        else{
            final TypedArray customAttrs = context.obtainStyledAttributes(attrs, R.styleable.MultiButton);
            buttonBackground = customAttrs.getResourceId(R.styleable.MultiButton_buttonBackground, DEFAULT);
            buttonBackgroundEnd = customAttrs.getResourceId(R.styleable.MultiButton_buttonBackgroundEnd, DEFAULT);
            if (buttonBackgroundEnd == DEFAULT) {
                buttonBackgroundEnd = buttonBackground;
            }
            textColors = customAttrs.getColorStateList(R.styleable.MultiButton_android_textColor);
            textArray = customAttrs.getTextArray(R.styleable.MultiButton_textArray);
            customAttrs.recycle();
        }

        if(textArray == null){
            throw new InflateException("MultiButton requires a textArray");
        }

        // Loop through CharSequences to create ToggleButtons
        for(int i=0; i< textArray.length; i++){
            final CharSequence text = textArray[i];
            final ToggleButton button = new ToggleButton(context);
            addView(button);
            button.setOnClickListener(this);
            button.setText(text);
            button.setTextOff(text);
            button.setTextOn(text);

            // Add weight
            LayoutParams lp = (LayoutParams) button.getLayoutParams();
            lp.weight = 1f;

            if(textColors != null){
                button.setTextColor(textColors);
            }

            // Assign custom backgrounds
            if(i == textArray.length -1){
                if(buttonBackgroundEnd != DEFAULT){
                    button.setBackgroundResource(buttonBackgroundEnd);
                }
            }
            else{
                if(buttonBackground != DEFAULT){
                    button.setBackgroundResource(buttonBackground);
                }
            }

            // Obtain reference to first button
            if(mSelectedButton == null){
                mSelectedButton = button;
                mSelectedButton.setChecked(true);
            }
        }
    }

// ****************** //
// Implement interface
// ****************** //

    @Override
    public void onClick(View view) {
        final CompoundButton button = (CompoundButton) view;
        button.setChecked(true);

        if(view != mSelectedButton){
            mSelectedButton.setChecked(false);
            if(mListener != null){
                mListener.onMultiButtonChecked(button, mSelectedButton);
            }
            mSelectedButton = button;
        }
    }

// ****************** //
// Public API         //
// ****************** //

    public void setOnMultiButtonCheckedListener(OnMultiButtonCheckedListener listener) {
        mListener = listener;
    }

    public CompoundButton getCheckedButton() {
        return mSelectedButton;
    }

// *********************** //
// Static internal Class   //
// *********************** //

    private static class SavedState extends BaseSavedState {

        private int selectedPosition;

        @SuppressWarnings("unused")
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        public SavedState(Parcel source) {
            super(source);
            selectedPosition = source.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(selectedPosition);
        }
    }

}
