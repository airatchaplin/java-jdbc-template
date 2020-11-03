package com.epam.izh.rd.online.autcion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Лот в ставке
 */
@Data
public class Item {
    private Long itemId;
    private Double bidIncrement;
    private Boolean buyItNow;
    private String description;
    private LocalDate startDate;
    private Double startPrice;
    private LocalDate stopDate;
    private String title;
    private Long userId;

    public Item(Long itemId, Double bidIncrement, Boolean buyItNow, String description, LocalDate startDate, Double startPrice, LocalDate stopDate, String title, Long userId) {
        this.itemId = itemId;
        this.bidIncrement = bidIncrement;
        this.buyItNow = buyItNow;
        this.description = description;
        this.startDate = startDate;
        this.startPrice = startPrice;
        this.stopDate = stopDate;
        this.title = title;
        this.userId = userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public Double getBidIncrement() {
        return bidIncrement;
    }

    public Boolean getBuyItNow() {
        return buyItNow;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Double getStartPrice() {
        return startPrice;
    }

    public LocalDate getStopDate() {
        return stopDate;
    }

    public String getTitle() {
        return title;
    }

    public Long getUserId() {
        return userId;
    }
}
