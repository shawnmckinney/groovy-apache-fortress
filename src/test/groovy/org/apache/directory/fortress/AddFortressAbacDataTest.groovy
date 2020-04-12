package org.apache.directory.fortress.core.groovy

import org.apache.directory.fortress.GroovyAdminMgr

class AddFortressAbacDataTest
{
    static void main (def args)
    {
        def test = new AddFortressAbacDataTest()
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

        admin.edit( 'add', 'orgunit', name: 'Default', type: 'USER' )
        admin.edit( 'add', 'role', name: 'Washer' )
        admin.edit( 'enable', 'roleconstraint', id: 'Washer', key: 'locale' )
        admin.edit( 'add', 'role', name: 'Teller' )
        admin.edit( 'enable', 'roleconstraint', id: 'Teller', key: 'locale' )
        admin.edit( 'enable', 'roleconstraint', id: 'Teller', key: 'strength'  )
        admin.edit( 'add', 'sdset', type: 'DYNAMIC', name: 'Bankers', members: ["Teller", "Washer"], cardinality: '2', description: 'Ducks in a row' )

        admin.edit( 'add', 'user', userId: 'huey', password: 'password', ou: 'Default', description: 'Groovy Test' )
        admin.edit( 'add', 'userrole', userId: 'huey', name: 'Teller' )
        admin.edit( 'add', 'roleconstraint', userId: 'huey', id: 'Teller', key: 'locale', value: 'East'  )
        admin.edit( 'add', 'roleconstraint', userId: 'huey', id: 'Teller', key: 'strength', value: '2FA'  )
        admin.edit( 'add', 'userrole', userId: 'huey', name: 'Washer' )
        admin.edit( 'add', 'roleconstraint', userId: 'huey', id: 'Washer', key: 'locale', value: 'North'  )
        admin.edit( 'add', 'roleconstraint', userId: 'huey', id: 'Washer', key: 'locale', value: 'South'  )

        admin.edit( 'add', 'user', userId: 'dewey', password: 'password', ou: 'Default', description: 'Groovy Test' )
        admin.edit( 'add', 'userrole', userId: 'dewey', name: 'Teller' )
        admin.edit( 'add', 'roleconstraint', userId: 'dewey', id: 'Teller', key: 'locale', value: 'North'  )
        admin.edit( 'add', 'roleconstraint', userId: 'dewey', id: 'Teller', key: 'strength', value: '2FA'  )
        admin.edit( 'add', 'userrole', userId: 'dewey', name: 'Washer' )
        admin.edit( 'add', 'roleconstraint', userId: 'dewey', id: 'Washer', key: 'locale', value: 'East'  )
        admin.edit( 'add', 'roleconstraint', userId: 'dewey', id: 'Washer', key: 'locale', value: 'South'  )

        admin.edit( 'add', 'user', userId: 'louie', password: 'password', ou: 'Default', description: 'Groovy Test' )
        admin.edit( 'add', 'userrole', userId: 'louie', name: 'Teller' )
        admin.edit( 'add', 'roleconstraint', userId: 'louie', id: 'Teller', key: 'locale', value: 'South'  )
        admin.edit( 'add', 'roleconstraint', userId: 'louie', id: 'Teller', key: 'strength', value: '2FA'  )
        admin.edit( 'add', 'userrole', userId: 'louie', name: 'Washer' )
        admin.edit( 'add', 'roleconstraint', userId: 'louie', id: 'Washer', key: 'locale', value: 'North'  )
        admin.edit( 'add', 'roleconstraint', userId: 'louie', id: 'Washer', key: 'locale', value: 'East'  )

        admin.edit( 'add', 'orgunit', name: 'Default', type: 'PERM' )
        admin.edit( 'add', 'permobj', objName: 'Currency', ou: 'Default' )
        admin.edit( 'add', 'permission', objName: 'Currency', opName: 'dry' )
        admin.edit( 'add', 'permission', objName: 'Currency', opName: 'rinse' )
        admin.edit( 'add', 'permission', objName: 'Currency', opName: 'soak' )

        admin.edit( 'add', 'permgrant', roleNm: 'Washer', objName: 'Currency', opName: 'dry' )
        admin.edit( 'add', 'permgrant', roleNm: 'Washer', objName: 'Currency', opName: 'rinse' )
        admin.edit( 'add', 'permgrant', roleNm: 'Washer', objName: 'Currency', opName: 'soak' )

        admin.edit( 'add', 'permobj', objName: 'Account', ou: 'Default' )
        admin.edit( 'add', 'permission', objName: 'Account', opName: 'deposit' )
        admin.edit( 'add', 'permission', objName: 'Account', opName: 'inquiry' )
        admin.edit( 'add', 'permission', objName: 'Account', opName: 'withdrawal' )

        admin.edit( 'add', 'permgrant', roleNm: 'Teller', objName: 'Account', opName: 'deposit' )
        admin.edit( 'add', 'permgrant', roleNm: 'Teller', objName: 'Account', opName: 'inquiry' )
        admin.edit( 'add', 'permgrant', roleNm: 'Teller', objName: 'Account', opName: 'withdrawal' )

        //admin.end()
    }

    def del ( )
    {
        GroovyAdminMgr admin = new GroovyAdminMgr()

        admin.edit( 'delete', 'permission', objName: 'Currency', opName: 'dry' )
        admin.edit( 'delete', 'permission', objName: 'Currency', opName: 'rinse' )
        admin.edit( 'delete', 'permission', objName: 'Currency', opName: 'soak' )
        admin.edit( 'delete', 'permobj', objName: 'Currency', ou: 'Default' )
        admin.edit( 'delete', 'permission', objName: 'Account', opName: 'deposit' )
        admin.edit( 'delete', 'permission', objName: 'Account', opName: 'inquiry' )
        admin.edit( 'delete', 'permission', objName: 'Account', opName: 'withdrawal' )
        admin.edit( 'delete', 'permobj', objName: 'Account', ou: 'Default' )
        admin.edit( 'delete', 'orgunit', name: 'Default', type: 'PERM' )

        admin.edit( 'delete', 'sdset', type: 'DYNAMIC', name: 'Bankers' )
        admin.edit( 'disable', 'roleconstraint', id: 'Washer', key: 'locale',  )
        admin.edit( 'delete', 'role', name: 'Washer' )
        admin.edit( 'disable', 'roleconstraint', id: 'Teller', key: 'locale',  )
        admin.edit( 'disable', 'roleconstraint', id: 'Teller', key: 'strength',  )
        admin.edit( 'delete', 'role', name: 'Teller' )

        admin.edit( 'delete', 'user', userId: 'huey' )
        admin.edit( 'delete', 'user', userId: 'dewey' )
        admin.edit( 'delete', 'user', userId: 'louie' )
        admin.edit( 'delete', 'orgunit', name: 'Default', type: 'USER' )

        //admin.end()
    }
}