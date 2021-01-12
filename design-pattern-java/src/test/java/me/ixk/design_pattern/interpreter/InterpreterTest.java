package me.ixk.design_pattern.interpreter;

import static org.junit.jupiter.api.Assertions.assertFalse;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2021/1/12 下午 8:46
 */
@Slf4j
class InterpreterTest {

    @Test
    void interpreter() {
        final Context context = new Context();
        final Variable x = new Variable("X");
        final Variable y = new Variable("Y");
        final Constant t = new Constant(true);
        context.assign(x, false);
        context.assign(y, true);
        final Expression exp = new And(t, new And(x, new Not(y)));
        log.info("t && x && (x || !y) = {}", exp.interpret(context));
        assertFalse(exp.interpret(context));
    }
}
