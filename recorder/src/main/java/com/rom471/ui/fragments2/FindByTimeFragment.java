package com.rom471.ui.fragments2;

import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rom471.adapter.RecordsAdapter;
import com.rom471.db.RecordsViewModel;
import com.rom471.db.Record;
import com.rom471.db2.OneUse;
import com.rom471.recorder.R;
import com.rom471.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FindByTimeFragment extends OneUseFindFragment {



    Button start_date_btn;
    Button end_date_btn;
    Button start_btn;
    TextView start_time_tv;
    TextView end_time_tv;
    TextView result_tv;

    long start_timestamp;
    long end_timestamp;
    Context context;

    public FindByTimeFragment(){
        super(R.layout.main_fragment_record_find_by_time,R.id.record_list_by_time);
    }

    public void bindView(){
        list_view=getActivity().findViewById(R.id.record_list_by_time);
        start_date_btn=getActivity().findViewById(R.id.record_time_start_btn);
        end_date_btn=getActivity().findViewById(R.id.record_time_end_btn);
        start_btn=getActivity().findViewById(R.id.time_start_search);
        start_time_tv =getActivity().findViewById(R.id.record_time_start_tv);
        end_time_tv =getActivity().findViewById(R.id.record_time_end_tv);
        result_tv=getActivity().findViewById(R.id.time_search_result);
    }
    //过滤时间
    private List<OneUse> filterByTime(List<OneUse> old_list, long start, long end){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        if(start_timestamp==0&&end_timestamp==0) //未指定时间则返回所有记录
            return old_list;
        List<OneUse> new_list=new ArrayList<>();
        for (OneUse r:old_list
        ) {
            long timestamp= r.getStartTimestamp();

            cal.setTimeInMillis(timestamp);
            int hours = cal.get(Calendar.HOUR_OF_DAY);
            int minutes = cal.get(Calendar.MINUTE);
            //int ms_in_day=(hours*60+minutes)*60*1000;
            int ms_in_day = cal.get(Calendar.MILLISECONDS_IN_DAY);

            if(start!=0&&ms_in_day<start)
                continue;
            if(end!=0&&ms_in_day>end)
                continue;
            new_list.add(r);
        }
        return  new_list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.record_time_start_btn:
                showTimePickerDialog(context,true  );
                break;
            case R.id.record_time_end_btn:
                showTimePickerDialog(context,false);
                break;
            case R.id.time_start_search:
                List<OneUse> records = filterByTime(mOneUses, start_timestamp, end_timestamp);
                result_tv.setText("查到记录："+records.size()+"条");
                mAdapter.setOneUses(records);
                list_view.setAdapter(mAdapter);

                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showTimePickerDialog(Context context,boolean start){
        Calendar cal = Calendar.getInstance();
        Dialog timeDialog=new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                SimpleDateFormat format = new SimpleDateFormat(" HH:mm ");
                // TODO Auto-generated method stub
                cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                cal.set(Calendar.MINUTE,minute);
                long timestamp=cal.get(Calendar.MILLISECONDS_IN_DAY);
                if(start){
                    start_time_tv.setText(format.format(cal.getTime()));
                    start_timestamp=timestamp;
                }
                else {
                    end_time_tv.setText(format.format(cal.getTime()));
                    end_timestamp=timestamp;
                }
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        timeDialog.setTitle("请选择时间");
        timeDialog.show();
    }

}
