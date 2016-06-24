package hanhit.mywalletapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigInteger;
import java.util.List;

import hanhit.mywalletapp.MyHandle;
import hanhit.mywalletapp.R;
import hanhit.mywalletapp.database.MyDatabase;
import hanhit.mywalletapp.model.Category;
import hanhit.mywalletapp.model.Item;

/**
 * Created by hanh.tran on 6/22/2016.
 */
public class AdapterListViewDetailReport extends BaseAdapter {
    private Activity mContext;
    private List objects;
    private MyHandle myHandle;

    public AdapterListViewDetailReport(Activity mContext, List objects) {
        this.mContext = mContext;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        myHandle = new MyHandle();

        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            try {
                Category category = (Category) objects.get(position);
                view = inflater.inflate(R.layout.custom_report_category, null);

                ImageView image = (ImageView) view.findViewById(R.id.image_category_report);
                int id_image = mContext.getResources().getIdentifier(category.getNameIconCategory(), "mipmap", mContext.getPackageName());
                if (id_image != 0)
                    image.setImageResource(id_image);

                TextView name_category = (TextView) view.findViewById(R.id.txt_name_category_report);
                name_category.setText(category.getNameCategory());

                TextView value_category = (TextView) view.findViewById(R.id.txt_value_category_report);

                BigInteger valueInt = getValueAllItemByCategory(category.getIdCategory());
                if (valueInt.compareTo(BigInteger.valueOf(0)) >= 0) {
                    value_category.setTextColor(mContext.getResources().getColor(R.color.color_income));
                } else {
                    value_category.setTextColor(mContext.getResources().getColor(R.color.color_expense));
                }
                String valueStr = myHandle.handleStringValue(valueInt + "");
                value_category.setText(valueStr + ",000 VND");

            } catch (ClassCastException e) {
                Item item = (Item) objects.get(position);
                view = inflater.inflate(R.layout.custom_report_item, null);

                TextView txt_name_item = (TextView) view.findViewById(R.id.txt_name_item_report);
                txt_name_item.setText(item.getNameItem());

                TextView txt_date_item = (TextView) view.findViewById(R.id.txt_date_item_report);
                txt_date_item.setText(item.getDateItem());

                TextView txt_value_item = (TextView) view.findViewById(R.id.txt_value_item_report);
                if (item.getTypeItem() == 0) {
                    txt_value_item.setTextColor(mContext.getResources().getColor(R.color.color_income));
                } else {
                    txt_value_item.setTextColor(mContext.getResources().getColor(R.color.color_expense));
                }
                String value = item.getValueItem();
                txt_value_item.setText(myHandle.handleStringValue(value) + ",000 VND");
            }
        }

        return view;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public BigInteger getValueAllItemByCategory(int id_cate) {
        BigInteger sumIncome = BigInteger.valueOf(0);
        BigInteger sumExpense = BigInteger.valueOf(0);
        for (int i = 0; i < objects.size(); i++) {
            try {
                Item item = (Item) objects.get(i);
                if (item.getIdCategoryItem() == id_cate) {
                    if (item.getTypeItem() == 0) {
                        BigInteger income = BigInteger.valueOf(Integer.parseInt(item.getValueItem()));
                        sumIncome = sumIncome.add(income);
                    } else {
                        BigInteger expense = BigInteger.valueOf(Integer.parseInt(item.getValueItem()));
                        sumExpense = sumExpense.add(expense);
                    }
                }
            } catch (ClassCastException ex) {}
        }
        return sumIncome.subtract(sumExpense);
    }
}
