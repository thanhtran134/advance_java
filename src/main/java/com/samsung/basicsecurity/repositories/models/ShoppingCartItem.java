package com.samsung.basicsecurity.repositories.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItem implements Serializable {
    public Product product;
    public int Qty;
}
