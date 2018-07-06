package com.squad.mayienter.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;
import com.squad.mayienter.interfaces.ScannerFragmentInteraction;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerFragment extends Fragment
        implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private ScannerFragmentInteraction scannerFragmentInteraction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scannerView = new ZXingScannerView(getActivity());
        return scannerView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ScannerFragmentInteraction) {
            scannerFragmentInteraction = (ScannerFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ScannerFragmentInteraction");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        if (scannerFragmentInteraction != null) {
            scannerFragmentInteraction.handleScannerResults(rawResult);
        }

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case, need some time to figure it out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scannerView.resumeCameraPreview(ScannerFragment.this);
            }
        }, 2000);
        // Can be removed if u don't need to reuse this scanner right after first usage.
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        scannerFragmentInteraction = null;
    }
}
