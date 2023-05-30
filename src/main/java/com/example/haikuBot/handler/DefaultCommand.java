package com.example.haikuBot.handler;

import com.example.haikuBot.components.Buttons;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class DefaultCommand {
    static String param = "";
    static Random random = new Random();
    String pathHokku = "src/main/resources/hokky.txt";
    String pathNames = "src/main/resources/names.txt";

    String HELP_TEXT = """
            Этот бот генерирует различные Хокку и изображения к ним. Команда для генерации:
            /generate - сгенерировать Хокку
            /help - справка""";

    public SendMessage helpBotOperation(long chatId) {
        log.info("help message operation");
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(HELP_TEXT);
        message.setReplyMarkup(Buttons.inlineMarkup());
        return message;
    }

    public SendPhoto startBotOperation(long chatId) {
        log.info("start message operation");
        SendPhoto photo = new SendPhoto();
        InputFile file = new InputFile();
        photo.setCaption(loadFile(pathHokku, pathNames));
        file.setMedia(loadImage());
        photo.setPhoto(file);
        photo.setChatId(chatId);
        photo.setReplyMarkup(Buttons.startInlineMarkup());
        return photo;
    }

    public String loadFile(String pathHokku, String pathNames) {
        List<String> contentHokku = readFileContents(pathHokku);
        List<String> contentNames = readFileContents(pathNames);
        if (contentHokku == null || contentNames == null) {
            System.out.println("Один из файлов пуст(");
            return ("Один из файлов пуст(");
        }
        String[] contextHokku = new String[3];
        for (int i = 0; i < 3; i++) {
            contextHokku[i] = contentHokku.get(random.nextInt(contentHokku.size() - 1));
        }
        String[] contextNames = new String[3];
        for (int i = 0; i < 3; i++) {
            contextNames[i] = contentNames.get(random.nextInt(contentNames.size() - 1));
        }
        String[] contextEra = {"г. до н.э.", "г. н.э"};
        param = contextHokku[1].substring(0, contextHokku[1].indexOf(" "));
        return contextHokku[0] + "\n" + contextHokku[1] + "\n" + contextHokku[2] + "\n" +
                "        -" + contextNames[0].substring(0, 1).toUpperCase() + contextNames[0].substring(1) +
                " " + contextNames[1].substring(0, 1).toUpperCase() + contextNames[1].substring(1) +
                " " + contextNames[2].substring(0, 1).toUpperCase() + contextNames[2].substring(1) +
                " " + random.nextInt(2000) + " " + contextEra[random.nextInt(2)];
    }

    public List<String> readFileContents(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл. Возможно файл не находится в нужной директории.");
            return null;
        }
    }

    private static File loadImage() {
        try {
            BufferedImage img = ImageIO.read(new URL(connectGoogle()));
            File file = new File("src/main/resources/sendThis.jpg");
            ImageIO.write(img, "png", file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //ошибки вылетают из-за неправильного запроса в param пролетают спец символы
    private static String connectGoogle() {
        String result = "";
        String neeeed = "https://gorod-812.ru/content/uploads/2020/11/hokku.jpg"; //при ошибке вылетит подготовленное изображение
        try {
            String googleUrl = "https://www.google.com/search?tbm=isch&q= Японская живопись " + param;
            Document doc1 = Jsoup.connect(googleUrl).get();
            Element media = doc1.select("[data-src]").get(0);
            String sourceUrl = media.attr("abs:data-src");
            result = "Image source= " + sourceUrl.replace("&quot", "");
            neeeed = sourceUrl.replace("&quot", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return neeeed;
    }

    // Яндекс начинает дропать капчу, что рушит реализацию
    public static String connectYandex() {
        final String url = "https://yandex.com/images/search?text=";
        String abc = "япония";
        final String charset = "UTF-8";
        try {
            Document docImage = Jsoup.connect(url
                    + abc).get();
            log.info(url
                    + URLEncoder.encode(abc, charset));
            Element images =
                    docImage.select("img.serp-item__thumb").first();
            assert images != null;
            String srcImage = images.attr("src");
            log.info("src: " + images.attr("src"));
            return srcImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
