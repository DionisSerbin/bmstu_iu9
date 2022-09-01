package ru.bmstu.iu9;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class AirportPartitioner extends Partitioner<AirportWritableComparable, Text> {

    @Override
    public int getPartition(AirportWritableComparable airportWritableComparable, Text text, int amountReducers) {
        int returned = Math.abs(airportWritableComparable.getAirportId().hashCode());
        return returned % amountReducers;
    }
}
