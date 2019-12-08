package com.kmb.budget;

class CategorySum {
    private String categoryName;
    private String balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

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
