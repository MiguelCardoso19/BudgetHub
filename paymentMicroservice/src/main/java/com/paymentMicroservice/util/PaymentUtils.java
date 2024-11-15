package com.paymentMicroservice.util;

import com.paymentMicroservice.dto.CreatePaymentDTO;
import com.paymentMicroservice.dto.CreatePaymentItemDTO;

public class PaymentUtils {

    public static long calculateAmount(CreatePaymentDTO createPaymentDTO) {
        long amout = 0;

        for (CreatePaymentItemDTO item : createPaymentDTO.getItems()) {
            amout += item.getAmount();
        }
        return amout;
    }
}
