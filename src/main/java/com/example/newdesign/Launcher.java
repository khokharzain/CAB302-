package com.example.newdesign;

import com.example.newdesign.model.DataBaseInitialiser;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {

        DataBaseInitialiser.initialize(); // create table

        Application.launch(HelloApplication.class, args);
    }
}