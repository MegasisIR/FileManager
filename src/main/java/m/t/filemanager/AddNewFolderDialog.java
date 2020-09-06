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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewFolderDialog extends DialogFragment {
    private AddNewFolderCallback addNewFolderCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addNewFolderCallback= (AddNewFolderCallback) context;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_folder, null, false);
        final TextInputEditText nameNewFolderEt = view.findViewById(R.id.et_dialog);
        final TextInputLayout textInputLayout = view.findViewById(R.id.etl_dialog);
        view.findViewById(R.id.btn_dialog_addNewFolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameNewFolderEt.length() > 0) {
                    addNewFolderCallback.onCreateFolderButtonClick(nameNewFolderEt.getText().toString());
                    dismiss();
                }else {
                    textInputLayout.setError("not empty");
                }
            }
        });
        return builder.setView(view).create();
    }

    public interface AddNewFolderCallback {
        void onCreateFolderButtonClick(String folderName);
    }
}
