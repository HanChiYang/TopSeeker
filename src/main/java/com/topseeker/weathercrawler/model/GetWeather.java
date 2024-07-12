package com.topseeker.weathercrawler.model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetWeather {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            // 读取JSON文件
            JsonNode rootNode = mapper.readTree(new File("path/to/your/json/file.json"));
            JsonNode locations = rootNode.path("cwbopendata").path("dataset").path("location");
            
            Map<String, Map<String, Temperature>> weatherData = new HashMap<>();
            
            for (JsonNode location : locations) {
                String locationName = location.path("locationName").asText();
                JsonNode weatherElements = location.path("weatherElement");
                
                Map<String, Temperature> tempMap = new HashMap<>();
                
                for (JsonNode element : weatherElements) {
                    String elementName = element.path("elementName").asText();
                    if (elementName.equals("MaxT") || elementName.equals("MinT")) {
                        for (JsonNode timeNode : element.path("time")) {
                            String date = timeNode.path("startTime").asText().substring(0, 10);
                            int temperature = timeNode.path("parameter").path("parameterName").asInt();
                            
                            Temperature temp = tempMap.getOrDefault(date, new Temperature());
                            if (elementName.equals("MaxT")) {
                                temp.setMaxTemp(temperature);
                            } else if (elementName.equals("MinT")) {
                                temp.setMinTemp(temperature);
                            }
                            tempMap.put(date, temp);
                        }
                    }
                }
                
                weatherData.put(locationName, tempMap);
            }
            
            // 打印結果
            for (String location : weatherData.keySet()) {
                System.out.println("Location: " + location);
                Map<String, Temperature> temps = weatherData.get(location);
                for (String date : temps.keySet()) {
                    Temperature temp = temps.get(date);
                    System.out.println("Date: " + date + ", MaxTemp: " + temp.getMaxTemp() + "C, MinTemp: " + temp.getMinTemp() + "C");
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Temperature {
    private int maxTemp;
    private int minTemp;

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }
}
