package com.github.youssefwadie.ytpl.client;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class VideoDurationResponse {
    private List<Item> items = Collections.emptyList();

    @Data
    public static class Item {
        private ContentDetail contentDetails;
        @Data
        public static class ContentDetail {
            private String duration;
        }
    }

}
