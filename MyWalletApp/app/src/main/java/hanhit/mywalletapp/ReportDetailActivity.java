package hanhit.mywalletapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hanhit.mywalletapp.adapter.AdapterListViewDetailReport;
import hanhit.mywalletapp.database.MyDatabase;
import hanhit.mywalletapp.model.Item;

public class ReportDetailActivity extends AppCompatActivity {

    private TextView mTitleReport;
    private ListView mListViewReport;
    private List itemList;

    private MyDatabase myDB;
    private AdapterListViewDetailReport mAdapter;
    private MyHandle myHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        init();

        // get and set content for title, value was sent from WalletManagerActivity
        // when press image detail
        Bundle bundle = getIntent().getBundleExtra("data");
        mTitleReport.setText(bundle.getString("title"));

        itemList = myDB.getObjectByMonth(myHandle.convertMonthTypeStringToNum(bundle.getString("month")));
        mAdapter = new AdapterListViewDetailReport(this, itemList);
        mListViewReport.setAdapter(mAdapter);
    }

    public void init(){
        mTitleReport = (TextView) findViewById(R.id.toolbar_title);
        mListViewReport = (ListView) findViewById(R.id.list_view_detail_report);
        myDB = new MyDatabase(this);
        myHandle = new MyHandle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDB.close();
    }


}
