package com.icommerce.shoppingapi.util;

import com.icommerce.shoppingapi.repository.model.OrderStatus;

import java.time.LocalDateTime;

public class Constant {

    public static final Long DEFAULT_ID= 1L;

    public static final String DEFAULT_NAME = "AAAAAAAAAA";

    public static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";

    public static final Double DEFAULT_PRICE = 1D;

    public static final String DEFAULT_BRAND = "AAAAAAAAAA";

    public static final String DEFAULT_COLOUR = "AAAAAAAAAA";

    public static final String DEFAULT_PICTURE_URL = "SAMPLE PICTURE URL";

    public static final Long DEFAULT_CATEGORY_ID = 1L;

    public static final Long DEFAULT_STOCK_QUANTITY = 1L;

    public static final LocalDateTime DEFAULT_CREATED_AT = LocalDateTime.of(2021, 10,13,0,0,22);

    public static final LocalDateTime DEFAULT_LAST_MODIFIED_AT = LocalDateTime.of(2021, 10,13,0,0,40);;

    public static final String DEFAULT_ORDER_STATUS = OrderStatus.INITIALIZE.name();

    public static final String DEFAULT_USER_PHONE = "PHONEEEEE";

    public static final String DEFAULT_USER_ADDRESS = "ADDRESS SAMPLEEE";

    public static final Integer DEFAULT_QUANTITY = 10;

    public static final String NAME_SEARCH_LIKE = "AAAA";

    public static final String DEFAULT_CATEGORY_NAME = "Catname";

}
