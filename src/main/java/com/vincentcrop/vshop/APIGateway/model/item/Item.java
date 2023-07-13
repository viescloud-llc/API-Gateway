package com.vincentcrop.vshop.APIGateway.model.item;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private int id;

    private String name;

    private String description;

    private String publicity;

    private double price;

    private double discountPrice;

    private String status = "NORMAL";

    private String barcode;

    private int oneStarRating;

    private int twoStarRating;

    private int threeStarRating;

    private int fourStarRating;

    private int fiveStarRating;

    private List<String> pictureURLs;

    private List<Category> categories = new ArrayList<>();
}
