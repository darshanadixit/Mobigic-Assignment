package in.darshana.mobigicassignment;

import java.io.Serializable;

public class FileModel implements Serializable {
    String mFileName;

    public FileModel(String mFileName) {
        this.mFileName = mFileName;
    }

    public String getmFileName() {
        return mFileName;
    }

    public void setmFileName(String mFileName) {
        this.mFileName = mFileName;
    }
}
