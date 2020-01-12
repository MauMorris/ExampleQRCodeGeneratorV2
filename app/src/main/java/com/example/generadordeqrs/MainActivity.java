package com.example.generadordeqrs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.example.generadordeqrs.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String charset = "UTF-8";

    private ActivityMainBinding mMainBinding;

    public ArrayAdapter adapterIncertidumbre, adapterCuentaClabe;

    public String strItemIncertidumbre;
    public String strCuentaClabe;
    public String strTdc;
    public String json;

    public Boolean isTdc = false;
    public Boolean isLogo = false;

    public CreateQRFromJson qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        adapterIncertidumbre = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item,
                SimulationData.incertidumbre);
        mMainBinding.incertidumbreSpinner.setAdapter(adapterIncertidumbre);

        adapterCuentaClabe = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item,
                SimulationData.clabeTdc);
        mMainBinding.cuentaClableSpinner.setAdapter(adapterCuentaClabe);

        setListeners();
    }

    public void setListeners() {
        mMainBinding.incertidumbreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strItemIncertidumbre = adapterView.getItemAtPosition(i).toString();
                Log.d(LOG_TAG + ": ", "seleccion de % de incertidumbre:: " + strItemIncertidumbre);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mMainBinding.cuentaClableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String localData = adapterView.getItemAtPosition(i).toString();
                Log.d(LOG_TAG + ": ", "seleccion de cuenta o TDC:: " + localData);
                setValuesWithLocalData(localData);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mMainBinding.imageInsideQrSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isLogo = b;
                Log.d(LOG_TAG + ": ", "bandera relacionada con el logo " + isLogo);
            }
        });

        mMainBinding.generarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qr = new CreateQRFromJson();

                json = qr.crearJson(mMainBinding.cantidadEditText.getText().toString(),
                        mMainBinding.conceptoEditText.getText().toString(),
                        mMainBinding.nombreEditText.getText().toString(),
                        isTdc,
                        strTdc,
                        strCuentaClabe);
                Log.d(LOG_TAG + ": ", "JSON Respuesta --> " + json);

                mMainBinding.qrCodeImageView.setImageBitmap(
                        qr.createQR(
                                json,
                                charset,
                                mMainBinding.qrCodeImageView.getLayoutParams().width,
                                strItemIncertidumbre,
                                MainActivity.this,
                                isLogo));

                Log.d(LOG_TAG + ": ", "se ha generado el QR correctamente");
            }
        });
    }


    private void setValuesWithLocalData(String localData) {
        switch (localData) {
            case "1500003862":
                setValues("", false, SimulationData.dataClabeQr[0]);
                break;
            case "1205791510":
                setValues("", false, SimulationData.dataClabeQr[1]);
                break;
            case "1500000077":
                setValues("", false, SimulationData.dataClabeQr[2]);
                break;
            case "0102196018":
                setValues("", false, SimulationData.dataClabeQr[3]);
                break;
            case "4152313304428563":
                setValues(SimulationData.dataTdcQr[0], true, "");
                break;
            case "4555041870000184":
                setValues(SimulationData.dataTdcQr[1], true, "");
                break;
            case "4555041000002290":
                setValues(SimulationData.dataTdcQr[2], true, "");
                break;
            case "4555042602360516":
                setValues(SimulationData.dataTdcQr[3], true, "");
                break;
            case "4555042700367868":
                setValues(SimulationData.dataTdcQr[4], true, "");
                break;
            case "4152313304428837":
                setValues(SimulationData.dataTdcQr[5], true, "");
                break;
        }
    }

    public void setValues(String numTdc, Boolean isTDC, String numCuentaClabe) {
        strTdc = numTdc;
        isTdc = isTDC;
        strCuentaClabe = numCuentaClabe;

        Log.d(LOG_TAG + ": ", "valores guardados para formar el JSON:: " +
                " numero TDC: " + numTdc +
                " numero CLABE: " + numCuentaClabe +
                " bandera TDC: " + isTDC);
    }
}
