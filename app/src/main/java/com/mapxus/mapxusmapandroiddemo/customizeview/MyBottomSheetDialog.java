package com.mapxus.mapxusmapandroiddemo.customizeview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

/**
 * Created by Edison on 2020/9/1.
 * Describe:
 */
public class MyBottomSheetDialog extends BottomSheetDialog {
    public MyBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void show() {
        super.show();
        FrameLayout bottomSheet = findViewById(com.google.android.material.R.id.design_bottom_sheet);
        ViewGroup.LayoutParams originLayoutParams = Objects.requireNonNull(bottomSheet).getLayoutParams();
        originLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        bottomSheet.setLayoutParams(originLayoutParams);
        BottomSheetBehavior<?> mDialogBehavior = BottomSheetBehavior.from(bottomSheet);
        mDialogBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public View setStyle(int styleRes, Context context) {
        View bottomSheetDialogView = LayoutInflater.from(context).inflate(styleRes, null);
        setContentView(bottomSheetDialogView);
        if (getWindow() != null) {
            getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        }
        getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        getBehavior().setSkipCollapsed(true);
        setCanceledOnTouchOutside(false);
        show();
        return bottomSheetDialogView;
    }
}
