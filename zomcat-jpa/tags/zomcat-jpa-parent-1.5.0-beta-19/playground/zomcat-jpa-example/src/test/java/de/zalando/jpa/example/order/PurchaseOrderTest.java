package de.zalando.jpa.example.order;

import org.junit.Test;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import de.zalando.jpa.config.TestProfiles;

/**
 * @author  jbellmann
 */
@ActiveProfiles(TestProfiles.H2)
@DirtiesContext
public class PurchaseOrderTest extends AbstractPurchaseOrderTestSupport {

    @Test
// @Rollback(false)
    public void testSavePurchaseOrder() {
        super.doTestSavePurchaseOrder();
    }
}