package com.video.rental.challenge.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Data
@Table(name = "products")
public class Products extends BaseEntity {

    @Column(nullable = false, name = "video_title")
    private String videoTitle;
    @Column(nullable = false, name = "rental_days")
    private int numberOfRentalDays;
    @Column(nullable = false, name = "total_amount")
    private BigDecimal totalAmount;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "user_product_fk"))
    private User user;

}
