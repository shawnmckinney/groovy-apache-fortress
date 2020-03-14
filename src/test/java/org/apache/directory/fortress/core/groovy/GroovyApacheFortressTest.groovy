package org.apache.directory.fortress.core.groovy

class GroovyApacheFortressTest
{
    GroovyAccessMgr aMgr = new GroovyAccessMgr()

    def abacConstraints()
    {
        // These should all pass...
        println( 'Test Curly:')

        isWasher3 ( 'Curly', 'password', 'North')
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

    def isWasher3 = { String userid, String password, String value ->

        assert aMgr.createSession2 (  userid, password, 'locale', value )

        assert aMgr.checkAccess("Currency", "dry")
        assert aMgr.checkAccess( "Currency", "rinse")
        assert aMgr.checkAccess( "Currency", "soak")

        assert !aMgr.checkAccess( "Account", "deposit")
        assert !aMgr.checkAccess( "Account", "inquiry")
        assert !aMgr.checkAccess( "Account", "withdrawal")

        println ( "Now $userid's a Washer in the $value.  3")
        //result = "Now $userid's a Washer in the $value."
    }

    def isWasher = { String userid, String password, String value ->

        assert aMgr.createSession (  userid, password, 'locale', value )

        assert aMgr.checkAccess("Currency", "dry")
        assert aMgr.checkAccess("Currency", "rinse")
        assert aMgr.checkAccess("Currency", "soak")

        assert !aMgr.checkAccess("Account", "deposit")
        assert !aMgr.checkAccess("Account", "inquiry")
        assert !aMgr.checkAccess("Account", "withdrawal")

        println ( "Now $userid's a Washer in the $value.")
    }

    def isTeller = { String userid, String password, String value ->

        assert aMgr.createSession (  userid, password, 'locale', value )

        assert !aMgr.checkAccess("Currency", "dry")
        assert !aMgr.checkAccess("Currency", "rinse")
        assert !aMgr.checkAccess("Currency", "soak")

        assert aMgr.checkAccess( "Account", "deposit")
        assert aMgr.checkAccess( "Account", "inquiry")
        assert aMgr.checkAccess( "Account", "withdrawal")

        println ( "Now $userid's a Teller in the $value.")
    }

    static void main (def args)
    {
        def test = new GroovyApacheFortressTest()
        test.abacConstraints()
        System.exit( 0 );
    }
}