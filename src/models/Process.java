package models;

public class Process {

    private int initialNextIOTime;
    private int nextIOTime;
    private int initialLifeTime;
    private int lifeTime;
    private int initialBlockedTime;
    private int blockedTime;
    private int quantum;
    private boolean unlocked;
    private ProcessStatus status;
    private String processName;

    public Process(String processName, RandomEngine randomEngine) {
        status = ProcessStatus.READY;
        this.processName = processName;
        nextIOTime = initialNextIOTime =  randomEngine.randomBetween(1,5);
        lifeTime = initialLifeTime = randomEngine.randomBetween(1, 20);
        blockedTime = initialBlockedTime = randomEngine.randomBetween(1,4);
        quantum = 3;
    }

    // ============================================================== UTILITY ==============================================================

    public void defineStatus() { // CORE
        if(lifeTime == 0) {
            status = ProcessStatus.KILLED;
        } else if (!unlocked && blockedTime == 0) {
            status = ProcessStatus.READY;
            unlocked = true;
        } else if (!unlocked && nextIOTime == 0) {
            status = ProcessStatus.BLOCKED;
        } else if (quantum == 0){
            status = ProcessStatus.READY;
        }
    }

    // ============================================================== DECREASERS ==============================================================

    public void decreaseLifeTime() {
        lifeTime--;
    }

    public void decreaseNextIOTime() {
        nextIOTime--;
    }

    public void decreaseBlockedTime() {
        blockedTime--;
    }

    public void decreaseQuantum() {
        quantum--;
    }

    public void decreaseOnRunning() {
        decreaseQuantum();
        decreaseLifeTime();
        decreaseNextIOTime();
    }

    // ============================================================== GETTERS && SETTERS ==============================================================

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public boolean isRunning() {
        return status == ProcessStatus.RUNNING;
    }

    public void execute() {
        status = ProcessStatus.RUNNING;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Process{" +
                "ProcessName = " + processName +
                " NextIOTime= " + nextIOTime + "/" + initialNextIOTime +
                ", lifeTime= " + lifeTime + "/" + initialLifeTime +
                ", BlockedTime= " + blockedTime + "/"+ initialBlockedTime +
                ", quantum= " + quantum +
                ", status= " + status +
                '}';
    }

    public int getQuantum() {
        return quantum;
    }
}
