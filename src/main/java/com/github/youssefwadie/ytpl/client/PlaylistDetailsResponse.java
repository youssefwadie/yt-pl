package com.github.youssefwadie.ytpl.client;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PlaylistDetailsResponse {
    private String nextPageToken;
    private List<Item> items = Collections.emptyList();

    @Data
    public static class Item {
        private ContentDetails contentDetails;

        @Data
        public static class ContentDetails {
            private String videoId;
        }
    }
}
