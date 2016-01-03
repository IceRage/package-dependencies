package com.bt.coding_test.exception;

import java.io.IOException;

/**
 * Class used to represent invalid input exceptions. 
 */
@SuppressWarnings("serial")
public class InvalidInputException extends IOException {

    public InvalidInputException(String message) {
        super(message);
    }
    
}
