/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress;


import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.directory.fortress.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.AccessMgrFactory;
import org.apache.directory.fortress.core.SecurityException;


public class AccessMgrTests extends TestCase
{
    private static final String CLS_NM = AccessMgrTests.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    public AccessMgrTests(String name )
    {
        super( name );
    }


    public void testAbacConstraints()
    {
        LOG.info( "Test Huey: ");
        // Negative use case, verify constraints are being evaluated:
        isNeither( new User( "Huey", "password") );
        isWasher( new User( "Huey", "password"), "north" );
        isWasher( new User( "Huey", "password"), "south" );
        isTeller( new User( "Huey", "password"), "east" );

        LOG.info( "Test Dewey: ");
        // Negative use case, verify constraints are being evaluated:
        isNeither( new User( "Dewey", "password") );
        isWasher( new User( "Dewey", "password"), "east" );
        isWasher( new User( "Dewey", "password"), "south" );
        isTeller( new User( "Dewey", "password"), "north" );

        LOG.info( "Test Louie: ");
        // Negative use case, verify constraints are being evaluated:
        isNeither( new User( "Louie", "password") );
        isWasher( new User( "Louie", "password"), "east" );
        isWasher( new User( "Louie", "password"), "north" );
        isTeller( new User( "Louie", "password"), "south" );
    }

    public void isNeither( User user )
    {
        String szLocation = ".isNeither";
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( );
            Session session = accessMgr.createSession( user, false );
            assertNotNull( session );

            List<UserRole> userRoles = session.getRoles();
            assertFalse( userRoles.contains( new UserRole( user.getUserId(), TIds.WASHER ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("MONEY", "dry" ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("MONEY", "rinse" ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("MONEY", "soak" ) ) );

            assertFalse( userRoles.contains( new UserRole( user.getUserId(), TIds.TELLER ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("ACCT", "deposit" ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("ACCT", "inquiry" ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("ACCT", "withdrawal" ) ) );
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }

    public void isTeller( User user, String value )
    {
        String szLocation = ".isTeller";

        List<RoleConstraint> constraints = new ArrayList();
        RoleConstraint constraint = new RoleConstraint();
        constraint.setKey( "locale" );
        constraint.setValue( value );
        constraints.add( constraint );

        constraint = new RoleConstraint();
        constraint.setKey( "strength" );
        constraint.setValue( "2fa" );
        constraints.add( constraint );

        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance( );
            Session session = accessMgr.createSession( user, constraints, false );
            assertNotNull( session );

            List<UserRole> userRoles = session.getRoles();
            assertFalse( userRoles.contains( new UserRole( user.getUserId(), TIds.WASHER ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("MONEY", "dry" ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("MONEY", "rinse" ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("MONEY", "soak" ) ) );

            assertTrue( userRoles.contains( new UserRole( user.getUserId(), TIds.TELLER ) ) );
            assertTrue( accessMgr.checkAccess( session, new Permission("ACCT", "deposit" ) ) );
            assertTrue( accessMgr.checkAccess( session, new Permission("ACCT", "inquiry" ) ) );
            assertTrue( accessMgr.checkAccess( session, new Permission("ACCT", "withdrawal" ) ) );
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }

    public void isWasher( User user, String value )
    {
        String szLocation = ".isWasher";

        List<RoleConstraint> constraints = new ArrayList();
        RoleConstraint constraint = new RoleConstraint();
        constraint.setKey( "locale" );
        constraint.setValue( value );
        constraints.add( constraint );

        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance( );
            Session session = accessMgr.createSession( user, constraints, false );
            assertNotNull( session );

            List<UserRole> userRoles = session.getRoles();
            assertTrue( userRoles.contains( new UserRole( user.getUserId(), TIds.WASHER ) ) );
            assertTrue( accessMgr.checkAccess( session, new Permission("MONEY", "dry" ) ) );
            assertTrue( accessMgr.checkAccess( session, new Permission("MONEY", "rinse" ) ) );
            assertTrue( accessMgr.checkAccess( session, new Permission("MONEY", "soak" ) ) );

            assertFalse( userRoles.contains( new UserRole( user.getUserId(), TIds.TELLER ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("ACCT", "deposit" ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("ACCT", "inquiry" ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("ACCT", "withdrawal" ) ) );
        }
        catch ( SecurityException ex )
        {
            LOG.error( szLocation + " caught SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new AccessMgrTests( "testAbacConstraints" ) );
        return suite;
    }
}
