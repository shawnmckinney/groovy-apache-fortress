package org.apache.directory.fortress;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.directory.fortress.core.*;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminMgrTests extends TestCase
{
    private static final String CLS_NM = AdminMgrTests.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    public AdminMgrTests(String name )
    {
        super( name );
    }

    public void testAddAbacConstraints()
    {
        try
        {
            DelAdminMgr delAdminMgr = DelAdminMgrFactory.createInstance();
            AdminMgr adminMgr = AdminMgrFactory.createInstance();

            delAdminMgr.add( new OrgUnit( TIds.DEFAULT, OrgUnit.Type.USER ) );
            Role washer = adminMgr.addRole( new Role ( TIds.WASHER ) );

            RoleConstraint locale = new RoleConstraint();
            locale.setType( RoleConstraint.RCType.USER );
            locale.setKey( "locale" );
            adminMgr.enableRoleConstraint( washer, locale );

            Role teller = adminMgr.addRole( new Role ( TIds.TELLER ) );
            adminMgr.enableRoleConstraint( teller, locale );

            RoleConstraint strength = new RoleConstraint();
            strength.setKey( "strength" );
            strength.setType( RoleConstraint.RCType.USER );
            adminMgr.enableRoleConstraint( teller, strength );

            SDSet dsd = new SDSet( );
            dsd.setName( "Bankers" );
            dsd.setCardinality( 2 );
            dsd.setMember( TIds.TELLER );
            dsd.setMember( TIds.WASHER );
            adminMgr.createDsdSet( dsd );

            User huey = new User( "Huey", "password" );
            huey.setOu( TIds.DEFAULT );
            adminMgr.addUser( huey );
            adminMgr.assignUser( new UserRole( huey.getUserId(), TIds.WASHER ));
            adminMgr.assignUser( new UserRole( huey.getUserId(), TIds.TELLER ));
            strength.setValue( "2FA" );
            strength.setId( TIds.TELLER );
            adminMgr.addRoleConstraint( new UserRole( huey.getUserId(), TIds.TELLER ), strength );
            locale.setValue( "East" );
            adminMgr.addRoleConstraint( new UserRole( huey.getUserId(), TIds.TELLER ), locale );
            locale.setValue( "North" );
            adminMgr.addRoleConstraint( new UserRole( huey.getUserId(), TIds.WASHER ), locale );
            locale.setValue( "South" );
            adminMgr.addRoleConstraint( new UserRole( huey.getUserId(), TIds.WASHER ), locale );

            User dewey = new User( "Dewey", "password" );
            dewey.setOu( TIds.DEFAULT );
            adminMgr.addUser( dewey );
            adminMgr.assignUser( new UserRole( dewey.getUserId(), TIds.WASHER ));
            adminMgr.assignUser( new UserRole( dewey.getUserId(), TIds.TELLER ));
            strength.setValue( "2FA" );
            strength.setId( TIds.TELLER );
            adminMgr.addRoleConstraint( new UserRole( dewey.getUserId(), TIds.TELLER ), strength );
            locale.setValue( "North" );
            adminMgr.addRoleConstraint( new UserRole( dewey.getUserId(), TIds.TELLER ), locale );
            locale.setValue( "East" );
            adminMgr.addRoleConstraint( new UserRole( dewey.getUserId(), TIds.WASHER ), locale );
            locale.setValue( "South" );
            adminMgr.addRoleConstraint( new UserRole( dewey.getUserId(), TIds.WASHER ), locale );

            User louie = new User( "Louie", "password" );
            louie.setOu( TIds.DEFAULT );
            adminMgr.addUser( louie );
            adminMgr.assignUser( new UserRole( louie.getUserId(), TIds.WASHER ));
            adminMgr.assignUser( new UserRole( louie.getUserId(), TIds.TELLER ));
            strength.setValue( "2FA" );
            strength.setId( TIds.TELLER );
            adminMgr.addRoleConstraint( new UserRole( louie.getUserId(), TIds.TELLER ), strength );
            locale.setValue( "South" );
            adminMgr.addRoleConstraint( new UserRole( louie.getUserId(), TIds.TELLER ), locale );
            locale.setValue( "North" );
            adminMgr.addRoleConstraint( new UserRole( louie.getUserId(), TIds.WASHER ), locale );
            locale.setValue( "East" );
            adminMgr.addRoleConstraint( new UserRole( louie.getUserId(), TIds.WASHER ), locale );

            delAdminMgr.add( new OrgUnit( TIds.DEFAULT, OrgUnit.Type.PERM ) );

            PermObj money = adminMgr.addPermObj( new PermObj( "MONEY", TIds.DEFAULT ));
            Permission dry = adminMgr.addPermission( new Permission( money.getObjName(), "dry") );
            Permission rinse = adminMgr.addPermission( new Permission( money.getObjName(), "rinse") );
            Permission soak = adminMgr.addPermission( new Permission( money.getObjName(), "soak") );
            adminMgr.grantPermission( dry, new Role( TIds.WASHER ) );
            adminMgr.grantPermission( rinse, new Role( TIds.WASHER ) );
            adminMgr.grantPermission( soak, new Role( TIds.WASHER ) );

            PermObj acct = adminMgr.addPermObj( new PermObj( "ACCT", TIds.DEFAULT ));
            Permission deposit = adminMgr.addPermission( new Permission( acct.getObjName(), "deposit") );
            Permission inquiry = adminMgr.addPermission( new Permission( acct.getObjName(), "inquiry") );
            Permission withdrawal = adminMgr.addPermission( new Permission( acct.getObjName(), "withdrawal") );
            adminMgr.grantPermission( deposit, new Role( TIds.TELLER ) );
            adminMgr.grantPermission( inquiry, new Role( TIds.TELLER ) );
            adminMgr.grantPermission( withdrawal, new Role( TIds.TELLER ) );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }

    public void testDelAbacConstraints()
    {
        try
        {
            DelAdminMgr delAdminMgr = DelAdminMgrFactory.createInstance();
            AdminMgr adminMgr = AdminMgrFactory.createInstance();

            adminMgr.deletePermission( new Permission( "MONEY", "dry") );
            adminMgr.deletePermission( new Permission( "MONEY", "rinse") );
            adminMgr.deletePermission( new Permission( "MONEY", "soak") );
            adminMgr.deletePermObj( new PermObj( "MONEY", TIds.DEFAULT ));

            adminMgr.deletePermission( new Permission( "ACCT", "deposit") );
            adminMgr.deletePermission( new Permission( "ACCT", "inquiry") );
            adminMgr.deletePermission( new Permission( "ACCT", "withdrawal") );
            adminMgr.deletePermObj( new PermObj( "ACCT", TIds.DEFAULT ));

            delAdminMgr.delete( new OrgUnit( TIds.DEFAULT, OrgUnit.Type.PERM ) );

            SDSet dsd = new SDSet( );
            dsd.setName( "Bankers" );
            adminMgr.deleteDsdSet( dsd );

            RoleConstraint locale = new RoleConstraint();
            locale.setType( RoleConstraint.RCType.USER );
            locale.setKey( "locale" );
            adminMgr.disableRoleConstraint( new Role( TIds.WASHER), locale );
            adminMgr.disableRoleConstraint( new Role( TIds.TELLER), locale );

            RoleConstraint strength = new RoleConstraint();
            locale.setType( RoleConstraint.RCType.USER );
            locale.setKey( "strength" );
            adminMgr.disableRoleConstraint( new Role( TIds.TELLER), locale );

            adminMgr.deleteRole( new Role( TIds.WASHER ) );
            adminMgr.deleteRole( new Role( TIds.TELLER ) );

            adminMgr.deleteUser( new User( "Huey"));
            adminMgr.deleteUser( new User( "Dewey"));
            adminMgr.deleteUser( new User( "Louie"));

            delAdminMgr.delete( new OrgUnit( TIds.DEFAULT, OrgUnit.Type.USER ) );
        }
        catch ( SecurityException ex )
        {
            LOG.error( "SecurityException rc=" + ex.getErrorId() + ", msg=" + ex.getMessage(), ex );
            fail( ex.getMessage() );
        }
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new AdminMgrTests( "testDelAbacConstraints" ) );
        suite.addTest( new AdminMgrTests( "testAddAbacConstraints" ) );
        return suite;
    }
}
