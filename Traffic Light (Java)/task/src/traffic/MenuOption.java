package traffic;

public enum MenuOption {
    ADD(1, "Add"),
    DELETE(2, "Delete"),
    SYSTEM(3, "System"),
    QUIT(0, "Quit");

    private final int number;
    private final String description;

    MenuOption(int number, String description) {
        this.number = number;
        this.description = description;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }
}