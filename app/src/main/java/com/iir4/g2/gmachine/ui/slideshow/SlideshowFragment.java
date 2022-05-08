package com.iir4.g2.gmachine.ui.slideshow;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.iir4.g2.gmachine.R;
import com.iir4.g2.gmachine.databinding.FragmentDashboardBinding;
import com.iir4.g2.gmachine.databinding.FragmentSlideshowBinding;
import com.iir4.g2.gmachine.models.MarqueMachineData;
import com.iir4.g2.gmachine.ui.dashboard.DashboardViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class SlideshowFragment extends Fragment {
    private FragmentSlideshowBinding binding;
    private static final String urlGetAchatYear = "http://10.0.2.2:8090/machines/byYear";
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries ;
    ArrayList<String> labelNames;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        labelNames = new ArrayList<>();
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getEntries();
        lineChart = root.findViewById(R.id.lineChart);
        return root;
    }
    private void getEntries() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlGetAchatYear, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", response);
                JSONArray Jarray = null;
                try {
                    Jarray = new JSONArray(response);
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONArray object = Jarray.optJSONArray(i);
                        Log.d("TAG", object.toString());
                        lineEntries = new ArrayList<>();
                        labelNames.add(String.valueOf(object.getInt(1)));
                        lineEntries.add(new Entry(object.getInt(0), object.getInt(1)));
                        lineDataSet = new LineDataSet(lineEntries, "");
                        lineData = new LineData(lineDataSet);
                        lineChart.setData(lineData);
                        lineDataSet.setColors(ColorTemplate.PASTEL_COLORS);
                        lineDataSet.setValueTextColor(Color.BLACK);
                        lineDataSet.setValueTextSize(18f);
                        lineChart.animateY(2000);
                        lineChart.getDescription().setText("Nombre de vente par An");
                        lineChart.getDescription().setTextSize(15);
                        lineChart.getDescription().setTextColor(getResources().getColor(R.color.gray));
                        XAxis xAxis = lineChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));
                        xAxis.setPosition(XAxis.XAxisPosition.TOP);
                        xAxis.setDrawGridLines(false);
                        xAxis.setGranularity(1f);
                        xAxis.setLabelCount(labelNames.size());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occur
                        Log.d("TAG", "onErrorResponse: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}