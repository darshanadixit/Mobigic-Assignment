package in.darshana.mobigicassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import in.darshana.mobigicassignment.Model.FileListResultItem;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {
    private List<FileListResultItem> mFileListResultItems;
    private Context mContext;

    public FileListAdapter(List<FileListResultItem> mFileListResultItems, Context mContext) {
        this.mFileListResultItems = mFileListResultItems;
        this.mContext = mContext;
    }

    @NonNull
    @NotNull
    @Override
    public FileListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       View mView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_file_list, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FileListAdapter.ViewHolder holder, int position) {
        FileListResultItem fileListResultItem = mFileListResultItems.get(position);
       // holder.mFileName.setText(fileListResultItem.);

        for(int i = 0; i < fileListResultItem.getFileName().size(); i++){
            holder.mFileName.setText(fileListResultItem.getFileName().get(i));
        }

    }

    @Override
    public int getItemCount() {
        return mFileListResultItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mFileName;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mFileName = itemView.findViewById(R.id.txtviewFileName);
        }
    }
}
