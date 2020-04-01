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

        GroovyAccessMgr rbac = new GroovyAccessMgr()
        //assert { rbac.start userid, "foo", 'locale', value }
        //assert { rbac.start userid, null, 'locale', value }
        //def result = rbac.start4 userid, null, 'locale', value
        assert { rbac.start4 userid, null, 'locale', value }
        //println "start: $result"
        //def result = rbac.canDo "Currency", "dry"
        assert { rbac.canDo "Currency", "dry" }
        //println "canDo: $result"
        def result = rbac.canDo "Account", "deposit"
        assert (!result)
        //assert { rbac.canDo "Currency", "dry" }
        //assert { rbac.canDo "Currency", "soak" }

        //assert not { rbac.canDo "Account", "deposit" } == false
        //assert { rbac.canDo "Account", "deposit" } == false
        //def result = rbac.canDo "Account", "deposit"
        //assert !( result )

        //assert ( aMgr.canDo "Currency", "dry" )
/*
        assert aMgr.isOk( "Currency", "rinse")
        assert aMgr.isOk( "Currency", "soak")

        assert !aMgr.isOk( "Account", "deposit")
        assert !aMgr.isOk( "Account", "inquiry")
        assert !aMgr.isOk( "Account", "withdrawal")
*/

        println ( "End $userid Washer in the $value.")
        //result = "Now $userid's a Washer in the $value."
    }

    def not = { arg ->
        return arg == false
    }

    def isWasher = { String userid, String password, String value ->

        assert aMgr.start (  userid, password, 'locale', value )

        assert aMgr.isOk("Currency", "dry")
        assert aMgr.isOk("Currency", "rinse")
        assert aMgr.isOk("Currency", "soak")

        assert !aMgr.isOk("Account", "deposit")
        assert !aMgr.isOk("Account", "inquiry")
        assert !aMgr.isOk("Account", "withdrawal")

        aMgr.end()

        println ( "End $userid Washer in the $value.")
    }

    def isTeller = { String userid, String password, String value ->

        assert aMgr.start (  userid, password, 'locale', value )

        assert !aMgr.isOk("Currency", "dry")
        assert !aMgr.isOk("Currency", "rinse")
        assert !aMgr.isOk("Currency", "soak")

        assert aMgr.isOk( "Account", "deposit")
        assert aMgr.isOk( "Account", "inquiry")
        assert aMgr.isOk( "Account", "withdrawal")

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