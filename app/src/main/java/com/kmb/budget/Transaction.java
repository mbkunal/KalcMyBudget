package com.kmb.budget;



class Transaction {
    private String sr;
    private String from;
    private String to;
    private String comment;
    private String date;
    private String amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Transaction(Long id,String sr, String from, String to, String comment, String date, String amount) {
        this.id = id;
        this.sr = sr;
        this.from = from;
        this.to = to;
        this.comment = comment;
        this.date = date;
        this.amount = amount;
    }
}