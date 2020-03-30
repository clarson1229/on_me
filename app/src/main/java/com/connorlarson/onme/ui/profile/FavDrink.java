package com.connorlarson.onme.ui.profile;

public class FavDrink {
    private String fDrinkId;
    private String fDrinkName;
    private String fDrinkDescription;

    public FavDrink(String fDrinkId, String fDrinkName, String fDrinkDescription) {
        this.fDrinkId = fDrinkId;
        this.fDrinkName = fDrinkName;
        this.fDrinkDescription = fDrinkDescription;
    }

    public String getfDrinkId() {
        return fDrinkId;
    }

    public void setfDrinkId(String fDrinkId) {
        this.fDrinkId = fDrinkId;
    }

    public String getfDrinkName() {
        return fDrinkName;
    }

    public void setfDrinkName(String fDrinkName) {
        this.fDrinkName = fDrinkName;
    }

    public String getfDrinkDescription() {
        return fDrinkDescription;
    }

    public void setfDrinkDescription(String fDrinkDescription) {
        this.fDrinkDescription = fDrinkDescription;
    }

}
