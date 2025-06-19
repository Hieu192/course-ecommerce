package com.Hieu2k3.course.services;

import com.Hieu2k3.course.dtos.requests.favorite.FavoriteRequest;
import com.Hieu2k3.course.dtos.responses.PageResponse;
import com.Hieu2k3.course.dtos.responses.favorite.FavoriteResponse;
import com.Hieu2k3.course.entity.Favorite;

public interface FavoriteService {
    void createFavorite (FavoriteRequest request);

    Favorite findById(Integer id);

    PageResponse<FavoriteResponse> findAllByUserCurrent(int page, int size);

    void deleteFavorite(Integer favoriteId);
}
