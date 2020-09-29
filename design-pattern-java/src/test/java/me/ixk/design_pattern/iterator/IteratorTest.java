package me.ixk.design_pattern.iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class IteratorTest {

    @Test
    void simple() {
        final Iterator<Integer> iterator = new ArrayAggregate().iterator();
        int index = 1;
        while (iterator.hasNext()) {
            final int curr = iterator.next();
            log.info("Item: {}", curr);
            assertEquals(index++, curr);
        }
    }
}
