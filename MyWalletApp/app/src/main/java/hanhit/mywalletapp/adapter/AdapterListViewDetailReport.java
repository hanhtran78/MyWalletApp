package hanhit.mywalletapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
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
    private MyDatabase myDb;

    public AdapterListViewDetailReport(Activity mContext, List objects) {
        this.mContext = mContext;
        this.objects = objects;
    }

    public String getMonth(String date) {
        String[] times = date.split("-");
        return times[1];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        myHandle = new MyHandle();
        myDb = new MyDatabase(mContext);

        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            try {
                Category category = (Category) objects.get(position);
                view = inflater.inflate(R.layout.custom_category_detail_report, null);

                ImageView image = (ImageView) view.findViewById(R.id.image_category_report);
                int id_image = mContext.getResources().getIdentifier(category.getNameIconCategory(), "mipmap", mContext.getPackageName());
                if (id_image != 0)
                    image.setImageResource(id_image);

                TextView name_category = (TextView) view.findViewById(R.id.txt_name_category_report);
                name_category.setText(category.getNameCategory());

                TextView value_category = (TextView) view.findViewById(R.id.txt_value_category_report);

                //test for month 6
                int valueInt = myDb.getValueAllItemByCategoryAndMonth("06", category.getIdCategory());
                if (valueInt > 0) {
                    value_category.setTextColor(mContext.getResources().getColor(R.color.color_income));
                } else {
                    value_category.setTextColor(mContext.getResources().getColor(R.color.color_expense));
                }
                String valueStr = myHandle.handleString(valueInt + "");
                value_category.setText(valueStr + ", 000 VND");

            } catch (ClassCastException e) {
                Item item = (Item) objects.get(position);
                view = inflater.inflate(R.layout.custom_item_detail_report, null);

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
                String value = item.getValueItem() + "";
                txt_value_item.setText(myHandle.handleString(value) + ",000 VND");
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
}
