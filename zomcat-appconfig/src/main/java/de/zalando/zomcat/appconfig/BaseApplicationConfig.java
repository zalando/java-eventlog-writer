package de.zalando.zomcat.appconfig;

import java.util.Collection;
import java.util.Map;

import de.zalando.appconfig.Configuration;

import de.zalando.config.domain.context.ApplicationContext;

import de.zalando.domain.Environment;

import de.zalando.zomcat.configuration.AppInstanceContextProvider;
import de.zalando.zomcat.jobs.JobConfigSource;

/**
 * @author  hjacobs
 * @author  mjuenemann
 */
public interface BaseApplicationConfig extends JobConfigSource {

    String BEAN_NAME = "applicationConfig";

    /**
     * @return  flag if this is a test system
     */
    boolean isTesting();

    /**
     * @return  the {@link Environment Environment}
     */
    Environment getEnvironment();

    boolean isLocalMachine();

    AppInstanceContextProvider getAppInstanceContext();

    /**
     * Retrieve an entire entity from the configuration. All fields of the object will be retrieved as a separate
     * configuration entry with key ClassName.methodName, e.g. AppDomain.paymentMethods (get of the method name is
     * omitted in the key). The id is saved as custom context in the database, so entities can only be defined in the
     * central config service at the moment.
     *
     * @param       classOfT  class of the interface that should be implemented by the returned proxy
     * @param       id        identifier of the entity. Will be mapped to the custom context and can be null
     *
     * @return      Dynamic proxy that implements classOfT
     *
     * @see         EntityFactory
     * @deprecated  Entities can now be retrieved by using an @AppConfigService with an @Entity method. Alternatively,
     *              you can also use the EntityFactory. This method is inferior, because it uses reflection on every
     *              invocation and only supports the custom context as identifier
     * @example     PaymentMethod pm = applicationConfig.getEntity(PaymentMethod.class, pmId);*
     */
    @Deprecated
    <T> T getEntity(final Class<T> classOfT, final String id);

    /**
     * Retrieve an entire entity from the configuration. All fields of the object will be retrieved as a separate
     * configuration entry with key ClassName.methodName, e.g. AppDomain.paymentMethods (get of the method name is
     * omitted in the key). The id is saved as custom context in the database, so entities can only be defined in the
     * central config service at the moment.
     *
     * @param       classOfT  class of the interface that should be implemented by the returned proxy
     * @param       id        identifier of the entity. Will be mapped to the custom context and can be null
     *
     * @return      Dynamic proxy that implements classOfT
     *
     * @see         EntityFactory
     * @deprecated  Entities can now be retrieved by using an @AppConfigService with an @Entity method. Alternatively,
     *              you can also use the EntityFactory. This method is inferior, because it uses reflection on every
     *              invocation and only supports the custom context as identifier
     * @example     PaymentMethod pm = applicationConfig.getEntity(PaymentMethod.class, pmId);
     */
    @Deprecated
    <T> T getEntity(final Class<T> classOfT, final int id);

    /**
     * Retrieve all entities that are defined in the central config service (other resources are not supported at the
     * moment) with their context information. Only use this method if you want to iterate over all specified contexts.
     * If you only want to get one entity instance, use getEntity or directly EntitiyConfigurationProxy
     *
     * @param       classOfT
     *
     * @return
     *
     * @deprecated  If you just want to retrieve all entities you can now use an @AppConfigService with a method that
     *              has an @EntityCollection annotation. This method has a very specific use case and should be removed
     *              from the library, because it adds little value. To achieve the exact same thing, use the
     *              EntityFactory's getSpecifiedContext method directly. This method is inferior, because it uses
     *              reflection on every invocation and only works if the custom context is the identifying context, i.e.
     *              it does not work for the AppDomain entity
     */
    @Deprecated
    <T> Map<ApplicationContext, Collection<T>> getEntitiesWithContext(final Class<T> classOfT);

    Configuration getConfig();

    <Toggle extends FeatureToggle> boolean isFeatureEnabled(final Toggle feature);

    <Toggle extends FeatureToggle> boolean isFeatureEnabled(final Toggle feature, final int appDomainId,
            final boolean defaultValue);

    <Toggle extends FeatureToggle> boolean isFeatureEnabled(final Toggle feature, final int appDomainId);

    <Toggle extends FeatureToggle> boolean isFeatureEnabled(final Toggle feature, final boolean defaultValue);

}
