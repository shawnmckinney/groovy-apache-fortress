package org.apache.directory.fortress

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
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Wrapper for the Apache Fortress AdminMgr.  Not thread safe.
 *
 */
class GroovyAdminMgr
{
    private static final Logger LOG = LoggerFactory.getLogger( GroovyAdminMgr.class.getName() );
    String reason

    /**
     * Wrapper for Apache Fortress Admin Manager APIs.
     * @param options Contains map that is bound for fortress entity.
     * @param operation name.  Dependent on entity, usually add or delete, enable is for role constraint.
     * @param entity name, i.e. not fully qualified class name. e.g. user, role, permission, permobj, roleconstraint, etc...
     * @return false if error or exception occurs.  Relies on the apache fortress runtime for validation checks.
     */
    boolean edit (Map<String,?> options, String operation, String entity )
    {
        boolean rc = false
        initialize()
        LOG.info "$operation: $entity: $options"
        try
        {
            switch ( entity.toLowerCase() )
            {
                case Ids.USER:
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, User.class.getName())
                    User user = get( options )
                    switch ( operation.toLowerCase() )
                    {
                        case Ids.ADD:
                            adminMgr.addUser( user )
                            break
                        case Ids.DELETE:
                            adminMgr.deleteUser( user )
                            break
                        default:
                            reason = "Invalid $entity operation: $operation"
                            break
                    }
                    break

                case Ids.ORGUNIT:
                    DelAdminMgr delMgr = DelAdminMgrFactory.createInstance()
                    options.put(FQDN, OrgUnit.class.getName())
                    OrgUnit ou = get( options )
                    switch ( operation.toLowerCase() )
                    {
                        case Ids.ADD:
                            delMgr.add( ou )
                            break
                        case Ids.DELETE:
                            delMgr.delete( ou )
                            break
                        default:
                            reason = "Invalid $entity operation: $operation"
                            break
                    }
                    break

                case Ids.ROLE:
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, Role.class.getName())
                    Role role = get( options )
                    switch ( operation.toLowerCase() )
                    {
                        case Ids.ADD:
                            adminMgr.addRole( role )
                            break
                        case Ids.DELETE:
                            adminMgr.deleteRole( role )
                            break
                        default:
                            reason = "Invalid $entity operation: $operation"
                            break
                    }
                    break

                case Ids.SDSET:
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, SDSet.class.getName())
                    SDSet sdset = get( options )
                    switch ( operation.toLowerCase() )
                    {
                        case Ids.ADD:
                            if( sdset.getType() == SDSet.SDType.DYNAMIC)
                                adminMgr.createDsdSet( sdset )
                            else
                                adminMgr.createSsdSet( sdset )
                            break
                        case Ids.DELETE:
                            if( sdset.getType() == SDSet.SDType.DYNAMIC)
                                adminMgr.deleteDsdSet( sdset )
                            else
                                adminMgr.deleteSsdSet( sdset )
                            break
                        default:
                            reason = "Invalid $entity operation: $operation"
                            break
                    }
                    break

                case Ids.ROLECONSTRAINT:
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, RoleConstraint.class.getName())
                    switch ( operation.toLowerCase() )
                    {
                        case Ids.ENABLE:
                            RoleConstraint constraint = get( options )
                            constraint.setType( RoleConstraint.RCType.USER )
                            adminMgr.enableRoleConstraint( new Role(constraint.getId()), constraint )
                            break
                        case Ids.DISABLE:
                            RoleConstraint constraint = get( options )
                            constraint.setType( RoleConstraint.RCType.USER )
                            adminMgr.disableRoleConstraint( new Role(constraint.getId()), constraint )
                            break
                        case Ids.ADD:
                            RoleConstraint constraint = get( options, true )
                            constraint.setType( RoleConstraint.RCType.USER )
                            adminMgr.addRoleConstraint( new UserRole(options.get('userId'), constraint.id ), constraint )
                            break
                        case Ids.DELETE:
                            RoleConstraint constraint = get( options, true )
                            constraint.setType( RoleConstraint.RCType.USER )
                            adminMgr.removeRoleConstraint( new UserRole(options.get('userId'), constraint.id ), constraint )
                            break
                        default:
                            reason = "Invalid $entity operation: $operation"
                            break
                    }
                    break

                case Ids.USERROLE:
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, UserRole.class.getName())
                    UserRole uRole = get( options )
                    switch ( operation.toLowerCase() )
                    {
                        case Ids.ADD:
                            adminMgr.assignUser( uRole )
                            break
                        case Ids.DELETE:
                            adminMgr.deassignUser( uRole )
                            break
                        default:
                            reason = "Invalid $entity operation: $operation"
                            break
                    }
                    break

                case Ids.PERMISSION:
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, Permission.class.getName())
                    Permission perm = get( options )
                    switch ( operation.toLowerCase() )
                    {
                        case Ids.ADD:
                            adminMgr.addPermission( perm )
                            break
                        case Ids.DELETE:
                            adminMgr.deletePermission( perm )
                            break
                        default:
                            reason = "Invalid $entity operation: $operation"
                            break
                    }
                    break

                case Ids.PERMOBJ:
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, PermObj.class.getName())
                    PermObj pObj = get( options )
                    switch ( operation.toLowerCase() )
                    {
                        case Ids.ADD:
                            adminMgr.addPermObj( pObj )
                            break
                        case Ids.DELETE:
                            adminMgr.deletePermObj( pObj )
                            break
                        default:
                            reason = "Invalid $entity operation: $operation"
                            break
                    }
                    break
                case Ids.PERMGRANT:
                    AdminMgr adminMgr = AdminMgrFactory.createInstance()
                    options.put(FQDN, PermGrant.class.getName())
                    PermGrant grant = get( options )
                    switch ( operation.toLowerCase() )
                    {
                        case Ids.ADD:
                            adminMgr.grantPermission( new Permission( grant.objName, grant.opName ), new Role( grant.roleNm ) )
                            break
                        case Ids.DELETE:
                            adminMgr.revokePermission( new Permission( grant.objName, grant.opName ), new Role( grant.roleNm ) )
                            break
                        default:
                            reason = "Invalid $entity operation: $operation"
                            break
                    }
                    break

                default:
                    reason = "Invalid entity: $entity"
                    break;
            }
            if ( reason == null )
                rc = true
        }
        catch ( e )
        {
            reason = e.toString()
        }
        if ( !rc )
            LOG.warn( reason )
        return rc
    }

    def end( )
    {
        reason = null
    }

    /**
     * Use Jackson to map->Fortress Entity.
     *
     * @param map directly corresponds with Fortress entity model.
     * @param ignore set to true to ignore unknown property names.
     * @return subclass for FortEntity
     */
    private def get ( Map<String, ?> map, boolean ignore=false )
    {
        ObjectMapper mapper
        if ( !ignore )
            mapper = new ObjectMapper()
        else
            mapper = new ObjectMapper().configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper.convertValue(map, FortEntity.class)
    }

    // A property on the model used to map from FortressEntity to the particular subclass, user, role, ...
    private String FQDN = 'fqcn'

    private initialize()
    {
        reason = null
    }
}