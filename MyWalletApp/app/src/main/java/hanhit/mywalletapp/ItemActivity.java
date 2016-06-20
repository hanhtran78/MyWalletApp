package hanhit.mywalletapp;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hanhit.mywalletapp.adapter.AdapterListViewWalletManager;
import hanhit.mywalletapp.database.MyDatabase;
import hanhit.mywalletapp.model.Category;
import hanhit.mywalletapp.model.Item;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitleToolbar;
    private TextView mTextIncome, mTextExpense, mTextValue, mTextZero,
            mTextVND, mTextDate;
    private EditText mEditNoteItem;
    private ImageView mImageDate, mImageCategory;
    private Spinner mSpinnerCategory;

    /* Keyboard*/
    private TextView mKey0, mKey1, mKey2, mKey3, mKey4, mKey5, mKey6,
            mKey7, mKey8, mKey9, mKeyOK, mKeyComma;
    private ImageView mKeyBackspace;

    private MyDatabase myDb;
    private boolean incomeClicked = true;
    private String valueInput = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getWidgets();
        myDb = new MyDatabase(this);

        // create mock data for category
//        if (myDb.insertCategory(new Category(0, "movie", "ic_movie")) &&
//                myDb.insertCategory(new Category(1, "shop", "ic_shop")) &&
//                myDb.insertCategory(new Category(2, "another", "ic_another"))) {
//            Toast.makeText(this, "Create mock catagory OK!", Toast.LENGTH_SHORT).show();
//        } else Toast.makeText(this, "Create mock category FAIL!", Toast.LENGTH_SHORT).show();


        initSpinner();
        click_button_keyboard();

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
            mSpinnerCategory.setSelection(item.getIdCategoryItem());
            showImageByName("ic_" + mSpinnerCategory.getSelectedItem().toString());

            Toast.makeText(ItemActivity.this, "Edit -- Update value for views", Toast.LENGTH_SHORT).show();
        } else {
            Calendar calendar = Calendar.getInstance();
            // Set current date for item
            mTextDate.setText(formatDate(calendar.getTime()));
            showImageByName("ic_" + mSpinnerCategory.getSelectedItem().toString());
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

        // handle spinner when item change
        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showImageByName("ic_" + mSpinnerCategory.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        // handle number button click

        mTextValue.setText(null);
        if (v.getTag() != null && "number_button".equals(v.getTag())) {
            valueInput += ((TextView) v).getText().toString();
            if (valueInput.length() >= 7) {
                mTextValue.setTextSize(25);
                mTextValue.setPadding(0, 25, 0, 0);
            } else if (valueInput.length() >= 12) {
                mTextValue.setTextSize(20);
                mTextValue.setPadding(0, 30, 0, 0);
            }
            mTextValue.setText(valueInput);
        }
        switch (v.getId()) {
            case R.id.key_backspace:
                valueInput = mTextValue.getText().toString();
                if (valueInput.length() > 0) {
                    String newValue = new StringBuilder(valueInput)
                            .deleteCharAt(valueInput.length() - 1).toString();
                    mTextValue.setText(newValue);
                }
                break;
            case R.id.key_ok:
                if (mTitleToolbar.getText().equals("Edit")) {
                    Bundle bundle = getIntent().getBundleExtra("data");
                    Item item = (Item) bundle.getSerializable("object");
                    updateItem(item);
                } else {
                    addNewItem();
                }
                break;
        }
    }

    public void addNewItem() {
        int id = myDb.numberItem();
        int type = 0;
        if (!incomeClicked)
            type = 1;
        String note = mEditNoteItem.getText().toString();
        int value = Integer.parseInt(valueInput);
        String date = mTextDate.getText().toString();
        int categoryItem = mSpinnerCategory.getSelectedItemPosition();
        if (myDb.insertItem(new Item(++id, type, note, date, value, categoryItem))) {
            Toast.makeText(ItemActivity.this, "Add new item success", Toast.LENGTH_SHORT).show();
            reInput();
        } else {
            Toast.makeText(ItemActivity.this, "Update  fail", Toast.LENGTH_SHORT).show();
        }
    }

    public void reInput() {
        mTextValue.setText("0");
        mTextValue.setTextSize(50);
        mTextValue.setPadding(0, 0, 0, 0);
        mEditNoteItem.setText(null);
        mTextDate.setText(formatDate(Calendar.getInstance().getTime()));
        mSpinnerCategory.setSelection(0);
    }

    public void updateItem(Item item) {
        int type = 0;
        if (!incomeClicked)
            type = 1;
        String note = mEditNoteItem.getText().toString();
        int value = Integer.parseInt(mTextValue.getText().toString());
        String date = mTextDate.getText().toString();
        int categoryItem = 0;
        if (mSpinnerCategory.getSelectedItem().equals("shop")) {
            categoryItem = 1;
        } else if (mSpinnerCategory.getSelectedItem().equals("another")) {
            categoryItem = 2;
        }
        item.setTypeItem(type);
        item.setNameItem(note);
        item.setValueItem(value);
        item.setDateItem(date);
        item.setIdCategoryItem(categoryItem);
        if (myDb.updateItem(item)) {
            Toast.makeText(ItemActivity.this, "Update success", Toast.LENGTH_SHORT).show();

            mTextValue.setText("0");
            mTextValue.setTextSize(50);
            mTextValue.setPadding(0, 0, 0, 0);
            mTextDate.setText(formatDate(Calendar.getInstance().getTime()));
            mSpinnerCategory.setSelection(0);
        } else {
            Toast.makeText(ItemActivity.this, "Add new item  fail", Toast.LENGTH_SHORT).show();
        }
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

        mEditNoteItem = (EditText) findViewById(R.id.edit_note_acti_item);
        mImageDate = (ImageView) findViewById(R.id.image_date_acti_item);
        mImageCategory = (ImageView) findViewById(R.id.image_category_acti_item);

        mKey0 = (TextView) findViewById(R.id.key_0);
        mKey1 = (TextView) findViewById(R.id.key_1);
        mKey2 = (TextView) findViewById(R.id.key_2);
        mKey3 = (TextView) findViewById(R.id.key_3);
        mKey4 = (TextView) findViewById(R.id.key_4);
        mKey5 = (TextView) findViewById(R.id.key_5);
        mKey6 = (TextView) findViewById(R.id.key_6);
        mKey7 = (TextView) findViewById(R.id.key_7);
        mKey8 = (TextView) findViewById(R.id.key_8);
        mKey9 = (TextView) findViewById(R.id.key_9);
        mKeyOK = (TextView) findViewById(R.id.key_ok);
        mKeyBackspace = (ImageView) findViewById(R.id.key_backspace);
        mKeyComma = (TextView) findViewById(R.id.key_comma);
    }

    public void click_button_keyboard() {
        mKey0.setOnClickListener((View.OnClickListener) this);
        mKey1.setOnClickListener((View.OnClickListener) this);
        mKey2.setOnClickListener((View.OnClickListener) this);
        mKey3.setOnClickListener((View.OnClickListener) this);
        mKey4.setOnClickListener((View.OnClickListener) this);
        mKey5.setOnClickListener((View.OnClickListener) this);
        mKey6.setOnClickListener((View.OnClickListener) this);
        mKey7.setOnClickListener((View.OnClickListener) this);
        mKey8.setOnClickListener((View.OnClickListener) this);
        mKey9.setOnClickListener((View.OnClickListener) this);
        mKeyComma.setOnClickListener((View.OnClickListener) this);
        mKeyOK.setOnClickListener((View.OnClickListener) this);
        mKeyBackspace.setOnClickListener((View.OnClickListener) this);
    }

    // Method format date
    public String formatDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    public void setColorWhenIncomeOrExpensePress() {
        if (incomeClicked) {
            mTextIncome.setBackgroundResource(R.drawable.border_text_view_income);
            mTextIncome.setTextColor(Color.WHITE);
            mTextExpense.setTextColor(Color.BLACK);
            mTextExpense.setBackgroundResource(R.drawable.border_text_view_normal);

            mTextValue.setTextColor(getResources().getColor(R.color.color_income));
            mTextZero.setTextColor(getResources().getColor(R.color.color_income));
            mTextVND.setTextColor(getResources().getColor(R.color.color_income));
        } else {
            mTextExpense.setBackgroundResource(R.drawable.border_text_view_expense);
            mTextExpense.setTextColor(Color.WHITE);
            mTextIncome.setTextColor(Color.BLACK);
            mTextIncome.setBackgroundResource(R.drawable.border_text_view_normal);

            mTextExpense.setBackgroundResource(R.drawable.border_text_view_expense);
            mTextValue.setTextColor(getResources().getColor(R.color.color_expense));
            mTextZero.setTextColor(getResources().getColor(R.color.color_expense));
            mTextVND.setTextColor(getResources().getColor(R.color.color_expense));
        }
    }

    // Init spinner
    public void initSpinner() {
        mSpinnerCategory = (Spinner) findViewById(R.id.spinner_category);

        ArrayList<String> categories = myDb.getAllCategory();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategory.setAdapter(dataAdapter);
    }

    public void showImageByName(String nameImage) {
        String pkgName = this.getPackageName();
        int resId = this.getResources().getIdentifier(nameImage, "mipmap", pkgName);

        if (resId != 0) {
            mImageCategory.setImageResource(resId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDb.close();
    }
}


