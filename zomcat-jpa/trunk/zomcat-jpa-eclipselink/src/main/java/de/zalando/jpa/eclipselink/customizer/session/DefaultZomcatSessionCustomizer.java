package de.zalando.jpa.eclipselink.customizer.session;

import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.mappings.ManyToOneMapping;
import org.eclipse.persistence.mappings.OneToManyMapping;

import de.zalando.jpa.eclipselink.customizer.classdescriptor.ClassDescriptorCustomizer;
import de.zalando.jpa.eclipselink.customizer.classdescriptor.TableNameClassDescriptorCustomizer;

/**
 * The {@link DefaultZomcatSessionCustomizer} registers
 * {@link de.zalando.jpa.eclipselink.customizer.databasemapping.ColumnNameCustomizer} s for {@link DirectToFieldMapping},
 * {@link OneToManyMapping} and {@link ManyToOneMapping} to confirm Zalandos 'Column-Name-Requirements'.
 *
 * @author  jbellmann
 */
public class DefaultZomcatSessionCustomizer extends AbstractZomcatSessionCustomizer {

    private final ClassDescriptorCustomizer clazzDescriptorCustomizer;

    public DefaultZomcatSessionCustomizer() {
        super();

        // TableNameClassDescriptorCustomizer should always be the first
        clazzDescriptorCustomizer = builder().with(new TableNameClassDescriptorCustomizer())
                                             .with(defaultColumnNameClassDescriptorCustomizer()).build();

    }

    @Override
    public ClassDescriptorCustomizer getClassDescriptorCustomizer() {
        return this.clazzDescriptorCustomizer;
    }

}
