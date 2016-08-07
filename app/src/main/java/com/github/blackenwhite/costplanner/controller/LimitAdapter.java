package com.github.blackenwhite.costplanner.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.blackenwhite.costplanner.R;
import com.github.blackenwhite.costplanner.model.DateManager;
import com.github.blackenwhite.costplanner.model.LimitMonthly;
import com.github.blackenwhite.costplanner.model.LimitMonthlyStorage;

public class LimitAdapter extends ArrayAdapter<String> {
    private final LimitsActivity mContext;
    private final String[] mRemainMonths;
    private final String CURRENT_YEAR;

    public LimitAdapter(LimitsActivity context, String[] remainMonths) {
        super(context, -1, remainMonths);
        mContext = context;
        mRemainMonths = remainMonths;
        CURRENT_YEAR = String.valueOf(DateManager.get().getCurrentYear());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_limit, parent, false);
        TextView monthText = (TextView) rowView.findViewById(R.id.row_limit_month);
        TextView limitText = (TextView) rowView.findViewById(R.id.row_limit_monthly_value);
        monthText.setText(mRemainMonths[position]);

        String monthIndex = DateManager.get().getMonthIndex(mRemainMonths[position]).toString();
        final LimitMonthly limitMonthly = LimitMonthlyStorage.get(mContext).getLimitMonthly(
                CURRENT_YEAR,
                monthIndex);
        if (limitMonthly != null) {
            limitText.setText(String.valueOf(limitMonthly.getLimitValue()));
        } else {
            limitText.setVisibility(View.GONE);
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getInputLimitDialog(mRemainMonths[position], limitMonthly).show();
            }
        });

        return rowView;
    }
}
