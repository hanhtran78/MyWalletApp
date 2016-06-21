package hanhit.mywalletapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.samsistemas.calendarview.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hanhit.mywalletapp.adapter.AdapterListViewWalletManager;
import hanhit.mywalletapp.database.MyDatabase;
import hanhit.mywalletapp.model.Item;

public class WalletManagerActivity extends AppCompatActivity {

    private CalendarView mCalendar;
    private ImageView mImageAddItem;
    private ListView mListViewItem;
    private ImageView mImageReport;
    private TextView mTextHide;

    private MyDatabase myDB;
    private ArrayList<Item> itemList = new ArrayList<>();
    private AdapterListViewWalletManager adapter;

    private Handler mTimeHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_manager);
        getWidgets();
        myDB = new MyDatabase(this);

        itemList = myDB.getAllItemByDate(formatDate(Calendar.getInstance().getTime()));
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
            }
        });

        mImageReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get value balance, income, expense from data
                // Show Dialog Report Of Month
                String month = mCalendar.getCurrentMonth();
                int[] sumValues = myDB.getValueAllByMonth();
                int income = sumValues[0];
                int expense = sumValues[1];
                createDialogReport(month, income - expense, income, expense);

                Toast.makeText(WalletManagerActivity.this, mCalendar.getCurrentMonth() + "", Toast.LENGTH_SHORT).show();
            }
        });

        mCalendar.setOnDateSelectedListener(new CalendarView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull Date date) {
                loadDataByDate(formatDate(date));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_OK && resultCode == RESULT_OK) {
            loadDataByDate(formatDate(Calendar.getInstance().getTime()));
        } else {
            Toast.makeText(WalletManagerActivity.this, "Cancel update!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataByDate(formatDate(Calendar.getInstance().getTime()));
        Toast.makeText(WalletManagerActivity.this, "Count number item: " + myDB.numberItem(), Toast.LENGTH_LONG).show();
    }

    public void loadDataByDate(String date) {
        itemList = myDB.getAllItemByDate(date);

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

    public void createDialogReport(String month, int balance, int income, int expense) {
        final Dialog dialog = new Dialog(WalletManagerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_content_report);

        // get and set value for widgets of dialog
        final TextView title_dialog = (TextView) dialog.findViewById(R.id.toolbar_title_dialog);
        title_dialog.setText("Summary for " + month);

        TextView balance_value = (TextView) dialog.findViewById(R.id.txt_value_balance_dialog);
        balance_value.setText(handleString(balance + "") + ",000 VND");

        TextView income_value = (TextView) dialog.findViewById(R.id.txt_value_income_dialog);
        income_value.setText(handleString(income + "") + ",000 VND ");

        TextView expense_value = (TextView) dialog.findViewById(R.id.txt_value_expense_dialog);
        expense_value.setText(handleString(expense + "") + ",000 VND ");

        ImageView image_detail = (ImageView) dialog.findViewById(R.id.image_detail_dialog);
        image_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReportDetail = new Intent(WalletManagerActivity.this, ReportDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", title_dialog.getText().toString());
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

    public String formatDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    public String handleString(String str) {
        String newString;
        switch (str.length()) {
            case 4:
                newString = new StringBuilder(str).insert(1, ",").toString();
                break;
            case 5:
                newString = new StringBuilder(str).insert(2, ",").toString();
                break;
            case 6:
                newString = new StringBuilder(str).insert(3, ",").toString();
                break;
            case 7:
                newString = new StringBuilder(str).insert(1, ",").insert(5, ",").toString();
                break;
            case 8:
                newString = new StringBuilder(str).insert(2, ",").insert(6, ",").toString();
                break;
            case 9:
                newString = new StringBuilder(str).insert(3, ",").insert(7, ",").toString();
                break;
            default:
                newString = str;
                break;
        }
        return newString;
    }

    public String convertMonthTypeStringToNum(String month){
        switch (month){
            case "JANUARY":
                return "01";
            case "FEBRUARY":
                return "02";
            case "MARCH":
                return "03";
            case "APRIL":
                return "04";
            case "MAY":
                return "05";
            case "JUNE":
                return "06";
            case "JULY0":
                return "07";
            case "AUGUST":
                return "08";
            case "SEPTEMBER":
                return "09";
            case "OCTOBER":
                return "10";
            case "NOVEMBER":
                return "11";
            default:
                return "12";
        }
    }


}
