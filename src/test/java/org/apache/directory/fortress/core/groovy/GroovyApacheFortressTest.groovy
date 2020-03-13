package org.apache.directory.fortress.core.groovy

import org.apache.directory.fortress.core.model.Session

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
        GroovyAccessMgr aMgr = new GroovyAccessMgr()
        Session session = aMgr.createSession( userid, password, 'locale', value )
        assert session != null && session.isAuthenticated()

        assert aMgr.checkAccess(session, "Currency", "dry")
        assert aMgr.checkAccess( session, "Currency", "rinse")
        assert aMgr.checkAccess( session, "Currency", "soak")

        assert !aMgr.checkAccess( session, "Account", "deposit")
        assert !aMgr.checkAccess( session, "Account", "inquiry")
        assert !aMgr.checkAccess( session, "Account", "withdrawal")

        println ( "Now ${userid}'s a Washer in the ${value}.")
    }

    def isTeller = { String userid, String password, String value ->
        GroovyAccessMgr aMgr = new GroovyAccessMgr()
        Session session = aMgr.createSession( userid, password, 'locale', value )
        assert session != null && session.isAuthenticated()

        assert !aMgr.checkAccess(session, "Currency", "dry")
        assert !aMgr.checkAccess(session, "Currency", "rinse")
        assert !aMgr.checkAccess( session, "Currency", "soak")

        assert aMgr.checkAccess( session, "Account", "deposit")
        assert aMgr.checkAccess( session, "Account", "inquiry")
        assert aMgr.checkAccess( session, "Account", "withdrawal")

        println ( "Now ${userid}'s a Teller in the ${value}.")
    }

    static void main (def args)
    {
        def test = new GroovyApacheFortressTest()
        test.abacConstraints()
        System.exit( 0 );
    }
}