package edu.byu.cs.tweeter.server.dao.dynamodb;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.base.BaseDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.item.Authtoken;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoDAO implements BaseDAO {
    private final int AUTH_LIFE_TIME = 60;

    private static final String AuthTableName = "authtoken";
    private static final String TokenAttr = "token";

    protected static DynamoDbEnhancedClient enhancedClient;

    public DynamoDAO (DynamoDbEnhancedClient enhancedClient) {
        DynamoDAO.enhancedClient = enhancedClient;
    }

    protected static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    @Override
    public String getUser(AuthToken token) {
        DynamoDbTable<Authtoken> table = enhancedClient.table(AuthTableName, TableSchema.fromBean(Authtoken.class));
        Key key = Key.builder().partitionValue(token.getToken()).build();

        Authtoken result = table.getItem(key);

        return result.getUser_alias();
    }

    @Override
    public boolean valAuthToken(AuthToken token) {
        DynamoDbTable<Authtoken> table = enhancedClient.table(AuthTableName, TableSchema.fromBean(Authtoken.class));
        Key key = Key.builder().partitionValue(token.getToken()).build();

        Authtoken result = table.getItem(key);

        if (result != null) {
            if (LocalTime.now().getLong(ChronoField.SECOND_OF_DAY) < result.getExpiration()) {
                result.setExpiration(LocalTime.now().getLong(ChronoField.SECOND_OF_DAY) + AUTH_LIFE_TIME);
                table.updateItem(result);
                return true;
            } else {
                deleteAuthToken(new AuthToken(result.getToken()));
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public AuthToken insertAuthToken(String alias) {
        String token = UUID.randomUUID().toString();
        long datetime = LocalTime.now().getLong(ChronoField.SECOND_OF_DAY);

        DynamoDbTable<Authtoken> table = enhancedClient.table(AuthTableName, TableSchema.fromBean(Authtoken.class));

        Authtoken newtoken = new Authtoken(token, datetime, datetime+AUTH_LIFE_TIME, alias);
        table.putItem(newtoken);

        AuthToken newToken = new AuthToken(token, String.valueOf(datetime));
        return newToken;
    }

    @Override
    public boolean deleteAuthToken(AuthToken token) {
        DynamoDbTable<Authtoken> table = enhancedClient.table(AuthTableName, TableSchema.fromBean(Authtoken.class));
        Key key = Key.builder()
                .partitionValue(token.getToken())
                .build();

        table.deleteItem(key);

        return true;
    }
}
