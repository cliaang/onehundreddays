package com.action.onehundred.onehundreddays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Action {
    private static final String JSON_id = "json_id";
    //private static final String JSON_length = "json_length";
    private static final String JSON_name = "json_name";
    private static final String JSON_startDate = "json_startDateString";
    //private static final String JSON_endDate = "json_endDateString";
    private static final String JSON_alarmingHour = "json_alarmingHour";
    private static final String JSON_alarmingMinute = "json_alarmingMinute";
    private static final String JSON_currentLasting = "json_currentLasting";
    private static final String JSON_totalCompletions = "json_totalCompletions";
    private static final String JSON_dailyRecords = "json_dailyRecords";
    private static final String JSON_actionCategory = "json_actionCategory";
    private static final String JSON_actionCategoryIndex = "json_actionCategoryIndex";

    // json name for inner class
    private static final String JSON_recordDate = "json_recordDate";
    private static final String JSON_completed = "json_completed";
    private static final String JSON_first = "json_first";
    private static final String JSON_last = "json_last";
    private static final String JSON_record = "json_record";


    // Action Data
    private UUID id;
    private Date start;
    private final int length=Config.ACTION_LENGTH;
    private Date end;
    private String actionName;
    private int alarmingHour=0;
    private int alarmingMinute = 0;
    private int currentLasting = 0;
    private int totalCompletions = 0;
    private DailyRecord[] records = new DailyRecord[Config.ACTION_LENGTH];

    private int actionCategory=-1;

    private int actionCategoryIndex=-1;

    public int getTotalCompletions() {
        return totalCompletions;
    }

    public int getCurrentLasting() {
        return currentLasting;
    }

    public Action(String name){
        id = UUID.randomUUID();
        actionName = name;
        start = new Date();
        calculateEndingDate();
        initDailyRecord();
    }

    public Action( String name, Date startDate){
        id = UUID.randomUUID();
        actionName = name;
        start = startDate;
        calculateEndingDate();
        initDailyRecord();
    }

    public Action(String name,int hour, int minute,int actionCategory, int actionCategoryIndex){
        id = UUID.randomUUID();
        actionName = name;
        start = new Date();
        alarmingHour = hour;
        alarmingMinute = minute;
        this.actionCategory = actionCategory;
        this.actionCategoryIndex = actionCategoryIndex;
        calculateEndingDate();
        initDailyRecord();
    }
    private Action(UUID id, Date start, String actionName,int hour, int minute,int actionCategory, int actionCategoryIndex, int currentLasting, int totalCompletions,DailyRecord[] records){
        this.id = id;
        this.start = start;
        this.actionName = actionName;
        alarmingHour = hour;
        alarmingMinute = minute;
        this.actionCategory = actionCategory;
        this.actionCategoryIndex = actionCategoryIndex;
        this.currentLasting = currentLasting;
        this.totalCompletions = totalCompletions;
        calculateEndingDate();
        this.records = records;

    }

    private void initDailyRecord(){
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        for (int i = 0; i<length; i++){
            records[i] = new DailyRecord(c.getTime());
            c.add(Calendar.DATE,1);
        }
    }

    /**
     * get the EndingDate through startingDate and length
     */
    private void calculateEndingDate(){
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DATE,Config.ACTION_LENGTH);
        end = c.getTime();
    }

    public String getActionName() {
        return actionName;
    }

    public Date getStart() {
        return start;
    }

    public DailyRecord[] getRecords() {
        return records;
    }

    public int getActionCategoryIndex() {
        return actionCategoryIndex;
    }

    public int getActionCategory() {
        return actionCategory;
    }

    /**
     * get the Action from JSONObject
     * @param jsonString the jsonString retrieved from SharedPreference.
     * @return the Action stored in JSONObject
     * @throws JSONException
     */
    public static Action parseJSONString(String jsonString) throws JSONException{
        JSONObject json;
        json = new JSONObject(jsonString);
        UUID id = UUID.fromString(json.getString(JSON_id));
        Date start = new Date(json.getLong(JSON_startDate));
        String actionName = json.getString(JSON_name);
        int hour = json.getInt(JSON_alarmingHour);
        int minute = json.getInt(JSON_alarmingMinute);
        int actionCategroy = json.getInt(JSON_actionCategory);
        int actionCategroyIndex = json.getInt(JSON_actionCategoryIndex);
        int currentLasting = json.getInt(JSON_currentLasting);
        int totalCompletions = json.getInt(JSON_totalCompletions);
        JSONArray array = json.getJSONArray(JSON_dailyRecords);
        DailyRecord[] dailyRecords = new DailyRecord[Config.ACTION_LENGTH];
        for (int i = 0; i<Config.ACTION_LENGTH;i++){
            JSONObject json_record = array.getJSONObject(i);
            Date date = new Date(json_record.getLong(JSON_recordDate));
            boolean completed = json_record.getBoolean(JSON_completed);
            boolean first = json_record.getBoolean(JSON_first);
            boolean last = json_record.getBoolean(JSON_last);
            String record = json_record.getString(JSON_record);
            dailyRecords[i] = new DailyRecord(date,record,completed,first,last);
        }
        return new Action(id,start,actionName,hour,minute,actionCategroy,actionCategroyIndex, currentLasting,totalCompletions,dailyRecords);
    }

    /**
     * transform the Action instance to JSONObject,and then save JSONObject.toString()
     * to SharedPreference.
     * @return the JSONObject corresponding to the Action
     * @throws JSONException
     */
    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_id,id.toString());
        json.put(JSON_name,actionName);
        //json.put(JSON_length,length);
        json.put(JSON_startDate, start.getTime());
        //json.put(JSON_endDate, end.getTime());
        json.put(JSON_alarmingHour,alarmingHour);
        json.put(JSON_alarmingMinute,alarmingMinute);
        json.put(JSON_currentLasting,currentLasting);
        json.put(JSON_totalCompletions,totalCompletions);
        json.put(JSON_actionCategory,actionCategory);
        json.put(JSON_actionCategoryIndex,actionCategoryIndex);
        JSONArray array = new JSONArray();
        JSONObject JSONRecords[] = new JSONObject[length];
        for(int i = 0; i<length;i++){
            JSONRecords[i] = new JSONObject();
            JSONRecords[i].put(JSON_recordDate,records[i].getDate().getTime());
            JSONRecords[i].put(JSON_completed, records[i].isCompleted());
            JSONRecords[i].put(JSON_first, records[i].isFirst());
            JSONRecords[i].put(JSON_last, records[i].isLast());
            JSONRecords[i].put(JSON_record, records[i].getRecord());
            array.put(i,JSONRecords[i]);
        }
        json.put(JSON_dailyRecords,array);
        return json;
    }
}
