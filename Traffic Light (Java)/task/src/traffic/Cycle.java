package traffic;

public class Cycle {
    private int remainingTime;
    private boolean isOpen;

    public Cycle(boolean isOpen) {
        this.remainingTime = -1;
        this.isOpen = isOpen;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
