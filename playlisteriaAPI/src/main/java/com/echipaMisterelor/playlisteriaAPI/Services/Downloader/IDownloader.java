package com.echipaMisterelor.playlisteriaAPI.Services.Downloader;

import java.io.IOException;

public interface IDownloader {
    void downloadYoutube(String targetFormat, String url, String outputFilename);

    void tryConvertContent(String inputFilePath, String outputFilePath, String targetFormat) throws IOException;

    void convertContent(String inputFilePath, String outputFilePath, String targetFormat) throws IOException;
}
