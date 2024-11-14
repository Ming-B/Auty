package com.example.myapplication.models;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorkflowModel extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "AUTY1";
    private static final String TABLE_WORKFLOWS = "workflows";
    private static final String KEY_ID = "id";
    private static final String KEY_WF_NAME = "workflow_name";
    private static final String KEY_STATUS = "status";
    private static final String KEY_USER_ID = "user_id";
//    private static final String KEY_T_NAME = "trigger_name";
//    private static final String KEY_R_NAME = "response_name";
//    private static final String KEY_RESPONSE = "response";


    public WorkflowModel(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public  void onCreate(SQLiteDatabase db) {
        String CREATE_WORKFLOW_TABLE = String.format(
                "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s VARCHAR(255) NOT NULL, " +
//                    "%s VARCHAR(255) NOT NULL, " +
//                    "%s VARCHAR(255) NOT NULL, " +
                    "%s INTEGER, " +
                    "%s INTEGER, " +
                    "FOREIGN KEY(%s) REFERENCES %s (%s)" +
//                    "%s TEXT" +
                ")"
//                , TABLE_WORKFLOWS, KEY_ID, KEY_WF_NAME, KEY_T_NAME, KEY_R_NAME, KEY_STATUS, KEY_RESPONSE);
        , TABLE_WORKFLOWS, KEY_ID, KEY_WF_NAME, KEY_STATUS, KEY_USER_ID, KEY_USER_ID, "users", KEY_USER_ID);

        db.execSQL(CREATE_WORKFLOW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_WORKFLOWS);

        db.execSQL(DROP_TABLE);

        onCreate(db);
    }

    public boolean addWorkflow(WorkflowConfig workflowConfig, long user_id){
        SQLiteDatabase db = this.getWritableDatabase();

        String workflow_name = workflowConfig.getWorkflowName();
//        String trigger_name = workflowConfig.getTriggerName();
//        String response_name = workflowConfig.getResponseName();
//        String response = workflowConfig.getResponse();
        Boolean status = workflowConfig.getStatus();

        WorkflowConfig obtainedWorkflowConfig = this.getWorkflow(workflow_name);

        if (obtainedWorkflowConfig != null) {
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put(KEY_WF_NAME, workflow_name);
            values.put(KEY_STATUS, status);
            values.put(KEY_USER_ID, user_id);

//            values.put(KEY_T_NAME, trigger_name);
//            values.put(KEY_R_NAME, response_name);
//            values.put(KEY_RESPONSE, response);

            db.insert(TABLE_WORKFLOWS, null, values);
            db.close();

            return true;
        }
    }

    public WorkflowConfig getWorkflow(String workflowName){
        SQLiteDatabase db = this.getWritableDatabase();

//        Cursor cursor = db.query(TABLE_WORKFLOWS,
//                new String[] {KEY_ID, KEY_WF_NAME, KEY_T_NAME, KEY_R_NAME, KEY_STATUS, KEY_RESPONSE},
//                KEY_WF_NAME + "=?",
//                new String[] {String.valueOf(workflowName)},
//                null, null, null, null
//                );

        Cursor cursor = db.query(TABLE_WORKFLOWS,
                new String[] {KEY_ID, KEY_WF_NAME, KEY_STATUS},
                KEY_WF_NAME + "=?",
                new String[] {String.valueOf(workflowName)},
                null, null, null, null
        );

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                return null;
            }
        } else {
            return  null;
        }

        int workflowNameIndex = cursor.getColumnIndex(KEY_WF_NAME);
//        int triggerNameIndex = cursor.getColumnIndex(KEY_T_NAME);
//        int responseNameIndex = cursor.getColumnIndex(KEY_R_NAME);
//        int responseIndex = cursor.getColumnIndex(KEY_RESPONSE);
        int statusIndex = cursor.getColumnIndex(KEY_STATUS);


        WorkflowConfig workflowConfig = new WorkflowConfig(
                cursor.getString(workflowNameIndex),
//                cursor.getString(triggerNameIndex),
//                cursor.getString(responseNameIndex),
                cursor.getInt(statusIndex) == 1
//                cursor.getString(responseIndex)
        );

        cursor.close();
        return workflowConfig;
    }

    @SuppressLint("Range")
    public ArrayList<WorkflowConfig> getWorkflows() {
        ArrayList<WorkflowConfig> workflowConfigs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                KEY_ID, KEY_WF_NAME, KEY_STATUS, KEY_USER_ID
        };
        Cursor cursor = db.query(TABLE_WORKFLOWS, columns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                WorkflowConfig workflowConfig = new WorkflowConfig();
                workflowConfig.setWorkflowName(cursor.getString(cursor.getColumnIndex(KEY_WF_NAME)));
                workflowConfig.setStatus(1 == cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));

                workflowConfigs.add(workflowConfig);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return workflowConfigs;
    }

    @SuppressLint("Range")
    public Map<String, Boolean> getWorkflowByUser(long user_id) {
        Map<String, Boolean> workflowConfigMapping = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_WORKFLOWS,
                new String[] { KEY_WF_NAME, KEY_STATUS},
                KEY_USER_ID + " = ?",
                new String[] { String.valueOf(user_id) },
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                WorkflowConfig workflow = new WorkflowConfig();
                workflow.setWorkflowName(cursor.getString(cursor.getColumnIndex(KEY_WF_NAME)));
                workflow.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)) == 1);
//                workflow.setUserId(cursor.getLong(cursor.getColumnIndex(KEY_USER_ID)));
                workflowConfigMapping.put(workflow.getWorkflowName(), workflow.getStatus());
            } while (cursor.moveToNext());
        }
        return workflowConfigMapping;
    }
}
