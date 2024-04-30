# Currency Converter
This is a simple currency converter that converts one currency to another. It uses the [ExchangeRate-API](https://www.exchangerate-api.com/) to get the exchange rates.

## Grafana and InfluxDB

Grafana shows a simple dashboard with a few metrics collected from the app. The metrics are stored in InfluxDB.

![Grafana](pics/Grafana.png)

### How to run

1. Start the InfluxDB and Grafana containers:
```bash
$ docker run -d -p 8086:8086 --name=influxdb influxdb:latest
$ docker run -d -p 3000:3000 --name=grafana grafana/grafana-enterprise
```
2. Create a Docker to connect the InfluxDB and Grafana containers:
```bash
$ docker network create influxGrafana
$ docker network connect influxGrafana grafana
$ docker network connect influxGrafana influxdb
```
3. Go to http://localhost:8086 and setup InfluxDB. Default values for the setup are:
    - Bucket: myBucket
    - Organization: currencyConvertor
    - Do not forget to add API token to the application.properties file.
    - Lookup the InfluxDB endpoint available for the Grafana container:
```bash
$ docker network inspect influxGrafana
// ...
            "e45b57fed441a3c024a1a3b69eee4991a38b3b36f33d51e5b6a19427318b3953": {
                "Name": "influxdb",
                "EndpointID": "65b43d2ace2f331cd824384c16e6cd29091c6bb7a664e90ffc8c55591101e921",
                "MacAddress": "02:42:ac:12:00:03",
                "IPv4Address": "172.18.0.3/16",
 // ...
```
In the example above, the InfluxDB IP is **172.18.0.3**
4. Go to http://localhost:3000 and setup Grafana:
    - Create a connector to InfluxDB
    - Make sure to choose the **Flux** query language 
    - No Basic Auth
    - Fill InfluxDB Details
    - ![GrafanaFluxConnector](pics/GrafanaFluxConnector.png)
    - Import the dashboard from `monitoring/grafana.json`
    - Note the DataSource UID (`ddkbsngx09ddsa`) needs to be updated in the JSON file
5. Set the correct values in the `application.properties` file, e.g.:
```properties
influxdb.token=Q5eo_o....
influxdb.bucket=myBucket
influxdb.org=currencyConvertor
influxdb.url=http://localhost:8086?timeout=5000&logLevel=BASIC
```
6. Run the application and check the metrics in Grafana.
