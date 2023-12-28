package traffic;

public class Road {
    private String name;
    private boolean isOpen;
    private Cycle cycle;

    public Road(String name, Cycle cycle) {
        this.name = name;
        this.cycle = cycle;
    }

    public String getName() {
        return name;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }
}
