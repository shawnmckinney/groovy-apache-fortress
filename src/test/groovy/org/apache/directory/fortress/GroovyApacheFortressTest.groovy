package org.apache.directory.fortress.core.groovy

import org.apache.directory.fortress.GroovyAccessMgr

class GroovyApacheFortressTest
{
    //GroovyAccessMgr aMgr = new GroovyAccessMgr()

    def abacConstraints()
    {
        // These should all pass...
        println( 'Test Huey:')
        isNeither ( 'Huey', 'password')
        isWasher ( 'Huey', 'password', 'North')
        isWasher ( 'Huey', 'password', 'South')
        isTeller ( 'Huey', 'password', locale: 'East', strength: '2fa', roles: ["washer", "teller"])

        println( 'Test Dewey:')
        isNeither ( 'Dewey', 'password')
        isWasher ( 'Dewey', 'password', 'East')
        isWasher ( 'Dewey', 'password', 'South')
        isTeller ( 'Dewey', 'password', locale: 'North', strength: '2fa')

        println( 'Test Louie:')
        isNeither ( 'Louie', null)
        isWasher ( 'Louie', null, 'North')
        isWasher ( 'Louie', null, 'East')
        isTeller ( 'Louie', null, locale: 'South', strength: '2fa')
    }

    def isNeither ( String userid, String password )
    {
        GroovyAccessMgr rbac = new GroovyAccessMgr()
        // if we don't load specify locale constraint, neither role will be activated:
        assert ( rbac.start ( userid, roles: ["washer", "teller"] ) )

        assert !rbac.canDo("Currency", "dry")
        assert !rbac.canDo("Currency", "rinse")
        assert !rbac.canDo("Currency", "soak")

        assert !rbac.canDo("Account", "deposit")
        assert !rbac.canDo("Account", "inquiry")
        assert !rbac.canDo("Account", "withdrawal")

        //aMgr.end()

        println ( "End $userid Neither.")
    }

    def isWasher ( String userid, String password, String value )
    {
        GroovyAccessMgr rbac = new GroovyAccessMgr()
        assert ( rbac.start ( userid, locale: value, roles: ["washer", "teller"] ) )

        assert rbac.canDo("Currency", "dry")
        assert rbac.canDo("Currency", "rinse")
        assert rbac.canDo("Currency", "soak")

        assert !rbac.canDo("Account", "deposit")
        assert !rbac.canDo("Account", "inquiry")
        assert !rbac.canDo("Account", "withdrawal")

        //aMgr.end()

        println ( "End $userid Washer in the $value.")
    }

    def isTeller ( Map options=[:], String userid, String password=null )
    {
        GroovyAccessMgr rbac = new GroovyAccessMgr()

        options.put( 'roles', ["washer", "teller"], )
        assert ( rbac.start ( options, userid ) )
        //assert ( rbac.start ( userid, locale: 'East', strength: '2fa', roles: ["washer", "teller"] ) )

        assert !rbac.canDo("Currency", "dry")
        assert !rbac.canDo("Currency", "rinse")
        assert !rbac.canDo("Currency", "soak")

        assert rbac.canDo( "Account", "deposit")
        assert rbac.canDo( "Account", "inquiry")
        assert rbac.canDo( "Account", "withdrawal")

        //aMgr.end()

        String value = options.get('locale')
        println ( "End $userid Teller in the $value.")
    }

    static void main (def args)
    {
        def test = new GroovyApacheFortressTest()
        test.abacConstraints()
        System.exit( 0 );
    }
}