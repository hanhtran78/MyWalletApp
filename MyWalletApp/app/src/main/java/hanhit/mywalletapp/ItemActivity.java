package hanhit.mywalletapp;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hanhit.mywalletapp.model.Item;

public class ItemActivity extends AppCompatActivity {

    private TextView mTextIncome, mTextExpense, mTextValue, mTextZero,
            mTextVND, mTextDate, mTextTpyeCategoryOfItem;
    private EditText mEditNoteItem;
    private ImageView mImageDate, mImageCategory, mImageChoiceCategory;
    private Button btnOK;

    private TextView mTitleToolbar;
    private boolean incomeClicked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getWidgets();

        // Set title for toolbar
        Bundle bundle = getIntent().getBundleExtra("data");
        String title = bundle.getString("title").toString();
        mTitleToolbar.setText(title);

        if (title.equals("Edit")) {
            Item item = (Item) bundle.getSerializable("object");
            if (item.getTypeItem() == 0) {
                incomeClicked = true;
                setColorWhenIncomeOrExpensePress();
            }
            mTextDate.setText(item.getDateItem());
            mTextValue.setText(item.getValueItem() + "");
            mEditNoteItem.setText(item.getNameItem());
            Toast.makeText(ItemActivity.this, "Edit -- Update value for views", Toast.LENGTH_SHORT).show();
        } else {
            Calendar calendar = Calendar.getInstance();
            // Set current date for item
            mTextDate.setText(formatDate(calendar.getTime()));
        }

        // Handle when press INCOME or EXPENSE
        setColorWhenIncomeOrExpensePress();
        mTextIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeClicked = true;
                setColorWhenIncomeOrExpensePress();
            }
        });
        mTextExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeClicked = false;
                setColorWhenIncomeOrExpensePress();
            }
        });
        // End handle

        // Chose date from DatePickerDialog
        mImageDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog date_picker = new DatePickerDialog(ItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dayStr = dayOfMonth + "";
                        if (dayOfMonth < 10)
                            dayStr = "0" + dayOfMonth;

                        int month = monthOfYear + 1;
                        String monthStr = month + "";
                        if (month < 10)
                            monthStr = "0" + monthOfYear;

                        mTextDate.setText(dayStr + "-" + monthStr + "-" + year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                date_picker.show();
            }
        });
    }

    // Get views
    public void getWidgets() {
        mTitleToolbar = (TextView) findViewById(R.id.toolbar_title);
        mTextIncome = (TextView) findViewById(R.id.txt_income_acti_item);
        mTextExpense = (TextView) findViewById(R.id.txt_expense_acti_item);
        mTextValue = (TextView) findViewById(R.id.txt_value_acti_item);
        mTextZero = (TextView) findViewById(R.id.txt_zero_acti_item);
        mTextVND = (TextView) findViewById(R.id.txt_vnd_acti_item);
        mTextDate = (TextView) findViewById(R.id.txt_date_acti_item);
        mTextTpyeCategoryOfItem = (TextView) findViewById(R.id.txt_type_category_acti_item);
        mEditNoteItem = (EditText) findViewById(R.id.edit_note_acti_item);
        mImageDate = (ImageView) findViewById(R.id.image_date_acti_item);
        mImageCategory = (ImageView) findViewById(R.id.image_category_acti_item);
        mImageChoiceCategory = (ImageView) findViewById(R.id.image_choice_category_acti_item);
        btnOK = (Button) findViewById(R.id.btnOK_acti_item);
    }

    // Method format date
    public String formatDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    public void setColorWhenIncomeOrExpensePress() {
        if (incomeClicked) {
            mTextIncome.setBackgroundColor(getResources().getColor(R.color.color_income));
            mTextIncome.setTextColor(Color.WHITE);
            mTextExpense.setTextColor(Color.BLACK);
            mTextExpense.setBackgroundColor(getResources().getColor(R.color.color_normal));

            mTextValue.setTextColor(getResources().getColor(R.color.color_income));
            mTextZero.setTextColor(getResources().getColor(R.color.color_income));
            mTextVND.setTextColor(getResources().getColor(R.color.color_income));
        } else {
            mTextExpense.setBackgroundColor(getResources().getColor(R.color.color_expense));
            mTextExpense.setTextColor(Color.WHITE);
            mTextIncome.setTextColor(Color.BLACK);
            mTextIncome.setBackgroundColor(getResources().getColor(R.color.color_normal));
            mTextExpense.setBackgroundColor(getResources().getColor(R.color.color_expense));
            mTextValue.setTextColor(getResources().getColor(R.color.color_expense));
            mTextZero.setTextColor(getResources().getColor(R.color.color_expense));
            mTextVND.setTextColor(getResources().getColor(R.color.color_expense));
        }
    }

}
