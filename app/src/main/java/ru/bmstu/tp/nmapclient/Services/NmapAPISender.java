package ru.bmstu.tp.nmapclient.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import ru.bmstu.tp.nmapclient.Services.Exceptions.BadRequestException;
import ru.bmstu.tp.nmapclient.Services.Exceptions.BadSessionIdException;
import ru.bmstu.tp.nmapclient.Services.Exceptions.InitialServerException;
import ru.bmstu.tp.nmapclient.Services.Exceptions.ServerConnectException;

public class NmapAPISender {
    private static final String SERVER_URL = "http://127.0.0.1:8080/nmap";
    private enum Status {
        OK, ERROR, BAD_ID, BAD_REQUEST, WAIT
    }

    private static StringBuilder sendRequest(StringBuilder url, String data) {
        URL getReq;
        try {
            getReq = new URL(url.toString());
        } catch (MalformedURLException e) {
            return null;
        }

        HttpURLConnection con;
        try {
            con = (HttpURLConnection)getReq.openConnection();
            if (data != null) {
                con.setDoOutput(true);
                con.setChunkedStreamingMode(0);
                OutputStream out = new BufferedOutputStream(con.getOutputStream());
                out.write(data.getBytes());
            }
        } catch (IOException e) {
            return null;
        }
        StringBuilder response = new StringBuilder();
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = con.getInputStream();
            if (con.getResponseCode() != 200) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignore) {}
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {}
            }
            con.disconnect();
        }
        return response;
    }

    public static Long getSessionId() throws ServerConnectException, InitialServerException {
        StringBuilder url = new StringBuilder(SERVER_URL);
        url.append("/session_id");
        StringBuilder response = sendRequest(url, null);
        if (response == null || response.length() == 0) {
            throw new ServerConnectException();
        }
        JSONObject json;
        try {
            json = new JSONObject(response.toString());
            switch (Status.valueOf(json.getString("status"))) {
                case OK :
                    return json.getLong("id");
                default:
                    throw new InitialServerException();
            }
        } catch (JSONException | IllegalArgumentException e) {
            throw new InitialServerException();
        }
    }

    public static void sendGcmId(Long SessionId, String GcmId) throws
            ServerConnectException, InitialServerException, BadSessionIdException, BadRequestException {
        StringBuilder url = new StringBuilder(SERVER_URL);
        url.append("/gcm_id?id=" + SessionId);
        StringBuilder response = sendRequest(url, GcmId);
        if (response == null || response.length() == 0) {
            throw new ServerConnectException();
        }
        JSONObject json;
        try {
            json = new JSONObject(response.toString());
            switch (Status.valueOf(json.getString("status"))) {
                case OK :
                    break;
                case BAD_ID :
                    throw new BadSessionIdException();
                case BAD_REQUEST :
                    throw new BadRequestException();
                default:
                    throw new InitialServerException();
            }
        } catch (JSONException e) {
            throw new InitialServerException();
        }
    }

    public static String sendAPIRequest(Long SessionId, String request) throws
            ServerConnectException, InitialServerException, BadSessionIdException, BadRequestException {
        StringBuilder url = new StringBuilder(SERVER_URL);
        url.append("/api_request?id=" + SessionId);
        StringBuilder response = sendRequest(url, request);
        if (response == null || response.length() == 0) {
            throw new ServerConnectException();
        }
        JSONObject json;
        try {
            json = new JSONObject(response.toString());
            switch (Status.valueOf(json.getString("status"))) {
                case OK :
                    return json.getString("result"); // return a json string
                case WAIT :
                    return null;
                case BAD_ID :
                    throw new BadSessionIdException();
                case BAD_REQUEST :
                    throw new BadRequestException();
                default:
                    throw new InitialServerException();
            }
        } catch (JSONException e) {
            throw new InitialServerException();
        }
    }
}
