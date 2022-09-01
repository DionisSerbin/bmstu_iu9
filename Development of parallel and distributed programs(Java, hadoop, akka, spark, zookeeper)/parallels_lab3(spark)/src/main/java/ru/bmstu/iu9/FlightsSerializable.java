package ru.bmstu.iu9;

import java.io.Serializable;

public class FlightsSerializable implements Serializable {

    private int airportIDFrom;
    private int airportIDTo;
    private float delayTime;
    private float cancelled;

    public FlightsSerializable() {}

    public FlightsSerializable(int airportIDFrom, int airportIDTo,
                               float delayTime, float cancelled){

        this.airportIDFrom = airportIDFrom;
        this.airportIDTo = airportIDTo;
        this.delayTime = delayTime;
        this.cancelled = cancelled;

    }

    public float getDelayTime(){
        return delayTime;
    }

    public float getCancelled(){
        return cancelled;
    }

    public int getAirportIDFrom(){
        return getAirportIDFrom();
    }

    public int getAirportIDTo(){
        return  airportIDTo;
    }

    public void setAirportIDFrom(int airportIDFrom){
        this.airportIDFrom = airportIDFrom;
    }

    public void setAirportIDTo(int airportIDTo){
        this.airportIDTo = airportIDTo;
    }

    public void setDelayTime(float delayTime){
        this.delayTime = delayTime;
    }

    public void setCancelled(float cancelled ){
        this.cancelled = cancelled;
    }

}
