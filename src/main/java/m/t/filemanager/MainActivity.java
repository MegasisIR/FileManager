package m.t.filemanager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.io.File;

public class MainActivity extends AppCompatActivity implements DialogAddFolder.AddNewFolderListener {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.toggleGroup);
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId == R.id.btn_main_list && isChecked) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_fragmentMain_mainFiles);
                    if (fragment instanceof FilesListFragment) {
                        ((FilesListFragment) fragment).setViewType(ViewType.ROW);
                    }
                } else if (checkedId == R.id.btn_main_grid && isChecked) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_fragmentMain_mainFiles);

                    ((FilesListFragment) fragment).setViewType(ViewType.GRID);

                }
            }
        });

        if (StorageHelper.isExternalStorageReadable()) {

            File externalFilesDir = getExternalFilesDir(null);
            assert externalFilesDir != null;
            listFragments(externalFilesDir.getPath(), false,0);
        }

        ImageView addNewFolderBtn = findViewById(R.id.iv_main_addFolder);

        addNewFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddFolder addFolder = new DialogAddFolder();
                addFolder.show(getSupportFragmentManager(), null);
            }
        });

        EditText boxSearch = findViewById(R.id.et_main_searchBox);
        boxSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_fragmentMain_mainFiles);
                if (fragment instanceof FilesListFragment) {
                    ((FilesListFragment) fragment).search(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    public void listFragments(String path, boolean addToBackStack,int viewType) {
        FilesListFragment filesListFragment = new FilesListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putInt("viewType",viewType);
        FragmentTransaction fileTransaction = getSupportFragmentManager().beginTransaction();
        filesListFragment.setArguments(bundle);
        fileTransaction.replace(R.id.frame_fragmentMain_mainFiles, filesListFragment);
        if (addToBackStack) fileTransaction.addToBackStack(null);
        fileTransaction.commit();
    }

    public void listFragments(String path,int viewType) {
        this.listFragments(path, true,viewType);
    }

    @Override
    public void addNewFolder(String folder) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_fragmentMain_mainFiles);
        if (fragment instanceof FilesListFragment) {
            ((FilesListFragment) fragment).onCreateNewFolder(folder);
        }
    }
}
