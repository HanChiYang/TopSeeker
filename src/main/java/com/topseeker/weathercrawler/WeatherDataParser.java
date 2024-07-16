package com.topseeker.weathercrawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
public class WeatherDataParser implements CommandLineRunner {

	@Autowired
	private WeatherService weatherService;

	public static void main(String[] args) throws Exception {
		new WeatherDataParser().run();
	}

	@Override
	public void run(String... args) throws Exception {
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

			System.out.println(filename + " 下載中");

			int length = 0;
			byte[] b = new byte[4096];
			while ((length = is.read(b)) != -1) {
				fos.write(b, 0, length);
				fos.flush();
			}

			fos.close();
			is.close();
			System.out.println(filename + " Done!");

			// 解析并保存 JSON 数据
			parseAndSaveWeatherData(file);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
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

		try {
			FileReader reader = new FileReader(file);
			WeatherData weatherData = gson.fromJson(reader, WeatherData.class);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			if (weatherData != null && weatherData.cwaopendata != null && weatherData.cwaopendata.dataset != null) {
				for (Location location : weatherData.cwaopendata.dataset.locations.location) {
					for (WeatherElement weatherElement : location.weatherElement) {
						if (weatherElement.elementName.equals("MaxT")) {
							for (Time time : weatherElement.time) {
								String locName = location.locationName;
								Date wxDate = new Date(dateFormat.parse(time.startTime.substring(0, 10)).getTime());

								
								for (ElementValue value : time.elementValue) {
//									value = time.elementValue.get(0).value;  // 取第一个元素的 value
									System.out.println("Location: " + locName);
									System.out.println("Time: " + wxDate);
									System.out.println("Highest Temp: " + value.value);
								}
							}
						}
						if (weatherElement.elementName.equals("MinT")) {

							for (Time time : weatherElement.time) {
								String locName = location.locationName;
								Date wxDate = new Date(dateFormat.parse(time.startTime.substring(0, 10)).getTime());

								for (ElementValue value : time.elementValue) {
									System.out.println("Location: " + locName);
									System.out.println("Time: " + wxDate);
									System.out.println("Lowest Temp: " + value.value);
								}
							}
						}
						if (weatherElement.elementName.equals("PoP24h")) {
							
							for (Time time : weatherElement.time) {
								String locName = location.locationName;
								Date wxDate = new Date(dateFormat.parse(time.startTime.substring(0, 10)).getTime());
								
								for (ElementValue value : time.elementValue) {
									System.out.println("Location: " + locName);
									System.out.println("Time: " + wxDate);
									System.out.println("PoP24h: " + value.value);
								}
							}
						}
						if (weatherElement.elementName.equals("UVI")) {
							
							for (Time time : weatherElement.time) {
								String locName = location.locationName;
								Date wxDate = new Date(dateFormat.parse(time.startTime.substring(0, 10)).getTime());
								
								for (ElementValue value : time.elementValue) {
									System.out.println("Location: " + locName);
									System.out.println("Time: " + wxDate);
									System.out.println("UVI: " + value.value);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
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
