package com.zjy.dao.common;

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.db.ConnectionSource;
import ch.qos.logback.core.db.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by Administrator on 2018/12/22.
 */
public abstract class DBAppenderBase<E> extends UnsynchronizedAppenderBase<E> {

    protected ConnectionSource connectionSource;

    @Override
    public void append(E eventObject) {
        Connection connection = null;
        PreparedStatement insertStatement = null;
        try {
            connection = connectionSource.getConnection();
            connection.setAutoCommit(false);
            insertStatement = connection.prepareStatement(getInsertSQL());
            synchronized (this) {
                subAppend(eventObject, connection, insertStatement);
            }
            connection.commit();
        } catch (Throwable sqle) {
            addError("problem appending event", sqle);
        } finally {
            DBHelper.closeStatement(insertStatement);
            DBHelper.closeConnection(connection);
        }
    }

    protected abstract void subAppend(E eventObject, Connection connection, PreparedStatement statement) throws Throwable;

    protected abstract String getInsertSQL();

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public void setConnectionSource(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }
}
