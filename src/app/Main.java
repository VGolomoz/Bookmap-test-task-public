package app;

import app.service.InputDataHandler;
import app.service.MyParser;

public class Main {

    public static void main(String[] args) {

        new MyParser(new InputDataHandler()).startRead();
    }
}




