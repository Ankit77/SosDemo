package app.sosdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import app.sosdemo.R;
import app.sosdemo.model.ActionModel;
import app.sosdemo.model.ContactModel;
import app.sosdemo.util.Constant;

/**
 * Created by ANKIT on 1/31/2017.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.RecyclerViewHolders> {

    private List<ContactModel> mList;
    private Context context;
    private long mLastClickTime = 0;
    private onCallListner onCallListner;

    public void setOnCallListner(ContactListAdapter.onCallListner onCallListner) {
        this.onCallListner = onCallListner;
    }

    public ContactListAdapter(Context context, List<ContactModel> list) {
        this.context = context;
        this.mList = list;

    }


    @Override
    public ContactListAdapter.RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView;
        layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_list, parent, false);
        return new ContactListAdapter.RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(ContactListAdapter.RecyclerViewHolders holder, final int position) {

        holder.tvName.setText(mList.get(position).getName());
        holder.tvAddress.setText(mList.get(position).getAddress());
        holder.tvContactPerson.setText(mList.get(position).getContactperson());
        holder.tvEmail.setText(mList.get(position).getEmail());
        holder.tvLandline.setText(mList.get(position).getLandline());
        holder.tvMobile.setText(mList.get(position).getMobile());


        holder.tvLandline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (onCallListner != null) {
                    onCallListner.onClick(mList.get(position).getLandline());
                }


            }
        });
        holder.tvMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (onCallListner != null) {
                    onCallListner.onClick(mList.get(position).getMobile());
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class RecyclerViewHolders extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvAddress;
        TextView tvLandline;
        TextView tvContactPerson;
        TextView tvMobile;
        TextView tvEmail;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.row_contactlist_tv_name);
            tvAddress = (TextView) itemView.findViewById(R.id.row_contactlist_tv_address);
            tvLandline = (TextView) itemView.findViewById(R.id.row_contactlist_tv_landline);
            tvContactPerson = (TextView) itemView.findViewById(R.id.row_contactlist_tv_contactperson);
            tvMobile = (TextView) itemView.findViewById(R.id.row_contactlist_tv_mobile);
            tvEmail = (TextView) itemView.findViewById(R.id.row_contactlist_tv_emailid);

        }
    }

    public interface onCallListner {
        public void onClick(String phonenumber);
    }


}
