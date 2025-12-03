package com.echipaMisterelor.playlisteriaAPI.Services.Downloader;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.AudioFormat;
import com.github.kiulian.downloader.model.videos.formats.Format;
import com.github.kiulian.downloader.model.videos.formats.VideoWithAudioFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DownloaderService implements IDownloader{

    private static HashMap<String, String> formats = new HashMap<>();

    public DownloaderService(){
        formats.put("mp3", "libmp3lame");
        formats.put("aac", "aac");
        formats.put("flac", "flac");
        formats.put("wav", "pcm_s16le");

    }

    @Override
    public void downloadYoutube(String targetFormat, String url, String outputFilename) {
        // init downloader with default config
        YoutubeDownloader downloader = new YoutubeDownloader();

        // sync parsing
        RequestVideoInfo request = new RequestVideoInfo(url);
        Response<VideoInfo> response = downloader.getVideoInfo(request);
        VideoInfo video = response.data();

        // get videos formats only with audio
        List<AudioFormat> videoWithAudioFormats = video.audioFormats();
        videoWithAudioFormats.forEach(it -> {
            System.out.println(it.extension().value() + " " + it.audioQuality() + " : " + it.url());
        });

        File outputDir = new File("my_videos");
        Format format = videoWithAudioFormats.get(0);

        System.out.println("Downloading video from youtube");

        // sync downloading
        RequestVideoFileDownload vid_request = new RequestVideoFileDownload(format)
                // optional params
                .saveTo(outputDir) // by default "videos" directory
                .renameTo(outputFilename) // by default file name will be same as video title on youtube
                .overwriteIfExists(true); // if false and file with such name already exits sufix will be added video(1).mp4
        Response<File> vid_response = downloader.downloadVideoFile(vid_request);
        File data = vid_response.data();

        //call the video metadata service to get the video metadata


        try {
            convertContent("my_videos/" + outputFilename + ".m4a", "my_videos/" + outputFilename + "." + targetFormat, targetFormat);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void tryConvertContent(String inputFilePath, String outputFilePath, String targetFormat) {
        System.out.println("Converting content");

        //if the format is not in the hashtable, return
        if(!formats.containsKey(targetFormat)){
            System.out.println("Format not supported");
            return;
        }

        //check if target file already exists in folder, and if that is the case, stop
        File f = new File(outputFilePath);
        if(f.exists() && !f.isDirectory()) {
            System.out.println("File already exists");
            return;
        }

        //try conversion
        try {
            convertContent(inputFilePath, outputFilePath, targetFormat);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void convertContent(String inputFilePath, String outputFilePath, String targetFormat) throws IOException{
        System.out.println(formats.get(targetFormat));
        // Construct the FFmpeg command
        String[] command = {
                "ffmpeg",
                "-nostdin",
                "-y",
                "-i", inputFilePath,
                "-acodec", formats.get(targetFormat),
                outputFilePath
        };

        // Execute the command using ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true); // Redirect error stream to input stream
        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
            }
        }

        // Wait for the process to complete
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("FFmpeg process returned non-zero exit code: " + exitCode);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
