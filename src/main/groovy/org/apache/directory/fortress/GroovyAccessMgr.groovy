package org.apache.directory.fortress

import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.AccessMgrFactory
import org.apache.directory.fortress.core.model.Permission
import org.apache.directory.fortress.core.model.RoleConstraint
import org.apache.directory.fortress.core.model.Session
import org.apache.directory.fortress.core.model.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Wrapper for Apache Fortress AccessMgr.  This class is not thread safe.
 * To begin an rbac session, call the start method.  If successful, the session will be set as an instance variable on this class.
 * Subsequently the canDo method may be called, which will use the session member.  Finally, call the end method to clear out the session and error data.
 */
class GroovyAccessMgr
{
    private static final Logger LOG = LoggerFactory.getLogger( GroovyAccessMgr.class.getName() );
    Session session
    String reason

    private initialize()
    {
        session = null
        reason = null
    }

    /**
     * Apache Fortress createSession wrapper.
     *
     * @param options are key value pairs used during the signon including: userId, password, roles and constraints.
     * The password is optional, if not passed in, will be a 'trusted' session.
     * roles (optional) contains a list of names to be considered for activation. If set, fortress only activates those in the list.
     * Otherwise, will attempt activation all of the roles assigned to the user.
     * Constraints (constraints) are used during the role activation phase, for processing dynamic attributes, e.g. locale.
     * @return boolean true if successful, otherwise false, with reason set.
     */
    boolean start( Map options=[:] )
    {
        boolean rc = false
        initialize()
        String userId, password=null
        LOG.info "start-> options:$options"
        List<RoleConstraint> constraints;
        String[] roles;
        for (entry in options)
        {
            if ( entry.key == 'roles')
            {
                roles = entry.value
            }
            else if ( entry.key == 'userId')
            {
                userId = entry.value
            }
            else if ( entry.key == 'password')
            {
                password = entry.value
            }
            else
            {
                if( constraints == null)
                    constraints = new ArrayList();
                RoleConstraint constraint = new RoleConstraint()
                constraint.setKey( entry.key )
                constraint.setValue( entry.value )
                constraints.add( constraint )
                LOG.info "Key: $entry.key Value: $entry.value"
            }
        }
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance()
            User user
            if( roles != null )
                user = new User( userId, password, roles )
            else
                user = new User( userId, password )

            if ( constraints == null )
                session = accessMgr.createSession( user, (password == null) )
            else
                session = accessMgr.createSession( user, constraints, (password == null) )

            rc = true
        }
        catch ( e )
        {
            reason = e.toString()
        }
        return rc
    }


    /**
     * Maps to the Fortress checkAccess method.  It is passed object, operation and optionally an objId.
     * It will determine if the user has a role activated in their session that has the passed permission.
     *
     * @param object name of resource being checked.  Like an account or customer record.
     * @param operation being performed like add, update, delete, etc.
     * @param objId is the identity of the object, e.g.  Account '123'.
     * @return boolean true if allowed, otherwise false.
     */
    boolean canDo ( String object, String operation, String objId = null )
    {
        boolean rc = false
        if ( session != null )
        {
            try
            {
                AccessMgr accessMgr = AccessMgrFactory.createInstance()
                Permission perm = new Permission(object, operation)
                if( objId != null )
                {
                    perm.objId = objId
                }
                rc = accessMgr.checkAccess( session, perm )
            }
            catch ( e )
            {
                reason = e.toString()
            }
        }
        else
        {
            reason = 'session is null'
        }
        return rc
    }

    /**
     * Determine if the session member variable contains the role being passed in here.
     *
     * @param roleNm required.
     * @return boolean true if role is found, otherwise false.
     */
    boolean inRole ( String roleNm )
    {
        boolean rc = false
        if ( session != null )
        {
            for( String name: session.getRoles() )
            {
                if( name.equalsIgnoreCase( roleNm ) )
                {
                    rc = true
                    break
                }
            }
        }
        else
        {
            reason = 'session is null'
        }
        return rc
    }

    /**
     * Clears out the session and error data.
     *
     * @return
     */
    def end( )
    {
        session = null
        reason = null
    }
}