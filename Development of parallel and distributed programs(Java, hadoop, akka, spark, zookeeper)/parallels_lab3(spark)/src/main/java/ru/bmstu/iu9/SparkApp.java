package ru.bmstu.iu9;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Map;

public class SparkApp {

    private static final String CODE_STR = "Code";
    private static final String YEAR_STR = "YEAR";
    private static final String QUOTE = "\"";
    private static final String NULL_STR = "";
    private static final String COMMA = ",";
    private static final String AIRPORTS_FILE = "L_AIRPORT_ID.csv";
    private static final String FLIGHTS_FILE = "664600583_T_ONTIME_sample.csv";
    private static final int COLUMN_NUMBER_AIRPORT_ID_FROM = 11;
    private static final int COLUMN_NUMBER_AIRPORT_ID_TO = 14;
    private static final int COLUMN_DELAY_TIME = 18;
    private static final int COLUMN_NUMBER_CANCALLED = 19;
    private static final float FLOAT_ZERO = 0.0f;
    private static final float FLOAT_ONE = 1.0f;

    private static JavaRDD<String> readFlightsFromFiles(JavaSparkContext sc){
        return sc.
                textFile(FLIGHTS_FILE);

    }

    private static JavaRDD<String> readAirportsFromFiles(JavaSparkContext sc){
        return sc.
                textFile(AIRPORTS_FILE);

    }

    private static JavaPairRDD<Integer, String> parseAirports (JavaRDD<String> airportsFile){
        return airportsFile.
                filter(s -> !s.contains(CODE_STR)).
                mapToPair(s -> {
                            s = s.replace(QUOTE, NULL_STR);
                            int firstCommaIndex = s.indexOf(COMMA);
                            return new Tuple2<>(
                                    Integer.valueOf(
                                            s.substring(0, firstCommaIndex)
                                    ),
                                    s.substring(firstCommaIndex + 1, s.length())
                            );
                        }
                );
    }

    private static JavaPairRDD<Tuple2<Integer, Integer>, FlightsSerializable> parseFlights(
            JavaRDD<String> flightsFile){
        return flightsFile.
                filter(s -> !s.contains(YEAR_STR)).
                mapToPair(s -> {
                            String[] columns = s.split(COMMA);
                            int airportIDFrom = Integer.parseInt(
                                    columns[COLUMN_NUMBER_AIRPORT_ID_FROM]);
                            int airportIDTo = Integer.parseInt(
                                    columns[COLUMN_NUMBER_AIRPORT_ID_TO]);
                            String delayTimeString = columns[COLUMN_DELAY_TIME];
                            float delayTime;

                            if(delayTimeString.equals(NULL_STR)){
                                delayTime = FLOAT_ZERO;
                            } else {
                                delayTime = Float.parseFloat(delayTimeString);
                            }

                            float cancelled = Float.parseFloat(
                                    columns[COLUMN_NUMBER_CANCALLED]);
                            return new Tuple2<>(
                                    new Tuple2<>(
                                            airportIDFrom,
                                            airportIDTo
                                    ),
                                    new FlightsSerializable(
                                            airportIDFrom,
                                            airportIDTo,
                                            delayTime,
                                            cancelled
                                    )
                            );
                        }
                );
    }

    private static JavaPairRDD<Tuple2<Integer, Integer>, String> combineDataByKey(
            JavaPairRDD<Tuple2<Integer, Integer>, FlightsSerializable> dataFlights){

        return dataFlights.
                combineByKey(
                        p -> new StatisticDelay(1,
                                p.getCancelled() == FLOAT_ONE ? 1 : 0,
                                p.getDelayTime() >  FLOAT_ZERO ? 1 : 0,
                                p.getDelayTime()),
                        (statisticDelay,p) -> StatisticDelay.addInStaticDelay(
                                statisticDelay,
                                p.getCancelled() == FLOAT_ONE,
                                p.getDelayTime() > FLOAT_ZERO,
                                p.getDelayTime()
                        ),
                        StatisticDelay::addStatistic)
                .mapToPair(
                        a-> new Tuple2<>(
                                a._1(),
                                StatisticDelay.outputString(a._2())
                        )
                );

    }

    private static JavaRDD<String> toStringResult (
            JavaPairRDD<Tuple2<Integer, Integer>, String> dataFlightsCombinedByKey,
            final Broadcast<Map<Integer, String>> broadcastedAirports){

        return dataFlightsCombinedByKey.
                map(a -> {
                    Map<Integer, String> airportsAllID = broadcastedAirports.value();
                    Tuple2<Integer, Integer> key = a._1();
                    String value = a._2();

                    String airportNameFrom = airportsAllID.get(key._1());
                    String airportNameTo = airportsAllID.get(key._2());

                    return "From " + airportNameFrom +
                            " -> " +
                            "To " + airportNameTo +
                            ":" + value;
                });

    }

    public static void main(String[] args){

        SparkConf conf = new SparkConf().setAppName("lab3 Spark App");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> flightsFile = readFlightsFromFiles(sc);

        JavaRDD<String> airportsFile = readAirportsFromFiles(sc);

        JavaPairRDD<
                Integer,
                String
                > dataAirport = parseAirports(airportsFile);

        final Broadcast<
                Map<
                        Integer,
                        String
                        >
                > broadcastedAirports = sc.broadcast(
                                dataAirport.collectAsMap()
        );

        JavaPairRDD<
                Tuple2<
                        Integer,
                        Integer
                        >,
                FlightsSerializable
                > dataFlights = parseFlights(flightsFile);

        JavaPairRDD<
                Tuple2<
                        Integer,
                        Integer
                        >,
                String
                > dataFlightsCombinedByKey = combineDataByKey(dataFlights);

        JavaRDD<String> result = toStringResult(
                dataFlightsCombinedByKey,
                broadcastedAirports
        );

        result.saveAsTextFile("hdfs://localhost:9000/user/denisserbin/output");
    }
}
