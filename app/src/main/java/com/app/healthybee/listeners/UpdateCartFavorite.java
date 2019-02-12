package com.app.healthybee.listeners;

import com.app.healthybee.models.FavouriteModel;

public interface UpdateCartFavorite {
    void OnAddItemToCart(FavouriteModel favouriteModel, int count, int card_plus_minus, int position);
}
