package controller;

import models.SOManager;

public class Controller {

    private SOManager soManager;

    public Controller() {
        initComponents();
    }

    private void initComponents() {
        soManager = new SOManager();
    }
}
