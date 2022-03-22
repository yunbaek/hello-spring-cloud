package com.hahoho87.catalogservice.service;

import com.hahoho87.catalogservice.entity.CatalogEntity;

import java.util.List;

public interface CatalogService {
    List<CatalogEntity> findAllCatalogs();
    CatalogEntity findCatalogByProductId(String productId);
}
