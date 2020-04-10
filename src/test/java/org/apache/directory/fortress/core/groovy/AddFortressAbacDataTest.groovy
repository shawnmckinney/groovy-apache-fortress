package org.apache.directory.fortress.core.groovy

class AddFortressAbacDataTest
{
    GroovyAccessMgr aMgr = new GroovyAccessMgr()

    def begin()
    {
        // These should all pass...
        println( 'Test ...:')
        del ( 'curly', 'north' )
        add ( 'curly', 'north' )
    }

    def add ( String userid, String value )
    {
        GroovyAdminMgr admin = new GroovyAdminMgr()

//        admin.doIt( 'delete', 'roleconstraint', id: 'Tellers2', key: 'strength',  )
//        admin.doIt( 'delete', 'userrole', userId: 'curly2', name: 'Tellers2' )
//        admin.doIt( 'add', 'userrole', userId: 'curly2', name: 'Tellers2' )
//        admin.doIt( 'enable', 'roleconstraint', id: 'Tellers2', key: 'strength',  )
        //admin.doIt( 'delete', 'userrole', userId: 'curly2', name: 'Tellers2' )
        //admin.doIt( 'delete', 'roleconstraint', id: 'Tellers2', userId: 'curly2', key: 'strength', value: '2FA'  )
        //admin.doIt( 'add', 'roleconstraint', id: 'Tellers2', userId: 'curly2', key: 'strength', value: '2FA'  )

        admin.doIt( 'add', 'orgunit', name: 'Default', type: 'USER' )
        admin.doIt( 'add', 'role', name: 'Washers2' )
        admin.doIt( 'enable', 'roleconstraint', value: 'Washers2', key: 'locale',  )
        admin.doIt( 'enable', 'roleconstraint', value: 'Washers2', key: 'strength',  )
        admin.doIt( 'add', 'role', name: 'Tellers2' )
        admin.doIt( 'enable', 'roleconstraint', value: 'Tellers2', key: 'locale',  )
        admin.doIt( 'enable', 'roleconstraint', value: 'Tellers2', key: 'strength',  )

        admin.doIt( 'add', 'user', userId: 'curly2', password: 'password', ou: 'Default3', description: 'Groovy Test' )
        admin.doIt( 'add', 'userrole', userId: 'curly2', name: 'Tellers2' )
        admin.doIt( 'add', 'roleconstraint', userId: 'curly2', id: 'Tellers2', key: 'locale', value: 'East'  )
        admin.doIt( 'add', 'roleconstraint', userId: 'curly2', id: 'Tellers2', key: 'strength', value: '2FA'  )

        admin.doIt( 'add', 'userrole', userId: 'curly2', name: 'Washers2' )
        admin.doIt( 'add', 'roleconstraint', userId: 'curly2', id: 'Washers2', key: 'locale', value: 'North'  )
        admin.doIt( 'add', 'roleconstraint', userId: 'curly2', id: 'Washers2', key: 'locale', value: 'South'  )

        admin.doIt( 'add', 'orgunit', name: 'Default', type: 'PERM' )
        admin.doIt( 'add', 'permobj', objName: 'Currency2', ou: 'Default' )
        admin.doIt( 'add', 'permission', objName: 'Currency2', opName: 'dry' )
        admin.doIt( 'add', 'permission', objName: 'Currency2', opName: 'rinse' )
        admin.doIt( 'add', 'permission', objName: 'Currency2', opName: 'soak' )

        admin.doIt( 'add', 'permobj', objName: 'Account2', ou: 'Default' )
        admin.doIt( 'add', 'permission', objName: 'Account2', opName: 'deposit' )
        admin.doIt( 'add', 'permission', objName: 'Account2', opName: 'inquiry' )
        admin.doIt( 'add', 'permission', objName: 'Account2', opName: 'withdrawal' )

        aMgr.end()

        println ( "End $userid add in the $value.")
    }

    def del ( String userid, String value )
    {
        GroovyAdminMgr admin = new GroovyAdminMgr()


        admin.doIt( 'delete', 'permission', objName: 'Currency2', opName: 'dry' )
        admin.doIt( 'delete', 'permission', objName: 'Currency2', opName: 'rinse' )
        admin.doIt( 'delete', 'permission', objName: 'Currency2', opName: 'soak' )
        admin.doIt( 'delete', 'permobj', objName: 'Currency2', ou: 'Default' )
        admin.doIt( 'delete', 'permission', objName: 'Account2', opName: 'deposit' )
        admin.doIt( 'delete', 'permission', objName: 'Account2', opName: 'inquiry' )
        admin.doIt( 'delete', 'permission', objName: 'Account2', opName: 'withdrawal' )
        admin.doIt( 'delete', 'permobj', objName: 'Account2', ou: 'Default' )
        admin.doIt( 'delete', 'orgunit', name: 'Default', type: 'PERM' )

        admin.doIt( 'disable', 'roleconstraint', id: 'Washers2', key: 'locale',  )
        admin.doIt( 'delete', 'role', name: 'Washers2' )
        admin.doIt( 'disable', 'roleconstraint', id: 'Tellers2', key: 'locale',  )
        admin.doIt( 'disable', 'roleconstraint', id: 'Tellers2', key: 'strength',  )
        admin.doIt( 'delete', 'role', name: 'Tellers2' )

        admin.doIt( 'delete', 'user', userId: 'curly2' )
        admin.doIt( 'delete', 'orgunit', name: 'Default', type: 'USER' )

        aMgr.end()

        println ( "End $userid add in the $value.")
    }

    static void main (def args)
    {
        def test = new AddFortressAbacDataTest()
        test.begin()
        System.exit( 0 );
    }
}