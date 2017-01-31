package app.sosdemo.adapter;

import android.content.Context;
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
import app.sosdemo.util.Constant;

/**
 * Created by indianic on 30/01/17.
 */

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.RecyclerViewHolders> {

    private List<ActionModel> mList;
    private Context context;
    private long mLastClickTime = 0;
    private onActionListner onactionListner;
    private boolean isPairedevice;

    public void setOnactionListner(onActionListner onactionListner) {
        this.onactionListner = onactionListner;
    }

    public DashboardAdapter(Context context, List<ActionModel> list, boolean isPairedDevice) {
        this.context = context;
        this.mList = list;
        this.isPairedevice = isPairedDevice;

    }


    @Override
    public DashboardAdapter.RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView;
        layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_dashboard_adapter, parent, false);
        return new RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(DashboardAdapter.RecyclerViewHolders holder, final int position) {

        holder.tvCaption.setText(mList.get(position).getCaption());
        if (mList.get(position).getAction().equalsIgnoreCase(Constant.TYPE_VIDEO)) {
            holder.ivImage.setImageResource(R.drawable.ic_videocam);
        } else if (mList.get(position).getAction().equalsIgnoreCase(Constant.TYPE_IMAGE)) {
            holder.ivImage.setImageResource(R.drawable.ic_photo);
        } else if (mList.get(position).getAction().equalsIgnoreCase(Constant.TYPE_AUDIO)) {
            holder.ivImage.setImageResource(R.drawable.ic_mic);
        }
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (onactionListner != null) {
                    onactionListner.onClick(mList.get(position).getAction());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class RecyclerViewHolders extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvCaption;
        LinearLayout llMain;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.row_dashboard_iv_image);
            tvCaption = (TextView) itemView.findViewById(R.id.row_dashboard_tv_name);
            llMain = (LinearLayout) itemView.findViewById(R.id.row_dashboard_ll_main);

        }
    }

    public interface onActionListner {
        public void onClick(String action);

    }
}
