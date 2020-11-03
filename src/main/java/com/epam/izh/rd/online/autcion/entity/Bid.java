package com.epam.izh.rd.online.autcion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Ставка
 */
@Data
public class Bid {
    private Long bidId;
    private LocalDate bidDate;
    private Double bidValue;
    private Long itemId;
    private Long userId;

    public Bid(Long bidId, LocalDate bidDate, Double bidValue, Long itemId, Long userId) {
        this.bidId = bidId;
        this.bidDate = bidDate;
        this.bidValue = bidValue;
        this.itemId = itemId;
        this.userId = userId;
    }

    public Long getBidId() {
        return bidId;
    }

    public LocalDate getBidDate() {
        return bidDate;
    }

    public Double getBidValue() {
        return bidValue;
    }

    public Long getItemId() {
        return itemId;
    }

    public Long getUserId() {
        return userId;
    }
}
