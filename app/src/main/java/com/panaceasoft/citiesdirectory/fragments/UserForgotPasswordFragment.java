package com.panaceasoft.citiesdirectory.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.panaceasoft.citiesdirectory.Config;
import com.panaceasoft.citiesdirectory.R;
import com.panaceasoft.citiesdirectory.activities.MainActivity;
import com.panaceasoft.citiesdirectory.activities.UserRegisterActivity;
import com.panaceasoft.citiesdirectory.utilities.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class UserForgotPasswordFragment extends Fragment {

    /**------------------------------------------------------------------------------------------------
     * Start Block - Private Variables
     **------------------------------------------------------------------------------------------------*/

    private View view;
    private EditText txtEmail;
    private Button btnRequest;
    private Button btnCancel;
    private ProgressDialog prgDialog;
    private String jsonStatusSuccessString;

    /**------------------------------------------------------------------------------------------------
     * End Block - Private Functions
     **------------------------------------------------------------------------------------------------*/


    /**------------------------------------------------------------------------------------------------
     * Start Block - Override Functions
     **------------------------------------------------------------------------------------------------*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_forgot_password, container, false);

        initData();

        initUI();

        return view;
    }

    /**------------------------------------------------------------------------------------------------
     * End Block - Private Functions
     **------------------------------------------------------------------------------------------------*/

    /**------------------------------------------------------------------------------------------------
     * Start Block - Init Data Functions
     **------------------------------------------------------------------------------------------------*/

    private void initData() {
        try {
            jsonStatusSuccessString = getResources().getString(R.string.json_status_success);

        }catch(Exception e){
            Utils.psErrorLogE("Error in init data.", e);
        }
    }

    /**------------------------------------------------------------------------------------------------
     * End Block - Init Data Functions
     **------------------------------------------------------------------------------------------------*/

    /**------------------------------------------------------------------------------------------------
     * Start Block - init UI Functions
     **------------------------------------------------------------------------------------------------*/

    private void initUI() {
        try {
            txtEmail = (EditText) this.view.findViewById(R.id.input_email);
            txtEmail.setTypeface(Utils.getTypeFace(Utils.Fonts.ROBOTO));
            btnRequest = (Button) this.view.findViewById(R.id.button_request);
            btnRequest.setTypeface(Utils.getTypeFace(Utils.Fonts.ROBOTO));
            btnRequest.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    doRequest();
                }
            });
            btnCancel = (Button) this.view.findViewById(R.id.button_cancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doCancel();
                }
            });
            prgDialog = new ProgressDialog(getActivity());
            prgDialog.setMessage("Please wait...");
            prgDialog.setCancelable(false);
        }catch(Exception e){
            Utils.psErrorLogE("Error in Init UI.", e);
        }
    }

    /**------------------------------------------------------------------------------------------------
     * End Block - init UI Functions
     **------------------------------------------------------------------------------------------------*/

    /**------------------------------------------------------------------------------------------------
     * Start Block - Private Functions
     **------------------------------------------------------------------------------------------------*/

    private void doCancel() {
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).openFragment(R.id.nav_profile);
        }else if(getActivity() instanceof UserRegisterActivity) {
            getActivity().finish();
        }
    }

    private void doRequest() {
        if(inputValidation()) {
            final String URL = Config.APP_API_URL + Config.GET_FORGOT_PASSWORD;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email", txtEmail.getText().toString());

            doSubmit(URL,params,view);
        }
    }

    private boolean inputValidation() {
        if(txtEmail.getText().toString().equals("")) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.email_validation_message,
                    Toast.LENGTH_LONG).show();
            return false;
        } else {
            if(!Utils.isEmailFormatValid(txtEmail.getText().toString())) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.email_format_validation_message,
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    public void doSubmit(String URL, final HashMap<String,String> params, final View view) {
        prgDialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String success_status = response.getString("status");
                            Utils.psLog(success_status);

                            prgDialog.cancel();
                            if (success_status.equals(jsonStatusSuccessString)) {
                                showSuccessPopup();
                            } else {
                                showFailPopup(response.getString("error"));
                            }

                        } catch (JSONException e) {
                            prgDialog.cancel();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prgDialog.cancel();
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        mRequestQueue.add(req);
    }

    public void showSuccessPopup() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.forgot_password)
                .content(R.string.forgot_success)
                .positiveText(R.string.OK)
                .show();
    }

    public void showFailPopup(String error) {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.forgot_password)
                .content(error)
                .positiveText(R.string.OK)
                .show();
    }

    /**------------------------------------------------------------------------------------------------
     * End Block - Private Functions
     **------------------------------------------------------------------------------------------------*/
}
