package com.example.weatherapponmidsemester;

import org.json.JSONObject;

public class Weather {

    private int humidity;
    private int pressure;
    private float temp_max;
    private float temp_min;
    private float temp;
    private float windSpeed;


    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public float getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(float temp_max) {
        this.temp_max = temp_max;
    }

    public float getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(float temp_min) {
        this.temp_min = temp_min;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }



    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWeatherDescription()
    {
        String weatherDescription = "";
        weatherDescription += "Humidity : " + humidity + "\n";
        weatherDescription += "Atmospheric Pressure : " + pressure + "\n";
        weatherDescription += "Max Temperature : " + temp_max + "\n";
        weatherDescription += "Min Temperature : " + temp_min + "\n";
        weatherDescription += "Current Temperature: " + temp + "\n";
        weatherDescription += "Wind Speed: " + windSpeed + "\n";
        return weatherDescription;
    }



}
