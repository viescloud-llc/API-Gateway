package com.vincentcrop.vshop.APIGateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ServicePrefixEnum {
    AUTHENTICATOR_SERVICE("authenticator"),
    ITEM_SERVICE("item"),
    VGAME_SERVICE("vgame"),
    FILE_MANAGER_SERVICE("file"),
    SMB_FILE_MANAGER_SERVICE("smb"),
    SATURDAY_SERVICE("saturday"),
    VENKINS_SERVICE("venkins"),
    RAPHAEL_SERVICE("raphael"),
    AFFILIATE_MARKETING_SERVICE("affiliate_marketing");

    private final String prefix;
}
