package m.t.filemanager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class FilesListFragment extends Fragment implements FileAdapter.OnClickFileItemListener {
    private static final String TAG = "ListFilesFragment";
    private RecyclerView recyclerView;
    private FileAdapter adapter;
    private String path;
    private GridLayoutManager gridLayoutManager;
    private ViewType viewType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments().getString("path");
        this.viewType = getArguments().getInt("viewType") == 0 ? ViewType.ROW : ViewType.GRID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_files, container, false);
        recyclerView = view.findViewById(R.id.rv_listFiles_files);
        gridLayoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        ImageView iconBackIv = view.findViewById(R.id.iv_listFiles_back);
        TextView addressCurrentFolder = view.findViewById(R.id.tv_listFiles_namePath);
        File currentFolder = new File(path);

        if (StorageHelper.isExternalStorageReadable()) {
            File[] files = currentFolder.listFiles();
            adapter = new FileAdapter(Arrays.asList(files), this);
            recyclerView.setAdapter(adapter);

        }

        addressCurrentFolder.setText(path.subSequence(path.lastIndexOf("/") + 1, path.length()));

        iconBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }


    @Override
    public void onFileItemClick(File file) {
        if (file.isDirectory()) {
            if (viewType == ViewType.ROW) {
                ((MainActivity) getActivity()).listFragments(file.getPath(), 0);

            } else if (ViewType.GRID == viewType) {
                ((MainActivity) getActivity()).listFragments(file.getPath(), 1);
            }
            Log.i(TAG, "onClick: " + file.getPath());
        }
    }

    @Override
    public void onDeleteFileItemClick(File file) {
        if (StorageHelper.isExternalStorageWriteable()) {
            if (file.delete()) {
                adapter.deleteFile(file);
            }
        }
    }

    @Override
    public void onCopyFileItemClick(File file) {
        if (StorageHelper.isExternalStorageWriteable()) {
            try {
                Log.i(TAG, "onCopyFileItemClick: " + getDestinationPath(file.getName()));
                copy(file, getDestinationPath(file.getName()));
                Toast.makeText(getContext(), file.getName() + getString(R.string.file_is_copied), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMoveFileItemClick(File file) {
        if (StorageHelper.isExternalStorageWriteable()) {
            try {
                copy(file, getDestinationPath(file.getName()));
                onDeleteFileItemClick(file);
                Toast.makeText(getContext(), file.getName() + getString(R.string.file_is_moved), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onCreateNewFolder(String nameFolder) {
        if (StorageHelper.isExternalStorageWriteable()) {
            File newFolder = new File(path + File.separator + nameFolder);
            if (!newFolder.exists()) {
                if (newFolder.mkdir()) {
                    adapter.addFolder(newFolder);
                    recyclerView.smoothScrollToPosition(0);
                }
            }
        }
    }

    private void copy(File source, File destination) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(source);
        FileOutputStream fileOutputStream = new FileOutputStream(destination);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = fileInputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, length);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }

    private File getDestinationPath(String fileName) {
        return new File(getContext().getExternalFilesDir(null).getPath() + File.separator + "Destination" + File.separator + fileName);
    }

    public void search(String query) {
        if (adapter != null)
            adapter.search(query);
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
        if (adapter != null) {
            adapter.setViewType(viewType);
            if (viewType == ViewType.GRID) {
                gridLayoutManager.setSpanCount(2);
            } else if (viewType == ViewType.ROW) {
                gridLayoutManager.setSpanCount(1);
            }
        }
    }

}
