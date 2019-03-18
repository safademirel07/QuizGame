package com.safademirel.quizgame.Activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


import com.safademirel.quizgame.R;

import java.util.ArrayList;
import java.util.List;


public class Ask extends AppCompatActivity {

    protected HorizontalBarChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);



        final int a = getIntent().getIntExtra("mA",1);
        final int b = getIntent().getIntExtra("mB",2);
        final int c = getIntent().getIntExtra("mC",3);
        final int d = getIntent().getIntExtra("mD",4);

        mChart = (HorizontalBarChart) findViewById(R.id.chart1);
        // mChart.setHighlightEnabled(false);

        mChart.setDrawBarShadow(false);


        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        mChart.setDrawGridBackground(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setTextSize(18);
        xl.setGranularity(10f);

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setEnabled(false);
        yl.setDrawGridLines(true);
        yl.setTextColor(Color.WHITE);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = mChart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setTextColor(Color.WHITE);
        yr.setTextSize(18);
        yr.setEnabled(false);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        setData(a,b,c,d);
        mChart.setFitBars(true);
        mChart.animateY(2500);


        mChart.getLegend().setEnabled(false);   // Hide the legend




        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setGravity(Gravity.CENTER);

    }

    private void setData(int a,int b,int c,int d) {

        float barWidth = 9f;
        float spaceForBar = 10f;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();



        yVals1.add(new BarEntry(30, a,"A"));
        yVals1.add(new BarEntry(20, b,"B"));
        yVals1.add(new BarEntry(10, c,"C"));
        yVals1.add(new BarEntry(0, d,"D"));



        BarDataSet set1;


        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");
            set1.setDrawIcons(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(barWidth);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(16);
            mChart.getXAxis().setValueFormatter(new BarValueFormatter(set1));
            mChart.setData(data);
        }
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("A");
        xAxis.add("B");
        xAxis.add("C");
        xAxis.add("D");
        return xAxis;
    }

    public class BarValueFormatter implements IAxisValueFormatter {
        private final DataSet mData;

        public BarValueFormatter(DataSet data) {
            mData = data;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (value == 30)
                return "A";
            else if (value == 20)
                return "B";
            else if (value == 10)
                return "C";
            else if (value == 0)
                return "D";
            else
                return "";
        }
    }


}
