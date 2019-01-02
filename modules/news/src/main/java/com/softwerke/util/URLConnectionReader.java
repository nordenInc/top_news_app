package com.softwerke.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * This util class provides data for specific URL and defines its type.
 * Will be used in {@code NewsResource} implementations.
 */
public class URLConnectionReader {

    /**
     * Gets page content and returns it to array of Strings.
     *
     * @param path to web source
     * @return array of Strings
     * @throws Exception
     */
    public static String getInfo(String path) throws Exception {

        URL url = new URL(path);
        URLConnection conn = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String input = reader.lines().collect(Collectors.joining("\n"));

        return input;
    }

    /**
     * Returns content type for web page.
     *
     * @param path
     * @return true - if type is JSON; false - if it is XML
     * @throws Exception
     */
    public static boolean getRequestType(String path) throws Exception {
        String input = getInfo(path);
        input.trim();
        if (input.charAt(0) == '<') {return false;}
        return true;
    }
}
