package com.samsung.basicsecurity.repositories;

import com.samsung.basicsecurity.repositories.models.Catalogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<Catalogs, Long> {
}
