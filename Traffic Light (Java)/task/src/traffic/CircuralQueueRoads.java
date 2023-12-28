package traffic;

class CircuralQueueRoads {
    private Road[] table;
    int front;
    int rear;
    int activeRoads;

    public CircuralQueueRoads(int length) {
        table = new Road[length];
        front = -1;
        rear = -1;
        activeRoads = 0;
    }

    public Boolean enqueue(String newRoadName) {
        int newIdx = (rear + 1) % table.length;
        if (newIdx == front) {
            return false;
        } else if (isEmpty()) {
            front++;

        }
        table[newIdx] = new Road(newRoadName, new Cycle(activeRoads == 0));
        rear = newIdx;
        activeRoads++;
        return true;
    }

    public Road dequeue() {
        if (isEmpty()) {
            return null;
        }
        Road tmp = table[front];
        if (table[front].getCycle().isOpen()) {
            synchronizeWhenFrontNeedsToBeDeleted(front);
        }
        table[front] = null;
        front = (front + 1) % table.length;
        if (table[front] == null && table[rear] == null) {
            front = -1;
            rear = -1;
        }
        activeRoads--;
        return tmp;
    }

    private void synchronizeWhenFrontNeedsToBeDeleted(int front) {
        Road road = table[getNextIndex(front)];
        if (road != null) {
            road.getCycle().setOpen(true);
        }
    }

    private int getNextIndex(int currIdx) {
        if (currIdx == table.length - 1) {
            int newIdx = 0;
            while (table[newIdx] == null) {
                newIdx++;
            }
            return newIdx;
        }
        return (currIdx + 1) % table.length;
    }

    private boolean isEmpty() {
        return front == -1;
    }

    public int getFront() {
        return front;
    }

    public Road[] getTable() {
        return table;
    }
}