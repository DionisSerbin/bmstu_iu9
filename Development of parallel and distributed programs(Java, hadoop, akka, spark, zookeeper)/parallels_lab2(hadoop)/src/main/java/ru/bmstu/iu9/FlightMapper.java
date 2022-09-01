package ru.bmstu.iu9;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FlightMapper extends Mapper<LongWritable, Text, AirportWritableComparable, Text> {
    private static final int DEST_AIRPORT_ID = 14;
    private static final int DELAY_NUMBER = 18;
    private static final String POINT = ",";
    private static final String DEST_AIRPORT_COLUMN_NAME = "\"DEST_AIRPORT_ID\"";
    private static final int DATA_INDICATOR = 1;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException,
            InterruptedException {
        String line = value.toString();
        String[] column = line.split(POINT);
        String airportIDString = column[DEST_AIRPORT_ID];

        if(!airportIDString.equals(DEST_AIRPORT_COLUMN_NAME)){

            int airportId = Integer.parseInt(airportIDString);
            String delay = column[DELAY_NUMBER];
            if (!delay.isEmpty()) {
                context.write(
                        new AirportWritableComparable(
                                new IntWritable(airportId),
                                new IntWritable(DATA_INDICATOR)),
                        new Text(delay));
            }

        }
    }
}

