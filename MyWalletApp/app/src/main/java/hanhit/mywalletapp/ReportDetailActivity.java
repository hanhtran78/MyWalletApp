package hanhit.mywalletapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class ReportDetailActivity extends AppCompatActivity {

    private TextView mTitleReport;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        mTitleReport = (TextView) findViewById(R.id.toolbar_title);

        // get and set content for title, value was sent from WalletManagerActivity
        // when press image detail
        Bundle bundle = getIntent().getBundleExtra("data");
        mTitleReport.setText(bundle.getString("title"));

        toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        //toolbar.
    }
}
