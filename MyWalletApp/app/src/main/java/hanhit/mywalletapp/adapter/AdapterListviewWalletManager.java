package hanhit.mywalletapp.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import hanhit.mywalletapp.ItemActivity;
import hanhit.mywalletapp.MyHandle;
import hanhit.mywalletapp.R;
import hanhit.mywalletapp.WalletManagerActivity;
import hanhit.mywalletapp.database.MyDatabase;
import hanhit.mywalletapp.model.Item;

/**
 * Created by hanh.tran on 6/16/2016.
 */
public class AdapterListViewWalletManager extends ArrayAdapter<Item> {

    private Activity mContext;
    private ArrayList<Item> itemList;
    private MyDatabase myDb;
    private MyHandle myHandle;

    public AdapterListViewWalletManager(Activity context, int resource, ArrayList<Item> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.itemList = objects;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Item itemSelected = itemList.get(position);
        final ViewHolderItem holder;
        View view = convertView;
        if (view == null) {
            myHandle = new MyHandle();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_listview_wallet_manager, null);
            holder = new ViewHolderItem();

            holder.image_category_item = (ImageView) view.findViewById(R.id.image_category_item);
            holder.txtNameItem = (TextView) view.findViewById(R.id.txtName);
            holder.txtDateOfItem = (TextView) view.findViewById(R.id.txtDate);
            holder.txtValueItem = (TextView) view.findViewById(R.id.txtValue);
            holder.image_edit = (ImageView) view.findViewById(R.id.image_edit);
            holder.image_delete = (ImageView) view.findViewById(R.id.image_delete);

            view.setTag(holder);
        } else {
            holder = (ViewHolderItem) view.getTag();
        }

        final Item item = itemList.get(position);
        holder.txtNameItem.setText(item.getNameItem());
        holder.txtDateOfItem.setText(item.getDateItem());
        holder.txtValueItem.setText(myHandle.handleString(item.getValueItem() + "") + ",000 VND");

        if (item.getTypeItem() == 0) {
            holder.txtValueItem.setTextColor(mContext.getResources().getColor(R.color.color_income));
        } else {
            holder.txtValueItem.setTextColor(mContext.getResources().getColor(R.color.color_expense));
        }

        if (item.getIdCategoryItem() == 0) {
            holder.showImageByName(mContext, "ic_movie");
        } else if (item.getIdCategoryItem() == 1) {
            holder.showImageByName(mContext, "ic_shop");
        } else {
            holder.showImageByName(mContext, "ic_another");
        }

        holder.image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEdit = new Intent(mContext, ItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", "Edit");
                bundle.putSerializable("object", itemSelected);
                intentEdit.putExtra("data", bundle);
                mContext.startActivityForResult(intentEdit, Activity.RESULT_OK);
            }
        });

        holder.image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setView(R.layout.layout_confirm_delete)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                myDb = new MyDatabase(mContext);
                                if (myDb.deleteItem(item.getIdItem()) != 0) {
                                    itemList.remove(position);
                                    Toast.makeText(mContext, "Deleted success!", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(mContext, "Delete fail! " + myDb.deleteItem(item.getIdItem()), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .show();
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.image_edit.setVisibility(View.VISIBLE);
                holder.image_delete.setVisibility(View.VISIBLE);
                return false;
            }
        });


        return view;
    }

    static class ViewHolderItem {
        private ImageView image_category_item;
        private ImageView image_delete;
        private ImageView image_edit;
        private TextView txtNameItem;
        private TextView txtDateOfItem;
        private TextView txtValueItem;

        public void showImageByName(Activity mActivity, String nameImage) {
            String pkgName = mActivity.getPackageName();
            int resId = mActivity.getResources().getIdentifier(nameImage, "mipmap", pkgName);

            if (resId != 0) {
                image_category_item.setImageResource(resId);
            }
        }
    }
}
