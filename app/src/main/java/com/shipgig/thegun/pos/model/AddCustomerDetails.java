package com.shipgig.thegun.pos.model;


public class AddCustomerDetails {
    public  String invoice_ID;
    public String store_ID;
    public String transaction_ID;

    public String getInvoice_ID() {
        return invoice_ID;
    }

    public void setInvoice_ID(String invoice_ID) {
        this.invoice_ID = invoice_ID;
    }

    public String getStore_ID() {
        return store_ID;
    }

    public void setStore_ID(String store_ID) {
        this.store_ID = store_ID;
    }

    public String getTransaction_ID() {
        return transaction_ID;
    }

    public void setTransaction_ID(String transaction_ID) {
        this.transaction_ID = transaction_ID;
    }
}