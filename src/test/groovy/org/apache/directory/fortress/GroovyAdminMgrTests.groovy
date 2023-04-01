/*  © 2023 iamfortress.net   */
package org.apache.directory.fortress

class GroovyAdminMgrTests
{
    static void main (def args)
    {
        new GroovyAdminMgrTests().delete()
        new GroovyAdminMgrTests().add()
        System.exit( 0 );
    }

    def add ( )
    {
        GroovyAdminMgr admin = new GroovyAdminMgr()
        //         Operation      Entity                Attribute Map
        admin.edit( Ids.ADD,    Ids.ORGUNIT,        name: TIds.DEFAULT, type: 'USER' )
        admin.edit( Ids.ADD,    Ids.ROLE,           name: TIds.WASHER )
        admin.edit( Ids.ENABLE, Ids.ROLECONSTRAINT, id: TIds.WASHER, key: 'locale' )
        admin.edit( Ids.ADD,    Ids.ROLE,           name: TIds.TELLER )
        admin.edit( Ids.ENABLE, Ids.ROLECONSTRAINT, id: TIds.TELLER, key: 'locale' )
        admin.edit( Ids.ENABLE, Ids.ROLECONSTRAINT, id: TIds.TELLER, key: 'strength'  )
        admin.edit( Ids.ADD,    Ids.SDSET,          type: 'DYNAMIC', name: 'Bankers', members: [TIds.TELLER, TIds.WASHER], cardinality: '2', description: 'Ducks in a row' )

        admin.edit( Ids.ADD, Ids.USER,              userId: 'huey', password: 'password', ou: TIds.DEFAULT, description: 'Groovy Test' )
        admin.edit( Ids.ADD, Ids.USERROLE,          userId: 'huey', name: TIds.TELLER )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'huey', id: TIds.TELLER, key: 'locale', value: 'East' )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'huey', id: TIds.TELLER, key: 'strength', value: '2FA' )
        admin.edit( Ids.ADD, Ids.USERROLE,          userId: 'huey', name: TIds.WASHER )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'huey', id: TIds.WASHER, key: 'locale', value: 'North' )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'huey', id: TIds.WASHER, key: 'locale', value: 'South' )

        admin.edit( Ids.ADD, Ids.USER,              userId: 'dewey', password: 'password', ou: TIds.DEFAULT, description: 'Groovy Test' )
        admin.edit( Ids.ADD, Ids.USERROLE,          userId: 'dewey', name: TIds.TELLER )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'dewey', id: TIds.TELLER, key: 'locale', value: 'North' )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'dewey', id: TIds.TELLER, key: 'strength', value: '2FA' )
        admin.edit( Ids.ADD, Ids.USERROLE,          userId: 'dewey', name: TIds.WASHER )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'dewey', id: TIds.WASHER, key: 'locale', value: 'East' )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'dewey', id: TIds.WASHER, key: 'locale', value: 'South' )

        admin.edit( Ids.ADD, Ids.USER,              userId: 'louie', password: 'password', ou: TIds.DEFAULT, description: 'Groovy Test' )
        admin.edit( Ids.ADD, Ids.USERROLE,          userId: 'louie', name: TIds.TELLER )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'louie', id: TIds.TELLER, key: 'locale', value: 'South' )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'louie', id: TIds.TELLER, key: 'strength', value: '2FA' )
        admin.edit( Ids.ADD, Ids.USERROLE,          userId: 'louie', name: TIds.WASHER )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'louie', id: TIds.WASHER, key: 'locale', value: 'North' )
        admin.edit( Ids.ADD, Ids.ROLECONSTRAINT,    userId: 'louie', id: TIds.WASHER, key: 'locale', value: 'East' )

        admin.edit( Ids.ADD, Ids.ORGUNIT,           name: TIds.DEFAULT, type: 'PERM' )
        admin.edit( Ids.ADD, Ids.PERMOBJ,           objName: 'MONEY', ou: TIds.DEFAULT )
        admin.edit( Ids.ADD, Ids.PERMISSION,        objName: 'MONEY', opName: 'dry' )
        admin.edit( Ids.ADD, Ids.PERMISSION,        objName: 'MONEY', opName: 'rinse' )
        admin.edit( Ids.ADD, Ids.PERMISSION,        objName: 'MONEY', opName: 'soak' )

        admin.edit( Ids.ADD, Ids.PERMGRANT,         roleNm: TIds.WASHER, objName: 'MONEY', opName: 'dry' )
        admin.edit( Ids.ADD, Ids.PERMGRANT,         roleNm: TIds.WASHER, objName: 'MONEY', opName: 'rinse' )
        admin.edit( Ids.ADD, Ids.PERMGRANT,         roleNm: TIds.WASHER, objName: 'MONEY', opName: 'soak' )

        admin.edit( Ids.ADD, Ids.PERMOBJ,           objName: 'ACCT', ou: TIds.DEFAULT )
        admin.edit( Ids.ADD, Ids.PERMISSION,        objName: 'ACCT', opName: 'deposit' )
        admin.edit( Ids.ADD, Ids.PERMISSION,        objName: 'ACCT', opName: 'inquiry' )
        admin.edit( Ids.ADD, Ids.PERMISSION,        objName: 'ACCT', opName: 'withdrawal' )

        admin.edit( Ids.ADD, Ids.PERMGRANT,         roleNm: TIds.TELLER, objName: 'ACCT', opName: 'deposit' )
        admin.edit( Ids.ADD, Ids.PERMGRANT,         roleNm: TIds.TELLER, objName: 'ACCT', opName: 'inquiry' )
        admin.edit( Ids.ADD, Ids.PERMGRANT,         roleNm: TIds.TELLER, objName: 'ACCT', opName: 'withdrawal' )
    }

    def delete ( )
    {
        GroovyAdminMgr admin = new GroovyAdminMgr()
        //         Operation      Entity                Attribute Map
        admin.edit( Ids.DELETE, Ids.PERMISSION, objName: 'MONEY', opName: 'dry' )
        admin.edit( Ids.DELETE, Ids.PERMISSION, objName: 'MONEY', opName: 'rinse' )
        admin.edit( Ids.DELETE, Ids.PERMISSION, objName: 'MONEY', opName: 'soak' )
        admin.edit( Ids.DELETE, Ids.PERMOBJ,    objName: 'MONEY', ou: TIds.DEFAULT )
        admin.edit( Ids.DELETE, Ids.PERMISSION, objName: 'ACCT', opName: 'deposit' )
        admin.edit( Ids.DELETE, Ids.PERMISSION, objName: 'ACCT', opName: 'inquiry' )
        admin.edit( Ids.DELETE, Ids.PERMISSION, objName: 'ACCT', opName: 'withdrawal' )
        admin.edit( Ids.DELETE, Ids.PERMOBJ,    objName: 'ACCT', ou: TIds.DEFAULT )
        admin.edit( Ids.DELETE, Ids.ORGUNIT,    name: TIds.DEFAULT, type: 'PERM' )

        admin.edit( Ids.DELETE, Ids.SDSET,          type: 'DYNAMIC', name: 'Bankers' )
        admin.edit( Ids.DISABLE,Ids.ROLECONSTRAINT, id: TIds.WASHER, key: 'locale'  )
        admin.edit( Ids.DELETE, Ids.ROLE,           name: TIds.WASHER)
        admin.edit( Ids.DISABLE,Ids.ROLECONSTRAINT, id: TIds.TELLER, key: 'locale'  )
        admin.edit( Ids.DISABLE,Ids.ROLECONSTRAINT, id: TIds.TELLER, key: 'strength'  )
        admin.edit( Ids.DELETE, Ids.ROLE,           name: TIds.TELLER)

        admin.edit( Ids.DELETE, Ids.USER,       userId: 'huey' )
        admin.edit( Ids.DELETE, Ids.USER,       userId: 'dewey' )
        admin.edit( Ids.DELETE, Ids.USER,       userId: 'louie' )
        admin.edit( Ids.DELETE, Ids.ORGUNIT,    name: TIds.DEFAULT, type: 'USER' )
    }
}
