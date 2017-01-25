package agh.cs.peacegatch;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static j2html.TagCreator.*;
import static spark.Spark.*;

/**
 * PeaceGatch
 * Created by luknw on 17.01.2017
 */

public class Main {
    public static void main(String[] args) {
        new Chat().init();
    }
}
