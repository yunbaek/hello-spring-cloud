package com.hahoho87.catalogservice.service;

import com.hahoho87.catalogservice.entity.CatalogEntity;
import com.hahoho87.catalogservice.exception.CatalogNotFoundException;
import com.hahoho87.catalogservice.repository.CatalogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;
    private final ModelMapper mapper;

    public CatalogServiceImpl(CatalogRepository catalogRepository, ModelMapper modelMapper) {
        this.catalogRepository = catalogRepository;
        this.mapper = modelMapper;
    }

    @Override
    public List<CatalogEntity> findAllCatalogs() {
        return catalogRepository.findAll();
    }

    @Override
    public CatalogEntity findCatalogByProductId(String productId) {
        return catalogRepository.findByProductId(productId)
                .orElseThrow(() -> new CatalogNotFoundException("Can not find Catalog by productId : " + productId));
    }
}
