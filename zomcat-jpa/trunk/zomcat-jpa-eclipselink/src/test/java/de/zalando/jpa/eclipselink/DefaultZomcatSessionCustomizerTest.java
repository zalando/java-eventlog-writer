package de.zalando.jpa.eclipselink;

import java.util.Map;
import java.util.Vector;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.sessions.DatabaseSessionImpl;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.sessions.Login;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.Session;

import org.junit.Test;

import org.mockito.Mockito;

import com.google.common.collect.Maps;

// @Ignore
public class DefaultZomcatSessionCustomizerTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void test() throws Exception {
        DefaultZomcatSessionCustomizer defaultSessionCustomizer = new DefaultZomcatSessionCustomizer();

        DatabaseField field = new DatabaseField();
// field.se

        DirectToFieldMapping mapping = new DirectToFieldMapping();
        mapping.setAttributeName("brandCode");
        mapping.setField(field);
        mapping.setAttributeClassification(String.class);

        Vector mappings = new Vector();
        mappings.add(mapping);

        ClassDescriptor descriptor = new ClassDescriptor();
        descriptor.setMappings(mappings);
        descriptor.setTableName("purchase_order_head");

        Map<Class, ClassDescriptor> result = Maps.newHashMap();

        result.put(Order.class, descriptor);

        Login login = Mockito.mock(Login.class);
        Project project = new Project();
        project.setDescriptors(result);
        project.setDatasourceLogin(login);

        Session session = new DatabaseSessionImpl(project);
        session.setSessionLog(new Log4jSessionLog());

        //
        defaultSessionCustomizer.customize(session);
    }

    static final class Order { }

}
