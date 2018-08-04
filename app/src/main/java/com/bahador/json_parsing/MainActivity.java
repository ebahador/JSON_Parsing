package com.bahador.json_parsing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ArrayList<HashMap<String, String>> contentList;
    Button btnParseJSON;

    InitialItems initItems = new InitialItems() {
        @Override
        public void BeginParsing() {
            JSONArray jsonArray;
            try {
                JSONObject reader = new JSONObject(loadJSONFromRaw());
                jsonArray = reader.getJSONArray("Information");
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("Id");
                        String name = jsonObject.getString("Name");
                        String surname = jsonObject.getString("Surname");
                        int age = jsonObject.getInt("Age");
                        initItems.InitToHashMap(id, name, surname, age);
                    } catch (JSONException e) {
                        //Oh Gosh!..
                        e.printStackTrace();
                    }
                }
                ListView listView = findViewById(R.id.listViewJSON);
                SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, contentList,
                        R.layout.list_item, new String[]{"Id", "Name", "Surname", "Age"},
                        new int[]{R.id.IdNum, R.id.IdName, R.id.IdSurname, R.id.IdAge});
                listView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void InitToHashMap(int id, String name, String surname, int age) {
            HashMap<String, String> contact = new HashMap<>();
            contact.put("Id", Integer.toString(id));
            contact.put("Name", name);
            contact.put("Surname", surname);
            contact.put("Age", Integer.toString(age));
            // add contact to contact list
            contentList.add(contact);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentList = new ArrayList<>();
        btnParseJSON = findViewById(R.id.jsonPars_btn);
        btnParseJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initItems.BeginParsing();
            }
        });
    }

    private String loadJSONFromRaw() {
        String json = null;

        try {
            InputStream inputStream = this.getResources().openRawResource(R.raw.data);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;

    }
}
