import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

class DynamicTrafficDelayTime {
    static final char poles[] = { 'A', 'B', 'C', 'D' };
    static final int avgVahicledimensions = 84; // considering only 4 wheel vehicles
    static char activePole = 'A';
    static int delayTime = 0;
    static int timerCounter = 0;

    static void printOutput(char pole, String poleState, int delayTime) {
        System.out.println("Pole: " + pole + " | State: " + poleState + " | Wait time: " + delayTime + " sec");
    }

    static int getRandomNumber(int maxRange) {
        int randNum = new Random().ints(1, 1, maxRange).findFirst().getAsInt();
        return randNum;
    }

    static int getVehiclesCountInPole(int sensorPosition) {
        // values are considered in feet
        int mockRoadWidth = 30, eachSensorDistance = 15;
        int maxVehicleCount = ((eachSensorDistance * sensorPosition * mockRoadWidth) / avgVahicledimensions);

        int vehiclesCountInPole = getRandomNumber(maxVehicleCount);
        return vehiclesCountInPole;
    }

    static int calculateDelayTime() {
        int sensorPosition = getRandomNumber(4);
        int vehiclesInRoad = getVehiclesCountInPole(sensorPosition);
        int roadOccupiedSpace = vehiclesInRoad * avgVahicledimensions;
        int avgVahicleSignalCrossTime = 4; // value is in seconds
        int spaceGetEmptyInEachSeconds = avgVahicledimensions / avgVahicleSignalCrossTime;
        int delayTime = roadOccupiedSpace / spaceGetEmptyInEachSeconds;
        return delayTime;
    }

    static String getPoleState(char pole) {
        String poleState = (pole == activePole) ? "Green" : "Red";
        return poleState;
    }

    static int getPoleDelayTime(char pole) {
        int poleDelayTime, poleValue = (int) pole, activePoleVal = (int) activePole;

        if (pole == activePole) {
            poleDelayTime = 0;
        } else {
            poleDelayTime = calculateDelayTime();
            if ((activePoleVal + 1) == poleValue || activePoleVal > 68) {
                delayTime = poleDelayTime;
            }
        }

        return poleDelayTime;
    }

    static void mainFunction() {
        int activePoleVal = (int) activePole;
        for (char pole : poles) {
            int poleValue = (int) pole;
            int poleDelayTime = getPoleDelayTime(pole);
            String poleState = getPoleState(pole);
            printOutput(pole, poleState, poleDelayTime);

            if ((activePoleVal + 1) == poleValue || activePoleVal >= 68) {
                setDelayTimer();
            }
        }
        activePoleVal += 1;
        if (activePoleVal > 68) {
            activePoleVal = 65;
        }
        activePole = (char) activePoleVal;
    }

    static void setDelayTimer() {
        Timer ParentTimer = new Timer();
        ParentTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Timer childTimer = new Timer();
                childTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("delayTime: " + delayTime);
                        if (delayTime == 0) {
                            mainFunction();
                            childTimer.cancel();
                        } else
                            delayTime--;

                    }
                }, 0, 1000);
                timerCounter++;
                if (timerCounter >= 5) {
                    ParentTimer.cancel();

                }
            }
        }, 0, delayTime * 1000);
    }

    public static void main(String[] args) {
        mainFunction();
    }

}