package com.panaceasoft.citiesdirectory;

import android.app.Application;

/**
 * Created by Panacea-Soft on 7/15/15.
 * Contact Email : teamps.is.cool@gmail.com
 */

public class Config extends Application{

    public static final String GOOGLE_PROJECT_NO = "312984937906";

    public static final String MSG_KEY = "m";

    public static final int PAGINATION = 3;

    public static final String APP_API_URL = "http://www.panacea-soft.com/citiesdirectory/index.php";
    //public static final String APP_API_URL = "http://192.168.43.52:7777/citiesdirectory/index.php";

    public static final String APP_IMAGES_URL = "http://www.panacea-soft.com/citiesdirectory/uploads/";
    //public static final String APP_IMAGES_URL = "http://192.168.43.52:7777/citiesdirectory/uploads/";

    public static final String GET_ALL = "/rest/cities/get"; // f

    public static final String ITEMS_BY_SUB_CATEGORY = "/rest/items/get/city_id/"; // f

    public static final String ITEMS_BY_ID = "/rest/items/get/id/"; // f

    public static final String SEARCH_BY_GEO = "/rest/items/search_by_geo/miles/"; //f

    public static final String POST_ITEM_INQUIRY = "/rest/items/inquiry/id/"; // f

    public static final String POST_USER_LOGIN = "/rest/appusers/login"; //f

    public static final String POST_REVIEW = "/rest/items/review/id/"; //f

    public static final String POST_USER_REGISTER = "/rest/appusers/add/"; //f

    public static final String POST_USER_UPDATE = "/rest/appusers/update/id/"; // f

    public static final String POST_ITEM_LIKE = "/rest/items/like/id/"; //f

    public static final String POST_ITEM_FAVOURITE = "/rest/items/favourite/id/"; //f

    public static final String POST_ITEM_SEARCH = "/rest/items/search/city_id/"; //f

    public static final String GET_FAVOURITE = "/rest/items/is_favourite/id/"; //f

    public static final String GET_LIKE = "/rest/items/is_like/id/"; //f

    public static final String GET_FORGOT_PASSWORD = "/rest/appusers/reset/email/"; //f

    public static final String GET_FAVOURITE_ITEMS = "/rest/items/user_favourites/user_id/"; //f

    public static final String GET_CITY_NEWS = "/rest/cities/feeds/city_id/"; //f

    public static final String POST_TOUCH_COUNT = "/rest/items/touch/id/"; //f

    public static final String POST_PROFILE_IMAGE = "/rest/images/upload"; //f

    public static final String POST_GCM_REGISTER = "/rest/gcm/register";

    public static final String POST_GCM_UNREGISTER = "/rest/gcm/unregister";

    public static final String POST_ITEM_RATING = "/rest/items/rating/id/"; //f

    public static final String POST_ITEM_IS_RATE = "/rest/items/is_rate/id/";


}
