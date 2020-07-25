package com.kmb.budget;

class CategorySum implements Comparable<CategorySum>{
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

    CategorySum(Long id,String categoryName, String balance,  String type) {
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

    String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }


    @Override
    public int compareTo(CategorySum categorySum) {
        if(Float.parseFloat(this.balance) < Float.parseFloat(categorySum.balance)){
            return -1;
        }else if(Float.parseFloat(this.balance) > Float.parseFloat(categorySum.balance)){
            return 1;
        }
        return 0;
    }
}
