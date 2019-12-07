package com.kmb.budget;

class CategorySum {
    private String categoryName;
    private String balance;

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

    public CategorySum(String categoryName, String balance) {
        this.categoryName = categoryName;
        this.balance = balance;
    }
}
