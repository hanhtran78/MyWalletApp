package hanhit.mywalletapp;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.samsistemas.calendarview.widget.CalendarView;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hanhit.mywalletapp.adapter.AdapterListViewWalletManager;
import hanhit.mywalletapp.database.MyDatabase;
import hanhit.mywalletapp.model.Item;

public class WalletManagerActivity extends AppCompatActivity {

    private String LOG_TAG = this.getClass().getSimpleName();

    private CalendarView mCalendar;
    private ImageView mImageAddItem;
    private ListView mListViewItem;
    private ImageView mImageReport;
    private TextView mTextHide;

    private MyDatabase myDB;
    private ArrayList<Item> itemList = new ArrayList<>();
    private AdapterListViewWalletManager adapter;
    private MyHandle myHandle;
    private static String currentMonthOfCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_manager);
        getWidgets();
        myDB = new MyDatabase(this);
        myHandle = new MyHandle();
        currentMonthOfCalendar = myHandle.getMonth(myHandle.formatDate(Calendar.getInstance().getTime()));

        itemList = myDB.getAllItemByDate(myHandle.formatDate(Calendar.getInstance().getTime()));
        adapter = new AdapterListViewWalletManager(WalletManagerActivity.this,
                R.layout.custom_listview_wallet_manager, itemList);
        mListViewItem.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        mImageAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startAddItemActivity = new Intent(WalletManagerActivity.this, ItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", "Add New");
                startAddItemActivity.putExtra("data", bundle);
                startActivity(startAddItemActivity);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });

        mImageReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get value balance, income, expense from data
                // Show Dialog Report Of Month
                BigInteger[] sumValues = myDB.getValueAllByMonth(currentMonthOfCalendar);
                BigInteger income = sumValues[0];
                BigInteger expense = sumValues[1];
                BigInteger balance = income.subtract(expense);
                String month = mCalendar.getCurrentMonth();
                if (income == expense && income.equals(BigInteger.valueOf(0))) {
                    Toast.makeText(WalletManagerActivity.this, "The " + month + " no have data to show!", Toast.LENGTH_SHORT).show();
                } else {
                    createDialogReport(month, balance, income, expense);
                }
            }
        });

        mCalendar.setOnDateSelectedListener(new CalendarView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull Date date) {
                loadDataByDate(myHandle.formatDate(date));
                currentMonthOfCalendar = myHandle.getMonth(myHandle.formatDate(date));
            }
        });

        mCalendar.setOnMonthChangedListener(new CalendarView.OnMonthChangedListener() {
            @Override
            public void onMonthChanged(@NonNull Date date) {
                currentMonthOfCalendar = myHandle.getMonth(myHandle.formatDate(date));
                itemList = myDB.getAllItemByMonth(currentMonthOfCalendar);
                updateListViewWalletManager();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_OK && resultCode == RESULT_OK) {
            loadDataByDate(myHandle.formatDate(Calendar.getInstance().getTime()));
        } else {
            Toast.makeText(WalletManagerActivity.this, "Cancel update!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentTime = myHandle.formatDate(Calendar.getInstance().getTime());
        String currentMonth = myHandle.getMonth(currentTime);

        if (currentMonth.equals(currentMonthOfCalendar)){
            loadDataByDate(currentTime);
        } else {
            itemList = myDB.getAllItemByMonth(currentMonthOfCalendar);
            updateListViewWalletManager();
        }
    }

    public void loadDataByDate(String date) {
        itemList = myDB.getAllItemByDate(date);
        updateListViewWalletManager();
    }

    public void updateListViewWalletManager() {
        if (itemList.size() > 0) {
            adapter.clear();
            adapter.addAll(itemList);
            adapter.notifyDataSetChanged();

            mTextHide.setVisibility(View.GONE);
            mListViewItem.setVisibility(View.VISIBLE);
        } else {
            mTextHide.setVisibility(View.VISIBLE);
            mListViewItem.setVisibility(View.GONE);
        }
    }

    public void createDialogReport(String month, BigInteger balance, BigInteger income, BigInteger expense) {
        final Dialog dialog = new Dialog(WalletManagerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_content_report);

        // get and set value for widgets of dialog
        final TextView title_dialog = (TextView) dialog.findViewById(R.id.toolbar_title_dialog);
        String title = "Summary for " + month;
        if (title.length() > 16) {
            title_dialog.setTextSize(20);
        }
        title_dialog.setText(title);

        TextView balance_value = (TextView) dialog.findViewById(R.id.txt_value_balance_dialog);
        balance_value.setText(myHandle.handleStringValue(balance + "") + ",000 VND");

        TextView income_value = (TextView) dialog.findViewById(R.id.txt_value_income_dialog);
        income_value.setText(myHandle.handleStringValue(income + "") + ",000 VND ");

        TextView expense_value = (TextView) dialog.findViewById(R.id.txt_value_expense_dialog);
        expense_value.setText(myHandle.handleStringValue(expense + "") + ",000 VND ");

        ImageView image_detail = (ImageView) dialog.findViewById(R.id.image_detail_dialog);
        image_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReportDetail = new Intent(WalletManagerActivity.this, ReportDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", title_dialog.getText().toString());
                bundle.putString("month", currentMonthOfCalendar);
                intentReportDetail.putExtra("data", bundle);
                startActivity(intentReportDetail);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void getWidgets() {
        mTextHide = (TextView) findViewById(R.id.txt_hide);
        mCalendar = (CalendarView) findViewById(R.id.calendar_view);
        mImageAddItem = (ImageView) findViewById(R.id.image_add);
        mListViewItem = (ListView) findViewById(R.id.list_view_item);
        mImageReport = (ImageView) findViewById(R.id.image_report);
        mListViewItem = (ListView) findViewById(R.id.list_view_item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDB.close();
    }
}
