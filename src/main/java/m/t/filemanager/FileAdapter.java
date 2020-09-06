package m.t.filemanager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private List<File> filesSearching;
    private List<File> files;
    private FileItemEventListener fileItemEventListener;
    private View moreIv;

    public FileAdapter(List<File> files, FileItemEventListener fileItemEventListener) {
        this.files = new ArrayList<>(files);
        this.filesSearching = this.files;
        this.fileItemEventListener = fileItemEventListener;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_files, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.bindFile(filesSearching.get(position));
    }

    @Override
    public int getItemCount() {
        return filesSearching.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        ImageView fileIcon;
        TextView fileNameTv;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTv = itemView.findViewById(R.id.tv_itemFiles_nameFolder);
            fileIcon = itemView.findViewById(R.id.iv_itemFile_icon);
            moreIv = itemView.findViewById(R.id.ivBtn_itemFiles_more);
        }

        public void bindFile(final File file) {
            if (file.isDirectory()) {
                fileIcon.setImageResource(R.drawable.ic_folder_black_32dp);
            } else {
                fileIcon.setImageResource(R.drawable.ic_file_black_32dp);
            }
            fileNameTv.setText(file.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileItemEventListener.onFileItemClick(file);
                }
            });

            moreIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_item_files, popupMenu.getMenu());
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_itemFile_delete:
                                    fileItemEventListener.onDeleteFileItemClick(file);
                                    break;
                                case R.id.menu_itemFile_copy:
                                    fileItemEventListener.onCopyFileItemClick(file);
                                    break;

                                case R.id.menu_itemFile_move:
                                    fileItemEventListener.onMoveFileItemClick(file);
                                    break;
                                default:

                            }
                            return false;
                        }
                    });
                }
            });
        }
    }

    public interface FileItemEventListener {
        void onFileItemClick(File file);

        void onDeleteFileItemClick(File file);

        void onCopyFileItemClick(File file);

        void onMoveFileItemClick(File file);
    }

    public void AddFile(File file) {
        files.add(0, file);
        notifyItemInserted(0);
    }

    public void deleteFile(File file) {
        int index = files.indexOf(file);
        if (index > -1) {
            files.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void searchQuery(String query) {
        if (query.length() > 0) {
            List<File> result = new ArrayList<>();
            for (File file : this.files) {
                if (file.getName().toLowerCase().contains(query.toLowerCase())) {
                    result.add(file);
                }
            }

            this.filesSearching = result;
            notifyDataSetChanged();
        } else {
            this.filesSearching = this.files;
            notifyDataSetChanged();
        }
    }
}
