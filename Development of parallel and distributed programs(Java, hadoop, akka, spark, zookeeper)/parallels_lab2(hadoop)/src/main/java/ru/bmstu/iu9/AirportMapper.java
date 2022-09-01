package ru.bmstu.iu9;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AirportMapper extends Mapper<LongWritable, Text, AirportWritableComparable, Text> {

    private static final String CODE = "Code";
    private static final String POINT = ",";
    private static final String QUOTES = "\"";
    private static final String NULL_STR = "";
    private static final int LIMIT_POINT = 2;
    private static final int AIRPORT_DATA_INDEX = 0;
    private static final int AIRPORT_NAME_INDEX = 1;
    private static final int DATA_INDICATOR = 0;

    private static String removeQuotes(final String data){
        return data.replaceAll(QUOTES, NULL_STR);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] column = line.split(POINT, LIMIT_POINT);

        String airportId = removeQuotes(column[AIRPORT_DATA_INDEX]);
        String airportName = removeQuotes(column[AIRPORT_NAME_INDEX]);

        if(!(airportId.equals(CODE))){
            int airportIdParse = Integer.parseInt(airportId);
            context.write(
                new AirportWritableComparable(
                    new IntWritable(airportIdParse),
                    new IntWritable(DATA_INDICATOR)
                ),
                new Text(airportName)
            );
        }
    }
}
