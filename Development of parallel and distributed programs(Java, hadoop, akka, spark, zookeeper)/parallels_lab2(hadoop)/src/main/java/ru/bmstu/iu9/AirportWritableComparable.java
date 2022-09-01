package ru.bmstu.iu9;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AirportWritableComparable implements WritableComparable<AirportWritableComparable> {

    private final IntWritable airportId;
    private final IntWritable dataIndex;

    protected IntWritable getAirportId(){
        return this.airportId;
    }

    protected IntWritable getDataIndex(){
        return this.dataIndex;
    }

    public AirportWritableComparable(IntWritable airportId, IntWritable dataIndex) {
        this.airportId = airportId;
        this.dataIndex = dataIndex;
    }

    public AirportWritableComparable(){
        airportId = new IntWritable(0);
        dataIndex = new IntWritable(0);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        airportId.write(dataOutput);
        dataIndex.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        airportId.readFields(dataInput);
        dataIndex.readFields(dataInput);
    }

    @Override
    public int compareTo(AirportWritableComparable o) {
        int compareAirportId = this.getAirportId().compareTo(
                o.getAirportId()
        );
        if (compareAirportId == 0){
            return this.getDataIndex().compareTo(
                    o.getDataIndex()
            );
        }
        return compareAirportId;
    }
}
