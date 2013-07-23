package de.zalando.jpa;

import org.junit.Test;

import de.zalando.jpa.validation.ConstraintViolationExceptionTranslator;

import junit.framework.Assert;

/**
 * @author  hjacobs
 */
public class ConstraintViolationExceptionTranslatorTest {

    @Test
    public void testTranslateExceptionIfPossible() {
        ConstraintViolationExceptionTranslator translator = new ConstraintViolationExceptionTranslator();
        Assert.assertNull(translator.translateExceptionIfPossible(new RuntimeException("TEST")));
    }

}
