package com.samsung.basicsecurity.repositories.models;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart implements Serializable {
    public List<ShoppingCartItem> items = new ArrayList<>();
    public Long TotalPrice;
    public Long TotalQty;

    public Long getTotalPrice() {
        return items.stream()
                .mapToLong(item -> item.getQty() * item.getProduct().getPrice())
                .sum();
    }

    public int getTotalQty() {
        return (int) items.stream()
                .mapToLong(ShoppingCartItem::getQty)
                .sum();
    }
}
