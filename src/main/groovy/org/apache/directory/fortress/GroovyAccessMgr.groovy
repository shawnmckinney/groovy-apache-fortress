package org.apache.directory.fortress

import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.AccessMgrFactory
import org.apache.directory.fortress.core.model.Permission
import org.apache.directory.fortress.core.model.RoleConstraint
import org.apache.directory.fortress.core.model.Session
import org.apache.directory.fortress.core.model.User

class GroovyAccessMgr
{
    Session session
    String reason

    private initialize()
    {
        session = null
        reason = null
    }

    boolean start( Map options=[:] )
    {
        boolean rc = false
        initialize()
        String userId, password=null
        println "start-> userId:$userId password:$password constraints:$options"
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
                println "Key: $entry.key Value: $entry.value"
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


    def end( )
    {
        session = null
        reason = null
    }

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
}