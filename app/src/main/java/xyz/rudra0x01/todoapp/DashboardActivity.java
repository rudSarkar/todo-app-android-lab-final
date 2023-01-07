package xyz.rudra0x01.todoapp;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import xyz.rudra0x01.todoapp.database.databaseConnect;
import xyz.rudra0x01.todoapp.extensions.SwipeDismissListViewTouchListener;
import xyz.rudra0x01.todoapp.session.LoginPreferences;
import xyz.rudra0x01.todoapp.AddTodoDialog.*;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    ListView todoListView;
    BottomNavigationView bottom_navigation;

    // TODO: make user info available to so I can logout the user
    public static final String USERNAME_KEY = "username";
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        todoListView = findViewById(R.id.todoList);
        bottom_navigation = findViewById(R.id.bottom_navigation);

        // TODO: get the username/email intent data
        mUsername = getIntent().getStringExtra(USERNAME_KEY);

        // get the todo list from the database
        databaseConnect databaseConnect = new databaseConnect(this);
        List<String> todoList = databaseConnect.getTodoList();

// create an array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoList);


        // set the adapter as the data source for the list view
        todoListView.setAdapter(adapter);

        // create the touch listener
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        todoListView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                // delete the todo
                                int position = 0;
                                String todoName = (String) listView.getAdapter().getItem(position);

                                try {
                                    databaseConnect.deleteTodoItem(todoName);
                                    Toast.makeText(getApplicationContext(), "Todo Deleted", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // refresh the todo list
                                List<String> todoList = databaseConnect.getTodoList();
                                ((ArrayAdapter) todoListView.getAdapter()).clear();
                                ((ArrayAdapter) todoListView.getAdapter()).addAll(todoList);
                                ((ArrayAdapter) todoListView.getAdapter()).notifyDataSetChanged();
                            }
                        });

// set the touch listener
        todoListView.setOnTouchListener(touchListener);

        LoginPreferences loginPreferences = new LoginPreferences(getApplicationContext());

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_add:
                        AddTodoDialogFragment dialog = new AddTodoDialogFragment();
                        dialog.show(getSupportFragmentManager(), "add_todo_dialog");
                        return true;
                    case R.id.menu_logout:
                        loginPreferences.logout(DashboardActivity.this);
                        // return to the login activity
                        startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });

    }
}