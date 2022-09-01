package ru.bmstu.iu9;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class AirportGroupingComparator extends WritableComparator {

    protected AirportGroupingComparator(){
        super(AirportWritableComparable.class, true);
    }

    @Override
    public int compare(WritableComparable first, WritableComparable second) {
        AirportWritableComparable firstAirportId = (AirportWritableComparable) first;
        AirportWritableComparable secondAirportId = (AirportWritableComparable) second;
        return firstAirportId.getAirportId().compareTo(
                secondAirportId.getAirportId()
        );
    }
}
