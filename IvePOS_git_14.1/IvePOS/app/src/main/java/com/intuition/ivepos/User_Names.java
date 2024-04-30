package com.intuition.ivepos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User_Names implements Parcelable {

    private String username;
    private String password;

    public User_Names(String username) {
        this.username = username;
       // this.password = password;
    }

    protected User_Names(Parcel in) {
        username = in.readString();
        password = in.readString();
    }

    public static final Creator<User_Names> CREATOR = new Creator<User_Names>() {
        @Override
        public User_Names createFromParcel(Parcel in) {
            return new User_Names(in);
        }

        @Override
        public User_Names[] newArray(int size) {
            return new User_Names[size];
        }
    };

    public String getUsername() {
        return username;
    }

//    public String getPassword() {
//        return password;
//    }

    @Override
    public String toString() {
        return username; // This is what will be displayed in the Spinner
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(password);
    }
}





