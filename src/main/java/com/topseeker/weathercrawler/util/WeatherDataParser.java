package com.topseeker.weathercrawler.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.topseeker.weathercrawler.model.WeatherService;

@Component
public class WeatherDataParser {

    @Autowired
    private WeatherService weatherService;

    @Scheduled(cron = "0 0 0,12 * * ?")
    public void downloadAndParseWeatherData() throws ParseException {
        File dir = new File("C:\\weather");
        if (!dir.exists()) {
            dir.mkdir();
        }

        String url = "https://opendata.cwa.gov.tw/fileapi/v1/opendataapi/F-B0053-055?Authorization=CWA-8B833D9D-DC47-49A0-80EE-E1314DD8FA4A&downloadType=WEB&format=JSON";
        String filename = "weather.json";
        File file = new File(dir, filename);
        try {
            URL myURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);

            int length = 0;
            byte[] b = new byte[4096];
            while ((length = is.read(b)) != -1) {
                fos.write(b, 0, length);
                fos.flush();
            }

            fos.close();
            is.close();

            parseAndSaveWeatherData(file);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ElementValueDeserializer implements JsonDeserializer<List<ElementValue>> {
        @Override
        public List<ElementValue> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            List<ElementValue> elementValues = new ArrayList<>();

            if (json.isJsonArray()) {
                for (JsonElement element : json.getAsJsonArray()) {
                    elementValues.add(new Gson().fromJson(element, ElementValue.class));
                }
            } else {
                elementValues.add(new Gson().fromJson(json, ElementValue.class));
            }

            return elementValues;
        }
    }

    public void parseAndSaveWeatherData(File file) throws ParseException {
        Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<List<ElementValue>>() {
        }.getType(), new ElementValueDeserializer()).create();
        
        //刪除表格內資料
        weatherService.truncateWeatherData();
        
        try {
            FileReader reader = new FileReader(file);
            WeatherData weatherData = gson.fromJson(reader, WeatherData.class);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            if (weatherData != null && weatherData.cwaopendata != null && weatherData.cwaopendata.dataset != null) {
                for (Location location : weatherData.cwaopendata.dataset.locations.location) {
                    for (WeatherElement weatherElement : location.weatherElement) {

                        if (weatherElement.elementName.equals("MaxT")) {
                            for (Time time : weatherElement.time) {
                                for (ElementValue value : time.elementValue) {
                                    String locName = location.locationName;
                                    Date wxDate = new Date(dateFormat.parse(time.startTime.substring(0, 10)).getTime());
                                    String hTemp = value.value;

                                    weatherService.saveWeatherData(locName, wxDate, hTemp, null, null, null, null,
                                            null);
                                }
                            }
                        }

                        if (weatherElement.elementName.equals("MinT")) {
                            for (Time time : weatherElement.time) {
                                for (ElementValue value : time.elementValue) {
                                    String locName = location.locationName;
                                    Date wxDate = new Date(dateFormat.parse(time.startTime.substring(0, 10)).getTime());
                                    String mTemp = value.value;

                                    weatherService.saveWeatherData(locName, wxDate, null, mTemp, null, null, null,
                                            null);
                                }
                            }
                        }

                        if (weatherElement.elementName.equals("PoP24h")) {
                            for (Time time : weatherElement.time) {
                                for (ElementValue value : time.elementValue) {
                                    String locName = location.locationName;
                                    Date wxDate = new Date(dateFormat.parse(time.startTime.substring(0, 10)).getTime());
                                    String rainRate = value.value;

                                    weatherService.saveWeatherData(locName, wxDate, null, null, rainRate, null, null,
                                            null);
                                }
                            }
                        }

                        if (weatherElement.elementName.equals("Wx")) {
                            for (Time time : weatherElement.time) {
                                ElementValue value = time.elementValue.get(0);

                                String locName = location.locationName;
                                Date wxDate = new Date(dateFormat.parse(time.startTime.substring(0, 10)).getTime());
                                String wX = value.value;

                                weatherService.saveWeatherData(locName, wxDate, null, null, null, wX, null, null);
                            }
                        }

                        if (weatherElement.elementName.equals("UVI")) {
                            for (Time time : weatherElement.time) {
                                String locName = location.locationName;
                                Date wxDate = new Date(dateFormat.parse(time.startTime.substring(0, 10)).getTime());

                                ElementValue value0 = time.elementValue.get(0);
                                String uviRate = value0.value;

                                ElementValue value1 = time.elementValue.get(1);
                                String uviDesc = value1.value;

                                weatherService.saveWeatherData(locName, wxDate, null, null, null, null, uviRate,
                                        uviDesc);

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("解析和保存数据时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    class WeatherData {
        @SerializedName("cwaopendata")
        public CwaOpenData cwaopendata;
    }

    class CwaOpenData {
        @SerializedName("dataset")
        public Dataset dataset;
    }

    class Dataset {
        @SerializedName("locations")
        public Locations locations;
    }

    class Locations {
        @SerializedName("locationName")
        public String locationName;

        @SerializedName("location")
        public List<Location> location;
    }

    class Location {
        @SerializedName("locationName")
        public String locationName;

        @SerializedName("weatherElement")
        public List<WeatherElement> weatherElement;
    }

    class WeatherElement {
        @SerializedName("elementName")
        public String elementName;

        @SerializedName("description")
        public String description;

        @SerializedName("time")
        public List<Time> time;
    }

    class Time {
        @SerializedName("startTime")
        public String startTime;

        @SerializedName("endTime")
        public String endTime;

        @SerializedName("elementValue")
        public List<ElementValue> elementValue;
    }

    class ElementValue {
        @SerializedName("value")
        public String value;

        @SerializedName("measures")
        public String measures;
    }
}