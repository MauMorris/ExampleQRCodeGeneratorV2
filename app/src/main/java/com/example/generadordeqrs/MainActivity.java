package com.example.generadordeqrs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.generadordeqrs.databinding.ActivityMainBinding;
import com.example.generadordeqrs.vm.QrMainActivityViewModel;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding mMainBinding;
    private QrMainActivityViewModel mQrViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mQrViewModel = ViewModelProviders.of(this).get(QrMainActivityViewModel.class);

        setViews(mMainBinding);
        setListeners();
        subscribeUI(mQrViewModel);
    }

    private void subscribeUI(QrMainActivityViewModel mQrViewModel) {
        mQrViewModel.getCurrentQrData().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                mMainBinding.qrCodeImageView.setImageBitmap(bitmap);
            }
        });
    }

    private void setViews(ActivityMainBinding mMainBinding) {
        ArrayAdapter adapterIncertidumbre = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                mQrViewModel.listDataErrorCorrection());

        ArrayAdapter adapterCuentaClabe = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                mQrViewModel.listDataClabeTdc());

        mMainBinding.incertidumbreSpinner.setAdapter(adapterIncertidumbre);
        mMainBinding.cuentaClableSpinner.setAdapter(adapterCuentaClabe);

        mMainBinding.nombreEditText.getText().clear();
        mMainBinding.conceptoEditText.getText().clear();
        mMainBinding.cantidadEditText.getText().clear();
        mMainBinding.imageInsideQrSwitch.setChecked(false);
    }

    public void setListeners() {
        mMainBinding.incertidumbreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemErrorCorrection = adapterView.getItemAtPosition(i).toString();
                mQrViewModel.setItemErrorCorrection(itemErrorCorrection);

                Log.d(LOG_TAG + ": ", "seleccion de % de incertidumbre:: " + itemErrorCorrection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mMainBinding.cuentaClableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String AccountdataSelected = adapterView.getItemAtPosition(i).toString();
                mQrViewModel.setValuesWithLocalData(AccountdataSelected);

                Log.d(LOG_TAG + ": ", "seleccion de cuenta o TDC:: " + AccountdataSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mMainBinding.imageInsideQrSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mQrViewModel.setHasLogoFlag(b);

                Log.d(LOG_TAG + ": ", "bandera relacionada con el logo " + b);
            }
        });

        mMainBinding.generarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQrViewModel.createQr(
                        MainActivity.this,
                        mMainBinding.cantidadEditText.getText().toString(),
                        mMainBinding.conceptoEditText.getText().toString(),
                        mMainBinding.nombreEditText.getText().toString(),
                        mMainBinding.qrCodeImageView.getLayoutParams().width);

                mMainBinding.nombreEditText.getText().clear();
                mMainBinding.conceptoEditText.getText().clear();
                mMainBinding.cantidadEditText.getText().clear();
                mMainBinding.imageInsideQrSwitch.setChecked(false);

                Toast.makeText(MainActivity.this, "Generando Código Qr", Toast.LENGTH_SHORT).show();

                Log.d(LOG_TAG + ": ", "se ha generado el QR correctamente");
            }
        });
    }
}