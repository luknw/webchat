//package agh.cs.peacegatch;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.eclipse.jetty.websocket.api.Session;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.Calendar;
//import java.util.Iterator;
//import java.util.Map;
//
///**
// * PeaceGatch
// * Created by luknw on 26.01.2017
// */
//
//public class Chatbot implements Sniffer {
//    private static final char COMMAND_START = '/';
//    private static final String TIME = "time";
//    private static final String DATE = "date";
//    private static final String WEATHER = "weather";
//    private static final String WEATHER_REQUEST = "";
//    private static final String CANNOT_ACCESS_WEATHER_SERVICE = "Cannot access weather service.";
//    private static final String PATH = "main";
//    private static final String FIELD_SEPARATOR = " \n";
//    private static final String KEY_VALUE_SEPARATOR = ": ";
//    private static final String DATE_SEP = " ";
//    private static final String ROYAL_CITY = "Cracow";
//
//    private final String name;
//    private final Chat context;
//    private final String channel;
//
//
//    public Chatbot(String name, Chat context, String channel) {
//        this.name = name;
//        this.context = context;
//        this.channel = channel;
//    }
//
//    @Override
//    public void sniffSniff(Session user, String message) {
//        String userChannel = context.getUsers().get(user).getChannel();
//        if (!userChannel.equals(channel)
//                || message.length() < 2
//                || message.charAt(0) != COMMAND_START) {
//            return;
//        }
//
//        User dummy = new UserImpl(name, userChannel, user);
//        switch (message.substring(1)) {
//            case TIME:
//                context.broadcastMessage(dummy, getTimeMessage());
//                break;
//            case DATE:
//                context.broadcastMessage(dummy, getDateMessage());
//                break;
//            case WEATHER:
//                context.broadcastMessage(dummy, getWeatherMessage());
//                break;
//        }
//    }
//
//    private String getWeatherMessage() {
//        JsonNode apiResponse;
//        try {
//            apiResponse = new ObjectMapper().readTree(new URL(WEATHER_REQUEST));
//        } catch (IOException e) {
//            return CANNOT_ACCESS_WEATHER_SERVICE;
//        }
//
//        return parseApiResponse(apiResponse);
//    }
//
//    private String parseApiResponse(JsonNode apiResponse) {
//        apiResponse = apiResponse.path(PATH);
//        Iterator<Map.Entry<String, JsonNode>> fields = apiResponse.fields();
//        StringBuilder parsed = new StringBuilder(ROYAL_CITY).append(KEY_VALUE_SEPARATOR);
//        while (fields.hasNext()) {
//            Map.Entry<String, JsonNode> field = fields.next();
//            parsed.append(field.getKey())
//                    .append(KEY_VALUE_SEPARATOR)
//                    .append(field.getValue().asText());
//            if (fields.hasNext()) {
//                parsed.append(FIELD_SEPARATOR);
//            }
//        }
//        return parsed.toString();
//    }
//
//    private String getTimeMessage() {
//        return Calendar.getInstance().getTime().toString().split(DATE_SEP)[3];
//    }
//
//    private String getDateMessage() {
//        String[] helper = Calendar.getInstance().getTime().toString().split(DATE_SEP);
//        StringBuilder date =
//                new StringBuilder(helper[0]).append(" ")
//                        .append(helper[1]).append(" ")
//                        .append(helper[2]).append(", ")
//                        .append(helper[5]);
//        return date.toString();
//    }
//}
