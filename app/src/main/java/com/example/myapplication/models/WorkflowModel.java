package com.example.myapplication.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkflowModel extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AUTY";
    private static final String TABLE_WORKFLOWS = "workflows";
    private static final String KEY_ID = "id";
    private static final String KEY_WF_NAME = "workflow_name";
    private static final String KEY_T_NAME = "trigger_name";
    private static final String KEY_R_NAME = "response_name";
    private static final String KEY_STATUS = "status";
    private static final String KEY_RESPONSE = "response";


    public WorkflowModel(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public  void onCreate(SQLiteDatabase db) {
        String CREATE_WORKFLOW_TABLE = String.format(
                "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s VARCHAR(255) NOT NULL, " +
                    "%s VARCHAR(255) NOT NULL, " +
                    "%s VARCHAR(255) NOT NULL, " +
                    "%s INTEGER, " +
                    "%s TEXT" +
                ")"
        , TABLE_WORKFLOWS, KEY_ID, KEY_WF_NAME, KEY_T_NAME, KEY_R_NAME, KEY_STATUS, KEY_RESPONSE);

        db.execSQL(CREATE_WORKFLOW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_WORKFLOWS);

        db.execSQL(DROP_TABLE);

        onCreate(db);
    }

    public boolean addWorkflow(WorkflowConfig workflowConfig){
        SQLiteDatabase db = this.getWritableDatabase();

        String workflow_name = workflowConfig.getWorkflowName();
        String trigger_name = workflowConfig.getTriggerName();
        String response_name = workflowConfig.getResponseName();
        String response = workflowConfig.getResponse();
        Boolean status = workflowConfig.getStatus();

        WorkflowConfig obtainedWorkflowConfig = this.getWorkflow(workflow_name);

        if (obtainedWorkflowConfig != null) {
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put(KEY_WF_NAME, workflow_name);
            values.put(KEY_T_NAME, trigger_name);
            values.put(KEY_R_NAME, response_name);
            values.put(KEY_STATUS, status);
            values.put(KEY_RESPONSE, response);

            db.insert(TABLE_WORKFLOWS, null, values);
            db.close();

            return true;
        }
    }

    public WorkflowConfig getWorkflow(String workflowName){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_WORKFLOWS,
                new String[] {KEY_ID, KEY_WF_NAME, KEY_T_NAME, KEY_R_NAME, KEY_STATUS, KEY_RESPONSE},
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
        int triggerNameIndex = cursor.getColumnIndex(KEY_T_NAME);
        int responseNameIndex = cursor.getColumnIndex(KEY_R_NAME);
        int responseIndex = cursor.getColumnIndex(KEY_RESPONSE);
        int statusIndex = cursor.getColumnIndex(KEY_STATUS);


        WorkflowConfig workflowConfig = new WorkflowConfig(
                cursor.getString(workflowNameIndex),
                cursor.getString(triggerNameIndex),
                cursor.getString(responseNameIndex),
                cursor.getInt(statusIndex) == 1,
                cursor.getString(responseIndex)
        );


        cursor.close();
        return workflowConfig;
    }
}
