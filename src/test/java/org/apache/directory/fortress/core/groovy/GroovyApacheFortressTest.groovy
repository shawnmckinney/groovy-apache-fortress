package org.apache.directory.fortress.core.groovy

import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.AccessMgrFactory
import org.apache.directory.fortress.core.model.Permission
import org.apache.directory.fortress.core.model.RoleConstraint
import org.apache.directory.fortress.core.model.Session
import org.apache.directory.fortress.core.model.User;

class GroovyApacheFortressTest
{
    def abacConstraints()
    {
        // These should all pass...
        println( 'Test Curly:')
        isWasher ( 'Curly', 'password', 'North')
        isWasher ( 'Curly', 'password', 'South')
        isTeller ( 'Curly', 'password', 'East')

        println( 'Test Moe:')
        isWasher ( 'Moe', 'password', 'East')
        isWasher ( 'Moe', 'password', 'South')
        isTeller ( 'Moe', 'password', 'North')

        println( 'Test Larry:')
        isWasher ( 'Larry', 'password', 'North')
        isWasher ( 'Larry', 'password', 'East')
        isTeller ( 'Larry', 'password', 'South')
    }

    def isWasher = { String userid, String password, String value ->
        Session session = createSession( userid, password, 'locale', value )
        assert session != null && session.isAuthenticated()
        assert checkAccess( session, "Currency", "dry") == true
        assert checkAccess( session, "Currency", "rinse") == true
        assert checkAccess( session, "Currency", "soak") == true

        assert checkAccess( session, "Account", "deposit") == false
        assert checkAccess( session, "Account", "inquiry") == false
        assert checkAccess( session, "Account", "withdrawal") == false

        println ( "Now ${userid}'s a Washer in the ${value}.")
    }

    def isTeller = { String userid, String password, String value ->
        Session session = createSession( userid, password, 'locale', value )
        assert session != null && session.isAuthenticated()
        assert checkAccess( session, "Currency", "dry") == false
        assert checkAccess( session, "Currency", "rinse") == false
        assert checkAccess( session, "Currency", "soak") == false

        assert checkAccess( session, "Account", "deposit") == true
        assert checkAccess( session, "Account", "inquiry") == true
        assert checkAccess( session, "Account", "withdrawal") == true

        println ( "Now ${userid}'s a Teller in the ${value}.")
    }

    def createSession = { String userId, String password, String key, String value ->
        AccessMgr accessMgr = AccessMgrFactory.createInstance()
        List<RoleConstraint> constraints = new ArrayList()
        RoleConstraint constraint = new RoleConstraint()
        constraint.setKey( key )
        constraint.setValue( value )
        constraints.add( constraint )
        return accessMgr.createSession( new User ( userId, password ), constraints, false)
    }

    def checkAccess = { Session session, String object, String operation ->
        AccessMgr accessMgr = AccessMgrFactory.createInstance()
        return accessMgr.checkAccess( session, new Permission( object, operation ) )
    }

    static void main (def args)
    {
        def test = new GroovyApacheFortressTest()
        test.abacConstraints()
        System.exit( 0 );
    }
}