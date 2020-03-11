package com.w.consumer.redis;

public class RatingKey extends BasePrefix{
    public RatingKey(String prefix) {
        super(prefix);
    }
    public static RatingKey UserRatingKey = new RatingKey("uid:");
    public static RatingKey RatingUidPidHtml = new RatingKey("RatingUidPidHtml");
}
