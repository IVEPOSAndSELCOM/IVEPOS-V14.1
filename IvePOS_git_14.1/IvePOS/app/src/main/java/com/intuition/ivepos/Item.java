package com.intuition.ivepos;

public class Item {
    public String name,id,phNo,phDisplayName,phType;
    public boolean isChecked;
    private String myText;
    private String childText;

    public String getMyText() {
        return myText;
    }

    public void setMyText(String myText) {
        this.myText = myText;
    }

    public String getChildText() {
        return childText;
    }

    public void setChildText(String childText) {
        this.childText = childText;
    }
}

//    public String name = "", id, father_name, child_name;
//    protected boolean isChecked;
//
//    private ArrayList<Item> countryList = new ArrayList<Item>();
//
//    public Item(String name, String father_name, String child_name, boolean isChecked, ArrayList<Item> countryList) {
//        super();
//        this.name = name;
//        this.father_name = father_name;
//        this.child_name = child_name;
//        this.isChecked = isChecked;
//        this.countryList = countryList;
//    }
//
//    public ArrayList<Item> getCountryList() {
//        return countryList;
//    }
//
//    public void setCountryList(ArrayList<Item> countryList) {
//        this.countryList = countryList;
//    }
//
//    ;
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getFather_name() {
//        return father_name;
//    }
//
//    public void setFather_name(String father_name) {
//        this.father_name = father_name;
//    }
//
//    public String getChild_name() {
//        return child_name;
//    }
//
//    public void setChild_name(String child_name) {
//        this.child_name = child_name;
//    }
//
//
//    public boolean isState() {
//        return isChecked;
//    }
//
//    public void setState(boolean isChecked) {
//        this.isChecked = isChecked;
//    }
//
////
////    public Item(String name) {
////        this.name = name;
////
////    }
////
////    public String getName() {
////        return name;
////    }
////
////    public void setName(String name) {
////        this.name = name;
////    }
////



