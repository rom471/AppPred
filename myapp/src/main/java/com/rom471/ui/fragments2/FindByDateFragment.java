package com.rom471.ui.fragments2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;

import com.rom471.db2.AppRecordsRepository;
import com.rom471.db2.OneUse;
import com.rom471.recorder.R;
import com.rom471.utils.DBUtils;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class FindByDateFragment extends OneUseFindFragment {





    Button start_date_btn;
    Button end_date_btn;
    Button start_btn;
    TextView start_date_tv;
    TextView end_date_tv;
    TextView result_tv;

    long start_timestamp;
    long end_timestamp;

    public FindByDateFragment(){
        super(R.layout.main_fragment_record_find_by_date,R.id.record_list_by_date);
    }
    private void registRecords(){
        AppRecordsRepository appRecordsRepository;
        appRecordsRepository=new AppRecordsRepository(getActivity().getApplication());
        appRecordsRepository.getAllOneUsesLive().observe(this,new Observer<List<OneUse>>(){
            @Override
            public void onChanged(List<OneUse> records) {
                DBUtils.setOneUseIcon(context,records);
                mAdapter.setOneUses(records);
                list_view.setAdapter(mAdapter);
            }
        });
    }

    public void bindView(){
        list_view=getActivity().findViewById(R.id.record_list_by_date);
        start_date_btn=getActivity().findViewById(R.id.record_date_start_btn);
        end_date_btn=getActivity().findViewById(R.id.record_date_end_btn);
        start_btn=getActivity().findViewById(R.id.date_start_search);
        start_btn.setOnClickListener(this);
        start_date_btn.setOnClickListener(this);
        end_date_btn.setOnClickListener(this);
        start_date_tv=getActivity().findViewById(R.id.record_date_start_tv);

        end_date_tv=getActivity().findViewById(R.id.record_date_end_tv);
        result_tv=getActivity().findViewById(R.id.date_search_result);
    }
    //过滤日期
    private List<OneUse> filterByDate( List<OneUse> old_list,long start,long end){
        if(start_timestamp==0&&end_timestamp==0) //未指定时间则返回所有记录
            return old_list;
        List<OneUse> new_list=new ArrayList<>();
        for (OneUse r:old_list
             ) {
            long timestamp= r.getStartTimestamp();
            if(start_timestamp!=0&&timestamp<start)
                continue;
            if(end_timestamp!=0&&timestamp>end)
                continue;
            new_list.add(r);
        }
        return  new_list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.record_date_start_btn:
               showDatePickerDialog(context,true  );
                break;
            case R.id.record_date_end_btn:
                showDatePickerDialog(context,false);
                break;
            case R.id.date_start_search:
                List<OneUse> records = filterByDate(mOneUses, start_timestamp, end_timestamp);
                result_tv.setText("查到记录："+records.size()+"条");
                mAdapter.setOneUses(records);
                list_view.setAdapter(mAdapter);

                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public  void showDatePickerDialog(Context activity,boolean start) {
        Calendar  calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                long timestamp = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTimeInMillis();
                if(start){
                    start_timestamp=timestamp;
                    start_date_tv.setText(""+(year%100)+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
                }
                else {
                    end_timestamp=timestamp;
                    end_date_tv.setText(""+(year%100)+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");

                }
            }
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }
}
