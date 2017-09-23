import hudson.*
import hudson.security.*
import jenkins.*
import jenkins.model.*
import java.util.*
import java.lang.reflect.*
import java.util.logging.*
import groovy.json.*
import com.michelin.cio.hudson.plugins.rolestrategy.*

def env = System.getenv()


/**************/
/*   Roles    */
/**************/

def globalRoleRead = "read"
def globalBuildRole = "build"
def globalRoleAdmin = "admin"

/*******************/
/* User and Groups */
/*******************/

def access = [
  admins: ["admin"],
  builders: ["admin"],
  readers: ["anonymous"]

]

/***************/
/* Permissions */
/***************/

def adminPermissions = [
"hudson.model.Hudson.Administer",
"hudson.model.Hudson.Read"

]

def readPermissions = [
"hudson.model.Hudson.Read",
"hudson.model.Item.Discover",
"hudson.model.Item.Read"

]

def buildPermissions = [
"hudson.model.Hudson.Read",
"hudson.model.Item.Build",
"hudson.model.Item.Cancel",
"hudson.model.Item.Read",
"hudson.model.Run.Replay"

]


def roleBasedAuthenticationStrategy = new RoleBasedAuthorizationStrategy()
Jenkins.instance.setAuthorizationStrategy(roleBasedAuthenticationStrategy)

Constructor[] constrs = Role.class.getConstructors()
for (Constructor<?> c : constrs) {
  c.setAccessible(true)
}

// Make the method assignRole accessible
Method assignRoleMethod = RoleBasedAuthorizationStrategy.class.getDeclaredMethod("assignRole", String.class, Role.class, String.class)
assignRoleMethod.setAccessible(true)

Set<Permission> adminPermissionSet = new HashSet<Permission>()
adminPermissions.each { p ->
  def permission = Permission.fromId(p)
  if (permission != null) {
    adminPermissionSet.add(permission)
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> buildPermissionSet = new HashSet<Permission>()
buildPermissions.each { p ->
  def permission = Permission.fromId(p)
  if (permission != null) {
    buildPermissionSet.add(permission)
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> readPermissionSet = new HashSet<Permission>()
readPermissions.each { p ->
  def permission = Permission.fromId(p)
  if (permission != null) {
    readPermissionSet.add(permission)
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

/************************/
/* Permissions -> Roles */
/************************/

// admins
Role adminRole = new Role(globalRoleAdmin, adminPermissionSet)
roleBasedAuthenticationStrategy.addRole(RoleBasedAuthorizationStrategy.GLOBAL, adminRole)

// builders
Role buildersRole = new Role(globalRoleAdmin, buildPermissionSet)
roleBasedAuthenticationStrategy.addRole(RoleBasedAuthorizationStrategy.GLOBAL, buildersRole)

// anonymous read
Role readRole = new Role(globalRoleRead, readPermissionSet)
roleBasedAuthenticationStrategy.addRole(RoleBasedAuthorizationStrategy.GLOBAL, readRole)

/************************/
/* Roles -> Group/Users */
/************************/

access.admins.each { l ->
  println("Granting admin to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleBasedAuthorizationStrategy.GLOBAL, adminRole, l)
}

access.builders.each { l ->
  println("Granting builder to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleBasedAuthorizationStrategy.GLOBAL, buildersRole, l)
}

access.readers.each { l ->
  println("Granting read to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleBasedAuthorizationStrategy.GLOBAL, readRole, l)
}

Jenkins.instance.save()

