package dev.bamban.aplanes.messages.handlers;

import dev.bamban.aplanes.Message;
import dev.bamban.aplanes.MessageRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class InfoReqMessageConsumer implements Consumer<Message> {
    private static final Pattern messagePattern = Pattern.compile("^(metar|shorttaf|taf|vatatis)\\s+(\\w+)$", Pattern.CASE_INSENSITIVE);
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private final MessageRepository messageRepository;

    public InfoReqMessageConsumer(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void accept(Message message) {
        var m = messagePattern.matcher(message.getContent());

        if (!m.find()) {
            message.setContent(message.getContent() + "\n" + "unknown information request");
            return;
        }

        var service = m.group(1).toUpperCase();
        var icao = m.group(2).toUpperCase();

        if (service.equals("METAR")) {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create("https://metar-taf.com/%s".formatted(icao)))
                    .build();
            var html = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .join();

            var doc = org.jsoup.Jsoup.parse(html);
            var metar = doc.select("code").text();

            message.setContent("%s %s\n%s".formatted(service, icao, metar));
            message.setReceiver("SERVER");

        } else if (service.equals("TAF")) {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create("https://metar-taf.com/taf/%s".formatted(icao)))
                    .build();
            var html = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .join();

            var doc = org.jsoup.Jsoup.parse(html);
            var taf = doc.select("code").text();

            message.setContent("%s %s\n%s".formatted(service, icao, taf));
            message.setReceiver("SERVER");
        }

        messageRepository.save(message);
    }
}
