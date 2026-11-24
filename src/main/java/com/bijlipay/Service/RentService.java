package com.bijlipay.Service;

import java.io.IOException;

public interface RentService {
    void calculateRental();

    void importData(boolean action) throws IOException;
}
