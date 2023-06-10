package com.programming.techie.springredditclone.tdd.chap03;

import com.programming.techie.springredditclone.tdd.chap02.PasswordStrength;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/*
    1. 서비스를 사용하려면 매달 1만원을 선불로 납부한다. 납부일 기준으로 한달 뒤가 서비스 만료일
    2. 2개월 이상 요금을 납부할 수 있다.
    3. 10만원 납부하면 서비스 1년을 제공한다.
 */
class Chat03Test {
    @Test
    void 만원_납부하면_한달_뒤가_만료일이_됨() {

        assertExpiryDate(PayData.builder()
                .billingDate(LocalDate.of(2019,3,1))
                .payAmount(10_000)
                .build(), LocalDate.of(2019,4,1));

    }

    private void assertExpiryDate(PayData payData, LocalDate expectedExpiryDate) {
        ExpiryDateCalculator cal = new ExpiryDateCalculator();
        LocalDate realExpiryDate = cal.calculateExpiryDate(payData);
        assertEquals(expectedExpiryDate, realExpiryDate);
    }

    @Test
    void 납부일과_한달_뒤_일자가_같지_않음() {
        assertExpiryDate(PayData.builder()
                .billingDate(LocalDate.of(2019,1,31))
                .payAmount(10_000)
                .build(), LocalDate.of(2019,2,28));
    }

    //TDD : 쉬운 코드(요건) 먼저, 예외 상황 먼저
    @Test
    void 첫_납부일과_만료일_일자가_다를때_만원_납부() {
        PayData payData = PayData.builder()
                .firstBillingDate(LocalDate.of(2019, 1, 31))
                .billingDate(LocalDate.of(2019, 2, 28))
                .payAmount(10_000)
                .build();


        PayData payData2 = PayData.builder()
                .firstBillingDate(LocalDate.of(2019, 1, 30))
                .billingDate(LocalDate.of(2019, 2, 28))
                .payAmount(10_000)
                .build();

//        PayData payData3 = PayData.builder()
//                .firstBillingDate(LocalDate.of(2019, 2, 28))
//                .billingDate(LocalDate.of(2019, 3, 31))
//                .payAmount(10_000)
//                .build();

        assertExpiryDate(payData2, LocalDate.of(2019,3,30));
    }

    @Test
    void 이만원_이상_납부하면_비례해서_만료일_계산() {
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019, 3, 1))
                        .payAmount(20_000)
                        .build(),
                LocalDate.of(2019, 5, 1)
        );

        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019, 3, 1))
                        .payAmount(30_000)
                        .build(),
                LocalDate.of(2019, 6, 1)
        );
    }

    @Test
    void 첫_납부일과_만료일_일자가_다를때_이만원_이상_납부() {
        assertExpiryDate(
                PayData.builder()
                        .firstBillingDate(LocalDate.of(2019, 1, 31))
                        .billingDate(LocalDate.of(2019, 2, 28))
                        .payAmount(20_000)
                        .build(),
                LocalDate.of(2019, 4, 30)
        );
    }

    @Test
    void 십만원을_납부하면_1년_제공() {
        assertExpiryDate(
                PayData.builder()
                .billingDate(LocalDate.of(2019,1,28))
                .payAmount(100_000)
                .build(),
                LocalDate.of(2020,1,28));
    }

    //상황, 실행, 결과 -> given, when, then
}
