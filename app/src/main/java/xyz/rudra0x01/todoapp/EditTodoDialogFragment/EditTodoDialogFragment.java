package xyz.rudra0x01.todoapp.EditTodoDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.DialogFragment;
import xyz.rudra0x01.todoapp.R;
import xyz.rudra0x01.todoapp.database.databaseConnect;

import java.util.List;

public class EditTodoDialogFragment extends DialogFragment {
    private static final String ARG_TODO_NAME = "todo_name";
    private ListView mTodoListView;

    public static EditTodoDialogFragment newInstance(String todoName, ListView todoListView) {
        EditTodoDialogFragment fragment = new EditTodoDialogFragment();
        fragment.mTodoListView = todoListView;
        Bundle args = new Bundle();
        args.putString(ARG_TODO_NAME, todoName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // add a positive button to save the edited todo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // inflate the layout
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_edit_todo_dialog, null);

        // get the todo name from the arguments
        String todoName = getArguments().getString(ARG_TODO_NAME);

        // set the todo name in the edit text
        EditText todoNameEditText = view.findViewById(R.id.edit_todo_name);
        todoNameEditText.setText(todoName);

        builder.setView(view);
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // get the edited todo name from the edit text
                EditText todoNameEditText = view.findViewById(R.id.edit_todo_name);
                String editedTodoName = todoNameEditText.getText().toString();

                try {
                    // update the todo in the database
                    databaseConnect databaseConnect = new databaseConnect(getActivity());
                    databaseConnect.updateTodoItem(editedTodoName, todoName.toString());
                    Toast.makeText(getContext(), "Todo updated", Toast.LENGTH_LONG).show();

                    // refresh the todo list
                    List<String> todoList = databaseConnect.getTodoList();
                    ((ArrayAdapter) mTodoListView.getAdapter()).clear();
                    ((ArrayAdapter) mTodoListView.getAdapter()).addAll(todoList);
                    ((ArrayAdapter) mTodoListView.getAdapter()).notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // dismiss the dialog
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}