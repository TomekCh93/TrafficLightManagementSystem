package traffic;

import java.util.concurrent.atomic.AtomicInteger;

class QueueThread extends Thread {
    AtomicInteger timeSinceStartup = new AtomicInteger(0);
    private RoadManagementSystem roadManagementSystem;

    public QueueThread(RoadManagementSystem roadManagementSystem) {
        this.roadManagementSystem = roadManagementSystem;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            timeSinceStartup.incrementAndGet();
            if (roadManagementSystem.getInSystemState().get()) {
                drawStateBoard();
            }
            RoadManagementSystem.clear();
        }
    }

    private void drawStateBoard() {
        updateQueue(false);
        System.out.printf("! %ss. have passed since system startup !%n", timeSinceStartup.get());
        System.out.printf("! Number of roads: %s !%n", roadManagementSystem.getRoads());
        System.out.printf("! Interval: %s !%n", roadManagementSystem.getInterval());
        if (roadManagementSystem.getDataQueue().activeRoads != 0) {
            Road[] table = roadManagementSystem.getDataQueue().getTable();
            int front = roadManagementSystem.getDataQueue().getFront();
            int rear = roadManagementSystem.getDataQueue().rear;
            for (int i = front; true; i = (1 + i) % table.length) {
                if (table[i].getCycle().isOpen()) {
                    System.out.printf("Road %s will be \u001B[32m%s for %ss. \u001B[0m%n", table[i].getName(), getStringFromBool(table[i].getCycle().isOpen()), table[i].getCycle().getRemainingTime());
                } else {
                    System.out.printf("Road %s will be \u001B[31m%s for %ss. \u001B[0m%n", table[i].getName(), getStringFromBool(table[i].getCycle().isOpen()), table[i].getCycle().getRemainingTime());
                }
                if (i == rear) {
                    break;
                }
            }
        }
        System.out.println("! Press \"Enter\" to open menu !");
    }

    private String getStringFromBool(boolean state) {
        return state ? "open" : "closed";
    }

    private void updateQueue(boolean changeFrontCycleMode) {
        Road[] table = roadManagementSystem.getDataQueue().getTable();
        int activeRoads = roadManagementSystem.getDataQueue().activeRoads;
        int frontCycleIdx = findFrontCycle(roadManagementSystem.getDataQueue().getTable());
        int initialCycleLenght = roadManagementSystem.getInterval();
        int frontNextCycleLength = 0;
        synchronizeCycles(changeFrontCycleMode, table, activeRoads, frontCycleIdx, initialCycleLenght, frontNextCycleLength);
    }

    private void synchronizeCycles(boolean changeFrontCycleMode, Road[] table, int activeRoads, int frontCycleIdx, int initialCycleLenght, int frontNextCycleLength) {
        int currIdx = frontCycleIdx;
        while (activeRoads > 0) {
            if (!changeFrontCycleMode && currIdx == frontCycleIdx && table[currIdx].getCycle().getRemainingTime() == 1) {
                table[currIdx].getCycle().setOpen(false);
                if (activeRoads > 1) {
                    table[getNextIndex(currIdx)].getCycle().setOpen(true);
                    updateQueue(true);
                } else {
                    table[currIdx].getCycle().setOpen(!table[currIdx].getCycle().isOpen());
                    table[currIdx].getCycle().setRemainingTime(initialCycleLenght);
                }
                return;
            } else if (currIdx == frontCycleIdx) {
                updateFrontCycle(table[currIdx], initialCycleLenght);

            } else {
                updateCycle(table[currIdx], frontNextCycleLength, frontCycleIdx, initialCycleLenght);
            }
            frontNextCycleLength += initialCycleLenght;
            currIdx = getNextIndex(currIdx);
            activeRoads--;
        }
    }

    private int getNextIndex(int currIdx) {
        if (currIdx == (roadManagementSystem.getDataQueue().getTable().length) - 1) {
            int newIdx = 0;
            while (roadManagementSystem.getDataQueue().getTable()[newIdx] == null) {
                newIdx++;
            }
            return newIdx;
        }
        return (currIdx + 1) % roadManagementSystem.getDataQueue().getTable().length;
    }

    private void updateCycle(Road road, int cycleLenght, int frontCycleIdx, int initialCycleLenght) {
        if (road.getCycle().getRemainingTime() == -1) {
            int timeDiffInFrontCycle = initialCycleLenght - roadManagementSystem.getDataQueue().getTable()[frontCycleIdx].getCycle().getRemainingTime();
            road.getCycle().setRemainingTime(cycleLenght - timeDiffInFrontCycle);
        } else if (road.getCycle().getRemainingTime() == 1) {
            road.getCycle().setRemainingTime(cycleLenght);
        } else {
            road.getCycle().setRemainingTime(road.getCycle().getRemainingTime() - 1);
        }
    }

    private void updateFrontCycle(Road road, int cycleLenght) {
        if (road.getCycle().getRemainingTime() == -1) {
            road.getCycle().setRemainingTime(cycleLenght);
        } else if (road.getCycle().getRemainingTime() == 1) {
            road.getCycle().setRemainingTime(cycleLenght);
        } else {
            road.getCycle().setRemainingTime(road.getCycle().getRemainingTime() - 1);
        }

    }

    private int findFrontCycle(Road[] table) {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && (table[i].getCycle().isOpen())) {
                return i;
            }
        }
        return -1;
    }

}