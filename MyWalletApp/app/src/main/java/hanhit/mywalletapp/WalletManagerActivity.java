package hanhit.mywalletapp;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.samsistemas.calendarview.widget.CalendarView;

import java.util.ArrayList;

import hanhit.mywalletapp.adapter.AdapterListviewWalletManager;
import hanhit.mywalletapp.model.Item;

public class WalletManagerActivity extends AppCompatActivity {

    private CalendarView mCalendar;
    private ImageView mImageAddItem;
    private ListView mListViewItem;
    private ImageView mImageReport, mImageEdit, mImageDelete;

    private ArrayList<Item> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_manager);
        getWidgets();

        for (int i = 0; i < 5; i++) {

            Item item = new Item(i, 0, "Item" + i, "01-02-1992", 1000, i);
            itemList.add(item);
        }

        mListViewItem.setAdapter(new AdapterListviewWalletManager(
                WalletManagerActivity.this,
                R.layout.custom_listview_wallet_manager,
                itemList));

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
                createDialogReport(month, 10, 14, 4);
            }
        });
    }

    public void createDialogReport(String month, int balance, int income, int expense) {
        final Dialog dialog = new Dialog(WalletManagerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_content_report);

        // get and set value for widgets of dialog
        final TextView title_dialog = (TextView) dialog.findViewById(R.id.toolbar_title_dialog);
        title_dialog.setText("Summary for " + month);
        TextView balance_value = (TextView) dialog.findViewById(R.id.txt_value_balance_dialog);
        balance_value.setText(balance + ",000 VND");
        TextView income_value = (TextView) dialog.findViewById(R.id.txt_value_income_dialog);
        income_value.setText(income+",000 VND ");
        TextView expense_value = (TextView) dialog.findViewById(R.id.txt_value_expense_dialog);
        expense_value.setText(expense+",000 VND");

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
        mCalendar = (CalendarView) findViewById(R.id.calendar_view);
        mImageAddItem = (ImageView) findViewById(R.id.image_add);
        mListViewItem = (ListView) findViewById(R.id.list_view_item);
        mImageReport = (ImageView) findViewById(R.id.image_report);
        mListViewItem = (ListView) findViewById(R.id.list_view_item);
        mImageDelete = (ImageView) findViewById(R.id.image_delete);
        mImageEdit = (ImageView) findViewById(R.id.image_edit);
    }

}
