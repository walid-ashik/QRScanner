package ashik.example.com.qrscanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //SETTING LAYOUT FOR SCANNER
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if(checkPermission()){

                Toast.makeText(MainActivity.this, "Permission is Granted", Toast.LENGTH_LONG).show();

            }else{
                requestPermission();
            }

        }

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);

    }

    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(MainActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public void onRequestPermissionResult(int requestCode, String permission[], int grantResults[]){

        switch (requestCode){

            case REQUEST_CAMERA :
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted){
                        Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_LONG).show();

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                            if(shouldShowRequestPermissionRationale(CAMERA)){
                                displayAlertMessage("You need to allow access for both permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                        }

                                    }
                                });
                                return;
                            }

                        }

                    }
                }
                break;

        }

    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .show();
    }


    //SCANNER RESULT HERE
    @Override
    public void handleResult(Result result) {

        //GETTING RESULT in STRING
        String scanResult = result.getText();
        if(!scanResult.equals("")){

            scannerView.resumeCameraPreview(MainActivity.this);

            //GOING NEXT ACTIVITY TO SHOW
            Intent scanResultViewActivity = new Intent(MainActivity.this, ScannerResult.class);
            scanResultViewActivity.putExtra("scanResult", scanResult);
            startActivity(scanResultViewActivity);
        }

    }

    @Override
    public void onResume(){
        super.onResume();

        if(checkPermission()){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                if(scannerView == null){
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();

            }
        }else{
            requestPermission();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        scannerView.stopCamera();

    }
}
