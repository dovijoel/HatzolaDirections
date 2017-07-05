package support.techwise.hatzolahdirections;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

/**
 * Created by dovi on 2017/07/03.
 * With help from https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
 */

public class SMSRecycleViewAdaptor extends CursorRecyclerViewAdapter<SMSRecycleViewAdaptor.smsCardViewHolder> {

    public static class smsCardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView smsNumber;
        TextView smsText;
        TextView smsDate;
        smsItem sms;


        public smsItem getSms() {
            return sms;
        }

        public void setSms(smsItem sms) {
            this.sms = sms;
        }

        /**
         * Default constructor
         * @param itemView
         */
        smsCardViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.smsCardView);
            Log.d("DEBUG", "Initialising views");
            smsNumber = (TextView) itemView.findViewById(R.id.smsNumber);
            smsText = (TextView) itemView.findViewById(R.id.smsText);
            smsDate = (TextView) itemView.findViewById(R.id.smsDate);
            cv.setOnClickListener(new CardView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DirectionsLib.startMaps(sms, itemView.getContext());
                    Toast toast = Toast.makeText(cv.getContext(), sms.getCallAddress(), Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        }


    }

    public SMSRecycleViewAdaptor(Context mContext, Cursor mCursor) {
        super(mContext, mCursor);
    }

    @Override
    public smsCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_cardview, parent, false);
        smsCardViewHolder smsCVH = new smsCardViewHolder(v);
        return smsCVH;
    }

    @Override
    public void onBindViewHolder(smsCardViewHolder holder, Cursor cursor) {
        if (cursor != null) {
            smsItem sms = smsItem.fromCursor(cursor);
            if (sms.isHatzolahType()) {
                holder.setSms(sms);
                holder.smsText.setText(sms.getBody());
                holder.smsNumber.setText(sms.getAddress());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String date = sdf.format(Long.parseLong(sms.getDate()));
                holder.smsDate.setText(date);

                /*
                if (sms.isHatzolahType()) {
                    holder.cv.setCardBackgroundColor(Color.GREEN);
                } else {
                    holder.cv.setCardBackgroundColor(Color.GRAY);
                }*/
            } else {
                holder.cv.setVisibility(View.GONE);
            }
        }
    }
}
