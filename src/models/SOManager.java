package models;

import java.util.concurrent.CopyOnWriteArrayList;

public class SOManager implements Runnable{

    private CopyOnWriteArrayList<Process> readyQueue;
    private CopyOnWriteArrayList<Process> blockedQueue;
    private RandomEngine randomEngine;
    private int simulationTime;
    private int nextProcessCountDown;
    private int processId;
    private CPU cpu;

    public SOManager() {
        initComponents();
        initSimulation();
    }

    private void initComponents() {
        readyQueue = new CopyOnWriteArrayList<>();
        blockedQueue = new CopyOnWriteArrayList<>();
        randomEngine = new RandomEngine();
        simulationTime = 50;
        nextProcessCountDown = randomEngine.randomBetween(1,4);
        cpu = new CPU();
    }

    @Override
    public void run() {
        while(simulationTime > 0) {
            showGeneralStats();
            boolean launching = false;
            boolean popped = false;
            if(!cpu.isBusy() && readyQueue.size() > 0) {
                handleCPUProcess(cpu.process(popFromReadyQueue()));
                popped = true;
            }
            if(nextProcessCountDown == 0) {
                launching = launchNewProcess(new Process("Process Id: " + (++processId), randomEngine), popped);
                nextProcessCountDown = randomEngine.randomBetween(1,4);
            }
            if(!popped && !launching && cpu.getCurrentProcess() != null) {
                handleCPUProcess(cpu.process(cpu.getCurrentProcess()));
            }
            decreaseSimulationStats();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void decreaseSimulationStats() {
        simulationTime--;
        nextProcessCountDown--;
    }

    private void showGeneralStats() {
        System.out.println("**********************************************************************************************************");
        showStats();
        showCPUStats();
        showQueues();
        System.out.println("**********************************************************************************************************");
    }

    private void showStats() {
        System.out.println("Simulation Time: " + simulationTime);
        System.out.println("Next Process : " + nextProcessCountDown);
    }

    private void showCPUStats() {
        System.out.println(cpu.toString());
    }

    public boolean launchNewProcess(Process process, boolean popped) {
        if(cpu.isBusy() || popped) {
            pushToReadyQueue(process);
            return false;
        } else {
            handleCPUProcess(cpu.process(process));
            return true;
        }
    }

    private ProcessStatus handleCPUProcess(Process process) {
        switch (process.getStatus()) {
            case READY:
                pushToReadyQueue(process);
                cpu.release();
                break;
            case BLOCKED:
                pushToBlockedQueue(process);
                cpu.release();
                break;
            case KILLED:
                cpu.release();
                break;
            case RUNNING:
                break;
        }
        return process.getStatus();
    }

    public void initSimulation() {
        Thread thread = new Thread(myInstance());
        thread.start();
    }

    // ============================================================== QUEUES MANAGER ==============================================================

    private void pushToReadyQueue(Process process) {
        readyQueue.add(readyQueue.size(), process);
    }

    private Process popFromReadyQueue() {
        return (readyQueue.size() > 0) ? readyQueue.remove(0) : null;
    }

    private void pushToBlockedQueue(Process process) {
        blockedQueue.add(blockedQueue.size(), process);
    }

    private Process popFromBlockedQueue() {
        return (blockedQueue.size() > 0) ? blockedQueue.remove(0) : null;
    }

    private void showReadyQueue() {
        System.out.println("Ready Queue =======================================");
        for (Process p : readyQueue) {
            System.out.println(p.toString());
        }
        System.out.println("===================================================");
    }

    private void showBlockedQueue() {
        System.out.println("Blocked Queue =====================================");
        for (Process p : blockedQueue) {
            System.out.println(p.toString());
        }
        System.out.println("===================================================");
    }

    private void showQueues() {
        showReadyQueue();
        showBlockedQueue();
    }

    // ============================================================== GETTERS && SETTERS ==============================================================

    private SOManager myInstance() {
        return this;
    }

}
