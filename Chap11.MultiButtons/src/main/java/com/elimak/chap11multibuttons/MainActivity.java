package com.elimak.chap11multibuttons;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.elimak.chap11multibuttons.MultiButton.OnMultiButtonCheckedListener;

public class MainActivity extends Activity implements OnMultiButtonCheckedListener {

    private MultiButton mMultiButton;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text_view);
        mMultiButton = (MultiButton) findViewById(R.id.multi_button);
        mMultiButton.setOnMultiButtonCheckedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTextView.setText(mMultiButton.getCheckedButton().getText());
    }

    @Override
    public void onMultiButtonChecked(CompoundButton checked, CompoundButton unchecked) {
        mTextView.setText(checked.getText());
    }
}
