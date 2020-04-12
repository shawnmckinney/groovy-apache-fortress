package org.apache.directory.fortress.core.groovy

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.directory.fortress.core.AdminMgrFactory
import org.apache.directory.fortress.core.AdminMgr
import org.apache.directory.fortress.core.DelAdminMgr
import org.apache.directory.fortress.core.DelAdminMgrFactory
import org.apache.directory.fortress.core.model.FortEntity
import org.apache.directory.fortress.core.model.OrgUnit
import org.apache.directory.fortress.core.model.PermGrant
import org.apache.directory.fortress.core.model.PermObj
import org.apache.directory.fortress.core.model.Permission
import org.apache.directory.fortress.core.model.Role
import org.apache.directory.fortress.core.model.RoleConstraint
import org.apache.directory.fortress.core.model.SDSet
import org.apache.directory.fortress.core.model.User
import org.apache.directory.fortress.core.model.UserRole
import com.fasterxml.jackson.databind.DeserializationFeature

class GroovyAdminMgr
{
    String reason
    // Root element of the fortress entity model.
    private Class BASE_CLASS = FortEntity.class

    // A property on the model used to map from FortressEntity to the particular subclass, user, role, ...
    private String FQDN = 'fqcn'

    private initialize()
    {
        reason = null
    }

    /**
     * Wrapper for Apache Fortress Admin Manager APIs.
     * @param options Contains map that is bound for fortress entity.
     * @param operation name.  Dependent on entity, usually add or delete, enable is for role constraint.
     * @param entity name, i.e. not fully qualified class name. e.g. user, role, permission, permobj, roleconstraint, etc...
     * @return false if error or exception occurs.  Relies on the apache fortress runtime for validation checks.
     */
    //boolean edit (Map<String, String> options=[:], String operation, String entity )
    //boolean edit (Map<String> options=[:], String operation, String entity )
    boolean edit (Map options=[:], String operation, String entity )
    {
        boolean rc = false
        initialize()
        println "edit-> options:$options"

        try
        {
            switch ( entity.toUpperCase() )
            {
                case 'USER':
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, User.class.getName())
                    User user = get( options, BASE_CLASS )
                    print "user: $user "
                    switch ( operation.toUpperCase() )
                    {
                        case 'ADD':
                            println ' add...'
                            adminMgr.addUser( user )
                            break
                        case 'DELETE':
                            println ' delete...'
                            adminMgr.deleteUser( user )
                            break;
                        default:
                            reason = 'Invalid USER operation'
                            println "***  Error GroovyAdminMgr: $reason"
                            break
                    }
                    break

                case 'ORGUNIT':
                    DelAdminMgr delMgr = DelAdminMgrFactory.createInstance()
                    options.put(FQDN, OrgUnit.class.getName())
                    OrgUnit ou = get( options, BASE_CLASS )
                    print "ou: $ou "
                    switch ( operation.toUpperCase() )
                    {
                        case 'ADD':
                            println ' add...'
                            delMgr.add( ou )
                            break
                        case 'DELETE':
                            println ' delete...'
                            delMgr.delete( ou )
                            break
                        default:
                            reason = 'Invalid ORGUNIT operation'
                            println "***  Error GroovyAdminMgr: $reason"
                            break
                    }
                    break

                case 'ROLE':
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, Role.class.getName())
                    Role role = get( options, BASE_CLASS )
                    print "role: $role "
                    switch ( operation.toUpperCase() )
                    {
                        case 'ADD':
                            println ' add...'
                            adminMgr.addRole( role )
                            break
                        case 'DELETE':
                            println ' delete...'
                            adminMgr.deleteRole( role )
                            break
                        default:
                            reason = 'Invalid ROLE operation'
                            println "***  Error GroovyAdminMgr: $reason"
                            break
                    }
                    break

                case 'SDSET':
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, SDSet.class.getName())
                    SDSet sdset = get( options, BASE_CLASS )
                    print "sdset: $sdset "
                    switch ( operation.toUpperCase() )
                    {
                        case 'ADD':
                            println ' add...'
                            if( sdset.getType() == SDSet.SDType.DYNAMIC)
                                adminMgr.createDsdSet( sdset )
                            else
                                adminMgr.createSsdSet( sdset )
                            break
                        case 'DELETE':
                            println ' delete...'
                        if( sdset.getType() == SDSet.SDType.DYNAMIC)
                            adminMgr.deleteDsdSet( sdset )
                        else
                            adminMgr.deleteSsdSet( sdset )
                        break
                        default:
                            reason = 'Invalid SDSET operation'
                            println "***  Error GroovyAdminMgr: $reason"
                            break
                    }
                    break

