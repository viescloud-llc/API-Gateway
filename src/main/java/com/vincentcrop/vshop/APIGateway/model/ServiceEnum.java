package com.vincentcrop.vshop.APIGateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ServiceEnum {
    AUTHENTICATOR_SERVICE(ServiceEnumConstant.AUTHENTICATOR_SERVICE, "authenticator"),
    ITEM_SERVICE(ServiceEnumConstant.ITEM_SERVICE, "item"),
    VGAME_SERVICE(ServiceEnumConstant.VGAME_SERVICE, "vgame"),
    FILE_MANAGER_SERVICE(ServiceEnumConstant.FILE_MANAGER_SERVICE, "file"),
    SMB_FILE_MANAGER_SERVICE(ServiceEnumConstant.SMB_FILE_MANAGER_SERVICE, "smb"),
    OBJECT_STORAGE_MANAGER_SERVICE(ServiceEnumConstant.OBJECT_STORAGE_MANAGER_SERVICE, "osm"),
    SATURDAY_SERVICE(ServiceEnumConstant.SATURDAY_SERVICE, "saturday"),
    VENKINS_SERVICE(ServiceEnumConstant.VENKINS_SERVICE, "venkins"),
    RAPHAEL_SERVICE(ServiceEnumConstant.RAPHAEL_SERVICE, "raphael"),
    AFFILIATE_MARKETING_SERVICE(ServiceEnumConstant.AFFILIATE_MARKETING_SERVICE, "affiliate_marketing");

    private final String name;
    private final String prefix;

    public class ServiceEnumConstant {
        public static final String AUTHENTICATOR_SERVICE = "AUTHENTICATOR-SERVICE";
        public static final String ITEM_SERVICE = "ITEM-SERVICE";
        public static final String VGAME_SERVICE = "VGAME-SERVICE";
        public static final String FILE_MANAGER_SERVICE = "FILE-MANAGER-SERVICE";
        public static final String SMB_FILE_MANAGER_SERVICE = "SMB-FILE-MANAGER-SERVICE";
        public static final String OBJECT_STORAGE_MANAGER_SERVICE = "OBJECT-STORAGE-MANAGER-SERVICE";
        public static final String SATURDAY_SERVICE = "SATURDAY-SERVICE";
        public static final String VENKINS_SERVICE = "VENKINS-SERVICE";
        public static final String RAPHAEL_SERVICE = "RAPHAEL-SERVICE";
        public static final String AFFILIATE_MARKETING_SERVICE = "AFFILIATE-MARKETING-SERVICE";
    }
}
