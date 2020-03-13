package org.apache.directory.fortress.core.groovy

import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.AccessMgrFactory
import org.apache.directory.fortress.core.model.Permission
import org.apache.directory.fortress.core.model.RoleConstraint
import org.apache.directory.fortress.core.model.Session
import org.apache.directory.fortress.core.model.User;

class GroovyAccessMgr
{
    def createSession = { String userId, String password, String key, String value ->
        AccessMgr accessMgr = AccessMgrFactory.createInstance()
        List<RoleConstraint> constraints = new ArrayList()
        RoleConstraint constraint = new RoleConstraint()
        constraint.setKey( key )
        constraint.setValue( value )
        constraints.add( constraint )
        return accessMgr.createSession( new User ( userId, password ), constraints, false)
    }

    def checkAccess = { Session session, String object, String operation ->
        AccessMgr accessMgr = AccessMgrFactory.createInstance()
        return accessMgr.checkAccess( session, new Permission( object, operation ) )
    }
}