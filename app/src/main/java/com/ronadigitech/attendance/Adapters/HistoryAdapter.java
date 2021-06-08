package com.ronadigitech.attendance.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.type.DateTime;
import com.ronadigitech.attendance.Activities.HistoryDetailActivity;
import com.ronadigitech.attendance.Models.Attendance;
import com.ronadigitech.attendance.R;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends BaseAdapter {

    private Context context;
    private List<Attendance> dataList;

    public HistoryAdapter(Context context, List<Attendance> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.list_history, null);

        HistoryDetailHolder historyDetailHolder = new HistoryDetailHolder();
        historyDetailHolder.txtHistoryType = convertView.findViewById(R.id.txt_HistoryType);
        historyDetailHolder.txtHistoryTime = convertView.findViewById(R.id.txt_HistoryTime);
        historyDetailHolder.imgHistoryIcon = convertView.findViewById(R.id.img_HistoryIcon);
        historyDetailHolder.linearHistory = convertView.findViewById(R.id.linearHistory);

        Attendance attendance = dataList.get(position);
        String date= DateFormat.getDateTimeInstance().format(attendance.getAttendance_created_at());
        Log.d("[DEBUG]", "timestamp: " + attendance.getAttendance_created_at());
        Log.d("[DEBUG]", "parsed: " + date);
        historyDetailHolder.txtHistoryTime.setText(date);

        String attendanceType = "";
        switch (attendance.getAttendance_type()) {
            case "in": {
                attendanceType = "Absen Masuk";
                Glide.with(context)
                        .load(R.drawable.ic_baseline_thumb_up_24)
                        .into(historyDetailHolder.imgHistoryIcon);
                break;
            }
            case "out": {
                attendanceType = "Absen Keluar";
                Glide.with(context)
                        .load(R.drawable.ic_logout)
                        .into(historyDetailHolder.imgHistoryIcon);
                break;
            }
            default: { break; }
        }

        historyDetailHolder.txtHistoryType.setText(attendanceType);

        String finalAttendanceType = attendanceType;
        historyDetailHolder.linearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("[DEBUG]", "clicked history card");

                Intent i = new Intent(context, HistoryDetailActivity.class);
                i.putExtra("pic1", attendance.getAttendance_photo_1());
                i.putExtra("pic2", attendance.getAttendance_photo_2());
                i.putExtra("pic3", attendance.getAttendance_photo_3());
                i.putExtra("pic4", attendance.getAttendance_photo_4());
                i.putExtra("time", date);
                i.putExtra("type", finalAttendanceType);
                context.startActivity(i);
            }
        });

        return convertView;
    }

    public class HistoryDetailHolder {
        TextView txtHistoryType, txtHistoryTime;
        ImageView imgHistoryIcon;
        LinearLayout linearHistory;
    }
}
