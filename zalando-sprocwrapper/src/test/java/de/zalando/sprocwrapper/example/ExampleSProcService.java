package de.zalando.sprocwrapper.example;

import java.util.Date;
import java.util.List;

import de.zalando.sprocwrapper.SProcCall;
import de.zalando.sprocwrapper.SProcParam;
import de.zalando.sprocwrapper.SProcService;
import de.zalando.sprocwrapper.sharding.ShardKey;
import de.zalando.sprocwrapper.sharding.VirtualShardIdentityStrategy;

/**
 * @author  jmussler
 */
@SProcService
public interface ExampleSProcService {
    @SProcCall(name = "create_article_simple")
    void createArticleSimple(@SProcParam String sku);

    @SProcCall
    void createArticleSimples(@SProcParam List<String> skus);

    @SProcCall(name = "create_article_simple_items")
    String createArticleSimpleItems(@SProcParam(name = "sku")
            @ShardKey String sku, @SProcParam int stockId,
            @SProcParam(name = "quantity") int quantity,
            @SProcParam(name = "price") int purchasePrice,
            @SProcParam(name = "referencenumber") String referenceNumber);

    @SProcCall
    Integer getSimpleInt();

    @SProcCall(name = "get_simple_int")
    int getSimpleIntAsPrimitive();

    @SProcCall
    long getSimpleLong();

    @SProcCall
    int getSimpleInt(@SProcParam int i);

    @SProcCall
    boolean getBoolean();

    @SProcCall
    void setBoolean(@SProcParam boolean bool);

    @SProcCall
    void useEnumParam(@SProcParam ExampleEnum enumParameter);

    @SProcCall
    void useDateParam(@SProcParam Date d);

    @SProcCall
    void useDateParam2(@SProcParam(sqlType = java.sql.Types.DATE) Date d);

    @SProcCall
    void useCharParam(@SProcParam char c);

    @SProcCall
    void useIntegerListParam(@SProcParam List<Integer> l);

    @SProcCall
    void getSimpleIntVoid(@SProcParam int i);

    @SProcCall
    boolean login(@SProcParam String userName,
            @SProcParam(sensitive = true) String password);

    @SProcCall(sql = "SELECT 'a' AS a, 'b' AS b UNION ALL SELECT 'c', 'd'")
    List<ExampleDomainObject> getResult();

    @SProcCall(sql = "SELECT 'a' AS a, 'b' AS b")
    ExampleDomainObject getSingleResult();

    @SProcCall(sql = "SELECT 5555")
    Integer getBla();

    @SProcCall(shardStrategy = VirtualShardIdentityStrategy.class)
    int getShardIndex(@ShardKey int shard);

    @SProcCall(runOnAllShards = true)
    List<String> collectDataFromAllShards(@SProcParam String someParameter);

    @SProcCall(sql = "SELECT 1 UNION ALL SELECT 2")
    List<Integer> getInts();

    @SProcCall(sql = "SELECT 1000 UNION ALL SELECT 2002")
    List<Long> getLongs();

    @SProcCall
    String createOrUpdateObject(@SProcParam ExampleDomainObject object);

    @SProcCall
    String createOrUpdateObjectWithEnum(@SProcParam ExampleDomainObjectWithEnum object);

    @SProcCall
    String createOrUpdateObjectWithDate(@SProcParam ExampleDomainObjectWithDate object);

    @SProcCall
    String createOrUpdateMultipleObjects(
            @SProcParam(type = "example_domain_object[]") List<ExampleDomainObject> objects);

    @SProcCall
    String createOrUpdateMultipleObjectsWithMap(
            @SProcParam(type = "example_domain_object_with_map[]") List<ExampleDomainObjectWithMap> objects);

    @SProcCall
    String createOrUpdateMultipleObjectsWithInnerObject(
            @SProcParam(type = "example_domain_object_with_inner_object[]") List<ExampleDomainObjectWithInnerObject> objects);

    @SProcCall
    void createOrUpdateMultipleObjectsWithMapVoid(
            @SProcParam(type = "example_domain_object_with_map[]") List<ExampleDomainObjectWithMap> objects);

    @SProcCall
    boolean reserveStock(@ShardKey @SProcParam String sku);

    @SProcCall(name = "create_or_update_address")
    AddressPojo createAddress(@SProcParam AddressPojo a);

    @SProcCall(name = "get_address")
    AddressPojo getAddress(@SProcParam AddressPojo a);
}
