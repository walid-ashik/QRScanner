package ashik.example.com.qrscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ScannerResult extends AppCompatActivity {

    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_result);

        mText = findViewById(R.id.textView);
        String scanResultFromScanner = getIntent().getStringExtra("scanResult");
        if(!scanResultFromScanner.equals("")){
            mText.setText(scanResultFromScanner);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mText.setText("");
    }
}
