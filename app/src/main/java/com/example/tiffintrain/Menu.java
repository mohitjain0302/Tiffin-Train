package com.example.tiffintrain;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu {
    private String menuName ;
    private int menuRate ;
    private ArrayList<String> menuItem ;
    private String type;
    private boolean isSevenDay ;
    private boolean isFifteenDay ;
    private boolean isOneMonth ;
    private int sevenDayRate ;
    private int fifteenDayRate ;
    private int oneMonthRate ;
    public Menu(){

    }

    public Menu(String menuName , int menuRate , ArrayList<String> menuItems){
        this.menuName = menuName ;
        this.menuRate = menuRate ;
        this.menuItem = menuItems ;
    }

    public Menu(String menuName , int menuRate , ArrayList<String> menuItems,String type)
    {
        this.menuName = menuName ;
        this.menuRate = menuRate ;
        this.menuItem = menuItems ;
        this.type = type;
        this.isSevenDay = false ;
        this.isFifteenDay = false;
        this.isOneMonth = false ;
        this.sevenDayRate = 0;
        this.fifteenDayRate=0;
        this.oneMonthRate=0;
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

    public ArrayList<String> getMenuItem() {
        return menuItem;
    }


    public boolean getIsSevenDay() {
        return isSevenDay ;
    }

    public boolean getIsFifteenDay() {
        return isFifteenDay ;
    }

    public boolean getIsOneMonth() {
        return isOneMonth ;
    }

    public int getFifteenDayRate() {
        return fifteenDayRate;
    }

    public int getOneMonthRate() {
        return oneMonthRate;
    }

    public int getSevenDayRate() {
        return sevenDayRate;
    }

}
