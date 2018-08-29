package org.dreamexposure.network.hastebin;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Handles uploads to hastebin for pastes for whatever reason may be needed.
 */
public class Uploader {
    /**
     * Uploads the specified paste to hastebin.
     *
     * @param data The data to upload.
     * @return The URL returned from hastebin of the paste.
     */
    public static String post(String data) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), data);

        Request request = new Request.Builder().url("https://hastebin.com/documents").post(body).build();

        try {
            Response response = client.newCall(request).execute();

            if (response.body() != null)
                return "https://hastebin.com/" + new JSONObject(response.body()).getString("key");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Could not post!";
    }
}
