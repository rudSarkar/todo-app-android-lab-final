package xyz.rudra0x01.todoapp.AddTodoDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import xyz.rudra0x01.todoapp.R;
import xyz.rudra0x01.todoapp.database.databaseConnect;

public class AddTodoDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // inflate the layout
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_add_todo_dialog, null);

        builder.setView(view);

        // add the buttons
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // get the todo name from the edit text
                EditText todoNameEditText = view.findViewById(R.id.edit_todo_name);
                String todoName = todoNameEditText.getText().toString();

                try {
                    // insert the todo into the database
                    databaseConnect databaseConnect = new databaseConnect(getActivity());
                    databaseConnect.insertTodoItem(todoName);
                    Toast.makeText(getContext(), "Todo added", Toast.LENGTH_LONG).show();
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

        // create and return the dialog
        return builder.create();
    }
}
