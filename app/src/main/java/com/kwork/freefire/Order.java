package com.kwork.freefire;

public class Order {
    public int id;
    public String accountID;
    String status;
    public String summ;

    public String getStatus() {
        return status;
    }

    public boolean setStatus(int status) {
        switch (status){
            case 1:
                this.status = "обрабатывается";
                break;
            case 2:
                this.status = "выплата";
                break;
            case 3:
                this.status = "оплачено";
                break;
            case 4:
                this.status = "отменено";
                break;
            default:
                return false;
        }
        return true;
    }
}
