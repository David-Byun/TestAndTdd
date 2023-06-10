package com.programming.techie.springredditclone.tdd.chap03;

import java.time.LocalDate;
import java.time.YearMonth;

public class ExpiryDateCalculator {

    public LocalDate calculateExpiryDate(PayData payData) {



        int addedMonths = payData.getPayAmount() == 100_000 ? 12 : payData.getPayAmount() / 10_000;

        if (payData.getFirstBillingDate() != null) {
            return expiryDateUsingFirstBillingDate(payData, addedMonths);
        } else {
            return payData.getBillingDate().plusMonths(addedMonths);
        }
    }

    private LocalDate expiryDateUsingFirstBillingDate(PayData payData, int addedMonths) {
        LocalDate candidateExp = payData.getBillingDate().plusMonths(addedMonths);
        if (payData.getFirstBillingDate().getDayOfMonth() != candidateExp.getDayOfMonth()) {

            final int dayLenOfCandiMon = YearMonth.from(candidateExp).lengthOfMonth();
            final int dayOfFirstBilling = payData.getFirstBillingDate().getDayOfMonth();

            //후보 만료일이 포함된 달의 마지막날이 첫 납부일의 일자수보다 작은 경우(ex 후보 : 4.30 첫납부일 3.31) -- 4.31인 경우에는 Exception이 터지기 때문에 조건으로 분기
            if (dayLenOfCandiMon < dayOfFirstBilling) {
                //첫번째가 3.31이고 후보군의 달이 4.30일인데 후보군의 달이 첫번째 달보다 작은 경우
                return candidateExp.withDayOfMonth(dayLenOfCandiMon);
            }
            //첫번째가 2.28이고 후보군의 달이 3.31 즉, 후보군의 달이 첫번째보다 큰 경우
            return candidateExp.withDayOfMonth(dayOfFirstBilling);
        } else {
            return candidateExp;
        }

    }


}
