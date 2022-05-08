package com.iir4.g2.gmachine.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.iir4.g2.gmachine.R;
import com.iir4.g2.gmachine.databinding.FragmentDashboardBinding;
import com.iir4.g2.gmachine.databinding.FragmentGalleryBinding;
import com.iir4.g2.gmachine.models.MarqueMachineData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    BarChart bar_marque_by_machine_statistic_fragment;
    private static final String urlGetMarqueByMachine = "http://10.0.2.2:8090/marques/count";

    ArrayList<BarEntry> barEntriesMarqueByMachine;
    ArrayList<MarqueMachineData> marqueMachineData;
    ArrayList<String> labelNames;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel galleryViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        marqueMachineData = new ArrayList<>();
        labelNames = new ArrayList<>();
        bar_marque_by_machine_statistic_fragment = root.findViewById(R.id.bar_marque_by_machine_statistic_fragment);
        barEntriesMarqueByMachine = new ArrayList<>();
        getMarqueBymachine();
        return root;
    }
    private void drawChart() {
        BarDataSet dataset = new BarDataSet(barEntriesMarqueByMachine, "Nombre de Machines par Marque");
        dataset.setColors(ColorTemplate.PASTEL_COLORS);
        dataset.setValueTextSize(20);
        dataset.setDrawValues(false);
        BarData barData = new BarData(dataset);

        XAxis xAxis = bar_marque_by_machine_statistic_fragment.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labelNames.size());

        bar_marque_by_machine_statistic_fragment.setFitBars(true);
        bar_marque_by_machine_statistic_fragment.setData(barData);
        bar_marque_by_machine_statistic_fragment.getDescription().setText("Number of Machine By Marque");
        bar_marque_by_machine_statistic_fragment.animateY(2000);
    }


    private void getMarqueBymachine() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlGetMarqueByMachine, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                        JSONObject Jarray = null;
                        try {
                            Jarray = new JSONObject(response);
                            JSONArray keys = Jarray.names();
                            for (int i = 0; i < Jarray.length(); i++) {
                                String key = keys.getString(i); // Here's your key
                                int value = Integer.parseInt(Jarray.getString(key)); // Here's your value
                                marqueMachineData.add(new MarqueMachineData(key, value));

                            }
                            for (int i = 0; i < marqueMachineData.size(); i++) {
                                labelNames.add(marqueMachineData.get(i).getMarque());
                                barEntriesMarqueByMachine.add(new BarEntry(i, marqueMachineData.get(i).getNbrMachine()));
                            }
                            drawChart();
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