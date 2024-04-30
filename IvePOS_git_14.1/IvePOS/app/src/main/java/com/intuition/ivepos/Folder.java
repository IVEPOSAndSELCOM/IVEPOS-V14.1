package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 3/9/2018.
 */

public class Folder {
    int _id;
    String filename;
    String folderame;

    public Folder(int _id, String filename, String folderame) {
        this._id = _id;
        this.filename = filename;
        this.folderame = folderame;
    }


    public Folder(){

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFolderame() {
        return folderame;
    }

    public void setFolderame(String folderame) {
        this.folderame = folderame;
    }
}
