package org.apache.directory.fortress.core.groovy

import org.apache.directory.fortress.GroovyAdminMgr

class AddFortressAbacDataTest
{


    public static final String ADD = 'add'
    public static final String ENABLE = 'enable'
    public static final String DELETE = 'delete'
    public static final String ORGUNIT = 'orgunit'
    public static final String ROLE = 'role'
    public static final String ROLECONSTRAINT = 'roleconstraint'
    public static final String USER = 'user'
    public static final String USERROLE = 'userrole'
    public static final String PERMOBJ = 'permobj'
    public static final String PERMISSION = 'permission'
    public static final String PERMGRANT = 'permgrant'
    public static final String WASHER = 'Washer'
    public static final String TELLER = 'Teller'
    public static final String SDSET = 'sdset'

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

        admin.edit(ADD, ORGUNIT, name: 'Default', type: 'USER' )
        admin.edit(ADD, ROLE, name: WASHER)
        admin.edit(ENABLE, ROLECONSTRAINT, id: WASHER, key: 'locale' )
        admin.edit(ADD, ROLE, name: TELLER)
        admin.edit(ENABLE, ROLECONSTRAINT, id: TELLER, key: 'locale' )
        admin.edit(ENABLE, ROLECONSTRAINT, id: TELLER, key: 'strength'  )
        admin.edit(ADD, SDSET, type: 'DYNAMIC', name: 'Bankers', members: ["$TELLER", "$WASHER"], cardinality: '2', description: 'Ducks in a row' )

        admin.edit(ADD, USER, userId: 'huey', password: 'password', ou: 'Default', description: 'Groovy Test' )
        admin.edit(ADD, USERROLE, userId: 'huey', name: TELLER)
        admin.edit(ADD, ROLECONSTRAINT, userId: 'huey', id: TELLER, key: 'locale', value: 'East'  )
        admin.edit(ADD, ROLECONSTRAINT, userId: 'huey', id: TELLER, key: 'strength', value: '2FA'  )
        admin.edit(ADD, USERROLE, userId: 'huey', name: WASHER)
        admin.edit(ADD, ROLECONSTRAINT, userId: 'huey', id: WASHER, key: 'locale', value: 'North'  )
        admin.edit(ADD, ROLECONSTRAINT, userId: 'huey', id: WASHER, key: 'locale', value: 'South'  )

        admin.edit(ADD, USER, userId: 'dewey', password: 'password', ou: 'Default', description: 'Groovy Test' )
        admin.edit(ADD, USERROLE, userId: 'dewey', name: TELLER)
        admin.edit(ADD, ROLECONSTRAINT, userId: 'dewey', id: TELLER, key: 'locale', value: 'North'  )
        admin.edit(ADD, ROLECONSTRAINT, userId: 'dewey', id: TELLER, key: 'strength', value: '2FA'  )
        admin.edit(ADD, USERROLE, userId: 'dewey', name: WASHER)
        admin.edit(ADD, ROLECONSTRAINT, userId: 'dewey', id: WASHER, key: 'locale', value: 'East'  )
        admin.edit(ADD, ROLECONSTRAINT, userId: 'dewey', id: WASHER, key: 'locale', value: 'South'  )

        admin.edit(ADD, USER, userId: 'louie', password: 'password', ou: 'Default', description: 'Groovy Test' )
        admin.edit(ADD, USERROLE, userId: 'louie', name: TELLER)
        admin.edit(ADD, ROLECONSTRAINT, userId: 'louie', id: TELLER, key: 'locale', value: 'South'  )
        admin.edit(ADD, ROLECONSTRAINT, userId: 'louie', id: TELLER, key: 'strength', value: '2FA'  )
        admin.edit(ADD, USERROLE, userId: 'louie', name: WASHER)
        admin.edit(ADD, ROLECONSTRAINT, userId: 'louie', id: WASHER, key: 'locale', value: 'North'  )
        admin.edit(ADD, ROLECONSTRAINT, userId: 'louie', id: WASHER, key: 'locale', value: 'East'  )

        admin.edit(ADD, ORGUNIT, name: 'Default', type: 'PERM' )
        admin.edit(ADD, PERMOBJ, objName: 'Currency', ou: 'Default' )
        admin.edit(ADD, PERMISSION, objName: 'Currency', opName: 'dry' )
        admin.edit(ADD, PERMISSION, objName: 'Currency', opName: 'rinse' )
        admin.edit(ADD, PERMISSION, objName: 'Currency', opName: 'soak' )

        admin.edit(ADD, PERMGRANT, roleNm: WASHER, objName: 'Currency', opName: 'dry' )
        admin.edit(ADD, PERMGRANT, roleNm: WASHER, objName: 'Currency', opName: 'rinse' )
        admin.edit(ADD, PERMGRANT, roleNm: WASHER, objName: 'Currency', opName: 'soak' )

        admin.edit(ADD, PERMOBJ, objName: 'Account', ou: 'Default' )
        admin.edit(ADD, PERMISSION, objName: 'Account', opName: 'deposit' )
        admin.edit(ADD, PERMISSION, objName: 'Account', opName: 'inquiry' )
        admin.edit(ADD, PERMISSION, objName: 'Account', opName: 'withdrawal' )

        admin.edit(ADD, PERMGRANT, roleNm: TELLER, objName: 'Account', opName: 'deposit' )
        admin.edit(ADD, PERMGRANT, roleNm: TELLER, objName: 'Account', opName: 'inquiry' )
        admin.edit(ADD, PERMGRANT, roleNm: TELLER, objName: 'Account', opName: 'withdrawal' )

        //admin.end()
    }

    def del ( )
    {
        GroovyAdminMgr admin = new GroovyAdminMgr()

        admin.edit(DELETE, PERMISSION, objName: 'Currency', opName: 'dry' )
        admin.edit(DELETE, PERMISSION, objName: 'Currency', opName: 'rinse' )
        admin.edit(DELETE, PERMISSION, objName: 'Currency', opName: 'soak' )
        admin.edit(DELETE, PERMOBJ, objName: 'Currency', ou: 'Default' )
        admin.edit(DELETE, PERMISSION, objName: 'Account', opName: 'deposit' )
        admin.edit(DELETE, PERMISSION, objName: 'Account', opName: 'inquiry' )
        admin.edit(DELETE, PERMISSION, objName: 'Account', opName: 'withdrawal' )
        admin.edit(DELETE, PERMOBJ, objName: 'Account', ou: 'Default' )
        admin.edit(DELETE, ORGUNIT, name: 'Default', type: 'PERM' )

        admin.edit(DELETE, SDSET, type: 'DYNAMIC', name: 'Bankers' )
        admin.edit( 'disable', ROLECONSTRAINT, id: WASHER, key: 'locale',  )
        admin.edit(DELETE, ROLE, name: WASHER)
        admin.edit( 'disable', ROLECONSTRAINT, id: TELLER, key: 'locale',  )
        admin.edit( 'disable', ROLECONSTRAINT, id: TELLER, key: 'strength',  )
        admin.edit(DELETE, ROLE, name: TELLER)

        admin.edit(DELETE, USER, userId: 'huey' )
        admin.edit(DELETE, USER, userId: 'dewey' )
        admin.edit(DELETE, USER, userId: 'louie' )
        admin.edit(DELETE, ORGUNIT, name: 'Default', type: 'USER' )

        //admin.end()
    }
}