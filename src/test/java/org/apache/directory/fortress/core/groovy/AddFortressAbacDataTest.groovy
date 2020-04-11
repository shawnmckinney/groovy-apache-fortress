package org.apache.directory.fortress.core.groovy

import org.apache.directory.fortress.core.model.SDSet

class AddFortressAbacDataTest
{
    GroovyAccessMgr aMgr = new GroovyAccessMgr()

    def begin()
    {
        // These should all pass...
        println( 'Getting the ducks in a row...')
        del ( )
        add ( )
    }

    def add ( )
    {
        GroovyAdminMgr admin = new GroovyAdminMgr()

        admin.doIt( 'add', 'orgunit', name: 'Default', type: 'USER' )
        admin.doIt( 'add', 'role', name: 'Washer' )
        admin.doIt( 'enable', 'roleconstraint', id: 'Washer', key: 'locale' )
        admin.doIt( 'add', 'role', name: 'Teller' )
        admin.doIt( 'enable', 'roleconstraint', id: 'Teller', key: 'locale' )
        admin.doIt( 'enable', 'roleconstraint', id: 'Teller', key: 'strength'  )
        admin.doIt( 'add', 'sdset', type: 'DYNAMIC', name: 'Bankers', members: ["Teller", "Washer"], cardinality: '2', description: 'Ducks in a row' )

        admin.doIt( 'add', 'user', userId: 'huey', password: 'password', ou: 'Default', description: 'Groovy Test' )
        admin.doIt( 'add', 'userrole', userId: 'huey', name: 'Teller' )
        admin.doIt( 'add', 'roleconstraint', userId: 'huey', id: 'Teller', key: 'locale', value: 'East'  )
        admin.doIt( 'add', 'roleconstraint', userId: 'huey', id: 'Teller', key: 'strength', value: '2FA'  )
        admin.doIt( 'add', 'userrole', userId: 'huey', name: 'Washer' )
        admin.doIt( 'add', 'roleconstraint', userId: 'huey', id: 'Washer', key: 'locale', value: 'North'  )
        admin.doIt( 'add', 'roleconstraint', userId: 'huey', id: 'Washer', key: 'locale', value: 'South'  )

        admin.doIt( 'add', 'user', userId: 'dewey', password: 'password', ou: 'Default', description: 'Groovy Test' )
        admin.doIt( 'add', 'userrole', userId: 'dewey', name: 'Teller' )
        admin.doIt( 'add', 'roleconstraint', userId: 'dewey', id: 'Teller', key: 'locale', value: 'North'  )
        admin.doIt( 'add', 'roleconstraint', userId: 'dewey', id: 'Teller', key: 'strength', value: '2FA'  )
        admin.doIt( 'add', 'userrole', userId: 'dewey', name: 'Washer' )
        admin.doIt( 'add', 'roleconstraint', userId: 'dewey', id: 'Washer', key: 'locale', value: 'East'  )
        admin.doIt( 'add', 'roleconstraint', userId: 'dewey', id: 'Washer', key: 'locale', value: 'South'  )

        admin.doIt( 'add', 'user', userId: 'louie', password: 'password', ou: 'Default', description: 'Groovy Test' )
        admin.doIt( 'add', 'userrole', userId: 'louie', name: 'Teller' )
        admin.doIt( 'add', 'roleconstraint', userId: 'louie', id: 'Teller', key: 'locale', value: 'South'  )
        admin.doIt( 'add', 'roleconstraint', userId: 'louie', id: 'Teller', key: 'strength', value: '2FA'  )
        admin.doIt( 'add', 'userrole', userId: 'louie', name: 'Washer' )
        admin.doIt( 'add', 'roleconstraint', userId: 'louie', id: 'Washer', key: 'locale', value: 'North'  )
        admin.doIt( 'add', 'roleconstraint', userId: 'louie', id: 'Washer', key: 'locale', value: 'East'  )

        admin.doIt( 'add', 'orgunit', name: 'Default', type: 'PERM' )
        admin.doIt( 'add', 'permobj', objName: 'Currency', ou: 'Default' )
        admin.doIt( 'add', 'permission', objName: 'Currency', opName: 'dry' )
        admin.doIt( 'add', 'permission', objName: 'Currency', opName: 'rinse' )
        admin.doIt( 'add', 'permission', objName: 'Currency', opName: 'soak' )

        admin.doIt( 'add', 'permgrant', roleNm: 'Washer', objName: 'Currency', opName: 'dry' )
        admin.doIt( 'add', 'permgrant', roleNm: 'Washer', objName: 'Currency', opName: 'rinse' )
        admin.doIt( 'add', 'permgrant', roleNm: 'Washer', objName: 'Currency', opName: 'soak' )

        admin.doIt( 'add', 'permobj', objName: 'Account', ou: 'Default' )
        admin.doIt( 'add', 'permission', objName: 'Account', opName: 'deposit' )
        admin.doIt( 'add', 'permission', objName: 'Account', opName: 'inquiry' )
        admin.doIt( 'add', 'permission', objName: 'Account', opName: 'withdrawal' )

        admin.doIt( 'add', 'permgrant', roleNm: 'Teller', objName: 'Account', opName: 'deposit' )
        admin.doIt( 'add', 'permgrant', roleNm: 'Teller', objName: 'Account', opName: 'inquiry' )
        admin.doIt( 'add', 'permgrant', roleNm: 'Teller', objName: 'Account', opName: 'withdrawal' )

        aMgr.end()
    }

    def del ( )
    {
        GroovyAdminMgr admin = new GroovyAdminMgr()

        admin.doIt( 'delete', 'permission', objName: 'Currency', opName: 'dry' )
        admin.doIt( 'delete', 'permission', objName: 'Currency', opName: 'rinse' )
        admin.doIt( 'delete', 'permission', objName: 'Currency', opName: 'soak' )
        admin.doIt( 'delete', 'permobj', objName: 'Currency', ou: 'Default' )
        admin.doIt( 'delete', 'permission', objName: 'Account', opName: 'deposit' )
        admin.doIt( 'delete', 'permission', objName: 'Account', opName: 'inquiry' )
        admin.doIt( 'delete', 'permission', objName: 'Account', opName: 'withdrawal' )
        admin.doIt( 'delete', 'permobj', objName: 'Account', ou: 'Default' )
        admin.doIt( 'delete', 'orgunit', name: 'Default', type: 'PERM' )

        admin.doIt( 'delete', 'sdset', type: 'DYNAMIC', name: 'Bankers' )
        admin.doIt( 'disable', 'roleconstraint', id: 'Washer', key: 'locale',  )
        admin.doIt( 'delete', 'role', name: 'Washer' )
        admin.doIt( 'disable', 'roleconstraint', id: 'Teller', key: 'locale',  )
        admin.doIt( 'disable', 'roleconstraint', id: 'Teller', key: 'strength',  )
        admin.doIt( 'delete', 'role', name: 'Teller' )

        admin.doIt( 'delete', 'user', userId: 'huey' )
        admin.doIt( 'delete', 'user', userId: 'dewey' )
        admin.doIt( 'delete', 'user', userId: 'louie' )
        admin.doIt( 'delete', 'orgunit', name: 'Default', type: 'USER' )

        aMgr.end()
    }

    static void main (def args)
    {
        def test = new AddFortressAbacDataTest()
        test.begin()
        System.exit( 0 );
    }
}