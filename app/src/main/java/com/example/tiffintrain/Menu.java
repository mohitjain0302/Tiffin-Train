package com.example.tiffintrain;

import java.util.ArrayList;

public class Menu {
    private String menuName ;
    private int menuRate ;
    private ArrayList<String> menuItems ;
    private String type;

    public Menu(){

    }

    public Menu(String menuName , int menuRate , ArrayList<String> menuItems){
        this.menuName = menuName ;
        this.menuRate = menuRate ;
        this.menuItems = menuItems ;
    }

    public Menu(String menuName , int menuRate , ArrayList<String> menuItems,String type)
    {
        this.menuName = menuName ;
        this.menuRate = menuRate ;
        this.menuItems = menuItems ;
        this.type = type;
    }
    public String getMenuName() {
        return menuName;
    }

    public int getMenuRate() {
        return menuRate;
    }

    public String getType(){
        return type;
    }

    public ArrayList<String> getMenuItems() {
        return menuItems;
    }
}
