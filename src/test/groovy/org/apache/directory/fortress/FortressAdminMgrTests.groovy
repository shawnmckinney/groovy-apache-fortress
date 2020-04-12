package org.apache.directory.fortress.core.groovy

import org.apache.directory.fortress.GroovyAdminMgr
import org.apache.directory.fortress.Ids

class FortressAdminMgrTests
{
    static void main (def args)
    {
        def test = new FortressAdminMgrTests()
        test.begin()
        System.exit( 0 );
    }

    def begin()
    {
        println( 'Get them ducks in a row.')
        del ( )
        add ( )
    }

    def add ( )
    {
        GroovyAdminMgr admin = new GroovyAdminMgr()

        admin.edit( Ids.ADD,    Ids.ORGUNIT,        name: 'Default', type: 'USER' )
        admin.edit( Ids.ADD,    Ids.ROLE,           name: Ids.WASHER)
        admin.edit( Ids.ENABLE, Ids.ROLECONSTRAINT, id: Ids.WASHER, key: 'locale' )
        admin.edit( Ids.ADD,    Ids.ROLE,           name: Ids.TELLER)
        admin.edit( Ids.ENABLE, Ids.ROLECONSTRAINT, id: Ids.TELLER, key: 'locale' )
        admin.edit( Ids.ENABLE, Ids.ROLECONSTRAINT, id: Ids.TELLER, key: 'strength'  )
        admin.edit( Ids.ADD,    Ids.SDSET,          type: 'DYNAMIC', name: 'Bankers', members: ["$Ids.TELLER", "$Ids.WASHER"], cardinality: '2', description: 'Ducks in a row' )

        admin.edit( Ids.ADD, Ids.USER,              userId: 'huey', password: 'password', ou: 'Default', description: 'Groovy Test' )
        admin.edit( Ids.ADD, Ids.USERROLE,          userId: 'huey', name: Ids.TELLER)
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'huey', id: Ids.TELLER, key: 'locale', value: 'East'  )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'huey', id: Ids.TELLER, key: 'strength', value: '2FA'  )
        admin.edit( Ids.ADD, Ids.USERROLE,          userId: 'huey', name: Ids.WASHER)
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'huey', id: Ids.WASHER, key: 'locale', value: 'North'  )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'huey', id: Ids.WASHER, key: 'locale', value: 'South'  )

        admin.edit( Ids.ADD, Ids.USER,              userId: 'dewey', password: 'password', ou: 'Default', description: 'Groovy Test' )
        admin.edit( Ids.ADD, Ids.USERROLE,          userId: 'dewey', name: Ids.TELLER)
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'dewey', id: Ids.TELLER, key: 'locale', value: 'North'  )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'dewey', id: Ids.TELLER, key: 'strength', value: '2FA'  )
        admin.edit( Ids.ADD, Ids.USERROLE,          userId: 'dewey', name: Ids.WASHER)
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'dewey', id: Ids.WASHER, key: 'locale', value: 'East'  )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'dewey', id: Ids.WASHER, key: 'locale', value: 'South'  )

        admin.edit( Ids.ADD, Ids.USER,              userId: 'louie', password: 'password', ou: 'Default', description: 'Groovy Test' )
        admin.edit( Ids.ADD, Ids.USERROLE,          userId: 'louie', name: Ids.TELLER)
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'louie', id: Ids.TELLER, key: 'locale', value: 'South'  )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'louie', id: Ids.TELLER, key: 'strength', value: '2FA'  )
        admin.edit( Ids.ADD, Ids.USERROLE,          userId: 'louie', name: Ids.WASHER)
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'louie', id: Ids.WASHER, key: 'locale', value: 'North'  )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'louie', id: Ids.WASHER, key: 'locale', value: 'East'  )

        admin.edit( Ids.ADD, Ids.ORGUNIT,           name: 'Default', type: 'PERM' )
        admin.edit( Ids.ADD, Ids.PERMOBJ,           objName: 'Currency', ou: 'Default' )
        admin.edit( Ids.ADD, Ids.PERMISSION,        objName: 'Currency', opName: 'dry' )
        admin.edit( Ids.ADD, Ids.PERMISSION,        objName: 'Currency', opName: 'rinse' )
        admin.edit( Ids.ADD, Ids.PERMISSION,        objName: 'Currency', opName: 'soak' )

        admin.edit( Ids.ADD, Ids.PERMGRANT,         roleNm: Ids.WASHER, objName: 'Currency', opName: 'dry' )
        admin.edit( Ids.ADD, Ids.PERMGRANT,         roleNm: Ids.WASHER, objName: 'Currency', opName: 'rinse' )
        admin.edit( Ids.ADD, Ids.PERMGRANT,         roleNm: Ids.WASHER, objName: 'Currency', opName: 'soak' )

        admin.edit( Ids.ADD, Ids.PERMOBJ,           objName: 'Account', ou: 'Default' )
        admin.edit( Ids.ADD, Ids.PERMISSION,        objName: 'Account', opName: 'deposit' )
        admin.edit( Ids.ADD, Ids.PERMISSION,        objName: 'Account', opName: 'inquiry' )
        admin.edit( Ids.ADD, Ids.PERMISSION,        objName: 'Account', opName: 'withdrawal' )

        admin.edit( Ids.ADD, Ids.PERMGRANT,         roleNm: Ids.TELLER, objName: 'Account', opName: 'deposit' )
        admin.edit( Ids.ADD, Ids.PERMGRANT,         roleNm: Ids.TELLER, objName: 'Account', opName: 'inquiry' )
        admin.edit( Ids.ADD, Ids.PERMGRANT,         roleNm: Ids.TELLER, objName: 'Account', opName: 'withdrawal' )

        //admin.end()
    }

    def del ( )
    {
        GroovyAdminMgr admin = new GroovyAdminMgr()

        admin.edit( Ids.DELETE, Ids.PERMISSION, objName: 'Currency', opName: 'dry' )
        admin.edit( Ids.DELETE, Ids.PERMISSION, objName: 'Currency', opName: 'rinse' )
        admin.edit( Ids.DELETE, Ids.PERMISSION, objName: 'Currency', opName: 'soak' )
        admin.edit( Ids.DELETE, Ids.PERMOBJ,    objName: 'Currency', ou: 'Default' )
        admin.edit( Ids.DELETE, Ids.PERMISSION, objName: 'Account', opName: 'deposit' )
        admin.edit( Ids.DELETE, Ids.PERMISSION, objName: 'Account', opName: 'inquiry' )
        admin.edit( Ids.DELETE, Ids.PERMISSION, objName: 'Account', opName: 'withdrawal' )
        admin.edit( Ids.DELETE, Ids.PERMOBJ,    objName: 'Account', ou: 'Default' )
        admin.edit( Ids.DELETE, Ids.ORGUNIT,    name: 'Default', type: 'PERM' )

        admin.edit( Ids.DELETE, Ids.SDSET,      type: 'DYNAMIC', name: 'Bankers' )
        admin.edit( Ids.DISABLE,Ids.ROLECONSTRAINT, id: Ids.WASHER, key: 'locale',  )
        admin.edit( Ids.DELETE, Ids.ROLE,       name: Ids.WASHER)
        admin.edit( Ids.DISABLE,Ids.ROLECONSTRAINT, id: Ids.TELLER, key: 'locale',  )
        admin.edit( Ids.DISABLE,Ids.ROLECONSTRAINT, id: Ids.TELLER, key: 'strength',  )
        admin.edit( Ids.DELETE, Ids.ROLE,       name: Ids.TELLER)

        admin.edit( Ids.DELETE, Ids.USER, userId: 'huey' )
        admin.edit( Ids.DELETE, Ids.USER, userId: 'dewey' )
        admin.edit( Ids.DELETE, Ids.USER, userId: 'louie' )
        admin.edit( Ids.DELETE, Ids.ORGUNIT, name: 'Default', type: 'USER' )

        //admin.end()
    }
}