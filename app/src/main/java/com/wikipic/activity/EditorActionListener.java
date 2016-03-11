package com.wikipic.activity;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.wikipic.util.Utils;

public class EditorActionListener implements TextView.OnEditorActionListener {
    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            // Hide keyboard when search button on keyboard is clicked.
            Utils.hideKeyboard(view, view.getContext());
            return true;
        }
        return false;
    }
}
