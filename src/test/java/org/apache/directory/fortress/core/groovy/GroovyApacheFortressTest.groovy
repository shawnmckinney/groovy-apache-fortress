package org.apache.directory.fortress.core.groovy

class GroovyApacheFortressTest
{
    GroovyAccessMgr aMgr = new GroovyAccessMgr()

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

        GroovyAccessMgr rbac = new GroovyAccessMgr()
        assert ( rbac.start ( userid, locale: value, roles: ["washers", "tellers"] ) )

        assert rbac.canDo("Currency", "dry")
        assert rbac.canDo("Currency", "rinse")
        assert rbac.canDo("Currency", "soak")

        assert !rbac.canDo("Account", "deposit")
        assert !rbac.canDo("Account", "inquiry")
        assert !rbac.canDo("Account", "withdrawal")

        aMgr.end()

        println ( "End $userid Washer in the $value.")
    }

    def isTeller = { String userid, String password, String value ->

        GroovyAccessMgr rbac = new GroovyAccessMgr()

        assert ( rbac.start ( userid, locale: value, roles: ["washers", "tellers"] ) )

        assert !rbac.canDo("Currency", "dry")
        assert !rbac.canDo("Currency", "rinse")
        assert !rbac.canDo("Currency", "soak")

        assert rbac.canDo( "Account", "deposit")
        assert rbac.canDo( "Account", "inquiry")
        assert rbac.canDo( "Account", "withdrawal")

        aMgr.end()

        println ( "End $userid Teller in the $value.")
    }

    static void main (def args)
    {
        def test = new GroovyApacheFortressTest()
        test.abacConstraints()
        System.exit( 0 );
    }
}