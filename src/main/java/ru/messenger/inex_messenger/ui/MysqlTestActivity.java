package ru.messenger.inex_messenger.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import android.util.JsonReader;

import ru.messenger.inex_messenger.R;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Алексей on 16.02.2017.
 */

public class MysqlTestActivity extends XmppActivity {

    private Button mCancelButton;
    private Button mSaveButton;
    private EditText edittexttag = null;
    private MysqlFragment myfragment = null;

    String getTextTag(){
        String retstr = "";
        if(this.edittexttag != null){
            return this.edittexttag.getText().toString();
        }
        return retstr;
    }

    private ListView messagesView = null;

    @Override
    protected void refreshUiReal() {

    }

    @Override
    void onBackendConnected() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mysql_test);
//
//        FragmentManager fm = getFragmentManager();
//        myfragment = (MysqlFragment) fm.findFragmentById(android.R.id.content);
//        if (myfragment == null || !myfragment.getClass().equals(SettingsFragment.class)) {
//            myfragment = new MysqlFragment();
//            fm.beginTransaction().replace(android.R.id.content, myfragment).commit();
//        }

        this.edittexttag = (EditText)findViewById(R.id.editTextTag);

        this.mSaveButton = (Button) findViewById(R.id.save_button);
        this.mCancelButton = (Button) findViewById(R.id.cancel_button);
        this.mSaveButton.setOnClickListener(this.mSaveButtonClickListener);
        this.mCancelButton.setOnClickListener(this.mCancelButtonClickListener);
        this.messagesView = (ListView) this.findViewById(R.id.mysql_mess_content);

    }

    private final View.OnClickListener mSaveButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    //insert();
//                    select();
//                    //select2(0);
//                    //select2(1);
//                }
//            }).start();

            new GrabURL().execute("test");
        }
    };

//    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
//        protected Long doInBackground(URL... urls) {
//            int count = urls.length;
//            long totalSize = 0;
//            for (int i = 0; i < count; i++) {
//                totalSize += Downloader.downloadFile(urls[i]);
//                publishProgress((int) ((i / (float) count) * 100));
//                // Escape early if cancel() is called
//                if (isCancelled()) break;
//            }
//            return totalSize;
//        }
//
//        protected void onProgressUpdate(Integer... progress) {
//            setProgressPercent(progress[0]);
//        }
//
//        protected void onPostExecute(Long result) {
//            showDialog("Downloaded " + result + " bytes");
//        }
//    }

    private class GrabURL extends AsyncTask<String, Void, String[]> {
        private static final int REGISTRATION_TIMEOUT = 3 * 1000;
        private static final int WAIT_TIMEOUT = 30 * 1000;
        private String content = null;
        private boolean error = false;
//        private ProgressDialog dialog =
//                new ProgressDialog(getBaseContext());

        protected void onPreExecute() {
//            dialog.setMessage("Getting your data... Please wait...");
//            dialog.show();
        }

        protected String[] doInBackground(String... urls) {
            String[] retmass = select(getTextTag());
//            dialog.dismiss();
            return retmass;
        }

        protected void onCancelled() {
//            dialog.dismiss();
//            Toast toast = Toast.makeText(this,
//                    "Error connecting to Server", Toast.LENGTH_LONG);
//            toast.show();
//            loadingMore = false;

        }

        protected void onPostExecute(String[] content) {
            set_list_items(content);
        }
    }
    void set_list_items(String[] messagesList){

        try {
            //view = inflater.inflate(R.layout.fragment_mysql, container, false);

            //messagesView = (ListView) view.findViewById(R.id.messages_view);
            //Activity actyvity = getActivity();
            ArrayAdapter<String> adapter = null; //android.R.layout.simple_list_item_1

            adapter = new ArrayAdapter<String>(this.getBaseContext(), R.layout.my_item_1, messagesList);


            // присваиваем адаптер списку
            messagesView.setAdapter(adapter);
        }
        catch(Exception e){
            Log.d("error: ", e.toString());
        }
    }

    public String[] select(String tag) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        String[] messages = null;

        String result = null;
        InputStream is = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.1.146/controller/query.php");
            nameValuePairs.add(new BasicNameValuePair("hashkey", "95aa0c8e222d014fa59892f635a7a227"));
            nameValuePairs.add(new BasicNameValuePair("query", "select * from mytest where param1 like '%" + tag + "%'"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = "";


//            JsonReader jreader = new JsonReader(new InputStreamReader(is, "UTF-8"));
//            readMessage(jreader);
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("pass 2", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 2", e.toString());
        }

        try {

            JSONArray jsonarray = new JSONArray(result);
            messages = new String[jsonarray.length()];
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String name = jsonobject.getString("param1");
                //String url = jsonobject.getString("url");
                messages[i] = name;
            }
            //MysqlFragment myfragment = new MysqlFragment();
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
        }
        return messages;
    }

    public void readMessage(JsonReader reader) throws IOException {
        long id = -1;
        String text = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextLong();
            } else if (name.equals("param1")) {
                text = reader.nextString();
            }
//            else if (name.equals("geo") && reader.peek() != JsonToken.NULL) {
//                geo = readDoublesArray(reader);
//            } else if (name.equals("user")) {
//                user = readUser(reader);
//            } else {
//                reader.skipValue();
//            }
        }
        reader.endObject();
    }

    void select2(int byGetOrPost) {
        if (byGetOrPost == 0) { //means by Get Method

            try {
                //String username = (String)arg0[0];
                //String password = (String)arg0[1];
                //String link = "http://myphpmysqlweb.hostei.com/login.php?username="+username+"& password="+password;


                String link = "http://192.168.1.146?hashkey=95aa0c8e222d014fa59892f635a7a227&query=" + URLEncoder.encode("select * from mytest", "UTF-8");

                URL url = new URL(link);

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));

                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                in.close();
                //return sb.toString();
            } catch (Exception e) {
                //return new String("Exception: " + e.getMessage());
            }
        } else {
            try {
//                String username = (String)arg0[0];
//                String password = (String)arg0[1];

                String link = "http://192.168.1.146/";
                String data = URLEncoder.encode("hashkey", "UTF-8") + "=" +
                        URLEncoder.encode("95aa0c8e222d014fa59892f635a7a227", "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode("select * from mytest", "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                String test = sb.toString();
                //return sb.toString();
            } catch (Exception e) {
                //return new String("Exception: " + e.getMessage());
            }
        }
    }


    private void insert() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://192.168.1.49/testdb";
            Connection c = DriverManager.getConnection(url, "root", "root");
            PreparedStatement st = c.prepareStatement("insert into mytest(param1,param2,param3,param4,param5,param6,param7,param8,param9) values(?,?,?,?,?,?,?,?,?)");
            st.setString(1, "Проверка проверка проверка");
            st.setInt(2, 2);
            st.setInt(3, 2);
            st.setInt(4, 2);
            st.setInt(5, 2);
            st.setInt(6, 2);
            st.setInt(7, 2);
            st.setInt(8, 2);
            st.setInt(9, 2);

            st.execute();
            st.close();
            c.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private final View.OnClickListener mCancelButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            finish();
        }
    };
}
