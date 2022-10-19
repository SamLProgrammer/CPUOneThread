package models;

public class CPU {

    private Process currentProcess;
    private Process previousProcess;
    private CPUStatus status;

    public CPU() {
        release();
    }

    public Process process(Process process) {
        getBusy();
        currentProcess = process;
        currentProcess.setQuantum((currentProcess != previousProcess && previousProcess != null) ? 3 : currentProcess.getQuantum());
        currentProcess.execute();
        currentProcess.decreaseOnRunning();
        currentProcess.defineStatus();
        return currentProcess;
    }

    private void getBusy() {
        status = CPUStatus.BUSY;
    }

    public boolean isBusy() {
        return status == CPUStatus.BUSY;
    }

    public void release() {
        status = CPUStatus.IDLE;
        previousProcess = currentProcess;
        currentProcess = null;
    }

    public CPUStatus getStatus() {
        return status;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    private CPU myInstance() {
        return this;
    }

    @Override
    public String toString() {
        return "CPU{" +
                "Status=" + status +
                ((currentProcess == null) ? "" : ", Current Process:" + currentProcess.toString()) +
                '}';
    }

}
