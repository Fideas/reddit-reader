package com.nicolascarrasco.www.redditreader.data;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nicolás Carrasco on 30/12/2015.
 */
public class FetchPostTask extends AsyncTask<String, Void, Post> {
    private static final String LOG_TAG = FetchPostTask.class.getSimpleName();
    private TaskDelegate mDelegate;

    public FetchPostTask(TaskDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    protected Post doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String postsJsonString;
        String subreddit = params[0];

        try {
            Uri builder = Uri.parse("http://www.reddit.com/r").buildUpon()
                    .appendPath(subreddit)
                    .appendPath(".json")
                    .appendQueryParameter("limit", "1")
                    .appendQueryParameter("after", params[1]).build();

            URL url = new URL(builder.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                //Nothing to do
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                //Stream was empty. No point in parsing
                return null;
            }

            postsJsonString = buffer.toString();

            try {
                return getPostDataFromJson(postsJsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

    private Post getPostDataFromJson(String postsJsonString) throws JSONException {

        final String REDDIT_CHILDREN = "children";
        final String REDDIT_DATA = "data";
        final String REDDIT_TITLE = "title";
        final String REDDIT_AFTER= "after";

        JSONObject postsJson = new JSONObject(postsJsonString);
        JSONObject dataJson = postsJson.getJSONObject(REDDIT_DATA);
        JSONArray childrenArray = dataJson.getJSONArray(REDDIT_CHILDREN);
        JSONObject jsonPost = childrenArray.getJSONObject(0).getJSONObject(REDDIT_DATA);

        String title = jsonPost.getString(REDDIT_TITLE);
        String after = dataJson.getString(REDDIT_AFTER);
        String subreddit = jsonPost.getString("subreddit");

        Post post = new Post();
        post.setTitle(title);
        post.setAfter(after);
        post.setSubreddit(subreddit);


        return post;
    }

    @Override
    protected void onPostExecute(Post post) {
        mDelegate.taskCompletionResult(post);
    }

    public interface TaskDelegate {
        void taskCompletionResult(Post result);
    }
}
