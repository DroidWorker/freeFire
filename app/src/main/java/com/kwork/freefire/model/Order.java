package com.kwork.freefire.model;

import com.google.firebase.database.DatabaseReference;

public class Order {
    public int id;
    public String userID;
    public String accountID;
    private String status;
    private Integer statusInt;
    public String summ;

    public String getStatus() {
        return status;
    }

    public boolean setStatus(int status) {
        switch (status){
            case 0:
                this.status = "статус";
                this.statusInt = 0;
                break;
            case 1:
                this.status = "обрабатывается";
                this.statusInt = 1;
                break;
            case 2:
                this.status = "выплата";
                this.statusInt = 2;
                break;
            case 3:
                this.status = "оплачено";
                this.statusInt = 3;
                break;
            case 4:
                this.status = "отменено";
                this.statusInt = 4;
                break;
            default:
                return false;
        }
        return true;
    }

    public void publish(DatabaseReference ref){
        ref.child(String.valueOf(id)).child("userID").setValue(userID);
        ref.child(String.valueOf(id)).child("accountID").setValue(accountID);
        ref.child(String.valueOf(id)).child("status").setValue(statusInt);
        ref.child(String.valueOf(id)).child("summ").setValue(summ);
    }
}
