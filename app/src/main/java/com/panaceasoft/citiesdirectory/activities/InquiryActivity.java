package com.panaceasoft.citiesdirectory.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.View;
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
import com.panaceasoft.citiesdirectory.GlobalData;
import com.panaceasoft.citiesdirectory.R;
import com.panaceasoft.citiesdirectory.utilities.Utils;
import android.widget.Button;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;


public class InquiryActivity extends AppCompatActivity {

    /**------------------------------------------------------------------------------------------------
     * Start Block - Private Variables
     **------------------------------------------------------------------------------------------------*/


    private Toolbar toolbar;
    private EditText txtName;
    private EditText txtEmail;
    private EditText txtMessage;
    private SharedPreferences pref;
    private Button btnSubmit;
    private ProgressDialog prgDialog;
    private String jsonStatusSuccessString;
    private SpannableString inquiry;

    /**------------------------------------------------------------------------------------------------
     * End Block - Private Variables
     **------------------------------------------------------------------------------------------------*/

    /**------------------------------------------------------------------------------------------------
     * Start Block - Override Functions
     **------------------------------------------------------------------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        initUI();

        initData();

        bindData();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.blank_anim, R.anim.left_to_right);
    }

    /**------------------------------------------------------------------------------------------------
     * End Block - Override Functions
     **------------------------------------------------------------------------------------------------*/

    /**------------------------------------------------------------------------------------------------
     * Start Block - Init Data Functions
     **------------------------------------------------------------------------------------------------*/

    private void initData() {
        try {
            pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            jsonStatusSuccessString = getResources().getString(R.string.json_status_success);
            inquiry = Utils.getSpannableString(getString(R.string.inquiry));
        } catch (Resources.NotFoundException e) {
            Utils.psErrorLog("Error in initData," ,e );
        }
    }

    /**------------------------------------------------------------------------------------------------
     * End Block - Init Data Functions
     **------------------------------------------------------------------------------------------------*/

    /**------------------------------------------------------------------------------------------------
     * Start Block - Init UI Functions
     **------------------------------------------------------------------------------------------------*/

    private void initUI() {

        try {
            initToolbar();
            initProgressDialog();
            txtName = (EditText) findViewById(R.id.input_name);
            txtName.setTypeface(Utils.getTypeFace(Utils.Fonts.ROBOTO));
            txtEmail = (EditText) findViewById(R.id.input_email);
            txtEmail.setTypeface(Utils.getTypeFace(Utils.Fonts.ROBOTO));
            txtMessage = (EditText) findViewById(R.id.input_message);
            txtMessage.setTypeface(Utils.getTypeFace(Utils.Fonts.ROBOTO));
            btnSubmit = (Button) findViewById(R.id.button_submit);
            btnSubmit.setTypeface(Utils.getTypeFace(Utils.Fonts.ROBOTO));
        } catch (Exception e) {
            Utils.psErrorLogE("Error in initUI.", e);
        }
    }

    private void initToolbar() {
        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Utils.psErrorLogE("Error in initToolbar.", e);
        }
    }

    private void initProgressDialog() {
        try {
            prgDialog = new ProgressDialog(this);
            prgDialog.setMessage("Please wait...");
            prgDialog.setCancelable(false);
        } catch (Exception e) {
            Utils.psErrorLogE("Error in initProgressDialog.", e);
        }
    }

    /**------------------------------------------------------------------------------------------------
     * End Block - Init UI Functions
     **------------------------------------------------------------------------------------------------*/

    /**------------------------------------------------------------------------------------------------
     * Start Block - Bind Data Functions
     **------------------------------------------------------------------------------------------------*/

    private void bindData() {
        toolbar.setTitle(inquiry);
    }

    /**------------------------------------------------------------------------------------------------
     * End Block - Bind Data Functions
     **------------------------------------------------------------------------------------------------*/


    /**------------------------------------------------------------------------------------------------
     * Start Block - Public Functions
     **------------------------------------------------------------------------------------------------*/

    public void doInquiry(View view) {
        if (inputValidation()) {

            final String URL = Config.APP_API_URL + Config.POST_ITEM_INQUIRY + GlobalData.itemData.id;
            Utils.psLog(URL);

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("name", txtName.getText().toString());
            params.put("email", txtEmail.getText().toString());
            params.put("message", txtMessage.getText().toString());
            params.put("city_id", String.valueOf(pref.getInt("_id", 0)));
            doSubmit(URL, params, view);
        }
    }

    public void doSubmit(String URL, final HashMap<String, String> params, final View view) {
        prgDialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                          //  pb.setVisibility(view.GONE);

                            String status = response.getString("status");
                            if (status.equals(jsonStatusSuccessString)) {
                                Utils.psLog(status);

                                showSuccessPopup();

                            } else {
                                showFailPopup();
                                Utils.psLog("Error in loading.");
                            }

                            prgDialog.cancel();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        mRequestQueue.add(req);
    }

    public void showSuccessPopup() {
        new MaterialDialog.Builder(this)
                .title(R.string.inquiry)
                .content(R.string.inquiry_success)
                .positiveText(R.string.OK)
                .callback(new MaterialDialog.ButtonCallback() {
                    public void onPositive(MaterialDialog dialog) {
                        finish();
                    }
                })
                .show();
    }

    public void showFailPopup() {
        new MaterialDialog.Builder(this)
                .title(R.string.inquiry)
                .content(R.string.inquiry_fail)
                .positiveText(R.string.OK)
                .show();
    }

    public boolean inputValidation() {

        if (txtName.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.name_validation_message,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (txtEmail.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.email_validation_message,
                    Toast.LENGTH_LONG).show();
            return false;
        } else {
            if (!Utils.isEmailFormatValid(txtEmail.getText().toString())) {
                Toast.makeText(getApplicationContext(), R.string.email_format_validation_message,
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if (txtMessage.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.inquiry_validation_message,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    /**------------------------------------------------------------------------------------------------
     * End Block - Public Functions
     **------------------------------------------------------------------------------------------------*/


}
