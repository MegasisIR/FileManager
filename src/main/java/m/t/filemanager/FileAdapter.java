package m.t.filemanager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private static final String TAG = "FileAdapter";
    private List<File> filteredFiles;
    private List<File> listFiles;
    private OnClickFileItemListener listener;
    private ViewType viewType = ViewType.ROW;

    public FileAdapter(List<File> listFiles, OnClickFileItemListener listener) {
        this.listFiles = new ArrayList<>(listFiles);
        this.listener = listener;
        this.filteredFiles = listFiles;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new FileViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(viewType == ViewType.ROW.getValue() ? R.layout.item_file : R.layout.item_file_grid, parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.bindFile(filteredFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredFiles.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType.getValue();
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconItemIv;
        private TextView nameItemTv;
        private ImageView iconMoreIv;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            iconItemIv = itemView.findViewById(R.id.iv_itemFile_iconFile);
            nameItemTv = itemView.findViewById(R.id.tv_itemFile_name);
            iconMoreIv = itemView.findViewById(R.id.iv_itemFile_more);
        }

        void bindFile(final File file) {
            nameItemTv.setText(file.getName());

            if (file.isDirectory()) {
                iconItemIv.setImageResource(R.drawable.ic_folder_black_32dp);
            } else {
                iconItemIv.setImageResource(R.drawable.ic_file_black_32dp);
            }
            iconMoreIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), iconMoreIv);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_item_files, popupMenu.getMenu());
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.menu_itemFile_delete:
                                    listener.onDeleteFileItemClick(file);
                                    break;
                                case R.id.menu_itemFile_copy:
                                    listener.onCopyFileItemClick(file);
                                    break;
                                case R.id.menu_itemFile_move:
                                    listener.onMoveFileItemClick(file);
                                    break;
                            }
                            return false;
                        }
                    });
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFileItemClick(file);
                }
            });
        }

    }

    public interface OnClickFileItemListener {
        void onFileItemClick(File file);

        void onDeleteFileItemClick(File file);

        void onCopyFileItemClick(File file);

        void onMoveFileItemClick(File file);
    }

    //add folder
    public void addFolder(File newFolder) {
        listFiles.add(0, newFolder);
        notifyItemInserted(0);
    }

    //delete file
    public void deleteFile(File file) {
        int index = listFiles.indexOf(file);
        listFiles.remove(index);
        notifyItemRemoved(index);
    }

    //search
    public void search(String query) {
        if (query.length() > 0) {
            List<File> result = new ArrayList<>();
            for (File file :
                    this.listFiles) {
                if (file.getName().toLowerCase().contains(query.toLowerCase())) {
                    result.add(file);
                }
            }

            this.filteredFiles = result;
            notifyDataSetChanged();
        } else {
            this.filteredFiles = this.listFiles;
            notifyDataSetChanged();
        }

    }
}
