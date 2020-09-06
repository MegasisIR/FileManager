package m.t.filemanager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;

public class MainActivity extends AppCompatActivity implements AddNewFolderDialog.AddNewFolderCallback {
    private EditText textSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (StorageHelper.isExternalStorageReadable()) {
            File externalFilesDir = getExternalFilesDir(null);
            listFiles(externalFilesDir.getPath(), false);
        }


        findViewById(R.id.iv_btn_main_addNewFolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddNewFolderDialog().show(getSupportFragmentManager(), null);
            }
        });
        textSearch = findViewById(R.id.et_main_search);
        textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
                if (fragment instanceof FilesListFragment) {
                    ((FilesListFragment) fragment).search(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void listFiles(String path, boolean addToBackStack) {
        FilesListFragment filesListFragment = new FilesListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        filesListFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_fragmentContainer, filesListFragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void listFiles(String path) {
        this.listFiles(path, true);
    }

    @Override
    public void onCreateFolderButtonClick(String folderName) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
        if (fragment != null && fragment instanceof FilesListFragment) {
            ((FilesListFragment) fragment).createNewFolder(folderName);
        }
    }
}
