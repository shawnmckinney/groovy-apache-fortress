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


public class AccessManagerTests extends TestCase
{
    private static final String CLS_NM = AccessManagerTests.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    public AccessManagerTests(String name )
    {
        super( name );
    }


    public void testAbacConstraints()
    {
        System.out.println( " Test Huey: " );

        // Negative use case, verify constraints are being evaluated:
        isNeither( new User( "Huey", "password") );

        // Huey is a washer in the north:
        List<RoleConstraint> constraints = new ArrayList();
        RoleConstraint constraint = new RoleConstraint();
        constraint.setKey( "locale" );
        constraint.setValue( "north" );
        constraints.add( constraint );
        isWasher( new User( "Huey", "password"), constraints );

        // Huey is a washer in the south:
        constraints = new ArrayList();
        constraint = new RoleConstraint();
        constraint.setKey( "locale" );
        constraint.setValue( "south" );
        constraints.add( constraint );
        isWasher( new User( "Huey", "password"), constraints );

        // Huey is a teller in the east:
        constraints = new ArrayList();
        constraint = new RoleConstraint();
        constraint.setKey( "locale" );
        constraint.setValue( "east" );
        constraint = new RoleConstraint();
        constraint.setKey( "strength" );
        constraint.setValue( "2fa" );
        constraints.add( constraint );
        isTeller( new User( "Huey", "password"), constraints );

        // Negative use case, verify constraints are being evaluated:
        isNeither( new User( "Dewey", "password") );

        // Dewey is a washer in the east:
        constraints = new ArrayList();
        constraint = new RoleConstraint();
        constraint.setKey( "locale" );
        constraint.setValue( "east" );
        constraints.add( constraint );
        isWasher( new User( "Dewey", "password"), constraints );

        // Dewey is a washer in the south:
        constraints = new ArrayList();
        constraint = new RoleConstraint();
        constraint.setKey( "locale" );
        constraint.setValue( "south" );
        constraints.add( constraint );
        isWasher( new User( "Dewey", "password"), constraints );

        // Dewey is a teller in the north:
        constraints = new ArrayList();
        constraint = new RoleConstraint();
        constraint.setKey( "locale" );
        constraint.setValue( "north" );
        constraint = new RoleConstraint();
        constraint.setKey( "strength" );
        constraint.setValue( "2fa" );
        constraints.add( constraint );
        isTeller( new User( "Dewey", "password"), constraints );

        // Negative use case, verify constraints are being evaluated:
        isNeither( new User( "Louie", "password") );

        // Louie is a washer in the east:
        constraints = new ArrayList();
        constraint = new RoleConstraint();
        constraint.setKey( "locale" );
        constraint.setValue( "east" );
        constraints.add( constraint );
        isWasher( new User( "Louie", "password"), constraints );

        // Louie is a washer in the north:
        constraints = new ArrayList();
        constraint = new RoleConstraint();
        constraint.setKey( "locale" );
        constraint.setValue( "north" );
        constraints.add( constraint );
        isWasher( new User( "Louie", "password"), constraints );

        // Louie is a teller in the south:
        constraints = new ArrayList();
        constraint = new RoleConstraint();
        constraint.setKey( "locale" );
        constraint.setValue( "south" );
        constraint = new RoleConstraint();
        constraint.setKey( "strength" );
        constraint.setValue( "2fa" );
        constraints.add( constraint );
        isTeller( new User( "Louie", "password"), constraints );
    }

    public void isNeither( User user )
    {
        String szLocation = ".isNeither";
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance( );
            Session session = accessMgr.createSession( user, false );
            assertNotNull( session );
            List<UserRole> userRoles = session.getRoles();
            assertFalse( userRoles.contains( new UserRole( user.getUserId(), TIds.WASHER ) ) );
            assertFalse( userRoles.contains( new UserRole( user.getUserId(), TIds.TELLER ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("MONEY", "dry" ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("MONEY", "rinse" ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("MONEY", "soak" ) ) );
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

    public void isTeller( User user, List<RoleConstraint> constraints )
    {
        String szLocation = ".isTeller";
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance( );
            Session session = accessMgr.createSession( user, constraints, false );
            assertNotNull( session );
            List<UserRole> userRoles = session.getRoles();
            assertFalse( userRoles.contains( new UserRole( user.getUserId(), TIds.WASHER ) ) );
            assertTrue( userRoles.contains( new UserRole( user.getUserId(), TIds.TELLER ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("MONEY", "dry" ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("MONEY", "rinse" ) ) );
            assertFalse( accessMgr.checkAccess( session, new Permission("MONEY", "soak" ) ) );
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

    public void isWasher( User user, List<RoleConstraint> constraints )
    {
        String szLocation = ".isWasher";
        try
        {
            // Instantiate the AccessMgr implementation.
            AccessMgr accessMgr = AccessMgrFactory.createInstance( );
            Session session = accessMgr.createSession( user, constraints, false );
            assertNotNull( session );
            List<UserRole> userRoles = session.getRoles();
            assertTrue( userRoles.contains( new UserRole( user.getUserId(), TIds.WASHER ) ) );
            assertFalse( userRoles.contains( new UserRole( user.getUserId(), TIds.TELLER ) ) );
            assertTrue( accessMgr.checkAccess( session, new Permission("MONEY", "dry" ) ) );
            assertTrue( accessMgr.checkAccess( session, new Permission("MONEY", "rinse" ) ) );
            assertTrue( accessMgr.checkAccess( session, new Permission("MONEY", "soak" ) ) );
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

    /**
     * Run the Fortress Abac samples.
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new AccessManagerTests( "testAbacConstraints" ) );
        return suite;
    }
}
