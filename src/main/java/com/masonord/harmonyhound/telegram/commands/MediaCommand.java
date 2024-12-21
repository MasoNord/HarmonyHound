package com.masonord.harmonyhound.telegram.commands;

import com.google.api.services.drive.model.File;
import com.masonord.harmonyhound.exception.ExceedFileDurationException;
import com.masonord.harmonyhound.exception.ExceedFileSizeLimitException;
import com.masonord.harmonyhound.exception.FileTooShortException;
import com.masonord.harmonyhound.exception.SongNotFoundException;
import com.masonord.harmonyhound.response.rapidapi.Metadata;
import com.masonord.harmonyhound.response.rapidapi.Metapages;
import com.masonord.harmonyhound.response.rapidapi.Sections;
import com.masonord.harmonyhound.response.telegram.FilePathResponse;
import com.masonord.harmonyhound.response.rapidapi.RecognizedSongResponse;
import com.masonord.harmonyhound.service.*;
import com.masonord.harmonyhound.util.DownloadUtil;
import com.masonord.harmonyhound.util.FileSystemUtil;
import com.masonord.harmonyhound.util.LanguageUtil;
import com.masonord.harmonyhound.util.MediaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;

public class MediaCommand implements Command {
    private final static Logger LOGGER = LoggerFactory.getLogger(MediaCommand.class);
    private final double MIN_LENGTH = 3.00;
    private final double MAX_LENGTH = 15.00;

    private final RecognizeMediaService recognizeMediaService;
    private final GoogleDriveService googleDriveService;
    private final FileSystemUtil fileSystemUtil;
    private final DownloadUtil downloadUtil;
    private final MediaUtil mediaUtil;
    private final Message message;
    private final LanguageUtil languageUtil;
    private final UserService userService;

    public MediaCommand(String botToken,
                        LanguageUtil languageUtil,
                        Message message,
                        UserService userService) {
        this.message = message;
        this.languageUtil = languageUtil;
        this.userService = userService;
        this.googleDriveService = new GoogleDriveServiceImpl();
        this.recognizeMediaService = new RecognizeMediaService();
        this.fileSystemUtil = new FileSystemUtil();
        this.mediaUtil = new MediaUtil();
        this.downloadUtil = new DownloadUtil(botToken, languageUtil);
    }

    @Override
    public BotApiMethod<?> execute() throws ExceedFileSizeLimitException, URISyntaxException, IOException, InterruptedException, GeneralSecurityException, SongNotFoundException {
        String chatId = message.getChatId().toString();
        String in, out;
        FilePathResponse response;

        if (message.hasVideo()) {
            response = downloadUtil.download(message.getVideo().getFileId(), chatId);
        }else if (message.hasAudio()) {
            response = downloadUtil.download(message.getAudio().getFileId(), chatId);
        }else if (message.hasVoice()) {
            response = downloadUtil.download(message.getVoice().getFileId(), chatId);
        }else {
            response = downloadUtil.download(message.getVideoNote().getFileId(), chatId);
        }

        in = "downloaded-media/chat_" + chatId + "/" + response.getResult().getFile_path().split("/")[1];
        out = "downloaded-media/chat_" + chatId + "/" + response.getResult().getFile_path().split("/")[1].split("\\.")[0] + ".wav";

        mediaUtil.convertFileToWav(in, out);
        double fileLength = mediaUtil.getAudioDuration(out);

        LOGGER
                .atDebug()
                .setMessage(
                """
                    chatId: {}\
                    input file: {}\
                    output file: {}
                """)
                .addArgument(chatId)
                .addArgument(in)
                .addArgument(out)
                .log();
        try {
            if (fileLength < MIN_LENGTH) {
                LOGGER
                    .atError()
                    .setMessage("The file length is too small: {} smaller than {}")
                    .addArgument(fileLength)
                    .addArgument(MIN_LENGTH)
                    .log();
                throw new FileTooShortException(languageUtil.getProperty("file.too.short"));
            }else if (fileLength > MAX_LENGTH) {
                LOGGER
                        .atError()
                        .setMessage("Exceeded the allowed file length limit: {} bigger than {}")
                        .addArgument(fileLength)
                        .addArgument(MAX_LENGTH)
                        .log();
                throw new ExceedFileDurationException(languageUtil.getProperty("file.duration.exceeded"));
            }
        } catch (ExceedFileDurationException | FileTooShortException e) {
            fileSystemUtil.deleteFile(in);
            fileSystemUtil.deleteFile(out);
            throw new RuntimeException(e.getMessage());
        }

        File fileLink = googleDriveService.uploadFile(out, chatId);
        RecognizedSongResponse recognizedAudio = recognizeMediaService.recognizeAudio(fileLink.getWebViewLink());
        googleDriveService.deleteFile(fileLink.getId());
        fileSystemUtil.deleteFile(in);
        fileSystemUtil.deleteFile(out);

        userService.updateUserApiCalls(Long.valueOf(chatId));

        if (Objects.isNull(recognizedAudio.getTrack()) || recognizedAudio.getMatches().isEmpty()) {
            LOGGER.atError().setMessage("The song has not been found").log();
            throw new SongNotFoundException(languageUtil.getProperty("song.not.found"));
        }

        LOGGER
            .atInfo()
            .setMessage("The song {} has been found successfully")
            .addArgument(recognizedAudio.getTrack().getTitle())
            .log();
        return getSendMessage(chatId, recognizedAudio);
    }

    private SendMessage getSendMessage(String chatId, RecognizedSongResponse recognizedAudio) {
        SendMessage sendMessage = new SendMessage();
        List<Sections> sections = recognizedAudio.getTrack().getSections();
        List<Metapages> metapages = (sections.get(0).getMetapages().isEmpty() ? null : sections.get(0).getMetapages());
        List<Metadata> metadata = (sections.get(0).getMetadata().isEmpty() ? null : sections.get(0).getMetadata());
        String genre = recognizedAudio.getTrack().getGenres().getPrimary();

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(
                "Accuracy match - " + (recognizedAudio.getLocation().getAccuracy() * 10000) + "%\n\n" +
                        "*" + recognizedAudio.getTrack().getTitle() + "*"+ "\n\n" +
                        (Objects.isNull(metadata) ? "" : "Album: " + metadata.get(0).getText() + '\n') +
                        (Objects.isNull(metadata) ? "" : "Label: " + metadata.get(1).getText() + '\n') +
                        (Objects.isNull(metapages) ? "" : "Artist: " + metapages.get(0).getCaption() + '\n') +
                        (Objects.isNull(genre) ? "" : "Genre: " + genre + '\n') +
                        (Objects.isNull(metadata) ? "" : "Released: " + metadata.get(2).getText()  + '\n') +
                        "*Shazam: *" + recognizedAudio.getTrack().getUrl() + "\n"
        );

        return sendMessage;
    }
}
