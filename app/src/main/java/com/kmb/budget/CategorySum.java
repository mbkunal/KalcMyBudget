package com.kmb.budget;

class CategorySum {
    private String categoryName;
    private String balance;
    private Long id;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategorySum(Long id,String categoryName, String balance,  String type) {
        this.categoryName = categoryName;
        this.balance = balance;
        this.id = id;
        this.type = type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public CategorySum(Long id,String categoryName, String balance) {
        this.categoryName = categoryName;
        this.balance = balance;
        this.id = id;
    }
}
