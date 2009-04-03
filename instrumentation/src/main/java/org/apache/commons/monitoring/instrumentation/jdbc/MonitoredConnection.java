/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.monitoring.instrumentation.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

import org.apache.commons.monitoring.Repository;

/**
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public class MonitoredConnection
    implements Connection
{

    /**
     * CallBack on connection beeing closed
     */
    public interface ConnectionClosedCallBack
    {
        void onConnectionClosed();
    }

    /** target connection */
    private Connection connection;

    private Repository repository;

    private ConnectionClosedCallBack callBack;

    /**
     * @param connection target connection
     * @param monitor monitor for opened connections
     */
    public MonitoredConnection( Connection connection, Repository repository, ConnectionClosedCallBack callBack )
    {
        super();
        this.connection = connection;
        this.repository = repository;
        this.callBack = callBack;
    }

    public void close()
        throws SQLException
    {
        connection.close();
        callBack.onConnectionClosed();
    }

    public Statement createStatement()
        throws SQLException
    {
        return monitor( connection.createStatement() );
    }

    public Statement createStatement( int resultSetType, int resultSetConcurrency, int resultSetHoldability )
        throws SQLException
    {
        return monitor( connection.createStatement( resultSetType, resultSetConcurrency, resultSetHoldability ) );
    }

    public Statement createStatement( int resultSetType, int resultSetConcurrency )
        throws SQLException
    {
        return monitor( connection.createStatement( resultSetType, resultSetConcurrency ) );
    }

    public CallableStatement prepareCall( String sql, int resultSetType, int resultSetConcurrency,
                                          int resultSetHoldability )
        throws SQLException
    {
        return monitor( connection.prepareCall( sql, resultSetType, resultSetConcurrency, resultSetHoldability ), sql );
    }

    public CallableStatement prepareCall( String sql, int resultSetType, int resultSetConcurrency )
        throws SQLException
    {
        return monitor( connection.prepareCall( sql, resultSetType, resultSetConcurrency ), sql );
    }

    public CallableStatement prepareCall( String sql )
        throws SQLException
    {
        return monitor( connection.prepareCall( sql ), sql );
    }

    public PreparedStatement prepareStatement( String sql, int resultSetType, int resultSetConcurrency,
                                               int resultSetHoldability )
        throws SQLException
    {
        return monitor( connection.prepareStatement( sql, resultSetType, resultSetConcurrency, resultSetHoldability ),
            sql );
    }

    public PreparedStatement prepareStatement( String sql, int resultSetType, int resultSetConcurrency )
        throws SQLException
    {
        return monitor( connection.prepareStatement( sql, resultSetType, resultSetConcurrency ), sql );
    }

    public PreparedStatement prepareStatement( String sql, int autoGeneratedKeys )
        throws SQLException
    {
        return monitor( connection.prepareStatement( sql, autoGeneratedKeys ), sql );
    }

    public PreparedStatement prepareStatement( String sql, int[] columnIndexes )
        throws SQLException
    {
        return monitor( connection.prepareStatement( sql, columnIndexes ), sql );
    }

    public PreparedStatement prepareStatement( String sql, String[] columnNames )
        throws SQLException
    {
        return monitor( connection.prepareStatement( sql, columnNames ), sql );
    }

    public PreparedStatement prepareStatement( String sql )
        throws SQLException
    {
        return monitor( connection.prepareStatement( sql ), sql );
    }

    /**
     * @param statement traget Statement
     * @return monitored Statement
     */
    private Statement monitor( Statement statement )
    {
        return new MonitoredStatement( statement, repository );
    }

    /**
     * @param statement traget PreparedStatement
     * @param sql SQL Query
     * @return monitored PreparedStatement
     */
    private PreparedStatement monitor( PreparedStatement statement, String sql )
    {
        return new MonitoredPreparedStatement( statement, sql, repository );
    }

    /**
     * @param statement target PreparedStatement
     * @param sql SQL Query
     * @return Monitored CallableStatement
     */
    private CallableStatement monitor( CallableStatement statement, String sql )
    {
        return new MonitoredCallableStatement( statement, sql, repository );
    }

    // --- delegates methods ---

    public void clearWarnings()
        throws SQLException
    {
        connection.clearWarnings();
    }

    public void commit()
        throws SQLException
    {
        connection.commit();
    }

    public boolean getAutoCommit()
        throws SQLException
    {
        return connection.getAutoCommit();
    }

    public String getCatalog()
        throws SQLException
    {
        return connection.getCatalog();
    }

    public int getHoldability()
        throws SQLException
    {
        return connection.getHoldability();
    }

    public DatabaseMetaData getMetaData()
        throws SQLException
    {
        return connection.getMetaData();
    }

    public int getTransactionIsolation()
        throws SQLException
    {
        return connection.getTransactionIsolation();
    }

    public Map<String, Class<?>> getTypeMap()
        throws SQLException
    {
        return connection.getTypeMap();
    }

    public SQLWarning getWarnings()
        throws SQLException
    {
        return connection.getWarnings();
    }

    public boolean isClosed()
        throws SQLException
    {
        return connection.isClosed();
    }

    public boolean isReadOnly()
        throws SQLException
    {
        return connection.isReadOnly();
    }

    public String nativeSQL( String sql )
        throws SQLException
    {
        return connection.nativeSQL( sql );
    }

    public void releaseSavepoint( Savepoint savepoint )
        throws SQLException
    {
        connection.releaseSavepoint( savepoint );
    }

    public void rollback()
        throws SQLException
    {
        connection.rollback();
    }

    public void rollback( Savepoint savepoint )
        throws SQLException
    {
        connection.rollback( savepoint );
    }

    public void setAutoCommit( boolean autoCommit )
        throws SQLException
    {
        connection.setAutoCommit( autoCommit );
    }

    public void setCatalog( String catalog )
        throws SQLException
    {
        connection.setCatalog( catalog );
    }

    public void setHoldability( int holdability )
        throws SQLException
    {
        connection.setHoldability( holdability );
    }

    public void setReadOnly( boolean readOnly )
        throws SQLException
    {
        connection.setReadOnly( readOnly );
    }

    public Savepoint setSavepoint()
        throws SQLException
    {
        return connection.setSavepoint();
    }

    public Savepoint setSavepoint( String name )
        throws SQLException
    {
        return connection.setSavepoint( name );
    }

    public void setTransactionIsolation( int level )
        throws SQLException
    {
        connection.setTransactionIsolation( level );
    }

    public void setTypeMap( Map<String, Class<?>> map )
        throws SQLException
    {
        connection.setTypeMap( map );
    }
}
