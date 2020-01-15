package com.example.generadordeqrs.vm;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.generadordeqrs.domain.CreateQRFromJson;
import com.example.generadordeqrs.datasource.SimulationData;

public class QrMainActivityViewModel extends ViewModel {
    private static final String LOG_TAG = QrMainActivityViewModel.class.getSimpleName();

    private String itemErrorCorrection;
    private String tdcInformation;
    private boolean isTdc;
    private String cuentaClabeInformation;
    private Boolean hasLogo = false;

    private MutableLiveData<Bitmap> currentQrImage;

    public QrMainActivityViewModel() {
        currentQrImage = new MutableLiveData<>();
    }

    public MutableLiveData<Bitmap> getCurrentQrData() {
        if(currentQrImage == null){
            currentQrImage = new MutableLiveData<>();
            return currentQrImage;
        } else
            return currentQrImage;
    }

    public String[] listDataErrorCorrection() {
        return SimulationData.incertidumbre;
    }

    public String[] listDataClabeTdc() {
        return SimulationData.clabeTdc;
    }

    public void setItemErrorCorrection(String itemErrorCorrection) {
        this.itemErrorCorrection = itemErrorCorrection;
    }

    public void setHasLogoFlag(boolean hasLogo) {
        this.hasLogo = hasLogo;
    }

    public void setValuesWithLocalData(String localData) {
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
        CreateQRFromJson qrGenerator = new CreateQRFromJson();

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
                hasLogo);

        hasLogo = false;

        currentQrImage.setValue(bitmap);
    }
}