package org.apache.directory.fortress.core.groovy

import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.AccessMgrFactory
import org.apache.directory.fortress.core.model.Permission
import org.apache.directory.fortress.core.model.RoleConstraint
import org.apache.directory.fortress.core.model.Session
import org.apache.directory.fortress.core.model.User;

class GroovyAccessMgr
{
    def Session createSession ( String userId, String password, String key = null, String value = null )
    {
        AccessMgr accessMgr = AccessMgrFactory.createInstance()
        User user = new User( userId, password)
        Session session
        if( key != null && value != null)
        {
            List<RoleConstraint> constraints = new ArrayList()
            RoleConstraint constraint = new RoleConstraint()
            constraint.setKey( key )
            constraint.setValue( value )
            constraints.add( constraint )
            accessMgr.createSession( user, constraints, false)
        }
        else
        {
            accessMgr.createSession( user, false)
        }
    }

    def boolean checkAccess ( Session session, String object, String operation, String objId = null )
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