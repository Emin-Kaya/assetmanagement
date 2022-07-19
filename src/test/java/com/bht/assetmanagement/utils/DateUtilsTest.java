package com.bht.assetmanagement.utils;

import com.bht.assetmanagement.shared.date.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DateUtilsTest {

    @Spy
    DateUtils dateUtils = Mockito.mock(DateUtils.class);

    @Test
    void createLocalDateTest() {
        Mockito.when(dateUtils.createLocalDate()).thenReturn("15.07.2022");
        String date = dateUtils.createLocalDate();

        Assertions.assertEquals(date, "15.07.2022");
    }

}
