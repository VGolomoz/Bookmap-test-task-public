package app;

import app.service.InputDataHandler;
import app.service.MyParser;
import app.util.CustomException;

public class Main {


    public static void main(String[] args) {


        try {
            new MyParser(new InputDataHandler()).read();
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }
}




