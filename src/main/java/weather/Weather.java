package weather;

class Weather {
    private float minTemp;
    private float maxTemp;
    private float temp;
    private float windSpeed;
    private float windDegree;
    private int clouds;
    private String description;
    private String type;
    private int humidity;
    private String currentLocation;

    int getHumidity() {
        return humidity;
    }

    void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    float getMinTemp() {
        return minTemp;
    }

    void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }

    float getMaxTemp() {
        return maxTemp;
    }

    void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }

    float getTemp() {
        return temp;
    }

    void setTemp(float temp) {
        this.temp = temp;
    }

    float getWindSpeed() {
        return windSpeed;
    }

    void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    float getWindDegree() {
        return windDegree;
    }

    void setWindDegree(float windDegree) {
        this.windDegree = windDegree;
    }

    int getClouds() {
        return clouds;
    }

    void setClouds(int clouds) {
        this.clouds = clouds;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    String getCurrentLocation() {
        return currentLocation;
    }

    void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }
}
