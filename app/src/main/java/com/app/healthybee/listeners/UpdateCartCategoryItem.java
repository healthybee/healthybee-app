package com.app.healthybee.listeners;

import com.app.healthybee.models.CategoryItem;

public interface UpdateCartCategoryItem {
    void OnAddItemToCart(CategoryItem categoryItem, int count, int card_plus_minus,int position);

}
