package com.github.youssefwadie.ytpl.cli;

import com.github.youssefwadie.ytpl.client.YoutubeApiClient;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.StringJoiner;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Option;

@RequiredArgsConstructor
@Component
@Command(name = "yt-pl", description = " A simple app to get the lengths of Youtube playlists")
public class YtPlCommand implements Callable<Integer> {
    @Parameters(description = "playlist url")
    private String playlistUrl;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "print this help and exit")
    private boolean usageHelpRequested;

    private final YoutubeApiClient youtubeApiClient;

    @Override
    public Integer call() throws Exception {
        if (usageHelpRequested) {
            CommandLine.usage(this, System.out);
            return 0;
        }

        if (!StringUtils.hasText(playlistUrl)) return 1;


        val playlistReport = youtubeApiClient.getPlaylistReport(playlistUrl);
        val numberOfVideosMessage = String.format("No of videos: %d", playlistReport.numberOfVideos());
        val videosDurationMessage = getVideosDurationMessage(playlistReport.totalDuration());

        System.out.println(numberOfVideosMessage);
        System.out.println(videosDurationMessage);
        return 0;
    }


    private String getVideosDurationMessage(Duration totalDuration) {
        val totalSeconds = totalDuration.get(ChronoUnit.SECONDS);
        val hours = totalSeconds / 3600;
        val minutes = (totalSeconds % 3600) / 60;
        val remainingSeconds = totalSeconds % 60;

        val exceedsAnHour = hours > 0;

        val sj = new StringJoiner(", ");

        if (exceedsAnHour) sj.add(hours + " hours");
        if (minutes != 0 || exceedsAnHour) sj.add(minutes + " minutes");
        sj.add(remainingSeconds + " seconds");

        return "Total length of playlist: " + sj;
    }

}
