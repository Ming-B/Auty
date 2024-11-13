//package com.example.myapplication.models;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import java.util.ArrayList;
//
//public class UserToWorkflowModel extends SQLiteOpenHelper {
//    private static final String DATABASE_NAME = "AUTY1";
//    private static final int DATABASE_VERSION = 2;
//
//    private static final String TABLE_USER_WORKFLOWS = "user_to_workflows";
//    private static final String KEY_USER_ID = "user_id";
//    private static final String KEY_WF_ID = "workflow_id";
//
//
//
//
//    public UserToWorkflowModel(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
////        String CREATE_TABLE_USER_TO_WORKFLOW = "CREATE TABLE " + TABLE_USER_WORKFLOWS + " ("
////                + KEY_USER_ID + " INTEGER, "
////                + KEY_WF_ID + " INTEGER, "
////                + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + "users" + "(" + KEY_USER_ID + "), "
////                + "FOREIGN KEY(" + KEY_WF_ID + ") REFERENCES " + "workflows" + "(" + KEY_WF_ID + "), "
////                + "PRIMARY KEY(" + KEY_USER_ID + ", " + KEY_WF_ID + "));";
////
//        String CREATE_TABLE_USER_TO_WORKFLOW = String.format("CREATE TABLE %s (" +
//                "%s INTEGER, " +
//                "%s INTEGER, " +
//                "FOREIGN KEY(%s) REFERENCES %s (%s), " +
//                "FOREIGN KEY(%s ) REFERENCES %s (%s), " +
//                "PRIMARY KEY(%s, %s));",
//                TABLE_USER_WORKFLOWS, KEY_USER_ID,  KEY_WF_ID, KEY_USER_ID, "users", KEY_USER_ID, KEY_WF_ID, "workflows", KEY_WF_ID, KEY_USER_ID, KEY_WF_ID);
//
//        db.execSQL(CREATE_TABLE_USER_TO_WORKFLOW);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_USER_WORKFLOWS);
//
//        db.execSQL(DROP_TABLE);
//
//        onCreate(db);
//    }
//
//    public ArrayList<WorkflowConfig> getWorkflowsForUser(int userID) {
//
//        int userId =
//        ArrayList<WorkflowConfig> workflows = new ArrayList<>();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT " + TABLE_WORKFLOWS + ".* FROM "
//                + TABLE_WORKFLOWS + " INNER JOIN "
//                + TABLE_USER_WORKFLOWS + " ON "
//                + TABLE_WORKFLOWS + "." + KEY_WF_ID + " = "
//                + TABLE_USER_WORKFLOWS + "." + KEY_WF_ID
//                + " WHERE " + TABLE_USER_WORKFLOWS + "." + KEY_USER_ID + " = ?";
//
//        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
//
//        if (cursor.moveToFirst()) {
//            do {
//                Workflow workflow = new Workflow();
//                workflow.setId(cursor.getInt(cursor.getColumnIndex(KEY_WF_ID)));
//                workflow.setWorkflowName(cursor.getString(cursor.getColumnIndex(KEY_WF_NAME)));
//                workflow.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
//                workflows.add(workflow);
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        db.close();
//
//        return workflows;
//    }
//}
