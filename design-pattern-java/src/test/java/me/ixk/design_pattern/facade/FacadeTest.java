package me.ixk.design_pattern.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/30 下午 2:53
 */
class FacadeTest {

    @Test
    void facade() {
        assertEquals("", ResultFacade.empty().render());
        final StringJsonResult json = ResultFacade.stringJson(
            "{\"key\": \"value\"}"
        );
        assertEquals("{\"key\": \"value\"}", json.render());
        assertEquals("application/json", json.contentType());
        assertEquals("text", ResultFacade.text("text").render());
        assertEquals(
            "<html></html>",
            ResultFacade.html("<html></html>").render()
        );
    }
}
