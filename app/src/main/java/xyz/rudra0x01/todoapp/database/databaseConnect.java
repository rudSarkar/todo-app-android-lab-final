package xyz.rudra0x01.todoapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class databaseConnect extends SQLiteOpenHelper {

    // database version, name, and table names
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todo_list_table";
    private static final String TABLE_USERS = "users_table";
    private static final String TABLE_TODO_LIST = "todo_list_table";

    // column names for users table
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    private static final String COLUMN_TODO_ID = "id";
    private static final String COLUMN_TODO_NAME = "todo_name";


    // create table SQL queries
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FULL_NAME + " TEXT,"
                    + COLUMN_EMAIL + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT"
                    + ")";
    private static final String CREATE_TABLE_TODO_LIST =
            "CREATE TABLE " + TABLE_TODO_LIST + "("
                    + COLUMN_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TODO_NAME + " TEXT"
                    + ")";

    // constructor
    public databaseConnect(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_TODO_LIST);
    }

    // upgrading tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_LIST);
        // create tables again
        onCreate(db);
    }

    // insert User
    public void insertUser(String full_name, String email, String password) {
        // get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // create content values to insert
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, full_name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        // insert row
        db.insert(TABLE_USERS, null, values);
        // close db connection
        db.close();
    }

    // check user info
    public boolean checkLogin(String email, String password) {
        // get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // define a projection that specifies which columns from the table you will use
        String[] projection = {
                COLUMN_ID,
                COLUMN_EMAIL,
                COLUMN_PASSWORD
        };

        // define a selection that specifies which rows from the table you want
        String selection = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";

        // define selection arguments
        String[] selectionArgs = {email, password};

        // create cursor to execute the query
        Cursor cursor = db.query(
                TABLE_USERS,   // table name
                projection,    // columns to return
                selection,     // columns to filter by row groups
                selectionArgs, // values to filter by row groups
                null,          // don't group the rows
                null,          // don't filter by row groups
                null           // don't sort
        );
        if (cursor.moveToFirst()) {
            // user exists, return true
            return true;
        } else {
            // user does not exist, return false
            return false;
        }
    }

    // insert data into todo list table
    public void insertTodoItem(String todoItem) {
        // get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // create content values to insert
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO_NAME, todoItem);

        // insert row
        db.insert(TABLE_TODO_LIST, null, values);

        // close db connection
        db.close();
    }

    // list todo
    public List<String> getTodoList() {
        List<String> todoList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        // query the database
        String query = "SELECT * FROM todo_list_table";
        Cursor cursor = db.rawQuery(query, null);

        // iterate through the cursor and add the todos to the list
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String todoName = cursor.getString(cursor.getColumnIndex("todo_name"));
                todoList.add(todoName);
            } while (cursor.moveToNext());
        }

        // close the cursor
        cursor.close();

        // return the list
        return todoList;
    }

    // update todo item
    public void updateTodoItem(int id, String todoName) {
        // get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // create content values to update
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO_NAME, todoName);

        // update row
        db.update(TABLE_TODO_LIST, values, COLUMN_TODO_ID + " = ?", new String[]{String.valueOf(id)});

        // close db connection
        db.close();
    }

    // delete todo item
    public void deleteTodoItem(String todoName) {
        // get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // delete row
        db.delete(TABLE_TODO_LIST, COLUMN_TODO_NAME + " = ?", new String[]{String.valueOf(todoName)});

        // close db connection
        db.close();
    }

}
