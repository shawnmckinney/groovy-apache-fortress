package org.apache.directory.fortress.core.groovy

import org.apache.directory.fortress.core.AccessMgr;
import org.apache.directory.fortress.core.AccessMgrFactory
import org.apache.directory.fortress.core.SecurityException
import org.apache.directory.fortress.core.model.Permission
import org.apache.directory.fortress.core.model.RoleConstraint
import org.apache.directory.fortress.core.model.Session
import org.apache.directory.fortress.core.model.User
import org.apache.directory.fortress.core.model.UserRole

class GroovyAccessMgr
{
    Session session
    String reason

    private initialize()
    {
        session = null
        reason = null
    }

    boolean start(String userId, String password, String key = null, String value = null )
    {
        boolean rc = false
        initialize()
        println( "Start $userId at $key $value.")

        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance()
            User user = new User( userId, password)
            if( key != null && value != null )
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
        catch ( e )
        {
            reason = e.toString()
        }
        return rc
    }

    //public def start3 = { String userId, String password = null, Object ... args ->

    public def start3 = { String userId, String password = null, String key = null, String value = null ->
        boolean rc = false
        initialize()

        //def key, value = null;

        List<RoleConstraint> constraints = new ArrayList()
        //if ( $args[0] != null && $args[1] != null )
        if ( key != null && value != null )
        {
            RoleConstraint constraint = new RoleConstraint()
            constraint.setKey( key )
            constraint.setValue( value )
            constraints.add( constraint )
        }
        println( "Start3 $userId at $key $value.")

        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance()
            User user = new User( userId, password)
            session = accessMgr.createSession( user, constraints, (password == null) )
            //session = accessMgr.createSession( user, constraints, false )
            rc = true
        }
        catch ( e )
        {
            reason = e.toString()
        }
        return rc
    }

    public def start2 = { String userId, String password, String key = null, String value = null ->
        boolean rc = false
        initialize()
        println( "Start2 $userId at $key $value.")

        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance()
            User user = new User( userId, password)
            if( key != null && value != null )
            {
                List<RoleConstraint> constraints = new ArrayList()
                RoleConstraint constraint = new RoleConstraint()
                constraint.setKey( key )
                constraint.setValue( value )
                constraints.add( constraint )
                session = accessMgr.createSession( user, constraints, false )
            }
            else
            {
                session = accessMgr.createSession( user, false )
            }
            rc = true
        }
        catch ( e )
        {
            reason = e.toString()
        }
        return rc
    }

    public def startx = { String userId, String password = null, String key = null, String value = null ->
        boolean rc = false
        initialize()

        //def key, value = null;

        List<RoleConstraint> constraints = new ArrayList()
        //if ( $args[0] != null && $args[1] != null )
        if ( key != null && value != null )
        {
            RoleConstraint constraint = new RoleConstraint()
            constraint.setKey( key )
            constraint.setValue( value )
            constraints.add( constraint )
        }
        println( "Start3 $userId at $key $value.")

        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance()
            User user = new User( userId, password)
            session = accessMgr.createSession( user, constraints, (password == null) )
            //session = accessMgr.createSession( user, constraints, false )
            rc = true
        }
        catch ( e )
        {
            reason = e.toString()
        }
        return rc
    }

    boolean start2( Map options=[:], String userId, String password=null )
    //boolean start2( String userId, String password=null )
    {
        boolean rc = true
        println("***********  userid: $userId password: $password ")
        return rc
    }

    boolean start( Map options=[:], String userId, String password=null )
    {
        boolean rc = false
        initialize()
        println "start-> userId:$userId password:$password constraints:$options"
        List<RoleConstraint> constraints;
        String[] roles;
        for (entry in options)
        {
            if ( entry.key == 'roles')
            {
                roles = entry.value
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
            session = accessMgr.createSession( user, constraints, (password == null) )
            rc = true
        }
        catch ( e )
        {
            reason = e.toString()
        }
        return rc
    }
    // assert volume({ l, b -> return l*b }, 12, 6, 10) == 720

    public def start4 = { String userId, String... args ->
        boolean rc = false
        initialize()

        def password = args[0]

        List<RoleConstraint> constraints
        //for ( String val : args[3] )
        constraints = new ArrayList()
        for ( int i = 1; i < args.length; i+=2 )
        {
            RoleConstraint constraint = new RoleConstraint()
            constraint.setKey( args[i] )
            constraint.setValue( args[i+1] )
            constraints.add( constraint )
            println( "***************  key=" + constraint.getKey() + ", value=" + constraint.getValue() )
        }

/*
        if ( key != null && value != null )
        {
            constraints = new ArrayList()
            RoleConstraint constraint = new RoleConstraint()
            constraint.setKey( key )
            constraint.setValue( value )
            constraints.add( constraint )
        }
*/

        println( "Start3 $userId.")
        //println( "Start3 $userId at $key $value.")

        try
        {
            AccessMgr accessMgr = AccessMgrFactory.createInstance()
            User user = new User( userId, password)
            session = accessMgr.createSession( user, constraints, (password == null) )
            //session = accessMgr.createSession( user, constraints, false )
            rc = true
        }
        catch ( e )
        {
            reason = e.toString()
        }
        return rc
    }

    boolean isOk(String object, String operation, String objId = null )
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

    def end( )
    {
        session = null
        reason = null
    }


    public def canDo = { String object, String operation, String objId = null ->

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


}