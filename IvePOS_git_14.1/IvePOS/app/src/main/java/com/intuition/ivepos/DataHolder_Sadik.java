package com.intuition.ivepos;

public class DataHolder_Sadik {
    private static String selectedUser;

    public static void setSelectedUser(String user) {
        selectedUser = user;
    }

    public static String getSelectedUser() {
        return selectedUser;
    }
}