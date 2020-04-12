package org.apache.directory.fortress.core.groovy

import org.apache.directory.fortress.GroovyAccessMgr
import org.apache.directory.fortress.Ids

class FortressAccessMgrTests
{
    def abacConstraints()
    {
        // These should all pass...
        println( 'Test Huey:')
        isNeither ( userId: 'Huey', password: 'password')
        isWasher ( userId: 'Huey', password: 'password', locale: 'North')
        isWasher ( userId: 'Huey', password: 'password', locale: 'South')
        isTeller ( userId: 'Huey', password: 'password', locale: 'East', strength: '2fa', roles: [Ids.WASHER, Ids.TELLER])
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
        assert ( !rbac.inRole ( Ids.WASHER ) )
        assert ( !rbac.inRole ( Ids.TELLER ) )

        assert !rbac.canDo('MONEY', "dry")
        assert !rbac.canDo('MONEY', "rinse")
        assert !rbac.canDo('MONEY', "soak")

        assert !rbac.canDo('ACCT', "deposit")
        assert !rbac.canDo('ACCT', "inquiry")
        assert !rbac.canDo('ACCT', "withdrawal")

        //aMgr.end()
        String userId = options.get('userId')
        println ( "End $userId Neither.")
    }

    def isWasher ( Map options=[:] )
    {
        GroovyAccessMgr rbac = new GroovyAccessMgr()
        assert ( rbac.start ( options ) )
        assert ( rbac.inRole ( Ids.WASHER ) )

        assert rbac.canDo('MONEY', "dry")
        assert rbac.canDo('MONEY', "rinse")
        assert rbac.canDo('MONEY', "soak")

        assert !rbac.canDo('ACCT', "deposit")
        assert !rbac.canDo('ACCT', "inquiry")
        assert !rbac.canDo('ACCT', "withdrawal")

        //aMgr.end()
        String userId = options.get('userId')
        String locale = options.get('locale')
        println ( "End $userId $Ids.WASHER in the $locale.")
    }

    def isTeller ( Map options=[:] )
    {
        GroovyAccessMgr rbac = new GroovyAccessMgr()

        options.put( 'roles', [Ids.WASHER, Ids.TELLER], )
        assert ( rbac.start ( options ) )
        assert ( rbac.inRole ( Ids.TELLER ) )
        //assert ( rbac.start ( userid, locale: 'East', strength: '2fa', roles: [Ids.WASHER, Ids.TELLER] ) )

        assert !rbac.canDo('MONEY', "dry")
        assert !rbac.canDo('MONEY', "rinse")
        assert !rbac.canDo('MONEY', "soak")

        assert rbac.canDo( 'ACCT', "deposit")
        assert rbac.canDo( 'ACCT', "inquiry")
        assert rbac.canDo( 'ACCT', "withdrawal")

        //aMgr.end()
        String userId = options.get('userId')
        String locale = options.get('locale')
        println ( "End $userId $Ids.TELLER in the $locale.")
    }

    static void main (def args)
    {
        def test = new FortressAccessMgrTests()
        test.abacConstraints()
        System.exit( 0 );
    }
}