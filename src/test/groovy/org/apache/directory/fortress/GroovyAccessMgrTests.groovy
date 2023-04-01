/*  © 2023 iamfortress.net   */
package org.apache.directory.fortress

class GroovyAccessMgrTests
{
    static void main (def args)
    {
        new GroovyAccessMgrTests().verify()
        System.exit( 0 );
    }

    def verify()
    {
        // These should all pass...
        println( 'Test Huey:')

        // Negative test case, verify constraints are being evaluated:
        isNeither ( userId: 'Huey')
        isWasher ( userId: 'Huey', locale: 'North')
        isWasher ( userId: 'Huey', locale: 'South')
        isTeller ( userId: 'Huey', locale: 'East', strength: '2fa', roles: [TIds.WASHER, TIds.TELLER])
        isNeither ( userId: 'Huey', locale: 'East')

        println( 'Test Dewey:')
        // Negative test case, verify constraints are being evaluated:
        isNeither ( userId: 'Dewey')
        isWasher ( userId: 'Dewey', locale:'East')
        isWasher ( userId: 'Dewey', locale:'South')
        isTeller ( userId: 'Dewey', locale: 'North', strength: '2fa')
        isNeither ( userId: 'Dewey', locale: 'North')

        println( 'Test Louie:')
        // Negative test case, verify constraints are being evaluated:
        isNeither ( userId: 'Louie' )
        isWasher ( userId: 'Louie', locale: 'North')
        isWasher ( userId: 'Louie', locale: 'East')
        isTeller ( userId: 'Louie', locale: 'South', strength: '2fa')
        isNeither ( userId: 'Louie', locale: 'South' )
    }

    def isNeither ( Map<String,?> options=[:] )
    {
        GroovyAccessMgr rbac = new GroovyAccessMgr()
        assert ( rbac.start ( options ) )

        assert ( !rbac.inRole ( TIds.WASHER ) )
        assert !rbac.canDo('MONEY', "dry")
        assert !rbac.canDo('MONEY', "rinse")
        assert !rbac.canDo('MONEY', "soak")

        assert ( !rbac.inRole ( TIds.TELLER ) )
        assert !rbac.canDo('ACCT', "deposit")
        assert !rbac.canDo('ACCT', "inquiry")
        assert !rbac.canDo('ACCT', "withdrawal")
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
    }
}
