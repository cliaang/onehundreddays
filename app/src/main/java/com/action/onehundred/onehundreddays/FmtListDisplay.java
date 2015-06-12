package com.action.onehundred.onehundreddays;

import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class FmtListDisplay extends ListFragment {
    private Action currentAction = new Action("");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            SharedPreferences sp = getActivity().getSharedPreferences(Config.CurrentAction, Context.MODE_PRIVATE);
            String jsonString = sp.getString(Config.CurrentActionDetails, "");
            currentAction = Action.parseJSONString(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> records = new ArrayList<>();
        String record;
        DateFormat df = new SimpleDateFormat("dd/MM", Locale.getDefault());
        for (int i = 0; i<100;i++){
            DailyRecord dailyRecord = currentAction.getRecords()[i];
            record = df.format( dailyRecord.getDate());
            if (dailyRecord.isCompleted()){
                record = record +" 完成 " +dailyRecord.getRecord();
            } else {
                record = record + " 未完成";
            }
            records.add(record);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_list_item_1,records);
        setListAdapter(adapter);
    }

}
