package app.service;

import app.util.CustomException;

import java.util.List;
import java.util.TreeMap;

import static app.util.Errors.*;
import static app.util.MyConstans.*;

public class InputDataHandler {

    private TreeMap<Long, Long> bid = new TreeMap<>();
    private TreeMap<Long, Long> ask = new TreeMap<>();
    private StringBuilder output = new StringBuilder();

    public String processing(List<String> inputData) throws CustomException {

        if(!inputData.isEmpty()) {

            inputData.forEach(input -> {

                try {
                    String[] splitDataLine = input.split(",");

                    if (splitDataLine.length == UPDATE_SIZE && splitDataLine[0].equals(UPDATE))
                        update(splitDataLine[1], splitDataLine[2], splitDataLine[3]);

                    else if (splitDataLine.length == ORDER_SIZE && splitDataLine[0].equals(ORDER))
                        order(splitDataLine[1], splitDataLine[2]);

                    else if (splitDataLine.length == QUERIE_FOR_SIZE_SIZE && splitDataLine[0].equals(QUERIE))
                        querie(splitDataLine[1], splitDataLine[2]);

                    else if (splitDataLine.length == QUERIE_FOR_BEST_SIZE && splitDataLine[0].equals(QUERIE))
                        querie(splitDataLine[1], null);

                    else throw new CustomException(INPUT_DATA_ERROR);
                } catch (CustomException e) {
                    System.err.println(e.getMessage());
                }
            });

        } else throw new CustomException(INPUT_DATA_ERROR);

        return output.toString();
    }

    private void update(String updatePrice, String updateSize, String updateType) throws CustomException {

        Long price = convertStringToLong(updatePrice);
        Long size = convertStringToLong(updateSize);
        updateType = checkStringInput(updateType);

        if (updateType.equalsIgnoreCase(BID)) {
            if (bid.containsKey(price)) {
                Long oldSize = bid.get(price);
                bid.put(price, oldSize + size);
            } else {
                bid.put(price, size);
            }
        } else if (updateType.equalsIgnoreCase(ASK)) {
            if (ask.containsKey(price)) {
                Long oldSize = ask.get(price);
                ask.put(price, oldSize + size);
            } else {
                ask.put(price, size);
            }
        } else {
            throw new CustomException(UPDATE_TYPE_ERROR);
        }
    }

    private void order(String orderType, String orderSize) throws CustomException {

        Long size = convertStringToLong(orderSize);
        orderType = checkStringInput(orderType);

        if (orderType.equalsIgnoreCase(BUY)) {

            if (!ask.isEmpty()) {
                Long mostCheapAsksSize = ask.get(ask.firstKey());
                if (size == mostCheapAsksSize) ask.remove(ask.firstKey());
                else if (size < mostCheapAsksSize) ask.put(ask.firstKey(), mostCheapAsksSize - size);
                else throw new CustomException(ORDER_INPUT_SIZE_ERROR);

            } else {
                throw new CustomException(ORDER_OFFER_ERROR);
            }

        } else if (orderType.equalsIgnoreCase(SELL)) {

            if (!bid.isEmpty()) {
                Long mostExpensiveBidsSize = bid.get(bid.lastKey());
                if (size == mostExpensiveBidsSize) bid.remove(bid.lastKey());
                else if (size <= mostExpensiveBidsSize) bid.put(bid.lastKey(), mostExpensiveBidsSize - size);
                else throw new CustomException(ORDER_INPUT_SIZE_ERROR);

            } else {
                throw new CustomException(ORDER_OFFER_ERROR);
            }
        } else {
            throw new CustomException(ORDER_TYPE_ERROR);
        }
    }

    private void querie(String querieType, String queriePrice) throws CustomException {

        querieType = checkStringInput(querieType);

        if (queriePrice == null) {

            Long bestPrice;
            Long bestSize;

            if (querieType.equalsIgnoreCase(BEST_BID)) {
                if (!bid.isEmpty()) {
                    bestPrice = bid.lastKey();
                    bestSize = bid.get(bestPrice);
                } else {
                    throw new CustomException(QUERIE_OFFER_ERROR);
                }
            } else if (querieType.equalsIgnoreCase(BEST_ASK)) {
                if (!ask.isEmpty()) {
                    bestPrice = ask.lastKey();
                    bestSize = ask.get(bestPrice);
                } else {
                    throw new CustomException(QUERIE_OFFER_ERROR);
                }
            } else {
                throw new CustomException(QUERIE_TYPE_ERROR);
            }
            output.append(bestPrice + "," + bestSize + "\r\n");

        } else {
            Long price = convertStringToLong(queriePrice);
            output.append(bid.getOrDefault(price, 0L) + ask.getOrDefault(price, 0L) + "\r\n");
        }
    }

    private Long convertStringToLong(String input) throws CustomException {

        Long output;

        try {
            if (input == null) throw new CustomException(INPUT_DATA_ERROR);
            output = Long.valueOf(input);
        } catch (NumberFormatException e) {
            throw new CustomException(INPUT_DATA_ERROR);
        }
        return output;
    }

    private String checkStringInput(String input) throws CustomException {

        if (input == null) throw new CustomException(INPUT_DATA_ERROR);

        return input;
    }
}
