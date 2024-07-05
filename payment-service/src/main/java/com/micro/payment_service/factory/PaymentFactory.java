package com.micro.payment_service.factory;

import com.micro.payment_service.enums.BankType;
import com.micro.payment_service.models.PaymentMethod;
import com.micro.payment_service.models.VNPay;

public class PaymentFactory {
    public static PaymentMethod getPaymentMethod(BankType bankType){
        switch (bankType) {
            case VNPay:
                return new VNPay();
            default:
                throw new IllegalArgumentException("Unsupport Bank Type: "+ bankType);
        }
    }
}
