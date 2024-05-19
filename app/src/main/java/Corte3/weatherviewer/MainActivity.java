package Corte3.weatherviewer;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.*;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Weather> weatherList = new ArrayList<>();

    private WeatherArrayAdapter weatherArrayAdapter;
    private ListView weatherListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), this::onApplyWindowInsets);
    }

    private WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        weatherListView = (ListView) findViewById(R.id.weatherListView);
        weatherArrayAdapter = new WeatherArrayAdapter(this, weatherList);
        weatherListView.setAdapter(weatherArrayAdapter);

        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText locationEditText =
                        (EditText) findViewById(R.id.locationEditText);
                URL url = createURL (locationEditText.getText().toString());

                if (url != null){
                    dismissKeyboard(locationEditText);
                    GetWeatherTask getLocalWeatherTask= new GetWeatherTask();
                    getLocalWeatherTask.execute(url);
                }
                else {
                    Snackbar.make(findViewById(R.id.coordinatorLayaout),
                            R.string.url_invalida, Snackbar.LENGTH_LONG);

                }
            }
            private void dismissKeyboard(View view){
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
            }

            private URL createURL(String city){
                String apiKey = getString(R.string.api_key);
                String baseUrl = getString(R.string.web_service_url);

                try{
                    String urlSring = baseUrl + URLEncoder.encode(city, "UFT-8")+
                            "&UNITS=IMPERIAL&CNT=16&APPID="+ apiKey;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            private class GetWeatherTask
                    extends AsyncTask<URL, Void, JSONObject>{

                public JSONObject doInBackground(URL... params){
                    HttpURLConnection connection= null;

                    try {
                        connection = (HttpURLConnection) params[0].openConnection();
                        int response = connection.getResponseCode();

                        if (response == HttpURLConnection.HTTP_OK) {
                            StringBuilder builder = new StringBuilder();

                            try (BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(connection.getInputStream()))) {

                                String line;

                                while (line = reader.readLine() != null) {
                                    builder.append(line);
                                }
                            } catch (IOException e) {
                                Snackbar.make(findViewById(R.id.coordinatorLayaout),
                                        R.string.Error_lecura, Snackbar.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            return new JSONObject(builder.toString());
                        }
                        else {
                            Snackbar.make(findViewById(R.id.coordinatorLayaout),
                                    R.string.Errro_conection,Snackbar.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        Snackbar.make(findViewById(R.id.coordinatorLayaout),
                                R.string.Errro_conection, Snackbar.LENGTH_LONG).show();
                    } finally {
                        connection.disconnect();
                    }
                    return null;
                }

            }
        });

    }
}