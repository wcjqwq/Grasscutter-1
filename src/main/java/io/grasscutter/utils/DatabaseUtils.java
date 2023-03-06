package io.grasscutter.utils;

import io.grasscutter.account.Account;
import io.grasscutter.data.DataInterface;
import io.grasscutter.data.DataSerializable;
import io.grasscutter.server.DedicatedServer;
import java.util.Map;

import io.grasscutter.utils.interfaces.Serializable;
import io.grasscutter.utils.objects.Counter;
import org.jetbrains.annotations.Nullable;

/* Utility methods for interfacing with the database. */
public interface DatabaseUtils {
    /**
     * Shortcut method to get the data interface.
     *
     * @return A {@link DataInterface} instance.
     */
    static DataInterface getDataInterface() {
        return DedicatedServer.getInstance().getDataInterface();
    }

    /**
     * Shortcut method to get the ID field name for a database.
     *
     * @param fieldName The field name.
     * @return The ID field name.
     */
    static String getIdFieldName(String fieldName) {
        return DatabaseUtils.getDataInterface().getIdFieldName(fieldName);
    }

    /**
     * Shortcut method to save an object.
     * The object should be annotated with {@link DataSerializable}.
     *
     * @param object The object to save.
     */
    static void save(Serializable object) {
        DatabaseUtils.getDataInterface().save(object);
    }

    /**
     * Shortcut method to get an account by username.
     *
     * @param username The username.
     * @return The account, or null if the account does not exist.
     */
    @Nullable static Account fetchAccount(String username) {
        return DatabaseUtils.getDataInterface().get(Account.class, Map.of("username", username));
    }

    /**
     * Shortcut method to get an account by userId.
     *
     * @param userId The userId.
     * @return The account, or null if the account does not exist.
     */
    @Nullable static Account fetchAccount(long userId) {
        return DatabaseUtils.getDataInterface().get(Account.class, Map.of("gameUserId", userId));
    }

    /**
     * Fetch the counter's value and increment it.
     *
     * @param counter The counter's type.
     * @return The counter's value.
     */
    static long fetchAndIncrement(String counter) {
        var dataInterface = DatabaseUtils.getDataInterface();
        var counterObject = dataInterface.get(Counter.class, Map.of("_id", counter));

        // If the counter does not exist, create it.
        if (counterObject == null) {
            counterObject = new Counter();
            counterObject.type = counter;
            counterObject.value = -1;
        }

        // Increment the counter.
        counterObject.value++;
        // Save the counter.
        counterObject.save();

        return counterObject.value;
    }
}
