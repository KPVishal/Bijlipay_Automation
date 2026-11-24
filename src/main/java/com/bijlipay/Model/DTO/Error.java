package com.bijlipay.Model.DTO;

public class Error {

    public String Message;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @Override
    public String toString() {
        return "Error{" +
                "Message='" + Message + '\'' +
                '}';
    }
}
