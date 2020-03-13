package org.apache.directory.fortress.core.groovy

import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.AccessMgrFactory
import org.apache.directory.fortress.core.model.Permission
import org.apache.directory.fortress.core.model.RoleConstraint
import org.apache.directory.fortress.core.model.Session
import org.apache.directory.fortress.core.model.User;
//import org.apache.directory.fortress.core.*

class GroovyApacheFortressTest
{
    def abacConstraints()
    {
        print( 'This is my first Groovy Example')

        isWasher ( 'curly', 'password', 'north')
        isWasher ( 'curly', 'password', 'south')
        isTeller ( 'curly', 'password', 'east')

        isWasher ( 'moe', 'password', 'east')
        isWasher ( 'moe', 'password', 'south')
        isTeller ( 'moe', 'password', 'north')

        isWasher ( 'larry', 'password', 'north')
        isWasher ( 'larry', 'password', 'east')
        isTeller ( 'larry', 'password', 'south')
    }


    def isWasher = { String userid, String password, String value ->
        Session session = createSession( userid, password, 'locale', value )
        assert checkAccess( session, "Currency", "dry") == true
        assert checkAccess( session, "Currency", "rinse") == true
        assert checkAccess( session, "Currency", "soak") == true

        assert checkAccess( session, "Account", "deposit") == false
        assert checkAccess( session, "Account", "inquiry") == false
        assert checkAccess( session, "Account", "withdrawal") == false

        println ( "${userid} is a washer")
    }

    def isTeller = { String userid, String password, String value ->
        Session session = createSession( userid, password, 'locale', value )
        assert checkAccess( session, "Currency", "dry") == false
        assert checkAccess( session, "Currency", "rinse") == false
        assert checkAccess( session, "Currency", "soak") == false

        assert checkAccess( session, "Account", "deposit") == true
        assert checkAccess( session, "Account", "inquiry") == true
        assert checkAccess( session, "Account", "withdrawal") == true

        println ( "${userid} is a teller")
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
        def first = new GroovyApacheFortressTest()
        first.abacConstraints()
        System.exit( 0 );
    }
}