package org.apache.directory.fortress.core.groovy

import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.AccessMgrFactory
import org.apache.directory.fortress.core.model.Permission
import org.apache.directory.fortress.core.model.RoleConstraint
import org.apache.directory.fortress.core.model.Session
import org.apache.directory.fortress.core.model.User
import org.apache.tools.ant.types.selectors.SelectSelector;

class GroovyAccessMgr
{
    Session session
    String reason

    boolean createSession ( String userId, String password, String key = null, String value = null )
    {
        boolean rc = false
        println('create session 1')
        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance()
            User user = new User( userId, password)
            if( key != null && value != null)
            {
                List<RoleConstraint> constraints = new ArrayList()
                RoleConstraint constraint = new RoleConstraint()
                constraint.setKey( key )
                constraint.setValue( value )
                constraints.add( constraint )
                session = accessMgr.createSession( user, constraints, false)
            }
            else
            {
                session = accessMgr.createSession( user, false)
            }
            rc = true
        }
        catch (org.apache.directory.fortress.core.SecurityException se)
        {
            reason = se.message
        }
        return rc
    }

    def createSession2 = { String userId, String password, String key = null, String value = null ->
        boolean rc = false
        println('create session 2')

        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance()
            User user = new User( userId, password)
            if( key != null && value != null)
            {
                List<RoleConstraint> constraints = new ArrayList()
                RoleConstraint constraint = new RoleConstraint()
                constraint.setKey( key )
                constraint.setValue( value )
                constraints.add( constraint )
                session = accessMgr.createSession( user, constraints, false)
            }
            else
            {
                session = accessMgr.createSession( user, false)
            }
            rc = true
        }
        catch (org.apache.directory.fortress.core.SecurityException se)
        {
            reason = se.message
        }
        return rc
    }

    boolean checkAccess ( String object, String operation, String objId = null )
    {
        AccessMgr accessMgr = AccessMgrFactory.createInstance()
        Permission perm = new Permission(object, operation)
        if( objId != null )
        {
            perm.objId = objId
        }
        accessMgr.checkAccess( session, perm )
    }
}