package com.gwtask.gwtask;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

    public class checkWordRequest extends AsyncTask<String, Void, Boolean> {
    @Override
    protected Boolean doInBackground(String... params) {
        Boolean result = false;
        final String app_id = "9c834ad6";
        final String app_key = "36db9ed6a34d96ec2902a5087868f5e4";

        try {
            URL url = new URL(dictionaryEntries(params[0]));
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("app_id",app_id);
            urlConnection.setRequestProperty("app_key",app_key);

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            String definition = "";
            String kategorija = "";

                JSONObject js = new JSONObject(stringBuilder.toString());
                JSONArray results = js.getJSONArray("results");

                JSONObject lentries = results.getJSONObject(0);
                JSONArray la = lentries.getJSONArray("lexicalEntries");
                JSONObject entries = la.getJSONObject(0);
                JSONArray e = entries.getJSONArray("entries");
                JSONObject senses = e.getJSONObject(0);
                JSONArray s = senses.getJSONArray("senses");
                JSONObject d = s.getJSONObject(0);
                JSONArray de = d.getJSONArray("definitions");
                definition = de.getString(0);

                Object lc = entries.get("lexicalCategory");
                kategorija = lc.toString();

            String laikas = String.valueOf(new SimpleDateFormat("MM-dd,HH:mm").format(Calendar.getInstance().getTime()));
            Zodis ieskomas = new Zodis(Util.ivestis, kategorija, definition, laikas);
            Util.dabartinis = ieskomas;
            Util.history.add(0, ieskomas);

            result = true;
        }
        catch (Exception e) {
            result = false;
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
    }

    private String dictionaryEntries(String word) {
            final String language = "en";
            final String word_id = word.toLowerCase();
            Util.ivestis = word_id;
            return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    }
}
