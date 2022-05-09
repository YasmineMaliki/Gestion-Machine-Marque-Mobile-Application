package com.iir4.g2.gmachine.ui.updateMarque;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.iir4.g2.gmachine.R;
import com.iir4.g2.gmachine.databinding.UpdateFragmentBinding;
import com.iir4.g2.gmachine.databinding.UpdateMarqueFragmentBinding;
import com.iir4.g2.gmachine.ui.gallery.GalleryFragment;
import com.iir4.g2.gmachine.ui.home.HomeFragment;
import com.iir4.g2.gmachine.ui.update.UpdateViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateMarqueFragment extends Fragment {
    private static final String urlAddMarque = "http://10.0.2.2:8090/marques/save";
    private UpdateMarqueViewModel mViewModel;
    private Button modifier ;
    String ids,codeText, libelleText;
    private UpdateMarqueFragmentBinding binding;
    EditText code, libelle;
    TextView id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        UpdateViewModel homeViewModel = new ViewModelProvider(this).get(UpdateViewModel.class);

        binding = UpdateMarqueFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        id = (TextView) root.findViewById(R.id.idMarque);
        code = (EditText)root.findViewById(R.id.code);
        libelle = (EditText)root.findViewById(R.id.libelle);
        modifier =(Button)  root.findViewById(R.id.Modifier);

        Bundle b = this.getArguments();
        if(b != null){
            id.setText(""+b.getString("id"));
            code.setText(""+b.getString("code"));
            libelle.setText(""+b.getString("libelle"));
        }
        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ids = id.getText().toString();
                codeText = code.getText().toString();
                libelleText = libelle.getText().toString();
                insertMarque(ids,codeText,libelleText);

            }
        });
        return root;
    }

    private void insertMarque(String id,String codeText, String libelleText) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JSONObject postData = new JSONObject();
        try {
            postData.put("id", id);
            postData.put("code", codeText);
            postData.put("libelle", libelleText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlAddMarque, postData,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getContext(), "Bien Modifié", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.toString().contains("End")) {
                    FragmentTransaction nextFrag= getFragmentManager().beginTransaction();
                    nextFrag.replace(R.id.nav_host_fragment_content_nav, new GalleryFragment());
                    nextFrag.setReorderingAllowed(true);
                    nextFrag.addToBackStack(null);
                    nextFrag.commit();
                    Toast.makeText(getContext(), "Bien Modifié", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), error.toString(),
                            Toast.LENGTH_SHORT).show();
                }
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}