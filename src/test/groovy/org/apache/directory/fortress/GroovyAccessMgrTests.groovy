/*  © 2020 iamfortress.net   */
package org.apache.directory.fortress


class GroovyAccessMgrTests
{
    def abacConstraints()
    {
        // These should all pass...
        println( 'Test Huey:')
        isNeither ( userId: 'Huey', password: 'password')
        isWasher ( userId: 'Huey', password: 'password', locale: 'North')
        isWasher ( userId: 'Huey', password: 'password', locale: 'South')
        isTeller ( userId: 'Huey', password: 'password', locale: 'East', strength: '2fa', roles: [TIds.WASHER, TIds.TELLER])
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

    def isNeither ( Map<String,?> options=[:] )
    {
        GroovyAccessMgr rbac = new GroovyAccessMgr()
        // if we don't load specify locale constraint, neither role will be activated:
        assert ( rbac.start ( options ) )

        assert ( !rbac.inRole ( TIds.WASHER ) )
        assert !rbac.canDo('MONEY', "dry")
        assert !rbac.canDo('MONEY', "rinse")
        assert !rbac.canDo('MONEY', "soak")

        assert ( !rbac.inRole ( TIds.TELLER ) )
        assert !rbac.canDo('ACCT', "deposit")
        assert !rbac.canDo('ACCT', "inquiry")
        assert !rbac.canDo('ACCT', "withdrawal")

        //aMgr.end()
        String userId = options.get('userId')
        println ( "End $userId Neither.")
    }

    def isWasher ( Map<String,?> options=[:] )
    {
        GroovyAccessMgr rbac = new GroovyAccessMgr()
        assert ( rbac.start ( options ) )

        assert ( rbac.inRole ( TIds.WASHER ) )
        assert rbac.canDo('MONEY', "dry")
        assert rbac.canDo('MONEY', "rinse")
        assert rbac.canDo('MONEY', "soak")

        assert ( !rbac.inRole ( TIds.TELLER ) )
        assert !rbac.canDo('ACCT', "deposit")
        assert !rbac.canDo('ACCT', "inquiry")
        assert !rbac.canDo('ACCT', "withdrawal")

        //aMgr.end()
        String userId = options.get('userId')
        String locale = options.get('locale')
        println ( "End $userId $TIds.WASHER in the $locale.")
    }

    def isTeller ( Map<String,?> options=[:] )
    {
        GroovyAccessMgr rbac = new GroovyAccessMgr()

        options.put( 'roles', [TIds.WASHER, TIds.TELLER], )
        assert ( rbac.start ( options ) )
        assert ( rbac.inRole ( TIds.TELLER ) )
        //assert ( rbac.start ( userid, locale: 'East', strength: '2fa', roles: [Ids.WASHER, Ids.TELLER] ) )
        assert rbac.canDo( 'ACCT', "deposit")
        assert rbac.canDo( 'ACCT', "inquiry")
        assert rbac.canDo( 'ACCT', "withdrawal")

        assert ( !rbac.inRole ( TIds.WASHER ) )
        assert !rbac.canDo('MONEY', "dry")
        assert !rbac.canDo('MONEY', "rinse")
        assert !rbac.canDo('MONEY', "soak")

        //aMgr.end()
        String userId = options.get('userId')
        String locale = options.get('locale')
        println ( "End $userId $TIds.TELLER in the $locale.")
    }

    static void main (def args)
    {
        def test = new GroovyAccessMgrTests()
        test.abacConstraints()
        System.exit( 0 );
    }
}
