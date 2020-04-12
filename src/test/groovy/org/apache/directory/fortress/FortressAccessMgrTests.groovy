package org.apache.directory.fortress.core.groovy

import org.apache.directory.fortress.GroovyAccessMgr

class FortressAccessMgrTests
{
    def abacConstraints()
    {
        // These should all pass...
        println( 'Test Huey:')
        isNeither ( userId: 'Huey', password: 'password')
        isWasher ( userId: 'Huey', password: 'password', locale: 'North')
        isWasher ( userId: 'Huey', password: 'password', locale: 'South')
        isTeller ( userId: 'Huey', password: 'password', locale: 'East', strength: '2fa', roles: ["washer", "teller"])
        isNeither ( userId: 'Huey', password: 'password', locale: 'East')

        println( 'Test Dewey:')
        isNeither ( userId: 'Dewey', password: 'password')
        isWasher ( userId: 'Dewey', password: 'password', locale:'East')
        isWasher ( userId: 'Dewey', password: 'password', locale:'South')
        isTeller ( userId: 'Dewey', password: 'password', locale: 'North', strength: '2fa')
        isNeither ( userId: 'Dewey', password: 'password', locale: 'North')

        println( 'Test Louie:')
        isNeither ( userId: 'Louie' )
        isWasher ( userId: 'Louie', locale: 'North')
        isWasher ( userId: 'Louie', locale: 'East')
        isTeller ( userId: 'Louie', locale: 'South', strength: '2fa')
        isNeither ( userId: 'Louie', locale: 'South' )
    }

    def isNeither ( Map options=[:] )
    {
        GroovyAccessMgr rbac = new GroovyAccessMgr()
        // if we don't load specify locale constraint, neither role will be activated:
        assert ( rbac.start ( options ) )

        assert !rbac.canDo("Currency", "dry")
        assert !rbac.canDo("Currency", "rinse")
        assert !rbac.canDo("Currency", "soak")

        assert !rbac.canDo("Account", "deposit")
        assert !rbac.canDo("Account", "inquiry")
        assert !rbac.canDo("Account", "withdrawal")

        //aMgr.end()
        String userId = options.get('userId')
        println ( "End $userId Neither.")
    }

    def isWasher ( Map options=[:] )
    {
        GroovyAccessMgr rbac = new GroovyAccessMgr()
        assert ( rbac.start ( options ) )

        assert rbac.canDo("Currency", "dry")
        assert rbac.canDo("Currency", "rinse")
        assert rbac.canDo("Currency", "soak")

        assert !rbac.canDo("Account", "deposit")
        assert !rbac.canDo("Account", "inquiry")
        assert !rbac.canDo("Account", "withdrawal")

        //aMgr.end()
        String userId = options.get('userId')
        String locale = options.get('locale')
        println ( "End $userId Washer in the $locale.")
    }

    def isTeller ( Map options=[:] )
    {
        GroovyAccessMgr rbac = new GroovyAccessMgr()

        options.put( 'roles', ["washer", "teller"], )
        assert ( rbac.start ( options ) )
        //assert ( rbac.start ( userid, locale: 'East', strength: '2fa', roles: ["washer", "teller"] ) )

        assert !rbac.canDo("Currency", "dry")
        assert !rbac.canDo("Currency", "rinse")
        assert !rbac.canDo("Currency", "soak")

        assert rbac.canDo( "Account", "deposit")
        assert rbac.canDo( "Account", "inquiry")
        assert rbac.canDo( "Account", "withdrawal")

        //aMgr.end()
        String userId = options.get('userId')
        String locale = options.get('locale')
        println ( "End $userId Teller in the $locale.")
    }

    static void main (def args)
    {
        def test = new FortressAccessMgrTests()
        test.abacConstraints()
        System.exit( 0 );
    }
}