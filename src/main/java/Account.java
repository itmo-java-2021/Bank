public class Account {
    private final long id;
    /**
     * количество денег
     */
    private double amount;
    private final long userId;
    private boolean blocked;

    public Account(long id, long userId) {
        this.id = id;
        this.userId = userId;
        this.amount = 0;
        this.blocked = false;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
