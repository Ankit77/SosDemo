package app.sosdemo.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.List;

import app.sosdemo.R;
import app.sosdemo.model.ContactModel;

/**
 * Created by ANKIT on 1/31/2017.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.RecyclerViewHolders> {

    private List<ContactModel> mList;
    private Context context;
    private long mLastClickTime = 0;
    private onCallListner onCallListner;
    private int lastPosition = -1;

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
        final int itemType = getItemViewType(position);
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.up_from_bottom
            );
            holder.itemView.startAnimation(animation);
        }
        lastPosition = position;
        holder.tvName.setText(mList.get(position).getOrgName());
        holder.tvAddress.setText(mList.get(position).getAddress());
        if (TextUtils.isEmpty(mList.get(position).getLandline1())) {
            holder.tvLandline.setText("N/A");
        } else {
            holder.tvLandline.setText(mList.get(position).getLandline1());
        }

        if (TextUtils.isEmpty(mList.get(position).getLandline2())) {
            holder.tvMobile.setText("N/A");
        } else {
            holder.tvMobile.setText(mList.get(position).getLandline2());
        }

        if (TextUtils.isEmpty(mList.get(position).getFax1())) {
            holder.tvFax.setText("N/A");
        } else {
            holder.tvFax.setText(mList.get(position).getFax1());
        }
        if (TextUtils.isEmpty(mList.get(position).getEmailAddress())) {
            holder.tvEmail.setText("N/A");
        } else {
            holder.tvEmail.setText(mList.get(position).getEmailAddress());
        }

        holder.tvLandline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (!TextUtils.isEmpty(mList.get(position).getLandline1())) {
                    if (onCallListner != null) {
                        onCallListner.onClick(mList.get(position).getLandline1());
                    }
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
                if (TextUtils.isEmpty(mList.get(position).getLandline2())) {
                    if (onCallListner != null) {
                        onCallListner.onClick(mList.get(position).getLandline2());
                    }
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
        TextView tvMobile;
        TextView tvFax;
        TextView tvEmail;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.row_contactlist_tv_name);
            tvAddress = (TextView) itemView.findViewById(R.id.row_contactlist_tv_address);
            tvLandline = (TextView) itemView.findViewById(R.id.row_contactlist_tv_landline);
            tvMobile = (TextView) itemView.findViewById(R.id.row_contactlist_tv_mobile);
            tvFax = (TextView) itemView.findViewById(R.id.row_contactlist_tv_fax);
            tvEmail = (TextView) itemView.findViewById(R.id.row_contactlist_tv_emailid);

        }
    }

    public interface onCallListner {
        public void onClick(String phonenumber);
    }


}
