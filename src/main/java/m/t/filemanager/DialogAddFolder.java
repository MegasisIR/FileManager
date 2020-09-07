package m.t.filemanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class DialogAddFolder extends DialogFragment {
    private AddNewFolderListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (AddNewFolderListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_folder, null, false);
        builder.setView(view);

        final TextInputLayout error = view.findViewById(R.id.etr_dialog_newFolder);
        final TextInputEditText editText = view.findViewById(R.id.et_dialog_newFolder);
        MaterialButton btn = view.findViewById(R.id.btn_dialog_addNewFolder);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()) {
                    error.setError(getString(R.string.is_not_empty));
                } else {
                    listener.addNewFolder(editText.getText().toString());
                    dismiss();
                }
            }
        });
        return builder.create();
    }

    public interface AddNewFolderListener {
        void addNewFolder(String folder);
    }
}
