package com.hahoho87.catalogservice.controller;

import com.hahoho87.catalogservice.entity.CatalogEntity;
import com.hahoho87.catalogservice.service.CatalogService;
import com.hahoho87.catalogservice.vo.CatalogResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog-service/catalogs")
public class CatalogController {

    private final ModelMapper mapper;
    private final CatalogService catalogService;

    public CatalogController(ModelMapper mapper, CatalogService catalogService) {
        this.mapper = mapper;
        this.catalogService = catalogService;
    }

    @GetMapping("/health-check")

    public String healthCheck(HttpServletRequest request) {
        return String.format("It's Working In Catalog Service on Port %s", request.getServerPort());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<CatalogResponse> getCatalogByProductId(@PathVariable String productId) {
        CatalogResponse result = mapper.map(catalogService.findCatalogByProductId(productId), CatalogResponse.class);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<CatalogResponse>> getAllCatalogs() {
        List<CatalogEntity> allCatalogs = catalogService.findAllCatalogs();
        List<CatalogResponse> result = allCatalogs.stream().map(c -> mapper.map(c, CatalogResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
