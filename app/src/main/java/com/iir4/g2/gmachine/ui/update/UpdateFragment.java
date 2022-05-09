package com.iir4.g2.gmachine.ui.update;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iir4.g2.gmachine.R;
import com.iir4.g2.gmachine.databinding.UpdateFragmentBinding;
import com.iir4.g2.gmachine.models.Machine;
import com.iir4.g2.gmachine.models.Marque;
import com.iir4.g2.gmachine.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdateFragment extends Fragment {

    private UpdateViewModel mViewModel;
    private UpdateFragmentBinding binding;
    private String idMarqueText;
    private EditText marque,prix,dateAchat;
    DatePickerDialog picker;
    EditText eText;
    TextView id ;
    private EditText reference ;
    private Spinner spinner ;
    List<Machine> machines;
    private Marque MarqueByID;
    String ids,referenceText, prixText, dateAchatText;
    private static final String urlAddMachine = "http://10.0.2.2:8090/machines/save";
    private static final String urlGetMarque = "http://10.0.2.2:8090/marques/all";
    private Button modifier ;
    ArrayList<String> marqueList = new ArrayList<>();
    ArrayAdapter<String> marqueAdapter;
    List<String> sIds = new ArrayList<String>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        UpdateViewModel homeViewModel = new ViewModelProvider(this).get(UpdateViewModel.class);

        binding = UpdateFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        id=(TextView) root.findViewById(R.id.idMachine);
        prix = (EditText) root.findViewById(R.id.prix);
        dateAchat = (EditText) root.findViewById(R.id.date);
        reference = (EditText) root.findViewById(R.id.reference);
        modifier = (Button) root.findViewById(R.id.Modifier);
        spinner = (Spinner) root.findViewById(R.id.marque);

        eText=root.findViewById(R.id.date);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                Activity currentActivity = (Activity) getContext();
                picker = new DatePickerDialog(currentActivity,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if(monthOfYear<10  && dayOfMonth<10)
                            eText.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
                        else if(monthOfYear<10)
                            eText.setText(year + "-0" + (monthOfYear + 1)+ "-" + dayOfMonth );
                        else if(dayOfMonth<10)
                            eText.setText(year  +"-" + (monthOfYear + 1)+ "-0" + dayOfMonth );
                        else
                            eText.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth );
                    }
                }, year, month, day);

                picker.show();

            }
        });

        getAllMarqueInSpinner();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String idd = sIds.get(pos);
                idMarqueText = idd;
                findMarqueByID(idd);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                Log.i("Message", "Nothing is selected");

            }
        });

        Bundle b = this.getArguments();
        if(b != null){
            id.setText(""+b.getString("id"));
            prix.setText(""+b.getString("prix"));
            dateAchat.setText(""+b.getString("dateAchat"));
            reference.setText(""+b.getString("reference"));
            for (int i = 0; i < spinner.getCount(); i++) {
                String item = spinner.getItemAtPosition(i).toString();
                Log.d("BAAAAA",item);

                if (item.equals(b.getString("marqueId"))){
                    spinner.setSelection(i);
                    break;
                }
            }
        }

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ids = id.getText().toString();
                Log.d("okid",ids);
                referenceText = reference.getText().toString();
                prixText = prix.getText().toString();
                dateAchatText = dateAchat.getText().toString();
                insert(ids,referenceText, prixText, dateAchatText, MarqueByID);

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void insert(String id,String referenceText, String prixText, String dateAchatText, Marque marque) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject postData = new JSONObject();
        JSONObject postDataMarque = new JSONObject();
        try {
            postData.put("id",id);
            postDataMarque.put("id", marque.getId());
            postDataMarque.put("code", marque.getCode());
            postDataMarque.put("libelle", marque.getLibelle());
            postData.put("dateAchat", dateAchatText);
            postData.put("prix", prixText);
            postData.put("reference", referenceText);
            postData.put("marque", postDataMarque);
            Log.d("ok",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlAddMachine, postData,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getContext(), "Bien Modifié", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.toString().contains("End of input at character 0")) {
                    FragmentTransaction nextFrag= getFragmentManager().beginTransaction();
                    nextFrag.replace(R.id.nav_host_fragment_content_nav, new HomeFragment());
                    nextFrag.setReorderingAllowed(true);
                    nextFrag.addToBackStack(null);
                    nextFrag.commit();

                    Toast.makeText(getContext(), "Bien Modifié",Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), error.toString(),
                            Toast.LENGTH_SHORT).show();
                }
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void getAllMarqueInSpinner() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlGetMarque,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String id = jsonobject.getString("id");
                                String libelle = jsonobject.getString("libelle");
                                String code = jsonobject.getString("code");
                                sIds.add(id);
                                marqueList.add(libelle);
                                marqueAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, marqueList);
                                marqueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(marqueAdapter);
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
    private Marque findMarqueByID(String idd) {
        final String urlFindMarque = "http://10.0.2.2:8090/marques/" + idd;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlFindMarque,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject Jarray = new JSONObject(response);
                            String code = Jarray.getString("code");
                            String libelle = Jarray.getString("libelle");
                            MarqueByID = new Marque(idd, code, libelle);

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
        return MarqueByID;
    }

}