package com.github.youssefwadie.ytpl.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class YoutubeApiClient {
    private final static String PLAYLIST_ID_REGEX = "^(\\S+list=)([\\w_-]+)\\S*$";
    private final Pattern playlistIdPattern = Pattern.compile(PLAYLIST_ID_REGEX);
    private final RestTemplate restTemplate;
    private final UriBuilderFactory uriBuilderFactory;

    public PlaylistReport getPlaylistReport(String playlistUrl) {
        val playlistId = parsePlaylistId(playlistUrl);
        val numberOfVideos = new AtomicInteger(0);

        Duration totalDuration = Duration.ZERO;
        String nextTokenPage = "";

        int pageNumber = 1;
        do {
            log.info("getting results [{}] page for playlist [{}]", pageNumber++, playlistId);
            val playlistDetails = getPlaylistDetails(playlistId, nextTokenPage);
            val blockDuration = playlistDetails.getItems()
                    .parallelStream()
                    .map(item -> item.getContentDetails().getVideoId())
                    .map(videoId -> {
                        numberOfVideos.incrementAndGet();
                        return getVideoDuration(videoId);
                    })
                    .reduce(Duration.ofNanos(0), Duration::plus);

            totalDuration = totalDuration.plus(blockDuration);
            nextTokenPage = playlistDetails.getNextPageToken();
        } while (StringUtils.hasText(nextTokenPage));
        return new PlaylistReport(totalDuration, numberOfVideos.get());
    }

    public PlaylistDetailsResponse getPlaylistDetails(String playlistId, String nextPageToken) {
        val playlistDetails = restTemplate.getForObject(buildPlaylistUri(playlistId, nextPageToken), PlaylistDetailsResponse.class);

        if (playlistDetails == null) {
            log.error("failed to fetch playlist [{}] details with next page token [{}]", playlistId, nextPageToken);
            throw new IllegalStateException("failed to fetch playlist %s details".formatted(playlistId));
        }

        return playlistDetails;
    }

    private Duration getVideoDuration(String videoId) {
        log.info("fetching video [{}] duration", videoId);
        val videoDuration = restTemplate.getForObject(buildVideoDurationUri(videoId), VideoDurationResponse.class);
        if (videoDuration == null) {
            log.warn("failed to fetch the video [{}] duration", videoId);
            return Duration.ZERO;
        }
        val durationAsString = videoDuration.getItems().get(0).getContentDetails().getDuration();
        return Duration.parse(durationAsString);
    }

    private String parsePlaylistId(String playlistUrl) {
        val matcher = playlistIdPattern.matcher(playlistUrl);
        if (!matcher.matches()) throw new IllegalArgumentException("Invalid playlist url");
        return matcher.group(2);
    }

    private URI buildVideoDurationUri(String videoId) {
        return uriBuilderFactory.builder()
                .path("videos")
                .queryParam("part", "contentDetails")
                .queryParam("id", videoId)
                .queryParam("fields", "items/contentDetails/duration")
                .build();
    }

    private URI buildPlaylistUri(String playlistId, String nextPageToken) {
        return uriBuilderFactory.builder()
                .path("playlistItems")
                .queryParam("part", "contentDetails")
                .queryParam("maxResults", 50)
                .queryParam("pageToken", nextPageToken)
                .queryParam("fields", "items/contentDetails/videoId,nextPageToken")
                .queryParam("playlistId", playlistId)
                .build();
    }
}
