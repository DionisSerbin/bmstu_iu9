package ru.bmstu.iu9;

import java.io.Serializable;

public class StatisticDelay  implements Serializable {

    private int countAllFlights;
    private int countCancelledFlights;
    private int countDelayedFlights;
    private float maxDelayed;
    private static final float FLOAT_HUNDRED = 100.0f;

    public StatisticDelay(){}

    public StatisticDelay(int countAllFlights, int countCancelledFlights,
                          int countDelayedFlights, float maxDelayed){

        this.countAllFlights = countAllFlights;
        this.countCancelledFlights = countCancelledFlights;
        this.countDelayedFlights = countDelayedFlights;
        this.maxDelayed = maxDelayed;
    }

    public int getCountAllFlights(){
        return countAllFlights;
    }

    public int getCountCancelledFlights(){
        return countCancelledFlights;
    }

    public int getCountDelayedFlights(){
        return countDelayedFlights;
    }

    public float getMaxDelayed(){
        return maxDelayed;
    }

    public void setCountAllFlights(int countAllFlights) {
        this.countAllFlights = countAllFlights;
    }

    public void setCountCancelledFlights(int countCancelledFlights) {
        this.countCancelledFlights = countCancelledFlights;
    }

    public void setCountDelayedFlights(int countDelayedFlights) {
        this.countDelayedFlights = countDelayedFlights;
    }

    public void setMaxDelayed(float maxDelayed) {
        this.maxDelayed = maxDelayed;
    }

    public static StatisticDelay addStatistic(StatisticDelay object1,
                                              StatisticDelay object2){
        return new StatisticDelay(
                object1.getCountAllFlights() + object2.getCountAllFlights(),
                object1.getCountCancelledFlights() + object2.getCountCancelledFlights(),
                object1.getCountDelayedFlights() + object2.getCountDelayedFlights(),
                object1.getMaxDelayed() + object2.getMaxDelayed()
                );

    }

    public static StatisticDelay addInStaticDelay(StatisticDelay object,
                                                  boolean cancelled, boolean delayed, float delayTime){
        int newCountFlights = object.getCountAllFlights() + 1;
        int newCountCancelledFLights = object.getCountCancelledFlights();
        int newCountDelayedFlights = object.getCountDelayedFlights();
        float newMaxDelayed;

        if(cancelled){
            newCountCancelledFLights++;
        }

        if(delayed){
            newCountDelayedFlights++;
        }

        if(delayTime > object.getMaxDelayed()){
            newMaxDelayed = delayTime;
        } else {
            newMaxDelayed = object.getMaxDelayed();
        }

        return new StatisticDelay(newCountFlights, newCountCancelledFLights,
                newCountDelayedFlights, newMaxDelayed);
    }

    public static String outputString(StatisticDelay object){

        float delayedPercent = makePercent((float) object.countDelayedFlights,
                (float) object.countAllFlights);

        float cancelledPercent = makePercent((float) object.countCancelledFlights,
                (float) object.countAllFlights);

        String output = "\n Max delay: " + object.maxDelayed +
                "\n Cancelled flights: " + cancelledPercent + "%" +
                "\n Delayed flights: " + delayedPercent + "%\n";

        return output;
    }

    public static float makePercent(float countThisFlights, float countAllFlights){
        return countThisFlights / countAllFlights * FLOAT_HUNDRED;
    }

}
