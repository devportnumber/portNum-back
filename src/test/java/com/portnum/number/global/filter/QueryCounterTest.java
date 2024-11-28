package com.portnum.number.global.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class QueryCounterTest {

    @Test
    @DisplayName("값을 증가시킬 수 있다.")
    void increase_Success(){
        //given
        QueryCounter queryCounter = new QueryCounter();

        //when
        int beforeCount = queryCounter.getCount();
        queryCounter.increase();

        //then
        assertThat(beforeCount).isZero();
        assertThat(queryCounter.getCount()).isOne();
    }
}