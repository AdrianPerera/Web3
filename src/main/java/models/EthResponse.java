package models;

public class EthResponse {


    private int fast;
    private int fastest;
    private int safeLow;
    private int average;
    private float blockTime;
    private int blockNum;
    private float speed;
    private float safeLowWait;
    private float avgWait;
    private float fastWait;
    private float fastestWait;

    public int getFast() {
        return fast;
    }

    public void setFast(int fast) {
        this.fast = fast;
    }

    public int getFastest() {
        return fastest;
    }

    public void setFastest(int fastest) {
        this.fastest = fastest;
    }

    public int getSafeLow() {
        return safeLow;
    }

    public void setSafeLow(int safeLow) {
        this.safeLow = safeLow;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public float getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(float blockTime) {
        this.blockTime = blockTime;
    }

    public int getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(int blockNum) {
        this.blockNum = blockNum;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSafeLowWait() {
        return safeLowWait;
    }

    public void setSafeLowWait(float safeLowWait) {
        this.safeLowWait = safeLowWait;
    }

    public float getAvgWait() {
        return avgWait;
    }

    public void setAvgWait(float avgWait) {
        this.avgWait = avgWait;
    }

    public float getFastWait() {
        return fastWait;
    }

    public void setFastWait(float fastWait) {
        this.fastWait = fastWait;
    }

    public float getFastestWait() {
        return fastestWait;
    }

    public void setFastestWait(float fastestWait) {
        this.fastestWait = fastestWait;
    }
}
