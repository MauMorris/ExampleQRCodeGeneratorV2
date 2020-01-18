package com.example.generadordeqrs.vm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.generadordeqrs.R;

import com.example.generadordeqrs.domain.UseCaseCreateQRFromJson;
import com.example.generadordeqrs.repository.RepositorySimulationData;

public class QrMainActivityViewModel extends ViewModel {
    private static final String LOG_TAG = QrMainActivityViewModel.class.getSimpleName();

    private String itemErrorCorrection;
    private String tdcInformation;
    private boolean isTdc;
    private String cuentaClabeInformation;
    private Boolean hasLogo = false;

    private MutableLiveData<Bitmap> currentQrImage;

    private UseCaseCreateQRFromJson qrGenerator;

    public QrMainActivityViewModel() {
        currentQrImage = new MutableLiveData<>();

        RepositorySimulationData repo = RepositorySimulationData.getInstance();
        qrGenerator = UseCaseCreateQRFromJson.getInstance(repo);
    }

    public MutableLiveData<Bitmap> getCurrentQrData() {
        if(currentQrImage == null){
            currentQrImage = new MutableLiveData<>();
            return currentQrImage;
        } else
            return currentQrImage;
    }

    public String[] listDataErrorCorrection() {
        return qrGenerator.getListOfErrorCorrection();
    }

    public String[] listDataClabeTdc() {
        return qrGenerator.getListOfClabeAndTdc();
    }

    public void setItemErrorCorrection(String itemErrorCorrection) {
        this.itemErrorCorrection = itemErrorCorrection;
    }

    public void setHasLogoFlag(boolean hasLogo) {
        this.hasLogo = hasLogo;
    }

    public void setValuesWithLocalData(String localData) {
        String[] dataClabeQr = qrGenerator.getListOfClabes();
        String[] dataTdcQr = qrGenerator.getListOfTdc();

        switch (localData) {
            case "1500003862":
                setValues("", false, dataClabeQr[0]);
                break;
            case "1205791510":
                setValues("", false, dataClabeQr[1]);
                break;
            case "1500000077":
                setValues("", false, dataClabeQr[2]);
                break;
            case "0102196018":
                setValues("", false, dataClabeQr[3]);
                break;
            case "4152313304428563":
                setValues(dataTdcQr[0], true, "");
                break;
            case "4555041870000184":
                setValues(dataTdcQr[1], true, "");
                break;
            case "4555041000002290":
                setValues(dataTdcQr[2], true, "");
                break;
            case "4555042602360516":
                setValues(dataTdcQr[3], true, "");
                break;
            case "4555042700367868":
                setValues(dataTdcQr[4], true, "");
                break;
            case "4152313304428837":
                setValues(dataTdcQr[5], true, "");
                break;
        }
    }

    private void setValues(String numTdc, Boolean isTDC, String numCuentaClabe) {
        tdcInformation = numTdc;
        isTdc = isTDC;
        cuentaClabeInformation = numCuentaClabe;

        Log.d(LOG_TAG + ": ", "valores guardados para formar el JSON:: " +
                " numero TDC: " + numTdc +
                " numero CLABE: " + numCuentaClabe +
                " bandera TDC: " + isTDC);
    }

    public void createQr(Context context, String cantidad, String concepto, String nombre, int width) {
        String jsonQrContent = qrGenerator.createJson(
                cantidad,
                concepto,
                nombre,
                isTdc,
                tdcInformation,
                cuentaClabeInformation);

        Log.d(LOG_TAG + ": ", "JSON Respuesta --> " + jsonQrContent);

        String charset = "UTF-8";

        Bitmap bitmap = qrGenerator.createQRBitmap(
                jsonQrContent,
                charset,
                width,
                itemErrorCorrection,
                context,
                hasLogo,
                Color.BLACK,
                Color.WHITE,
                R.drawable.ic_bmovil);

        hasLogo = false;

        currentQrImage.setValue(bitmap);
    }
}