                case 'ROLECONSTRAINT':
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, RoleConstraint.class.getName())
                    switch ( operation.toUpperCase() )
                    {
                        case 'ENABLE':
                            RoleConstraint constraint = get( options, BASE_CLASS )
                            print "roleconstraint: $constraint "
                            println ' add...'
                            constraint.setType( RoleConstraint.RCType.USER )
                            adminMgr.enableRoleConstraint( new Role(constraint.getId()), constraint )
                            break
                        case 'DISABLE':
                            RoleConstraint constraint = get( options, BASE_CLASS )
                            print "roleconstraint: $constraint "
                            println ' delete...'
                            constraint.setType( RoleConstraint.RCType.USER )
                            adminMgr.disableRoleConstraint( new Role(constraint.getId()), constraint )
                            break;
                        case 'ADD':
                            RoleConstraint constraint = get( options, BASE_CLASS, true )
                            print "roleconstraint: $constraint "
                            println ' add...'
                            constraint.setType( RoleConstraint.RCType.USER )
                            adminMgr.addRoleConstraint( new UserRole(options.get('userId'), constraint.id ), constraint )
                            break
                        case 'DELETE':
                            RoleConstraint constraint = get( options, BASE_CLASS, true )
                            print "roleconstraint: $constraint "
                            println ' delete...'
                            constraint.setType( RoleConstraint.RCType.USER )
                            adminMgr.removeRoleConstraint( new UserRole(options.get('userId'), constraint.id ), constraint )
                            break;
                        default:
                            reason = 'Invalid ROLECONSTRAINT operation'
                            println "***  Error GroovyAdminMgr: $reason"
                            break
                    }
                    break

                case 'USERROLE':
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, UserRole.class.getName())
                    UserRole uRole = get( options, BASE_CLASS )
                    print "userrole: $uRole "
                    switch ( operation.toUpperCase() )
                    {
                        case 'ADD':
                            println ' add...'
                            adminMgr.assignUser( uRole )
                            break
                        case 'DELETE':
                            println ' delete...'
                            adminMgr.deassignUser( uRole )
                            break
                        default:
                            reason = 'Invalid USERROLE operation'
                            println "***  Error GroovyAdminMgr: $reason"
                            break
                    }
                    break

                case 'PERMISSION':
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, Permission.class.getName())
                    Permission perm = get( options, BASE_CLASS )
                    print "permission: $perm "
                    switch ( operation.toUpperCase() )
                    {
                        case 'ADD':
                            println ' add...'
                            adminMgr.addPermission( perm )
                            break
                        case 'DELETE':
                            println ' delete...'
                            adminMgr.deletePermission( perm )
                            break
                        default:
                            reason = 'Invalid PERMISSION operation'
                            println "***  Error GroovyAdminMgr: $reason"
                            break
                    }
                    break

                case 'PERMOBJ':
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, PermObj.class.getName())
                    PermObj pObj = get( options, BASE_CLASS )
                    print "permobj: $pObj "
                    switch ( operation.toUpperCase() )
                    {
                        case 'ADD':
                            println ' add...'
                            adminMgr.addPermObj( pObj )
                            break
                        case 'DELETE':
                            println ' delete...'
                            adminMgr.deletePermObj( pObj )
                            break
                        default:
                            reason = 'Invalid PERMOBJ operation'
                            println "***  Error GroovyAdminMgr: $reason"
                            break
                    }
                    break
                case 'PERMGRANT':
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, PermGrant.class.getName())
                    PermGrant grant = get( options, BASE_CLASS )
                    print "permgrant: $grant "
                    switch ( operation.toUpperCase() )
                    {
                        case 'ADD':
                            println ' add...'
                            adminMgr.grantPermission( new Permission( grant.objName, grant.opName ), new Role( grant.roleNm ) )
                            break
                        case 'DELETE':
                            println ' delete...'
                            adminMgr.revokePermission( new Permission( grant.objName, grant.opName ), new Role( grant.roleNm ) )
                            break
                        default:
                            reason = 'Invalid PERMGRANT operation'
                            println "***  Error GroovyAdminMgr: $reason"
                            break
                    }
                    break

                default:
                    reason = 'Invalid entity'
                    println "***  Error GroovyAdminMgr: $reason"
                    return false
            }
            rc = true
        }
        catch ( e )
        {
            reason = e.toString()
            println "***  Exception GroovyAdminMgr: $reason"
        }
        return rc
    }

    def end( )
    {
        reason = null
    }

    private def get ( Map<String, String> map, Class cls, boolean ignore=false )
    {
        ObjectMapper mapper
        if ( !ignore )
            mapper = new ObjectMapper()
        else
            mapper = new ObjectMapper().configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper.convertValue(map, cls)
    }
}