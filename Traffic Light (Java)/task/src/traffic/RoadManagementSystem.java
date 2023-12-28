package traffic;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class RoadManagementSystem {
    private Integer roads;
    private Integer interval;
    private Scanner scanner = new Scanner(System.in);
    private Thread t1 = new QueueThread(this);
    private AtomicBoolean inSystemState = new AtomicBoolean(false);
    private CircuralQueueRoads dataQueue;

    public RoadManagementSystem() {
    }

    public Integer getInterval() {
        return this.interval;
    }

    public Integer getRoads() {
        return this.roads;
    }

    public AtomicBoolean getInSystemState() {
        return inSystemState;
    }

    public CircuralQueueRoads getDataQueue() {
        return dataQueue;
    }

    public void startSystem() throws IOException {
        System.out.println("Welcome to the traffic management system!");
        getInitialValues();
        initializeObjects();

        while (true) {
            showMenu();
            switch (getInput(scanner.nextLine())) {
                case 1:
                    addRoad();
                    System.in.read();
                    break;
                case 2:
                    deleteRoad();
                    System.in.read();
                    break;
                case 3:
                    inSystemState.set(true);
                    if (scanner.nextLine().equals("")) {
                        inSystemState.set(false);
                    }
                    break;
                case 0:
                    t1.interrupt();
                    try {
                        t1.join();
                    } catch (InterruptedException e) {

                    }
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Incorrect option");
                    System.in.read();
                    break;
            }


        }
    }

    private void initializeObjects() {
        t1.setName("QueueThread");
        t1.start();
        dataQueue = new CircuralQueueRoads(roads);
    }

    private int getInput(String next) {
        int res = 0;
        try {
            res = Integer.parseInt(next);
            if (res < 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            return 99;
        }

        return res;

    }

    public static void clear() {
        try {
            var clearCommand = System.getProperty("os.name")
                    .contains("Windows") ?
                    new ProcessBuilder("cmd", "/c", "cls") :
                    new ProcessBuilder("clear");
            clearCommand.inheritIO()
                    .start()
                    .waitFor();
        } catch (IOException | InterruptedException e) {
        }
    }

    private void deleteRoad() {
        Road roadToDelete = dataQueue.dequeue();
        if (roadToDelete == null) {
            System.out.println("Queue is empty!");
            return;
        }
        System.out.printf("%s deleted", roadToDelete.getName());
    }

    private void addRoad() throws IOException {
        System.out.println("Input road name:");
        String newRoad = scanner.nextLine();
        if (!dataQueue.enqueue(newRoad)) {
            System.out.println("Queue is full!");
            return;
        }
        System.out.printf("%s Added!", newRoad);
    }

    private int getUserInput(String prompt) {
        int value = 0;
        boolean isValid = false;
        System.out.println(prompt);
        while (!isValid) {
            Scanner scanner = new Scanner(System.in);
            try {
                value = scanner.nextInt();
                if (value <= 0) {
                    throw new Exception();
                }
                isValid = true;
            } catch (Exception e) {
                System.out.println("Error! Incorrect Input. Try again:");
            }
        }
        return value;
    }

    private void getInitialValues() {
        roads = getUserInput("Input the number of roads:");
        interval = getUserInput("Input the interval:");
    }

    private void showMenu() {
        System.out.println("Menu:");
        int i = 1;
        for (MenuOption option : MenuOption.values()) {
            System.out.println(option.getNumber() + ".  " + option.getDescription());
            i++;
        }
    }


}
