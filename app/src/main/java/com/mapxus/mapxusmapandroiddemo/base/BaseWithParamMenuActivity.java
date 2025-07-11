package com.mapxus.mapxusmapandroiddemo.base;

import android.view.Menu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mapxus.mapxusmapandroiddemo.R;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * Created by Edison on 2020/9/1.
 * Describe:
 */
public abstract class BaseWithParamMenuActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map_params, menu);
        TextView tvParams = (TextView) menu.findItem(R.id.params).getActionView();
        tvParams.setText(getString(R.string.params));
        tvParams.setTextSize(AutoSizeUtils.sp2px(this, 4));
        tvParams.setPadding(0, 0, AutoSizeUtils.dp2px(this, 5), 0);
        tvParams.setTextColor(getResources().getColor(R.color.white));
        tvParams.setOnClickListener(v -> {
            if (isDestroyed() || isFinishing()) return;
            initBottomSheetDialog();
        });
        return true;
    }

    protected abstract void initBottomSheetDialog();
}
